"""Makes each beat's voice lines fit inside the beat's window (the brief fixes the windows).
If the lines of a beat add up to more than the window, all of that beat's lines are sped up by
the same small amount (pitch unchanged), never more than 20 percent. Beats that fit are left alone.

    python voice/fit_beats.py          run after make_voice.py and before align.py

The untouched line is kept beside each file as <id>.orig.wav, so this can be run again safely.
"""
import json, os, shutil, subprocess, sys
import soundfile as sf

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.abspath(os.path.join(HERE, "..", ".."))
VOICE = os.path.join(ROOT, "video", "public", "voice")
LINES = os.path.join(ROOT, "video", "src", "generated", "narration.json")
LEAD, GAP, TAIL, SILENT = 0.25, 0.2, 0.35, 0.9   # the same lead and gap build_video_data.js uses
MAX = 1.2


def ffmpeg_exe():
    exe = shutil.which("ffmpeg")
    if exe:
        return exe
    import imageio_ffmpeg
    return imageio_ffmpeg.get_ffmpeg_exe()


def main():
    lines = json.load(open(LINES, encoding="utf-8"))
    beats = json.load(open(os.path.join(ROOT, "video", "timeline.json"), encoding="utf-8"))["beats"]
    exe = ffmpeg_exe()
    for b in beats:
        ids = [l["id"] for l in lines if l["beat"] == b["beat"]]
        total, have = LEAD, []
        for lid in ids:
            wav, orig = os.path.join(VOICE, lid + ".wav"), os.path.join(VOICE, lid + ".orig.wav")
            if os.path.exists(orig):
                shutil.copyfile(orig, wav)          # always start again from the untouched line
            if os.path.exists(wav):
                d = sf.info(wav).duration
                have.append((lid, wav, orig, d))
                total += d + GAP
            else:
                total += SILENT + GAP
        window = b["end"] - b["start"] - TAIL
        spoken = sum(d for *_, d in have)
        over = total - GAP - window
        if over <= 0 or not have:
            print(f"Beat {b['beat']}: {total - GAP:5.1f}s of {b['end'] - b['start']}s, fits")
            continue
        factor = spoken / (spoken - over)
        if factor > MAX:
            print(f"Beat {b['beat']}: {total - GAP:5.1f}s needs {factor:.2f}x, more than {MAX}x. Cut words instead; left unchanged.")
            continue
        for lid, wav, orig, d in have:
            if not os.path.exists(orig):
                shutil.copyfile(wav, orig)
            tmp = wav + ".tmp.wav"
            r = subprocess.run([exe, "-y", "-v", "error", "-i", orig, "-af", f"atempo={factor:.4f}", tmp])
            if r.returncode == 0:
                os.replace(tmp, wav)
        print(f"Beat {b['beat']}: {total - GAP:5.1f}s of {b['end'] - b['start']}s, sped up {factor:.3f}x to fit")


if __name__ == "__main__":
    main()
