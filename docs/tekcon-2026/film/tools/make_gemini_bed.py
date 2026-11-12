"""Builds film/music/bed.wav from assets/music/bed_gemini.mp3.

Shapes the Gemini music bed so it blends behind the narration without masking voice clarity:
1. Decodes assets/music/bed_gemini.mp3 to 44.1 kHz stereo PCM via ffmpeg.
2. Applies a gentle -3.5 dB vocal-clarity pocket around 2.2 kHz (RBJ peaking EQ).
3. Smoothly levels section energy across Beat 1..Beat 5 (so loud Beat 2/4/5 sections
   do not jump +10 dB over Beat 1/3).
4. Ducks cleanly under every spoken line in film/data.json (targeting ~-41 dBFS effective
   under voice after lib.js vol=0.12, blooming ~+6 dB in pauses, soft Beat-1 gap, clean
   2:00 fade-out).

Run: python film/tools/make_gemini_bed.py
"""
import json, math, os, shutil, struct, subprocess, tempfile, wave

HERE = os.path.dirname(os.path.abspath(__file__))
FILM = os.path.dirname(HERE)
KIT = os.path.dirname(FILM)
SRC = os.path.join(KIT, "assets", "music", "bed_gemini.mp3")
DATA = os.path.join(FILM, "data.json")
OUT = os.path.join(FILM, "music", "bed.wav")

SR = 44100
TOTAL_SEC = 120.0
# film/lib.js plays music/bed.wav at vol=0.12 (-18.42 dB).
# Target raw bed.wav RMS levels before vol=0.12:
TARGET_VOICE_DB = -22.5   # -> ~ -40.9 dBFS in mix (~20 dB below -21.1 dBFS voice)
TARGET_GAP_DB = -16.5     # -> ~ -34.9 dBFS in mix (warm +6 dB swell between lines)


def find_ffmpeg():
    exe = os.environ.get("FFMPEG") or shutil.which("ffmpeg")
    if exe and os.path.exists(exe):
        return exe
    sub = os.path.join("video", "node_modules", "@remotion", "compositor-win32-x64-msvc", "ffmpeg.exe")
    for base in (KIT, os.path.join(os.path.expanduser("~"), "Desktop", "tekcon-2026")):
        cand = os.path.join(base, sub)
        if os.path.exists(cand):
            return cand
    raise RuntimeError("ffmpeg not found on PATH, FFMPEG env, or video/node_modules")


def peaking_eq_coeffs(sr, freq=2200.0, q=0.85, gain_db=-3.5):
    a = 10 ** (gain_db / 40.0)
    w0 = 2.0 * math.pi * freq / sr
    alpha = math.sin(w0) / (2.0 * q)
    b0 = 1.0 + alpha * a
    b1 = -2.0 * math.cos(w0)
    b2 = 1.0 - alpha * a
    a0 = 1.0 + alpha / a
    a1 = -2.0 * math.cos(w0)
    a2 = 1.0 - alpha / a
    return (b0 / a0, b1 / a0, b2 / a0, a1 / a0, a2 / a0)


def apply_biquad_stereo(left, right, coeffs):
    b0, b1, b2, a1, a2 = coeffs
    for ch in (left, right):
        x1 = x2 = y1 = y2 = 0.0
        for i, x0 in enumerate(ch):
            y0 = b0 * x0 + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2
            ch[i] = y0
            x2, x1 = x1, x0
            y2, y1 = y1, y0


def smooth_box(arr, radius):
    n = len(arr)
    pref = [0.0] * (n + 1)
    for i, v in enumerate(arr):
        pref[i + 1] = pref[i] + v
    out = [0.0] * n
    for i in range(n):
        lo = max(0, i - radius)
        hi = min(n, i + radius + 1)
        out[i] = (pref[hi] - pref[lo]) / (hi - lo)
    return out


