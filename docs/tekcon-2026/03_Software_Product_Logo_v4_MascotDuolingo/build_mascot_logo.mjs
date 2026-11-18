import fs from "node:fs";
import os from "node:os";
import path from "node:path";
import { spawn } from "node:child_process";
import { createRequire } from "node:module";

const require = createRequire(import.meta.url);
const WebSocket = require("c:/Users/Zendrix/Desktop/tekcon-2026/video/node_modules/ws");

const OUT_DIR = "c:/Users/Zendrix/Desktop/tekcon-2026/03_Software_Product_Logo_v4_MascotDuolingo";
const CHROME = "C:/Program Files/Google/Chrome/Application/chrome.exe";
const LEXEND_SEMIBOLD = "C:/Users/Zendrix/Desktop/playIT/app/src/main/res/font/lexend_semibold.ttf";
const LEXEND_BOLD = "C:/Users/Zendrix/Desktop/playIT/app/src/main/res/font/lexend_bold.ttf";

const lexendSemiB64 = fs.readFileSync(LEXEND_SEMIBOLD).toString("base64");
const lexendBoldB64 = fs.readFileSync(LEXEND_BOLD).toString("base64");

const facePath = fs.existsSync(path.join(OUT_DIR, "_face_defringed.png"))
  ? path.join(OUT_DIR, "_face_defringed.png")
  : "c:/Users/Zendrix/Desktop/tekcon-2026/assets/product_logo_face.png";
const faceB64 = fs.readFileSync(facePath).toString("base64");

// ============================================================================
// 1. DUOLINGO-STYLE MASCOT APP ICON (512x512 coordinate space)
// Full-bleed borderless Lagoon Teal squircle + Unmodified Lily Face zoomed & optically centered
// ============================================================================
function makeMascotSquircleDefsAndBody({ idPrefix = "mIcon", outlineRim = false, showGrid = false } = {}) {
  // Squircle inset=16, width=480, height=480, rx=112 (matching iOS/Android Duolingo icon curvature)
  // Unmodified product_logo_face.png has bbox x=125..1874 (1749w), y=275..1723 (1448h) inside 2000x2000.
  // Scaling the 2000x2000 image to 476x476 and placing at x=18, y=22 puts Lily's ears & cheeks filling
  // 82% of the squircle width with generous teal breathing room and her big eyes right on the optical center line!
  const rim = outlineRim
    ? `<rect x="21" y="21" width="470" height="470" rx="107" fill="none" stroke="#FFFDF8" stroke-width="10"/>`
    : "";

  const gridOverlay = showGrid
    ? `<g opacity="0.35" stroke="#FFFDF8" stroke-width="1.5" fill="none" stroke-dasharray="5 5">
        <rect x="16" y="16" width="480" height="480" rx="112"/>
        <circle cx="256" cy="256" r="196"/>
        <circle cx="256" cy="256" r="132"/>
        <line x1="0" y1="248" x2="512" y2="248"/>
        <line x1="256" y1="0" x2="256" y2="512"/>
      </g>`
    : "";

  return {
    defs: `
      <clipPath id="${idPrefix}_clip">
        <rect x="16" y="16" width="480" height="480" rx="112"/>
      </clipPath>
    `,
    body: `
      <g clip-path="url(#${idPrefix}_clip)">
        <!-- Rich Lagoon Teal base (#0B7F7A) -->
        <rect x="0" y="0" width="512" height="512" fill="#0B7F7A"/>
        <!-- Duolingo-style crisp flat 2-tone upper highlight plane -->
        <path d="M 0 0 L 512 0 L 512 215 Q 256 270 0 200 Z" fill="#10918B"/>
        <!-- Subtle lower cel shadow shelf -->
        <rect x="0" y="456" width="512" height="56" fill="#086864"/>
        <!-- Soft subtle drop shadow under Lily's chin for depth -->
        <ellipse cx="256" cy="430" rx="132" ry="26" fill="#075855" opacity="0.45"/>
        <!-- Unmodified Lily Mascot Face (zoomed & optically centered like Duo) -->
        <image href="data:image/png;base64,${faceB64}" x="18" y="22" width="476" height="476" preserveAspectRatio="xMidYMid meet"/>
      </g>
      ${rim}
      ${gridOverlay}
    `,
  };
}

