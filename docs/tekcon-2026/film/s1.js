// Beats 1 and 2a (0:00 to 0:28): Mia is stuck, Lily arrives.
// These keyframes were designed on an 18-second mock-up clock; KM() in lib.js stretches that clock onto the real voice.
// Where Lily stands, as a shift from her drawing's home position: beside the small phone, and beside the big phone in the demo.
// Both keep her whole body clear of the phone, so the two never overlap.
const LX1 = 166, LX2 = 250;
/* ---- scenery drawn by formula: hills, banderitas ---- */
[[90, 330, 150, "#DCC09C", "#EBD6B8"], [860, 330, 128, "#DCC09C", "#EBD6B8"], [290, 290, 122, "#C49A6C", "#D8B48A"], [1190, 320, 126, "#C49A6C", "#D8B48A"],
 [480, 270, 104, "#A9805A", "#C19A72"], [705, 250, 96, "#B58B62", "#CBA57C"], [1020, 300, 138, "#A9805A", "#C19A72"]].forEach(([x, w, h, c, hi]) => {
  const g = $("#hills"), l = x - w / 2, r = x + w / 2;
  el("path", { d: `M${l} 542 C${l + w * .12} ${542 - h * 1.32} ${r - w * .12} ${542 - h * 1.32} ${r} 542Z`, fill: c }, g);
  el("path", { d: `M${x - w * .27} ${542 - h * .6} C${x - w * .18} ${542 - h * .84} ${x - w * .05} ${542 - h * .93} ${x + w * .06} ${542 - h * .94}`, fill: "none", stroke: hi, "stroke-width": 11, "stroke-linecap": "round", opacity: .85 }, g);
});
const FLAG = [["#F2545B", "#D8434A"], ["#FFC53D", "#F2A91E"], ["#3BA7D9", "#2B8FC0"], ["#2E9A3E", "#23812F"], ["#F7922F", "#E27B1C"]];
[[50, 44], [130, 62], [212, 74], [296, 78], [380, 72], [462, 64], [546, 66], [630, 80], [716, 86], [802, 78], [886, 64], [970, 48], [1054, 36], [1140, 30], [1222, 28]].forEach(([x, y], i) => {
  const [c, d] = FLAG[i % 5], g = el("g", { class: "flag", style: `animation-duration:${1.05 + i % 3 * .22}s; animation-delay:-${i * .37}s` }, $("#flags"));
  el("path", { d: `M${x} ${y} h36 l-18 46Z`, fill: c, stroke: c, "stroke-width": 5, "stroke-linejoin": "round" }, g);
  el("path", { d: `M${x + 18} ${y} h18 l-18 46Z`, fill: d, stroke: d, "stroke-width": 5, "stroke-linejoin": "round" }, g);
  el("circle", { cx: x + 9, cy: y + 8, r: 3, fill: "#fff", opacity: .5 }, g);
});

/* ---- camera ---- */
KM("#cam", [
  [0, { transform: "translate(0px,0px) scale(1.03)" }], [5, { transform: "translate(22px,-6px) scale(1.17)" }], [6.9, { transform: "translate(22px,-8px) scale(1.2)" }],
  [7.5, { transform: "translate(0px,10px) scale(1.04)" }, "ease-in-out"], [11.2, { transform: "translate(0px,10px) scale(1.07)" }],
  [12.4, { transform: "translate(12px,-14px) scale(1.21)" }], [13.2, { transform: "translate(-108px,84px) scale(1.46)" }], [14.15, { transform: "translate(-114px,90px) scale(1.5)" }, "cubic-bezier(.3,1.15,.5,1)"],   // closer on the phone while the small Lily peeks
  [14.6, { transform: "translate(-10px,16px) scale(1.05)" }], [14.95, { transform: "translate(-10px,0px) scale(1.05)" }, "linear"],
  [15.0, { transform: "translate(-17px,6px) scale(1.05)" }, "linear"], [15.06, { transform: "translate(-4px,-5px) scale(1.05)" }, "linear"], [15.12, { transform: "translate(-14px,3px) scale(1.05)" }, "linear"], [15.18, { transform: "translate(-10px,0px) scale(1.05)" }],
  [17.5, { transform: "translate(-20px,0px) scale(1.06)" }], [18, { transform: "translate(0px,0px) scale(1.03)" }]]);

