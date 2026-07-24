# playIT — 05. Map Screen
### MapScreen Audit + Phase D Tasks
See `00_ProjectRules.md` for source priority and global constraints. Consumes the 4-state node color system from `01_Foundation.md` §2.1 and `MapNodeBase` from `02_ComponentLibrary.md`. Reduced-motion gating detailed in `03_MotionSystem.md`.

---

## 1. MapScreen Audit `[orig §3.4]`

| | |
|---|---|
| **Strengths** | Winding Candy-Crush-style path is well-suited to spatial-journey mental models research recommends; auto-scroll to current node is already specified; locked/unlocked states exist. |
| **Weaknesses** | Node coloring appears per-letter/decorative rather than status-semantic (`01_Foundation.md` §1.3 #5); parallax clouds present real reduced-motion risk if not gated (see `03_MotionSystem.md`); hearts + star counters visible but no persistent app-wide linear progress bar (`01_Foundation.md` §1.2). |
| **Why it matters** | This is the highest-traffic screen in the app — every session starts and ends here, so inconsistencies here are seen more often than anywhere else. |
| **Educational impact** | **High** — this is where "are we there yet?" anxiety is either resolved or created. |
| **Effort** | Medium-High |
| **Recommendation** | Apply the 4-state node color system; gate parallax behind the reduced-motion toggle; consider adding a slim top-anchored "X/28 letters" bar as a secondary, non-competing progress signal above the winding path. |

---

## 2. Map Node Semantics (full spec, resolves `01_Foundation.md` §1.3 #5) `[orig §2.4]`

Node fill color is no longer decorative-per-letter. Strict 4-state system:

- **Locked** → `disabled` (monochromatic gray) + padlock icon
- **Unlocked/upcoming** → `learningBlue`, static
- **Current/active** → `achievementGold`, pulsing (largest node)
- **Completed** → `growthGreen`, solid, star-count badge

A per-letter rainbow treatment can still exist *inside* the node (the glyph or anchor illustration) — but the node's status ring/fill must always communicate progress state first. Iconography rules (padlock, checkmark, etc.) per `02_ComponentLibrary.md` §7.

**Component:** `MapNodeBase.kt` (shared base for `LetterNode` + `BlendItChallengeNode`) — see `02_ComponentLibrary.md` §2, package structure, and §10 task C5.

---

## 3. Phase D — High-Impact Screens (Map-specific tasks)

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| D1. Map Screen: apply `MapNodeBase` color system | Fix decorative→semantic node coloring | Child instantly understands progress state | 02_ComponentLibrary.md C5 | Medium | Must Have |
| D2. Map Screen: gate parallax clouds behind reduced-motion state | Prevent sensory overload | Safer for motion-sensitive children | 03_MotionSystem.md C8 | Low | Must Have |
| D3. Map Screen: add slim top-anchored "X/28 letters" secondary progress signal | Answer "are we there yet?" at the journey level, not just per-lesson | Reduced temporal anxiety | 02_ComponentLibrary.md C6 (`SegmentedProgressBar`) | Medium | Should Have |
| D4. Profile Select / Name Prompt / Splash: apply tokens, verify 16dp spacing | Baseline consistency on first-run screens | Fewer accidental profile mis-taps | 01_Foundation.md A1–A2 | Low | Should Have — full screen detail in `01_Foundation.md` §6 |

---

## 4. Priority Context

Map Screen ranks #1 in the Summary Priority Ranking by Educational Impact × Frequency (`10_FinalPolish.md` §Deployment Readiness) — highest frequency, status-color fix + progress bar.
