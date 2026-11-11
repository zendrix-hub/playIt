// Beat 4 (1:10 to 1:40): results and validation, on a board over the scene.  Beat 5 (1:40 to 2:00): impact and close.
// Every number comes from data.js (built from round2_data.json). While they are projections the board carries a PROJECTED tag.
const N = D.numbers;
K("#bubble", [[27.45, { opacity: 1, transform: "scale(1)" }], [27.75, { opacity: 0, transform: "scale(.6)" }]]);      // Lily's "Mabuhay!" bubble leaves before the logo arrives

/* =====================================================================================================
   Beat 4 — the board
   ===================================================================================================== */
const t30 = cue("b4_01", "30"), t16 = cue("b4_01", "16"), tPar = cue("b4_01", "parents"), tTea = cue("b4_01", "teachers"),
  t15 = cue("b4_02", "15"), tHappy = cue("b4_02", "children"), t12 = cue("b4_02", "12"), tPassed = cue("b4_02", "passed"),
  tGrown = cue("b4_03", "grown"), tS1 = cue("b4_03", "4.5"), tS2 = cue("b4_03", "4.8"), tS3 = cue("b4_03", "4.7"),
  tQuote = cue("b4_04", "One"), tTests = cue("b4_05", "691");
const big2 = (id, n, sub, extra = "") => `<g id="${id}" opacity="0" style="transform-origin: 50% 50%">
  <text x="245" y="250" text-anchor="middle" ${FONT_T} font-size="170" fill="${TEAL}" stroke="#fff" stroke-width="18" stroke-linejoin="round" paint-order="stroke">${n}</text>
  <text x="245" y="312" text-anchor="middle" ${FONT_B} font-size="48" fill="#0B3F4A">${sub}</text>${extra}</g>`;
const eyes = (x, y, r = 6) => `<circle cx="${x - r * 1.25}" cy="${y}" r="${r}" fill="#fff"/><circle cx="${x + r * 1.25}" cy="${y}" r="${r}" fill="#fff"/><circle cx="${x - r * 1.1}" cy="${y + .6}" r="${r * .55}" fill="${INK}"/><circle cx="${x + r * 1.4}" cy="${y + .6}" r="${r * .55}" fill="${INK}"/>`;
const kidX = i => 470 + (i % 8) * 62, kidY = i => 150 + Math.floor(i / 8) * 104;
const adultX = j => j < N.parents ? 470 + j * 50 : 470 + N.parents * 50 + 30 + (j - N.parents) * 50;

