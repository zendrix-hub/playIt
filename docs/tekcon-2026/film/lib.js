// PlayIT pitch film: the engine.
// Everything on screen is placed by ONE clock (seconds from 0 to 120). The same page can therefore be
// scrubbed, played with sound in a browser, or rendered frame by frame into the MP4.
const D = window.FILM_DATA, T = D.duration, NS = "http://www.w3.org/2000/svg";
const $ = s => document.querySelector(s);
const STEP = ["#3BA7D9", "#F7922F", "#2E9A3E", "#F2545B", "#FFC53D"];   // the app's four step colours, plus sun yellow
const INK = "#4A342E", TEAL = "#0B7F7A", CREAM = "#FFFDF6";

/* ---------- the script and its timing ---------- */
const LINE = Object.fromEntries(D.voice.map(v => [v.id, v]));
const norm = w => w.toLowerCase().replace(/[^a-z0-9.]/g, "").replace(/\.$/, "");
// cue("b3_02", "hears")  -> the second at which the narrator says that word (nth = 0 for the first match)
function cue(id, word, off = 0, nth = 0) {
  const hits = LINE[id].words.filter(x => norm(x.w).startsWith(norm(word)));
  if (!hits[nth]) { console.error("cue not found: " + id + " / " + word); return LINE[id].start + off; }
  return hits[nth].s + off;
}

/* ---------- keyframes ---------- */
// K(target, [[second, {css}, easing], ...]) can be called many times for one element; commit() merges them.
const REG = new Map();
function K(sel, frames) {
  const e = typeof sel === "string" ? $(sel) : sel;
  if (!e) { console.error("K: nothing matches " + sel); return; }
  if (!REG.has(e)) REG.set(e, []);
  REG.get(e).push(...frames);
  // Mia's arms exist twice (behind the book, and a forearm-and-hand copy in front of it): the copy gets the same moves
  const twin = e.id && /^(arm|fore|point|fing)[LR]$/.test(e.id) && document.getElementById(e.id + "f");
  if (twin) K(twin, frames);
}
function commit() {
  for (const [e, list] of REG) {
    const fr = list.map((f, i) => ({ t: Math.max(0, Math.min(T, f[0])), p: f[1], ease: f[2], i })).sort((a, b) => a.t - b.t || a.i - b.i);
    const props = new Set(); fr.forEach(f => Object.keys(f.p).forEach(k => props.add(k)));
    const head = {}, tail = {};
    for (const k of props) {            // hold the first value from 0 and the last value to the end
      const first = fr.find(f => k in f.p), last = [...fr].reverse().find(f => k in f.p);
      if (first.t > 0) head[k] = first.p[k];
      if (last.t < T) tail[k] = last.p[k];
    }
    const kf = fr.map(f => ({ offset: f.t / T, easing: f.ease || "ease-in-out", ...f.p }));
    if (Object.keys(head).length) kf.unshift({ offset: 0, easing: "linear", ...head });
    if (Object.keys(tail).length) kf.push({ offset: 1, ...tail });
    e.animate(kf, { duration: T * 1000, fill: "both" });
  }
}
const show = (sel, a, b, max = 1, fade = .25) => K(sel, [[Math.max(0, a - fade), { opacity: 0 }], [a, { opacity: max }], [b, { opacity: max }], [b + fade, { opacity: 0 }]]);
const pop = (sel, a, b) => K(sel, [
  [a - .01, { opacity: 0, transform: "scale(.2)" }], [a + .26, { opacity: 1, transform: "scale(1.14)" }, "ease-out"],
  [a + .42, { opacity: 1, transform: "scale(1)" }], [b, { opacity: 1, transform: "scale(1)" }], [b + .28, { opacity: 0, transform: "scale(.5)" }]]);
