# playIT — 01. Foundation
### Repository Audit + Design Tokens + Touch Targets + Elevation
See `00_ProjectRules.md` for source priority, global constraints, and the full document map. Component-level fixes that consume these tokens live in `02_ComponentLibrary.md`.

---

## 1. Repository Audit Findings

### 1.1 Feature Completion Matrix (SDD v1.0 vs. Scope vs. Roadmap) `[orig §1.1]`

| Feature Area | SRS/Scope Says | SDD Detailed Design Covers It? | Status |
|---|---|---|---|
| MVVM + Clean Architecture (3-layer) | Yes | Yes — Presentation/Domain/Data fully diagrammed | ✅ Complete |
| Room/SQLite offline persistence | Yes | Yes — full ERD, 12 entities, prepopulated DB | ✅ Complete |
| Vosk offline speech recognition | Yes | Yes — `VoskRecognizer`, `AudioCapture`, `SpeechValidator` at 75% confidence | ✅ Complete |
| Jetpack Compose UI, "all 10 screens" | Scope §1.2 says 10 screens | Screen Inventory table lists 12 screens (Splash, ProfileSelect, NamePrompt, Map, HearIt, SayIt, FindIt, LetterComplete, BlendIt, BlendItComplete, ParentDashboard, ReportPreview) | ⚠️ Documentation inconsistency — see `00_ProjectRules.md` §0.7 |
| Gamification engine (hearts, stars, streaks, badges) | Yes | Yes — `HeartManager`, `StarCalculator`, `StreakTracker`, `Achievement` entity | ✅ Complete, design-conflict resolved in `02_ComponentLibrary.md` (Hearts Ruling) |
| Candy Crush-style winding map, 28 letter nodes | Yes | Yes — `MapScreen`, `LetterNode`, winding path mockup shown | ✅ Complete |
| Letter grouping, Word Challenge per group | Added v0.2 per adviser | Yes — `LetterGroup`, `LetterGroupMember`, `GroupUnlockManager`, Blend It gated on group completion | ✅ Complete |
| Parent dashboard + PDF report | Yes | Yes — math-gate protected, `ReportGenerator`, `PdfExporter` (native `PdfDocument`, no extra library) | ✅ Complete |
| Multi-profile support, up to 6 profiles | Yes | Yes — `Profile` entity, `SessionManager` singleton scopes every write to `activeProfileId` | ✅ Complete |
| Word Challenge unlocked after full letter group | Yes | Yes — verified via `verifyGroupUnlock` in Blend It sequence diagram | ✅ Complete |
| Game Module decoupling (v1.0 change) | v1.0 changelog: "Absorbed by (sayit, findit, blendit)" | Confirmed — no monolithic Game Module remains | ✅ Complete, good refactor |
| Design System enforcement | Implied by "polish" goal | Not evidenced anywhere in the SDD. No `Color.kt`/`Type.kt`/`Spacing.kt` token references in any component table. | ❌ Missing — the actual gap, not the architecture |

**Verdict:** Every functional requirement traceable through the SRS → SDD chain is implemented. The project is **untokenized**, not unfinished — a systemization problem, not a build-more-features problem.

### 1.2 Architectural Deviations `[orig §1.2]`

| Deviation | Detail | Risk |
|---|---|---|
| Blend It interaction model | UX research specifies phoneme-level blending (dragging /m/ + /a/ to fuse into "ma," Endless Alphabet style). SDD's Module 5 is a whole-word spelling game — tap letter tiles into slots, static tile bank with distractors. | Medium — pedagogical drift, adviser-approved but never reconciled with the research. **Recommended: keep the implementation, add a lightweight blending cue** — see `10_FinalPolish.md` task E4. |
| Interaction mechanic: tap vs. drag | Research assumes drag-and-drop for blending. SDD's `LetterTile`/`LetterSlot` is tap-to-place, tap-to-remove. | Low — arguably a positive deviation (more forgiving for 6-year-old motor skills than drag-release precision). Document as intentional accessibility improvement. |
| Progress visibility | v1.0 principle: "Progress visibility at all times." Research specifies a persistent, top-anchored, 16dp linear progress bar. SDD's actual persistent UI is the 3-step `SubLevelProgressBar` plus a star/heart counter — no map-level or app-level always-visible bar. | Low-Medium — within-lesson progress is covered; cross-lesson progress is not. See `05_MapScreen.md` task D3. |

