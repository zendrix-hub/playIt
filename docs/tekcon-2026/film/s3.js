// Beat 4 (1:10 to 1:40): results and validation, on a board over the scene.  Beat 5 (1:40 to 2:00): impact and close.
// Every number comes from data.js (built from round2_data.json). If the status there is ever "projected", the board carries a PROJECTED tag.
const N = D.numbers;
K("#bubble", [[LINE.b2_03.start - .15, { opacity: 1, transform: "scale(1)" }], [LINE.b2_03.start + .15, { opacity: 0, transform: "scale(.6)" }]]);      // Lily's "Hi there!" bubble leaves before the logo arrives

/* =====================================================================================================
   Beat 4 — the board
   ===================================================================================================== */
const t30 = cue("b4_01", "30"), t16 = cue("b4_01", "16"), tPar = cue("b4_01", "parents"), tTea = cue("b4_01", "teachers"),
  t15 = cue("b4_02", "15"), tHappy = cue("b4_02", "children"), t14 = cue("b4_02", "14"), tAgain = cue("b4_02", "again"),
  tGrown = cue("b4_03", "grown"), tS1 = cue("b4_03", "4.6"), tS2 = cue("b4_03", "4.4"), tS3 = cue("b4_03", "4.7"),
  tQuote = cue("b4_04", "One");
const big2 = (id, n, sub, extra = "") => `<g id="${id}" opacity="0" style="transform-origin: 50% 50%">
  <text x="245" y="250" text-anchor="middle" ${FONT_T} font-size="170" fill="${TEAL}" stroke="#fff" stroke-width="18" stroke-linejoin="round" paint-order="stroke">${n}</text>
  <text x="245" y="312" text-anchor="middle" ${FONT_B} font-size="48" fill="#0B3F4A">${sub}</text>${extra}</g>`;
const eyes = (x, y, r = 6) => `<circle cx="${x - r * 1.25}" cy="${y}" r="${r}" fill="#fff"/><circle cx="${x + r * 1.25}" cy="${y}" r="${r}" fill="#fff"/><circle cx="${x - r * 1.1}" cy="${y + .6}" r="${r * .55}" fill="${INK}"/><circle cx="${x + r * 1.4}" cy="${y + .6}" r="${r * .55}" fill="${INK}"/>`;
// a picture in a white card with a teal edge and a soft shadow; label is a pill on the bottom edge
const photo = (id, img, x, y, w, h, rot, label = "", c = TEAL, extra = "") => `<g id="${id}" opacity="0" style="transform-origin: 50% 50%"><g transform="rotate(${rot} ${x + w / 2} ${y + h / 2})">
  <rect x="${x - 4}" y="${y + 6}" width="${w + 20}" height="${h + 20}" rx="26" fill="#0B5F5B" opacity=".22"/><rect x="${x - 10}" y="${y - 10}" width="${w + 20}" height="${h + 20}" rx="26" fill="#fff" stroke="${c}" stroke-width="7"/>
  <clipPath id="${id}Clip"><rect x="${x}" y="${y}" width="${w}" height="${h}" rx="17"/></clipPath>
  ${img.startsWith("<") ? `<g clip-path="url(#${id}Clip)">${img}</g>` : `<image href="img/${img}" x="${x}" y="${y}" width="${w}" height="${h}" preserveAspectRatio="xMidYMid slice" clip-path="url(#${id}Clip)"/>`}
  ${label ? `<rect x="${x + w / 2 - (label.length * 8.6 + 34)}" y="${y + h - 8}" width="${2 * (label.length * 8.6 + 34)}" height="50" rx="25" fill="${c}" stroke="#fff" stroke-width="6"/><text x="${x + w / 2}" y="${y + h + 26}" text-anchor="middle" ${FONT_B} font-size="27" fill="#fff">${label}</text>` : ""}${extra}</g></g>`;
// ---- the people on the board, drawn like Mia (no outlines, two-tone shading, big glossy eyes) and dressed like the
// learners and the parents and teachers in the team's pictures: school uniforms, braids, a floral blouse, barong and filipiniana with IDs
const SKIN = ["#DDA273", "#C98B5C"], SKIN2 = ["#C98E62", "#B27749"], HAIR = "#3B2A26";
const glossy = (x, y, r) => `<circle cx="${x}" cy="${y}" r="${r}" fill="#2B1E1B"/><circle cx="${x - r * .35}" cy="${y - r * .4}" r="${r * .38}" fill="#fff"/><circle cx="${x + r * .35}" cy="${y + r * .3}" r="${r * .17}" fill="#fff"/>`;
const face = (cx, cy, r, sk, smile = true) => `<circle cx="${cx - r}" cy="${cy + r * .15}" r="${r * .26}" fill="${sk[1]}"/><circle cx="${cx + r}" cy="${cy + r * .15}" r="${r * .26}" fill="${sk[1]}"/>
  <circle cx="${cx + r * .12}" cy="${cy}" r="${r}" fill="${sk[1]}"/><circle cx="${cx - r * .06}" cy="${cy - r * .03}" r="${r * .97}" fill="${sk[0]}"/>
  ${glossy(cx - r * .37, cy + r * .12, r * .25)}${glossy(cx + r * .37, cy + r * .12, r * .25)}
  <ellipse cx="${cx - r * .62}" cy="${cy + r * .45}" rx="${r * .17}" ry="${r * .11}" fill="#F4A0A0"/><ellipse cx="${cx + r * .62}" cy="${cy + r * .45}" rx="${r * .17}" ry="${r * .11}" fill="#F4A0A0"/>
  ${smile ? `<path d="M${cx - r * .24} ${cy + r * .45} q${r * .24} ${r * .3} ${r * .48} 0Z" fill="#8A3B3B"/>` : ""}`;
