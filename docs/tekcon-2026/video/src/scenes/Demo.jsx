// Beats 2-3 in the app's own flat style. The stage is Lily's island; the phone plays REAL footage.
import React from "react";
import { AbsoluteFill, Img, OffthreadVideo, staticFile, interpolate } from "remotion";
import { D, Stage, Phone, Placeholder, Lily, Pill, Logo, has, clipSeconds, useT, usePop, fade, easeInOut } from "../parts";
import { C, BODY, TITLE, STEP_NAMES } from "../theme";

const Rings = () => {
  const { t } = useT();
  return [0, 1, 2].map((i) => {
    const k = ((t * 0.9 + i / 3) % 1);
    return <div key={i} style={{ position: "absolute", left: 760 - k * 260, top: 520 - k * 130, width: 80 + k * 260, height: 80 + k * 260, borderRadius: "50%", border: `8px solid ${C.steps[1]}`, opacity: 1 - k }} />;
  });
};
const Padlock = () => {
  const { t } = useT();
  const travel = fade(t, 0.6, 2.2), lock = usePop(2.4);
  return (
    <>
      {[0, 1, 2, 3, 4].map((i) => (
        <div key={i} style={{ position: "absolute", left: 1500 - travel * 330 + i * 26, top: 520 - (10 + (i % 3) * 14), width: 12, height: 20 + (i % 3) * 28, borderRadius: 6, background: C.steps[2], opacity: 1 - fade(t, 2.0, 2.5) }} />
      ))}
      <div style={{ position: "absolute", left: 1210, top: 300, transform: `scale(${lock})`, width: 90, height: 76, borderRadius: 16, background: C.teal }}>
        <div style={{ position: "absolute", left: 20, top: -42, width: 50, height: 56, borderRadius: "28px 28px 0 0", border: `12px solid ${C.teal}`, borderBottom: "none", boxSizing: "border-box" }} />
        <div style={{ position: "absolute", left: 37, top: 26, width: 16, height: 24, borderRadius: 8, background: C.white }} />
      </div>
    </>
  );
};
const Tiles = () => {
  const { t, dur } = useT();
  return ["S", "A", "M"].map((ch, i) => {
    const k = usePopAt(dur * 0.72 + i * 0.18);
    return (
      <div key={i} style={{ position: "absolute", left: 1250 + i * 130, top: 330 + (1 - k) * 160, width: 110, height: 110, borderRadius: 22, background: C.sun, opacity: Math.min(1, k * 2),
        fontFamily: TITLE, fontWeight: 800, fontSize: 80, color: C.brown, textAlign: "center", lineHeight: "110px", boxShadow: "0 8px 0 #E0A51F", transform: `scale(${0.6 + 0.4 * k})` }}>{ch}</div>
    );
  });
};
// hook wrapper so the map() above stays legal
const usePopAt = (at) => usePop(at);
const Report = () => {
  const k = usePop(0.9);
  return (
    <div style={{ position: "absolute", left: 1240, top: 300 + (1 - k) * 80, width: 300, height: 400, borderRadius: 18, background: C.white, opacity: k, boxShadow: "0 16px 40px rgba(74,46,24,0.25)", padding: 30, boxSizing: "border-box" }}>
      <div style={{ height: 30, width: "70%", borderRadius: 8, background: C.teal, marginBottom: 26 }} />
      {[90, 75, 85, 60, 80].map((w, i) => <div key={i} style={{ height: 18, width: `${w}%`, borderRadius: 9, background: C.off, marginBottom: 20 }} />)}
    </div>
  );
};
const Airplane = ({ entering }) => {
  const k = usePop(entering ? 1.2 : -10);
  return (
    <div style={{ position: "absolute", right: 60, top: 84, transform: `scale(${k})`, transformOrigin: "100% 0%", background: C.card, border: `4px solid ${C.teal}`, borderRadius: 40,
      padding: "14px 30px", fontFamily: BODY, fontWeight: 700, fontSize: 34, color: C.teal }}>✈ No internet</div>
  );
};

// What plays inside the phone. A real screen recording (assets/recordings/rec_*.mp4) always wins.
// Until it arrives, the real screenshot named in the shot's `screen` is shown, with a tap ring at
// each of `taps` ([x%, y%, seconds into the shot]). Nothing else is ever put in the phone.
const Tap = ({ x, y, at }) => {
  const { t } = useT();
  const k = fade(t, at, at + 0.55);
  if (t < at - 0.25 || k >= 1) return null;
  const press = fade(t, at - 0.25, at);
  return (
    <>
      <div style={{ position: "absolute", left: `${x}%`, top: `${y}%`, width: 150 * k + 30, height: 150 * k + 30, transform: "translate(-50%, -50%)", borderRadius: "50%", border: `${8 * (1 - k) + 2}px solid ${C.sun}`, boxSizing: "border-box", opacity: 1 - k }} />
      <div style={{ position: "absolute", left: `${x}%`, top: `${y}%`, width: 46, height: 46, transform: `translate(-50%, -50%) scale(${1.3 - 0.3 * press})`, borderRadius: "50%", background: "rgba(255,255,255,0.75)", border: `4px solid ${C.brown}`, boxSizing: "border-box", opacity: press * (1 - k) }} />
    </>
  );
};
const Screen = ({ rec, screen, taps = [], audio, span, offset = 0 }) => {
  const p = "rec/" + rec;
  if (has(p)) {
    const s = clipSeconds(p);
    const rate = s ? Math.min(1.25, Math.max(0.75, s / span)) : 1;
    return <OffthreadVideo src={staticFile(p)} muted={!audio} volume={audio ? 1 : 0} playbackRate={rate} style={{ width: "100%", height: "100%", objectFit: "cover" }} />;
  }
  if (screen && has("img/" + screen)) {
    return (
      <>
        <Img src={staticFile("img/" + screen)} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
        {taps.map(([x, y, at], i) => <Tap key={i} x={x} y={y} at={at + offset} />)}
      </>
    );
  }
  return <Placeholder label={`REAL APP SCREEN NEEDED\n${rec}\n(screen recording\nor filmed phone)`} />;
};
// The key of what a shot leaves on the phone, so the next shot knows whether the screen changed.
const shown = (rec, screen) => (has("rec/" + rec) ? "rec:" + rec : "img:" + (screen || rec));

