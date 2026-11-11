import React from "react";
import { AbsoluteFill, Img, OffthreadVideo, staticFile, interpolate, spring } from "remotion";
import { D, StoryPicture, Placeholder, Stage, has, clipSeconds, useT, fade, usePop, easeInOut } from "../parts";
import { C, BODY, TITLE } from "../theme";

// A clean letter laid over a page in a still, for a picture whose own letter came out wrong and
// cannot be regenerated. `cover` in timeline.json is in the picture's own pixels: size [w, h],
// patch (the outline of the paper to repaint), paper (two colours, top to bottom), and the letter
// with the matrix that lays it flat on the page.
const Cover = ({ cover }) => {
  const [w, h] = cover.size;
  return (
    <svg viewBox={`0 0 ${w} ${h}`} preserveAspectRatio="xMidYMid slice" style={{ position: "absolute", left: 0, top: 0, width: "100%", height: "100%" }}>
      <defs>
        <linearGradient id="coverPaper" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor={cover.paper[0]} />
          <stop offset="1" stopColor={cover.paper[1]} />
        </linearGradient>
        <filter id="coverSoft"><feGaussianBlur stdDeviation="0.9" /></filter>
      </defs>
      <polygon points={cover.patch} fill="url(#coverPaper)" filter="url(#coverSoft)" />
      <text transform={`matrix(${cover.matrix.join(" ")})`} textAnchor="middle" fontFamily={BODY} fontWeight="700" fontSize={cover.fontSize} fill={cover.ink || "#4A3A2E"}>{cover.text}</text>
    </svg>
  );
};

export const StoryShot = ({ shot }) => (
  <StoryPicture clip={shot.clip} still={shot.still} move={shot.move} label={shot.what} dim={shot.dim} focus={shot.focus} lights={shot.lights} glow={shot.glow}>
    {shot.cover ? <Cover cover={shot.cover} /> : null}
  </StoryPicture>
);

// Three pictures sliding in like page turns, each drifting slowly once it lands.
export const Triptych = ({ shot }) => {
  const { t, dur } = useT();
  const each = dur / 3;
  return (
    <AbsoluteFill style={{ background: C.night, flexDirection: "row", gap: 24, padding: 60, boxSizing: "border-box" }}>
      {shot.stills.map((s, i) => {
        const k = easeInOut(fade(t, i * each, i * each + 0.6));
        const drift = 1.04 + 0.07 * interpolate(t, [i * each, dur], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });
        const p = has("story/" + s) ? "story/" + s : null;
        const pos = (shot.focus && shot.focus[i]) || "50% 50%";
        return (
          <div key={i} style={{ flex: 1, position: "relative", borderRadius: 28, overflow: "hidden", opacity: k, transform: `translateX(${(1 - k) * 140}px) rotate(${(1 - k) * 3}deg)` }}>
            {p ? <Img src={staticFile(p)} style={{ width: "100%", height: "100%", objectFit: "cover", objectPosition: pos, transform: `scale(${drift})`, transformOrigin: pos }} /> : <Placeholder dark label={`STORY WORLD\nneeds: ${s}`} />}
            <AbsoluteFill style={{ background: "linear-gradient(rgba(10,12,30,0) 55%, rgba(10,12,30,0.7) 100%)" }} />
            <div style={{ position: "absolute", left: 0, right: 0, bottom: 190, textAlign: "center", fontFamily: BODY, fontStyle: "italic", fontSize: 40, color: "#FFFFFF", textShadow: "0 2px 10px rgba(0,0,0,0.7)" }}>
              {shot.labels[i]}
            </div>
          </div>
        );
      })}
    </AbsoluteFill>
  );
};