UI(`<g id="board" opacity="0">
  <rect x="-10" y="-10" width="1300" height="740" fill="#FFF8E7" opacity=".97"/>
  <circle cx="50" cy="660" r="180" fill="${TEAL}" opacity=".07"/><circle cx="1240" cy="60" r="160" fill="#FFC53D" opacity=".18"/><circle cx="1180" cy="680" r="90" fill="${STEP[3]}" opacity=".08"/>
  ${D.projected ? `<rect x="40" y="26" width="356" height="44" rx="22" fill="#FFC53D"/><text x="218" y="56" text-anchor="middle" ${FONT_B} font-size="21" fill="#3A2A00">PROJECTED · not final results</text>` : ""}
  ${big2("n30", N.total, "people")}
  ${big2("n15", N.happy, "of " + N.children, `<circle cx="245" cy="380" r="40" fill="#FFC53D"/><circle cx="231" cy="371" r="5" fill="${INK}"/><circle cx="259" cy="371" r="5" fill="${INK}"/><path d="M225 388 q20 20 40 0" stroke="${INK}" stroke-width="6" fill="none" stroke-linecap="round"/><text x="245" y="462" text-anchor="middle" ${FONT_B} font-size="29" fill="#0B3F4A">chose the happy face</text>`)}
  ${big2("n12", N.sayit, "of " + N.children, `<circle cx="245" cy="380" r="40" fill="${STEP[1]}"/><rect x="237" y="356" width="16" height="30" rx="8" fill="#fff"/><path d="M229 376 a16 16 0 0 0 32 0" stroke="#fff" stroke-width="5" fill="none" stroke-linecap="round"/><path d="M245 393 v10 M236 403 h18" stroke="#fff" stroke-width="5" stroke-linecap="round"/><text x="245" y="462" text-anchor="middle" ${FONT_B} font-size="29" fill="#0B3F4A">passed Say It first try</text>`)}
  <g id="bridge" opacity="0" style="transform-origin: 50% 50%"><text x="245" y="206" text-anchor="middle" ${FONT_T} font-size="58" fill="${TEAL}" stroke="#fff" stroke-width="12" stroke-linejoin="round" paint-order="stroke">The bridge</text>
    <text x="245" y="260" text-anchor="middle" ${FONT_B} font-size="34" fill="#0B3F4A">to the child</text>
    <path d="M120 410 Q245 300 370 410" stroke="#A8703F" stroke-width="18" fill="none" stroke-linecap="round"/><path d="M150 388 v34 M197 362 v60 M245 352 v70 M293 362 v60 M340 388 v34" stroke="#C98A52" stroke-width="8" stroke-linecap="round"/>
    <rect x="146" y="296" width="38" height="72" rx="19" fill="${STEP[2]}"/>${eyes(165, 318, 5)}<rect x="312" y="322" width="34" height="46" rx="17" fill="${STEP[1]}"/>${eyes(329, 340, 4.5)}</g>
  <g id="kids" transform="translate(470 136) scale(1.36) translate(-470 -150)">${Array.from({ length: N.children }, (_, i) => `<g id="kid${i}" opacity="0" style="transform-origin: 50% 100%"><rect x="${kidX(i)}" y="${kidY(i)}" width="46" height="60" rx="23" fill="${STEP[i % 5]}"/>${eyes(kidX(i) + 23, kidY(i) + 24)}
    <path d="M${kidX(i) + 15} ${kidY(i) + 39} q8 7 16 0" stroke="#fff" stroke-width="3.5" fill="none" stroke-linecap="round"/>
    <g id="kf${i}" opacity="0" style="transform-origin: 50% 100%"><circle cx="${kidX(i) + 23}" cy="${kidY(i) - 20}" r="15" fill="#FFC53D" stroke="#fff" stroke-width="3"/><circle cx="${kidX(i) + 18}" cy="${kidY(i) - 23}" r="2.2" fill="${INK}"/><circle cx="${kidX(i) + 28}" cy="${kidY(i) - 23}" r="2.2" fill="${INK}"/><path d="M${kidX(i) + 16} ${kidY(i) - 17} q7 7 14 0" stroke="${INK}" stroke-width="2.6" fill="none" stroke-linecap="round"/></g>
    <g id="kc${i}" opacity="0" style="transform-origin: 50% 100%"><circle cx="${kidX(i) + 23}" cy="${kidY(i) - 20}" r="15" fill="${STEP[2]}" stroke="#fff" stroke-width="3"/><path d="M${kidX(i) + 16} ${kidY(i) - 20} l5 6 l10 -12" stroke="#fff" stroke-width="4" fill="none" stroke-linecap="round" stroke-linejoin="round"/></g></g>`).join("")}
    <text id="kidsLabel" opacity="0" x="710" y="352" text-anchor="middle" ${FONT_B} font-size="27" fill="${TEAL}">${N.children} children · our users</text></g>
  <g id="adults" transform="translate(0 44)">${Array.from({ length: N.parents + N.teachers }, (_, j) => { const p = j < N.parents, x = adultX(j); return `<g id="ad${j}" opacity="0" style="transform-origin: 50% 100%">
    ${p ? `<circle cx="${x + 21}" cy="402" r="9" fill="#3B2A26"/>` : ""}<rect x="${x}" y="406" width="42" height="80" rx="21" fill="${p ? STEP[2] : TEAL}"/>${eyes(x + 21, 432, 5.2)}<path d="M${x + 14} 446 q7 6 14 0" stroke="#fff" stroke-width="3.2" fill="none" stroke-linecap="round"/>
    ${p ? "" : `<path d="M${x + 13} 462 h16 l-8 12Z" fill="#fff"/>`}</g>`; }).join("")}
    <text id="parLabel" opacity="0" x="${470 + (N.parents * 50 - 8) / 2}" y="528" text-anchor="middle" ${FONT_B} font-size="30" fill="${STEP[2]}">${N.parents} parents</text>
    <text id="teaLabel" opacity="0" x="${adultX(N.parents) + (N.teachers * 50 - 8) / 2}" y="528" text-anchor="middle" ${FONT_B} font-size="30" fill="${TEAL}">${N.teachers} teachers</text></g>
  ${[[N.paUseful, "Home learning", "parents", STEP[2]], [N.paOffline, "Works offline", "parents", STEP[2]], [N.teOverall, "Teaching", "teachers", TEAL]].map(([v, l1, l2, c], i) => { const x = 470 + i * 250; return `<g id="sc${i}" opacity="0" style="transform-origin: 50% 100%">
    <rect x="${x + 6}" y="128" width="226" height="226" rx="30" fill="#0B5F5B" opacity=".2"/><rect x="${x}" y="120" width="226" height="226" rx="30" fill="#fff" stroke="${c}" stroke-width="8"/>
    <text x="${x + 113}" y="214" text-anchor="middle" ${FONT_T} font-size="92" fill="${c}">${v.toFixed(1)}</text><text x="${x + 113}" y="242" text-anchor="middle" ${FONT_B} font-size="21" fill="#5B6B6B" font-weight="600">out of 5</text>
    <clipPath id="scc${i}"><rect x="${x + 28}" y="250" width="${170 * v / 5}" height="40"/></clipPath>
    ${[0, 1].map(k => `<g ${k ? `clip-path="url(#scc${i})"` : ""} fill="${k ? "#FFC53D" : "#E7E2D3"}">${[0, 1, 2, 3, 4].map(s => `<path d="${STAR}" transform="translate(${x + 45 + s * 34} 271) scale(.85)"/>`).join("")}</g>`).join("")}
    <text x="${x + 113}" y="318" text-anchor="middle" ${FONT_B} font-size="25" fill="#0B3F4A">${l1}</text><text x="${x + 113}" y="339" text-anchor="middle" ${FONT_B} font-size="18" fill="#5B6B6B" font-weight="600">${l2}</text></g>`; }).join("")}
  <g id="quote" opacity="0" style="transform-origin: 0% 100%"><rect x="110" y="270" width="130" height="200" rx="65" fill="${TEAL}"/><path d="M196 276 a65 65 0 0 1 44 60 v70 a65 65 0 0 1 -44 60Z" fill="#096B67"/>${eyes(175, 340, 13)}
    <path d="M152 380 q23 20 46 0" stroke="#fff" stroke-width="7" fill="none" stroke-linecap="round"/><path d="M158 420 h34 l-17 24Z" fill="#fff"/>
    <path d="M330 150 h810 a40 40 0 0 1 40 40 v190 a40 40 0 0 1 -40 40 h-740 l-90 50 22 -50 h-2 a40 40 0 0 1 -40 -40 v-190 a40 40 0 0 1 40 -40Z" fill="#fff" stroke="${TEAL}" stroke-width="9" stroke-linejoin="round"/>
    <text x="735" y="250" text-anchor="middle" ${FONT_B} font-size="52" fill="${TEAL}">“Very useful and engaging</text><text x="735" y="316" text-anchor="middle" ${FONT_B} font-size="52" fill="${TEAL}">for Grade 1 learners.”</text>
    <text x="735" y="378" text-anchor="middle" ${FONT_B} font-size="28" fill="#5B6B6B" font-weight="600">DepEd Grade 1 teacher</text></g>
  <g id="tests" opacity="0" style="transform-origin: 50% 50%"><text x="640" y="350" text-anchor="middle" ${FONT_T} font-size="230" fill="${STEP[2]}" stroke="#fff" stroke-width="22" stroke-linejoin="round" paint-order="stroke">${N.tests}</text>
    <text x="640" y="430" text-anchor="middle" ${FONT_B} font-size="60" fill="#0B3F4A">tests pass</text><rect x="570" y="456" width="140" height="48" rx="24" fill="${STEP[2]}"/><text x="640" y="489" text-anchor="middle" ${FONT_B} font-size="27" fill="#fff">0 fail</text></g>
  <g id="ticks">${[[150, 150], [300, 520], [1090, 170], [1000, 520], [210, 360], [1130, 370], [420, 130], [880, 120], [470, 560], [820, 570]].map(([x, y], i) => `<g id="tk${i}" opacity="0" style="transform-origin: 50% 50%"><circle cx="${x}" cy="${y}" r="26" fill="${STEP[2]}"/><path d="M${x - 12} ${y} l8 9 l16 -19" stroke="#fff" stroke-width="6" fill="none" stroke-linecap="round" stroke-linejoin="round"/></g>`).join("")}</g>
</g>`);
show("#board", 70.0, 99.9, 1, .3);
const hopIn = (sel, t) => K(sel, [[t, { opacity: 0, transform: "translateY(30px) scale(.3)" }], [t + .2, { opacity: 1, transform: "translateY(-12px) scale(1.12)" }, "ease-out"], [t + .36, { opacity: 1, transform: "translateY(0px) scale(1)" }]]);
const hop = (sel, t, h = 16) => K(sel, [[t, { transform: "translateY(0px) scale(1)" }], [t + .13, { transform: `translateY(${-h}px) scale(1.08)` }, "ease-out"], [t + .3, { transform: "translateY(0px) scale(1)" }]]);
const fade = (sel, t, to) => K(sel, [[t, { opacity: to === 0 ? 1 : 0 }], [t + .3, { opacity: to }]]);
// 30 people: 16 children step in first, then the parents and teachers
pop("#n30", t30 - .15, t15 - .25);
for (let i = 0; i < N.children; i++) hopIn("#kid" + i, t16 - .1 + i * .05);
show("#kidsLabel", t16 + .6, tGrown - .2); S("sfx_opening_pluck.wav", t16 - .1, .22);
for (let j = 0; j < N.parents + N.teachers; j++) hopIn("#ad" + j, (j < N.parents ? tPar - .15 + j * .05 : tTea - .15 + (j - N.parents) * .06));
show("#parLabel", tPar + .35, tQuote - .3); show("#teaLabel", tTea + .3, tQuote - .3); S("sfx_opening_pluck.wav", tPar - .1, .2); S("sfx_opening_pluck.wav", tTea - .1, .2);
// 15 of 16 chose the happy face; 12 of 16 passed Say It first try
pop("#n15", t15 - .1, t12 - .25); pop("#n12", t12 - .1, tGrown - .25);
K("#adults", [[t15 - .1, { opacity: 1 }], [t15 + .3, { opacity: .3 }], [tGrown - .3, { opacity: .3 }], [tGrown, { opacity: 1 }], [tQuote - .35, { opacity: 1 }], [tQuote - .05, { opacity: 0 }]]);
for (let i = 0; i < N.children; i++) {
  if (i < N.happy) { const t = tHappy + .1 + i * .085; pop("#kf" + i, t, t12 - .35); hop("#kid" + i, t); }
  if (i < N.sayit) { const t = tPassed - .1 + i * .11; pop("#kc" + i, t, tGrown - .35); hop("#kid" + i, t); }
}
S("sfx_correct_chime.mp3", tHappy + .1, .22); S("sfx_correct_chime.mp3", tPassed - .1, .22);
// the grown-ups who open the door: the bridge, and their three scores
K("#kids", [[tGrown - .3, { opacity: 1 }], [tGrown, { opacity: 0 }]]);
pop("#bridge", tGrown - .1, tQuote - .3);
for (let j = 0; j < N.parents + N.teachers; j++) hop("#ad" + j, tGrown + .25 + j * .06, 20);
[tS1, tS2, tS3].forEach((t, i) => { K("#sc" + i, [[t - .15, { opacity: 0, transform: "translateY(40px) scale(.4)" }], [t + .1, { opacity: 1, transform: "translateY(-8px) scale(1.08)" }, "ease-out"], [t + .28, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tQuote - .35, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tQuote - .08, { opacity: 0, transform: "translateY(0px) scale(.6)" }]]); S("sfx_opening_pluck.wav", t - .1, .24); });
// the teacher's words
pop("#quote", tQuote - .05, tTests - .3);
// 691 tests pass
pop("#tests", tTests - .1, 99.75); S("sfx_streak_badge_unlock.mp3", tTests, .3);
for (let i = 0; i < 10; i++) pop("#tk" + i, tTests + .25 + i * .13, 99.6 + i * .01);

