// Shared building blocks. Every picture goes through has()/Slot so a missing file
// becomes a labelled placeholder instead of a failed render.
import React from "react";
import { AbsoluteFill, Img, OffthreadVideo, staticFile, interpolate, spring, useCurrentFrame, useVideoConfig } from "remotion";
import data from "./generated/data.json";
import { C, BODY, TITLE } from "./theme";

export const D = data;
export const has = (p) => Boolean(p) && data.files[p] !== undefined;
export const clipSeconds = (p) => (data.files[p] && data.files[p].seconds) || null;

export const useT = () => {
  const frame = useCurrentFrame();
  const { fps, durationInFrames } = useVideoConfig();
  return { frame, fps, t: frame / fps, dur: durationInFrames / fps, durationInFrames };
};
// 0 -> 1 with a soft bounce, starting at `at` seconds into the shot.
export const usePop = (at = 0, damping = 12) => {
  const { frame, fps } = useT();
  return spring({ frame: frame - at * fps, fps, config: { damping, mass: 0.6 } });
};
export const fade = (t, a, b) => interpolate(t, [a, b], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });

export const Placeholder = ({ label, dark }) => (
  <AbsoluteFill style={{ background: dark ? "#2A3350" : "#FFFFFF", border: `6px dashed ${C.slot}`, boxSizing: "border-box",
    justifyContent: "center", alignItems: "center", color: C.slot, fontFamily: BODY, fontWeight: 700, fontSize: 30, textAlign: "center", padding: 30, whiteSpace: "pre-line" }}>
    {label}
  </AbsoluteFill>
);

// A full-frame clip or still with a slow camera move; falls back to a placeholder.
export const StoryPicture = ({ clip, still, move = "push-in", label, dim }) => {
  const { t, dur } = useT();
  const k = interpolate(t, [0, dur], [0, 1]);
  const scale = move === "pull-out" ? 1.1 - 0.08 * k : 1.02 + 0.08 * k;
  const inner = { width: "100%", height: "100%", objectFit: "cover" };
  let body;
  if (has("story/" + clip)) {
    const s = clipSeconds("story/" + clip);
    const rate = s ? Math.min(1.5, Math.max(0.5, s / dur)) : 1;
    body = <OffthreadVideo src={staticFile("story/" + clip)} muted playbackRate={rate} style={inner} />;
  } else if (has("story/" + still) || has("img/" + still)) {
    const p = has("story/" + still) ? "story/" + still : "img/" + still;
    body = <Img src={staticFile(p)} style={{ ...inner, transform: `scale(${scale})` }} />;
  } else {
    body = <Placeholder dark label={`STORY WORLD (Pixar style)\n${label || ""}\nneeds: ${[clip, still].filter(Boolean).join(" or ")}`} />;
  }
  return (
    <AbsoluteFill style={{ background: C.night, overflow: "hidden" }}>
      {body}
      {dim ? <AbsoluteFill style={{ background: "#000", opacity: 0.35 * k }} /> : null}
    </AbsoluteFill>
  );
};

