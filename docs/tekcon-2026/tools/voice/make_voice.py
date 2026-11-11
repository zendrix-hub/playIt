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
    import soundfile as sf
    from chatterbox.tts import ChatterboxTTS
    model = ChatterboxTTS.from_pretrained(device="cpu")

    def say(text, path):
        wav = model.generate(text, audio_prompt_path=sample)
        sf.write(path, wav.squeeze(0).cpu().numpy(), model.sr)
    return say


def ffmpeg_exe():
    """The system ffmpeg, or the one that comes with the imageio-ffmpeg package."""
    exe = shutil.which("ffmpeg")
    if exe:
        return exe
    try:
        import imageio_ffmpeg
        return imageio_ffmpeg.get_ffmpeg_exe()
    except Exception:
        return None


# Spellings the voice reads wrongly, and how to say them. Captions keep the script's spelling.
SAY = {"PlayIT": "Play It", "DepEd": "Dep Ed", "CIT University": "C I T University", "nanay": "nah-nai"}


def spoken(text):
    for a, b in SAY.items():
        text = text.replace(a, b)
    return text


def pitch_up(path, factor=1.26):
    """Lily's lines: raise the pitch about four semitones, keep the length. Needs ffmpeg."""
    exe = ffmpeg_exe()
    if not exe:
        print("    ffmpeg not found; Lily's line left at normal pitch")
        return
    import soundfile as sf
    rate = sf.info(path).samplerate
    tmp = path + ".tmp.wav"
    cmd = [exe, "-y", "-v", "error", "-i", path, "-af", f"asetrate={rate}*{factor},aresample={rate},atempo={1/factor:.4f}", tmp]
    if subprocess.run(cmd).returncode == 0:
        os.replace(tmp, path)


def finish(path, target_db=-20.0):
    """Trim silence at both ends and bring every line to the same loudness (RMS), so no line jumps out."""
    import numpy as np, soundfile as sf
    x, sr = sf.read(path, dtype="float32")
    if x.ndim > 1:
        x = x.mean(axis=1)
    loud = np.where(np.abs(x) > 10 ** (-42 / 20))[0]
    if len(loud):
        x = x[max(0, loud[0] - int(0.05 * sr)):min(len(x), loud[-1] + int(0.12 * sr))]
    rms = float(np.sqrt(np.mean(x ** 2))) or 1e-9
    gain = 10 ** (target_db / 20) / rms
    gain = min(gain, 0.89 / (float(np.abs(x).max()) or 1e-9))  # never clip
    sf.write(path, x * gain, sr)
    return len(x) / sr, 20 * np.log10(rms * gain)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--engine", choices=["kokoro", "chatterbox"], default="kokoro")
    ap.add_argument("--voice", default="am_michael", help="Kokoro voice name")
    ap.add_argument("--sample", default=os.path.join(ROOT, "assets", "voice_sample.wav"))
    ap.add_argument("--only", default="")
    ap.add_argument("--force", action="store_true")
    ap.add_argument("--finish-only", action="store_true", help="only trim and level the lines that already exist")
    a = ap.parse_args()

    if not os.path.exists(LINES):
        sys.exit("narration.json is missing. Run:  node build_video_data.js  (inside tools/) first.")
    lines = json.load(open(LINES, encoding="utf-8"))
    only = set(x for x in a.only.split(",") if x)
    os.makedirs(OUT, exist_ok=True)
    if a.finish_only:
        for ln in lines:
            out = os.path.join(OUT, ln["id"] + ".wav")
            if os.path.exists(out) and not os.path.exists(os.path.join(SUPPLIED, ln["id"] + ".wav")):
                secs, db = finish(out)
                print(f"  {ln['id']}: {secs:5.2f}s  {db:6.1f} dB  {ln['text'][:50]}")
        return
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
        say(spoken(ln["text"]), out)
        if ln["speaker"] == "lily":
            pitch_up(out)
        finish(out)
        made += 1
    print(f"Voice lines: {made} generated, {skipped} skipped. Engine: {a.engine}.")


if __name__ == "__main__":
    main()
