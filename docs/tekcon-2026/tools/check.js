// Checks the kit and prints PASS / WARN / FAIL lines. Exit code 1 if anything fails.
// Run after every change: npm run check   (or npm run build, which ends with this)
const { ROOT, data, content: c, projected, get, fill, fs, path } = require("./lib");
const { build } = require("./build_pitch");

let fails = 0, warns = 0;
const pass = (m) => console.log("PASS  " + m);
const warn = (m) => { warns++; console.log("WARN  " + m); };
const fail = (m) => { fails++; console.log("FAIL  " + m); };
const ok = (cond, good, bad) => (cond ? pass(good) : fail(bad));

// ---- 1. Survey data adds up ----
const g = data.groups, ch = data.children, pa = data.parents, te = data.teachers, tc = data.technical;
const gsum = g.children + g.parents + g.teachers + g.technical;
ok(gsum === data.participants_total, `groups add up to ${data.participants_total} participants`, `groups add up to ${gsum}, but participants_total is ${data.participants_total}`);
[["children", ch], ["parents", pa], ["teachers", te], ["technical", tc]].forEach(([k, o]) =>
  ok(o.n === g[k], `${k}.n matches groups.${k} (${o.n})`, `${k}.n is ${o.n} but groups.${k} is ${g[k]}`));
const splits = [
  ["happy/okay/sad faces", [ch.happy_face, ch.okay_face, ch.sad_face], ch.n, true],
  ["liked-characters faces", [ch.liked_characters_happy, ch.liked_characters_okay, ch.liked_characters_neutral], ch.n, false],
  ["play-again faces", [ch.play_again_happy, ch.play_again_okay, ch.play_again_neutral], ch.n, false],
  ["easy-to-play faces", [ch.easy_to_play_happy, ch.easy_to_play_okay, ch.easy_to_play_neutral], ch.n, false],
  ["Find It outcomes", [ch.findit_no_hearts_lost, ch.findit_one_or_two_mistakes, ch.findit_needed_help], ch.n, false],
  ["Blend It outcomes", [ch.blendit_smooth, ch.blendit_minor_retries, ch.blendit_needed_hint], ch.n, false],
  ["home internet answers", [pa.home_internet_always_wifi, pa.home_internet_mobile_or_patchy, pa.home_internet_none], pa.n, false],
];
splits.forEach(([name, vals, n, required]) => {
  if (!required && vals.every((v) => v == null)) return;
  const sum = vals.reduce((a, b) => a + b, 0);
  ok(sum === n, `${name} add up to ${n}`, `${name} add up to ${sum}, should be ${n}`);
});
[["children", ch], ["technical", tc]].forEach(([k, o]) => Object.entries(o).forEach(([f, v]) => {
  if (f !== "n" && !f.startsWith("median") && !f.startsWith("_") && typeof v === "number" && (v < 0 || v > o.n || !Number.isInteger(v)))
    fail(`${k}.${f} is ${v}; it must be a whole number of people from 0 to ${o.n}`);
}));
if (ch.sayit_first_try != null && ch.sayit_within_two_tries != null)
  ok(ch.sayit_within_two_tries >= ch.sayit_first_try, "Say It within two tries is at least the first-try count", "sayit_within_two_tries is smaller than sayit_first_try");
const means = [...Object.entries(pa).filter(([k, v]) => k.endsWith("_mean") && v !== null).map(([k, v]) => ["parents." + k, v]),
  ...Object.entries(te.item_means).map(([k, v]) => ["teachers " + k, v]), ["teachers.overall_mean", te.overall_mean]];
const badMeans = means.filter(([, v]) => typeof v !== "number" || v < 1 || v > 5);
ok(badMeans.length === 0, "all ratings are between 1 and 5", "ratings outside 1 to 5: " + badMeans.map(([k, v]) => `${k}=${v}`).join(", "));
const items = Object.values(te.item_means), avg = items.reduce((a, b) => a + b, 0) / items.length;
ok(Math.abs(avg - te.overall_mean) <= 0.051, `teachers.overall_mean matches the item average (${avg.toFixed(2)})`, `teachers.overall_mean is ${te.overall_mean} but the items average ${avg.toFixed(2)}`);
ok(items.length === te.items_total, "teacher item count matches items_total", `items_total is ${te.items_total} but there are ${items.length} items`);