/* ---- fireflies at dusk, flowers and butterflies by day ---- */
[[150, 330], [250, 470], [640, 300], [900, 420], [1040, 330], [1130, 480], [760, 600], [330, 610], [980, 580], [560, 250]].forEach(([x, y], i) => {
  const g = el("g", { class: "ff", style: `animation-duration:${7 + i % 4 * 1.7}s, ${1.2 + i % 3 * .5}s; animation-delay:-${i * 1.3}s, -${i * .4}s` }, $("#fireflies"));
  el("circle", { cx: x, cy: y, r: 11, fill: "#FFF3A0", opacity: .22 }, g); el("circle", { cx: x, cy: y, r: 3.6, fill: "#FFFBD6" }, g);
});
KM("#fireflies", [[0, { opacity: 1 }], [14.2, { opacity: 1 }], [15, { opacity: 0 }], [17.7, { opacity: 0 }], [18, { opacity: 1 }]]);
[[150, 648], [250, 690], [1010, 646], [1110, 684], [960, 700], [70, 700], [1200, 650]].forEach(([x, y], i) => {
  const g = el("g", { id: "fl" + i, style: "transform-origin: 50% 100%" }, $("#flowers"));
  const c = ["#F2545B", "#FFC53D", "#3BA7D9", "#F7922F", "#F2545B", "#3BA7D9", "#F7922F"][i], cy = y - 46;
  el("path", { d: `M${x} ${y} V${cy}`, stroke: "#36A04B", "stroke-width": 6, "stroke-linecap": "round" }, g);
  el("ellipse", { cx: x - 11, cy: y - 12, rx: 12, ry: 6, fill: "#4CB85C", transform: `rotate(28 ${x - 11} ${y - 12})` }, g);
  el("ellipse", { cx: x + 11, cy: y - 16, rx: 12, ry: 6, fill: "#36A04B", transform: `rotate(-28 ${x + 11} ${y - 16})` }, g);
  for (let k = 0; k < 5; k++) { const px = x + 13 * Math.cos(k * 1.257 - 1.57), py = cy + 13 * Math.sin(k * 1.257 - 1.57); el("circle", { cx: px, cy: py, r: 10.5, fill: c }, g); el("circle", { cx: px - 3, cy: py - 3, r: 3, fill: "#fff", opacity: .5 }, g); }
  el("circle", { cx: x, cy: cy, r: 8, fill: c === "#FFC53D" ? "#F7922F" : "#FFC53D" }, g); el("circle", { cx: x - 2.5, cy: cy - 2.5, r: 2.6, fill: "#FFF1B8" }, g);
  const a = 15.25 + i * .14;
  KM("#fl" + i, [[a, { transform: "scale(0)" }], [a + .3, { transform: "scale(1.25)" }, "ease-out"], [a + .45, { transform: "scale(1)" }], [17.6, { transform: "scale(1)" }], [17.95, { transform: "scale(0)" }]]);
});
KM("#bf1", [[15.3, { opacity: 0, transform: "translate(1320px,300px)" }], [15.4, { opacity: 1, transform: "translate(1300px,300px)" }], [16.1, { opacity: 1, transform: "translate(1120px,430px)" }], [16.9, { opacity: 1, transform: "translate(1000px,350px)" }], [17.6, { opacity: 1, transform: "translate(930px,450px)" }], [17.8, { opacity: 0, transform: "translate(920px,450px)" }]]);
KM("#bf2", [[15.6, { opacity: 0, transform: "translate(-40px,400px)" }], [15.7, { opacity: 1, transform: "translate(-20px,400px)" }], [16.4, { opacity: 1, transform: "translate(180px,310px)" }], [17.0, { opacity: 1, transform: "translate(290px,390px)" }], [17.6, { opacity: 1, transform: "translate(370px,330px)" }], [17.8, { opacity: 0, transform: "translate(380px,330px)" }]]);