// Small flat-style pieces of a Philippine landscape, drawn in code.
const Palm = ({ x, y, h = 300, lean = 6 }) => (
  <div style={{ position: "absolute", left: x, top: y, width: 10, height: h, transform: `rotate(${lean}deg)`, transformOrigin: "50% 100%" }}>
    <div style={{ position: "absolute", left: -8, top: 0, width: 26, height: h, borderRadius: 13, background: "#9A6B3F" }} />
    {[-70, -35, 0, 35, 70].map((a) => (
      <div key={a} style={{ position: "absolute", left: 5, top: 0, width: h * 0.62, height: h * 0.16, borderRadius: "0 100% 0 100%", background: a % 70 === 0 ? "#2E9A3E" : "#3FB14F",
        transform: `rotate(${a - 90}deg)`, transformOrigin: "0% 50%" }} />
    ))}
    {[0, 1, 2].map((k) => <div key={k} style={{ position: "absolute", left: -10 + k * 12, top: 8, width: 20, height: 20, borderRadius: "50%", background: "#6B4A2A" }} />)}
  </div>
);
const BahayKubo = ({ x, y, s = 1 }) => (
  <div style={{ position: "absolute", left: x, top: y, width: 240 * s, height: 220 * s }}>
    <div style={{ position: "absolute", left: 0, top: 0, width: 0, height: 0, borderLeft: `${120 * s}px solid transparent`, borderRight: `${120 * s}px solid transparent`, borderBottom: `${100 * s}px solid #B8863B` }} />
    <div style={{ position: "absolute", left: 30 * s, top: 96 * s, width: 180 * s, height: 78 * s, background: "#E8C98A", borderRadius: 6 }} />
    <div style={{ position: "absolute", left: 66 * s, top: 112 * s, width: 44 * s, height: 40 * s, background: "#FFF6DE", border: `${4 * s}px solid #9A6B3F`, boxSizing: "border-box" }} />
    <div style={{ position: "absolute", left: 136 * s, top: 112 * s, width: 40 * s, height: 62 * s, background: "#9A6B3F", borderRadius: `${6 * s}px ${6 * s}px 0 0` }} />
    {[40, 110, 186].map((l) => <div key={l} style={{ position: "absolute", left: l * s, top: 172 * s, width: 12 * s, height: 46 * s, background: "#7A5230" }} />)}
  </div>
);
const Banderitas = () => {
  const cols = [C.steps[1], C.sun, C.steps[3], C.steps[4], C.steps[2], C.teal];
  return (
    <div style={{ position: "absolute", left: 0, top: 0, width: "100%", height: 70 }}>
      <div style={{ position: "absolute", left: -20, top: -46, width: 1960, height: 80, borderRadius: "50%", border: "3px solid #9A6B3F", borderTop: "none", boxSizing: "border-box" }} />
      {Array.from({ length: 30 }).map((_, i) => {
        const u = i / 29, drop = 30 * Math.sin(Math.PI * u);
        return <div key={i} style={{ position: "absolute", left: 14 + i * 65, top: 2 + drop, width: 0, height: 0, borderLeft: "17px solid transparent", borderRight: "17px solid transparent", borderTop: `30px solid ${cols[i % cols.length]}` }} />;
      })}
    </div>
  );
};

// Lily's island, set in the Philippines: the plate B1 if it exists, otherwise drawn in code
// (Chocolate Hills of Bohol, where tarsiers live; coconut palms; a bahay kubo; fiesta banderitas).
export const Stage = ({ children, shift = 0 }) => (
  <AbsoluteFill style={{ background: `linear-gradient(${C.sky} 0%, ${C.sky} 35%, ${C.sand} 100%)`, overflow: "hidden" }}>
    {has("story/B1.png") ? (
      <Img src={staticFile("story/B1.png")} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
    ) : (
      <>
        <div style={{ position: "absolute", left: 70, top: 96, width: 120, height: 120, borderRadius: "50%", background: C.sun }} />
        {[0, 1, 2, 3, 4, 5, 6, 7].map((k) => (
          <div key={k} style={{ position: "absolute", left: 124, top: 150, width: 12, height: 44, borderRadius: 6, background: C.sun, transform: `rotate(${k * 45}deg) translateY(-98px)`, transformOrigin: "50% 0%" }} />
        ))}
        {[[520, 170, 240], [1250, 130, 300], [1560, 250, 190]].map(([x, y, w], i) => (
          <div key={i} style={{ position: "absolute", left: x - shift * 0.2, top: y, width: w, height: w * 0.32, borderRadius: w, background: "#FFFFFF", opacity: 0.9 }} />
        ))}
        {[[-80, 610, 520, "#A7C97A"], [330, 650, 440, "#96BE6B"], [700, 620, 560, "#A7C97A"], [1180, 660, 430, "#96BE6B"], [1540, 610, 560, "#A7C97A"]].map(([x, y, w, col], i) => (
          <div key={i} style={{ position: "absolute", left: x - shift * 0.35, top: y, width: w, height: w * 0.95, borderRadius: "50% 50% 0 0 / 60% 60% 0 0", background: col }} />
        ))}
        <div style={{ position: "absolute", left: -100, top: 800, width: 2200, height: 300, background: "#7CC46F" }} />
        <BahayKubo x={1560 - shift * 0.5} y={640} s={0.9} />
        <Palm x={60} y={470} h={360} lean={-7} />
        <Palm x={1850} y={520} h={330} lean={8} />
        <div style={{ position: "absolute", left: -100, top: 900, width: 2200, height: 400, borderRadius: "50% 50% 0 0", background: C.sand }} />
        {[0, 1, 2, 3, 4, 5].map((i) => (
          <div key={i} style={{ position: "absolute", left: 180 + i * 330 - shift, top: 960, width: 150, height: 46, borderRadius: "50%", background: "#F2DDB8" }} />
        ))}
        <Banderitas />
      </>
    )}
    {children}
  </AbsoluteFill>
);

