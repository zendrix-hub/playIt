"""Nobody on the build side can hear the voice, so this prints what Whisper hears in each line
next to the script, with length and loudness. A line whose heard words differ from the script
needs a human ear:   python voice/check_voice.py
"""
import json, os, re
import numpy as np, soundfile as sf
from faster_whisper import WhisperModel

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.abspath(os.path.join(HERE, "..", ".."))
VOICE = os.path.join(ROOT, "video", "public", "voice")
lines = json.load(open(os.path.join(ROOT, "video", "src", "generated", "narration.json"), encoding="utf-8"))
model = WhisperModel("small.en", device="cpu", compute_type="int8")
norm = lambda t: re.sub(r"[^a-z0-9 ]", "", t.lower().replace("-", " ")).split()
odd = 0
for l in lines:
    wav = os.path.join(VOICE, l["id"] + ".wav")
    if not os.path.exists(wav):
        print(f"{l['id']}  (no audio)  {l['text']}"); continue
    x, sr = sf.read(wav, dtype="float32")
    db = 20 * np.log10(float(np.sqrt(np.mean(x ** 2))) or 1e-9)
    segs, _ = model.transcribe(wav)
    heard = " ".join(s.text.strip() for s in segs)
    a, b = norm(l["text"]), norm(heard)
    same = len(set(a) & set(b)) / max(1, len(set(a)))
    flag = "" if same >= 0.8 else "   <-- CHECK BY EAR"
    odd += bool(flag)
    print(f"{l['id']}  {len(x) / sr:5.2f}s {db:6.1f} dB  [{l['speaker']}]{flag}\n    script: {l['text']}\n    heard : {heard}")
print(f"\n{odd} line(s) to check by ear.")
