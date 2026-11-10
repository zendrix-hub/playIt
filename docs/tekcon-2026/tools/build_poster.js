// Builds the editable PlayIT research poster (22.5 x 28.5 in, portrait).
// Wording comes from ../content.json, numbers from ../round2_data.json,
// pictures from ../assets/. Do not type text or numbers into this file.
const PptxGenJS = require("pptxgenjs");
const { ROOT, content: c, projected, get, show, fill, fs, path } = require("./lib");
let sizeOf = null;
try { sizeOf = require("image-size"); sizeOf = sizeOf.default || sizeOf.imageSize || sizeOf; } catch (e) { /* stretch-to-box fallback */ }

// PREVIEW_FONT=Calibri builds a throwaway copy for machines without the real fonts.
const PREVIEW = process.env.PREVIEW_FONT;
const BODY = PREVIEW || "Lexend", TITLE = PREVIEW || "Sniglet";

// Colour system: teal leads, sunflower highlights, cream cards.
// The four step colours appear ONLY on the four steps, as in the app.
const C = {
  teal: "0B7F7A", tealPale: "D6EEEC", sun: "FFC53D", sand: "FFF2DC", skyPale: "E1F3FA", card: "FFFDF8",
  cardLine: "EAD9BF", brown: "4A2E18", text: "3B2416", grey: "7A6A5E", white: "FFFFFF", off: "E4DCD0",
  slot: "F7922F", steps: ["3BA7D9", "F7922F", "2E9A3E", "F2545B"],
};
const W = 22.5, H = 28.5, M = 0.75, CW = W - 2 * M, GAP = 0.3;

const pptx = new PptxGenJS();
pptx.defineLayout({ name: "CARTOLINA", width: W, height: H });
pptx.layout = "CARTOLINA";
pptx.title = "PlayIT Research Poster";
const s = pptx.addSlide();
s.background = { color: C.sand };
const RR = pptx.ShapeType.roundRect, RECT = pptx.ShapeType.rect, OVAL = pptx.ShapeType.ellipse;

const box = (x, y, w, h, fill, o = {}) => {
  const shape = o.shape || RR;
  const opts = { x, y, w, h, fill: { color: fill } };
  if (shape === RR) opts.rectRadius = o.r ?? 0.3; // only rounded rectangles take a radius
  if (o.line) { opts.line = { color: o.line, width: o.lw || 2 }; if (o.dash) opts.line.dashType = o.dash; }
  s.addShape(shape, opts);
};
const txt = (t, x, y, w, h, o = {}) =>
  s.addText(t, { x, y, w, h, fontFace: o.face || BODY, fontSize: o.size || 24, color: o.color || C.text,
    bold: !!o.bold, italic: !!o.italic, align: o.align || "left", valign: o.valign || "top", margin: 0,
    lineSpacingMultiple: o.ls || 1.05 });
const card = (x, y, w, h) => box(x, y, w, h, C.card, { line: C.cardLine });
const heading = (label, x, y, w, size = 46) => {
  box(x, y + 0.08, 0.26, 0.8, C.sun, { r: 0.13 });
  txt(label, x + 0.48, y, w - 0.48, 0.95, { face: TITLE, size, color: C.brown, bold: true, valign: "middle" });
};
// A picture slot. If assets/<file> exists it is placed, fitted and centred; otherwise a dashed box says what goes there.
const slot = (file, what, x, y, w, h) => {
  const p = path.join(ROOT, "assets", file);
  if (fs.existsSync(p)) {
    let iw = w, ih = h;
    if (sizeOf) {
      try { const d = sizeOf(p); const k = Math.min(w / d.width, h / d.height); iw = d.width * k; ih = d.height * k; } catch (e) { /* keep box size */ }
    }
    s.addImage({ path: p, x: x + (w - iw) / 2, y: y + (h - ih) / 2, w: iw, h: ih });
    return true;
  }
  box(x, y, w, h, C.white, { line: C.slot, lw: 3, dash: "dash", r: 0.2 });
  txt(`${what}\nassets/${file}`, x + 0.1, y + 0.1, w - 0.2, h - 0.2, { size: 14, color: C.slot, align: "center", valign: "middle", bold: true });
  return false;
};

