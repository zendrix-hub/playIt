"""PlayIT product logo = the app's launcher icon, rebuilt in HD.
Frame, background and body are drawn from their measured geometry (exact anti-aliasing);
the face is the team's 2000 px Lily face art with its outline smoothed."""
import os, sys, numpy as np
from PIL import Image
from scipy import ndimage
HERE = os.path.dirname(os.path.abspath(__file__))
OUT = int(sys.argv[1]) if len(sys.argv) > 1 else 2000; k = OUT / 512.0   # design units: the 512 px app icon
yy, xx = np.mgrid[0:OUT, 0:OUT].astype(np.float32); X = (xx + .5) / k; Y = (yy + .5) / k   # pixel centres in design units
def rrect_cov(x0, y0, x1, y1, r):            # coverage of a rounded rectangle, from its signed distance (in pixels)
    cx, cy = (x0 + x1) / 2, (y0 + y1) / 2; hx, hy = (x1 - x0) / 2 - r, (y1 - y0) / 2 - r
    qx, qy = np.abs(X - cx) - hx, np.abs(Y - cy) - hy
    sd = np.hypot(np.maximum(qx, 0), np.maximum(qy, 0)) + np.minimum(np.maximum(qx, qy), 0) - r
    return np.clip(.5 - sd * k, 0, 1)
def box_cov(x0, x1):                          # vertical band coverage
    return np.clip(np.minimum(X - x0, x1 - X) * k + .5, 0, 1)
c = lambda *v: np.array(v, np.float32)
outer = rrect_cov(16, 16, 496, 496, 114); inner = rrect_cov(26, 26, 486, 486, 104)
# layers, painted back to front (straight colour + coverage)
img = np.zeros((OUT, OUT, 3), np.float32); A = np.zeros((OUT, OUT), np.float32)
def paint(col, cov):
    global img, A
    col = np.broadcast_to(col, img.shape); img = img * (1 - cov[..., None]) + col * cov[..., None]
paint(c(46, 56, 63), outer)
t = np.clip((Y - 26) / 460, 0, 1)[..., None]; cream = c(255, 249, 232) * (1 - t) + c(255, 241, 213) * t
paint(cream, inner)
body = box_cov(105, 406) * inner * np.clip((Y - 250) * k, 0, 1)
d = np.abs(X - 256); f = np.where(d < 4, 1, np.where(d > 20, 0, .5 * (1 + np.cos(np.pi * (d - 4) / 16))))[..., None]
orange = c(250, 126, 40) * (1 - f) + c(250, 165, 95) * f      # the face art's own body orange, so the hand-over is invisible
paint(orange, body); paint(c(230, 106, 32), box_cov(378, 406) * inner * np.clip((Y - 250) * k, 0, 1))
# the face art (2000 px), smoothed outline, placed by the eye measurements
fa = np.asarray(Image.open(os.path.join(HERE, "..", "..", "assets", "product_logo_face.png")).convert("RGBA")).astype(np.float32)
a0 = fa[..., 3] / 255
solid = ndimage.binary_erosion(a0 > .98, iterations=3)
w = ndimage.gaussian_filter(solid.astype(np.float32), 3) + 1e-6
rgbf = np.stack([ndimage.gaussian_filter(fa[..., i] * solid, 3) / w for i in range(3)], -1)
edge = ~solid; fa[..., :3][edge] = rgbf[edge]                       # a 3 px band inside the outline takes the colour from further in: no light rim
sm = ndimage.gaussian_filter(a0, 2.2); a1 = np.clip((sm - .5) / .18 + .5, 0, 1)   # smooth, evenly anti-aliased outline
# below the muzzle the body takes over: keep only the light muzzle there
S, TX, TY = 0.22989, 25.70, 46.24                                    # face px -> design units
fy = (np.arange(2000) * S + TY)[:, None]
lab, _ = ndimage.label((fa[..., 1] > 165) & (fa[..., 2] < 200) & (a0 > .5))      # light, not white: muzzle, eye rings, ear insides
win = lab[1150:1500, 750:1250]; ids, cnt = np.unique(win[win > 0], return_counts=True); mz = ids[cnt.argmax()]
muzzle = ndimage.binary_fill_holes(lab == mz)                                     # with the nose, mouth and tongue inside it
light = np.clip(ndimage.gaussian_filter(ndimage.binary_dilation(muzzle, iterations=2).astype(np.float32), 1.2), 0, 1)
a1 = np.where(fy > 352, a1 * light, a1)
face = np.dstack([fa[..., :3], a1 * 255]).clip(0, 255).astype(np.uint8)
sz = round(2000 * S * k); fimg = Image.fromarray(face, "RGBA")
# premultiply before resizing so edges do not darken or lighten
pm = np.asarray(fimg).astype(np.float32); pm[..., :3] *= pm[..., 3:4] / 255
R = np.stack([np.asarray(Image.fromarray(pm[..., i]).resize((sz, sz), Image.LANCZOS)) for i in range(4)], -1)
ox, oy = round(TX * k), round(TY * k)
reg = (slice(oy, oy + sz), slice(ox, ox + sz))
fa_a = np.clip(R[..., 3] / 255, 0, 1); fa_c = np.where(fa_a[..., None] > 1e-4, R[..., :3] / np.maximum(R[..., 3:4], 1e-4) * 255, 0)
sub = img[reg]; img[reg] = sub * (1 - fa_a[..., None]) + fa_c * fa_a[..., None]
out = np.dstack([img, outer * 255]).clip(0, 255).round().astype(np.uint8)
Image.fromarray(out, "RGBA").save(sys.argv[2] if len(sys.argv) > 2 else f"logo_{OUT}.png"); print("ok", OUT)
