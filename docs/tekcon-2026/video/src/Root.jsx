import React from "react";
import { Composition } from "remotion";
import { Pitch } from "./Pitch";
import data from "./generated/data.json";

export const Root = () => {
  const fps = data.timeline.fps;
  const last = data.timeline.shots[data.timeline.shots.length - 1];
  return <Composition id="Pitch" component={Pitch} fps={fps} width={1920} height={1080} durationInFrames={Math.round(last.end * fps)} />;
};