// ---- 2. Poster wording fits its space (at the fixed font sizes) ----
const limits = [
  ["tagline", c.tagline, 40], ["full_title", c.full_title, 100], ["org_line", c.org_line, 70],
  ["headline.label", c.headline.label, 32], ["solution.text", c.solution.text, 120],
  ["how.subline", c.how.subline, 48], ["how.parent.text", c.how.parent.text, 70],
  ["results.subline", fill(c.results.subline), 100], ["results.quote + source", fill(c.results.quote) + fill(c.results.quote_source), 105],
  ["results.footer", fill(c.results.footer), 120], ["results.users_heading", c.results.users_heading, 56], ["results.adults_heading", c.results.adults_heading, 56], ["qr.label", c.qr.label, 42],
  ...c.problem.items.flatMap((it, i) => [[`problem.items[${i}].title`, it.title, 30], [`problem.items[${i}].text`, it.text, 44]]),
  ...c.solution.badges.map((b, i) => [`solution.badges[${i}]`, b, 16]),
  ...c.how.steps.map((st, i) => [`how.steps[${i}].label`, st.label, 12]),
  ...c.how.tech.map((t, i) => [`how.tech[${i}]`, t, 28]),
  ...c.results.counts.map((it, i) => [`results.counts[${i}].label`, it.label, 36]),
  ...c.results.ratings.map((it, i) => [`results.ratings[${i}].label`, fill(it.label) + (it.goal ? ", goal 4.2" : ""), 60]),
  ...c.impact.points.map((p, i) => [`impact.points[${i}]`, p, 58]),
  ...c.team.members.map((m, i) => [`team.members[${i}]`, m, 24]),
  ...c.references.map((r, i) => [`references[${i}]`, r, 150]),
];
const tooLong = limits.filter(([, t, max]) => t.length > max);
ok(tooLong.length === 0, `all ${limits.length} poster text fields fit their space`,
  "text too long (shorten the wording, do not shrink the font): " + tooLong.map(([k, t, max]) => `${k} is ${t.length} chars, limit ${max}`).join("; "));
const counts = [["problem.items", c.problem.items.length, 3], ["solution.badges", c.solution.badges.length, 3], ["how.steps", c.how.steps.length, 4],
  ["how.tech", c.how.tech.length, 4], ["results.counts", c.results.counts.length, 3], ["results.ratings", c.results.ratings.length, 3],
  ["impact.points", c.impact.points.length, 2], ["references", c.references.length, 4]];
counts.forEach(([k, n, want]) => { if (n !== want) fail(`${k} has ${n} entries; the layout holds exactly ${want}`); });
try {
  [...c.results.counts.flatMap((it) => [it.value, it.of]), ...c.results.ratings.map((it) => it.value), c.headline.value, c.headline.of].forEach(get);
  pass("every poster figure points to a real value in round2_data.json");
} catch (e) { fail(e.message); }
c.results.counts.forEach((it) => { if (get(it.value) > get(it.of)) fail(`${it.value} (${get(it.value)}) is larger than ${it.of} (${get(it.of)})`); });
ok(c.qr.label === "Scan to Watch Our 2-Minute Project Pitch", "QR label uses the required wording", 'qr.label must read exactly "Scan to Watch Our 2-Minute Project Pitch"');

// ---- 3. Pitch script fits two minutes ----
try {
  const { out, beats, total, secs } = build();
  const budget = { "Beat 1": 45, "Beat 2": 50, "Beat 3": 68, "Beat 4": 70, "Beat 5": 40 };
  beats.forEach((b) => ok(b.words <= budget[b.beat], `${b.beat}: ${b.words} words (limit ${budget[b.beat]})`, `${b.beat}: ${b.words} words, over the limit of ${budget[b.beat]}; cut words in templates/narration_script.template.md`));
  ok(beats.length === 5, "script has the five required beats", `script has ${beats.length} beats, needs 5`);
  ok(total <= 265 && secs <= 120, `narration is ${total} words, about ${secs} seconds (limit 265 words, 120 s)`, `narration is ${total} words, about ${secs} s; limit is 265 words and 120 s`);
  for (const [name, text] of Object.entries(out)) {
    const p = path.join(ROOT, "01_Video_Pitch", name);
    ok(fs.existsSync(p) && fs.readFileSync(p, "utf8") === text, `${name} is up to date`, `${name} is out of date; run: npm run pitch`);
    if (/\{\{|\}\}/.test(text)) fail(`${name} still contains an unfilled {{field}}`);
  }
} catch (e) { fail("pitch build: " + e.message); }

// ---- 4. Poster file ----
const poster = path.join(ROOT, "02_Research_Poster", "Research_Poster_Source.pptx");
if (!fs.existsSync(poster)) fail("poster file missing; run: npm run poster");
else {
  const pm = fs.statSync(poster).mtimeMs;
  const newer = ["round2_data.json", "content.json"].filter((f) => fs.statSync(path.join(ROOT, f)).mtimeMs > pm);
  ok(newer.length === 0, "poster is newer than both source files", `poster is older than ${newer.join(" and ")}; run: npm run poster`);
}

