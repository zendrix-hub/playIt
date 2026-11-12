"""Cut the screen recording into film frames for the phone, following recording_plan.json.
Writes rec/f_NNNN.jpg, where NNNN is the film frame (30 fps), and rec/rec.js (the list for the player).
Run from film/tools with the voice Python:  python make_rec_frames.py"""
import json, os, subprocess, glob
HERE = os.path.dirname(os.path.abspath(__file__)); FILM = os.path.dirname(HERE); ROOT = os.path.dirname(FILM)
P = json.load(open(os.path.join(FILM, "recording_plan.json"), encoding="utf-8"))
FF = os.path.join(ROOT, "video", "node_modules", "@remotion", "compositor-win32-x64-msvc", "ffmpeg.exe")
SRC = os.path.join(ROOT, P["source"]); OUT = os.path.join(FILM, "rec"); FPS = 30
os.makedirs(OUT, exist_ok=True)
for f in glob.glob(os.path.join(OUT, "f_*.jpg")): os.remove(f)
c = P["crop"]; segs = []
for s in P["segments"]:
    (a, b), (fa, fb), sp = s["src"], s["film"], s["speed"]
    # sampling the recording at 30/speed fps gives one picture per film frame
    n0 = round(fa * FPS); count = max(1, round((b - a) / sp * FPS))
    subprocess.run([FF, "-v", "error", "-y", "-ss", f"{a:.3f}", "-i", SRC, "-t", f"{b - a:.3f}", "-an",
                    "-vf", f"crop={c['w']}:{c['h']}:{c['x']}:{c['y']},scale=360:-2",
                    "-r", f"{FPS / sp:g}", "-frames:v", str(count), "-q:v", "3", "-start_number", str(n0),
                    os.path.join(OUT, "f_%04d.jpg")], check=True)
    end = round(s.get("hold", fb) * FPS)
    segs.append([n0, end]); print(f"{s['slot']:11s} film {fa:6.2f}-{s.get('hold', fb):6.2f}  frames {n0}-{end}  ({count} cut)")
frames = sorted(int(os.path.basename(f)[2:6]) for f in glob.glob(os.path.join(OUT, "f_*.jpg")))

# the profile name: the recording says "Dj", the story's child is Mia
import numpy as np
from PIL import Image, ImageDraw, ImageFont
RL = P.get("relabel")
if RL:
    font_file = os.path.join(os.environ.get("LOCALAPPDATA", ""), "Microsoft", "Windows", "Fonts", RL["font"])
    if not os.path.exists(font_file): font_file = os.path.join(os.environ.get("WINDIR", "C:/Windows"), "Fonts", RL["font"])
    fp = lambda n: os.path.join(OUT, f"f_{n:04d}.jpg")
    for sp in RL["spots"]:
        size = round(sp["cap"] / 0.7); font = ImageFont.truetype(font_file, size)
        x0, y0, x1, y1 = sp["find"]
        T = np.asarray(Image.open(fp(sp["ref_frame"])).convert("L"), dtype=float)[y0:y1, x0:x1]
        done = 0
        for n in [n for n in frames if sp["frames"][0] <= n <= sp["frames"][1]]:
            im = Image.open(fp(n)).convert("RGB"); G = np.asarray(im.convert("L"), dtype=float)
            best = None; top = sp.get("clip_top", 0)   # rows above clip_top are covered by a fixed header
            for dy in range(-y0, G.shape[0] - y1):
                vis = np.arange(y0 + dy, y1 + dy) >= top
                if vis.mean() < .4: continue
                err = np.abs(G[y0 + dy:y1 + dy, x0:x1] - T)[vis].mean()
                if best is None or err < best[0]: best = (err, dy)
            if n in sp.get("force", []): best = (0, 0)
            if best is None or best[0] > 30:     # the name is not on this frame
                if best and best[0] < 45: print(f"  frame {n}: name only partly matched (error {best[0]:.0f}, dy {best[1]}), left as recorded")
                continue
            dy = best[1]; orig = im.copy(); A = np.asarray(im); cx0, cy0, cx1, cy1 = sp["cover"]; cy0 += dy; cy1 += dy
            ring = np.concatenate([A[cy0 - 2, cx0:cx1], A[cy1 + 1, cx0:cx1]])
            bg = tuple(int(v) for v in np.median(ring, axis=0))
            ink = tuple(int(v) for v in np.percentile(A[max(y0 + dy, top):y1 + dy, x0:x1].reshape(-1, 3), 3, axis=0))
            keep = None
            if sp.get("keep"): kx0, ky0, kx1, ky1 = sp["keep"]; keep = im.crop((kx0, ky0 + dy, kx1, ky1 + dy))
            # fill each row with a blend of the pixels just left and right of the name, so gradients stay smooth
            B = np.asarray(im).astype(float).copy(); wv = np.linspace(0, 1, cx1 - cx0 + 1)[None, :, None]
            L = B[cy0:cy1 + 1, cx0 - 2:cx0 - 1]; R = B[cy0:cy1 + 1, cx1 + 1:cx1 + 2]
            B[cy0:cy1 + 1, cx0:cx1 + 1] = L * (1 - wv) + R * wv
            im = Image.fromarray(B.clip(0, 255).astype("uint8")); g = ImageDraw.Draw(im)
            g.text((sp["x"], sp["baseline"] + dy), sp["text"], font=font, fill=ink, anchor="ls")
            if keep: im.paste(keep, (round(g.textlength(sp["text"], font=font)) + sp["x"] + 8, sp["keep"][1] + dy))
            if top > 0: im.paste(orig.crop((0, 0, im.width, top)), (0, 0))   # the header stays on top
            im.save(fp(n), quality=92); done += 1
        print(f"relabel {sp['where']}: {done} frames")
with open(os.path.join(OUT, "rec.js"), "w") as fh:
    fh.write("// Written by tools/make_rec_frames.py. Do not edit.\nwindow.REC = " + json.dumps({"fps": FPS, "segments": segs, "frames": frames}) + ";\n")
print(len(frames), "frames,", round(sum(os.path.getsize(os.path.join(OUT, f"f_{n:04d}.jpg")) for n in frames) / 1e6, 1), "MB")