/* =====================================================================================================
   Beat 5 — back with Mia and Lily, then the team, then the logo
   ===================================================================================================== */
const tNo1 = cue("b5_01", "signal"), tNo2 = cue("b5_01", "ads"), tNo3 = cue("b5_01", "child"), tTars = cue("b5_02", "tarsier"), tOne = cue("b5_02", "one"),
  tWe = cue("b5_03", "We"), tGroup = cue("b5_03", "Group"), tThis = cue("b5_03", "this"), tPlayIt = LINE.b5_04.start;
K("#cam", [[69.7, CAM(-60, 30, 1.04)], [100.0, CAM(-60, 30, 1.04)], [101.0, CAM(-70, 24, 1.1)], [105.9, CAM(-74, 22, 1.12)], [107.0, CAM(-96, 12, 1.2)], [110.3, CAM(-104, 8, 1.24)], [111.1, CAM(0, 40, 1)]]);
lilyPoses([[100.1, "idle"], [tTars - .15, "celebrating"], [110.4, "waving"]]);
legs(100, 116.5, 14, 5.2);
// three things Mia no longer needs
const noStamp = (i, x, icon, label) => UI(`<g id="no${i}" opacity="0" style="transform-origin: 50% 50%">
  <circle cx="${x + 5}" cy="138" r="78" fill="#0B5F5B" opacity=".25"/><circle cx="${x}" cy="130" r="74" fill="#fff" stroke="${STEP[3]}" stroke-width="11"/>${icon(x, 130)}
  <path d="M${x - 50} 80 L${x + 50} 180" stroke="${STEP[3]}" stroke-width="13" stroke-linecap="round"/>
  <rect x="${x - 112}" y="218" width="224" height="52" rx="26" fill="${STEP[3]}" stroke="#fff" stroke-width="6"/><text x="${x}" y="254" text-anchor="middle" ${FONT_T} font-size="30" fill="#fff">${label}</text></g>`);