// ---------- 1. Title band, with Lily as the hero ----------
const y1 = 0.5, h1 = 4.4;
box(0, 0, W, y1 + h1 + 0.15, C.skyPale, { shape: RECT });
box(M, y1, CW, h1, C.teal, { r: 0.5 });
const logoCols = [C.sun, C.white, C.sun, C.white, C.sun, C.white];
s.addText([...c.logo].map((ch, i) => ({ text: ch, options: { color: logoCols[i % logoCols.length] } })),
  { x: M + 0.6, y: y1 + 0.1, w: 8.6, h: 2.0, fontFace: TITLE, fontSize: 130, bold: true, margin: 0, valign: "middle" });
txt(c.tagline, M + 0.6, y1 + 2.1, 9.4, 0.6, { face: TITLE, size: 32, color: C.sun, valign: "middle" });
txt(c.full_title, M + 0.6, y1 + 2.78, 9.4, 0.95, { size: 21, color: C.white, bold: true });
txt(c.org_line, M + 0.6, y1 + 3.78, 9.4, 0.45, { size: 18, color: C.white, valign: "middle" });
// headline result bubble
const bx = M + 10.3, bw = 4.2;
box(bx, y1 + 0.55, bw, 2.75, C.sun, { r: 0.4 });
txt(`${show(c.headline.value)} of ${show(c.headline.of)}`, bx + 0.2, y1 + 0.7, bw - 0.4, 1.2,
  { face: TITLE, size: 62, bold: true, color: C.brown, align: "center", valign: "middle" });
txt(c.headline.label, bx + 0.25, y1 + 1.9, bw - 0.5, 1.2, { size: 24, bold: true, color: C.brown, align: "center", valign: "middle" });
slot(c.lily_asset, "LILY (hero)\nin-app flat style,\ntransparent PNG", M + 14.9, y1 + 0.2, 5.7, 4.0);

// ---------- 2. Problem + Solution ----------
const y2 = y1 + h1 + GAP, h2 = 4.2, half = (CW - GAP) / 2;
card(M, y2, half, h2);
heading(c.problem.heading, M + 0.4, y2 + 0.22, half - 0.8);
c.problem.items.forEach((it, i) => {
  const yy = y2 + 1.32 + i * 0.94;
  box(M + 0.45, yy + 0.04, 0.78, 0.78, C.teal, { shape: OVAL });
  txt(String(i + 1), M + 0.45, yy + 0.04, 0.78, 0.78, { face: TITLE, size: 32, color: C.white, bold: true, align: "center", valign: "middle" });
  txt(it.title, M + 1.5, yy, half - 1.9, 0.44, { size: 26, bold: true, color: C.brown, valign: "middle" });
  txt(it.text, M + 1.5, yy + 0.44, half - 1.9, 0.44, { size: 24, valign: "middle" });
});
const sx = M + half + GAP;
card(sx, y2, half, h2);
heading(c.solution.heading, sx + 0.4, y2 + 0.22, half - 0.8);
txt(c.solution.text, sx + 0.45, y2 + 1.32, half - 0.9, 1.75, { size: 26, bold: true, color: C.brown, ls: 1.1 });
const bdw = (half - 0.9 - 2 * 0.2) / 3;
c.solution.badges.forEach((b, i) => {
  box(sx + 0.45 + i * (bdw + 0.2), y2 + 3.25, bdw, 0.65, C.teal, { r: 0.32 });
  txt(b, sx + 0.45 + i * (bdw + 0.2), y2 + 3.25, bdw, 0.65, { size: 21, bold: true, color: C.white, align: "center", valign: "middle" });
});