function buildAppIconSvg({ outlineRim = false, showGrid = false } = {}) {
  const { defs, body } = makeMascotSquircleDefsAndBody({ idPrefix: "app", outlineRim, showGrid });
  return `<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="512" height="512">
  <defs>${defs}</defs>
  ${body}
</svg>`;
}

// ============================================================================
// 2. CLEAN LINELESS GEOMETRIC WORDMARK: "PlayIT"
// Matches Lily's lineless flat vector style (Teal "Play" + Apricot/Gold "IT", clean open 'P' counter)
// ============================================================================
function buildCustomWordmarkGroup({ mode = "light", showGrid = false } = {}) {
  const colMain = mode === "dark" ? "#FFFDF8" : "#0B7F7A";
  const colAccent = mode === "dark" ? "#FFC53D" : "#F7922F";
  const sw = 38;

  const grid = showGrid
    ? `<g opacity="0.28" stroke="${mode === "dark" ? "#FFC53D" : "#0B7F7A"}" stroke-width="1.5" fill="none" stroke-dasharray="4 4">
        <line x1="0" y1="27" x2="825" y2="27"/>
        <line x1="0" y1="85" x2="825" y2="85"/>
        <line x1="0" y1="235" x2="825" y2="235"/>
        <circle cx="84" cy="102" r="56"/>
        <circle cx="314" cy="160" r="56"/>
        <circle cx="485" cy="164" r="49"/>
      </g>`
    : "";

  return `
    <g>
      <g fill="none" stroke="${colMain}" stroke-width="${sw}" stroke-linecap="round" stroke-linejoin="round">
        <!-- P (clean open circular counter so Lily's face is 100% the hero) -->
        <line x1="42" y1="46" x2="42" y2="216"/>
        <path d="M 42 46 L 84 46 A 56 56 0 0 1 84 158 L 42 158"/>
        <!-- l (Featherly curved foot) -->
        <path d="M 198 46 L 198 184 A 32 32 0 0 0 230 216"/>
        <!-- a (single-story circular bowl + right vertical stem) -->
        <circle cx="314" cy="160" r="56"/>
        <line x1="370" y1="104" x2="370" y2="216"/>
        <!-- y (circular U-cup + Featherly circular hook tail) -->
        <path d="M 436 104 L 436 164 A 49 49 0 0 0 534 164 L 534 104"/>
        <path d="M 534 104 L 534 224 A 48 48 0 0 1 486 272 L 478 272"/>
      </g>
      <g fill="none" stroke="${colAccent}" stroke-width="${sw}" stroke-linecap="round" stroke-linejoin="round">
        <!-- I -->
        <line x1="604" y1="46" x2="604" y2="216"/>
        <line x1="590" y1="46" x2="618" y2="46"/>
        <line x1="590" y1="216" x2="618" y2="216"/>
        <!-- T -->
        <line x1="682" y1="46" x2="786" y2="46"/>
        <line x1="734" y1="46" x2="734" y2="216"/>
      </g>
      ${grid}
    </g>
  `;
}

function buildLockupHorizontalSvg({ mode = "light" } = {}) {
  const { defs, body } = makeMascotSquircleDefsAndBody({
    idPrefix: `h_${mode}`,
    outlineRim: mode === "dark",
  });
  const wm = buildCustomWordmarkGroup({ mode });
  return `<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1390 500" width="1390" height="500">
  <defs>${defs}</defs>
  <g transform="translate(40, 40) scale(0.82)">
    ${body}
  </g>
  <g transform="translate(520, 122)">
    ${wm}
  </g>
</svg>`;
}