/* ---- confetti and dust when Lily lands ---- */
for (let i = 0; i < 18; i++) {
  el("rect", { id: "cf" + i, x: 754 + LX1, y: 440, width: 10, height: 16, rx: 3, fill: STEP[i % 5], opacity: 0, style: "transform-origin: 50% 50%" }, $("#confetti"));
  const dx = (i - 8.5) * 27 + (i % 3) * 9, up = 150 + (i * 37) % 130, r = 200 + i * 47;
  KM("#cf" + i, [[14.9, { opacity: 0, transform: "translate(0px,0px) rotate(0deg)" }], [14.95, { opacity: 1, transform: "translate(0px,0px) rotate(0deg)" }, "ease-out"],
    [15.45, { opacity: 1, transform: `translate(${dx}px,${-up}px) rotate(${r}deg)` }, "ease-in"], [16.5, { opacity: 0, transform: `translate(${dx * 1.45}px,70px) rotate(${r * 2.4}deg)` }]]);
}
[["#dustL", -1], ["#dustR", 1]].forEach(([s, d]) => KM(s, [[14.93, { opacity: 0, transform: "translate(0px,0px) scale(.6)" }], [14.97, { opacity: .95, transform: "translate(0px,0px) scale(.8)" }, "ease-out"], [15.55, { opacity: 0, transform: `translate(${d * 56}px,-8px) scale(2.2)` }]]));

/* ---- light: dusk until Lily arrives (14.2 s), then day ---- */
KM("#duskSky",  [[0, { opacity: 1 }], [14.1, { opacity: 1 }], [16, { opacity: 0 }], [17.6, { opacity: 0 }], [18, { opacity: 1 }]]);
KM("#duskVeil", [[0, { opacity: .3 }], [11.2, { opacity: .3 }], [12.6, { opacity: .42 }], [14.1, { opacity: .42 }], [15.8, { opacity: 0 }], [17.6, { opacity: 0 }], [18, { opacity: .3 }]]);
["#rb0", "#rb1", "#rb2", "#rb3", "#rb4"].forEach((r, i) => KM(r, [[0, { strokeDashoffset: 100 }], [15.5 + i * .09, { strokeDashoffset: 100 }, "ease-out"], [16.6 + i * .09, { strokeDashoffset: 0 }], [17.7, { strokeDashoffset: 0 }], [17.95, { strokeDashoffset: 100 }]]));
KM("#stars",    [[0, { opacity: 1 }], [14.1, { opacity: 1 }], [15.2, { opacity: 0 }], [17.7, { opacity: 0 }], [18, { opacity: 1 }]]);
KM("#sun",      [[0, { transform: "translateY(330px)" }], [14.1, { transform: "translateY(330px)" }], [16.2, { transform: "translateY(0px)" }, "cubic-bezier(.2,1.3,.4,1)"], [17.7, { transform: "translateY(0px)" }], [18, { transform: "translateY(330px)" }]]);
KM("#sunRays",  [[0, { opacity: 0, transform: "rotate(0deg)" }], [15.6, { opacity: 0, transform: "rotate(0deg)" }], [16.4, { opacity: .9, transform: "rotate(12deg)" }], [17.6, { opacity: .9, transform: "rotate(30deg)" }], [18, { opacity: 0, transform: "rotate(30deg)" }]]);
$("#sunRays").style.transformOrigin = "50% 50%";
KM("#lampLight",[[0, { opacity: 1 }], [11.3, { opacity: 1 }], [12.4, { opacity: .45 }], [14.1, { opacity: .45 }], [15.4, { opacity: 0 }], [17.7, { opacity: 0 }], [18, { opacity: 1 }]]);
KM("#kuboWin",  [[0, { fill: "#FFE08A" }], [14.2, { fill: "#FFE08A" }], [15.8, { fill: "#8A5A33" }], [17.7, { fill: "#8A5A33" }], [18, { fill: "#FFE08A" }]]);

