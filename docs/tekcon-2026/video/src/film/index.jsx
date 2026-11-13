import React from "react";
import { Composition, registerRoot } from "remotion";
import { Film } from "./Film";

// The film itself lives in ../../film/ as a web page; this only wraps it so Remotion can turn it into an MP4.
// The film is two minutes whatever the frame rate. scripts/render_film.mjs passes the frame rate in (30 unless PITCH_FPS is set).
const SECONDS = 120;
const Root = () => (
  <Composition id="Film" component={Film} width={1920} height={1080} fps={30} durationInFrames={SECONDS * 30} defaultProps={{ fps: 30 }}
    calculateMetadata={({ props }) => { const fps = Number(props.fps) || 30; return { fps, durationInFrames: SECONDS * fps }; }} />
);
registerRoot(Root);
