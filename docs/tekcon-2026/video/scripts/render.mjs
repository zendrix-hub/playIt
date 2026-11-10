// Renders the pitch video.   node scripts/render.mjs stills | draft | final   [--shots=3,12]
//   stills : one PNG per shot into out/stills/  (look at these before rendering video)
//   draft  : half-size MP4 for quick review     -> out/draft.mp4
//   final  : 1920x1080 MP4                      -> out/<TeamCode>_2-Minute_Pitch.mp4
import { bundle } from "@remotion/bundler";
import { renderMedia, renderStill, selectComposition } from "@remotion/renderer";
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const here = path.dirname(fileURLToPath(import.meta.url));
const root = path.join(here, "..");
const mode = process.argv[2] || "stills";
const only = (process.argv.find((a) => a.startsWith("--shots=")) || "").replace("--shots=", "").split(",").filter(Boolean).map(Number);
const data = JSON.parse(fs.readFileSync(path.join(root, "src", "generated", "data.json"), "utf8"));
const out = path.join(root, "out");
fs.mkdirSync(path.join(out, "stills"), { recursive: true });

// On a network that blocks Remotion's browser download, point PITCH_BROWSER at any Chrome or
// chrome-headless-shell executable and it is used instead.
const browserExecutable = process.env.PITCH_BROWSER || undefined;
// A full Chrome needs the newer headless mode; a headless-shell build uses the default.
const chromeMode = browserExecutable && !/headless[-_]shell/i.test(browserExecutable) ? "chrome-for-testing" : undefined;
if (browserExecutable) console.log("Using browser: " + browserExecutable);
console.log("Bundling...");
const serveUrl = await bundle({ entryPoint: path.join(root, "src", "index.jsx"), publicDir: path.join(root, "public") });
const composition = await selectComposition({ serveUrl, id: "Pitch", browserExecutable, chromeMode });
const fps = composition.fps;

if (mode === "stills") {
  const shots = data.timeline.shots.filter((s) => !only.length || only.includes(s.id));
  for (const s of shots) {
    const frame = Math.min(composition.durationInFrames - 1, Math.round((s.start + (s.end - s.start) * 0.8) * fps));
    const file = path.join(out, "stills", `shot${String(s.id).padStart(2, "0")}.png`);
    await renderStill({ composition, serveUrl, output: file, frame, scale: 0.5, browserExecutable, chromeMode });
    console.log(`  shot ${s.id} (${s.type}) at ${(frame / fps).toFixed(1)}s -> ${path.relative(root, file)}`);
  }
  console.log(`Stills written: ${shots.length}. Look at them before rendering video.`);
} else {
  const final = mode === "final";
  const file = path.join(out, final ? `${data.teamCode}_2-Minute_Pitch.mp4` : "draft.mp4");
  let last = -1;
  await renderMedia({
    composition, serveUrl, codec: "h264", outputLocation: file, browserExecutable, chromeMode, scale: final ? 1 : 0.5, crf: final ? 18 : 28,
    onProgress: ({ progress }) => { const p = Math.floor(progress * 10); if (p !== last) { last = p; console.log(`  ${p * 10}%`); } },
  });
  console.log(`${final ? "Final" : "Draft"} video written: ${path.relative(root, file)}  (${(composition.durationInFrames / fps).toFixed(1)}s)`);
}
