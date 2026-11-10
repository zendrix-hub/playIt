// Colours and fonts match the app and the poster. Do not add colours.
// The fonts ship inside the project (from npm), so rendering needs no internet connection.
import { continueRender, delayRender } from "remotion";
import "@fontsource/lexend/400.css";
import "@fontsource/lexend/600.css";
import "@fontsource/lexend/700.css";
import "@fontsource/sniglet/400.css";
import "@fontsource/sniglet/800.css";

export const BODY = "Lexend";
export const TITLE = "Sniglet";

// Hold each frame until the fonts are ready, so no frame is drawn in a stand-in font.
if (typeof document !== "undefined" && document.fonts) {
  const handle = delayRender("Loading Lexend and Sniglet");
  const wanted = ["400 20px Lexend", "600 20px Lexend", "700 20px Lexend", "400 20px Sniglet", "800 20px Sniglet"];
  Promise.all(wanted.map((f) => document.fonts.load(f))).then(() => continueRender(handle), () => continueRender(handle));
}

export const C = {
  teal: "#0B7F7A", tealPale: "#D6EEEC", sun: "#FFC53D", sand: "#FFF2DC", sky: "#E1F3FA", card: "#FFFDF8",
  brown: "#4A2E18", text: "#3B2416", grey: "#7A6A5E", white: "#FFFFFF", off: "#E4DCD0", leaf: "#2E9A3E",
  lily: "#FA842C", lilyShade: "#E96D25", cream: "#FFE8CE", peach: "#FEC484", night: "#1E2A44", slot: "#F7922F",
  steps: { 1: "#3BA7D9", 2: "#F7922F", 3: "#2E9A3E", 4: "#F2545B" },
};
export const STEP_NAMES = { 1: "1  Hear It", 2: "2  Say It", 3: "3  Find It", 4: "4  Blend It" };