const R = (x, y, d) => ({ transform: `translate(${x}px,${y}px) rotate(${d}deg)` });
function el(tag, attrs, parent) { const e = document.createElementNS(NS, tag); for (const k in attrs) e.setAttribute(k, attrs[k]); parent.appendChild(e); return e; }
const UI = html => $("#ui").insertAdjacentHTML("beforeend", html);          // drawn over the camera, never moves with it
const WORLD = html => $("#cam").insertAdjacentHTML("beforeend", html);      // drawn in the scene, moves with the camera
// a mouth that opens and shuts between a and b (shows `sel`, the talking drawing, on and off)
function flap(sel, a, b, step = .13) { const f = [[0, { opacity: 0 }, "steps(1)"]]; let on = true; for (let t = a; t < b; t += step) { f.push([t, { opacity: on ? 1 : 0 }, "steps(1)"]); on = !on; } f.push([b, { opacity: 0 }, "steps(1)"]); K(sel, f); }

/* ---------- the first 28 seconds were designed on an 18-second mock-up clock; this maps that clock onto the voice ---------- */
const WARP = [[0, 0], [5, 5.4], [5.1, 5.58], [6.9, 6.95], [7.0, 6.97], [7.3, cue("b1_03", "teacher")], [8.5, cue("b1_03", "nanay")], [9.8, cue("b1_03", "signal")],
  // every point below follows the voice, so a new recording moves the picture with it
  [10.9, LINE.b1_03.end + .03], [11.3, cue("b1_04", "So") + .09], [12.4, cue("b1_04", "book") + .06], [12.69, LINE.b2_01.start - .85], [12.7, LINE.b2_01.start - .75],
  [13.5, cue("b2_01", "small") - .21], [14.05, cue("b2_01", "peeks") + .25], [14.2, cue("b2_01", "in") + .35], [14.6, LINE.b2_02.start + .12], [14.95, LINE.b2_02.start + .41],
  [15.4, LINE.b2_02.start + .51], [17.6, LINE.b2_03.start - .08]];
function warp(t) { for (let i = 1; i < WARP.length; i++) if (t <= WARP[i][0]) { const [a, A2] = WARP[i - 1], [b, B2] = WARP[i]; return A2 + (B2 - A2) * (t - a) / (b - a); } return WARP[WARP.length - 1][1]; }
const KM = (sel, frames) => K(sel, frames.filter(f => f[0] <= 17.6001).map(f => [warp(f[0]), f[1], f[2]]));
const showM = (sel, a, b, max = 1) => KM(sel, [[Math.max(0, a - .25), { opacity: 0 }], [a, { opacity: max }], [b, { opacity: max }], [b + .25, { opacity: 0 }]]);
const popM = (sel, a, b) => KM(sel, [
  [a - .01, { opacity: 0, transform: "scale(.2)" }], [a + .28, { opacity: 1, transform: "scale(1.12)" }, "ease-out"],
  [a + .45, { opacity: 1, transform: "scale(1)" }], [b, { opacity: 1, transform: "scale(1)" }], [b + .3, { opacity: 0, transform: "scale(.6)" }]]);

/* ---------- Mia's arms ----------
   Each arm has two parts: #armL / #armR turn at the shoulder, #foreL / #foreR at the elbow. A pose is { a, b }: the two angles in
   degrees. At rest (0, 0) the forearms lie on the table with a hand on each side of the book.
   reach() works out the pose that puts the hand, or the fingertip when pointing, on a spot in the scene. */