function buildLockupStackedSvg({ mode = "light" } = {}) {
  const { defs, body } = makeMascotSquircleDefsAndBody({ idPrefix: `s_${mode}` });
  const wm = buildCustomWordmarkGroup({ mode });
  return `<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 1000" width="1000" height="1000">
  <defs>${defs}</defs>
  <g transform="translate(205.6, 42) scale(1.15)">
    ${body}
  </g>
  <g transform="translate(127, 676) scale(0.90)">
    ${wm}
  </g>
</svg>`;
}

function buildWordmarkOnlySvg({ mode = "light" } = {}) {
  const wm = buildCustomWordmarkGroup({ mode });
  return `<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 850 315" width="850" height="315">
  <g transform="translate(10, 10)">
    ${wm}
  </g>
</svg>`;
}

async function withChromeCdp(fn) {
  const port = 9338;
  const userData = path.join(os.tmpdir(), `playit_mascot_chrome_${Date.now()}`);
  fs.mkdirSync(userData, { recursive: true });
  const chrome = spawn(CHROME, [
    "--headless=new",
    "--disable-gpu",
    `--remote-debugging-port=${port}`,
    `--user-data-dir=${userData}`,
    "--no-first-run",
    "--no-default-browser-check",
    "about:blank",
  ], { stdio: "ignore" });

  let wsUrl = null;
  for (let i = 0; i < 40; i++) {
    try {
      const res = await fetch(`http://127.0.0.1:${port}/json/list`);
      const list = await res.json();
      const page = list.find((t) => t.type === "page");
      if (page?.webSocketDebuggerUrl) { wsUrl = page.webSocketDebuggerUrl; break; }
    } catch {}
    await new Promise((r) => setTimeout(r, 150));
  }
  if (!wsUrl) throw new Error("Chrome CDP did not start");

  const ws = new WebSocket(wsUrl);
  await new Promise((res, rej) => { ws.once("open", res); ws.once("error", rej); });

  let nextId = 1;
  const pending = new Map();
  ws.on("message", (raw) => {
    const msg = JSON.parse(raw.toString());
    if (msg.id && pending.has(msg.id)) {
      const { resolve, reject } = pending.get(msg.id);
      pending.delete(msg.id);
      if (msg.error) reject(new Error(JSON.stringify(msg.error)));
      else resolve(msg.result);
    }
  });

  const cdp = (method, params = {}) =>
    new Promise((resolve, reject) => {
      const id = nextId++;
      pending.set(id, { resolve, reject });
      ws.send(JSON.stringify({ id, method, params }));
    });

  await cdp("Page.enable");
  await cdp("Runtime.enable");

  try {
    await fn(cdp);
  } finally {
    ws.close();
    chrome.kill();
    setTimeout(() => { try { fs.rmSync(userData, { recursive: true, force: true }); } catch {} }, 500);
  }
}