// The morph. A generated clip if there is one. Otherwise it is made in code: the phone glows,
// then the app's flat style sweeps outward from it in two rings (the halfway room, then Lily's
// island), and flat Lily hops out of the phone and lands at the centre of the island.
export const Morph = ({ shot }) => {
  const { t, dur, frame, fps } = useT();
  if (has("story/" + shot.clip)) {
    const s = clipSeconds("story/" + shot.clip);
    return (
      <AbsoluteFill style={{ background: C.night }}>
        <OffthreadVideo src={staticFile("story/" + shot.clip)} muted playbackRate={s ? Math.min(1.5, Math.max(0.5, s / dur)) : 1} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
      </AbsoluteFill>
    );
  }
  const [a, b] = shot.stills;
  const from = shot.from || [51, 82];           // the phone in the first still, [x%, y%]
  const ox = 19.2 * from[0], oy = 10.8 * from[1], R = 1750;
  const k1 = easeInOut(fade(t, dur * 0.1, dur * 0.56)), k2 = easeInOut(fade(t, dur * 0.42, dur * 0.88));
  const reveal = (k) => ({ clipPath: `circle(${k * R}px at ${ox}px ${oy}px)` });
  const Ring = ({ k, color }) => (k > 0 && k < 1 ? (
    <div style={{ position: "absolute", left: ox - k * R, top: oy - k * R, width: 2 * k * R, height: 2 * k * R, borderRadius: "50%", boxSizing: "border-box",
      border: `${26 * (1 - k) + 6}px solid ${color}`, boxShadow: `0 0 70px 24px ${color}`, opacity: 0.85 * (1 - k * k) }} />
  ) : null);
  const zoom = 1 + 0.1 * easeInOut(fade(t, 0, dur * 0.5));
  // Lily: out of the phone, up in an arc, down onto the island
  const hop = easeInOut(fade(t, dur * 0.4, dur * 0.8));
  const land = spring({ frame: frame - dur * 0.8 * fps, fps, config: { damping: 8, mass: 0.6 } });
  const lf = shot.lilyFrom || from;            // the phone in the halfway still: Lily hops out of it
  const lx = interpolate(hop, [0, 1], [19.2 * lf[0], 960]), ly = interpolate(hop, [0, 1], [10.8 * lf[1], 700]) - Math.sin(hop * Math.PI) * 260;
  const size = interpolate(hop, [0, 1], [90, 460]);
  const squash = hop < 1 ? 1 : 1 - 0.14 * Math.sin(Math.min(1, land) * Math.PI) * (1 - Math.min(1, land) * 0.5);
  const lily = has("img/lily_waving.png");
  return (
    <AbsoluteFill style={{ background: C.night, overflow: "hidden" }}>
      <AbsoluteFill style={{ transform: `scale(${zoom})`, transformOrigin: `${from[0]}% ${from[1]}%` }}>
        <StoryPicture still={a} move="none" label="last Pixar frame" glow={from} />
      </AbsoluteFill>
      <AbsoluteFill style={reveal(k1)}>
        {has("story/" + b) ? <Img src={staticFile("story/" + b)} style={{ width: "100%", height: "100%", objectFit: "cover" }} /> : <AbsoluteFill style={{ background: `linear-gradient(${C.sky}, ${C.sand})` }} />}
      </AbsoluteFill>
      <Ring k={k1} color="rgba(255,197,61,0.9)" />
      <AbsoluteFill style={reveal(k2)}>
        <Stage />
      </AbsoluteFill>
      <Ring k={k2} color="rgba(255,255,255,0.9)" />
      {lily && hop > 0 ? (
        <Img src={staticFile("img/lily_waving.png")} style={{ position: "absolute", left: lx - size / 2, top: ly - size / 2, width: size, height: size, objectFit: "contain",
          opacity: fade(hop, 0, 0.12), transform: `rotate(${(1 - hop) * -18}deg) scale(${2 - squash}, ${squash})`, transformOrigin: "50% 100%" }} />
      ) : null}
    </AbsoluteFill>
  );
};

export const Team = ({ shot }) => {
  const { t, dur } = useT();
  const pop = usePop(0.2);
  const team = D.content.team;
  const p = has("img/" + shot.still) ? "img/" + shot.still : null;
  const drift = 1.08 - 0.08 * easeInOut(fade(t, 0, dur));
  return (
    <AbsoluteFill style={{ background: C.night, justifyContent: "center", alignItems: "center" }}>
      <div style={{ width: 1280, height: 720, marginTop: -20, borderRadius: 36, overflow: "hidden", position: "relative", transform: `scale(${0.92 + 0.08 * pop})` }}>
        {p ? <Img src={staticFile(p)} style={{ width: "100%", height: "100%", objectFit: "cover", transform: `scale(${drift})` }} /> : <Placeholder dark label={`TEAM REVEAL (Pixar-style group image)\nneeds: ${shot.still}`} />}
      </div>
      <div style={{ position: "absolute", top: 60, left: 0, right: 0, textAlign: "center", fontFamily: BODY, fontSize: 32, fontWeight: 600, color: "#FFFFFF", opacity: pop }}>
        {team.members.join("  ·  ")}
      </div>
    </AbsoluteFill>
  );
};