// The phone frame. Real footage plays inside; nothing in here is ever drawn or generated.
export const Phone = ({ rec, x = 960, y = 520, h = 760, audio = false, tilt = 0 }) => {
  const { dur } = useT();
  const w = h * 0.49;
  const p = "rec/" + rec;
  const s = clipSeconds(p);
  const rate = s ? Math.min(1.25, Math.max(0.75, s / dur)) : 1;
  return (
    <div style={{ position: "absolute", left: x - w / 2, top: y - h / 2, width: w, height: h, borderRadius: 46, background: "#1B1B1F",
      padding: 14, boxSizing: "border-box", boxShadow: "0 24px 60px rgba(74,46,24,0.35)", transform: `rotate(${tilt}deg)` }}>
      <div style={{ position: "relative", width: "100%", height: "100%", borderRadius: 34, overflow: "hidden", background: "#FFFFFF" }}>
        {has(p) ? (
          <OffthreadVideo src={staticFile(p)} muted={!audio} volume={audio ? 1 : 0} playbackRate={rate} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
        ) : (
          <Placeholder label={`REAL APP FOOTAGE\n${rec}\n(screen recording\nor filmed phone)`} />
        )}
      </div>
    </div>
  );
};

// Lily in her real in-app pose, with a small hop when she appears.
export const Lily = ({ pose = "idle", x = 520, y = 760, size = 360, flip = false }) => {
  const pop = usePop(0.05);
  const { t } = useT();
  const bob = Math.sin(t * 3) * 6;
  const file = `img/lily_${pose}.png`;
  const fallback = has(file) ? file : has("img/lily_waving.png") ? "img/lily_waving.png" : null;
  const style = { position: "absolute", left: x - size / 2, top: y - size / 2 + bob - (1 - pop) * -40, width: size, height: size,
    transform: `scale(${0.7 + 0.3 * pop}) scaleX(${flip ? -1 : 1})`, transformOrigin: "50% 100%" };
  if (fallback) return <Img src={staticFile(fallback)} style={{ ...style, objectFit: "contain" }} />;
  return (
    <div style={style}>
      <div style={{ position: "absolute", inset: "12%", borderRadius: "50%", background: C.lily }} />
      {[30, 54].map((l) => (
        <div key={l} style={{ position: "absolute", left: `${l}%`, top: "34%", width: "17%", height: "17%", borderRadius: "50%", background: C.peach }}>
          <div style={{ position: "absolute", inset: "24%", borderRadius: "50%", background: "#615048" }} />
        </div>
      ))}
      <div style={{ position: "absolute", left: 0, right: 0, bottom: "-12%", textAlign: "center", fontFamily: BODY, fontSize: 18, fontWeight: 700, color: C.slot }}>
        lily_{pose}.png
      </div>
    </div>
  );
};

export const Pill = ({ text, color, x, y, size = 44, at = 0 }) => {
  const pop = usePop(at);
  return (
    <div style={{ position: "absolute", left: x, top: y, transform: `translate(-50%, -50%) scale(${pop})`, background: color, color: C.white,
      fontFamily: BODY, fontWeight: 700, fontSize: size, padding: `${size * 0.3}px ${size * 0.8}px`, borderRadius: size * 1.2, whiteSpace: "nowrap",
      boxShadow: "0 8px 20px rgba(74,46,24,0.18)" }}>
      {text}
    </div>
  );
};

export const Logo = ({ size = 220, x = 960, y = 300, at = 0 }) => {
  const { frame, fps } = useT();
  const cols = [C.sun, C.teal, C.sun, C.teal, C.sun, C.teal];
  return (
    <div style={{ position: "absolute", left: x, top: y, transform: "translate(-50%, -50%)", fontFamily: TITLE, fontWeight: 800, fontSize: size, whiteSpace: "nowrap", lineHeight: 1 }}>
      {[..."PlayIT"].map((ch, i) => {
        const p = spring({ frame: frame - (at + i * 0.09) * fps, fps, config: { damping: 9, mass: 0.5 } });
        return <span key={i} style={{ display: "inline-block", color: cols[i], transform: `translateY(${(1 - p) * 60}px) scale(${p})`, WebkitTextStroke: `${size * 0.03}px ${C.brown}`, paintOrder: "stroke fill" }}>{ch}</span>;
      })}
    </div>
  );
};
