// Beat 2b (0:28 to 0:40): "This is Lily, and this is PlayIT".  Beat 3 (0:40 to 1:10): the demonstration.
// Times come from the voice: cue("b3_02", "hears") is the second the narrator says "hears".
const FONT_T = 'font-family="Sniglet, Segoe UI, sans-serif" font-weight="800"', FONT_B = 'font-family="Fredoka, Lexend, Segoe UI, sans-serif" font-weight="700"';
const CAM = (x, y, s) => ({ transform: `translate(${x}px,${y}px) scale(${s})` });
const LT = (x, y, r = 0, sx = 1, sy = 1) => ({ transform: `translate(${x}px,${y}px) rotate(${r}deg) scale(${sx},${sy})` });
const HD = (r, y) => ({ transform: `rotate(${r}deg) translateY(${y}px)` });
const PU = (x, y) => ({ transform: `translate(${x}px,${y}px)` });

/* ---------- small helpers for the characters ---------- */
const POSES = { waving: "#lilyA", celebrating: "#lilyC", idle: "#lily_idle", pointing: "#lily_pointing", listening: "#lily_listening", encouraging: "#lily_encouraging", thinking: "#lily_thinking" };
function lilyPoses(list) { for (const [name, sel] of Object.entries(POSES)) K(sel, list.map(([t, n]) => [t, { opacity: n === name ? 1 : 0 }, "steps(1)"])); }
function lilyHop(t, x0, x1, h = 56) {
  K("#lily", [[t, LT(x0, 22)], [t + .1, LT(x0, 26, 0, 1.08, .9)], [t + .32, LT((x0 + x1) / 2, 22 - h, x1 >= x0 ? 6 : -6, .95, 1.08), "ease-in"], [t + .55, LT(x1, 22, 0, 1.14, .84), "ease-out"], [t + .72, LT(x1, 22)]]);
}
function miaMouth(sel, a, b) {            // swap her smile for another mouth between a and b
  K("#mHappy", [[a - .08, { opacity: 1 }], [a, { opacity: 0 }], [b, { opacity: 0 }], [b + .08, { opacity: 1 }]]);
  K(sel, [[a - .08, { opacity: 0 }], [a, { opacity: 1 }], [b, { opacity: 1 }], [b + .08, { opacity: 0 }]]);
}
function miaBounce(a, n = 3, h = 9) { const f = [[a, { transform: "translateY(0px)" }]]; for (let i = 0; i < n; i++) f.push([a + .12 + i * .34, { transform: `translateY(${-h}px)` }], [a + .3 + i * .34, { transform: "translateY(0px)" }]); K("#miaBody", f); }
function miaCheer(a, b) {                 // both arms fly up, like Lily's cheering pose
  ["L", "R"].forEach(s => armK(s, [[a - .25, REST], [a, CHEER(s, 78), "cubic-bezier(.3,1.5,.5,1)"], [a + .2, CHEER(s, 94)], [a + .4, CHEER(s, 76)], [b, CHEER(s, 90)], [b + .3, REST]]));
  K("#eyesOpen", [[a, { opacity: 0 }, "steps(1)"], [a + .55, { opacity: 1 }, "steps(1)"]]); K("#eyesHappy", [[a, { opacity: 1 }, "steps(1)"], [a + .55, { opacity: 0 }, "steps(1)"]]);
  miaBounce(a, 2, 10);
}
const TAP = reach("R", 631, 440, { point: true }), TAP_BACK = reach("R", 624, 444, { point: true });   // fingertip on the edge of the big phone
function miaTap(t) {                         // the arm reaches out to the phone, the index finger taps
  armK("R", [[t - .28, REST], [t, TAP, "ease-out"], [t + .22, TAP_BACK], [t + .55, REST]]);
  pointing("R", t - .12, t + .4);
}
function legs(a, b, amp, spd) { const fl = [], fr = []; for (let t = a; t <= b; t += .15) { fl.push([t, { transform: `rotate(${(amp * Math.sin(t * spd)).toFixed(2)}deg)` }, "linear"]); fr.push([t, { transform: `rotate(${(amp * Math.sin(t * spd + 2.4)).toFixed(2)}deg)` }, "linear"]); } K("#legL", fl); K("#legR", fr); }

