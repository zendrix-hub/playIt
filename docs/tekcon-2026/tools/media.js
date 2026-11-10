// Finds ffmpeg / ffprobe: the system ones if installed, otherwise the copies bundled with Remotion.
const { spawnSync } = require("child_process");
const path = require("path");
const VIDEO = path.join(__dirname, "..", "video");

const tryRun = (cmd, args, opts = {}) => spawnSync(cmd, args, { encoding: "utf8", shell: process.platform === "win32", ...opts });
const find = (name) => {
  if (tryRun(name, ["-version"]).status === 0) return { cmd: name, pre: [], cwd: undefined, via: "system" };
  const r = tryRun("npx", ["--no-install", "remotion", name, "-version"], { cwd: VIDEO });
  if (r.status === 0) return { cmd: "npx", pre: ["--no-install", "remotion", name], cwd: VIDEO, via: "remotion" };
  return null;
};
let cache = {};
const tool = (name) => (name in cache ? cache[name] : (cache[name] = find(name)));
const run = (name, args) => {
  const t = tool(name);
  if (!t) return { status: 1, stdout: "", stderr: `${name} not found` };
  return tryRun(t.cmd, [...t.pre, ...args], { cwd: t.cwd });
};
// Length of a media file in seconds, or null if it cannot be read.
const seconds = (file) => {
  const r = run("ffprobe", ["-v", "error", "-show_entries", "format=duration", "-of", "default=nw=1:nk=1", file]);
  const v = parseFloat((r.stdout || "").trim().split(/\s+/).pop());
  return r.status === 0 && isFinite(v) ? v : null;
};
module.exports = { tool, run, seconds, VIDEO };