/* ---- Mia ---- */
KM("#miaHead", [
  [0, { transform: "rotate(0deg) translateY(0px)" }], [1.2, { transform: "rotate(7deg) translateY(4px)" }], [4.8, { transform: "rotate(7deg) translateY(4px)" }],
  [5.3, { transform: "rotate(3deg) translateY(0px)" }], [6.2, { transform: "rotate(4deg) translateY(1px)" }], [6.34, { transform: "rotate(-5deg) translateY(1px)" }], [6.48, { transform: "rotate(7deg) translateY(1px)" }], [6.62, { transform: "rotate(-4deg) translateY(1px)" }], [6.78, { transform: "rotate(5deg) translateY(2px)" }],
  [7.4, { transform: "rotate(-6deg) translateY(-2px)" }], [8.6, { transform: "rotate(-1deg) translateY(-3px)" }], [9.9, { transform: "rotate(6deg) translateY(-2px)" }],
  [11.2, { transform: "rotate(4deg) translateY(2px)" }], [12.2, { transform: "rotate(13deg) translateY(12px)" }], [12.9, { transform: "rotate(13deg) translateY(12px)" }],
  [13.5, { transform: "rotate(8deg) translateY(4px)" }], [14.5, { transform: "rotate(-9deg) translateY(-8px)" }, "cubic-bezier(.3,1.6,.5,1)"], [15.2, { transform: "rotate(-5deg) translateY(-3px)" }],
  [16.2, { transform: "rotate(-8deg) translateY(-5px)" }], [17.4, { transform: "rotate(-5deg) translateY(-3px)" }], [18, { transform: "rotate(0deg) translateY(0px)" }]]);
KM("#miaBody", [[0, { transform: "translateY(0px)" }], [11.4, { transform: "translateY(0px)" }], [12.2, { transform: "translateY(9px)" }], [13.4, { transform: "translateY(9px)" }], [14.5, { transform: "translateY(-10px)" }, "cubic-bezier(.3,1.6,.5,1)"], [15.3, { transform: "translateY(0px)" }], [15.5, { transform: "translateY(-9px)" }], [15.7, { transform: "translateY(0px)" }], [15.9, { transform: "translateY(-9px)" }], [16.1, { transform: "translateY(0px)" }], [16.3, { transform: "translateY(-8px)" }], [16.5, { transform: "translateY(0px)" }], [16.7, { transform: "translateY(-7px)" }], [16.9, { transform: "translateY(0px)" }], [17.1, { transform: "translateY(-6px)" }], [17.3, { transform: "translateY(0px)" }]]);
KM("#pupils", [
  [0, { transform: "translate(0px,0px)" }], [1.2, { transform: "translate(-3px,6px)" }], [2.6, { transform: "translate(3px,6px)" }], [3.8, { transform: "translate(-3px,6px)" }], [4.8, { transform: "translate(0px,6px)" }],
  [7.4, { transform: "translate(-6px,-6px)" }], [8.6, { transform: "translate(0px,-8px)" }], [9.9, { transform: "translate(6px,-6px)" }],
  [11.3, { transform: "translate(0px,6px)" }], [12.9, { transform: "translate(0px,7px)" }], [13.4, { transform: "translate(7px,3px)" }], [14.5, { transform: "translate(7px,-3px)" }], [17.6, { transform: "translate(7px,-3px)" }], [18, { transform: "translate(0px,0px)" }]]);