/* ---------- the phone ---------- */
const big = (x, y) => [756 + (x - 756) * 2.6, 498 + (y - 498) * 2.6];      // where a point on the small phone ends up when the phone is big
function screen(name, t) { K("#scr_" + name, [[t - .01, { opacity: 0, transform: "translateX(75px)" }, "steps(1)"], [t, { opacity: 1, transform: "translateX(75px)" }, "ease-out"], [t + .35, { opacity: 1, transform: "translateX(0px)" }]]); }
// Tap positions in this file were measured on the earlier 84-wide screen, which cut the top and bottom off the app.
// at() moves them onto today's screen, which shows the whole app (75 wide: the recording's own shape).
const at = (x, y) => window.REC ? [756 + (x - 756) * .8929, 327.96 + (y - 318.48) * .8929] : [756 + (x - 756) * .9151, 328 + (y - 320.67) * .9151];
function tap(t, x, y) { [x, y] = at(x, y); K("#tap", [[t, { opacity: 1, transform: `translate(${x - 756}px,${y - 410}px) scale(.4)` }, "ease-out"], [t + .45, { opacity: 0, transform: `translate(${x - 756}px,${y - 410}px) scale(2.6)` }, "steps(1)"]]); S("sfx_opening_pluck.wav", t, .2); }

K("#tap", [[0, { opacity: 0 }, "steps(1)"]]);      // the tap ring stays hidden until the first tap

/* =====================================================================================================
   Beat 2b — 0:28 to 0:40
   ===================================================================================================== */
const tLily = cue("b2_03", "Lily"), tPlay = cue("b2_03", "PlayIT"), tPhon = cue("b2_03", "phonics"),
  tB1 = cue("b2_03", "DepEd"), tB2 = cue("b2_03", "listens"), tB3 = cue("b2_03", "works");

WORLD(`<g transform="translate(${LX1 - 110} 0)"><g id="tagLily" opacity="0" style="transform-origin: 50% 100%">
  <path d="M855 288 L869 312 L883 288Z" fill="${TEAL}" stroke="#fff" stroke-width="7" stroke-linejoin="round"/>
  <rect x="801" y="232" width="136" height="62" rx="31" fill="${TEAL}" stroke="#fff" stroke-width="7"/><rect x="857" y="284" width="24" height="14" fill="${TEAL}"/>
  <text x="869" y="277" text-anchor="middle" ${FONT_T} font-size="40" fill="#fff">Lily</text></g></g>`);
UI(`<g id="logo" opacity="0" style="transform-origin: 50% 50%">
  <text x="640" y="180" text-anchor="middle" ${FONT_T} font-size="132" fill="#0B5F5B" stroke="#0B5F5B" stroke-width="20" stroke-linejoin="round" opacity=".3">PlayIT</text>
  <text x="640" y="170" text-anchor="middle" ${FONT_T} font-size="132" stroke="#fff" stroke-width="20" stroke-linejoin="round" paint-order="stroke"><tspan fill="#FFC53D">Play</tspan><tspan fill="${TEAL}">IT</tspan></text></g>`);
// "a phonics game for Grade 1 learners" is shown once, under the logo, as a sing-along line (not also as a caption at the bottom)
window.CAP_PLACE = ph => ph.v.id === "b2_03" && ph.words.some(x => /^phonics/i.test(x.w)) ? { cls: "capTag", until: tB1 - .75 } : null;
const badge = (i, x, color, icon, l1, l2) => UI(`<g id="bd${i}" opacity="0" style="transform-origin: 50% 50%">
  <rect x="${x + 6}" y="184" width="300" height="118" rx="30" fill="#0B5F5B" opacity=".28"/>
  <rect x="${x}" y="176" width="300" height="118" rx="30" fill="#fff" stroke="${color}" stroke-width="8"/>
  <circle cx="${x + 66}" cy="235" r="42" fill="${color}"/>${icon(x + 66, 235)}
  <text x="${x + 122}" y="230" ${FONT_B} font-size="30" fill="#0B3F4A">${l1}</text><text x="${x + 122}" y="266" ${FONT_B} font-size="30" fill="#0B3F4A">${l2}</text></g>`);
