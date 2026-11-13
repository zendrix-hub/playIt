"""Tidy one voice take: shorten over-long pauses and bring it to the same loudness as the other lines.

    python tidy_take.py ../voice_takes/b3_03_t2.wav ../voice_new/b3_03.wav            pauses longer than 0.55 s are cut to 0.55 s, level -20 dB
    python tidy_take.py in.wav out.wav --pause 0.45 --level -21

Only silence is removed (with a short crossfade inside the silence); the words are untouched. Use it when the clearest take
runs a little too long for its slot. Run hear.py on the result, then align_new.py.
"""
import sys, numpy as np, soundfile as sf
args = sys.argv[1:]; src, dst = args[0], args[1]; keep, target = .55, -20.0
if "--pause" in args: keep = float(args[args.index("--pause") + 1])
if "--level" in args: target = float(args[args.index("--level") + 1])
rms_db = lambda x: 20 * np.log10(np.sqrt(np.mean(x ** 2)) + 1e-12)
x, sr = sf.read(src, dtype="float32")
if x.ndim > 1: x = x.mean(axis=1)
hop = int(sr * .01); n = len(x) // hop
e = np.array([np.sqrt(np.mean(x[i * hop:(i + 1) * hop] ** 2)) for i in range(n)]); quiet = e < 10 ** (-42 / 20)
runs, i = [], 0
while i < n:
    if quiet[i]:
        j = i
        while j < n and quiet[j]: j += 1
        if (j - i) * .01 >= .30 and i > 0 and j < n: runs.append((i * hop, j * hop))
        i = j
    else: i += 1
out, pos, xf = [], 0, int(sr * .015)
for a, b in runs:
    dur = (b - a) / sr
    if dur > keep:
        drop = int((dur - keep) * sr); mid = (a + b) // 2; c0, c1 = mid - drop // 2, mid + drop - drop // 2
        left = x[pos:c0].copy(); w = np.linspace(0, 1, xf); left[-xf:] = left[-xf:] * (1 - w) + x[c1 - xf:c1] * w
        out.append(left); pos = c1; print(f"pause at {a / sr:.2f}s: {dur:.2f}s -> {keep:.2f}s")
    else: print(f"pause at {a / sr:.2f}s: {dur:.2f}s kept")
out.append(x[pos:]); y = np.concatenate(out)
g = 10 ** ((target - rms_db(y)) / 20); y = y * g; c = .80; over = np.abs(y) > c        # soft knee above 0.80, ceiling 0.89, so a loud peak never clips
y[over] = np.sign(y[over]) * (c + (.89 - c) * np.tanh((np.abs(y[over]) - c) / (.89 - c)))
sf.write(dst, y, sr)
print(f"{len(x) / sr:.2f}s -> {len(y) / sr:.2f}s, {rms_db(y):.1f} dB, peak {20 * np.log10(np.abs(y).max()):.1f} dB  ->  {dst}")