async function renderHtmlToPng(cdp, html, outPath, width, height, transparent = true) {
  await cdp("Emulation.setDeviceMetricsOverride", { width, height, deviceScaleFactor: 1, mobile: false });
  await cdp("Emulation.setDefaultBackgroundColorOverride", {
    color: transparent ? { r: 0, g: 0, b: 0, a: 0 } : { r: 247, g: 250, b: 249, a: 1 },
  });
  const doc = `<!DOCTYPE html><html><head><meta charset="utf-8"><style>
    * { margin:0; padding:0; box-sizing:border-box; }
    html, body { width:${width}px; height:${height}px; overflow:hidden; background: transparent; }
    @font-face { font-family: 'PlayITLexend'; src: url('data:font/ttf;base64,${lexendSemiB64}') format('truetype'); font-weight: 600; }
    @font-face { font-family: 'PlayITLexendBold'; src: url('data:font/ttf;base64,${lexendBoldB64}') format('truetype'); font-weight: 700; }
  </style></head><body>${html}</body></html>`;
  const tmpHtml = path.join(os.tmpdir(), `playit_m_${Date.now()}_${Math.random().toString(36).slice(2)}.html`);
  fs.writeFileSync(tmpHtml, doc, "utf8");
  await cdp("Page.navigate", { url: "file:///" + tmpHtml.replace(/\\/g, "/") });
  await cdp("Runtime.evaluate", {
    expression: `(async () => {
      await document.fonts.ready;
      await Promise.all(Array.from(document.images).map(im => im.complete ? null : new Promise(r => { im.onload = r; im.onerror = r; })));
      await new Promise(r => requestAnimationFrame(() => requestAnimationFrame(r)));
    })()`,
    awaitPromise: true,
  });
  const { data } = await cdp("Page.captureScreenshot", {
    format: "png",
    fromSurface: true,
    clip: { x: 0, y: 0, width, height, scale: 1 },
  });
  try { fs.unlinkSync(tmpHtml); } catch {}
  fs.writeFileSync(outPath, Buffer.from(data, "base64"));
  console.log("Wrote:", outPath, `${width}x${height}`);
}

const iconSvg = buildAppIconSvg();
const iconGridSvg = buildAppIconSvg({ showGrid: true });
const wmLightSvg = buildWordmarkOnlySvg({ mode: "light" });
const wmDarkSvg = buildWordmarkOnlySvg({ mode: "dark" });
const horizLightSvg = buildLockupHorizontalSvg({ mode: "light" });
const horizDarkSvg = buildLockupHorizontalSvg({ mode: "dark" });
const stackSvg = buildLockupStackedSvg({ mode: "light" });

fs.writeFileSync(path.join(OUT_DIR, "PlayIT_AppIcon.svg"), iconSvg);
fs.writeFileSync(path.join(OUT_DIR, "PlayIT_Wordmark_Teal.svg"), wmLightSvg);
fs.writeFileSync(path.join(OUT_DIR, "PlayIT_Wordmark_White.svg"), wmDarkSvg);
fs.writeFileSync(path.join(OUT_DIR, "PlayIT_Lockup_Horizontal_Light.svg"), horizLightSvg);
fs.writeFileSync(path.join(OUT_DIR, "PlayIT_Lockup_Horizontal_OnTeal.svg"), horizDarkSvg);
fs.writeFileSync(path.join(OUT_DIR, "PlayIT_Lockup_Stacked.svg"), stackSvg);