export const DemoShot = ({ shot }) => {
  const { t, dur } = useT();
  const cut = dur * (shot.split || 0.5);
  const second = shot.rec2 && t > cut;
  const step = second ? shot.step2 : shot.step;
  const showAirplane = shot.id >= 9; // pinned from the moment airplane mode goes on
  // screens slide across inside the phone whenever the screen changes
  const demos = D.timeline.shots.filter((x) => x.type === "demo");
  const prev = demos[demos.findIndex((x) => x.id === shot.id) - 1];
  const prevKey = prev ? (prev.rec2 ? shown(prev.rec2, prev.screen2) : shown(prev.rec, prev.screen)) : null;
  const firstKey = shown(shot.rec, shot.screen);
  const SLIDE = 0.45;
  const in1 = prev && prevKey !== firstKey ? easeInOut(fade(t, 0, SLIDE)) : 1;
  const in2 = shot.rec2 ? easeInOut(fade(t, cut, cut + SLIDE)) : 0;
  const layer = (x) => ({ position: "absolute", inset: 0, transform: `translateX(${x}%)` });
  const rise = prev ? 0 : (1 - easeInOut(fade(t, 0, 0.7))) * 900; // the phone slides up when the demo begins
  return (
    <Stage>
      {shot.title ? <Logo size={190} x={1430} y={330} at={0.4} /> : null}
      {shot.title ? <div style={{ position: "absolute", left: 1430, top: 480, transform: "translate(-50%, -50%)", fontFamily: TITLE, fontSize: 44, color: C.teal, whiteSpace: "nowrap", opacity: fade(t, 1.4, 2.0) }}>Offline phonics for Grade 1 learners</div> : null}
      {shot.badges ? shot.badges.map((b, i) => <Pill key={b} text={b} color={C.teal} x={1400} y={300 + i * 130} size={50} at={0.3 + i * 1.3} />) : null}
      <Phone x={960 - (shot.title || shot.badges ? 130 : 0)} y={520 + rise} tilt={shot.airplane ? interpolate(t, [0, 0.5], [-5, 0], { extrapolateRight: "clamp" }) : 0}>
        {in1 < 1 ? <div style={layer(-100 * in1)}><Screen rec={prev.rec2 || prev.rec} screen={prev.rec2 ? prev.screen2 : prev.screen} span={dur} /></div> : null}
        {in2 < 1 ? <div style={layer(100 * (1 - in1) - 100 * in2)}><Screen rec={shot.rec} screen={shot.screen} taps={shot.taps} audio={Boolean(shot.recAudio)} span={shot.rec2 ? cut : dur} /></div> : null}
        {shot.rec2 && in2 > 0 ? <div style={layer(100 * (1 - in2))}><Screen rec={shot.rec2} screen={shot.screen2} taps={shot.taps2} span={dur - cut} offset={cut} /></div> : null}
      </Phone>
      <Lily pose={shot.pose} x={shot.title || shot.badges ? 380 : 470} y={770} size={380} />
      {step ? <Pill text={STEP_NAMES[step]} color={C.steps[step]} x={470} y={250} size={56} at={second ? cut + 0.1 : 0.15} /> : null}
      {shot.label ? <Pill text={shot.label} color={C.teal} x={470} y={250} size={56} at={0.15} /> : null}
      {shot.note ? <div style={{ position: "absolute", left: 470, top: 330, transform: "translate(-50%, 0)", fontFamily: BODY, fontStyle: "italic", fontSize: 36, color: C.brown, whiteSpace: "nowrap", opacity: fade(t, 1.0, 1.6) }}>{shot.note}</div> : null}
      {shot.prop === "rings" ? <Rings /> : null}
      {shot.prop === "padlock" ? <Padlock /> : null}
      {shot.prop === "tiles" && second ? <Tiles /> : null}
      {shot.prop === "report" ? <Report /> : null}
      {showAirplane ? <Airplane entering={shot.airplane === "enter"} /> : null}
    </Stage>
  );
};

export const LogoCard = ({ shot }) => {
  const { t } = useT();
  return (
    <Stage>
      <Logo size={260} x={960} y={360} at={0.1} />
      <div style={{ position: "absolute", left: 960, top: 540, transform: "translate(-50%, -50%)", fontFamily: TITLE, fontSize: 56, color: C.teal, whiteSpace: "nowrap", opacity: fade(t, 0.9, 1.4) }}>Offline phonics for Grade 1 learners</div>
      <Lily pose={shot.pose} x={960} y={800} size={340} />
    </Stage>
  );
};