const ARM = { L: { P: [470, 452], E: [440, 480], W: [408.7, 490.4] }, R: { P: [570, 452], E: [600, 480], W: [631.3, 490.4] }, hand: 47, tip: 61 };
const REST = { a: 0, b: 0 };
function reach(s, x, y, o = {}) {       // o.point: measure to the fingertip; o.flip: the other elbow position; o.turn: whole turns added at the elbow
  const A = ARM[s], [px, py] = A.P, deg = r => r * 180 / Math.PI, wrap = v => ((v + 180) % 360 + 360) % 360 - 180;
  const L1 = Math.hypot(A.E[0] - px, A.E[1] - py), L2 = o.point ? ARM.tip : ARM.hand;
  const th0 = Math.atan2(A.E[1] - py, A.E[0] - px), f0 = Math.atan2(A.W[1] - A.E[1], A.W[0] - A.E[0]);
  const d = Math.max(Math.abs(L1 - L2) + .5, Math.min(L1 + L2 - .5, Math.hypot(x - px, y - py)));
  const phi = Math.atan2(y - py, x - px), al = Math.acos((L1 * L1 + d * d - L2 * L2) / (2 * L1 * d));
  let th = [phi + al, phi - al].sort((p, q) => Math.sin(q) - Math.sin(p))[o.flip ? 1 : 0];      // by default the lower elbow
  const ex = px + L1 * Math.cos(th), ey = py + L1 * Math.sin(th), f = Math.atan2(y - ey, x - ex);
  const a = wrap(deg(th - th0)), b = wrap(deg(f - f0) - a) + 360 * (o.turn || 0);
  return { a: +a.toFixed(1), b: +b.toFixed(1) };
}
const LR = (s, a, b) => s === "L" ? { a, b } : { a: -a, b: -b };       // a pose given for the left arm, mirrored for the right
// armK("L", [[second, pose, easing], ...]); km = true when the seconds are on the mock-up clock of the first 28 seconds
function armK(s, frames, km) {
  const f = km ? KM : K;
  f("#arm" + s, frames.map(([t, p, e]) => [t, R(0, 0, p.a), e])); f("#fore" + s, frames.map(([t, p, e]) => [t, R(0, 0, p.b), e]));
  f("#tsh" + s, frames.map(([t, p]) => [t, { opacity: Math.abs(p.a) + Math.abs(p.b) < 4 ? 1 : 0 }]));      // the hand's shadow on the table
}
const sw = (f, sel, a, b, on, off, d) => f(sel, [[a - d, { opacity: off }], [a, { opacity: on }], [b, { opacity: on }], [b + d, { opacity: off }]]);
function pointing(s, a, b, km) { const f = km ? KM : K; sw(f, "#point" + s, a, b, 1, 0, .08); sw(f, "#fing" + s, a, b, 0, 1, .08); }   // index finger out between a and b
function front(s, a, b, km) { sw(km ? KM : K, "#front" + s, a, b, 1, 0, .12); }       // the hand is in front of the book between a and b
function oscM(sel, fn, step = .12) { const f = []; for (let t = 0; t <= 17.6; t += step) f.push([t, { transform: fn(t) }, "linear"]); KM(sel, f); }

/* ---------- sound ---------- */
const AUDIO = [];                                    // {file, at, vol}; the renderer reads this list too
const S = (file, at, vol = .3) => AUDIO.push({ file: "sfx/" + file, at, vol });
D.voice.filter(v => v.file).forEach(v => AUDIO.push({ file: v.file, at: v.start, vol: 1 }));
AUDIO.push({ file: "music/bed.wav", at: 0, vol: .12 });
window.FILM_AUDIO = AUDIO;

