// The whole video: every shot from timeline.json in order, plus voice, music, captions
// and the PROJECTED tag. Scene choice is by shot.type.
import React from "react";
import { AbsoluteFill, Audio, Sequence, staticFile, useCurrentFrame, useVideoConfig } from "remotion";
import { D, has } from "./parts";
import { C, BODY } from "./theme";
import { StoryShot, Triptych, Morph, Team } from "./scenes/Story";
import { DemoShot, LogoCard } from "./scenes/Demo";
import { ResultsPeople, ResultsCounts, ResultsBridge, ResultsQuote, ResultsTests } from "./scenes/Results";

const SCENES = {
  story: StoryShot, triptych: Triptych, morph: Morph, team: Team, demo: DemoShot, logo: LogoCard,
  results_people: ResultsPeople, results_counts: ResultsCounts, results_bridge: ResultsBridge, results_quote: ResultsQuote, results_tests: ResultsTests,
};

const Captions = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const t = frame / fps;
  const cue = D.captions.find((c) => t >= c.start && t < c.end);
  if (!cue) return null;
  return (
    <div style={{ position: "absolute", left: 0, right: 0, bottom: 44, display: "flex", justifyContent: "center" }}>
      <div style={{ maxWidth: 1500, background: "rgba(27,27,31,0.82)", color: "#FFFFFF", fontFamily: BODY, fontWeight: 600, fontSize: 46, lineHeight: 1.25,
        padding: "14px 34px", borderRadius: 22, textAlign: "center" }}>
        {cue.speaker === "lily" ? <span style={{ color: C.sun }}>Lily: </span> : null}
        {cue.text}
      </div>
    </div>
  );
};

const ProjectedTag = () => {
  const { fps } = useVideoConfig();
  const t = useCurrentFrame() / fps;
  const b = D.timeline.beats.find((x) => x.beat === 4);
  if (!D.projected || t < b.start || t >= b.end) return null;
  return (
    <div style={{ position: "absolute", left: 50, top: 44, background: C.sun, border: `3px solid ${C.brown}`, color: C.brown, fontFamily: BODY, fontWeight: 700, fontSize: 28, padding: "10px 26px", borderRadius: 30 }}>
      PROJECTED — replace with real results
    </div>
  );
};

export const Pitch = () => {
  const { fps } = useVideoConfig();
  return (
    <AbsoluteFill style={{ background: C.sand }}>
      {D.timeline.shots.map((shot) => {
        const Scene = SCENES[shot.type];
        return (
          <Sequence key={shot.id} from={Math.round(shot.start * fps)} durationInFrames={Math.round((shot.end - shot.start) * fps)} name={`${shot.id} ${shot.type}`}>
            <Scene shot={shot} />
          </Sequence>
        );
      })}
      {D.voice.filter((v) => v.file && has(v.file)).map((v) => (
        <Sequence key={v.id} from={Math.round(v.start * fps)} name={`voice ${v.id}`}>
          <Audio src={staticFile(v.file)} />
        </Sequence>
      ))}
      {D.music ? <Audio src={staticFile(D.music)} volume={0.12} loop /> : null}
      <ProjectedTag />
      <Captions />
    </AbsoluteFill>
  );
};
