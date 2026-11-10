"""Word timings for captions. Listens to each voice line with Whisper (faster-whisper, CPU)
and writes video/public/voice/<id>.words.json. The caption TEXT always comes from the script,
never from what Whisper heard, so names like PlayIT stay spelled correctly.

    python voice/align.py            only lines that are new or changed
    python voice/align.py --force    all lines
"""
import json, os, sys

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.abspath(os.path.join(HERE, "..", ".."))
VOICE = os.path.join(ROOT, "video", "public", "voice")
LINES = os.path.join(ROOT, "video", "src", "generated", "narration.json")


def main():
    force = "--force" in sys.argv
    if not os.path.exists(LINES):
        sys.exit("narration.json is missing. Run:  node build_video_data.js  (inside tools/) first.")
    lines = {l["id"]: l for l in json.load(open(LINES, encoding="utf-8"))}
    todo = []
    for lid in lines:
        wav, out = os.path.join(VOICE, lid + ".wav"), os.path.join(VOICE, lid + ".words.json")
        if os.path.exists(wav) and (force or not os.path.exists(out) or os.path.getmtime(out) < os.path.getmtime(wav)):
            todo.append((lid, wav, out))
    if not todo:
        print("Word timings: nothing to do."); return
    from faster_whisper import WhisperModel
    print("Loading Whisper small.en on the CPU (first run downloads the model)...")
    model = WhisperModel("small.en", device="cpu", compute_type="int8")
    for lid, wav, out in todo:
        segments, _ = model.transcribe(wav, word_timestamps=True)
        heard = [w for s in segments for w in (s.words or [])]
        script = lines[lid]["text"].split()
        if not heard:
            print(f"  {lid}: Whisper heard nothing; captions will be spread evenly"); continue
        if len(heard) == len(script):
            words = [{"word": s, "start": round(h.start, 2), "end": round(h.end, 2)} for s, h in zip(script, heard)]
        else:  # counts differ (numbers, names): spread the script words across what was heard
            t0, t1 = heard[0].start, heard[-1].end
            step = (t1 - t0) / len(script)
            words = [{"word": s, "start": round(t0 + i * step, 2), "end": round(t0 + (i + 1) * step, 2)} for i, s in enumerate(script)]
        json.dump(words, open(out, "w", encoding="utf-8"))
        print(f"  {lid}: {len(words)} words timed" + ("" if len(heard) == len(script) else " (spread evenly; word counts differed)"))
    print(f"Word timings written for {len(todo)} lines.")


if __name__ == "__main__":
    main()
