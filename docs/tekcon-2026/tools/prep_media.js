// Copies and prepares everything the video needs into video/public/.
//   assets/*.png|jpg                      -> public/img/        (Lily poses, team image, ...)
//   assets/recordings/*.mp4               -> public/rec/        (REAL app footage; resized, loudness evened)
//   01_Video_Pitch/generated/*.png|mp4    -> public/story/      (generated stills, plates and clips)
//   assets/music/*                        -> public/music/      (one licensed track)
//   assets/voice/*.wav                    -> public/voice/      (hand-supplied lines, e.g. Lily's real app audio)
// Safe to run again: unchanged files are skipped.
const { ROOT, fs, path } = require("./lib");
const { tool, run, VIDEO } = require("./media");

const PUB = path.join(VIDEO, "public");
const ff = tool("ffmpeg");
const list = (dir, re) => (fs.existsSync(dir) ? fs.readdirSync(dir).filter((f) => re.test(f)).map((f) => path.join(dir, f)) : []);
const fresh = (src, dst) => fs.existsSync(dst) && fs.statSync(dst).mtimeMs >= fs.statSync(src).mtimeMs;
let done = 0, skipped = 0, copied = 0;

const put = (src, sub, args) => {
  const dstDir = path.join(PUB, sub);
  fs.mkdirSync(dstDir, { recursive: true });
  const dst = path.join(dstDir, path.basename(src));
  if (fresh(src, dst)) return void skipped++;
  if (args && ff) {
    const r = run("ffmpeg", ["-y", "-i", src, ...args, dst]);
    if (r.status === 0) return void done++;
    console.log(`  ffmpeg could not convert ${path.basename(src)}; copied as it is`);
  }
  fs.copyFileSync(src, dst);
  copied++;
};

const A = path.join(ROOT, "assets"), G = path.join(ROOT, "01_Video_Pitch", "generated");
list(A, /\.(png|jpe?g)$/i).forEach((f) => put(f, "img"));
list(path.join(A, "recordings"), /\.(mp4|mov|webm)$/i).forEach((f) =>
  put(f, "rec", ["-vf", "scale=-2:1280,fps=30", "-c:v", "libx264", "-crf", "20", "-pix_fmt", "yuv420p", "-c:a", "aac", "-af", "loudnorm=I=-16:TP=-1.5", "-movflags", "+faststart"]));
list(G, /\.(png|jpe?g)$/i).forEach((f) => put(f, "story"));
list(G, /\.(mp4|mov|webm)$/i).forEach((f) =>
  put(f, "story", ["-vf", "scale=1920:1080:force_original_aspect_ratio=increase,crop=1920:1080,fps=30", "-an", "-c:v", "libx264", "-crf", "18", "-pix_fmt", "yuv420p", "-movflags", "+faststart"]));
list(path.join(A, "voice"), /\.wav$/i).forEach((f) => put(f, "voice"));
const music = list(path.join(A, "music"), /\.(mp3|m4a|wav|ogg)$/i);
if (music.length) put(music[0], "music");
if (music.length > 1) console.log("  more than one file in assets/music/; using " + path.basename(music[0]));

console.log(`Media prepared: ${done} converted, ${copied} copied, ${skipped} unchanged.  ffmpeg: ${ff ? ff.via : "NOT FOUND (files copied without conversion)"}`);
