// Beat 4. Everything here is drawn from round2_data.json (through generated/data.json).
// No number is typed in this file.
import React from "react";
import { interpolate } from "remotion";
import { D, Stage, Lily, useT, usePop, fade, easeInOut } from "../parts";
import { C, BODY, TITLE } from "../theme";

const N = D.numbers;
const big = { fontFamily: TITLE, fontWeight: 800, color: C.teal, lineHeight: 1 };
const Card = ({ children, style }) => <div style={{ position: "absolute", background: C.card, borderRadius: 36, boxShadow: "0 16px 40px rgba(74,46,24,0.16)", ...style }}>{children}</div>;
const Person = ({ x, y, s, color, k }) => (
  <div style={{ position: "absolute", left: x, top: y, width: s, height: s * 1.7, transform: `scale(${k})`, transformOrigin: "50% 100%" }}>
    <div style={{ position: "absolute", left: "22%", top: 0, width: "56%", height: s * 0.56, borderRadius: "50%", background: color }} />
    <div style={{ position: "absolute", left: 0, top: s * 0.64, width: "100%", height: s * 1.06, borderRadius: `${s * 0.5}px ${s * 0.5}px 12px 12px`, background: color }} />
  </div>
);
const PopPerson = ({ at, ...p }) => <Person {...p} k={usePop(at)} />;

export const ResultsPeople = () => {
  const { t } = useT();
  const g = N.groups;
  const kids = Array.from({ length: g.children }), adults = Array.from({ length: g.parents + g.teachers });
  const forward = fade(t, 1.6, 2.4);
  return (
    <Stage>
      <Lily pose="pointing" x={250} y={780} size={330} />
      <Card style={{ left: 470, top: 110, width: 1320, height: 800 }}>
        <div style={{ position: "absolute", left: 60, top: 44, ...big, fontSize: 96 }}>{N.total} participants</div>
        {adults.map((_, i) => <PopPerson key={"a" + i} at={0.3 + i * 0.04} x={80 + i * 84} y={250} s={58} color={i < g.parents ? "#9CCFCB" : "#C8B9A8"} />)}
        {kids.map((_, i) => <PopPerson key={"k" + i} at={0.9 + i * 0.04} x={80 + i * 74} y={450 + forward * 40} s={48 + forward * 12} color={C.teal} />)}
        <div style={{ position: "absolute", left: 60, top: 650, fontFamily: BODY, fontSize: 42, color: C.text }}>
          <b style={{ color: C.teal }}>{g.children} Grade 1 learners</b> — our users
        </div>
        <div style={{ position: "absolute", left: 60, top: 712, fontFamily: BODY, fontSize: 36, color: C.grey }}>
          {g.parents} parents · {g.teachers} teachers
        </div>
      </Card>
    </Stage>
  );
};

const DotRow = ({ item, y, at }) => {
  const { t } = useT();
  const v = item.value, n = item.of, d = Math.min(56, (1000 - (n - 1) * 12) / n);
  return (
    <div style={{ position: "absolute", left: 60, top: y, opacity: fade(t, at, at + 0.4) }}>
      <div style={{ ...big, fontSize: 110, width: 520 }}>{v} of {n}</div>
      <div style={{ position: "absolute", left: 540, top: 8, display: "flex", gap: 12 }}>
        {Array.from({ length: n }).map((_, i) => {
          const lit = i < v && t > at + 0.5 + i * 0.09;
          return <div key={i} style={{ width: d, height: d, borderRadius: "50%", background: lit ? C.teal : C.off, transform: `scale(${lit ? 1 : 0.85})` }} />;
        })}
      </div>
      <div style={{ position: "absolute", left: 540, top: 76, fontFamily: BODY, fontSize: 40, color: C.text, whiteSpace: "nowrap" }}>{item.label}</div>
    </div>
  );
};
export const ResultsCounts = () => {
  const { dur } = useT();
  return (
    <Stage>
      <Card style={{ left: 120, top: 150, width: 1680, height: 700 }}>
        <div style={{ position: "absolute", left: 60, top: 44, fontFamily: BODY, fontWeight: 700, fontSize: 44, color: C.teal }}>{D.content.results.users_heading}</div>
        <DotRow item={N.counts[0]} y={190} at={0.2} />
        <DotRow item={N.counts[1]} y={420} at={dur * 0.5} />
      </Card>
    </Stage>
  );
};