badge(1, 150, STEP[1], (x, y) => `<text x="${x}" y="${y + 13}" text-anchor="middle" ${FONT_T} font-size="40" fill="#fff">ma</text>`, "Marungko", "order");
badge(2, 490, STEP[0], (x, y) => `<rect x="${x - 9}" y="${y - 27}" width="18" height="32" rx="9" fill="#fff"/><path d="M${x - 17} ${y - 6} a17 17 0 0 0 34 0" stroke="#fff" stroke-width="5" fill="none" stroke-linecap="round"/><path d="M${x} ${y + 11} v12 M${x - 10} ${y + 23} h20" stroke="#fff" stroke-width="5" stroke-linecap="round"/>`, "Listens to", "the child");
badge(3, 830, STEP[2], (x, y) => `<path d="M${x - 27} ${y + 1} l54 -21 -15 46 -11 -17 -15 15 -1 -15Z" fill="#fff" stroke="#fff" stroke-width="3" stroke-linejoin="round"/>`, "Works", "offline");

K("#cam", [[27.6, CAM(-20, 0, 1.06)], [28.7, CAM(0, 60, 1)], [39.5, CAM(0, 60, 1)], [40.6, CAM(-80, 22, 1.08)], [window.REC ? 68.4 : 65.6, CAM(-80, 22, 1.08)], [window.REC ? 69.3 : 66.5, CAM(-60, 30, 1.04)], [69.7, CAM(-60, 30, 1.04)]]);
K("#phoneLight", [[27.6, { opacity: .25 }], [29, { opacity: 0 }]]);
K("#browL", [[27.9, { transform: "rotate(6deg) translateY(-9px)" }], [28.6, { transform: "rotate(0deg) translateY(-3px)" }]]);
K("#browR", [[27.9, { transform: "rotate(-6deg) translateY(-9px)" }], [28.6, { transform: "rotate(0deg) translateY(-3px)" }]]);
["L", "R"].forEach(s => armK(s, [[27.95, CLAP(s, false)], [28.55, REST]]));      // the clapping of Beat 2 ends: hands back on the table
legs(27.7, 69.6, 9, 4.4);
pop("#tagLily", tLily - .25, tPlay - .3);
lilyHop(tLily - .2, LX1, LX1, 40);
K("#logo", [[tPlay - .2, { opacity: 0, transform: "translate(0px,0px) scale(.2) rotate(-8deg)" }], [tPlay + .12, { opacity: 1, transform: "translate(0px,0px) scale(1.16) rotate(3deg)" }, "ease-out"],
  [tPlay + .34, { opacity: 1, transform: "translate(0px,0px) scale(1) rotate(0deg)" }], [tB1 - .7, { opacity: 1, transform: "translate(0px,0px) scale(1) rotate(0deg)" }],
  [tB1 - .2, { opacity: 1, transform: "translate(0px,-68px) scale(.56) rotate(0deg)" }], [39.45, { opacity: 1, transform: "translate(0px,-68px) scale(.56) rotate(0deg)" }], [39.75, { opacity: 0, transform: "translate(0px,-68px) scale(.3) rotate(0deg)" }]]);
K("#miaHead", [[27.6, HD(-5, -3)], [tPlay, HD(-5, -3)], [tPlay + .4, HD(-9, -6)], [tB1 - .3, HD(-8, -5)], [39.7, HD(-4, -2)]]);
K("#pupils", [[27.6, PU(7, -3)], [tPlay, PU(7, -3)], [tPlay + .4, PU(4, -7)], [39.6, PU(4, -7)], [40.2, PU(7, 0)]]);
S("sfx_opening_pluck.wav", tPlay - .1, .32);
[[1, tB1], [2, tB2], [3, tB3]].forEach(([i, t]) => { pop("#bd" + i, t - .1, 39.4 + i * .06); lilyHop(t - .05, LX1, LX1, 34); miaBounce(t, 1, 7); S("sfx_opening_pluck.wav", t - .05, .22); });
lilyPoses([[27.6, "waving"], [tB1 - .3, "pointing"], [39.7, "waving"], [40.4, "pointing"], [cue("b3_02", "First") - .2, "listening"], [cue("b3_02", "Then") - .1, "idle"],
  [cue("b3_02b", "checks") + .2, "celebrating"], [cue("b3_02b", "Nothing") + .6, "idle"], [cue("b3_03", "Miss") - .1, "encouraging"], [cue("b3_04", "Next") - .1, "pointing"],
  [cue("b3_04", "Then") - .1, "thinking"], [65.0, "celebrating"], [66.0, "waving"]]);

/* =====================================================================================================
   Beat 3 — 0:40 to 1:10: the phone grows so we can watch the real app; Mia taps, listens and speaks; Lily reacts
   ===================================================================================================== */