KM("#browL", [[0, { transform: "rotate(0deg) translateY(0px)" }], [5.2, { transform: "rotate(0deg) translateY(0px)" }], [6.2, { transform: "rotate(-16deg) translateY(-3px)" }], [13.6, { transform: "rotate(-18deg) translateY(-3px)" }], [14.5, { transform: "rotate(6deg) translateY(-9px)" }], [17.6, { transform: "rotate(6deg) translateY(-9px)" }], [18, { transform: "rotate(0deg) translateY(0px)" }]]);
KM("#browR", [[0, { transform: "rotate(0deg) translateY(0px)" }], [5.2, { transform: "rotate(0deg) translateY(0px)" }], [6.2, { transform: "rotate(16deg) translateY(-3px)" }], [13.6, { transform: "rotate(18deg) translateY(-3px)" }], [14.5, { transform: "rotate(-6deg) translateY(-9px)" }], [17.6, { transform: "rotate(-6deg) translateY(-9px)" }], [18, { transform: "rotate(0deg) translateY(0px)" }]]);
showM("#mNeutral", 0, 4.9); showM("#mSad", 6.7, 14.1); showM("#mHappy", 14.5, 17.7);
KM("#mMmm", [[4.9, { opacity: 0, transform: "scaleX(1)" }], [5.1, { opacity: 1, transform: "scaleX(1)" }], [5.4, { opacity: 1, transform: "scaleX(.7)" }], [5.7, { opacity: 1, transform: "scaleX(1.1)" }], [6, { opacity: 1, transform: "scaleX(.7)" }], [6.3, { opacity: 1, transform: "scaleX(1)" }], [6.6, { opacity: 1, transform: "scaleX(.85)" }], [6.8, { opacity: 0, transform: "scaleX(1)" }]]);
KM("#blushL", [[0, { opacity: .55 }], [14.3, { opacity: .55 }], [15, { opacity: 1 }], [17.6, { opacity: 1 }], [18, { opacity: .55 }]]);
KM("#blushR", [[0, { opacity: .55 }], [14.3, { opacity: .55 }], [15, { opacity: 1 }], [17.6, { opacity: 1 }], [18, { opacity: .55 }]]);
// Her hands lie on the table beside the book. The left one reaches in and traces the letter with a finger, both press the book
// shut, then she throws her arms up and claps. (Poses and helpers: "Mia's arms" in lib.js.)
// Tracing: the left forearm tucks behind the book (HIDE) and her hand comes over the book's top edge (#traceHand) to follow the letter M.
const HIDE = LR("L", 0, 178), TRACE_PTS = [[474, 487], [477, 465], [487, 478], [497, 465], [500, 487], [488, 474]], TP = ([x, y]) => ({ transform: `translate(${x}px,${y}px)` });
function tracing(ts, km) {            // ts: the six seconds at which the fingertip is on each spot of the letter
  const f = km ? KM : K, a = ts[0], b = ts[5];
  f("#traceHand", [[a - .34, { opacity: 0 }, "steps(1)"], [a - .32, { opacity: 1 }, "steps(1)"], [b + .62, { opacity: 1 }, "steps(1)"], [b + .64, { opacity: 0 }, "steps(1)"]]);
  f("#traceHand", [[a - .32, TP([478, 424])], ...ts.map((t, i) => [t, TP(TRACE_PTS[i])]), [b + .3, TP(TRACE_PTS[5])], [b + .62, TP([488, 424])]]);
}
const LIFT = s => LR(s, 2, 105), CHEER = (s, k) => LR(s, k, 22), CLAP = (s, shut) => shut ? LR(s, -64, 219) : LR(s, -58, 199);
// While the book is open she holds it up by its sides, like a child reading (HOLD and holding() in lib.js); the left hand lets go
// to trace the letter and comes back.
["L", "R"].forEach(s => {
  const H = HOLD(s), f = [[0, H]];
  if (s === "L") { f.push([.8, H], [1.15, HIDE], [4.6, HIDE], [4.95, H]); holding(s, 0, .8, true); holding(s, 4.95, 11.3, true); }
  else holding(s, 0, 11.3, true);
  // both hands go to the top corners of the book and come down with it as it shuts
  f.push([11.3, H], [11.62, LIFT(s)], [12.05, LR(s, 0, 30)], [12.4, REST], [14.2, REST],
    [14.55, CHEER(s, 78), "cubic-bezier(.3,1.5,.5,1)"], [14.75, CHEER(s, 94)], [14.95, CHEER(s, 76)], [15.2, CHEER(s, 90)]);
  let shut = true; for (let t = 15.45; t < 17.4; t += .15) { f.push([t, CLAP(s, shut)]); shut = !shut; }
  armK(s, f, true);
  front(s, 11.42, 12.3, true);
});
tracing([1.2, 1.8, 2.4, 3.0, 3.6, 4.2], true);
// eyes squeeze shut in delight for a moment when Lily lands
KM("#eyesOpen",  [[0, { opacity: 1 }, "steps(1)"], [14.9, { opacity: 0 }, "steps(1)"], [15.5, { opacity: 1 }, "steps(1)"]]);
KM("#eyesHappy", [[0, { opacity: 0 }, "steps(1)"], [14.9, { opacity: 1 }, "steps(1)"], [15.5, { opacity: 0 }, "steps(1)"]]);
// legs swing under the table: lazy while reading, still when sad, fast when happy
const legAmp = t => t < 5 ? 13 : t < 11.3 ? 7 : t < 14.4 ? 1.5 : t < 17.6 ? 21 : 13, legSpd = t => t >= 14.4 && t < 17.6 ? 12 : 4.6;
oscM("#legL", t => `rotate(${(legAmp(t) * Math.sin(t * legSpd(t))).toFixed(2)}deg)`);
oscM("#legR", t => `rotate(${(legAmp(t) * Math.sin(t * legSpd(t) + 2.4)).toFixed(2)}deg)`);
KM("#sigh", [[12.15, { opacity: 0, transform: "translate(0px,0px) scale(.4)" }], [12.45, { opacity: .85, transform: "translate(14px,8px) scale(1)" }], [13.3, { opacity: 0, transform: "translate(52px,26px) scale(1.7)" }]]);

