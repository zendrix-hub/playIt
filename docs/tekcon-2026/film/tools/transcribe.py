"""Print the words of a recording with their times:  python transcribe.py file.wav [model]"""
import sys
from faster_whisper import WhisperModel
m = WhisperModel(sys.argv[2] if len(sys.argv) > 2 else "small.en", device="cpu", compute_type="int8")
segs, _ = m.transcribe(sys.argv[1], word_timestamps=True, language="en")
for s in segs:
    print(f"[{s.start:6.2f}-{s.end:6.2f}] " + " ".join(f"{w.word.strip()}@{w.start:.2f}" for w in s.words))