const tWatch = LINE.b3_01.start, tTaps = cue("b3_01", "taps"), tFirst = cue("b3_02", "First"), tHears = cue("b3_02", "hears"),
  tThen = cue("b3_02", "Then"), tSays = cue("b3_02", "says"), tChecks = cue("b3_02b", "checks"), tNothing = cue("b3_02b", "Nothing"), tMouse = LINE.b3_02v.start,
  tMiss = cue("b3_03", "Miss"), tNever = cue("b3_03", "never"), tMouth = cue("b3_03", "mouth"), tWith = cue("b3_03", "says"),
  tNext = cue("b3_04", "Next"), tFinds = cue("b3_04", "finds"), tBlend0 = cue("b3_04", "Then"), tLetters = cue("b3_04", "letters"), tParents = cue("b3_05", "parents");

lilyHop(39.85, LX1, LX2, 70);
// with the recording, the phone stays big through the Parent Zone; the results board covers the scene at 68.7
const PH_OUT = window.REC ? 68.35 : 65.5;
K("#phoneBig", [[40.2, { transform: "scale(1)" }, "cubic-bezier(.3,1.35,.5,1)"], [41.0, { transform: "scale(2.6)" }], [PH_OUT, { transform: "scale(2.6)" }], [PH_OUT + .7, { transform: "scale(1)" }]]);
K("#phoneStand", [[40.15, { opacity: 1 }], [40.45, { opacity: 0 }], [PH_OUT + .4, { opacity: 0 }], [PH_OUT + .7, { opacity: 1 }]]);
// focus on Mia, the phone and Lily: the far background blurs (harder while the camera moves, like motion blur), loses some colour and fades toward cream
const FOCUS_IN = 40.15, FOCUS_OUT = window.REC ? 68.25 : 65.45;
K("#bgFar", [[FOCUS_IN, { filter: "blur(0px) saturate(1)" }], [FOCUS_IN + .45, { filter: "blur(10px) saturate(.8)" }, "ease-in"], [FOCUS_IN + 1.0, { filter: "blur(5px) saturate(.75)" }, "ease-out"],
  [FOCUS_OUT, { filter: "blur(5px) saturate(.75)" }], [FOCUS_OUT + .4, { filter: "blur(10px) saturate(.85)" }, "ease-in"], [FOCUS_OUT + .95, { filter: "blur(0px) saturate(1)" }, "ease-out"]]);
K("#bgWash", [[FOCUS_IN, { opacity: 0 }], [FOCUS_IN + .8, { opacity: 1 }], [FOCUS_OUT + .2, { opacity: 1 }], [FOCUS_OUT + .9, { opacity: 0 }]]);
// the butterflies fly off for the demo so nothing but the three of them moves in front
["#bf1", "#bf2"].forEach(b => K(b, [[FOCUS_IN + .2, { opacity: 1 }], [FOCUS_IN + .6, { opacity: 0 }], [FOCUS_OUT + .6, { opacity: 0 }], [FOCUS_OUT + 1.1, { opacity: 1 }]]));

// offline is built in, so no switch to flip: a small "No Wi-Fi needed" chip stays for the whole demo
UI(`<g id="noNet" opacity="0" style="transform-origin: 0% 0%"><rect x="60" y="134" width="286" height="54" rx="27" fill="#fff" stroke="${TEAL}" stroke-width="6"/>
  <g transform="translate(97 161)" stroke="${TEAL}" stroke-width="5" fill="none" stroke-linecap="round"><path d="M-17 -5 a24 24 0 0 1 34 0"/><path d="M-10 2 a14 14 0 0 1 20 0"/><circle cx="0" cy="9" r="2.5" fill="${TEAL}"/><path d="M-16 -14 L16 16" stroke="${STEP[3]}"/></g>
  <text x="126" y="171" ${FONT_B} font-size="25" fill="#0B3F4A">No Wi-Fi needed</text></g>`);
pop("#noNet", tWatch + .2, 65.5); S("sfx_node_unlock_chime.mp3", tWatch + .2, .2);

