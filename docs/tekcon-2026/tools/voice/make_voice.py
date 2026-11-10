"""Generate the narration, one WAV per line, into video/public/voice/.

    python voice/make_voice.py                       draft voice (Kokoro, fast)
    python voice/make_voice.py --engine chatterbox --sample ../assets/voice_sample.wav
                                                     your cloned voice (slow on a PC with no graphics card)
    python voice/make_voice.py --only b4_01,b4_02    redo just these lines
    python voice/make_voice.py --force               redo everything

Lines come from video/src/generated/narration.json (written by build_video_data.js).
A line is skipped when assets/voice/<id>.wav exists: a hand-supplied file always wins
(use that for Lily's real app audio and for Mia's "Mmm").
Only clone a voice whose owner has agreed.
"""
import argparse, json, os, shutil, subprocess, sys

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.abspath(os.path.join(HERE, "..", ".."))
OUT = os.path.join(ROOT, "video", "public", "voice")
SUPPLIED = os.path.join(ROOT, "assets", "voice")
LINES = os.path.join(ROOT, "video", "src", "generated", "narration.json")


def kokoro_engine(voice):
    import numpy as np, soundfile as sf
    from kokoro import KPipeline
    pipe = KPipeline(lang_code="a")  # American English

    def say(text, path):
        parts = [audio for _, _, audio in pipe(text, voice=voice, speed=1.0)]
        sf.write(path, np.concatenate(parts), 24000)
    return say


def chatterbox_engine(sample):
    if not sample or not os.path.exists(sample):
        sys.exit("Chatterbox needs --sample pointing at a 10-20 second WAV of the voice to clone.")
    import torchaudio as ta
    from chatterbox.tts import ChatterboxTTS
    model = ChatterboxTTS.from_pretrained(device="cpu")

    def say(text, path):
        ta.save(path, model.generate(text, audio_prompt_path=sample), model.sr)
    return say


def pitch_up(path, factor=1.26):
    """Lily's lines: raise the pitch about four semitones, keep the length. Needs ffmpeg."""
    if not shutil.which("ffmpeg"):
        print("    ffmpeg not found; Lily's line left at normal pitch")
        return
    tmp = path + ".tmp.wav"
    cmd = ["ffmpeg", "-y", "-v", "error", "-i", path, "-af", f"asetrate=24000*{factor},aresample=24000,atempo={1/factor:.4f}", tmp]
    if subprocess.run(cmd).returncode == 0:
        os.replace(tmp, path)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--engine", choices=["kokoro", "chatterbox"], default="kokoro")
    ap.add_argument("--voice", default="am_michael", help="Kokoro voice name")
    ap.add_argument("--sample", default=os.path.join(ROOT, "assets", "voice_sample.wav"))
    ap.add_argument("--only", default="")
    ap.add_argument("--force", action="store_true")
    a = ap.parse_args()

    if not os.path.exists(LINES):
        sys.exit("narration.json is missing. Run:  node build_video_data.js  (inside tools/) first.")
    lines = json.load(open(LINES, encoding="utf-8"))
    only = set(x for x in a.only.split(",") if x)
    os.makedirs(OUT, exist_ok=True)
    say = None
    made = skipped = 0
    for ln in lines:
        if only and ln["id"] not in only:
            continue
        out = os.path.join(OUT, ln["id"] + ".wav")
        if os.path.exists(os.path.join(SUPPLIED, ln["id"] + ".wav")):
            print(f"  {ln['id']}: supplied file in assets/voice/ is used"); skipped += 1; continue
        if ln["speaker"] == "mia":
            print(f"  {ln['id']}: Mia's line is not generated. Supply assets/voice/{ln['id']}.wav or leave it silent."); skipped += 1; continue
        if os.path.exists(out) and not a.force and not only:
            skipped += 1; continue
        if say is None:
            print(f"Loading the {a.engine} voice (first run downloads the model)...")
            say = kokoro_engine(a.voice) if a.engine == "kokoro" else chatterbox_engine(a.sample)
        print(f"  {ln['id']} [{ln['speaker']}]: {ln['text'][:60]}")
        say(ln["text"], out)
        if ln["speaker"] == "lily":
            pitch_up(out)
        made += 1
    print(f"Voice lines: {made} generated, {skipped} skipped. Engine: {a.engine}.")


if __name__ == "__main__":
    main()
