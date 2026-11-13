"""Write down what is actually said in voice files, WITHOUT showing Whisper the script.

    python hear.py ../voice_takes/*.wav          (or any WAV files or folders)

align_new.py gives Whisper the script as a hint, so it reports the script back even when a word is slurred. This tool gives no
hint, so a slurred word shows up as what it sounds like ("DepEd's Marungko" came back as "depth at smarunko" on 13 Nov).
For each file it prints the words heard, the words Whisper was unsure of, and, when the file name starts with a line id
from ../lines.json (b2_03.wav, b2_03_t2.wav), the script line and whether the words match.
"""
import sys, os, glob, json, re
from faster_whisper import WhisperModel
HERE = os.path.dirname(os.path.abspath(__file__)); FILM = os.path.dirname(HERE)
lines = {l["id"]: l["text"] for l in json.load(open(os.path.join(FILM, "lines.json"), encoding="utf-8"))}
NUM = {"1": "one", "5": "five", "9": "nine", "14": "fourteen", "15": "fifteen", "16": "sixteen", "30": "thirty", "56": "fifty six"}
def norm(t):
    t = t.lower().replace("playit", "play it").replace("play-it", "play it").replace("play -it", "play it").replace("wi-fi", "wifi").replace("wi fi", "wifi").replace("-", " ")
    t = re.sub(r"(\d) ?\.(\d)", r"\1 point \2", t); t = re.sub(r"\b\d+\b", lambda m: NUM.get(m.group(), m.group()), t)
    return re.sub(r"[^a-z' ]", " ", t).split()
files = []
for a in sys.argv[1:] or [os.path.join(FILM, "voice_takes")]:
    files += sorted(glob.glob(os.path.join(a, "*.wav"))) if os.path.isdir(a) else sorted(glob.glob(a))
m = WhisperModel("small.en", device="cpu", compute_type="int8")
for f in files:
    segs, _ = m.transcribe(f, word_timestamps=True, language="en")
    ws = [w for s in segs for w in s.words]; heard = " ".join(w.word.strip() for w in ws)
    name = os.path.basename(f); lid = next((i for i in sorted(lines, key=len, reverse=True) if name.startswith(i)), None)
    print(f"{name}  ({ws[-1].end:.2f}s)" if ws else name)
    print("   heard:  " + heard)
    if lid:
        a, b = norm(lines[lid]), norm(heard); diff = [w for w in b if w not in a] + [w for w in a if w not in b]
        print("   script: " + lines[lid]); print("   words match: " + ("yes" if a == b else "NO  (" + ", ".join(dict.fromkeys(diff)) + ")"))
    unsure = ", ".join(f"{w.word.strip()} ({w.probability:.2f})" for w in ws if w.probability < .6)
    if unsure: print("   unsure of: " + unsure)
    sys.stdout.flush()
