import React, { useEffect, useLayoutEffect, useRef, useState } from "react";
import { AbsoluteFill, Audio, Sequence, continueRender, delayRender, staticFile, useCurrentFrame, useVideoConfig } from "remotion";

// Shows film/film.html in a frame and, for every video frame, tells the page which second to draw.
// The sounds (voice, music, the app's effects) come from the list the page itself builds, so picture and sound cannot drift apart.
export const Film = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const ref = useRef(null);
  const [audio, setAudio] = useState(null);
  const [handle] = useState(() => delayRender("loading the film page", { timeoutInMilliseconds: 180000 }));

  useEffect(() => {
    let stop = false;
    const wait = () => {
      const w = ref.current && ref.current.contentWindow;
      if (w && w.FILM_READY) { setAudio(w.FILM_AUDIO.map((a) => ({ ...a }))); continueRender(handle); }
      else if (!stop) setTimeout(wait, 50);
    };
    wait();
    return () => { stop = true; };
  }, [handle]);

  useLayoutEffect(() => {
    const w = ref.current && ref.current.contentWindow;
    if (!w || !w.FILM_READY) return;
    w.seek(frame / fps);
    w.document.body.getBoundingClientRect();   // make the page apply the new time before the frame is captured
  }, [frame, fps, audio]);

  return (
    <AbsoluteFill style={{ background: "#BFE6F5" }}>
      <iframe ref={ref} title="film" src={staticFile("film/film.html") + "?embed=1"} style={{ width: "100%", height: "100%", border: 0 }} />
      {(audio || []).map((a, i) => (
        <Sequence key={i} from={Math.round(a.at * fps)} layout="none" name={a.file}>
          <Audio src={staticFile("film/" + a.file)} volume={a.vol} />
        </Sequence>
      ))}
    </AbsoluteFill>
  );
};