await withChromeCdp(async (cdp) => {
  await renderHtmlToPng(cdp, iconSvg.replace('width="512" height="512"', 'width="2000" height="2000"'), path.join(OUT_DIR, "PlayIT_AppIcon_2000x2000.png"), 2000, 2000, true);
  await renderHtmlToPng(cdp, iconSvg, path.join(OUT_DIR, "PlayIT_AppIcon_512x512.png"), 512, 512, true);
  await renderHtmlToPng(cdp, wmLightSvg.replace('width="850" height="315"', 'width="2550" height="945"'), path.join(OUT_DIR, "PlayIT_Wordmark_Teal.png"), 2550, 945, true);
  await renderHtmlToPng(cdp, wmDarkSvg.replace('width="850" height="315"', 'width="2550" height="945"'), path.join(OUT_DIR, "PlayIT_Wordmark_White.png"), 2550, 945, true);
  await renderHtmlToPng(cdp, horizLightSvg.replace('width="1390" height="500"', 'width="2780" height="1000"'), path.join(OUT_DIR, "PlayIT_Lockup_Horizontal_Light.png"), 2780, 1000, true);
  await renderHtmlToPng(cdp, horizDarkSvg.replace('width="1390" height="500"', 'width="2780" height="1000"'), path.join(OUT_DIR, "PlayIT_Lockup_Horizontal_OnTeal.png"), 2780, 1000, true);
  await renderHtmlToPng(cdp, stackSvg.replace('width="1000" height="1000"', 'width="2000" height="2000"'), path.join(OUT_DIR, "PlayIT_Lockup_Stacked.png"), 2000, 2000, true);

  const iconB64 = fs.readFileSync(path.join(OUT_DIR, "PlayIT_AppIcon_512x512.png")).toString("base64");
  const horizLB64 = fs.readFileSync(path.join(OUT_DIR, "PlayIT_Lockup_Horizontal_Light.png")).toString("base64");
  const horizDB64 = fs.readFileSync(path.join(OUT_DIR, "PlayIT_Lockup_Horizontal_OnTeal.png")).toString("base64");
  const stackB64 = fs.readFileSync(path.join(OUT_DIR, "PlayIT_Lockup_Stacked.png")).toString("base64");
  const iconGridInline = iconGridSvg.replace('width="512" height="512"', 'width="100%" height="100%"');

  const sheetHtml = `
    <div style="width:2400px;height:1480px;background:#F7FAF9;padding:52px;font-family:'PlayITLexend',sans-serif;color:#173B3A;">
      <div style="display:flex;justify-content:space-between;align-items:flex-end;margin-bottom:36px;border-bottom:3px solid #D5ECE9;padding-bottom:24px;">
        <div>
          <div style="font-family:'PlayITLexendBold',sans-serif;font-size:44px;color:#0B7F7A;letter-spacing:-1px;">PlayIT — Duolingo-Style Mascot Brand System</div>
          <div style="font-size:20px;color:#557271;margin-top:6px;">Unmodified Lily mascot face zoomed into a borderless Lagoon Teal squircle + clean lineless geometric wordmark</div>
        </div>
        <div style="background:#0B7F7A;color:#FFFDF8;border-radius:16px;padding:12px 24px;font-family:'PlayITLexendBold',sans-serif;font-size:18px;">Final Mascot Logo System</div>
      </div>

      <div style="display:grid;grid-template-columns:1.1fr 1.25fr 1fr;gap:30px;height:1220px;">
        <!-- Col 1: App Icon + Grid + Launcher Scale -->
        <div style="background:#FFFFFF;border-radius:28px;padding:30px;box-shadow:0 12px 32px rgba(11,127,122,0.08);display:flex;flex-direction:column;justify-content:space-between;">
          <div>
            <div style="font-family:'PlayITLexendBold',sans-serif;font-size:24px;color:#0B7F7A;">01 · Text-Free Mascot App Icon</div>
            <div style="font-size:15px;color:#557271;margin-top:4px;">Unmodified Lily face optically centered on full-bleed Lagoon Teal (#0B7F7A)</div>
          </div>

          <div style="display:grid;grid-template-columns:1fr 1fr;gap:18px;align-items:center;margin:14px 0;">
            <div style="text-align:center;">
              <img src="data:image/png;base64,${iconB64}" style="width:270px;height:270px;display:block;margin:0 auto;" />
              <div style="font-size:13px;color:#557271;margin-top:10px;">Final App Icon (Borderless Squircle)</div>
            </div>
            <div style="text-align:center;">
              <div style="width:270px;height:270px;margin:0 auto;">${iconGridInline}</div>
              <div style="font-size:13px;color:#557271;margin-top:10px;">Optical Center-Mass Grid</div>
            </div>
          </div>

          <div style="background:#EEF7F6;border-radius:20px;padding:22px;">
            <div style="font-family:'PlayITLexendBold',sans-serif;font-size:13px;letter-spacing:1px;color:#0B7F7A;margin-bottom:14px;">SMALL-SCALE RECOGNITION (16PX -> 96PX)</div>
            <div style="display:flex;align-items:flex-end;justify-content:space-around;">
              <div style="text-align:center;"><img src="data:image/png;base64,${iconB64}" style="width:96px;height:96px;display:block;margin:0 auto;" /><div style="font-size:12px;margin-top:6px;">96px</div></div>
              <div style="text-align:center;"><img src="data:image/png;base64,${iconB64}" style="width:64px;height:64px;display:block;margin:0 auto;" /><div style="font-size:12px;margin-top:6px;">64px</div></div>
              <div style="text-align:center;"><img src="data:image/png;base64,${iconB64}" style="width:48px;height:48px;display:block;margin:0 auto;" /><div style="font-size:12px;margin-top:6px;font-weight:700;">48dp</div></div>
              <div style="text-align:center;"><img src="data:image/png;base64,${iconB64}" style="width:32px;height:32px;display:block;margin:0 auto;" /><div style="font-size:12px;margin-top:6px;">32px</div></div>
              <div style="text-align:center;"><img src="data:image/png;base64,${iconB64}" style="width:16px;height:16px;display:block;margin:0 auto;" /><div style="font-size:12px;margin-top:6px;">16px</div></div>
            </div>
          </div>
        </div>

        <!-- Col 2: Horizontal Lockups -->
        <div style="display:flex;flex-direction:column;gap:26px;">
          <div style="background:#FFFFFF;border-radius:28px;padding:30px;box-shadow:0 12px 32px rgba(11,127,122,0.08);flex:1;display:flex;flex-direction:column;justify-content:center;">
            <div style="font-family:'PlayITLexendBold',sans-serif;font-size:24px;color:#0B7F7A;margin-bottom:14px;">02 · Primary Horizontal Lockup (Light / Sky / Cream)</div>
            <div style="background:#FFFDF8;border-radius:20px;padding:28px;display:flex;align-items:center;justify-content:center;border:2px solid #E2D3BC;">
              <img src="data:image/png;base64,${horizLB64}" style="max-width:94%;max-height:210px;object-fit:contain;" />
            </div>
          </div>

          <div style="background:#0B7F7A;border-radius:28px;padding:30px;box-shadow:0 12px 32px rgba(11,127,122,0.16);flex:1;display:flex;flex-direction:column;justify-content:center;">
            <div style="font-family:'PlayITLexendBold',sans-serif;font-size:24px;color:#FFC53D;margin-bottom:14px;">03 · Dark / Brand Teal Surface Lockup</div>
            <div style="display:flex;align-items:center;justify-content:center;padding:18px;">
              <img src="data:image/png;base64,${horizDB64}" style="max-width:94%;max-height:210px;object-fit:contain;" />
            </div>
          </div>
        </div>

        <!-- Col 3: Stacked Lockup -->
        <div style="background:#FFFFFF;border-radius:28px;padding:30px;box-shadow:0 12px 32px rgba(11,127,122,0.08);display:flex;flex-direction:column;justify-content:space-between;align-items:center;">
          <div style="align-self:flex-start;">
            <div style="font-family:'PlayITLexendBold',sans-serif;font-size:24px;color:#0B7F7A;">04 · Stacked Brand Lockup</div>
            <div style="font-size:15px;color:#557271;margin-top:4px;">2000×2000 master for square cards, splash headers, and store badges</div>
          </div>
          <img src="data:image/png;base64,${stackB64}" style="width:520px;height:520px;object-fit:contain;margin:auto 0;" />
          <div style="background:#FFF8E7;border-radius:16px;padding:16px;font-size:14px;color:#3B2416;line-height:1.5;width:100%;">
            <b>100% Unmodified Mascot:</b> Uses your exact <code>product_logo_face.png</code> art on a borderless Lagoon Teal squircle, paired with the lineless geometric <b>PlayIT</b> logotype.
          </div>
        </div>
      </div>
    </div>
  `;

  await renderHtmlToPng(cdp, sheetHtml, path.join(OUT_DIR, "PlayIT_Mascot_Duolingo_Brand_Sheet.png"), 2400, 1480, false);
});
try { fs.unlinkSync(path.join(OUT_DIR, "_face_defringed.png")); } catch {}
console.log("MASCOT DUOLINGO LOGO COMPLETE");
