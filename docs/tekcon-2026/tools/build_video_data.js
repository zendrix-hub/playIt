// Gathers everything the video needs into video/src/generated/data.json:
// the timeline, the narration lines with their timing, captions, the survey numbers,
// and the list of media files that actually exist. Run after any change.
const { ROOT, data, content: c, projected, get, fill, fs, path } = require("./lib");
const { build } = require("./build_pitch");
const { seconds, VIDEO } = require("./media");

const PUB = path.join(VIDEO, "public"), GEN = path.join(VIDEO, "src", "generated");
// Estimates are used only until real voice files exist. 150 words a minute is a typical generated-voice pace.
const WPM = 150, GAP = 0.2, LEAD = 0.25;

// ---- narration lines, from the generated script ----
const parseLines = (md) => {
  const lines = [];
  const re = /^## Beat (\d+)[^\n]*\n([\s\S]*?)(?=^## |^---)/gm;
  let m;
  while ((m = re.exec(md))) {
    const beat = Number(m[1]);
    m[2].split("\n").filter((l) => l.startsWith(">")).map((l) => l.slice(1).trim()).filter(Boolean).forEach((raw, i) => {
      const speaker = /\*\*LILY:\*\*/.test(raw) ? "lily" : /^\*[^*].*\*$/.test(raw) ? "mia" : "narrator";
      const text = raw.replace(/\*\*LILY:\*\*/g, "").replace(/[*\[\]]/g, "").replace(/\s+/g, " ").trim();
      lines.push({ id: `b${beat}_${String(i + 1).padStart(2, "0")}`, beat, speaker, text });
    });
  }
  return lines;
};
const wavSeconds = (file) => {
  try {
    const b = fs.readFileSync(file);
    if (b.toString("ascii", 0, 4) !== "RIFF") return null;
    let off = 12, rate = null;
    while (off + 8 <= b.length) {
      const id = b.toString("ascii", off, off + 4), size = b.readUInt32LE(off + 4);
      if (id === "fmt ") rate = b.readUInt32LE(off + 16); // byte rate
      if (id === "data" && rate) return Math.min(size, b.length - off - 8) / rate;
      off += 8 + size + (size % 2);
    }
  } catch (e) { /* unreadable */ }
  return null;
};
const walk = (dir, base = "") => (fs.existsSync(dir) ? fs.readdirSync(dir, { withFileTypes: true }).flatMap((d) =>
  d.isDirectory() ? walk(path.join(dir, d.name), base + d.name + "/") : [base + d.name]) : []);
const chunk = (words, maxChars = 42) => {
  const out = []; let cur = [];
  words.forEach((w) => {
    if (cur.length && (cur.map((x) => x.word).join(" ") + " " + w.word).length > maxChars) { out.push(cur); cur = []; }
    cur.push(w);
  });
  if (cur.length) out.push(cur);
  return out;
};

