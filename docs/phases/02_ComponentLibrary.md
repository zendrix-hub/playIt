# playIT — 02. Component Library
### Component Inventory, Package Structure, Hearts Ruling, Error/Empty States
See `00_ProjectRules.md` for source priority and global constraints. Consumes tokens defined in `01_Foundation.md`. Consumed by every screen file (`05`–`10`).

---

## 1. Duplication Found in the Current SDD Component Tables `[orig §4.1]`

Cross-referencing every module's Front-end/Back-end component tables in the SDD reveals the same UI pattern implemented independently, per-screen, at least four times:

| Pattern | Currently implemented as (per SDD) | Screens |
|---|---|---|
| Feedback card (correct/incorrect) | `FeedbackCard` (Say It), inline flash on `PictureCard` (Find It), `WordFeedbackCard` (Blend It) — 3 separate implementations | Say It, Find It, Blend It |
| Mascot instruction bubble | `MascotBubble` — named consistently, but no evidence of a shared composable vs. per-screen copies | Map, Hear It, Say It, Find It, Blend It |
| Heart pool display | `HeartDisplay` — SDD notes Find It and Blend It "reuse" the Say It instance/class (good backend discipline), but visual composable reuse isn't confirmed | Say It, Find It, Blend It, (Map shows hearts icon too) |
| In-lesson progress indicator | `SubLevelProgressBar` (3-step pill) vs. `BlendItProgressIndicator` ("X/5 words") vs. Map's star/heart top bar — three different visual treatments of the same idea | Hear It, Say It, Find It, Blend It, Map |
| Circular audio-trigger button | `PlayButton` (Hear It), `MicrophoneButton` (Say It), `WordAudioButton` (Blend It) — three named composables, likely three separate visual implementations | Hear It, Say It, Blend It |
| Buttons | No `PlayItButton` component referenced anywhere in the SDD — Primary/Success/Secondary styling defined only at the Design System level, never operationalized | All screens |

This is exactly the duplication pattern the Gemini review predicted from the outside ("If you find yourself repeatedly typing `Modifier.padding(16.dp).background(Color.Blue)`, your UI lacks reusable components") — the SDD's own documentation independently confirms it from the inside.

---

## 2. Proposed `core:ui` Package Structure `[orig §4.2]`

```
core/ui/
├── theme/
│   └── PlayItTheme.kt              // wraps MaterialTheme with all tokens (see 01_Foundation.md)
├── tokens/
│   ├── Color.kt                    // 01_Foundation.md §2/2.1
│   ├── Type.kt                     // 01_Foundation.md §2.2
│   ├── Spacing.kt                  // 01_Foundation.md §2.3
│   ├── Shape.kt                    // corner radii
│   ├── Elevation.kt                // 01_Foundation.md §4
│   └── Motion.kt                   // 03_MotionSystem.md
├── components/
│   ├── button/
│   │   └── PlayItButton.kt         // variant: Primary | Secondary | Success
│   ├── card/
│   │   ├── LearningCard.kt
│   │   └── RewardCard.kt
│   ├── feedback/
│   │   └── FeedbackCard.kt         // variant: Correct | Retry — REPLACES 3 implementations
│   ├── mascot/
│   │   ├── MascotBubble.kt         // state: Happy|Excited|Thinking|Encouraging|Celebrating
│   │   └── MascotIdleScaffold.kt   // 10-second idle cue logic — see 03_MotionSystem.md
│   ├── progress/
│   │   ├── SegmentedProgressBar.kt // replaces SubLevelProgressBar + BlendItProgressIndicator
│   │   └── TopStatsBar.kt
│   ├── hearts/
│   │   └── HeartDisplay.kt         // single shared visual, reused everywhere HeartManager is
│   ├── audio/
│   │   └── CircularAudioButton.kt  // base for PlayButton | MicrophoneButton | WordAudioButton
│   ├── map/
│   │   ├── MapNodeBase.kt          // shared base for LetterNode + BlendItChallengeNode — see 05_MapScreen.md
│   │   └── PathConnector.kt
│   ├── star/
│   │   └── StarAnimation.kt
│   └── dialog/
│       └── FullScreenDialog.kt     // per research: no partial modals for children
└── a11y/
    └── ReducedMotionState.kt       // see 03_MotionSystem.md — backs ReducedMotionToggle in Parent Dashboard
```

---

## 3. Consolidation Priorities (highest leverage first) `[orig §4.3]`

| Priority | Component | Why first | Screens touched | Fixes |
|---|---|---|---|---|
| 1 | `FeedbackCard` | Directly fixes the red/orange Design System violation in one place instead of three | Say It, Find It, Blend It | 01_Foundation.md §1.3 #1 |
| 2 | `HeartDisplay` + `HeartManager` copy layer | Directly implements the hearts reframing ruling (§4 below) in one place | Say It, Find It, Blend It, Map | 01_Foundation.md §1.3 #6 |
| 3 | `PlayItButton` | Highest-frequency component in the app; currently zero shared implementation | All 12 screens | General consistency, Gemini review #3 |
| 4 | `MascotBubble` + idle scaffold | Enables new idle-cueing behavior to ship once, everywhere | Map, Hear It, Say It, Find It, Blend It | 01_Foundation.md §1.3 #4 |
| 5 | `MapNodeBase` | Enables the 4-state color system to be enforced structurally | Map | 01_Foundation.md §1.3 #5 |
| 6 | `SegmentedProgressBar` | Consolidates 3 visual treatments into 1 | Hear It, Say It, Find It, Blend It | Consistency |
| 7 | `CircularAudioButton` | Lower urgency — hold-to-record vs. tap-to-play differ functionally, so this is a shared *base*, not full unification | Hear It, Say It, Blend It | Consistency |

---

