"""Check new voice takes against the script and save their word times for the captions.

    python align_new.py [folder]        default ../voice_new

For each <id>.wav: Whisper writes down what it hears, the words are compared with the line in ../lines.json,
and <id>.words.json is written next to the WAV. Lines where the words differ are listed so they can be redone.
"""
import json, os, re, sys, difflib
from faster_whisper import WhisperModel
HERE = os.path.dirname(os.path.abspath(__file__)); FILM = os.path.dirname(HERE)
folder = sys.argv[1] if len(sys.argv) > 1 else os.path.join(FILM, "voice_new")
lines = {l["id"]: l for l in json.load(open(os.path.join(FILM, "lines.json"), encoding="utf-8"))}
NUM = {"1": "one", "6": "six", "12": "twelve", "15": "fifteen", "16": "sixteen", "30": "thirty", "56": "fifty six"}
def norm(t):
    t = t.lower().replace("playit", "play it").replace("wi-fi", "wifi").replace("wi fi", "wifi").replace("grown-ups", "grown ups")
    t = re.sub(r"\b(\d+\.\d)\b", lambda m: " point ".join(m.group(1).split(".")), t)
    t = re.sub(r"\b\d+\b", lambda m: NUM.get(m.group(), m.group()), t)
    t = re.sub(r"(\w)\.(\w)", r"\1\2", t)
    return re.sub(r"[^a-z' ]", " ", t).split()
m = WhisperModel("small.en", device="cpu", compute_type="int8")
bad = []
for f in sorted(os.listdir(folder)):
    if not f.endswith(".wav"): continue
    lid = f[:-4]; script = lines[lid]["text"]
    segs, _ = m.transcribe(os.path.join(folder, f), word_timestamps=True, language="en", initial_prompt=script)
    words = [w for s in segs for w in s.words]
    heard = " ".join(w.word.strip() for w in words)
    a, b = norm(script), norm(heard)
    ratio = difflib.SequenceMatcher(None, a, b).ratio()
    sw = script.split()
    if len(words) == len(sw):        # same count: keep the script's spelling with Whisper's times
        out = [{"word": sw[i], "start": round(w.start, 3), "end": round(w.end, 3)} for i, w in enumerate(words)]
    else:
        out = [{"word": w.word.strip(), "start": round(w.start, 3), "end": round(w.end, 3)} for w in words]
    json.dump(out, open(os.path.join(folder, lid + ".words.json"), "w"), indent=1)
    flag = "" if ratio > .93 else "   <-- CHECK"
    if flag: bad.append(lid)
    print(f"{lid}: match {ratio:.2f} words {len(words)}/{len(sw)}{flag}\n   script: {script}\n   heard:  {heard}", flush=True)
print("to redo:", ",".join(bad) or "none")