noStamp(0, 330, (x, y) => [0, 1, 2, 3].map(k => `<rect x="${x - 34 + k * 19}" y="${y + 26 - (k + 1) * 14}" width="13" height="${(k + 1) * 14}" rx="5" fill="${TEAL}"/>`).join(""), "No signal");
noStamp(1, 640, (x, y) => `<rect x="${x - 40}" y="${y - 26}" width="80" height="52" rx="12" fill="#FFC53D"/><text x="${x}" y="${y + 14}" text-anchor="middle" ${FONT_T} font-size="38" fill="${INK}">AD</text>`, "No ads");
noStamp(2, 950, (x, y) => `<circle cx="${x}" cy="${y}" r="36" fill="${STEP[0]}"/><circle cx="${x}" cy="${y}" r="26" fill="#fff"/><path d="M${x} ${y} v-17 M${x} ${y} h12" stroke="${INK}" stroke-width="6" stroke-linecap="round"/>`, "No waiting");
[tNo1 - .35, tNo2 - .4, tNo3 - .3].forEach((t, i) => { pop("#no" + i, t, 105.7 + i * .06); S("sfx_opening_pluck.wav", t, .24); });
// Mia opens her book again and reads
K("#bookClosed", [[100.5, { opacity: 1 }, "steps(1)"], [100.7, { opacity: 0 }, "steps(1)"]]);
K("#bookOpen", [[100.69, { opacity: 0, transform: "scaleY(.3)" }, "steps(1)"], [100.7, { opacity: 1, transform: "scaleY(.3)" }, "ease-out"], [100.95, { opacity: 1, transform: "scaleY(1.08)" }], [101.1, { opacity: 1, transform: "scaleY(1)" }]]);
K("#armL", [[100.3, R(0, 0, 0)], [100.6, R(0, -6, -22)], [101.0, R(0, 0, 0)], [101.6, R(0, -4, -30)], [102.3, R(0, -6, -44)], [103.0, R(0, -4, -32)], [103.7, R(0, -6, -46)], [104.4, R(0, -4, -34)], [105.2, R(0, -6, -42)], [105.9, R(0, 0, 0)]]);
K("#armR", [[100.3, R(0, 0, 0)], [100.6, R(0, -6, 22)], [101.0, R(0, 0, 0)]]);
K("#miaHead", [[69.6, HD(-4, -2)], [100.0, HD(-4, -2)], [101.2, HD(7, 4)], [105.8, HD(7, 4)], [106.4, HD(-5, -2)], [110.4, HD(-5, -2)]]);
K("#pupils", [[69.6, PU(5, -5)], [100.0, PU(5, -5)], [101.2, PU(-3, 6)], [103.4, PU(3, 6)], [105.6, PU(-2, 6)], [106.3, PU(7, -2)], [110.4, PU(7, -2)]]);
// one sound at a time
lilyHop(tTars - .1, 210, 210, 64); sayIt(tOne - .05); miaCheer(tOne + 1.15, 110.2); lilyHop(tOne + 1.2, 210, 210, 50);
[0, 1].forEach(i => K("#hrt" + i, [[tTars + .3 + i * .22, { opacity: 0, transform: "translateY(20px) scale(.3)" }], [tTars + .55 + i * .22, { opacity: 1, transform: "translateY(0px) scale(1.2)" }, "ease-out"], [tTars + 1.4 + i * .22, { opacity: 1, transform: "translateY(-26px) scale(1)" }], [tTars + 1.8 + i * .22, { opacity: 0, transform: "translateY(-44px) scale(.8)" }]]));
// the team
const names = D.team.members.map(n => n.replace(/\s*\(.*\)/, ""));
UI(`<rect id="endVeil" opacity="0" x="-10" y="-10" width="1300" height="740" fill="#FFF8E7"/>
  <g id="ribbon" opacity="0" style="transform-origin: 50% 50%"><rect x="256" y="70" width="780" height="92" rx="46" fill="#0B5F5B" opacity=".28"/><rect x="250" y="62" width="780" height="92" rx="46" fill="${TEAL}" stroke="#fff" stroke-width="8"/>
    <text x="640" y="125" text-anchor="middle" ${FONT_T} font-size="50" fill="#fff">Group 56 · CIT-University</text></g>
  ${names.map((n, i) => { const x = 200 + i * 220, c = STEP[i % 5]; return `<g id="tm${i}" opacity="0" style="transform-origin: 50% 100%">
    <g class="wave" style="transform-origin: 0% 100%; animation-delay:-${i * .23}s"><path d="M${x + 52} 318 l34 -44" stroke="${c}" stroke-width="26" stroke-linecap="round"/><circle cx="${x + 88}" cy="272" r="15" fill="${c}"/></g>
    <rect x="${x - 62}" y="230" width="124" height="176" rx="62" fill="${c}"/><path d="M${x + 20} 236 a62 62 0 0 1 42 56 v52 a62 62 0 0 1 -42 56Z" fill="#000" opacity=".1"/>
    ${eyes(x, 296, 13)}<ellipse cx="${x - 38}" cy="324" rx="11" ry="7" fill="#F4A0A0" opacity=".8"/><ellipse cx="${x + 38}" cy="324" rx="11" ry="7" fill="#F4A0A0" opacity=".8"/>
    <path d="M${x - 18} 330 q18 18 36 0" stroke="#fff" stroke-width="6" fill="none" stroke-linecap="round"/>
    <rect x="${x - 104}" y="424" width="208" height="46" rx="23" fill="#fff" stroke="${c}" stroke-width="6"/><text x="${x}" y="455" text-anchor="middle" ${FONT_B} font-size="${n.length > 15 ? 20 : 23}" fill="#0B3F4A">${n}</text>
    ${i === 0 ? `<rect x="${x - 34}" y="476" width="68" height="28" rx="14" fill="#FFC53D"/><text x="${x}" y="496" text-anchor="middle" ${FONT_B} font-size="18" fill="#3A2A00">Lead</text>` : ""}</g>`; }).join("")}
  <text id="adviser" opacity="0" x="640" y="560" text-anchor="middle" ${FONT_B} font-size="26" fill="#5B6B6B" font-weight="600">${D.team.adviser}</text>
  <g id="endCard" opacity="0"><text x="640" y="352" text-anchor="middle" ${FONT_B} font-size="40" fill="${TEAL}">Offline phonics for Grade 1 learners</text>
    <g id="lilyEnd" style="transform-origin: 50% 100%"><image href="img/lily_waving.png" x="470" y="350" width="300" height="300"/><image id="lilyEndTalk" href="img/lily_waving_talk.png" x="470" y="350" width="300" height="300" opacity="0"/></g>
    <g id="playit" style="transform-origin: 50% 50%"><text x="975" y="452" text-anchor="middle" transform="rotate(-8 975 452)" ${FONT_T} font-size="78" fill="#FFC53D" stroke="${TEAL}" stroke-width="12" stroke-linejoin="round" paint-order="stroke">Play it!</text></g></g>`);
