"""Clone the narrator's lines from the user's own voice sample (Chatterbox, CPU).

    python clone_lines.py                      every narrator line in ../lines.json
    python clone_lines.py b4_01,b4_02          only these lines

Takes about 7 seconds of work per second of speech on this PC. Writes to ../voice_new/ so the film keeps
its current voice until the new takes are checked and copied into ../voice/. Uses the trimming, levelling and
spelling rules of the kit's make_voice.py. Clone only the voice of someone who agreed (here: the user's own).
"""
import json, os, sys, time
HERE = os.path.dirname(os.path.abspath(__file__)); FILM = os.path.dirname(HERE)
KIT_VOICE = os.environ.get("KIT_VOICE", r"C:\Users\riva.zn\playIt\docs\tekcon-2026\tools\voice")
sys.path.insert(0, KIT_VOICE)
import make_voice as mv
import soundfile as sf

SAMPLE = os.path.join(FILM, "..", "assets", "voice_sample_2.wav")
REF = os.path.join(FILM, "..", "assets", "voice_sample_2_ref.wav")
REF_SPAN = (13.5, 23.3)      # "Her teacher has a full classroom. Her nanay is still at work..." (the model listens to about 10 s)

if not os.path.exists(REF):
    x, sr = sf.read(SAMPLE); sf.write(REF, x[int(REF_SPAN[0] * sr):int(REF_SPAN[1] * sr)], sr)
only = set(sys.argv[1].split(",")) if len(sys.argv) > 1 else None
lines = [l for l in json.load(open(os.path.join(FILM, "lines.json"), encoding="utf-8")) if (l["id"] in only if only else l["speaker"] == "narrator")]   # named lines are cloned whoever speaks them
out_dir = os.path.join(FILM, "voice_new"); os.makedirs(out_dir, exist_ok=True)
say = mv.chatterbox_engine(REF)
for l in lines:
    t0 = time.time(); out = os.path.join(out_dir, l["id"] + ".wav")
    say(mv.spoken(l["text"]), out); secs, db = mv.finish(out)
    print(f"{l['id']}: {secs:5.2f}s {db:6.1f} dB  ({time.time() - t0:4.0f}s work)  {l['text'][:60]}", flush=True)
print("done", flush=True)