const Bar = ({ item, y, at }) => {
  const { t } = useT();
  const grow = fade(t, at + 0.2, at + 1.1), W = 900;
  return (
    <div style={{ position: "absolute", left: 60, top: y, opacity: fade(t, at, at + 0.3) }}>
      <div style={{ fontFamily: BODY, fontSize: 36, color: C.text, whiteSpace: "nowrap" }}>{item.label}</div>
      <div style={{ position: "absolute", left: 0, top: 58, width: W, height: 44, borderRadius: 22, background: C.off }} />
      <div style={{ position: "absolute", left: 0, top: 58, width: Math.max(44, W * ((item.value - 1) / 4) * grow), height: 44, borderRadius: 22, background: C.teal }} />
      {item.goal ? <div style={{ position: "absolute", left: W * ((item.goal - 1) / 4) - 3, top: 50, width: 6, height: 60, background: C.brown }} /> : null}
      <div style={{ position: "absolute", left: W + 40, top: 30, ...big, fontSize: 84, whiteSpace: "nowrap" }}>{item.value.toFixed(1)} / 5</div>
    </div>
  );
};
// The deck of the bridge: y for a given x, so the figures stand on it.
const deckY = (x) => { const u = (x - 180) / 1560; return 950 - 600 * u * (1 - u); };
export const ResultsBridge = () => {
  const { t, dur } = useT();
  const draw = easeInOut(fade(t, 0.1, 1.0));
  const pass = easeInOut(fade(t, 1.0, 2.6));
  const px = interpolate(pass, [0, 1], [760, 1270]);
  const stand = (x, s) => ({ x: x - s / 2, y: deckY(x) - s * 1.7 - 12, s });
  return (
    <Stage>
      {/* the bridge: the grown-ups pass the phone across to the child */}
      <svg width="1920" height="1080" style={{ position: "absolute", left: 0, top: 0 }}>
        <path d="M 180 950 Q 960 650 1740 950" fill="none" stroke={C.brown} strokeWidth="26" strokeLinecap="round" opacity="0.85" pathLength="1" strokeDasharray="1" strokeDashoffset={1 - draw} />
      </svg>
      <Person {...stand(600, 64)} color="#9CCFCB" k={usePop(0.5)} />
      <Person {...stand(700, 64)} color="#C8B9A8" k={usePop(0.6)} />
      <Person {...stand(1330, 50)} color={C.teal} k={usePop(0.9)} />
      <div style={{ position: "absolute", left: px - 23, top: deckY(px) - 150 - Math.sin(pass * Math.PI) * 40, width: 46, height: 84, borderRadius: 10, background: "#1B1B1F", border: `4px solid ${C.white}`, boxSizing: "border-box", opacity: fade(t, 0.8, 1.0) }} />
      <Card style={{ left: 240, top: 96, width: 1440, height: 560 }}>
        <div style={{ position: "absolute", left: 60, top: 36, fontFamily: BODY, fontWeight: 700, fontSize: 42, color: C.teal }}>{D.content.results.adults_heading}</div>
        {N.ratings.map((r, i) => <Bar key={i} item={r} y={120 + i * 140} at={0.8 + i * (dur - 2.4) / 3} />)}
      </Card>
    </Stage>
  );
};

export const ResultsQuote = () => {
  const pop = usePop(0.2);
  return (
    <Stage>
      <Lily pose="listening" x={300} y={780} size={330} />
      <Card style={{ left: 520, top: 220, width: 1240, height: 480, transform: `scale(${0.9 + 0.1 * pop})`, opacity: pop }}>
        <div style={{ position: "absolute", left: 70, top: 60, right: 70, fontFamily: BODY, fontWeight: 700, fontStyle: "italic", fontSize: 64, color: C.brown, lineHeight: 1.25 }}>{N.quote}</div>
        <div style={{ position: "absolute", left: 70, bottom: 54, fontFamily: BODY, fontSize: 36, color: C.grey }}>— {N.quoteSource}</div>
      </Card>
    </Stage>
  );
};

export const ResultsTests = () => {
  const { t } = useT();
  const pop = usePop(1.0);
  return (
    <Stage>
      <div style={{ position: "absolute", left: 160, top: 120, width: 1600, display: "flex", flexWrap: "wrap", gap: 14 }}>
        {Array.from({ length: 144 }).map((_, i) => (
          <div key={i} style={{ width: 86, height: 60, borderRadius: 14, background: C.leaf, opacity: t > i * 0.012 ? 0.9 : 0, color: C.white, fontFamily: BODY, fontWeight: 700, fontSize: 38, textAlign: "center", lineHeight: "60px" }}>✓</div>
        ))}
      </div>
      <Card style={{ left: 460, top: 360, width: 1000, height: 320, transform: `scale(${pop})` }}>
        <div style={{ position: "absolute", left: 0, right: 0, top: 50, textAlign: "center", ...big, fontSize: 112, whiteSpace: "nowrap" }}>{N.testsPass} tests pass</div>
        <div style={{ position: "absolute", left: 0, right: 0, top: 210, textAlign: "center", fontFamily: BODY, fontSize: 44, color: C.grey }}>{N.testsFail} fail</div>
      </Card>
    </Stage>
  );
};