/* ---- the "Mmm" letters ---- */
[["#mm1", 5.0], ["#mm2", 5.35], ["#mm3", 5.7]].forEach(([s, a]) => KM(s, [
  [a, { opacity: 0, transform: "translate(0px,0px) rotate(0deg) scale(.5)" }], [a + .25, { opacity: 1, transform: "translate(8px,-12px) rotate(-8deg) scale(1)" }],
  [a + .9, { opacity: 1, transform: "translate(26px,-46px) rotate(9deg) scale(1.05)" }], [a + 1.6, { opacity: 0, transform: "translate(34px,-84px) rotate(-6deg) scale(.9)" }]]));

/* ---- the three obstacles ---- */
popM("#tagMia", .6, 3.0); popM("#cardM", 2.6, 4.9);
popM("#tb1", 7.3, 10.9); popM("#tb2", 8.5, 11.0); popM("#tb3", 9.8, 11.1);
KM("#clockHand", [[8.5, { transform: "rotate(0deg)" }, "linear"], [11.2, { transform: "rotate(1080deg)" }]]);
[["#sb4", 10.0], ["#sb3", 10.14], ["#sb2", 10.28], ["#sb1", 10.42]].forEach(([s2, t]) => KM(s2, [[0, { fill: "#0B7F7A" }], [t, { fill: "#0B7F7A" }], [t + .1, { fill: "#D8D2C4" }], [17.9, { fill: "#D8D2C4" }], [18, { fill: "#0B7F7A" }]]));
KM("#sx", [[10.5, { opacity: 0, transform: "scale(.2) rotate(-40deg)" }], [10.66, { opacity: 1, transform: "scale(1.5) rotate(8deg)" }, "ease-out"], [10.8, { opacity: 1, transform: "scale(1) rotate(0deg)" }], [11.3, { opacity: 1, transform: "scale(1) rotate(0deg)" }], [11.5, { opacity: 0, transform: "scale(1) rotate(0deg)" }]]);