### 1.3 UI Inconsistencies (Design System v1.0 vs. SDD-documented implementation) `[orig §1.3]`

Highest-value finding in the audit.

| # | Design System v1.0 Rule | SDD / Mockup Evidence | Contradiction | Resolved In |
|---|---|---|---|---|
| 1 | *"Error Colors — Avoid harsh red... Never use: Large red X, Flashing red screens, Punishment visuals."* Use Gentle Correction Orange `#FFB74D` instead. | `FeedbackCard` (Say It): "Green or red binary result card." `PictureCard` (Find It): "green or red flash feedback"; "flash red, disable image." `WordFeedbackCard` (Blend It): "Green/red result card." | Direct violation, repeated across 3 of 5 core modules. Single highest-priority visual fix in the whole plan. | `02_ComponentLibrary.md` §Unified FeedbackCard |
| 2 | Touch target minimum: 48dp; Recommended 56dp; Important Actions 64dp+. | Gemini repo review recommends the same 48dp floor. UX research (CCI literature) recommends 54–64dp minimum, 16dp dead space between adjacent targets. | Two of four source docs undershoot the evidence-based floor the research doc establishes. | §3 below (this file) |
| 3 | Elevation: "Soft elevation" shadow on buttons; Learning Card "Elevation: 4dp." | Research: *"Tonal Elevation over Drop Shadows... For children, complex shadows can create visual noise... M3's tone-based surface colors... creates a flatter, cleaner interface."* | Stylistic conflict — specified shadow model is the less research-backed of two options in the team's own documents. | §4 below (this file) |
| 4 | No mascot idle/scaffolding behavior explicitly required beyond emotional-state list. | Research: idle >10 seconds should trigger mascot "gaze cueing." SDD's `MascotBubble` doesn't document idle-timeout behavior. | Gap, not contradiction. | `03_MotionSystem.md` §Mascot Idle Scaffolding |
| 5 | Map node styling implied to communicate status via color. | Map mockup shows each letter node in a distinct, seemingly arbitrary color rather than a consistent unlocked/current/completed/locked mapping. | Partial contradiction — colors read as decorative rather than semantic. | `05_MapScreen.md` |
| 6 | Hearts: v1.0 defines heart-loss/recovery sounds but never defines a heart visual component. | SDD fully implements `HeartDisplay`, `HeartManager`, 5-heart pools, reset-to-3-on-depletion. UX research recommends against a lives system entirely. | Three-way disagreement needing deliberate resolution. | `02_ComponentLibrary.md` §Hearts Ruling |

### 1.4 Technical Debt (synthesized from Gemini repository review + SDD cross-check) `[orig §1.4]`

| Item | Source | Notes |
|---|---|---|
| Vosk models + audio assets bundled directly in `/assets`, inflating APK size (`am/final.mdl`, `blend_am.mp3`, `word_mac.mp3`, etc.) | Gemini review, Part 1 | Recommend Play Asset Delivery **install-time** module only — see `00_ProjectRules.md` §0.5 and `10_FinalPolish.md` task I3. |
| Risk of bloated ViewModels holding Compose/Context references | Gemini review, Part 1 | Verification task, not a known defect — see `10_FinalPolish.md` task I4. |
| No enforced Design System / token usage | Gemini review, Part 2 & 5 | Confirmed independently by §1.1 above — zero token references anywhere in the SDD's component tables. Resolved by §2 below. |
| Composables possibly exceeding 100 lines | Gemini review, Part 4 | Flagged as code-review checklist item — see `10_FinalPolish.md` task I4. |
| Hardcoded `dp` values likely inconsistent across tablet/phone sizes used in Philippine public schools | Gemini review, Part 2 | Real deployment risk; resolved by the token system in §2 below. |

### 1.5 Refactoring Opportunities That Do Not Touch Working Code `[orig §1.5]`

- Introduce a `core:ui` token + component package (see `02_ComponentLibrary.md`) — purely additive.
- Swap hardcoded color/shape values for token references screen-by-screen, independently, in any order.
- Consolidate the three independently-implemented feedback cards into one component — see `02_ComponentLibrary.md`.
- No schema changes, no new Room entities, no changes to `SessionManager`, `UnlockManager`, `GroupUnlockManager`, or the Vosk pipeline are required anywhere.