K("#endVeil", [[tWe - .35, { opacity: 0 }], [tWe, { opacity: .86 }], [tPlayIt - .3, { opacity: .86 }], [tPlayIt, { opacity: .97 }]]);
pop("#ribbon", tGroup - .35, tPlayIt - .3);
names.forEach((_, i) => { const t = tWe + .25 + i * .2; K("#tm" + i, [[t, { opacity: 0, transform: "translateY(60px) scale(.3)" }], [t + .22, { opacity: 1, transform: "translateY(-14px) scale(1.1)" }, "ease-out"], [t + .4, { opacity: 1, transform: "translateY(0px) scale(1)" }],
  [tThis, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tThis + .15, { opacity: 1, transform: "translateY(-18px) scale(1.05)" }], [tThis + .32, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tPlayIt - .35, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tPlayIt - .1, { opacity: 0, transform: "translateY(30px) scale(.6)" }]]); S("sfx_opening_pluck.wav", t, .2); });
show("#adviser", tWe + 1.5, tPlayIt - .35);
// the logo and Lily's last word
$("#ui").appendChild($("#logo"));
K("#logo", [[tPlayIt - .25, { opacity: 0, transform: "translate(0px,60px) scale(.2) rotate(-8deg)" }], [tPlayIt + .1, { opacity: 1, transform: "translate(0px,60px) scale(1.2) rotate(3deg)" }, "ease-out"], [tPlayIt + .32, { opacity: 1, transform: "translate(0px,60px) scale(1.06) rotate(0deg)" }],
  [118.4, { opacity: 1, transform: "translate(0px,60px) scale(1.06) rotate(0deg)" }], [118.9, { opacity: 1, transform: "translate(0px,60px) scale(1.1) rotate(0deg)" }], [119.6, { opacity: 1, transform: "translate(0px,60px) scale(1.06) rotate(0deg)" }]]);
