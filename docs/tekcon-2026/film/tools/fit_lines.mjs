// After new voice files: keep every line at its planned second, but push a line later if the one before it
// now runs into it. Prints where each beat ends against the next beat's start.   node tools/fit_lines.mjs [--write]
import fs from "fs"; import path from "path"; import { fileURLToPath } from "url";
const F = path.dirname(path.dirname(fileURLToPath(import.meta.url)));
const dur = f => { const b = fs.readFileSync(f); let o = 12, br; while (o < b.length) { const id = b.toString("ascii", o, o + 4), n = b.readUInt32LE(o + 4); if (id === "fmt ") br = b.readUInt32LE(o + 16); if (id === "data") return n / br; o += 8 + n + (n & 1); } };
const BEAT = { 1: 0, 2: 20, 3: 40, 4: 68.75, 5: 100, 6: 120 }, GAP = 0.2;
const lines = JSON.parse(fs.readFileSync(F + "/lines.json"));
let end = 0;
lines.forEach((l, i) => {
  const f = `${F}/voice/${l.id}.wav`, d = fs.existsSync(f) ? dur(f) : 0.9;
  const start = Math.max(l.start, end + (i ? GAP : 0)); if (Math.abs(start - l.start) > .005) console.log(`${l.id}: moved ${l.start} -> ${start.toFixed(2)}`);
  l.start = +start.toFixed(2); end = l.start + d;
  const next = lines[i + 1];
  if (!next || next.beat !== l.beat) console.log(`Beat ${l.beat}: voice ends ${end.toFixed(2)}, next beat at ${BEAT[l.beat + 1]}${end > BEAT[l.beat + 1] - .1 ? "   <-- RUNS OVER" : ""}`);
});
if (process.argv.includes("--write")) fs.writeFileSync(F + "/lines.json", JSON.stringify(lines, null, 1) + "\n");
