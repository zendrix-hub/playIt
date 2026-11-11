// Finds ffmpeg / ffprobe: the system ones if installed, otherwise the copies bundled with Remotion.
const { spawnSync } = require("child_process");
const path = require("path");
const fs = require("fs");
const VIDEO = path.join(__dirname, "..", "video");

const tryRun = (cmd, args, opts = {}) => spawnSync(cmd, args, { encoding: "utf8", shell: process.platform === "win32", ...opts });
// A full ffmpeg installed by pip (imageio-ffmpeg) into tools/voice/.venv, if there is one.
// Remotion's own copy is cut down: it cannot scale, change frame rate or measure loudness.
const pipFfmpeg = () => {
  const dir = path.join(__dirname, "voice", ".venv", "Lib", "site-packages", "imageio_ffmpeg", "binaries");
  try { const f = fs.readdirSync(dir).find((x) => /^ffmpeg.*\.exe$/i.test(x)); return f ? path.join(dir, f) : null; } catch (e) { return null; }
};
const find = (name) => {
  if (name === "ffmpeg") {
    const f = pipFfmpeg();
    if (f && spawnSync(f, ["-version"], { encoding: "utf8" }).status === 0) return { cmd: f, pre: [], cwd: undefined, via: "imageio-ffmpeg", direct: true };
  }
  if (tryRun(name, ["-version"]).status === 0) return { cmd: name, pre: [], cwd: undefined, via: "system" };
  // Remotion's copy, called directly (going through npx costs seconds per file)
  const own = path.join(VIDEO, "node_modules", "@remotion", "compositor-win32-x64-msvc", name + ".exe");
  if (fs.existsSync(own) && spawnSync(own, ["-version"], { encoding: "utf8" }).status === 0) return { cmd: own, pre: [], cwd: undefined, via: "remotion", direct: true };
  const r = tryRun("npx", ["--no-install", "remotion", name, "-version"], { cwd: VIDEO });
  if (r.status === 0) return { cmd: "npx", pre: ["--no-install", "remotion", name], cwd: VIDEO, via: "remotion" };
  return null;
};
let cache = {};
const tool = (name) => (name in cache ? cache[name] : (cache[name] = find(name)));
const run = (name, args) => {
  const t = tool(name);
  if (!t) return { status: 1, stdout: "", stderr: `${name} not found` };
  if (t.direct) return spawnSync(t.cmd, args, { encoding: "utf8" });
  return tryRun(t.cmd, [...t.pre, ...args], { cwd: t.cwd });
};
// Length of a media file in seconds, or null if it cannot be read.
const seconds = (file) => {
  const r = run("ffprobe", ["-v", "error", "-show_entries", "format=duration", "-of", "default=nw=1:nk=1", file]);
  const v = parseFloat((r.stdout || "").trim().split(/\s+/).pop());
  return r.status === 0 && isFinite(v) ? v : null;
};
module.exports = { tool, run, seconds, VIDEO, pipFfmpeg };
