// Renders film/film.html into a picture or an MP4.
//   node scripts/render_film.mjs still 24.8 61 97      one PNG per second listed  -> out/film_<sec>.png
//   node scripts/render_film.mjs draft                 960x540 MP4, whole film      -> out/film_draft.mp4
//   node scripts/render_film.mjs draft 0 28            only seconds 0 to 28
//   node scripts/render_film.mjs final                 1920x1080 MP4                -> out/<TeamCode>_2-Minute_Pitch.mp4
// On a network that blocks Remotion's own browser download, set PITCH_BROWSER to the path of chrome.exe first.
import { bundle } from "@remotion/bundler";
import { renderMedia, renderStill, selectComposition } from "@remotion/renderer";
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = path.join(path.dirname(fileURLToPath(import.meta.url)), "..");
const film = path.join(root, "..", "film"), pub = path.join(root, "public"), out = path.join(root, "out");
const [mode = "still", ...rest] = process.argv.slice(2);
fs.rmSync(path.join(pub, "film"), { recursive: true, force: true });
fs.cpSync(film, path.join(pub, "film"), { recursive: true, filter: (f) => !/[\\/]tools([\\/]|$)|\.md$|[\\/]_/.test(f) });
fs.mkdirSync(out, { recursive: true });

const browserExecutable = process.env.PITCH_BROWSER || undefined;
const chromeMode = browserExecutable && !/headless[-_]shell/i.test(browserExecutable) ? "chrome-for-testing" : undefined;
console.log("Bundling...");
const serveUrl = await bundle({ entryPoint: path.join(root, "src", "film", "index.jsx"), publicDir: pub });
const composition = await selectComposition({ serveUrl, id: "Film", browserExecutable, chromeMode });
const fps = composition.fps, common = { composition, serveUrl, browserExecutable, chromeMode, timeoutInMilliseconds: 180000 };

if (mode === "still") {
  for (const s of rest.length ? rest.map(Number) : [10]) {
    const file = path.join(out, `film_${s}.png`);
    await renderStill({ ...common, output: file, frame: Math.min(composition.durationInFrames - 1, Math.round(s * fps)), scale: 0.5 });
    console.log("  " + path.relative(root, file));
  }
} else {
  const final = mode === "final";
  const from = rest[0] ? Math.round(Number(rest[0]) * fps) : 0, to = rest[1] ? Math.round(Number(rest[1]) * fps) - 1 : composition.durationInFrames - 1;
  const code = (() => { try { return JSON.parse(fs.readFileSync(path.join(root, "..", "content.json"), "utf8")).header.team_code; } catch { return null; } })() || "G56";
  const file = path.join(out, final ? `${code}_2-Minute_Pitch.mp4` : "film_draft.mp4");
  let last = -1; const t0 = Date.now();
  await renderMedia({ ...common, codec: "h264", outputLocation: file, scale: final ? 1 : 0.5, crf: final ? 18 : 26, frameRange: [from, to],
    onProgress: ({ progress }) => { const p = Math.floor(progress * 10); if (p !== last) { last = p; console.log(`  ${p * 10}%  (${Math.round((Date.now() - t0) / 1000)} s)`); } } });
  console.log(`${final ? "Final" : "Draft"} written: ${path.relative(root, file)}  (${((to - from + 1) / fps).toFixed(1)} s of film)`);
}
