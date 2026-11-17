// Makes assets/qr_app.png: the QR code to the current playit.apk on Google Drive (view-only link),
// dark brown #3B2416 on Shell White #FFFDF8, and proves the image scans back to the same link.
// Needs the npm packages qrcode, jsqr and pngjs (for example: npm i qrcode jsqr pngjs, or set NODE_PATH).
//   node make_qr_app.js
const fs = require("fs"), path = require("path");
const QR = require("qrcode"), jsQR = require("jsqr"), { PNG } = require("pngjs");

// The "?usp=drivesdk" tail is left off: the link opens the same Drive page, and the shorter text
// gives a code with fewer, larger squares that scans more easily from a projected slide.
const URL = "https://drive.google.com/file/d/1pIr2360noHf64TOT-sAwk_uNlk2gSeQa/view";
const OUT = path.join(__dirname, "assets", "qr_app.png");

(async () => {
  await QR.toFile(OUT, URL, { errorCorrectionLevel: "Q", margin: 4, width: 1200, color: { dark: "#3B2416", light: "#FFFDF8" } });
  const seg = QR.create(URL, { errorCorrectionLevel: "Q" });
  const png = PNG.sync.read(fs.readFileSync(OUT));
  const got = jsQR(new Uint8ClampedArray(png.data.buffer, png.data.byteOffset, png.data.length), png.width, png.height);
  console.log(`qr_app.png: version ${seg.version} (${seg.modules.size} x ${seg.modules.size}), error correction Q, ${png.width} x ${png.height} px`);
  console.log(got && got.data === URL ? `check: scans back to ${got.data}` : `check FAILED: read ${got && got.data}`);
  if (!got || got.data !== URL) process.exit(1);
})();