// the four steps, named the way the app names them
// With the recording, the phone turns to Blend It at about 1:02 whatever the narrator's pace, so the label changes then at the latest.
const tBlendUI = window.REC ? Math.min(tBlend0, 62.0) : tBlend0;
[["Hear It", STEP[0], tFirst - .15, tThen - .25], ["Say It", STEP[1], tThen - .15, tNext - .25], ["Find It", STEP[2], tNext - .15, tBlendUI - .25], ["Blend It", STEP[3], tBlendUI - .15, 65.5]].forEach(([name, c, a, b], i) => {
  UI(`<g id="st${i}" opacity="0" style="transform-origin: 0% 50%"><rect x="66" y="56" width="${name.length > 7 ? 300 : 280}" height="78" rx="39" fill="#0B5F5B" opacity=".28"/>
    <rect x="60" y="48" width="${name.length > 7 ? 300 : 280}" height="78" rx="39" fill="${c}" stroke="#fff" stroke-width="7"/><circle cx="102" cy="87" r="27" fill="#fff"/>
    <text x="102" y="101" text-anchor="middle" ${FONT_T} font-size="38" fill="${c}">${i + 1}</text><text x="144" y="101" ${FONT_T} font-size="40" fill="#fff">${name}</text></g>`);
  pop("#st" + i, a, b);
});

// the real screens, in the order the narrator walks through them
// The screen recording (rec/, cut by tools/make_rec_frames.py from recording_plan.json) plays inside the phone.
// Without it, the film falls back to the screenshots below.
const REC = window.REC;
if (REC) {
  const el = $("#recScr"), F = REC.frames, name = n => `rec/f_${String(n).padStart(4, "0")}.jpg`;
  window.REC_IMGS = F.map(n => { const i = new Image(); i.src = name(n); return i; });
  let cur = -2;
  (window.ON_SEEK = window.ON_SEEK || []).push(t => {
    const n = Math.round(t * REC.fps); let f = -1;
    if (REC.segments.some(([a, b]) => n >= a && n <= b)) { let lo = 0, hi = F.length - 1; while (lo < hi) { const m = (lo + hi + 1) >> 1; if (F[m] <= n) lo = m; else hi = m - 1; } if (F[lo] <= n) f = F[lo]; }
    if (f !== cur) { cur = f; if (f >= 0) el.setAttribute("href", name(f)); el.setAttribute("opacity", f >= 0 ? 1 : 0); }
  });
}
if (!REC) screen("map", tTaps - .35); if (!REC) screen("hearit", tTaps + 1.3); if (!REC) screen("sayit", tThen - .2); if (!REC) screen("sayit_cue", tMiss - .15); if (!REC) screen("findit", tNext - .15); if (!REC) screen("blendit", tBlend0 - .15);
// (only for the screenshot fallback: with the recording these would hold a stray screenshot on the phone from 0:00)
if (!REC) ["map", "hearit", "sayit", "sayit_cue", "findit", "blendit"].forEach(n => K("#scr_" + n, [[65.5, { opacity: 1 }], [65.8, { opacity: 0 }]]));
K("#pupils", [[40.2, PU(7, 0)], [65.6, PU(7, 0)], [66.2, PU(5, -5)]]);
K("#miaHead", [[40.2, HD(-4, -2)], [tHears - .2, HD(-4, -2)], [tHears + .1, HD(-10, -2)], [tThen - .3, HD(-10, -2)], [tThen, HD(-4, -2)], [tMiss, HD(-4, -2)], [tMiss + .4, HD(3, 3)], [tMouth, HD(-3, 0)], [tNext, HD(-4, -2)], [69.6, HD(-4, -2)]]);

