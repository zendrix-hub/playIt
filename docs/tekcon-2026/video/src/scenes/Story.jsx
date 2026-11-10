import React from "react";
import { AbsoluteFill, Img, OffthreadVideo, staticFile, interpolate } from "remotion";
import { D, StoryPicture, Placeholder, Stage, Lily, has, clipSeconds, useT, fade, usePop } from "../parts";
import { C, BODY } from "../theme";

export const StoryShot = ({ shot }) => <StoryPicture clip={shot.clip} still={shot.still} move={shot.move} label={shot.what} dim={shot.dim} />;

// Three pictures sliding in like page turns.
export const Triptych = ({ shot }) => {
  const { t, dur } = useT();
  const each = dur / 3;
  return (
    <AbsoluteFill style={{ background: C.night, flexDirection: "row", gap: 24, padding: 60, boxSizing: "border-box" }}>
      {shot.stills.map((s, i) => {
        const k = fade(t, i * each, i * each + 0.5);
        const p = has("story/" + s) ? "story/" + s : null;
        return (
          <div key={i} style={{ flex: 1, position: "relative", borderRadius: 28, overflow: "hidden", opacity: k, transform: `translateX(${(1 - k) * 120}px)` }}>
            {p ? <Img src={staticFile(p)} style={{ width: "100%", height: "100%", objectFit: "cover" }} /> : <Placeholder dark label={`STORY WORLD\nneeds: ${s}`} />}
            <div style={{ position: "absolute", left: 0, right: 0, bottom: 190, textAlign: "center", fontFamily: BODY, fontStyle: "italic", fontSize: 40, color: "#FFFFFF", textShadow: "0 2px 10px rgba(0,0,0,0.7)" }}>
              {shot.labels[i]}
            </div>
          </div>
        );
      })}
    </AbsoluteFill>
  );
};

// The morph. A generated clip if there is one; otherwise a blend through the halfway picture,
// spreading outward from the phone, ending on the island stage.
export const Morph = ({ shot }) => {
  const { t, dur } = useT();
  if (has("story/" + shot.clip)) {
    const s = clipSeconds("story/" + shot.clip);
    return (
      <AbsoluteFill style={{ background: C.night }}>
        <OffthreadVideo src={staticFile("story/" + shot.clip)} muted playbackRate={s ? Math.min(1.5, Math.max(0.5, s / dur)) : 1} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
      </AbsoluteFill>
    );
  }
  const [a, b] = shot.stills;
  const k1 = fade(t, dur * 0.15, dur * 0.6), k2 = fade(t, dur * 0.45, dur * 0.95);
  const reveal = (k) => ({ clipPath: `circle(${k * 130}% at 50% 62%)` });
  const zoom = 1 + 0.06 * interpolate(t, [0, dur], [0, 1]);
  return (
    <AbsoluteFill style={{ background: C.night }}>
      <AbsoluteFill style={{ transform: `scale(${zoom})` }}>
        <StoryPicture still={a} move="none" label="last Pixar frame" />
      </AbsoluteFill>
      <AbsoluteFill style={reveal(k1)}>
        {has("story/" + b) ? <Img src={staticFile("story/" + b)} style={{ width: "100%", height: "100%", objectFit: "cover" }} /> : <AbsoluteFill style={{ background: `linear-gradient(${C.sky}, ${C.sand})` }} />}
      </AbsoluteFill>
      <AbsoluteFill style={reveal(k2)}>
        <Stage><Lily pose="waving" x={960} y={700} size={460} /></Stage>
      </AbsoluteFill>
      {k1 > 0 && k2 < 1 ? <AbsoluteFill style={{ background: "radial-gradient(circle at 50% 62%, rgba(255,197,61,0.35), rgba(255,197,61,0) 60%)", opacity: 1 - Math.abs(0.5 - (k1 + k2) / 2) * 2 }} /> : null}
    </AbsoluteFill>
  );
};

export const Team = ({ shot }) => {
  const pop = usePop(0.2);
  const team = D.content.team;
  const p = has("img/" + shot.still) ? "img/" + shot.still : null;
  return (
    <AbsoluteFill style={{ background: C.night, justifyContent: "center", alignItems: "center" }}>
      <div style={{ width: 1280, height: 720, marginTop: -20, borderRadius: 36, overflow: "hidden", position: "relative", transform: `scale(${0.92 + 0.08 * pop})` }}>
        {p ? <Img src={staticFile(p)} style={{ width: "100%", height: "100%", objectFit: "cover" }} /> : <Placeholder dark label={`TEAM REVEAL (Pixar-style group image)\nneeds: ${shot.still}`} />}
      </div>
      <div style={{ position: "absolute", top: 60, left: 0, right: 0, textAlign: "center", fontFamily: BODY, fontSize: 32, fontWeight: 600, color: "#FFFFFF", opacity: pop }}>
        {team.members.join("  ·  ")}
      </div>
    </AbsoluteFill>
  );
};