---

## 2. Design Tokens (v2.0) `[orig §2.3]`

All hardcoded values across the codebase should resolve to these tokens. Token names use dot notation for direct mapping to Compose `object` structures (`Color.kt`, `Type.kt`, `Spacing.kt`, `Shape.kt`, `Motion.kt`, `Elevation.kt`).

```kotlin
// Illustrative token structure -- not literal file contents
object PlayItColor {
    val learningBlue = Color(0xFF4A90E2)      // primary.action
    val learningBlueDeep = Color(0xFF3A7BC8)  // primary.text -- see contrast note below
    val growthGreen = Color(0xFF4CAF50)       // success.fill
    val achievementGold = Color(0xFFFFC107)   // reward.fill
    val energyOrange = Color(0xFFFF9800)      // encouragement.accent
    val friendlyPurple = Color(0xFF8E7DF2)    // challenge.fill (Blend It)
    val softSky = Color(0xFFEAF6FF)           // surface.background
    val creamWhite = Color(0xFFFFFDF8)        // surface.card
    val correctionOrange = Color(0xFFFFB74D)  // error.fill -- REPLACES red everywhere
    val textPrimary = Color(0xFF2D3748)
    val textSecondary = Color(0xFF718096)
    val border = Color(0xFFE2E8F0)
    val disabled = Color(0xFFCBD5E0)
}
```

### 2.1 Color System `[orig §2.4]`

**Carried forward from v1.0 unchanged:** Learning Blue, Growth Green, Achievement Gold, Energy Orange, Friendly Purple, Soft Sky, Cream White, Correction Orange, full Neutral palette.

**Resolved conflicts:**