// tap the letter on the map
if (REC) { miaTap(42.87); tap(42.92, 754, 377); } else { miaTap(tTaps + .35); tap(tTaps + .4, 756, 410.6); }
// 1 Hear It: tap the speaker; sound comes out of the phone
miaTap(tHears - .35); tap(tHears - .3, 756, 452.5);
WORLD(`<g id="snd" fill="none" stroke="${STEP[0]}" stroke-width="10" stroke-linecap="round"><path id="snd0" opacity="0" d="M606 262 q-24 26 0 52"/><path id="snd1" opacity="0" d="M590 244 q-42 44 0 88"/><path id="snd2" opacity="0" d="M574 226 q-60 62 0 124"/></g>`);
[0, 1, 2].forEach(i => { const f = []; for (let t = tHears - .1; t < tThen - .5; t += .5) f.push([t + i * .12, { opacity: 0 }], [t + i * .12 + .1, { opacity: 1 }], [t + i * .12 + .3, { opacity: 1 }], [t + i * .12 + .45, { opacity: 0 }]); K("#snd" + i, f); });
S("ph_m.wav", tHears, .5);
// 2 Say It: Mia says the sound; the phone itself marks it right
WORLD(`<g ${FONT_T} fill="#fff" stroke="${STEP[1]}" stroke-width="3" paint-order="stroke"><text id="say0" x="452" y="250" font-size="40" opacity="0">M</text><text id="say1" x="494" y="236" font-size="34" opacity="0">m</text><text id="say2" x="530" y="250" font-size="30" opacity="0">m</text></g>`);
const sayIt = t => { miaMouth("#mMmm", t, t + 1.0); [0, 1, 2].forEach(i => K("#say" + i, [[t + i * .16, { opacity: 0, transform: "translate(0px,0px) scale(.5)" }], [t + i * .16 + .2, { opacity: 1, transform: "translate(0px,-14px) scale(1.1)" }, "ease-out"], [t + i * .16 + .8, { opacity: 1, transform: "translate(6px,-52px) scale(1)" }], [t + i * .16 + 1.3, { opacity: 0, transform: "translate(8px,-80px) scale(.9)" }]])); S("ph_m.wav", t + .05, .45); };
// Say It: Mia taps the microphone, then says the word on the screen ("Mouse!", in the user's voice) and it floats into the phone
if (REC) { miaTap(46.62); tap(46.67, 756, 437); } else { miaTap(tSays - .1); tap(tSays - .05, 756, 440.5); } miaMouth("#mMmm", tMouse, tMouse + .7);
WORLD(`<text id="sayW" x="470" y="250" ${FONT_T} font-size="44" fill="#fff" stroke="${STEP[1]}" stroke-width="3" paint-order="stroke" opacity="0" style="transform-origin: 50% 50%">Mouse!</text>`);
K("#sayW", [[tMouse, { opacity: 0, transform: "translate(0px,0px) scale(.5)" }], [tMouse + .25, { opacity: 1, transform: "translate(0px,-16px) scale(1.15)" }, "ease-out"], [tMouse + .9, { opacity: 1, transform: "translate(60px,-40px) scale(1)" }],
  [tMouse + 1.4, { opacity: 0, transform: "translate(150px,-10px) scale(.4)" }, "ease-in"]]);
UI(`<g id="chk" opacity="0" style="transform-origin: 50% 50%"><circle cx="852" cy="122" r="48" fill="${STEP[2]}" stroke="#fff" stroke-width="9"/><path d="M830 123 l15 16 l30 -34" stroke="#fff" stroke-width="11" fill="none" stroke-linecap="round" stroke-linejoin="round"/></g>
  <g id="lock" opacity="0" style="transform-origin: 100% 50%"><rect x="906" y="186" width="330" height="62" rx="31" fill="#fff" stroke="${TEAL}" stroke-width="6"/>
  <rect x="930" y="212" width="30" height="24" rx="6" fill="${TEAL}"/><path d="M936 212 v-7 a9 9 0 0 1 18 0 v7" stroke="${TEAL}" stroke-width="5" fill="none"/><text x="974" y="227" ${FONT_B} font-size="25" fill="#0B3F4A">Stays on the phone</text></g>`);
