# playIT — 03. Motion System
### Motion Language, Reduced-Motion Toggle, Mascot Idle Scaffolding
See `00_ProjectRules.md` for source priority and global constraints. Depends on tokens in `01_Foundation.md`; the `MascotIdleScaffold` and `ReducedMotionState` components are inventoried in `02_ComponentLibrary.md` (C4, C8).

---

## 1. Motion Language & Animation Standards `[orig §2.12]`

Durations unchanged: Micro 150–250ms, Standard 300–500ms, Celebration 600–1200ms. Spring physics: `dampingRatio = MediumBouncy, stiffness = Low`.

**New rules:**
- Background decorative loops disabled during active tasks (Hear It, Say It, Find It, Blend It), permitted only on Map/Celebration.
- Object motion travels straight, predictable, slow paths.
- Reduced-motion mode replaces:
  - particles → fades
  - pulsing loops → static soft fade-in
  - parallax → static layers
- Enforced via a real toggle, not just intention — see `ReducedMotionToggle`, §2 below.

---

## 2. Reduced-Motion System (resolves accessibility gap in `01_Foundation.md` §5) `[orig §2.13]`

| Rule | Standard | Status |
|---|---|---|
| Motion safety | Reduced-motion toggle | Gap — no such composable exists in the SDD; add it |

**Component:** `ReducedMotionState.kt` (in `core/ui/a11y/`, see `02_ComponentLibrary.md` §2) backs a `ReducedMotionToggle` composable that must live in Parent Dashboard (see `09_ParentDashboard.md` task F1) since it is a parent-facing setting.

**Scope of enforcement — every motion source in the app must check this state, not just some:**
- Map parallax clouds (see `05_MapScreen.md` task D2)
- Celebration particles (confetti, star drop/bounce/glow — see `10_FinalPolish.md` task G3)
- Mascot idle loop (§3 below)

---

## 3. Mascot Behavior System — Idle Scaffolding (extends v1.0, resolves `01_Foundation.md` §1.3 #4) `[orig §2.11]`

Retains v1.0 fully: friendly-teacher personality, five emotional states (Happy, Excited, Thinking, Encouraging, Celebrating), text+audio always paired, exact feedback-language substitutions.

**New — idle scaffolding:** After 10 seconds of no interaction, `MascotBubble` shifts to Thinking and gaze/points toward the correct next action. Never framed as a penalty or hint-counter increase.

Implementation note: `MascotIdleScaffold.kt` wraps `MascotBubble.kt` and owns the 10-second timer logic (see `02_ComponentLibrary.md` §2, package structure). Applies to: Map, Hear It, Say It, Find It, Blend It.

---

## 4. Phase / Task Cross-Reference

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| C8. Add `ReducedMotionToggle` to Parent Dashboard + wire to `ReducedMotionState` | Close accessibility gap | Usable by neurodivergent/motion-sensitive children | 01_Foundation.md A1–A2 | Low-Medium | Must Have |
| D2. Map Screen: gate parallax clouds behind reduced-motion state | Prevent sensory overload | Safer for motion-sensitive children | C8 | Low | Must Have |
| F1. Parent Dashboard: wire `ReducedMotionToggle` into a real Settings section | Give the accessibility toggle a home | Parents can act on motion-sensitivity needs | C8 | Low | Must Have |
| H2. Reduced-motion audit end-to-end (Map parallax, celebration particles, mascot idle loop) | Ensure the toggle actually suppresses every motion source | Real protection for motion-sensitive/neurodivergent children | C8, D2 | Medium | Must Have |
| C4. Build `MascotBubble` + `MascotIdleScaffold` (10s idle cue) | Ship idle-scaffolding once, everywhere | Non-judgmental help exactly when a child stalls | 01_Foundation.md A1–A2 | Medium | Should Have |

Full task detail and priority context: D2 in `05_MapScreen.md`, F1 in `09_ParentDashboard.md`, H2 and G3 in `10_FinalPolish.md`, C4/C8 in `02_ComponentLibrary.md`.