// ---------- 3. How it works ----------
const y3 = y2 + h2 + GAP, h3 = 5.7;
card(M, y3, CW, h3);
heading(c.how.heading, M + 0.4, y3 + 0.22, 8);
txt(c.how.subline, M + 8.4, y3 + 0.22, 8.0, 0.95, { size: 24, color: C.grey, valign: "middle" });
const colW = 3.2, sw = 1.9, sh = 2.7, sy = y3 + 1.35;
c.how.steps.forEach((st, i) => {
  const cx = M + 0.45 + i * colW;
  slot(st.asset, "fresh\nscreenshot", cx + (colW - sw) / 2 - 0.15, sy, sw, sh);
  box(cx, sy + sh + 0.12, colW - 0.3, 0.56, C.steps[i], { r: 0.28 });
  txt(st.label, cx, sy + sh + 0.12, colW - 0.3, 0.56, { size: 24, bold: true, color: C.white, align: "center", valign: "middle" });
  if (i < c.how.steps.length - 1)
    txt("›", cx + colW - 0.75, sy + 0.95, 0.6, 0.8, { face: TITLE, size: 44, color: C.brown, bold: true, align: "center", valign: "middle" });
});
// parents: visibly separate from the child's four steps
const px = M + 0.45 + 4 * colW + 0.2;
box(px - 0.05, sy, 0.05, sh + 0.68, C.cardLine, { shape: RECT });
slot(c.how.parent.asset, "fresh\nscreenshot", px + 0.3, sy, sw, sh);
txt(c.how.parent.title, px + 2.45, sy + 0.15, M + CW - 0.45 - (px + 2.45), 0.55, { size: 28, bold: true, color: C.teal, valign: "middle" });
txt(c.how.parent.text, px + 2.45, sy + 0.8, M + CW - 0.45 - (px + 2.45), 2.4, { size: 24, ls: 1.1 });
const tw = (CW - 0.9 - 3 * 0.2) / 4;
c.how.tech.forEach((t, i) => {
  box(M + 0.45 + i * (tw + 0.2), y3 + h3 - 0.85, tw, 0.55, C.tealPale, { r: 0.27 });
  txt(t, M + 0.45 + i * (tw + 0.2), y3 + h3 - 0.85, tw, 0.55, { size: 20, bold: true, color: C.teal, align: "center", valign: "middle" });
});

// ---------- 4. Results: pictures, not just numbers ----------
const y4 = y3 + h3 + GAP, h4 = 7.1, r = c.results;
card(M, y4, CW, h4);
heading(r.heading, M + 0.4, y4 + 0.22, 11.5);
if (projected) {
  box(M + 12.2, y4 + 0.38, CW - 12.65, 0.62, C.sun, { r: 0.31, line: C.brown, lw: 1.5 });
  txt("PROJECTED — replace with real results before printing", M + 12.2, y4 + 0.38, CW - 12.65, 0.62,
    { size: 18, bold: true, color: C.brown, align: "center", valign: "middle" });
}
txt(fill(r.subline), M + 0.45, y4 + 1.25, CW - 0.9, 0.5, { size: 24, color: C.grey, valign: "middle" });
// who said what: the children are the users; parents and teachers judge it for them
[[r.users_heading, M + 0.45, 9.85], [r.adults_heading, M + 10.9, CW - 10.9 - 0.45]].forEach(([t, x, w]) => {
  box(x, y4 + 1.85, w, 0.5, C.tealPale, { r: 0.25 });
  txt(t, x + 0.25, y4 + 1.85, w - 0.5, 0.5, { size: 20, bold: true, color: C.teal, valign: "middle" });
});
const ry = y4 + 2.45, rh = 1.18;
// left: people counts as rows of dots
r.counts.forEach((it, i) => {
  const yy = ry + i * rh, v = Number(get(it.value)), n = Number(get(it.of));
  txt(`${v} of ${n}`, M + 0.45, yy, 3.4, 1.1, { face: TITLE, size: 46, bold: true, color: C.teal, valign: "middle" });
  const d = Math.min(0.4, (6.1 - (n - 1) * 0.1) / n);
  for (let k = 0; k < n; k++)
    box(M + 3.95 + k * (d + 0.1), yy + 0.1, d, d, k < v ? C.teal : C.off, { shape: OVAL });
  txt(it.label, M + 3.95, yy + 0.58, 6.3, 0.5, { size: 24, valign: "middle" });
});
// right: ratings as bars on the real 1-to-5 scale
const rx = M + 10.9, barW = 6.9;
r.ratings.forEach((it, i) => {
  const yy = ry + i * rh, v = Number(get(it.value));
  txt(fill(it.label) + (it.goal ? `, goal ${it.goal.toFixed(1)}` : ""), rx, yy, CW - 10.9 - 0.45, 0.5, { size: 22, valign: "middle" });
  box(rx, yy + 0.58, barW, 0.5, C.off, { r: 0.25 });
  box(rx, yy + 0.58, Math.max(0.5, barW * (v - 1) / 4), 0.5, C.teal, { r: 0.25 });
  if (it.goal) box(rx + barW * (it.goal - 1) / 4 - 0.03, yy + 0.5, 0.06, 0.66, C.brown, { shape: RECT });
  txt(`${v.toFixed(1)} / 5`, rx + barW + 0.2, yy + 0.4, 2.6, 0.85, { face: TITLE, size: 40, bold: true, color: C.teal, valign: "middle" });
});
txt([{ text: fill(r.quote), options: { bold: true, italic: true, color: C.brown } },
     { text: "  — " + fill(r.quote_source), options: { color: C.grey } }],
  M + 0.45, y4 + 6.0, CW - 0.9, 0.55, { size: 24, valign: "middle" });