def main():
    if not os.path.exists(SRC):
        raise FileNotFoundError(f"Missing source music: {SRC}")
    data = json.load(open(DATA, encoding="utf-8"))
    ff = find_ffmpeg()

    with tempfile.NamedTemporaryFile(suffix=".wav", delete=False) as tf:
        tmp_wav = tf.name
    try:
        subprocess.run([ff, "-y", "-v", "error", "-i", SRC, "-ac", "2", "-ar", str(SR), "-c:a", "pcm_s16le", tmp_wav], check=True)
        with wave.open(tmp_wav, "rb") as w:
            n_frames = min(w.getnframes(), int(TOTAL_SEC * SR))
            raw = w.readframes(n_frames)
    finally:
        if os.path.exists(tmp_wav):
            os.remove(tmp_wav)

    samples = struct.unpack(f"<{n_frames * 2}h", raw)
    left = [samples[i * 2] / 32768.0 for i in range(n_frames)]
    right = [samples[i * 2 + 1] / 32768.0 for i in range(n_frames)]

    # Pad to exact 120.0s if needed
    target_frames = int(TOTAL_SEC * SR)
    if n_frames < target_frames:
        pad = target_frames - n_frames
        left.extend([0.0] * pad)
        right.extend([0.0] * pad)
        n_frames = target_frames

    # 1. Gentle vocal-clarity dip at 2.2 kHz so instruments never mask diction
    apply_biquad_stereo(left, right, peaking_eq_coeffs(SR, freq=2200.0, q=0.85, gain_db=-3.5))

    # 2. Compute 50 ms frame RMS curve and smooth over ~2.5 s for gentle section leveling
    hop = int(0.05 * SR)  # 20 Hz control grid (2400 points for 120s)
    m = (n_frames + hop - 1) // hop
    block_rms = [0.0] * m
    for k in range(m):
        s0 = k * hop
        s1 = min(n_frames, s0 + hop)
        acc = 0.0
        for i in range(s0, s1):
            acc += 0.5 * (left[i] * left[i] + right[i] * right[i])
        block_rms[k] = math.sqrt(acc / max(1, s1 - s0))

    # Twice box-smoothed (~triangular/Gaussian) over +-1.5s to get slow musical contour
    contour = smooth_box(smooth_box(block_rms, 30), 30)

    # 3. Determine desired target RMS at every 50ms control frame
    voice_spans = [(v["start"] - 0.18, v["end"] + 0.22) for v in data["voice"] if v.get("file")]
    speech_mask = [0.0] * m
    for k in range(m):
        t = (k + 0.5) * 0.05
        if any(a <= t <= b for a, b in voice_spans):
            speech_mask[k] = 1.0
    # Smooth transitions (~0.25s attack/release)
    speech_env = smooth_box(smooth_box(speech_mask, 4), 4)

    gain_ctrl = [0.0] * m
    voice_lin = 10 ** (TARGET_VOICE_DB / 20.0)
    gap_lin = 10 ** (TARGET_GAP_DB / 20.0)
    for k in range(m):
        t = (k + 0.5) * 0.05
        tgt_db = TARGET_GAP_DB + speech_env[k] * (TARGET_VOICE_DB - TARGET_GAP_DB)
        # Keep dusk->day transition (16.5s-19.6s) subtly softer so Lily's entrance pops
        if 16.5 <= t <= 19.6:
            # Smooth dip down to -6 dB at 18.0s
            dip = 0.5 * (1.0 - math.cos(2.0 * math.pi * (t - 16.5) / 3.1))
            tgt_db -= 6.0 * dip
        tgt_lin = 10 ** (tgt_db / 20.0)
        # Avoid boosting near-silence noise floor at very end (118.5s+)
        cur = max(contour[k], 0.03)
        g = tgt_lin / cur
        # Cap section boost/cut to keep musical integrity natural
        g = max(0.18, min(1.35, g))
        # Fade-in first 0.6s, clean cosine fade-out last 1.2s
        if t < 0.6:
            g *= 0.5 * (1.0 - math.cos(math.pi * t / 0.6))
        elif t > TOTAL_SEC - 1.2:
            rem = max(0.0, (TOTAL_SEC - t) / 1.2)
            g *= 0.5 * (1.0 - math.cos(math.pi * rem))
        gain_ctrl[k] = g

    # Interpolate sample-by-sample and soft-limit at -1.5 dBFS
    ceil_lin = 10 ** (-1.5 / 20.0)
    pcm = bytearray(n_frames * 4)
    sq_sum = 0.0
    peak = 0.0
    for i in range(n_frames):
        pos = i / hop
        k0 = min(m - 1, int(pos))
        k1 = min(m - 1, k0 + 1)
        frac = pos - k0
        g = gain_ctrl[k0] + frac * (gain_ctrl[k1] - gain_ctrl[k0])
        lv = left[i] * g
        rv = right[i] * g
        if abs(lv) > ceil_lin:
            lv = math.copysign(ceil_lin * math.tanh(abs(lv) / ceil_lin), lv)
        if abs(rv) > ceil_lin:
            rv = math.copysign(ceil_lin * math.tanh(abs(rv) / ceil_lin), rv)
        sq_sum += 0.5 * (lv * lv + rv * rv)
        peak = max(peak, abs(lv), abs(rv))
        li = max(-32768, min(32767, int(round(lv * 32767.0))))
        ri = max(-32768, min(32767, int(round(rv * 32767.0))))
        struct.pack_into("<hh", pcm, i * 4, li, ri)

    with wave.open(OUT, "wb") as w:
        w.setnchannels(2)
        w.setsampwidth(2)
        w.setframerate(SR)
        w.writeframes(pcm)

    rms_db = 20.0 * math.log10(math.sqrt(sq_sum / n_frames) + 1e-12)
    pk_db = 20.0 * math.log10(peak + 1e-12)
    print(f"Wrote {os.path.relpath(OUT, KIT)} ({n_frames / SR:.2f}s, {SR}Hz 2ch): RMS={rms_db:.1f} dBFS, Peak={pk_db:.1f} dBFS")


if __name__ == "__main__":
    main()
