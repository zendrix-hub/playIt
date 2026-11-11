// Builds film/data.js (and data.json) from: lines.json (the script and when each line starts), the voice files,
// and ../round2_data.json (the survey numbers).   Run:  node film/tools/make_data.mjs
import fs from "node:fs"; import path from "node:path"; import { fileURLToPath } from "node:url";
const F = path.join(path.dirname(fileURLToPath(import.meta.url)), ".."), KIT = path.join(F, "..");
const lines = JSON.parse(fs.readFileSync(path.join(F, "lines.json"), "utf8"));
const R = JSON.parse(fs.readFileSync(path.join(KIT, "round2_data.json"), "utf8"));
const C = JSON.parse(fs.readFileSync(path.join(KIT, "content.json"), "utf8"));

// ---- read a WAV: length, and where the silences are ----
function wav(file) {
  const b = fs.readFileSync(file); let p = 12, fmt = null, data = null;
  while (p + 8 <= b.length) { const id = b.toString("ascii", p, p + 4), n = b.readUInt32LE(p + 4); if (id === "fmt ") fmt = { tag: b.readUInt16LE(p + 8), ch: b.readUInt16LE(p + 10), rate: b.readUInt32LE(p + 12), bits: b.readUInt16LE(p + 22) }; if (id === "data") { data = b.subarray(p + 8, Math.min(b.length, p + 8 + n)); break; } p += 8 + n + (n % 2); }
  const bytes = fmt.bits / 8, n = Math.floor(data.length / (bytes * fmt.ch)), s = new Float32Array(n);
  for (let i = 0; i < n; i++) { const o = i * bytes * fmt.ch; s[i] = fmt.tag === 3 ? data.readFloatLE(o) : fmt.bits === 16 ? data.readInt16LE(o) / 32768 : fmt.bits === 32 ? data.readInt32LE(o) / 2147483648 : (data.readUInt8(o) - 128) / 128; }
  const win = Math.round(fmt.rate * 0.02), rms = []; for (let i = 0; i + win <= n; i += win) { let a = 0; for (let j = 0; j < win; j++) a += s[i + j] * s[i + j]; rms.push(Math.sqrt(a / win)); }
  const peak = Math.max(...rms), quiet = rms.map(v => v < peak * 0.06), gaps = []; let st = -1;
  quiet.forEach((q, i) => { if (q && st < 0) st = i; if ((!q || i === quiet.length - 1) && st >= 0) { const e = q ? i + 1 : i; if ((e - st) * 0.02 >= 0.14) gaps.push([st * 0.02, e * 0.02]); st = -1; } });
  const dur = n / fmt.rate, first = quiet.findIndex(q => !q) * 0.02, last = (quiet.length - [...quiet].reverse().findIndex(q => !q)) * 0.02;
  return { dur, first: Math.max(0, first), last: Math.min(dur, last), gaps: gaps.filter(g => g[0] > first + 0.1 && g[1] < last - 0.1) };
}
const syl = w => { const t = w.replace(/[^a-z0-9.]/gi, ""); if (/^\d/.test(t)) return ({ "30": 2, "16": 2, "15": 2, "12": 1, "4.5": 3, "4.8": 3, "4.7": 3, "691": 6, "1": 1, "56": 3 })[t.replace(/\.$/, "")] || t.replace(/\./g, "").length + 1; return Math.max(1, (t.toLowerCase().replace(/e$/, "").match(/[aeiouy]+/g) || []).length); };

const voice = lines.map(l => {
  const file = path.join(F, "voice", l.id + ".wav"), has = fs.existsSync(file), script = l.text.split(/\s+/);
  if (!has) return { ...l, end: l.start + 0.9, file: null, words: script.map((w, i) => ({ w, s: l.start + i * 0.3, e: l.start + (i + 1) * 0.3 })), timing: "no audio" };
  const a = wav(file); let words = null, timing = "heard";
  const wj = path.join(F, "voice", l.id + ".words.json");
  if (fs.existsSync(wj)) { const w = JSON.parse(fs.readFileSync(wj, "utf8")), d = w.map(x => x.end - x.start), even = Math.max(...d) - Math.min(...d) < 0.03;
    if (!even && w.length === script.length) words = w.map((x, i) => ({ w: script[i], s: x.start, e: x.end })); }
  if (!words) {            // no trustworthy word times: put each sentence between the pauses, then share it out by syllables
    timing = "pauses";
    const sents = []; let cur = []; script.forEach(w => { cur.push(w); if (/[.?!:]["”']?$/.test(w)) { sents.push(cur); cur = []; } }); if (cur.length) sents.push(cur);
    const cuts = a.gaps.slice().sort((x, y) => (y[1] - y[0]) - (x[1] - x[0])).slice(0, sents.length - 1).sort((x, y) => x[0] - y[0]);
    let spans = []; let t = a.first; cuts.forEach(g => { spans.push([t, g[0]]); t = g[1]; }); spans.push([t, a.last]);
    if (spans.length !== sents.length) { spans = [[a.first, a.last]]; sents.splice(0, sents.length, script); timing = "spread"; }
    words = []; sents.forEach((sw, k) => { const tot = sw.reduce((n, w) => n + syl(w), 0), [s0, s1] = spans[k]; let acc = 0; sw.forEach(w => { const s = s0 + (s1 - s0) * acc / tot; acc += syl(w); words.push({ w, s, e: s0 + (s1 - s0) * acc / tot }); }); });
  }
  return { ...l, end: +(l.start + a.dur).toFixed(2), file: "voice/" + l.id + ".wav", timing, words: words.map(x => ({ w: x.w, s: +(l.start + x.s).toFixed(2), e: +(l.start + x.e).toFixed(2) })) };
});
const data = {
  duration: 120, fps: 30, projected: R.status !== "actual",
  beats: [0, 20, 40, 70, 100, 120], voice,
  numbers: { total: R.participants_total, children: R.groups.children, parents: R.groups.parents, teachers: R.groups.teachers,
    happy: R.children.happy_face, sayit: R.children.sayit_first_try, paUseful: R.parents.usefulness_mean, paOffline: R.parents.offline_benefit_mean, teOverall: R.teachers.overall_mean,
    tests: R.actual_figures_not_from_round2.unit_tests_passed },
  team: { members: C.team.members, adviser: C.team.adviser },
};
fs.writeFileSync(path.join(F, "data.json"), JSON.stringify(data, null, 1));
fs.writeFileSync(path.join(F, "data.js"), "// Written by tools/make_data.mjs. Do not edit.\nwindow.FILM_DATA = " + JSON.stringify(data) + ";\n");
for (const v of voice) console.log(v.id.padEnd(6), v.start.toFixed(2).padStart(7), v.end.toFixed(2).padStart(7), (v.timing || "").padEnd(8), v.words.map(x => x.w + "@" + x.s.toFixed(1)).join(" ").slice(0, 150));