// a Grade 1 learner in a 46 x 64 box whose top left is (x, y); v picks the look
function kidFig(x, y, v, noHands = "") {
  const cx = x + 23, girl = [1, 2, 3, 5].includes(v % 6), green = v % 4 >= 2, low = green ? ["#2E8B4E", "#237040"] : ["#3552A0", "#2A4182"], sk = v % 5 === 3 ? SKIN2 : SKIN;
  const hairBack = girl ? (v % 6 === 1 ? `<ellipse cx="${cx - 16}" cy="${y + 30}" rx="3.4" ry="7" fill="${HAIR}"/><ellipse cx="${cx + 16}" cy="${y + 30}" rx="3.4" ry="7" fill="${HAIR}"/><circle cx="${cx - 16}" cy="${y + 25}" r="2.6" fill="${STEP[3]}"/><circle cx="${cx + 16}" cy="${y + 25}" r="2.6" fill="${STEP[3]}"/>`
    : `<circle cx="${cx}" cy="${y + 17}" r="16.5" fill="${HAIR}"/><rect x="${cx - 18}" y="${y + 14}" width="9" height="${v % 6 === 5 ? 22 : 17}" rx="4.5" fill="${HAIR}"/><rect x="${cx + 9}" y="${y + 14}" width="9" height="${v % 6 === 5 ? 22 : 17}" rx="4.5" fill="${HAIR}"/>`) : "";
  const hairTop = girl ? `<path d="M${cx - 16} ${y + 18} Q${cx - 16} ${y + 1} ${cx} ${y + 1} Q${cx + 17} ${y + 1} ${cx + 16} ${y + 18} Q${cx + 9} ${y + 9} ${cx + 1} ${y + 12} Q${cx - 8} ${y + 8} ${cx - 16} ${y + 18}Z" fill="${HAIR}"/>`
    : `<path d="M${cx - 16} ${y + 17} Q${cx - 17} ${y + 1} ${cx - 1} ${y} Q${cx + 13} ${y - 1} ${cx + 16} ${y + 14} Q${cx + 8} ${y + 8} ${cx - 3} ${y + 10} Q${cx - 11} ${y + 11} ${cx - 16} ${y + 17}Z" fill="${HAIR}"/><path d="M${cx - 2} ${y + 1} l3 -5 l2 5Z" fill="${HAIR}"/>`;
  const extra = (girl && v % 6 === 3 ? `<path d="M${cx - 14} ${y + 9} Q${cx} ${y - 4} ${cx + 14} ${y + 9}" stroke="${STEP[3]}" stroke-width="3" fill="none" stroke-linecap="round"/>` : "")
    + (girl && v % 6 === 2 ? `<rect x="${cx - 13}" y="${y + 6}" width="7" height="3.4" rx="1.7" fill="#F48FB1" transform="rotate(-25 ${cx - 9} ${y + 8})"/>` : "")
    + (!girl && v % 6 === 4 ? `<g stroke="${HAIR}" stroke-width="1.3" fill="none"><circle cx="${cx - 5.5}" cy="${y + 20}" r="4.8"/><circle cx="${cx + 5.5}" cy="${y + 20}" r="4.8"/><path d="M${cx - .7} ${y + 20} h1.4"/></g>` : "");
  const bottom = girl ? `<path d="M${cx - 12} ${y + 45} h24 l4 12 h-32Z" fill="${low[0]}"/><path d="M${cx + 3} ${y + 45} h9 l4 12 h-11Z" fill="${low[1]}"/>`
    : `<rect x="${cx - 12}" y="${y + 45}" width="24" height="11" rx="3" fill="${low[0]}"/><rect x="${cx + 2}" y="${y + 45}" width="10" height="11" rx="3" fill="${low[1]}"/>`;
  return `<rect x="${cx - 9}" y="${y + 55}" width="6" height="7" fill="${sk[0]}"/><rect x="${cx + 3}" y="${y + 55}" width="6" height="7" fill="${sk[0]}"/>
    <rect x="${cx - 9}" y="${y + 59}" width="6" height="3" fill="#fff"/><rect x="${cx + 3}" y="${y + 59}" width="6" height="3" fill="#fff"/><ellipse cx="${cx - 6.5}" cy="${y + 63.5}" rx="5" ry="2.6" fill="#2B1E1B"/><ellipse cx="${cx + 6.5}" cy="${y + 63.5}" rx="5" ry="2.6" fill="#2B1E1B"/>
    ${bottom}${noHands.includes("L") ? "" : `<circle cx="${cx - 14}" cy="${y + 44}" r="3.6" fill="${sk[0]}"/>`}${noHands.includes("R") ? "" : `<circle cx="${cx + 14}" cy="${y + 44}" r="3.6" fill="${sk[1]}"/>`}
    <rect x="${cx - 12.5}" y="${y + 32}" width="25" height="16" rx="7" fill="#fff"/><rect x="${cx + 2}" y="${y + 32}" width="10.5" height="16" rx="6" fill="#DDE6EC"/><path d="M${cx - 6} ${y + 32} l6 5 l6 -5Z" fill="#E3EAEE"/>
    ${girl ? `<path d="M${cx - 3.5} ${y + 37} l3.5 2 l3.5 -2 v4 l-3.5 -2 l-3.5 2Z" fill="${green ? "#2E8B4E" : "#3552A0"}"/>` : ""}
    ${hairBack}${face(cx, y + 18, 15, sk)}${hairTop}${extra}`;
}
// a parent or a teacher in a 42 x 82 box whose top left is (x, y)
function adultFig(x, y, teacher, v, noHands = "") {
  const cx = x + 21, female = teacher ? v % 5 !== 1 && v % 5 !== 4 : v % 2 === 0, sk = v % 3 === 1 ? SKIN2 : SKIN;
  const top = teacher ? ["#F3E6CC", "#E2D2B0"] : [["#E86A7A", "#CF5465"], ["#8C8F4E", "#777A3F"], ["#F7922F", "#DE7B1C"], ["#3BA7D9", "#2A8DBD"]][v % 4];
  const low = teacher ? (female ? "#4A3A33" : "#2F3540") : ["#4A6FA5", "#6B4A3A"][v % 2];
  const deco = teacher ? `<path d="M${cx - 8} ${y + 40} v22 M${cx + 8} ${y + 40} v22" stroke="#D8C49C" stroke-width="2.4" stroke-dasharray="3 3"/><path d="M${cx - 6} ${y + 33} L${cx} ${y + 49} L${cx + 6} ${y + 33}" stroke="#3552A0" stroke-width="2" fill="none"/><rect x="${cx - 4}" y="${y + 48}" width="8" height="10" rx="1.5" fill="#fff"/><rect x="${cx - 2.4}" y="${y + 50}" width="4.8" height="3.6" fill="#9FB4D9"/>`
    : v % 4 === 0 ? `<circle cx="${cx - 7}" cy="${y + 42}" r="2.4" fill="#FFC53D"/><circle cx="${cx + 5}" cy="${y + 47}" r="2.4" fill="${TEAL}"/><circle cx="${cx - 4}" cy="${y + 55}" r="2.4" fill="#FFC53D"/><circle cx="${cx + 8}" cy="${y + 58}" r="2" fill="#FFF1B8"/>`
    : `<path d="M${cx - 6} ${y + 33} l6 6 l6 -6Z" fill="${top[1]}"/>`;
  const hairBack = female ? (v % 3 === 0 && !teacher ? `<circle cx="${cx}" cy="${y + 2}" r="6" fill="${HAIR}"/>` : `<circle cx="${cx}" cy="${y + 15}" r="14.5" fill="${HAIR}"/><rect x="${cx - 16}" y="${y + 12}" width="9" height="${teacher ? 26 : 22}" rx="4.5" fill="${HAIR}"/><rect x="${cx + 7}" y="${y + 12}" width="9" height="${teacher ? 26 : 22}" rx="4.5" fill="${HAIR}"/>`) : "";
  const hairTop = female ? `<path d="M${cx - 13} ${y + 15} Q${cx - 13} ${y + 1} ${cx} ${y + 1} Q${cx + 14} ${y + 1} ${cx + 13} ${y + 15} Q${cx + 4} ${y + 6} ${cx - 13} ${y + 15}Z" fill="${HAIR}"/>`
    : `<path d="M${cx - 13} ${y + 14} Q${cx - 13} ${y} ${cx + 1} ${y} Q${cx + 13} ${y} ${cx + 13} ${y + 11} Q${cx + 2} ${y + 6} ${cx - 6} ${y + 8} Q${cx - 11} ${y + 9} ${cx - 13} ${y + 14}Z" fill="${HAIR}"/>`;
  return `<rect x="${cx - 11}" y="${y + 60}" width="22" height="21" rx="4" fill="${low}"/><rect x="${cx - .8}" y="${y + 66}" width="1.6" height="15" fill="#000" opacity=".18"/>
    <ellipse cx="${cx - 6}" cy="${y + 81.5}" rx="6" ry="2.6" fill="#2B1E1B"/><ellipse cx="${cx + 6}" cy="${y + 81.5}" rx="6" ry="2.6" fill="#2B1E1B"/>
    <rect x="${cx - 16}" y="${y + 30}" width="32" height="36" rx="12" fill="${top[0]}"/><rect x="${cx + 3}" y="${y + 30}" width="13" height="36" rx="10" fill="${top[1]}"/>
    ${noHands.includes("L") ? "" : `<circle cx="${cx - 17}" cy="${y + 60}" r="4.2" fill="${sk[0]}"/>`}${noHands.includes("R") ? "" : `<circle cx="${cx + 17}" cy="${y + 60}" r="4.2" fill="${sk[1]}"/>`}${deco}
    ${hairBack}${face(cx, y + 17, 13, sk)}${hairTop}`;
}
// ---- the two group pictures, redrawn in code from the references learners.jpg and teachers.jpg (only the team picture is used as a file)
const cardBack = (x, y, w, h) => `<rect x="${x}" y="${y}" width="${w}" height="${h}" fill="#E1F3FA"/><circle cx="${x + w - 52}" cy="${y + 40}" r="22" fill="#FFC53D" opacity=".85"/>
  <path d="M${x} ${y + h * .66} Q${x + w * .2} ${y + h * .44} ${x + w * .42} ${y + h * .62} Q${x + w * .64} ${y + h * .42} ${x + w} ${y + h * .6} V${y + h} H${x}Z" fill="#D8B48A" opacity=".55"/>
  <rect x="${x}" y="${y + h * .76}" width="${w}" height="${h * .24}" fill="#C4F2DA"/>
  <path d="M${x} ${y + 16} Q${x + w / 2} ${y + 40} ${x + w} ${y + 14}" stroke="#A8703F" stroke-width="2" fill="none"/>${[0, 1, 2, 3, 4, 5, 6, 7].map(k => { const fx = x + 18 + k * (w - 36) / 7, fy = y + 16 + 24 * Math.sin(Math.PI * (k + .5) / 8) * .9; return `<path d="M${fx - 7} ${fy} h14 l-7 13Z" fill="${STEP[k % 4]}"/>`; }).join("")}`;
const skinHand = (x, y, sk = SKIN) => `<circle cx="${x}" cy="${y}" r="3.8" fill="${sk[0]}"/>`;
// learner props, in the learner's own 46 x 64 box: an open picture book, a tablet, a raised pointing hand, a backpack
const kidBook = (cx, y, c) => `<rect x="${cx - 13}" y="${y + 34}" width="26" height="16" rx="2" fill="${c}"/><rect x="${cx - 11.5}" y="${y + 35.5}" width="10.8" height="13" rx="1" fill="#fff"/><rect x="${cx + .7}" y="${y + 35.5}" width="10.8" height="13" rx="1" fill="#fff"/>
  <rect x="${cx - 10}" y="${y + 38}" width="8" height="5" rx="1" fill="#7CC4E8"/><circle cx="${cx + 6}" cy="${y + 41}" r="3" fill="#F7922F"/><path d="M${cx - 10} ${y + 45.5} h7 M${cx + 2} ${y + 46} h8" stroke="#C9C2B2" stroke-width="1.2"/>${skinHand(cx - 13, y + 43)}${skinHand(cx + 13, y + 43)}`;
const kidTablet = (cx, y) => `<rect x="${cx - 11}" y="${y + 33}" width="22" height="16" rx="3" fill="#33363D"/><rect x="${cx - 9}" y="${y + 35}" width="18" height="12" rx="1.5" fill="#FFC53D"/><circle cx="${cx - 4}" cy="${y + 39}" r="3.6" fill="#F2545B"/><circle cx="${cx + 3}" cy="${y + 42}" r="4" fill="#3BA7D9"/><circle cx="${cx + 6}" cy="${y + 37}" r="2.6" fill="#2E9A3E"/>${skinHand(cx - 11, y + 43)}${skinHand(cx + 11, y + 43)}`;
const kidPoint = (cx, y) => `<path d="M${cx + 11} ${y + 38} L${cx + 19} ${y + 31}" stroke="#fff" stroke-width="6" stroke-linecap="round"/>${skinHand(cx + 21, y + 29)}`;
const kidPack = (cx, y, c) => `<rect x="${cx - 17}" y="${y + 31}" width="34" height="20" rx="7" fill="${c}"/>`;
const learnersScene = (x, y, w, h) => { const S = 1.78, sp = (w - 34 - 46 * S) / 5, top = y + h - 14 - 64 * S;
  const look = [[0, "", kidBook, "#2E9A3E", "LR"], [1, "pack", null, "#F48FB1", ""], [2, "", kidBook, "#7E57C2", "LR"], [3, "pack", kidPoint, "#A8703F", "R"], [4, "", kidTablet, "", "LR"], [5, "pack", null, "#F48FB1", ""]];
  return cardBack(x, y, w, h) + look.map(([v, pack, prop, c, hide], i) => { const gx = x + 17 + i * sp;
    return `<g transform="translate(${gx} ${top}) scale(${S})">${pack ? kidPack(23, 0, c) : ""}${kidFig(0, 0, v, hide)}${prop ? prop(23, 0, c) : ""}</g>`; }).join(""); };
// parent and teacher props, in the grown-up's 42 x 82 box: a phone, a picture book held to the chest, a lesson planner, a raised open hand
const adPhone = (cx, y) => `<rect x="${cx - 15}" y="${y + 40}" width="11" height="19" rx="2.5" fill="#33363D" transform="rotate(-12 ${cx - 9} ${y + 50})"/>${skinHand(cx - 9, y + 56)}${skinHand(cx + 2, y + 47)}`;
const adBook = (cx, y) => `<rect x="${cx - 6}" y="${y + 36}" width="17" height="23" rx="2" fill="#7CC4E8" transform="rotate(-6 ${cx + 2} ${y + 47})"/><rect x="${cx - 4}" y="${y + 39}" width="13" height="5" rx="1" fill="#F2545B" transform="rotate(-6 ${cx + 2} ${y + 47})"/><rect x="${cx - 3}" y="${y + 47}" width="11" height="8" rx="1" fill="#8FD18A" transform="rotate(-6 ${cx + 2} ${y + 47})"/>${skinHand(cx + 9, y + 50)}`;
const adPlanner = (cx, y) => `<g transform="rotate(14 ${cx + 10} ${y + 52})"><rect x="${cx + 1}" y="${y + 41}" width="18" height="22" rx="2" fill="#fff"/><path d="M${cx + 3} ${y + 47} h14 M${cx + 3} ${y + 52} h14 M${cx + 3} ${y + 57} h14 M${cx + 8} ${y + 44} v17 M${cx + 13} ${y + 44} v17" stroke="#7FA6D8" stroke-width="1"/><rect x="${cx + 1}" y="${y + 41}" width="18" height="3.4" fill="#3552A0"/></g>${skinHand(cx + 9, y + 60)}`;
const adWave = (cx, y, sleeve) => `<path d="M${cx + 13} ${y + 40} L${cx + 22} ${y + 31}" stroke="${sleeve}" stroke-width="7" stroke-linecap="round"/><circle cx="${cx + 24}" cy="${y + 28}" r="4.6" fill="${SKIN[0]}"/>`;
const adultsScene = (x, y, w, h) => { const S = 1.52, sp = (w - 30 - 42 * S) / 4, top = y + h - 10 - 84 * S;
  const look = [[false, 0, adPhone, "LR"], [false, 1, adWave, "R", "#777A3F"], [true, 0, adBook, "R"], [true, 2, adWave, "R", "#E2D2B0"], [true, 1, adPlanner, "R"]];
  return cardBack(x, y, w, h) + look.map(([teacher, v, prop, hide, sleeve], i) => `<g transform="translate(${x + 15 + i * sp} ${top}) scale(${S})">${adultFig(0, 0, teacher, v, hide)}${prop(21, 0, sleeve)}</g>`).join(""); };
// Beat 1's thought bubbles show the same people as the results board, drawn smaller and fewer:
// the teacher (with her picture book) in front of a crowded class, and nanay (the mum with her phone) at work
{
  const tb1 = $("#tb1Art"), tb2 = $("#tb2Art");
  if (tb1) tb1.innerHTML = `<clipPath id="tb1Clip"><circle cx="300" cy="186" r="79"/></clipPath><g clip-path="url(#tb1Clip)">
    ${[[234, 126, 1], [330, 126, 4]].map(([x, y, v]) => `<g transform="translate(${x} ${y}) scale(.72)">${kidFig(0, 0, v)}</g>`).join("")}
    <g transform="translate(279 98)">${adultFig(0, 0, true, 0, "R")}${adBook(21, 0)}</g>
    ${[2, 0, 5, 3, 6].map((v, i) => `<g class="kid" style="animation-delay:${-i * .13}s"><g transform="translate(${222 + i * 30} ${168 + (i % 2) * 5}) scale(.78)">${kidFig(0, 0, v)}</g></g>`).join("")}</g>`;
  if (tb2) tb2.innerHTML = `<clipPath id="tb2Clip"><circle cx="520" cy="128" r="75"/></clipPath><g clip-path="url(#tb2Clip)">
    <g transform="translate(482 58) scale(1.22)">${adultFig(0, 0, false, 0, "LR")}${adPhone(21, 0)}</g>
    <rect x="533" y="134" width="22" height="20" rx="5" fill="#A8703F"/><path d="M538 134 q6 -12 12 0" stroke="#A8703F" stroke-width="3.6" fill="none" stroke-linecap="round"/></g>`;
}
const kidX = i => 470 + (i % 8) * 62, kidY = i => 150 + Math.floor(i / 8) * 104;
const adultX = j => j < N.parents ? 470 + j * 50 : 470 + N.parents * 50 + 30 + (j - N.parents) * 50;

UI(`<g id="board" opacity="0">
  <rect x="-10" y="-10" width="1300" height="740" fill="#FFF8E7" opacity=".97"/>
  <circle cx="50" cy="660" r="180" fill="${TEAL}" opacity=".07"/><circle cx="1240" cy="60" r="160" fill="#FFC53D" opacity=".18"/><circle cx="1180" cy="680" r="90" fill="${STEP[3]}" opacity=".08"/>
  ${D.projected ? `<rect x="40" y="26" width="356" height="44" rx="22" fill="#FFC53D"/><text x="218" y="56" text-anchor="middle" ${FONT_B} font-size="21" fill="#3A2A00">PROJECTED · not final results</text>` : ""}
  ${big2("n30", N.total, "people")}
  ${photo("phKids", learnersScene(470, 80, 380, 212), 470, 80, 380, 212, -3, N.children + " Grade 1 learners", STEP[0])}
  ${photo("phAdults", adultsScene(845, 352, 380, 212), 845, 352, 380, 212, 3, N.parents + " parents · " + N.teachers + " teachers", STEP[2])}
  ${big2("n15", N.happy, "of " + N.children, `<circle cx="245" cy="380" r="40" fill="#FFC53D"/><circle cx="231" cy="371" r="5" fill="${INK}"/><circle cx="259" cy="371" r="5" fill="${INK}"/><path d="M225 388 q20 20 40 0" stroke="${INK}" stroke-width="6" fill="none" stroke-linecap="round"/><text x="245" y="462" text-anchor="middle" ${FONT_B} font-size="29" fill="#0B3F4A">chose the happy face</text>`)}
  ${big2("n14", N.again, "of " + N.children, `<circle cx="245" cy="380" r="40" fill="${STEP[1]}"/><path d="${STAR}" transform="translate(245 381) scale(1.25)" fill="#fff"/><text x="245" y="462" text-anchor="middle" ${FONT_B} font-size="29" fill="#0B3F4A">would play it again</text>`)}
  <g id="bridge" opacity="0" style="transform-origin: 50% 50%"><text x="245" y="206" text-anchor="middle" ${FONT_T} font-size="58" fill="${TEAL}" stroke="#fff" stroke-width="12" stroke-linejoin="round" paint-order="stroke">The bridge</text>
    <text x="245" y="260" text-anchor="middle" ${FONT_B} font-size="34" fill="#0B3F4A">to the child</text>
    ${photo("phBridge", adultsScene(60, 300, 370, 206), 60, 300, 370, 206, -2).replace('opacity="0"', 'opacity="1"')}</g>
  <g id="kids" transform="translate(470 136) scale(1.36) translate(-470 -150)">${Array.from({ length: N.children }, (_, i) => `<g id="kid${i}" opacity="0" style="transform-origin: 50% 100%">${kidFig(kidX(i), kidY(i) - 4, i)}
    <g id="kf${i}" opacity="0" style="transform-origin: 50% 100%"><circle cx="${kidX(i) + 23}" cy="${kidY(i) - 20}" r="15" fill="#FFC53D" stroke="#fff" stroke-width="3"/><circle cx="${kidX(i) + 18}" cy="${kidY(i) - 23}" r="2.2" fill="${INK}"/><circle cx="${kidX(i) + 28}" cy="${kidY(i) - 23}" r="2.2" fill="${INK}"/><path d="M${kidX(i) + 16} ${kidY(i) - 17} q7 7 14 0" stroke="${INK}" stroke-width="2.6" fill="none" stroke-linecap="round"/></g>
    <g id="kc${i}" opacity="0" style="transform-origin: 50% 100%"><circle cx="${kidX(i) + 23}" cy="${kidY(i) - 20}" r="15" fill="${STEP[2]}" stroke="#fff" stroke-width="3"/><path d="M${kidX(i) + 16} ${kidY(i) - 20} l5 6 l10 -12" stroke="#fff" stroke-width="4" fill="none" stroke-linecap="round" stroke-linejoin="round"/></g></g>`).join("")}
    <text id="kidsLabel" opacity="0" x="710" y="352" text-anchor="middle" ${FONT_B} font-size="27" fill="${TEAL}">${N.children} children · our users</text></g>
  <g id="adults" transform="translate(0 44)">${Array.from({ length: N.parents + N.teachers }, (_, j) => { const p = j < N.parents, x = adultX(j); return `<g id="ad${j}" opacity="0" style="transform-origin: 50% 100%">
    ${adultFig(x, 404, !p, p ? j : j - N.parents)}</g>`; }).join("")}
    <text id="parLabel" opacity="0" x="${470 + (N.parents * 50 - 8) / 2}" y="528" text-anchor="middle" ${FONT_B} font-size="30" fill="${STEP[2]}">${N.parents} parents</text>
    <text id="teaLabel" opacity="0" x="${adultX(N.parents) + (N.teachers * 50 - 8) / 2}" y="528" text-anchor="middle" ${FONT_B} font-size="30" fill="${TEAL}">${N.teachers} teachers</text></g>
  ${[[N.paFit, "Grade 1 fit", `${N.parents} parents`, STEP[2]], [N.paRec, "Recommend", `${N.parents} parents`, STEP[2]], [N.teOverall, "Teaching", `${N.teRated} of ${N.teachers} teachers`, TEAL]].map(([v, l1, l2, c], i) => { const x = 470 + i * 250; return `<g id="sc${i}" opacity="0" style="transform-origin: 50% 100%">
    <rect x="${x + 6}" y="128" width="226" height="226" rx="30" fill="#0B5F5B" opacity=".2"/><rect x="${x}" y="120" width="226" height="226" rx="30" fill="#fff" stroke="${c}" stroke-width="8"/>
    <text x="${x + 113}" y="214" text-anchor="middle" ${FONT_T} font-size="92" fill="${c}">${v.toFixed(1)}</text><text x="${x + 113}" y="242" text-anchor="middle" ${FONT_B} font-size="21" fill="#5B6B6B" font-weight="600">out of 5</text>
    <clipPath id="scc${i}"><rect x="${x + 28}" y="250" width="${170 * v / 5}" height="40"/></clipPath>
    ${[0, 1].map(k => `<g ${k ? `clip-path="url(#scc${i})"` : ""} fill="${k ? "#FFC53D" : "#E7E2D3"}">${[0, 1, 2, 3, 4].map(s => `<path d="${STAR}" transform="translate(${x + 45 + s * 34} 271) scale(.85)"/>`).join("")}</g>`).join("")}
    <text x="${x + 113}" y="318" text-anchor="middle" ${FONT_B} font-size="25" fill="#0B3F4A">${l1}</text><text x="${x + 113}" y="339" text-anchor="middle" ${FONT_B} font-size="18" fill="#5B6B6B" font-weight="600">${l2}</text></g>`; }).join("")}
  <g id="quote" opacity="0" style="transform-origin: 0% 100%"><g transform="translate(118 262) scale(2.55)">${adultFig(0, 0, true, 0)}</g>
    <path d="M330 150 h810 a40 40 0 0 1 40 40 v190 a40 40 0 0 1 -40 40 h-740 l-90 50 22 -50 h-2 a40 40 0 0 1 -40 -40 v-190 a40 40 0 0 1 40 -40Z" fill="#fff" stroke="${TEAL}" stroke-width="9" stroke-linejoin="round"/>
    <text x="735" y="250" text-anchor="middle" ${FONT_B} font-size="52" fill="${TEAL}">“Very useful and engaging</text><text x="735" y="316" text-anchor="middle" ${FONT_B} font-size="52" fill="${TEAL}">for Grade 1 learners.”</text>
    <text x="735" y="378" text-anchor="middle" ${FONT_B} font-size="28" fill="#5B6B6B" font-weight="600">DepEd Grade 1 teacher</text></g>
</g>`);
show("#board", 68.7, 99.9, 1, .3);
const hopIn = (sel, t) => K(sel, [[t, { opacity: 0, transform: "translateY(30px) scale(.3)" }], [t + .2, { opacity: 1, transform: "translateY(-12px) scale(1.12)" }, "ease-out"], [t + .36, { opacity: 1, transform: "translateY(0px) scale(1)" }]]);
const hop = (sel, t, h = 16) => K(sel, [[t, { transform: "translateY(0px) scale(1)" }], [t + .13, { transform: `translateY(${-h}px) scale(1.08)` }, "ease-out"], [t + .3, { transform: "translateY(0px) scale(1)" }]]);
const fade = (sel, t, to) => K(sel, [[t, { opacity: to === 0 ? 1 : 0 }], [t + .3, { opacity: to }]]);
// 30 people: 16 children step in first, then the parents and teachers
pop("#n30", t30 - .15, t15 - .25);
const tilt = (sel, t, out, r) => K(sel, [[t, { opacity: 0, transform: `translateY(50px) scale(.3) rotate(${-r}deg)` }], [t + .25, { opacity: 1, transform: "translateY(-10px) scale(1.06) rotate(0deg)" }, "ease-out"], [t + .42, { opacity: 1, transform: "translateY(0px) scale(1) rotate(0deg)" }],
  [out - .3, { opacity: 1, transform: "translateY(0px) scale(1.02) rotate(0deg)" }], [out, { opacity: 0, transform: `translateX(${r > 0 ? 260 : -260}px) scale(.5) rotate(${r * 3}deg)` }, "ease-in"]]);
tilt("#phKids", t16 - .2, t15 - .2, -4); tilt("#phAdults", tPar - .2, t15 - .1, 4);
S("sfx_opening_pluck.wav", t16 - .2, .22); S("sfx_opening_pluck.wav", tPar - .2, .2);
for (let i = 0; i < N.children; i++) hopIn("#kid" + i, t15 - .05 + i * .03);
show("#kidsLabel", t15 + .5, tGrown - .2);
for (let j = 0; j < N.parents + N.teachers; j++) hopIn("#ad" + j, t15 + .1 + j * .03);
show("#parLabel", t15 + .5, tQuote - .3); show("#teaLabel", t15 + .5, tQuote - .3);
// 15 of 16 chose the happy face; 14 of 16 would play it again
pop("#n15", t15 - .1, t14 - .25); pop("#n14", t14 - .1, tGrown - .25);
K("#adults", [[t15 - .1, { opacity: 1 }], [t15 + .3, { opacity: .3 }], [tGrown - .3, { opacity: .3 }], [tGrown, { opacity: 1 }], [tQuote - .35, { opacity: 1 }], [tQuote - .05, { opacity: 0 }]]);
for (let i = 0; i < N.children; i++) {
  if (i < N.happy) { const t = tHappy + .1 + i * .085; pop("#kf" + i, t, t14 - .35); hop("#kid" + i, t); }
  if (i < N.again) { const t = t14 + .15 + i * .1; pop("#kc" + i, t, tGrown - .35); hop("#kid" + i, t); }
}
S("sfx_correct_chime.mp3", tHappy + .1, .22); S("sfx_correct_chime.mp3", t14 + .15, .22);
// the grown-ups who open the door: the bridge, and their three scores
K("#kids", [[tGrown - .3, { opacity: 1 }], [tGrown, { opacity: 0 }]]);
pop("#bridge", tGrown - .1, tQuote - .3);
for (let j = 0; j < N.parents + N.teachers; j++) hop("#ad" + j, tGrown + .25 + j * .06, 20);
[tS1, tS2, tS3].forEach((t, i) => { K("#sc" + i, [[t - .15, { opacity: 0, transform: "translateY(40px) scale(.4)" }], [t + .1, { opacity: 1, transform: "translateY(-8px) scale(1.08)" }, "ease-out"], [t + .28, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tQuote - .35, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tQuote - .08, { opacity: 0, transform: "translateY(0px) scale(.6)" }]]); S("sfx_opening_pluck.wav", t - .1, .24); });
// the teacher's words
pop("#quote", tQuote - .05, 99.75);

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
// both hands lift the book open by its top corners; the right one goes back to the table, the left one traces the letter again
// then she holds it up by its sides again (HOLD) until she lets go to cheer
const tHoldEnd = tOne + 1.15 - .45;
armK("L", [[100.4, REST], [100.75, LR("L", 0, 30)], [101.02, LIFT("L")], [101.4, HIDE], [105.6, HIDE], [105.95, HOLD("L")], [tHoldEnd, HOLD("L")]]);
armK("R", [[100.4, REST], [100.75, LR("R", 0, 30)], [101.02, LIFT("R")], [101.45, HOLD("R")], [tHoldEnd, HOLD("R")]]);
holding("L", 105.95, tHoldEnd); holding("R", 101.45, tHoldEnd);
tracing([101.6, 102.3, 103.0, 103.7, 104.4, 105.2]); front("L", 100.52, 101.3); front("R", 100.52, 101.35);
K("#miaHead", [[69.6, HD(-4, -2)], [100.0, HD(-4, -2)], [101.2, HD(7, 4)], [105.8, HD(7, 4)], [106.4, HD(-5, -2)], [110.4, HD(-5, -2)]]);
K("#pupils", [[69.6, PU(5, -5)], [100.0, PU(5, -5)], [101.2, PU(-3, 6)], [103.4, PU(3, 6)], [105.6, PU(-2, 6)], [106.3, PU(7, -2)], [110.4, PU(7, -2)]]);
// one sound at a time
lilyHop(tTars - .1, LX2, LX2, 64); sayIt(tOne - .05); miaCheer(tOne + 1.15, 110.2); lilyHop(tOne + 1.2, LX2, LX2, 50);
[0, 1].forEach(i => K("#hrt" + i, [[tTars + .3 + i * .22, { opacity: 0, transform: "translateY(20px) scale(.3)" }], [tTars + .55 + i * .22, { opacity: 1, transform: "translateY(0px) scale(1.2)" }, "ease-out"], [tTars + 1.4 + i * .22, { opacity: 1, transform: "translateY(-26px) scale(1)" }], [tTars + 1.8 + i * .22, { opacity: 0, transform: "translateY(-44px) scale(.8)" }]]));
// the team
const names = D.team.members.map(n => n.replace(/\s*\(.*\)/, ""));
UI(`<rect id="endVeil" opacity="0" x="-10" y="-10" width="1300" height="740" fill="#FFF8E7"/>
  <g id="ribbon" opacity="0" style="transform-origin: 50% 50%"><rect x="336" y="24" width="620" height="76" rx="38" fill="#0B5F5B" opacity=".28"/><rect x="330" y="18" width="620" height="76" rx="38" fill="${TEAL}" stroke="#fff" stroke-width="7"/>
    <text x="640" y="70" text-anchor="middle" ${FONT_T} font-size="40" fill="#fff">Group 56 · CIT University</text></g>
  <g id="teamPic" opacity="0" style="transform-origin: 50% 50%"><g id="teamZoom" style="transform-origin: 50% 50%">${photo("teamCard", "team_2d.jpg", 300, 116, 680, 380, 0).replace('opacity="0"', 'opacity="1"')}</g></g>
  ${names.map((n, i) => { const x = 200 + i * 220, c = STEP[i % 5]; return `<g id="tm${i}" opacity="0" style="transform-origin: 50% 50%">
    <rect x="${x - 104}" y="516" width="208" height="46" rx="23" fill="#fff" stroke="${c}" stroke-width="6"/><text x="${x}" y="547" text-anchor="middle" ${FONT_B} font-size="${n.length > 15 ? 20 : 23}" fill="#0B3F4A">${n}</text>
    ${i === 0 ? `<rect x="${x - 30}" y="500" width="60" height="24" rx="12" fill="#FFC53D"/><text x="${x}" y="518" text-anchor="middle" ${FONT_B} font-size="16" fill="#3A2A00">Lead</text>` : ""}</g>`; }).join("")}
  <text id="adviser" opacity="0" x="640" y="598" text-anchor="middle" ${FONT_B} font-size="26" fill="#5B6B6B" font-weight="600">${D.team.adviser}</text>
  <g id="endCard" opacity="0"><text x="640" y="352" text-anchor="middle" ${FONT_B} font-size="40" fill="${TEAL}">Offline phonics for Grade 1 learners</text>
    <g id="lilyEnd" style="transform-origin: 50% 100%"><image href="img/lily_waving.png" x="470" y="350" width="300" height="300"/><image id="lilyEndTalk" href="img/lily_waving_talk.png" x="470" y="350" width="300" height="300" opacity="0"/></g>
    <g id="playit" style="transform-origin: 50% 50%"><text x="975" y="452" text-anchor="middle" transform="rotate(-8 975 452)" ${FONT_T} font-size="78" fill="#FFC53D" stroke="${TEAL}" stroke-width="12" stroke-linejoin="round" paint-order="stroke">Play it!</text></g></g>`);
K("#endVeil", [[tWe - .35, { opacity: 0 }], [tWe, { opacity: .86 }], [tPlayIt - .3, { opacity: .86 }], [tPlayIt, { opacity: .97 }]]);
pop("#ribbon", tGroup - .35, tPlayIt - .3);
K("#teamPic", [[tWe - .2, { opacity: 0, transform: "translateY(60px) scale(.5)" }], [tWe + .1, { opacity: 1, transform: "translateY(-10px) scale(1.04)" }, "ease-out"], [tWe + .3, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tPlayIt - .35, { opacity: 1, transform: "translateY(0px) scale(1)" }], [tPlayIt - .1, { opacity: 0, transform: "translateY(30px) scale(.6)" }]]);
K("#teamZoom", [[tWe, { transform: "scale(1)" }], [tPlayIt, { transform: "scale(1.04)" }]]);
names.forEach((_, i) => { const t = tWe + .55 + i * .2; K("#tm" + i, [[t, { opacity: 0, transform: "translateY(60px) scale(.3)" }], [t + .22, { opacity: 1, transform: "translateY(-14px) scale(1.1)" }, "ease-out"], [t + .4, { opacity: 1, transform: "translateY(0px) scale(1)" }],
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