const make = () => {
  const timeline = JSON.parse(fs.readFileSync(path.join(VIDEO, "timeline.json"), "utf8"));
  const lines = parseLines(build().out["narration_script.md"]);

  const files = {};
  walk(PUB).forEach((f) => {
    const full = path.join(PUB, f);
    files[f] = /\.wav$/i.test(f) ? { seconds: wavSeconds(full) } : /\.(mp4|mov|webm|mp3|m4a)$/i.test(f) ? { seconds: seconds(full) } : {};
  });

  // voice: lines run one after another inside their beat
  const voice = [], captions = [], overruns = [];
  timeline.beats.forEach((b) => {
    let cursor = b.start + LEAD;
    lines.filter((l) => l.beat === b.beat).forEach((l) => {
      const file = `voice/${l.id}.wav`;
      const real = files[file] && files[file].seconds;
      const est = Math.max(0.9, (l.text.split(/\s+/).length / WPM) * 60);
      const dur = real || est;
      const v = { ...l, start: +cursor.toFixed(2), end: +(cursor + dur).toFixed(2), file: real ? file : null, estimated: !real };
      voice.push(v);
      const wordsFile = path.join(PUB, "voice", `${l.id}.words.json`);
      let words = null;
      if (real && fs.existsSync(wordsFile)) { try { words = JSON.parse(fs.readFileSync(wordsFile, "utf8")); } catch (e) { words = null; } }
      if (words && words.length) {
        chunk(words).forEach((g) => captions.push({ start: +(v.start + g[0].start).toFixed(2), end: +(v.start + g[g.length - 1].end + 0.15).toFixed(2), text: g.map((x) => x.word).join(" "), speaker: l.speaker }));
      } else {
        const groups = chunk(l.text.split(/\s+/).map((w) => ({ word: w })));
        const total = groups.reduce((a, g) => a + g.map((x) => x.word).join(" ").length, 0);
        let t = v.start;
        groups.forEach((g) => { const len = g.map((x) => x.word).join(" ").length; const d = dur * (len / total); captions.push({ start: +t.toFixed(2), end: +(t + d).toFixed(2), text: g.map((x) => x.word).join(" "), speaker: l.speaker }); t += d; });
      }
      cursor += dur + GAP;
    });
    const used = cursor - GAP;
    if (used > b.end + 0.05) overruns.push({ beat: b.beat, over: +(used - b.end).toFixed(1) });
  });

  // ---- shots follow the voice: a shot with a cue starts when the narrator reaches it ----
  const wordsOf = {};
  voice.forEach((v) => { const f = path.join(PUB, "voice", `${v.id}.words.json`); if (v.file && fs.existsSync(f)) { try { wordsOf[v.id] = JSON.parse(fs.readFileSync(f, "utf8")); } catch (e) { /* ignore */ } } });
  const cueTime = (cue) => {
    if (!cue) return null;
    if (cue.startsWith("@")) { const v = voice.find((x) => x.id === cue.slice(1)); return v ? v.start : null; }
    const v = voice.find((x) => x.text.includes(cue));
    if (!v) return null;
    const idx = v.text.indexOf(cue), before = v.text.slice(0, idx).split(/\s+/).filter(Boolean).length, w = wordsOf[v.id];
    if (w && w[before]) return v.start + w[before].start;
    return v.start + (v.end - v.start) * (idx / v.text.length);
  };
  const missingCues = [];
  const MIN = 1.0, LEADIN = 0.2;
  timeline.shots.forEach((s) => { s.planned = [s.start, s.end]; });
  timeline.beats.forEach((b) => {
    const inBeat = timeline.shots.filter((s) => s.planned[0] >= b.start && s.planned[0] < b.end);
    inBeat.forEach((s, i) => {
      if (i === 0) { s.start = b.start; return; }
      const t = cueTime(s.cue);
      if (s.cue && t === null) missingCues.push({ shot: s.id, cue: s.cue });
      const want = t === null ? s.planned[0] : t - LEADIN;
      const latest = b.end - MIN * (inBeat.length - i);
      s.start = +Math.min(latest, Math.max(inBeat[i - 1].start + MIN, want)).toFixed(2);
    });
    inBeat.forEach((s, i) => { s.end = i + 1 < inBeat.length ? inBeat[i + 1].start : b.end; });
    inBeat.forEach((s) => {
      if (!s.cue2) return;
      const t = cueTime(s.cue2);
      if (t === null) missingCues.push({ shot: s.id, cue: s.cue2 });
      else s.split = +Math.min(0.85, Math.max(0.15, (t - LEADIN - s.start) / (s.end - s.start))).toFixed(3);
    });
  });

  const r = c.results, a = data.actual_figures_not_from_round2;
  const numbers = {
    total: data.participants_total, groups: data.groups,
    counts: r.counts.map((it) => ({ value: get(it.value), of: get(it.of), label: it.label })),
    ratings: r.ratings.map((it) => ({ value: get(it.value), label: fill(it.label) + (it.goal ? `, goal ${it.goal.toFixed(1)}` : ""), goal: it.goal || null })),
    quote: fill(r.quote), quoteSource: fill(r.quote_source), testsPass: a.unit_tests_passed, testsFail: a.unit_tests_failed,
  };
  const musicFile = Object.keys(files).find((f) => f.startsWith("music/"));
  return { out: { timeline, voice, captions, numbers, files, music: musicFile || null, projected, content: { team: c.team, results: { users_heading: r.users_heading, adults_heading: r.adults_heading } }, teamCode: c.team_code },
    lines, overruns, missingCues };
};

module.exports = { make, GEN };

if (require.main === module) {
  const { out, lines, overruns, missingCues } = make();
  fs.mkdirSync(GEN, { recursive: true });
  fs.writeFileSync(path.join(GEN, "data.json"), JSON.stringify(out, null, 1));
  fs.writeFileSync(path.join(GEN, "narration.json"), JSON.stringify(lines, null, 1));
  const real = out.voice.filter((v) => v.file).length;
  console.log(`Video data written: ${out.timeline.shots.length} shots, ${lines.length} narration lines (${real} with real audio, ${lines.length - real} timed by estimate), ${Object.keys(out.files).length} media files.`);
  overruns.forEach((o) => console.log(`  WARNING: Beat ${o.beat} narration runs ${o.over}s past the end of its beat.`));
  missingCues.forEach((m) => console.log(`  WARNING: shot ${m.shot} has the cue "${m.cue}" but no narration line contains it; the planned time is used.`));
}