// ---- 5. Pitch video ----
try {
  const { make, GEN } = require("./build_video_data");
  const { seconds, VIDEO } = require("./media");
  const { out, overruns, missingCues } = make();
  const shots = out.timeline.shots, beats = out.timeline.beats;
  const gaps = shots.filter((s, i) => (i === 0 ? s.start !== 0 : s.start !== shots[i - 1].end) || s.end <= s.start);
  ok(gaps.length === 0, `video timeline: ${shots.length} shots touch with no gaps or overlaps`, "video timeline has a gap, overlap or zero-length shot at shot " + gaps.map((s) => s.id).join(", "));
  ok(shots[shots.length - 1].end === 120, "video timeline ends at 2:00", `video timeline ends at ${shots[shots.length - 1].end}s; it must end at 120`);
  const want = [0, 20, 40, 70, 100, 120], got = [beats[0].start, ...beats.map((b) => b.end)];
  ok(JSON.stringify(got) === JSON.stringify(want), "beats keep the required windows (0:20, 0:40, 1:10, 1:40, 2:00)", `beat boundaries are ${got.join(", ")}; the brief requires ${want.join(", ")}`);
  const cut = beats.filter((b) => !shots.some((s) => s.start === b.start));
  ok(cut.length === 0, "every beat starts on a shot boundary", "no shot starts exactly at the start of beat " + cut.map((b) => b.beat).join(", "));
  ok(missingCues.length === 0, "every shot cue is found in the narration", "shot cues not found in the narration (fix the cue in video/timeline.json): " + missingCues.map((m) => `shot ${m.shot} \"${m.cue}\"`).join(", "));
  const real = out.voice.filter((v) => v.file).length;
  if (overruns.length) {
    const msg = overruns.map((o) => `Beat ${o.beat} by ${o.over}s`).join(", ");
    real === out.voice.length ? fail(`narration runs past its beat: ${msg}; cut words in templates/narration_script.template.md`) : warn(`narration may run past its beat (${msg}, partly estimated); re-check once every line has real audio`);
  } else pass("narration fits inside every beat");
  const need = out.voice.filter((v) => v.speaker !== "mia").length;
  real >= need ? pass(`all ${need} spoken lines have audio`) : warn(`${real} of ${need} spoken lines have audio; the rest are silent with estimated timing (run: npm run video:voice)`);
  const dataFile = path.join(GEN, "data.json");
  ok(fs.existsSync(dataFile) && fs.readFileSync(dataFile, "utf8") === JSON.stringify(out, null, 1), "video data is up to date", "video data is out of date; run: node build_video_data.js");
  const footage = [...new Set(shots.flatMap((s) => [s.rec, s.rec2]).filter(Boolean))];
  const noFootage = footage.filter((f) => !out.files["rec/" + f]);
  noFootage.length ? warn(`${noFootage.length} of ${footage.length} real app recordings missing from assets/recordings/: ${noFootage.join(", ")}`) : pass("all real app recordings are present");
  const finalFile = path.join(VIDEO, "out", `${c.team_code}_2-Minute_Pitch.mp4`);
  if (!fs.existsSync(finalFile)) warn("final video not rendered yet (run: npm run video:final)");
  else {
    const len = seconds(finalFile);
    if (len === null) warn("final video exists but its length could not be read (ffprobe not found)");
    else ok(len <= 120.05, `final video is ${len.toFixed(1)}s (limit 120)`, `final video is ${len.toFixed(1)}s; the brief allows 120 at most`);
    if (fs.statSync(finalFile).mtimeMs < fs.statSync(path.join(ROOT, "round2_data.json")).mtimeMs) warn("final video is older than round2_data.json; render it again");
  }
} catch (e) { fail("video check: " + e.message); }

// ---- 6. Pictures and print readiness (warnings only) ----
const assets = [c.lily_asset, c.team.asset, c.qr.asset, c.how.parent.asset, ...c.how.steps.map((st) => st.asset)];
const missing = assets.filter((a) => !fs.existsSync(path.join(ROOT, "assets", a)));
missing.length ? warn(`${missing.length} of ${assets.length} pictures not in assets/ yet: ${missing.join(", ")}`) : pass("all poster pictures are in assets/");
if (projected) warn('round2_data.json status is "projected": the figures are NOT real results');
// team_code "G56" was confirmed as the official code on 12 Nov 2026.

console.log("");
console.log(`${fails} failed, ${warns} warnings.`);
console.log(fails === 0 && warns === 0 ? "READY TO PRINT AND RECORD: yes" : "READY TO PRINT AND RECORD: no" + (fails ? " (fix the FAIL lines first)" : " (clear the WARN lines)"));
process.exit(fails ? 1 : 0);
