"""Builds the music bed from the app's own pluck sound (assets/sfx/sfx_opening_pluck.wav),
so there is nothing to license. Writes assets/music/bed.wav, exactly as long as the video.

    python voice/make_music.py

Shape: a sparse, slow line for Beat 1 that stops before Lily arrives (the quiet matters),
then a light playful pattern from 0:20, thinner under the results, fuller for the close.
The same notes come out every run.
"""
import json, os
import numpy as np, soundfile as sf

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.abspath(os.path.join(HERE, "..", ".."))
PLUCK = os.path.join(ROOT, "assets", "sfx", "sfx_opening_pluck.wav")
OUT = os.path.join(ROOT, "assets", "music", "bed.wav")
SR = 44100


def load_pluck():
    x, sr = sf.read(PLUCK, dtype="float32")
    if x.ndim > 1:
        x = x.mean(axis=1)
    if sr != SR:
        n = int(len(x) * SR / sr)
        x = np.interp(np.linspace(0, len(x) - 1, n), np.arange(len(x)), x).astype("float32")
    return x / (np.abs(x).max() or 1)


def note(pluck, semitones, length=1.6):
    """The pluck moved up or down by semitones, with a soft tail."""
    ratio = 2 ** (semitones / 12)
    n = int(len(pluck) / ratio)
    y = np.interp(np.linspace(0, len(pluck) - 1, n), np.arange(len(pluck)), pluck)
    out = np.zeros(int(length * SR), dtype="float32")
    m = min(len(y), len(out))
    out[:m] = y[:m]
    # a short echo so single notes do not sound dry
    for k, g in ((0.19, 0.35), (0.38, 0.16), (0.57, 0.07)):
        d = int(k * SR)
        out[d:d + m] += g * y[:max(0, min(m, len(out) - d))]
    out *= np.exp(-np.linspace(0, 3.0, len(out)))
    return out


def main():
    timeline = json.load(open(os.path.join(ROOT, "video", "timeline.json"), encoding="utf-8"))
    total = timeline["beats"][-1]["end"]
    pluck = load_pluck()
    mix = np.zeros(int((total + 2) * SR), dtype="float32")
    cache = {}

    def put(t, semi, gain):
        if semi not in cache:
            cache[semi] = note(pluck, semi)
        y, i = cache[semi], int(t * SR)
        if i < len(mix):
            mix[i:i + len(y)] += gain * y[:len(mix) - i]

    rng = np.random.default_rng(56)
    # Beat 1: slow and sparse, a minor colour, fading out before 0:19
    minor = [-12, -9, -7, -5, -2, 0]
    t = 0.6
    while t < 17.5:
        put(t, int(rng.choice(minor)), 0.55 * min(1.0, (18.5 - t) / 5))
        t += 1.43
    # From 0:20: major pentatonic, steady eighths with a low note on each bar
    major = [0, 2, 4, 7, 9, 12, 14, 16]
    bars = [0, 0, 5, 7]  # I I IV V, in semitones
    step = 60 / 96 / 2
    t, i = 20.3, 0
    while t < total - 3.0:
        bar = bars[(i // 8) % 4]
        level = 0.45 if 70 <= t < 100 else 0.75 if t >= 100 else 0.6
        if i % 8 == 0:
            put(t, bar - 12, 0.9 * level)
        if 40 <= t < 100 and i % 2 == 1:
            pass  # leave room for the voice during the demo and the results
        else:
            put(t, bar + major[(i * 3 + (i // 8)) % len(major)], level * (0.8 if i % 2 == 0 else 0.5))
        t += step
        i += 1
    for k, semi in enumerate([0, 4, 7, 12]):  # last chord, rolled
        put(total - 2.6 + k * 0.09, semi, 0.8)
    mix = mix[:int(total * SR)]
    fade = int(0.8 * SR)
    mix[-fade:] *= np.linspace(1, 0, fade)
    mix *= 0.5 / (np.abs(mix).max() or 1)  # peak at -6 dBFS
    os.makedirs(os.path.dirname(OUT), exist_ok=True)
    sf.write(OUT, mix, SR)
    print(f"Music bed written: {os.path.relpath(OUT, ROOT)}  {len(mix) / SR:.1f}s")


if __name__ == "__main__":
    main()