txt(fill(r.footer), M + 0.45, y4 + 6.55, CW - 0.9, 0.45, { size: 20, color: C.grey, valign: "middle" });

// ---------- 5. Impact · Team · QR ----------
const y5 = y4 + h4 + GAP, h5 = 3.1, iw = 6.6, qw = 5.2, tmw = CW - iw - qw - 2 * GAP;
card(M, y5, iw, h5);
heading(c.impact.heading, M + 0.4, y5 + 0.15, iw - 0.8, 38);
txt(c.impact.points.map((p, i) => ({ text: p, options: { breakLine: i < c.impact.points.length - 1, bullet: true } })),
  M + 0.45, y5 + 1.1, iw - 0.8, 1.9, { size: 22, ls: 1.08 });
const tx = M + iw + GAP;
card(tx, y5, tmw, h5);
slot(c.team.asset, "Pixar-style\ngroup image", tx + 0.3, y5 + 0.3, 4.2, 2.5);
txt(c.team.heading, tx + 4.75, y5 + 0.25, tmw - 5.0, 0.55, { face: TITLE, size: 30, bold: true, color: C.brown, valign: "middle" });
txt([...c.team.members, c.team.adviser].map((m, i, a) => ({ text: m, options: { breakLine: i < a.length - 1, bold: i === 0, color: i === a.length - 1 ? C.grey : C.text } })),
  tx + 4.75, y5 + 0.85, tmw - 5.0, 2.1, { size: 18, ls: 1.08 });
const qx = tx + tmw + GAP;
box(qx, y5, qw, h5, C.teal, { r: 0.35 });
slot(c.qr.asset, "QR code\n(square)", qx + 0.3, y5 + 0.35, 2.4, 2.4);
// Optional second code: appears only when assets/<app_qr.asset> exists. The required video QR is unchanged.
const appQr = c.app_qr && fs.existsSync(path.join(ROOT, "assets", c.app_qr.asset));
txt(c.qr.label, qx + 2.95, y5 + 0.3, qw - 3.2, appQr ? 1.25 : 2.5, { size: appQr ? 17 : 22, bold: true, color: C.white, valign: "middle" });
if (appQr) {
  slot(c.app_qr.asset, "", qx + 2.95, y5 + 1.6, 1.15, 1.15);
  txt(c.app_qr.label, qx + 4.15, y5 + 1.6, qw - 4.3, 1.15, { size: 12, bold: true, color: C.white, valign: "middle" });
}

// ---------- 6. References ----------
const y6 = y5 + h5 + GAP;
txt("Selected References", M + 0.1, y6, 6, 0.4, { size: 20, bold: true, color: C.brown, valign: "middle" });
const refCol = (CW - 0.6) / 2, perCol = Math.ceil(c.references.length / 2);
[c.references.slice(0, perCol), c.references.slice(perCol)].forEach((col, i) =>
  txt(col.map((t, k) => ({ text: t, options: { breakLine: k < col.length - 1 } })),
    M + 0.1 + i * (refCol + 0.4), y6 + 0.45, refCol, H - y6 - 0.75, { size: 18, ls: 1.02 }));

const outName = PREVIEW ? path.join(__dirname, "_preview.pptx") : path.join(ROOT, "02_Research_Poster", `${c.team_code}_Research_Poster_Source.pptx`);
pptx.writeFile({ fileName: outName }).then(() =>
  console.log(`Poster written: ${path.relative(ROOT, outName)}  [Round 2 figures: ${projected ? "PROJECTED" : "ACTUAL"}]  bottom of layout at ${(y6 + 0.45).toFixed(2)} in`));
