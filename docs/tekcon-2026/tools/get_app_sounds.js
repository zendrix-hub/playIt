// Copies the app's own sound effects out of the app branch (origin/main) into assets/sfx/,
// so the video uses the real PlayIT sounds. Run once:  node get_app_sounds.js
const { execFileSync } = require("child_process");
const { ROOT, fs, path } = require("./lib");
const SRC = "app/src/main/assets/audio/";
const WANT = [
  "ui/sfx_opening_pluck.wav", "ui/sfx_opening_playit.wav", "ui/sfx_correct_chime.mp3", "ui/sfx_incorrect_pop.mp3",
  "ui/sfx_node_unlock_chime.mp3", "ui/sfx_heart_recovery_sparkle.mp3", "ui/sfx_level_complete_fanfare.mp3",
  "ui/sfx_streak_badge_unlock.mp3", "phonemes/ph_m.wav",
];
const out = path.join(ROOT, "assets", "sfx");
fs.mkdirSync(out, { recursive: true });
WANT.forEach((f) => {
  const buf = execFileSync("git", ["show", `origin/main:${SRC}${f}`], { cwd: ROOT, maxBuffer: 64 * 1024 * 1024 });
  fs.writeFileSync(path.join(out, path.basename(f)), buf);
  console.log(`  ${path.basename(f)}  ${(buf.length / 1024).toFixed(0)} KB`);
});
console.log(`App sounds copied: ${WANT.length} -> assets/sfx/`);