/* ---- the book ---- */
KM("#bookOpen",   [[0, { opacity: 1, transform: "scaleY(1)" }], [11.6, { opacity: 1, transform: "scaleY(1)" }], [12.0, { opacity: 1, transform: "scaleY(.3)" }, "ease-in"], [12.02, { opacity: 0, transform: "scaleY(.3)" }], [17.6, { opacity: 0, transform: "scaleY(.3)" }], [18, { opacity: 1, transform: "scaleY(1)" }]]);
KM("#bookClosed", [[12.0, { opacity: 0, transform: "scaleY(1.5)" }], [12.02, { opacity: 1, transform: "scaleY(1.5)" }], [12.25, { opacity: 1, transform: "scaleY(.9)" }], [12.4, { opacity: 1, transform: "scaleY(1)" }], [17.6, { opacity: 1, transform: "scaleY(1)" }], [18, { opacity: 0, transform: "scaleY(1)" }]]);

/* ---- the phone wakes up ---- */
(() => { const f = [[12.7, { transform: "rotate(0deg) translateY(0px)" }, "linear"]]; let d = 1; for (let t = 12.76; t < 13.45; t += .06) { f.push([t, { transform: `rotate(${d * 2.6}deg) translateY(-4px)` }, "linear"]); d = -d; } f.push([13.46, { transform: "rotate(0deg) translateY(0px)" }]); f.push([14.2, { transform: "rotate(0deg) translateY(5px)" }]); f.push([14.4, { transform: "rotate(0deg) translateY(0px)" }]); KM("#phone", f); KM("#peekFollow", f); })();   // the peeking Lily shakes and dips with the phone
KM("#phoneScreen", [[0, { fill: "#27304F" }], [12.7, { fill: "#27304F" }], [13.0, { fill: "#FFF6CF" }], [17.6, { fill: "#FFF6CF" }], [18, { fill: "#27304F" }]]);
// the phone shows the app's start screen from the first second (a little dim, as an idle screen); it brightens when the phone wakes
KM("#phoneApp", [[0, { opacity: .85 }], [12.7, { opacity: .85 }], [13.35, { opacity: 1 }], [17.6, { opacity: 1 }]]);
KM("#phoneLight",  [[12.7, { opacity: 0 }], [13.2, { opacity: .9 }], [14.3, { opacity: 1 }], [15.6, { opacity: .25 }], [17.6, { opacity: .25 }], [18, { opacity: 0 }]]);
KM("#rings", [[12.8, { opacity: 0 }], [12.9, { opacity: 1 }], [15, { opacity: 1 }], [15.4, { opacity: 0 }]]);
["#ring1", "#ring2"].forEach((s2, i) => { const f = [[0, { transform: "scale(.5)", opacity: 0 }]]; for (let t = 12.8 + i * .45; t < 15.0; t += .9) { f.push([t, { transform: "scale(.5)", opacity: 1 }, "ease-out"]); f.push([t + .88, { transform: "scale(2.5)", opacity: 0 }, "steps(1)"]); } KM(s2, f); });

/* ---- Lily: peek, spring out, land, talk ---- */
// 1. "Someone small, with very big eyes, peeks in": a small Lily comes up from behind the phone until her eyes clear its top
//    edge, looks left and right, comes up to a smile, ducks, then springs straight up. She is hidden wherever the phone body is.
const PK = (y, r = 0, x = 0) => ({ transform: `translate(${x}px,${y}px) rotate(${r}deg)` });
const T_OUT = 14.36;                                    // the moment she is wholly above the phone and the full-size Lily takes over
KM("#lilyPeek", [
  [0, PK(0)], [12.95, PK(0), "cubic-bezier(.3,1.4,.5,1)"], [13.5, PK(-72)], [13.7, PK(-74, -9, -3)], [13.9, PK(-74, 9, 3)], [14.05, PK(-86)],
  [14.2, PK(-58), "ease-in"], [T_OUT, PK(-135, 0, 3)]]);