/* ---------- sing-along captions, built from the script; each word lights up when it is spoken ---------- */
function captions() {
  const stage = $("#stage"), phrases = [];
  for (const v of D.voice) {
    let cur = [];
    v.words.forEach((x, i) => {
      cur.push(x);
      const left = v.words.length - 1 - i, endS = /[.?!:]["”']?$/.test(x.w), comma = /,$/.test(x.w);
      if (i === v.words.length - 1 || ((endS || (comma && cur.length >= 4) || cur.length >= 7) && left >= 2)) { phrases.push({ v, words: cur }); cur = []; }
    });
  }
  phrases.forEach((ph, n) => {
    const a = ph.words[0].s - .08, next = phrases[n + 1], b = Math.min(ph.words[ph.words.length - 1].e + .3, next ? next.words[0].s - .12 : T);
    // a scene may place one phrase somewhere else: window.CAP_PLACE(phrase) -> { cls: extra CSS class, until: second it stays up to }
    const place = window.CAP_PLACE ? window.CAP_PLACE(ph) : null;
    const d = document.createElement("div"); d.className = "cap" + (place ? " " + place.cls : ""); stage.appendChild(d);
    if (ph.v.speaker === "lily") { const nm = document.createElement("b"); nm.textContent = "Lily:"; d.appendChild(nm); }
    const lo = { color: "#fff", transform: "translateY(0%) scale(1)" }, hi = { color: "#FFD23F", transform: "translateY(-9%) scale(1.14)" };
    ph.words.forEach(x => {
      const sp = document.createElement("span"); sp.className = "w"; sp.textContent = x.w; d.appendChild(sp);
      K(sp, [[x.s - .03, lo], [x.s + .05, hi, "ease-out"], [Math.max(x.s + .06, x.e - .04), hi], [x.e + .08, lo]]);
    });
    const end = place && place.until ? place.until : b;
    K(d, [[Math.max(0, a - .15), { opacity: 0 }], [a, { opacity: 1 }], [end, { opacity: 1 }], [end + .12, { opacity: 0 }]]);
  });
}

/* ---------- the clock ---------- */
let ANIMS = [];
window.seek = t => { const ms = Math.max(0, Math.min(T, t)) * 1000; for (const a of ANIMS) a.currentTime = ms; window.FILM_T = t; for (const f of window.ON_SEEK || []) f(t); };
async function boot() {
  captions();
  await Promise.all(["800 40px Sniglet", "400 40px Sniglet", "700 40px Fredoka", "600 40px Fredoka"].map(f => document.fonts.load(f)));
  await Promise.all([...document.querySelectorAll("image")].map(im => new Promise(ok => { const i = new Image(); i.onload = i.onerror = ok; i.src = im.getAttribute("href"); })));
  commit();
  document.body.getBoundingClientRect();
  ANIMS = document.getAnimations(); ANIMS.forEach(a => a.pause());
  const q = new URLSearchParams(location.search);
  const vb = q.get("vb"); if (vb) $("#stage svg").setAttribute("viewBox", vb.split(",").join(" "));
  window.seek(parseFloat(q.get("t") || "0"));
  window.FILM_READY = true;
  if (q.has("embed")) { document.body.classList.add("embed"); return; }
  player();
}

/* ---------- playing it in a browser, with sound ---------- */
function player() {
  const play = $("#play"), scrub = $("#scrub"), clock = $("#clock");
  const tracks = AUDIO.map(x => { const a = new Audio(x.file); a.preload = "auto"; a.volume = x.vol; return { a, at: x.at }; });
  let playing = false, t = window.FILM_T || 0, last = 0;
  const sound = () => tracks.forEach(x => {
    const lt = t - x.at, dur = x.a.duration || 1e9;
    if (playing && lt >= 0 && lt < dur) { if (x.a.paused) { x.a.currentTime = lt; x.a.play().catch(() => {}); } else if (Math.abs(x.a.currentTime - lt) > .25) x.a.currentTime = lt; }
    else if (!x.a.paused) x.a.pause();
  });
  const set = p => { playing = p; play.textContent = p ? "Pause" : "Play"; last = performance.now(); sound(); };
  play.onclick = () => { if (t >= T) t = 0; set(!playing); };
  scrub.max = T; scrub.oninput = () => { t = +scrub.value; window.seek(t); tracks.forEach(x => x.a.pause()); sound(); };
  document.querySelectorAll("[data-go]").forEach(b => b.onclick = () => { t = +b.dataset.go; window.seek(t); tracks.forEach(x => x.a.pause()); set(true); });
  (function tick(now) {
    if (playing) { t += (now - last) / 1000; last = now; if (t >= T) { t = T; set(false); } window.seek(t); sound(); }
    if (!scrub.matches(":active")) scrub.value = t;
    clock.textContent = Math.floor(t / 60) + ":" + String(Math.floor(t % 60)).padStart(2, "0") + " / 2:00";
    requestAnimationFrame(tick);
  })(performance.now());
}
