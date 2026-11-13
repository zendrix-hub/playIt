"""Clone lines of the script in the narrator's voice (Chatterbox, on the CPU).

    python clone_lines.py                         every narrator line of ../lines.json        -> ../voice_new/<id>.wav
    python clone_lines.py b4_01,b4_02             only these lines                            -> ../voice_new/
    python clone_lines.py b2_03 --takes 4         four different takes of each line           -> ../voice_takes/<id>_t1.wav ...
    python clone_lines.py b2_03 --takes 4 --say "Dep-Ed's=Dep Ed's"      same, with a word respelled for the voice only

Cloning is not exact: the same line comes out differently each time, and now and then a word is slurred. So for a line that
matters, make several takes, run hear.py on them (it writes down what is actually said, without being shown the script),
pick the clearest, copy it to ../voice_new/<id>.wav, then run align_new.py. tidy_take.py shortens long pauses if the take runs long.

Nothing in ../voice/ is touched: the film keeps its current voice until you copy a checked take in.
Takes about 7 to 12 seconds of work per second of speech on an old PC. Uses the trimming, levelling and spelling rules of the
kit's tools/voice/make_voice.py (SAY = words the voice reads wrongly and how to spell them for it).
Clone only the voice of someone who agreed. Here it is the team lead's own voice.

The voice tools (make_voice.py, and the model if it was fetched with get_model.sh) are looked for in ../../tools/voice.
Set the environment variable KIT_VOICE to use a copy somewhere else.
"""
import json, os, sys, time
HERE = os.path.dirname(os.path.abspath(__file__)); FILM = os.path.dirname(HERE)
KIT_VOICE = os.environ.get("KIT_VOICE") or os.path.join(FILM, "..", "tools", "voice")
sys.path.insert(0, KIT_VOICE)
import make_voice as mv
import soundfile as sf

SAMPLE = os.path.join(FILM, "..", "assets", "voice_sample_2.wav")
REF = os.path.join(FILM, "..", "assets", "voice_sample_2_ref.wav")
REF_SPAN = (13.5, 23.3)      # "Her teacher has a full classroom. Her nanay is still at work..." (the model listens to about 10 s)

args = sys.argv[1:]; takes = 0; say_extra = []; only = None
while args:
    a = args.pop(0)
    if a == "--takes": takes = int(args.pop(0))
    elif a == "--say": say_extra.append(args.pop(0).split("=", 1))
    else: only = set(a.split(","))
if not os.path.exists(REF):
    x, sr = sf.read(SAMPLE); sf.write(REF, x[int(REF_SPAN[0] * sr):int(REF_SPAN[1] * sr)], sr)
lines = [l for l in json.load(open(os.path.join(FILM, "lines.json"), encoding="utf-8")) if (l["id"] in only if only else l["speaker"] == "narrator")]   # named lines are cloned whoever speaks them
out_dir = os.path.join(FILM, "voice_takes" if takes else "voice_new"); os.makedirs(out_dir, exist_ok=True)
say = mv.chatterbox_engine(REF)
for n, l in enumerate(lines):
    text = mv.spoken(l["text"])
    for a, b in say_extra: text = text.replace(a, b)
    for k in range(1, max(takes, 1) + 1):
        t0 = time.time(); out = os.path.join(out_dir, l["id"] + (f"_t{k}" if takes else "") + ".wav")
        if takes:
            import torch; torch.manual_seed(100 * n + k)      # a different, repeatable take each time
        say(text, out); secs, db = mv.finish(out)
        print(f"{os.path.basename(out)}: {secs:5.2f}s {db:6.1f} dB  ({time.time() - t0:4.0f}s work)  {text[:70]}", flush=True)
print("done ->", os.path.relpath(out_dir, FILM), flush=True)