## 4. Hearts Ruling — resolving 01_Foundation.md §1.3 #6 `[orig §2.16]`

**Ruling: keep the hearts mechanic; reframe it entirely at the presentation layer. Do not remove `HeartManager` or its data model.**

Reasoning: removing a fully-implemented, tested mechanic this close to deployment is high-risk for low benefit. The research's real objection is *punitive framing*, not a bounded-attempts mechanic — and v1.0 already wrote the reconciliation ("Instead of 'You lost a heart,' use 'Let's practice one more time'"), it just isn't applied everywhere hearts appear.

**Required changes (presentation-only):**
1. Every heart-loss moment triggers v1.0's copy substitution — never a bare "−1" or context-free icon loss.
2. Heart icons render in Energy Orange or Correction Orange, **never red**.
3. Recovery (3 consecutive correct → +1 heart) gets equal or greater visual production value than loss.
4. Depletion/reset reads as "Let's try again together!" with mascot in Encouraging state — never "Game Over."
5. `heartsLost` stays a valid internal accuracy metric for Parent Dashboard (see `09_ParentDashboard.md`) but is never surfaced to the child as a loss count.

Document this as a conscious design decision in your capstone defense, not an implicit one.

---

## 5. Error States `[orig §2.17]`

Universal rule, identical across `FeedbackCard`, `PictureCard`, `WordFeedbackCard`: Correction Orange fill + retry-arrow icon + gentle shake (150–250ms) + neutral "boop" sound + mascot Encouraging state + v1.0 copy substitution. No screen shake as punishment, no buzzer, no flashing, no red anywhere. Say It's >40dB noise alert should also use Correction Orange, not red — currently unspecified in the SDD; see `04_AudioSystem.md` and `07_SayIt.md`.

## 6. Empty States `[orig §2.18]`

Never blank/broken. Faded silhouette or mascot-pointing gesture indicates what to do. Profile grid's dashed-circle "Add Profile" placeholder is a correct existing pattern — extend the same language elsewhere.

## 7. Iconography `[orig §2.9]`

- Solid, filled icons only — no outlines in child-facing UI.
- Minimum 24dp icon canvas within its 54dp+ touch target (see `01_Foundation.md` §3).
- Every color-coded state carries a matching icon: checkmark (correct), retry-arrow (incorrect), padlock (locked), waveform (listening).

## 8. Illustration Guidelines (carried forward + reinforced) `[orig §2.10]`

- Rounded, organic shapes; no sharp angles. Saturated color for interactive elements only; muted/pastel surfaces.
- **Anchor-image permanence (hard rule):** once a phoneme's illustration is set (e.g., *mangga* for /m/), the identical asset is reused everywhere — Hear It, Find It distractors, Blend It, Parent Dashboard. One `phonemeId → imagePath` mapping, never overridden per-screen. Audit task: `10_FinalPolish.md` G1.

## 9. Screen Templates `[orig §2.15]`

1. **Focus Template** (Hear It, Say It, Find It): top bar (back + hearts/streak) → hero content zone → bottom mascot bubble + primary CTA. Max 3 interactive elements.
2. **Journey Template** (Map): persistent top stats bar → scrollable winding path → floating mascot bubble. Auto-scroll to current node on entry.
3. **Celebration Template** (Letter Complete, Blend It Complete): full-bleed reward visual → single primary CTA, zero competing actions.

Parent Dashboard and Report Preview are exempt — adult information-density patterns apply (see `09_ParentDashboard.md`).

---

## 10. Phase C — Component Refactoring Tasks

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| C1. Build unified `FeedbackCard` (Correct/Retry variants) | Retire 3 separate implementations | Consistent, predictable feedback everywhere | 01_Foundation.md B1 | Medium-High | Must Have |
| C2. Build unified `HeartDisplay` + copy layer | Retire per-screen heart visuals | Consistent hearts framing enforced structurally | Hearts Ruling above (B2) | Medium | Must Have |
| C3. Build `PlayItButton` (Primary/Secondary/Success) | Retire ad hoc button styling | Predictable CTA location/behavior (Fitts's Law) | 01_Foundation.md A1–A2 | Medium | Must Have |
| C4. Build `MascotBubble` + `MascotIdleScaffold` (10s idle cue) | Ship idle-scaffolding once, everywhere | Non-judgmental help exactly when a child stalls | 01_Foundation.md A1–A2 | Medium | Should Have |
| C5. Build `MapNodeBase` w/ 4-state color system | Enforce status-semantic node coloring | Clear at-a-glance sense of progress on the Map | See 05_MapScreen.md | Medium | Must Have |
| C6. Build `SegmentedProgressBar` | Consolidate 3 progress visual treatments | Consistent "how much is left" signal | 01_Foundation.md A1–A2 | Low-Medium | Should Have |
| C7. Build `CircularAudioButton` shared base | Consistent tap animation/target size across Play/Mic/WordAudio buttons | Predictable audio-control behavior | 01_Foundation.md A1–A2, A3 | Medium | Should Have |
| C8. Add `ReducedMotionToggle` to Parent Dashboard + wire to `ReducedMotionState` | Close accessibility gap | Usable by neurodivergent/motion-sensitive children | 01_Foundation.md A1–A2 | Low-Medium | Must Have — full spec in `03_MotionSystem.md` |

## 11. Phase B tasks owned jointly with Foundation (listed here for component traceability)

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| B1. Replace red feedback with Correction Orange across Say It / Find It / Blend It | Fix highest-priority Design System violation | Emotional safety during every error moment | 01_Foundation.md A1–A2 | Medium | Must Have |
| B2. Reframe hearts per Hearts Ruling (§4 above) | Reconcile growth-mindset research with existing mechanic | Failure feels safe, not punitive | 01_Foundation.md A1–A2 | Medium | Must Have |
