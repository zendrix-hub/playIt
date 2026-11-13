"""Rebuild Mia's arm markup in film.html from one set of numbers (left arm at rest; the right arm is its mirror about x = 520)."""
import math, sys
import os; p = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "film.html")
s = open(p, encoding="utf-8", newline="").read(); crlf = "\r\n" in s; s = s.replace("\r\n", "\n")
P = (470, 452); E = (440, 480); HAND = 47; WR = 33; K = float(sys.argv[1]) if len(sys.argv) > 1 else 1.0   # K: hand size
ang = math.atan2(15, -45); ux, uy = math.cos(ang), math.sin(ang); W = (E[0] + WR * ux, E[1] + WR * uy)
vx, vy = (-uy, ux)
if vy < 0: vx, vy = -vx, -vy
f = lambda v: ("%.1f" % v).rstrip("0").rstrip(".")
def arm(side, sfx, front):
    m = (lambda x: x) if side == "L" else (lambda x: 1040 - x)
    base, shade = ("#DDA273", "#BE7F52") if side == "L" else ("#D29766", "#AE7349")
    armc = base if side == "L" else "#C98B5C"
    slv, hem = ("#F0F4F7", "#D5DDE3") if side == "L" else ("#DDE6EC", "#C6D0D8")
    pt = lambda x, y: f"{f(m(x))} {f(y)}"
    line = lambda a, b, w, c, extra="": f'<path d="M{pt(*a)} L{pt(*b)}" stroke="{c}" stroke-width="{f(w)}" stroke-linecap="round"{extra}/>'
    hx, hy = E[0] + HAND * ux, E[1] + HAND * uy
    # open hand lying on the table: palm, four fingers toward the viewer, thumb on the inner side, thin lines between the fingers
    fing = [f'<ellipse cx="{f(m(hx))}" cy="{f(hy - 2 * K)}" rx="{f(12.6 * K)}" ry="{f(8.2 * K)}" fill="{base}"/>']
    xs = [-9.2 * K, -3.1 * K, 3.1 * K, 9.2 * K]; tips = [7.4 * K, 9.6 * K, 9.6 * K, 7.4 * K]
    for dx, tip in zip(xs, tips): fing.append(line((hx + dx, hy), (hx + dx * 1.05, hy + tip), 5.3 * K, base))
    for dx in (-6.15 * K, 0, 6.15 * K): fing.append(line((hx + dx, hy + 1.5 * K), (hx + dx * 1.05, hy + 7.2 * K), 1.25, shade))
    fing.append(line((hx + 9.6 * K, hy - 6.4 * K), (hx + 13.6 * K, hy - 2.6 * K), 5.6 * K, base))
    # pointing hand: a fist with the index finger out along the forearm
    cx, cy = W[0] + 6 * ux, W[1] + 6 * uy; o = lambda a, b: (cx + a * ux + b * vx, cy + a * uy + b * vy)
    a0 = math.degrees(ang); rot = f'rotate({f(a0) if side == "L" else f(180 - a0)} {f(m(cx))} {f(cy)})'
    point = [f'<ellipse cx="{f(m(cx))}" cy="{f(cy)}" rx="{f(8.6 * K)}" ry="{f(10 * K)}" fill="{base}" transform="{rot}"/>']
    for b in (-1.4 * K, 3 * K, 7.2 * K): point.append(line(o(5 * K, b), o(8.6 * K, b), 4.2 * K, shade))
    point.append(line(o(4 * K, -6.2 * K), o(19 * K, -6.2 * K), 5.4 * K, base))
    point.append(line(o(-2 * K, -9.2 * K), o(4.8 * K, -10.4 * K), 5 * K, base))
    fore = f'''<g id="fore{side}{sfx}" style="transform-origin: {f(m(E[0]))}px {E[1]}px">
          {line(E, W, 13, armc)}
          <g id="fing{side}{sfx}">{"".join(fing)}</g>
          <g id="point{side}{sfx}" opacity="0">{"".join(point)}</g>
        </g>'''
    if front: return f'''<g id="arm{side}{sfx}" style="transform-origin: {f(m(P[0]))}px {P[1]}px">
        {fore}
      </g>'''
    return f'''<g id="arm{side}{sfx}" style="transform-origin: {f(m(P[0]))}px {P[1]}px">
        {fore}
        {line((464, 462), E, 15, armc)}
        <circle cx="{f(m(E[0]))}" cy="{E[1]}" r="7.2" fill="{armc}"/>
        {line((471, 450), (465, 461), 22, slv)}
        <path d="M{pt(455, 463)} Q{pt(464, 469)} {pt(475, 464)}" stroke="{hem}" stroke-width="3" fill="none" stroke-linecap="round"/>
      </g>'''
hx, hy = E[0] + HAND * ux, E[1] + HAND * uy
sh = lambda x: f'cx="{f(x)}" cy="{f(hy + 5.4 * K)}" rx="{f(17.5 * K)}" ry="{f(6.6 * K)}" fill="#55371C" fill-opacity=".42"'
back = f'''      <!-- Mia's arms. Each has a short blouse sleeve, an upper arm, and a forearm with a hand that bends at the elbow (#foreL/#foreR).
           At rest the forearms lie on the table with a hand on each side of the book, never on it. The arms are drawn BEHIND the book.
           Each hand has two drawings: #fingL/#fingR (open, four fingers and a thumb) and #pointL/#pointR (index finger out, for taps).
           #tshL/#tshR are the hands' shadows on the table. This block is written by tools/gen_arms.py; its numbers must match ARM in lib.js ("Mia's arms"). -->
      <ellipse id="tshL" {sh(hx)}/><ellipse id="tshR" {sh(1040 - hx)}/>
      {arm("L", "", False)}
      {arm("R", "", False)}

'''
a = s.index("      <!-- Mia's arms. Each has"); b = s.index("      <!-- book: a real open book"); s = s[:a] + back + s[b:]
a = s.index('      <g id="frontL" opacity="0">'); b = s.index("      <!-- lamp -->")
s = s[:a] + f'''      <g id="frontL" opacity="0">{arm("L", "f", True)}</g>
      <g id="frontR" opacity="0">{arm("R", "f", True)}</g>

''' + s[b:]
open(p, "w", encoding="utf-8", newline="").write(s.replace("\n", "\r\n") if crlf else s)
print("E", E, "W", [round(v, 1) for v in W], "hand centre", round(hx, 1), round(hy, 1))
