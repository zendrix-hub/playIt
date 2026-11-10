// Shared helpers: load the two source files and resolve {path} placeholders.
const fs = require("fs");
const path = require("path");
const ROOT = path.join(__dirname, "..");
const readJson = (f) => JSON.parse(fs.readFileSync(path.join(ROOT, f), "utf8"));
const data = readJson("round2_data.json");
const content = readJson("content.json");
const projected = data.status !== "actual";

const get = (p) => {
  const v = p.split(".").reduce((o, k) => (o == null ? undefined : o[k]), data);
  if (v === undefined) throw new Error(`round2_data.json has no value at "${p}"`);
  return v;
};
// Ratings always show one decimal (4.0 not 4).
const show = (p) => { const v = get(p); return typeof v === "number" && /_mean$/.test(p) ? v.toFixed(1) : String(v); };
const fill = (s) => String(s).replace(/\{([\w.\-]+)\}/g, (_, p) => show(p));

module.exports = { ROOT, data, content, projected, get, show, fill, fs, path };