- **Error color enforcement (resolves 1.3 #1 — highest priority):** `color.error.fill` = Correction Orange `#FFB74D` is now the *only* permitted fill for incorrect states across `FeedbackCard`, `PictureCard`, and `WordFeedbackCard`. Red is removed from the palette entirely. Pair every error state with a non-color cue (retry-arrow icon, gentle shake, "boop" sound) to satisfy color-independence at the same time. Full component spec in `02_ComponentLibrary.md`.
- **Contrast verification (new):** `learningBlueDeep #3A7BC8` is added as a text-safe variant of Learning Blue. White text at 18–20sp Medium weight on base `#4A90E2` should be measured against the 4.5:1 AA threshold; if it falls short, buttons render text in `learningBlueDeep` territory rather than accepting marginal contrast. Full audit task in `09_ParentDashboard.md` (H1).
- **Map node semantics (resolves 1.3 #5):** Node fill color is no longer decorative-per-letter. Strict 4-state system — full spec in `05_MapScreen.md`:
  - Locked → `disabled` (monochromatic gray) + padlock icon
  - Unlocked/upcoming → `learningBlue`, static
  - Current/active → `achievementGold`, pulsing (largest node)
  - Completed → `growthGreen`, solid, star-count badge

  A per-letter rainbow treatment can still exist *inside* the node (glyph/anchor illustration) — but the node's status ring/fill must always communicate progress state first.

### 2.2 Typography `[orig §2.5]`

| Tier | v1.0 | v2.0 | Weight | Used For | Rationale |
|---|---|---|---|---|---|
| Display XL | 40sp | **48sp** | ExtraBold | Letter cards, celebration screens | Research: children's text should be "vastly oversized" vs. adult apps |
| Heading | 28sp | **32sp** | Bold | Screen titles | Same rationale, moderated from research's 40sp for practicality on phones |
| Subheading | 22sp | **24sp** | SemiBold | Instructions | — |
| Body Large *(new tier)* | — | **20sp** | Medium | Mascot messages, primary in-lesson text | Matches research's `BodyLarge`; floor for anything a child reads |
| Body | 18sp | 18sp *(unchanged)* | Medium | Parent Dashboard body only | Adult-facing screens keep v1.0 sizing |
| Caption | 16sp | 16sp *(restricted)* | Regular | Helper text — Parent Dashboard only | Banned from child-facing screens; 20sp is the child-facing floor |

**Font family: no change.** Nunito/Poppins retained over the research doc's Google Sans Flex/Quicksand suggestion — both are rounded, child-friendly, single-story sans-serifs; switching this late adds licensing/re-testing risk for no real gain.

### 2.3 Spacing System (carried forward, unchanged) `[orig §2.6]`

Base unit 8dp; scale 4/8/16/24/32/48/64dp exactly as v1.0. Already correct and evidence-aligned.

---

## 3. Touch Target Rules (resolves 1.3 #2) `[orig §2.7]`

| Tier | v1.0 / Gemini review | v2.0 (evidence-based) |
|---|---|---|
| Minimum | 48dp | **54dp** |
| Recommended | 56dp | 56dp *(unchanged)* |
| Important actions | 64dp+ | 64–72dp *(unchanged; matches SDD's 72dp mic button)* |
| Min spacing between targets | Not specified | **16dp**, per CCI literature |

---

## 4. Elevation & Surface System (resolves 1.3 #3) `[orig §2.8]`

v1.0's drop-shadow model is replaced with tonal elevation, with one calibrated exception:

| Level | Old | New | Used For |
|---|---|---|---|
| 0 | Flat | Flat | Screen background |
| 1 | 4dp shadow | Tonal tint (+4%), no hard shadow | Learning/Picture Cards |
| 2 | Not specified | Tonal tint (+8%) | Modals |
| 3 | "Soft elevation" | 2dp soft shadow **retained only on Primary CTA** | Fitts's-Law: the one place a shadow earns its cognitive cost |
| Reward | Shadow | Tonal + soft glow, no hard shadow | Reward Card, star burst |

---

## 5. Accessibility Standards (consolidated, resolves 1.3 #2 + gaps) `[orig §2.13]`

| Rule | Standard | Status |
|---|---|---|
| Touch targets | 54dp min, 16dp spacing | Needs raise from 48dp |
| Text contrast | 4.5:1 body, 7:1 Parent Dashboard | Needs verification, esp. Learning Blue on white — see `09_ParentDashboard.md` |
| Color independence | Icon/shape backup for every color state | Partially present; enforced via `02_ComponentLibrary.md` |
| Reading support | Text + audio always | Already correct in SDD |
| Motion safety | Reduced-motion toggle | Gap — see `03_MotionSystem.md` |
| Cognitive load | ≤3 actions/screen, 1 CTA per completion screen | Apply as literal checklist item on every screen file |

---

## 6. Phase A — Foundation Tasks (execution roadmap)

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| A1. Build token files (`Color.kt`, `Type.kt`, `Spacing.kt`, `Shape.kt`, `Elevation.kt`, `Motion.kt`) | Single source of truth for every visual value | Indirect — enables every downstream fix | None | Low | Must Have |
| A2. Wrap app in `PlayItTheme` / `MaterialTheme`, remove hardcoded colors/fonts | Eliminate the duplication the Gemini review flagged | Consistent look across all screens on first load | A1 | Medium | Must Have |
| A3. Raise global minimum touch target 48dp → 54dp | Meet CCI-backed motor-skill floor | Fewer mis-taps for 5–7 year olds | A1 | Low | Must Have |
| A4. Correct SDD scope statement ("10 screens" → 12) | Documentation hygiene for capstone defense | N/A (academic correctness) | None | Low | Should Have |

**Downstream screens that consume Foundation tokens with no structural change (token-application only):** SplashScreen, ProfileSelectScreen, NamePromptScreen. See task D4 in `05_MapScreen.md` for their consolidated task entry.

- **SplashScreen** — Correctly offline-only per SDD; mascot-centered branding matches emotional-design intent. No functional weaknesses. Educational impact: Low. Effort: Low. Apply tokens only.
- **ProfileSelectScreen** — Grid of large circular avatars (64dp+) already exceeds the 54dp floor; dashed-circle "Add Profile" empty-state pattern is correct (see `02_ComponentLibrary.md` §Empty States). SDD doesn't specify spacing value between profile cards beyond "minimum 16dp" — verify against the token. Educational impact: Medium (mis-taps cascade into wrong child's progress being recorded). Effort: Low.
- **NamePromptScreen** — Simple, single-focus flow, matches Focus Template. Text input for name entry is inherently reading/writing-dependent for a pre-literate user — a structural tension the SDD doesn't address; fine if parent-assisted, but the assumption should be explicit. Educational impact: Low (one-time, likely parent-assisted). Effort: Low. Recommendation: add a small "Ask a grown-up to help type your name" mascot line — a 1-line copy change.