pop("#chk", tChecks + .2, tMiss - .35); S("sfx_correct_chime.mp3", tChecks + .2, .28); miaCheer(tChecks + .3, tChecks + 1.3);
lilyHop(tChecks + .25, LX2, LX2, 50);
pop("#lock", tNothing - .1, tMiss - .3);
// Miss it? Lily never scolds: she shows the mouth shape and says it with her
K("#phone", [[tMiss - .05, { transform: "rotate(0deg) translateY(0px)" }, "linear"], [tMiss + .05, { transform: "rotate(-2.2deg) translateY(0px)" }, "linear"], [tMiss + .15, { transform: "rotate(2.2deg) translateY(0px)" }, "linear"], [tMiss + .25, { transform: "rotate(-1.4deg) translateY(0px)" }, "linear"], [tMiss + .35, { transform: "rotate(0deg) translateY(0px)" }]]);
S("sfx_incorrect_pop.mp3", tMiss, .2);
miaMouth("#mSad", tMiss + .1, tMouth - .2);
K("#browL", [[tMiss, { transform: "rotate(0deg) translateY(-3px)" }], [tMiss + .3, { transform: "rotate(-14deg) translateY(-3px)" }], [tMouth - .3, { transform: "rotate(-14deg) translateY(-3px)" }], [tMouth, { transform: "rotate(0deg) translateY(-3px)" }]]);
K("#browR", [[tMiss, { transform: "rotate(0deg) translateY(-3px)" }], [tMiss + .3, { transform: "rotate(14deg) translateY(-3px)" }], [tMouth - .3, { transform: "rotate(14deg) translateY(-3px)" }], [tMouth, { transform: "rotate(0deg) translateY(-3px)" }]]);
WORLD(`<g fill="#F2545B" transform="translate(${LX2 - 210} 0)"><path id="hrt0" opacity="0" style="transform-origin: 50% 50%" d="M936 262 c-10 -16 -32 -4 -22 14 l22 22 22 -22 c10 -18 -12 -30 -22 -14Z"/><path id="hrt1" opacity="0" style="transform-origin: 50% 50%" d="M1006 236 c-8 -12 -24 -3 -17 10 l17 17 17 -17 c7 -13 -9 -22 -17 -10Z"/></g>`);
[0, 1].forEach(i => K("#hrt" + i, [[tNever + i * .22, { opacity: 0, transform: "translateY(20px) scale(.3)" }], [tNever + i * .22 + .25, { opacity: 1, transform: "translateY(0px) scale(1.2)" }, "ease-out"], [tNever + i * .22 + 1.0, { opacity: 1, transform: "translateY(-26px) scale(1)" }], [tNever + i * .22 + 1.4, { opacity: 0, transform: "translateY(-44px) scale(.8)" }]]));
UI(`<g id="mouthCard" opacity="0" style="transform-origin: 50% 100%"><rect x="906" y="52" width="290" height="246" rx="30" fill="#0B5F5B" opacity=".28"/><rect x="900" y="44" width="290" height="246" rx="30" fill="#fff" stroke="${STEP[1]}" stroke-width="8"/>
  <image href="img/mouth_lips_together.png" x="967" y="52" width="156" height="156"/><text x="1045" y="246" text-anchor="middle" ${FONT_B} font-size="29" fill="#0B3F4A">Lips together</text>
  <text x="1045" y="276" text-anchor="middle" ${FONT_T} font-size="26" fill="${STEP[1]}">mmm</text></g>`);
pop("#mouthCard", tMouth - .45, tNext - .3); if (!REC) tap(tMouth - .2, 769, 436);   // in the recording the app shows the lips by itself, so there is nothing to tap
flap("#lily_encouraging_talk", tWith, tWith + 1.0); sayIt(tWith + .05); miaBounce(tNext - .5, 1, 8);
// 3 Find It: three taps, three stars
const STAR = "M0 -18 L5 -6 18 -5 8 4 11 17 0 10 -11 17 -8 4 -18 -5 -5 -6Z";
WORLD(`<g id="stars3" fill="#FFC53D" stroke="#fff" stroke-width="4" stroke-linejoin="round">${[0, 1, 2].map(i => `<path id="star${i}" opacity="0" d="${STAR}"/>`).join("")}</g>`);
(REC ? [[735, 433.6, 60.68], [777, 433.6, 61.28], [756, 467.2, 61.68]] : [[775, 406.5, tFinds + .2], [736.5, 439.4, tFinds + .9], [775, 439.4, tFinds + 1.6]]).forEach(([x, y, t], i) => {
  miaTap(t - .05); tap(t, x, y); const [bx, by] = big(...at(x, y));
  K("#star" + i, [[t, { opacity: 0, transform: `translate(${bx}px,${by}px) scale(.2) rotate(0deg)` }], [t + .2, { opacity: 1, transform: `translate(${bx}px,${by - 30}px) scale(1.5) rotate(40deg)` }, "ease-out"],
    [t + .95, { opacity: 1, transform: `translate(${bx + (i - 1) * 70}px,${by - 170}px) scale(1.1) rotate(200deg)` }], [t + 1.35, { opacity: 0, transform: `translate(${bx + (i - 1) * 90}px,${by - 210}px) scale(.6) rotate(260deg)` }]]);
});
// 4 Blend It: the letter tiles from the screen come together
UI(`<g id="tiles" opacity="0">${["A", "M", "S"].map((c, i) => `<g id="tile${i}" style="transform-origin: 50% 50%"><rect x="${70 + i * 104}" y="218" width="86" height="88" rx="20" fill="#E2A91E"/><rect x="${70 + i * 104}" y="210" width="86" height="86" rx="20" fill="#FFC53D"/><rect x="${80 + i * 104}" y="218" width="34" height="9" rx="4.5" fill="#FFE08A"/>
  <text x="${113 + i * 104}" y="274" text-anchor="middle" ${FONT_T} font-size="58" fill="${INK}">${c}</text></g>`).join("")}</g>`);