KM("#lilyPeekWrap", [[0, { opacity: 0 }, "steps(1)"], [12.9, { opacity: 1 }, "steps(1)"], [T_OUT, { opacity: 0 }, "steps(1)"]]);
// 2. The full-size Lily starts exactly where the small one left off (same place, same size), grows as she flies, and lands
//    beside the phone. Nothing clips her: she is in front of the scene and stands clear of the phone.
const LL = (x, y, r = 0, sx = 1, sy = 1) => ({ transform: `translate(${x}px,${y}px) rotate(${r}deg) scale(${sx},${sy})` });
KM("#lily", [
  [0, LL(0, -187, 0, .44, .44)], [T_OUT, LL(0, -187, 0, .44, .44), "ease-out"],
  [14.6, LL(LX1 * .66, -226, -12, .74, .9), "cubic-bezier(.5,0,.8,.4)"],
  [14.95, LL(LX1, 22, 0, 1.2, .78), "ease-out"], [15.15, LL(LX1, 0, 0, .95, 1.08)], [15.35, LL(LX1, 22)],
  [15.7, LL(LX1, 22, -4)], [16.05, LL(LX1, 14, 4)], [16.4, LL(LX1, 22, -4)], [16.75, LL(LX1, 14, 4)], [17.1, LL(LX1, 22, -3)],
  [17.6, LL(LX1, 22), "steps(1)"]]);
KM("#lilyWrap", [[0, { opacity: 0 }, "steps(1)"], [T_OUT, { opacity: 1 }, "steps(1)"]]);
// she lands in her cheering pose, then waves and talks
KM("#lilyC", [[0, { opacity: 0 }, "steps(1)"], [14.9, { opacity: 1 }, "steps(1)"], [15.45, { opacity: 0 }, "steps(1)"]]);
KM("#lilyA", [[0, { opacity: 1 }, "steps(1)"], [14.9, { opacity: 0 }, "steps(1)"], [15.45, { opacity: 1 }, "steps(1)"]]);
// mouth flap: swap between the app's closed and talking drawings while she speaks
(() => { const f = [[0, { opacity: 0 }, "steps(1)"]]; let on = true; for (let t = 15.5; t < 17.3; t += .14) { f.push([t, { opacity: on ? 1 : 0 }, "steps(1)"]); on = !on; } f.push([17.3, { opacity: 0 }, "steps(1)"]); KM("#lilyB", f); })();
KM("#sparks", [[14.5, { opacity: 0, transform: "scale(.4)" }], [14.9, { opacity: 1, transform: "scale(1)" }], [15.8, { opacity: 0, transform: "scale(1.25)" }]]);
$("#sparks").style.transformOrigin = "50% 60%";
popM("#bubble", 15.4, 17.5);
KM("#mabuhay", [[15.45, { transform: "scale(.2) rotate(-14deg)" }], [15.75, { transform: "scale(1.28) rotate(5deg)" }, "ease-out"], [15.95, { transform: "scale(1) rotate(0deg)" }], [16.4, { transform: "scale(1.06) rotate(-3deg)" }], [16.85, { transform: "scale(1) rotate(3deg)" }], [17.4, { transform: "scale(1.04) rotate(0deg)" }]]);
["#w1", "#w2", "#w3", "#w4", "#w5"].forEach((w, i) => KM(w, [[0, { fillOpacity: 0 }], [15.95 + i * .2, { fillOpacity: 0 }], [16.05 + i * .2, { fillOpacity: 1 }], [17.9, { fillOpacity: 1 }], [18, { fillOpacity: 0 }]]));

/* ---- captions ---- */