show("#endCard", tPlayIt - .1, 120.5, 1, .25);
K("#lilyEnd", [[tPlayIt - .1, { transform: "translateY(120px) scale(.6,.6)" }], [tPlayIt + .2, { transform: "translateY(-40px) scale(.96,1.08)" }, "ease-out"], [tPlayIt + .42, { transform: "translateY(0px) scale(1.1,.88)" }], [tPlayIt + .58, { transform: "translateY(0px) scale(1,1)" }],
  [118.3, { transform: "translateY(0px) scale(1,1)" }], [118.5, { transform: "translateY(-26px) scale(.97,1.05)" }], [118.75, { transform: "translateY(0px) scale(1.06,.92)" }], [118.9, { transform: "translateY(0px) scale(1,1)" }]]);
flap("#lilyEndTalk", tPlayIt, LINE.b5_04.end);
K("#playit", [[tPlayIt, { opacity: 0, transform: "scale(.2)" }], [tPlayIt + .25, { opacity: 1, transform: "scale(1.2)" }, "ease-out"], [tPlayIt + .45, { opacity: 1, transform: "scale(1)" }], [119.0, { opacity: 1, transform: "scale(1.06)" }]]);
S("sfx_level_complete_fanfare.mp3", tPlayIt - .2, .2); S("sfx_correct_chime.mp3", tPlayIt + .6, .26);

// the two butterflies keep wandering for the rest of the film
[["#bf1", 930, 450, 1], ["#bf2", 370, 330, -1]].forEach(([b, x0, y0, d]) => { const f = []; for (let t = 27.6, i = 0; t <= 120; t += 2.4, i++) f.push([t, { transform: `translate(${Math.round(x0 + d * 150 * Math.sin(i * .9) + 60 * Math.sin(i * 2.3))}px,${Math.round(y0 - 110 + 110 * Math.cos(i * 1.3))}px)` }]); K(b, f); });
