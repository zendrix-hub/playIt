import React from "react";
import { Composition, registerRoot } from "remotion";
import { Film } from "./Film";

// The film itself lives in ../../film/ as a web page; this only wraps it so Remotion can turn it into an MP4.
const Root = () => <Composition id="Film" component={Film} fps={30} width={1920} height={1080} durationInFrames={3600} />;
registerRoot(Root);