if (REC) [[741.7, 458.4, 62.5], [744.2, 458.4, 62.9], [756, 458.4, 63.13]].forEach(([x, y, t]) => { miaTap(t - .05); tap(t, x, y); });
else show("#tiles", tLetters - .25, 65.4, 1, .2);
if (!REC) [[738, 459], [756, 459], [773.6, 459]].forEach(([x, y], i) => {
  const t = tLetters - .15 + i * .42; miaTap(t - .05); tap(t, x, y);
  K("#tile" + i, [[t - .02, { transform: `translate(0px,40px) scale(.2)` }], [t + .22, { transform: `translate(0px,0px) scale(1.15)` }, "ease-out"], [t + .38, { transform: `translate(0px,0px) scale(1)` }],
    [tLetters + 1.25, { transform: `translate(0px,0px) scale(1)` }], [tLetters + 1.55, { transform: `translate(${(1 - i) * 18}px,0px) scale(1.06)` }, "cubic-bezier(.3,1.6,.5,1)"], [tLetters + 1.85, { transform: `translate(${(1 - i) * 18}px,-10px) scale(1.06)` }], [tLetters + 2.05, { transform: `translate(${(1 - i) * 18}px,0px) scale(1.06)` }]]);
});
S("sfx_level_complete_fanfare.mp3", 64.9, .18); miaCheer(65.0, 65.7); lilyHop(65.0, LX2, LX2, 60);
// parents get a plain progress report
UI(`<g id="report" opacity="0" style="transform-origin: 50% 0%"><rect x="566" y="52" width="290" height="250" rx="28" fill="#0B5F5B" opacity=".28"/><rect x="560" y="44" width="290" height="250" rx="28" fill="#fff" stroke="${TEAL}" stroke-width="8"/>
  <path d="M564 96 v-24 a24 24 0 0 1 24 -24 h234 a24 24 0 0 1 24 24 v24Z" fill="${TEAL}"/><text x="705" y="84" text-anchor="middle" ${FONT_B} font-size="26" fill="#fff">Progress report</text>
  ${[["M", STEP[2], 150], ["S", STEP[0], 112], ["A", STEP[1], 74]].map(([c, col, w], i) => `<circle cx="598" cy="${130 + i * 42}" r="16" fill="${col}"/><text x="598" y="${138 + i * 42}" text-anchor="middle" ${FONT_T} font-size="22" fill="#fff">${c}</text>
  <rect x="626" y="${122 + i * 42}" width="150" height="16" rx="8" fill="#E7E2D3"/><rect id="bar${i}" x="626" y="${122 + i * 42}" width="${w}" height="16" rx="8" fill="${col}" style="transform-origin: 0% 50%"/><path d="${STAR}" transform="translate(806 ${130 + i * 42}) scale(.72)" fill="${i < 2 ? "#FFC53D" : "#E7E2D3"}"/>`).join("")}
  <circle cx="600" cy="260" r="17" fill="#DDA273"/><path d="M583 261 a17 17 0 0 1 34 0 q-8 -9 -17 -7 q-9 -2 -17 7Z" fill="#3B2A26"/><circle cx="600" cy="241" r="7" fill="#3B2A26"/>
  <text x="626" y="268" ${FONT_B} font-size="21" fill="#5B6B6B" font-weight="600">For parents</text>
  <rect x="762" y="246" width="66" height="32" rx="9" fill="${STEP[3]}"/><text x="795" y="269" text-anchor="middle" ${FONT_B} font-size="20" fill="#fff">PDF</text></g>`);
if (!REC) pop("#report", tParents - .2, 68.95);
if (!REC) [0, 1, 2].forEach(i => K("#bar" + i, [[tParents, { transform: "scaleX(.05)" }], [tParents + .5 + i * .2, { transform: "scaleX(1)" }, "ease-out"]]));
S("sfx_node_unlock_chime.mp3", tParents - .1, .22);
K("#pupils", [[66.2, PU(5, -5)], [69.6, PU(5, -5)]]);
