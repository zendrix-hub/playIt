# playIT Master Design & Engineering Blueprint v2.0
### BSIT Capstone Project — IT332-56 | Cebu Institute of Technology University
**Prepared as:** Repository Audit → Design System v2.0 → Component Library Spec → UI/UX Roadmap → Screen-by-Screen Plan → Technical Refactoring Recommendations → Deployment Readiness Assessment

**Source priority applied throughout this document (per project instructions):**
1. Repository implementation (SDD v1.0, as the documented source of truth for what is built)
2. UX/Educational Research (`playIT Design Bible and UX Research Report`)
3. Software Design Description (SRS-derived architecture)
4. Existing Design System v1.0

Where these four sources conflict, this document states the conflict explicitly, explains the reasoning, and gives one resolved recommendation — it does not silently pick a side.

---

## Executive Summary

playIT's architecture is genuinely strong: Clean Architecture + MVVM, a fully offline Room schema, Vosk-based on-device speech recognition, and a profile-scoped data model are all appropriate, well-documented, and — per the repository review — implemented to a "Functional Prototype (Alpha)" standard with **High** architectural quality. The gap is entirely in the presentation layer: no enforced design system, ad-hoc styling, and several places where the actual implementation (as documented in the SDD) contradicts the project's own Design System v1.0 and the educational-psychology research the team already produced.

The good news for a capstone timeline: **every recommendation in this document is presentation-layer only.** Nothing here asks you to touch `PlayItDatabase`, the Vosk integration, repository interfaces, or your ERD. You built the hard, invisible 80% correctly. This blueprint is about making the visible 20% match the quality of the engineering underneath it.

Four findings drive most of what follows:

1. **Your own Design System v1.0 explicitly bans harsh red for incorrect answers** ("Never use: Large red X, Flashing red screens, Punishment visuals"), but the SDD describes `FeedbackCard`, `PictureCard`, and feedback flows in Say It / Find It as **"green or red"** — a direct contradiction between your design rules and your documented implementation.
2. **Your UX research explicitly recommends against lives/hearts systems** ("Heart/Life Indicators: Avoid... Use additive progression instead"), yet hearts are a core, heavily-implemented mechanic across Say It, Find It, and Blend It (`HeartManager`, `HeartDisplay`, deduction/recovery logic, reset-to-3-on-depletion). This is a real pedagogical tension worth a deliberate decision, not a silent one.
3. **Touch target minimums disagree across your own documents**: Design System v1.0 and the Gemini repo review both say 48dp; your own UX research cites Child-Computer Interaction (CCI) studies recommending 54–64dp minimums for ages 5–7. Right now the lower number is what's likely implemented.
4. **Elevation strategy disagrees**: Design System v1.0 specifies drop shadows ("Soft elevation," "Elevation: 4dp"); your UX research explicitly recommends Material 3 *tonal* elevation over drop shadows for children, citing reduced visual noise.

None of these require new features. All four are token-level or component-level fixes. Section 1 below documents the full audit; Section 2 resolves every conflict into a single v2.0 design system; Sections 3–7 turn that resolution into an executable, priority-ordered plan.

---

## Phase 1 — Repository Audit

### 1.1 Feature Completion Matrix (SDD v1.0 vs. Scope vs. Roadmap)

| Feature Area | SRS/Scope Says | SDD Detailed Design Covers It? | Status |
|---|---|---|---|
| MVVM + Clean Architecture (3-layer) | Yes | Yes — Presentation/Domain/Data fully diagrammed | ✅ Complete |
| Room/SQLite offline persistence | Yes | Yes — full ERD, 12 entities, prepopulated DB | ✅ Complete |
| Vosk offline speech recognition | Yes | Yes — `VoskRecognizer`, `AudioCapture`, `SpeechValidator` at 75% confidence | ✅ Complete |
| Jetpack Compose UI, "all 10 screens" | Scope §1.2 says **10 screens** | Screen Inventory table lists **12 screens** (Splash, ProfileSelect, NamePrompt, Map, HearIt, SayIt, FindIt, LetterComplete, BlendIt, BlendItComplete, ParentDashboard, ReportPreview) | ⚠️ **Documentation inconsistency** — scope statement is stale relative to the actual screen inventory |
| Gamification engine (hearts, stars, streaks, badges) | Yes | Yes — `HeartManager`, `StarCalculator`, `StreakTracker`, `Achievement` entity | ✅ Complete, but see §1.3 for design-conflict |
| Candy Crush-style winding map, 28 letter nodes | Yes | Yes — `MapScreen`, `LetterNode`, winding path mockup shown | ✅ Complete |
| Letter grouping, Word Challenge per group | Added v0.2 per adviser | Yes — `LetterGroup`, `LetterGroupMember`, `GroupUnlockManager`, Blend It gated on group completion | ✅ Complete |
| Parent dashboard + PDF report | Yes | Yes — math-gate protected, `ReportGenerator`, `PdfExporter` (native `PdfDocument`, no extra library) | ✅ Complete |
| Multi-profile support, up to 6 profiles | Yes | Yes — `Profile` entity, `SessionManager` singleton scopes every write to `activeProfileId` | ✅ Complete |
| Word Challenge unlocked after full letter group | Yes | Yes — verified via `verifyGroupUnlock` in Blend It sequence diagram | ✅ Complete |
| Game Module decoupling (v1.0 change) | v1.0 changelog: "Absorbed by (sayit, findit, blendit)" | Confirmed — no monolithic Game Module remains; each sub-skill has its own screen/ViewModel | ✅ Complete, and a good refactor |
| Design System enforcement | Implied by "polish" goal | **Not evidenced anywhere in the SDD.** No `Color.kt`/`Type.kt`/`Spacing.kt` token references in any component table. | ❌ **Missing — this is the actual gap**, not the architecture |

**Verdict:** Every functional requirement traceable through the SRS → SDD chain is implemented. The project is not "unfinished" in the engineering sense the Gemini review used that word for — it is **untokenized**. That reframing matters for how you triage the remaining work: this is a systemization problem, not a build-more-features problem.

### 1.2 Architectural Deviations

| Deviation | Detail | Risk |
|---|---|---|
| Blend It interaction model | UX research (Marungko literacy section) specifies phoneme-level blending: dragging /m/ + /a/ into proximity to fuse into the syllable "ma," modeled on Endless Alphabet's magnetic drag-and-drop. The SDD's actual Module 5 is a **whole-word spelling game** — tap letter tiles into slots to spell "CAT," with a static tile bank including distractors. | **Medium — pedagogical drift.** This isn't a bug, but it is a scope change that happened informally (adviser-approved per changelog) and was never reconciled with the original phono-syllabic blending pedagogy your own research doc builds its case on. Worth a conscious decision: keep Blend It as whole-word retrieval practice (valid on its own terms — it exercises spelling/decoding) or restore a syllable-fusion moment earlier in the flow (e.g., inside Hear It or a new micro-step) so the "immediate blending" principle from Marungko isn't lost entirely. Recommended: **keep the implementation, add a lightweight blending cue** (see §5, Lesson Screens phase) rather than rebuild the module. |
| Interaction mechanic: tap vs. drag | Both the UX research and (implicitly) Design System v1.0 assume drag-and-drop for blending contexts. SDD's `LetterTile`/`LetterSlot` design is tap-to-place, tap-to-remove. | **Low.** Tap-to-place is actually *more* forgiving for 6-year-old motor skills than drag-and-drop (no drag-release precision required), so this is arguably a **positive** deviation from the research recommendation. Document it as an intentional accessibility improvement rather than a defect. |
| Progress visibility | Design System v1.0 principle: "Progress visibility at all times." UX research component library specifies a persistent, top-anchored, 16dp-thick linear progress bar. SDD's actual persistent progress UI is the 3-step `SubLevelProgressBar` (Hear/Say/Find pills) plus a star/heart counter in the top bar — there is no map-level or app-level always-visible linear bar. | **Low-Medium.** Within-lesson progress is covered; cross-lesson "how much of the whole map is left" is not surfaced as a persistent bar, only via the Map screen itself. |

### 1.3 UI Inconsistencies (Design System v1.0 vs. SDD-documented implementation)

This is the highest-value finding in the audit — these are concrete, fixable contradictions between rules the team already wrote down and what the SDD says was built.

| # | Design System v1.0 Rule | SDD / Mockup Evidence | Contradiction |
|---|---|---|---|
| 1 | *"Error Colors — Avoid harsh red... Never use: Large red X, Flashing red screens, Punishment visuals."* Use Gentle Correction Orange `#FFB74D` instead. | Module 3 (Say It): `FeedbackCard` — *"Green or red binary result card."* Module 4 (Find It): `PictureCard` — *"green or red flash feedback"*; sequence diagram — *"flash red, disable image."* Module 5 (Blend It): `WordFeedbackCard` — *"Green/red result card."* | **Direct violation, repeated across 3 of 5 core modules.** This is the single highest-priority visual fix in the whole audit. |
| 2 | Touch target minimum: 48dp (Design System v1.0), Recommended 56dp, Important Actions 64dp+. | Gemini repo review recommends the same 48dp floor. UX research (Section 8, citing CCI literature) recommends **54–64dp minimum**, not 48dp, with 16dp dead space between adjacent targets. | Two of your four source documents (Design System v1.0, Gemini review) undershoot the evidence-based floor your own research doc establishes. |
| 3 | Elevation: "Soft elevation" shadow on buttons; Learning Card "Elevation: 4dp." | UX research Section 5: *"Tonal Elevation over Drop Shadows... For children, complex shadows can create visual noise... M3's tone-based surface colors... creates a flatter, cleaner interface."* | Stylistic conflict — the currently-specified shadow-based elevation is the *less* research-backed of the two options already in your own documents. |
| 4 | No mascot idle/scaffolding behavior explicitly required beyond emotional-state list. | UX research Section 7: idle >10 seconds should trigger mascot "gaze cueing" toward the correct answer/next action. SDD component tables (`MascotBubble`) don't document an idle-timeout behavior. | Gap, not a contradiction — the research recommends a specific behavior the SDD never operationalized. |
| 5 | Map node styling implied to communicate status via color (Learning Blue = active, Achievement Gold = completed/reward, Disabled gray = locked). | Map screen mockup shows each letter node in a **distinct, seemingly arbitrary color** (P blue, N locked-gray, A green, S red-orange, M red, B green, E orange) rather than a consistent unlocked/current/completed/locked color mapping. | Partial contradiction — colors appear decorative (Duolingo-style per-letter branding) rather than semantic/status-driven, which works against the "Color Independence" and "one primary goal" accessibility principles both design docs call for. |
| 6 | Hearts: v1.0 defines heart-loss/recovery **sounds** ("Gentle whoosh," "Bright sparkle") but never defines a heart **visual component** in its own Component Library section. | SDD fully implements `HeartDisplay`, `HeartManager`, 5-heart pools, reset-to-3-on-depletion, and shows hearts in the Map, Say It, and Find It top bars. UX research explicitly recommends **against** a lives system. | Three-way disagreement: v1.0 is silent on the *visual*, research says avoid the *mechanic* entirely, SDD implements it fully. This needs a deliberate resolution — see §2.16. |

### 1.4 Technical Debt (synthesized from the Gemini repository review + SDD cross-check)

| Item | Source | Notes |
|---|---|---|
| Vosk models + audio assets bundled directly in `/assets`, inflating APK size (`am/final.mdl`, `blend_am.mp3`, `word_mac.mp3`, etc.) | Gemini review, Part 1 | Recommend Play Asset Delivery **install-time** module only (not on-demand/fast-follow) to preserve the offline-first guarantee required for rural deployment in Baleno. On-demand delivery would silently break offline-first the first time a child opens the app without connectivity. |
| Risk of bloated ViewModels holding Compose/Context references | Gemini review, Part 1 | Not confirmable from the SDD alone (SDD only documents intended contracts, not actual code), but worth a targeted audit — the SDD's ViewModel responsibility tables are clean on paper, so this is a verification task, not a known defect. |
| No enforced Design System / token usage | Gemini review, Part 2 & 5 | Confirmed independently by §1.1 above — zero token references anywhere in the SDD's component tables. |
| Composables possibly exceeding 100 lines | Gemini review, Part 4 | Cannot verify without source; flagged as a code-review checklist item in §5 (Final QA). |
| Hardcoded `dp` values likely inconsistent across tablet/phone sizes used in Philippine public schools | Gemini review, Part 2 | Confirmed as a real deployment risk given the SDD explicitly targets shared/rural devices; resolved by the token system in §2.3. |

### 1.5 Refactoring Opportunities That Do Not Touch Working Code

These are presentation-layer-only changes that preserve every ViewModel, Repository, DAO, and Vosk integration exactly as documented:

- Introduce a `core:ui` token + component package (Section 4) — purely additive, no existing screen logic changes required to *start*.
- Swap hardcoded color/shape values for token references screen-by-screen, independently, in any order — this is why the roadmap in Section 5 can be sequenced by UX impact rather than by technical dependency.
- Consolidate the three independently-implemented feedback cards (`FeedbackCard`, `PictureCard` flash, `WordFeedbackCard`) into one component — this is where the red/orange inconsistency actually gets fixed, in one place, instead of three.
- No schema changes, no new Room entities, no changes to `SessionManager`, `UnlockManager`, `GroupUnlockManager`, or the Vosk pipeline are required anywhere in this blueprint.


---

## Phase 2 — Design System v2.0

Every conflict identified in Section 1 is resolved below. Where v1.0 was already correct, it is carried forward unchanged — this is a resolution and extension of v1.0, not a replacement.

### 2.1 Design Philosophy (carried forward, unchanged)

playIT's core principle stands as written: *"Every interaction should make a child feel successful, capable, and motivated to continue learning."* The seven priorities in v1.0 (clarity before decoration, encouragement before correction, audio before reading, progress visibility, consistency, large touch targets, cultural familiarity) remain the governing philosophy of v2.0. Nothing in this revision changes *why* — only *how precisely* the tokens and components deliver on it.

### 2.2 Educational UX Principles (grounding, from the research report)

Four research findings should be treated as **non-negotiable constraints** on every future screen, because they map directly to measurable working-memory and motivation effects in 5–7 year olds, not aesthetic preference:

1. **≤3 interactable elements per screen.** Working memory in this age range holds 2–3 chunks. Any screen with more than 3 simultaneous tap targets (excluding passive/decorative elements) is a cognitive-load bug, not a design choice.
2. **Extraneous load → zero.** Every pixel that isn't the lesson itself (navigation chrome, decorative background motion, redundant icons) is competing directly with phonics comprehension for the same limited cognitive budget.
3. **Growth mindset over performance framing.** No letter grades, no "Wrong!," no visible penalty language. Failure states must read as *"try again,"* never as *"you lost."*
4. **Dual-coding + spatial contiguity.** Every phoneme/word must pair sound + image + text in tight physical proximity (CTML). This is already respected structurally in Blend It Complete ("word and image grouped tightly") — hold this standard everywhere a new phoneme or word is introduced.

### 2.3 Design Tokens (v2.0)

All hardcoded values across the codebase should resolve to these tokens. Token names use dot notation for direct mapping to Compose `object` structures (`Color.kt`, `Type.kt`, `Spacing.kt`, `Shape.kt`, `Motion.kt`, `Elevation.kt`).

```kotlin
// Illustrative token structure -- not literal file contents
object PlayItColor {
    val learningBlue = Color(0xFF4A90E2)      // primary.action
    val learningBlueDeep = Color(0xFF3A7BC8)  // primary.text -- see 2.4 contrast note
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

### 2.4 Color System

**Carried forward from v1.0 unchanged:** Learning Blue, Growth Green, Achievement Gold, Energy Orange, Friendly Purple, Soft Sky, Cream White, Correction Orange, full Neutral palette.

**Resolved conflicts:**

- **Error color enforcement (resolves 1.3 #1 — highest priority):** `color.error.fill` = Correction Orange `#FFB74D` is now the *only* permitted fill for incorrect states across `FeedbackCard`, `PictureCard`, and `WordFeedbackCard`. Red is removed from the palette entirely. Pair every error state with a non-color cue (retry-arrow icon, gentle shake, "boop" sound) to satisfy color-independence at the same time.
- **Contrast verification (new):** `learningBlueDeep #3A7BC8` is added as a text-safe variant of Learning Blue. White text at 18–20sp Medium weight on base `#4A90E2` should be measured against the 4.5:1 AA threshold; if it falls short, buttons render text in `learningBlueDeep` territory rather than accepting marginal contrast on a product built for emergent readers.
- **Map node semantics (resolves 1.3 #5):** Node fill color is no longer decorative-per-letter. Strict 4-state system:
  - Locked → `disabled` (monochromatic gray) + padlock icon
  - Unlocked/upcoming → `learningBlue`, static
  - Current/active → `achievementGold`, pulsing (largest node)
  - Completed → `growthGreen`, solid, star-count badge

  A per-letter rainbow treatment can still exist *inside* the node (the glyph or anchor illustration) — but the node's status ring/fill must always communicate progress state first.

### 2.5 Typography

| Tier | v1.0 | v2.0 | Weight | Used For | Rationale |
|---|---|---|---|---|---|
| Display XL | 40sp | **48sp** | ExtraBold | Letter cards, celebration screens | Research: children's text should be "vastly oversized" vs. adult apps |
| Heading | 28sp | **32sp** | Bold | Screen titles | Same rationale, moderated from research's 40sp for practicality on phones |
| Subheading | 22sp | **24sp** | SemiBold | Instructions | — |
| **Body Large** *(new tier)* | — | **20sp** | Medium | Mascot messages, primary in-lesson text | Matches research's `BodyLarge`; floor for anything a child reads |
| Body | 18sp | 18sp *(unchanged)* | Medium | Parent Dashboard body only | Adult-facing screens keep v1.0 sizing |
| Caption | 16sp | 16sp *(restricted)* | Regular | Helper text — **Parent Dashboard only** | Banned from child-facing screens; 20sp is the child-facing floor |

**Font family: no change.** Nunito/Poppins retained over the research doc's Google Sans Flex/Quicksand suggestion — both are rounded, child-friendly, single-story sans-serifs; switching this late adds licensing/re-testing risk for no real gain.

### 2.6 Spacing System (carried forward, unchanged)

Base unit 8dp; scale 4/8/16/24/32/48/64dp exactly as v1.0. Already correct and evidence-aligned.

### 2.7 Touch Target Rules (resolves 1.3 #2)

| Tier | v1.0 / Gemini review | v2.0 (evidence-based) |
|---|---|---|
| Minimum | 48dp | **54dp** |
| Recommended | 56dp | 56dp *(unchanged)* |
| Important actions | 64dp+ | 64–72dp *(unchanged; matches SDD's 72dp mic button)* |
| Min spacing between targets | Not specified | **16dp**, per CCI literature |

### 2.8 Elevation & Surface System (resolves 1.3 #3)

v1.0's drop-shadow model is replaced with tonal elevation, with one calibrated exception:

| Level | Old | New | Used For |
|---|---|---|---|
| 0 | Flat | Flat | Screen background |
| 1 | 4dp shadow | Tonal tint (+4%), no hard shadow | Learning/Picture Cards |
| 2 | Not specified | Tonal tint (+8%) | Modals |
| 3 | "Soft elevation" | 2dp soft shadow **retained only on Primary CTA** | Fitts's-Law: the one place a shadow earns its cognitive cost |
| Reward | Shadow | Tonal + soft glow, no hard shadow | Reward Card, star burst |

### 2.9 Iconography *(new — not in v1.0)*

- Solid, filled icons only — no outlines in child-facing UI.
- Minimum 24dp icon canvas within its 54dp+ touch target.
- Every color-coded state carries a matching icon: checkmark (correct), retry-arrow (incorrect), padlock (locked), waveform (listening).

### 2.10 Illustration Guidelines (carried forward + reinforced)

- Rounded, organic shapes; no sharp angles. Saturated color for interactive elements only; muted/pastel surfaces.
- **Anchor-image permanence (hard rule):** once a phoneme's illustration is set (e.g., *mangga* for /m/), the identical asset is reused everywhere — Hear It, Find It distractors, Blend It, Parent Dashboard. One `phonemeId → imagePath` mapping, never overridden per-screen.

### 2.11 Mascot Behavior System (extends v1.0, resolves 1.3 #4)

Retains v1.0 fully: friendly-teacher personality, five emotional states, text+audio always paired, exact feedback-language substitutions.

**New — idle scaffolding:** After 10 seconds of no interaction, `MascotBubble` shifts to Thinking and gaze/points toward the correct next action. Never framed as a penalty or hint-counter increase.

### 2.12 Motion Language & Animation Standards

Durations unchanged (Micro 150–250ms, Standard 300–500ms, Celebration 600–1200ms). Spring physics: `dampingRatio = MediumBouncy, stiffness = Low`.

**New rules:** background decorative loops disabled during active tasks (Hear/Say/Find/Blend It), permitted only on Map/Celebration. Object motion travels straight, predictable, slow paths. Reduced-motion mode replaces particles → fades, pulsing loops → static soft fade-in, parallax → static layers — enforced via a real toggle, not just intention (see `ReducedMotionToggle`, Section 4).

### 2.13 Accessibility Standards (consolidated, resolves 1.3 #2 + gaps)

| Rule | Standard | Status |
|---|---|---|
| Touch targets | 54dp min, 16dp spacing | Needs raise from 48dp |
| Text contrast | 4.5:1 body, **7:1 Parent Dashboard** | Needs verification, esp. Learning Blue on white |
| Color independence | Icon/shape backup for every color state | Partially present; enforce via 2.9 |
| Reading support | Text + audio always | Already correct in SDD |
| Motion safety | Reduced-motion toggle | **Gap** — no such composable in SDD; add it |
| Cognitive load | ≤3 actions/screen, 1 CTA per completion screen | Apply as literal checklist item in Phase 3 |

### 2.14 Component Library

See Phase 4 (Section 4) for the full engineering specification and package structure. Section 2 defines the *rules*; Section 4 defines the *inventory*.

### 2.15 Screen Templates

1. **Focus Template** (Hear It, Say It, Find It): top bar (back + hearts/streak) → hero content zone → bottom mascot bubble + primary CTA. Max 3 interactive elements.
2. **Journey Template** (Map): persistent top stats bar → scrollable winding path → floating mascot bubble. Auto-scroll to current node on entry.
3. **Celebration Template** (Letter Complete, Blend It Complete): full-bleed reward visual → single primary CTA, zero competing actions.

Parent Dashboard and Report Preview are exempt — adult information-density patterns apply.

### 2.16 Interaction & Feedback Patterns — resolving the hearts conflict (1.3 #6)

**Ruling: keep the hearts mechanic; reframe it entirely at the presentation layer. Do not remove `HeartManager` or its data model.**

Reasoning: removing a fully-implemented, tested mechanic this close to deployment is high-risk for low benefit. The research's real objection is *punitive framing*, not a bounded-attempts mechanic — and v1.0 already wrote the reconciliation ("Instead of 'You lost a heart,' use 'Let's practice one more time'"), it just isn't applied everywhere hearts appear.

**Required changes (presentation-only):**
1. Every heart-loss moment triggers v1.0's copy substitution — never a bare "−1" or context-free icon loss.
2. Heart icons render in Energy Orange or Correction Orange, **never red**.
3. Recovery (3 consecutive correct → +1 heart) gets equal or greater visual production value than loss.
4. Depletion/reset reads as "Let's try again together!" with mascot in Encouraging state — never "Game Over."
5. `heartsLost` stays a valid internal accuracy metric for Parent Dashboard but is never surfaced to the child as a loss count.

Document this as a conscious design decision in your capstone defense, not an implicit one.

### 2.17 Error States

Universal rule: Correction Orange fill + retry-arrow icon + gentle shake (150–250ms) + neutral "boop" sound + mascot Encouraging state + v1.0 copy substitution — identical across `FeedbackCard`, `PictureCard`, `WordFeedbackCard`. No screen shake as punishment, no buzzer, no flashing, no red anywhere. Say It's >40dB noise alert should also use Correction Orange, not red — currently unspecified in the SDD.

### 2.18 Empty States

Never blank/broken. Faded silhouette or mascot-pointing gesture indicates what to do. Profile grid's dashed-circle "Add Profile" placeholder is a correct existing pattern — extend the same language elsewhere.

### 2.19 Audio Guidelines (carried forward, tightened)

Warm human voiceover (already correctly implemented via pre-recorded assets). Background music restricted to Map/Celebration only, disabled during active tasks — **needs verification**, not documented either way in the SDD. Distinct consistent micro-sounds per action per v1.0's Sound Design table — enforce, don't redesign.

---

## Phase 3 — UI/UX Screen-by-Screen Audit

Each of the 12 screens from the SDD's Screen Inventory is audited against the v2.0 Design System. "Effort" reflects presentation-layer work only, assuming the Section 4 component library exists first.

### 3.1 SplashScreen

| | |
|---|---|
| **Strengths** | Correctly offline-only per SDD ("no network calls block the UI thread"); mascot-centered branding matches emotional-design intent. |
| **Weaknesses** | None functionally blocking; purely a token-application target once `PlayItTheme` exists. |
| **Why it matters less** | Lowest-frequency screen (seen once per app open, ~1 second); low leverage for effort spent. |
| **Educational impact** | Low |
| **Effort** | Low |
| **Recommendation** | Apply tokens only. No structural change needed. |

### 3.2 ProfileSelectScreen

| | |
|---|---|
| **Strengths** | Grid of large circular avatars (64dp+) already exceeds the new 54dp floor; dashed-circle "Add Profile" empty-state pattern is correct per 2.18. |
| **Weaknesses** | SDD doesn't specify spacing value between profile cards beyond "minimum 16dp" — needs explicit verification against the token, not just a stated intention. |
| **Educational impact** | Medium — this is a pre-literate identification task; mis-taps here cascade into a wrong child's progress being recorded. |
| **Effort** | Low |
| **Recommendation** | Verify 16dp spacing is enforced via the spacing token, not a hardcoded value; apply `PlayItButton`/`ProfileCard` component once built. |

### 3.3 NamePromptScreen

| | |
|---|---|
| **Strengths** | Simple, single-focus flow (name + avatar), matches Focus Template well already. |
| **Weaknesses** | Text input field for name entry is inherently reading/writing-dependent for a pre-literate user — this is a structural tension the SDD doesn't address (a 6-year-old typing their own name is unlikely without help, which is fine if a parent is expected to assist at setup, but that assumption should be explicit). |
| **Educational impact** | Low (one-time setup, likely parent-assisted) |
| **Effort** | Low |
| **Recommendation** | No redesign needed; add a small "Ask a grown-up to help type your name" mascot line to make the parent-assist assumption explicit rather than implicit — a 1-line copy change. |

### 3.4 MapScreen

| | |
|---|---|
| **Strengths** | Winding Candy-Crush-style path is well-suited to spatial-journey mental models research recommends; auto-scroll to current node is already specified; locked/unlocked states exist. |
| **Weaknesses** | Node coloring appears per-letter/decorative rather than status-semantic (§1.3 #5); parallax clouds present real reduced-motion risk if not gated; hearts + star counters visible but no persistent app-wide linear progress bar (§1.2). |
| **Why** | This is the highest-traffic screen in the app — every session starts and ends here, so inconsistencies here are seen more often than anywhere else. |
| **Educational impact** | **High** — this is where "are we there yet?" anxiety (research §4) is either resolved or created. |
| **Effort** | Medium-High |
| **Recommendation** | Apply the 4-state node color system (§2.4); gate parallax behind the reduced-motion toggle; consider adding a slim top-anchored "X/28 letters" bar as a secondary, non-competing progress signal above the winding path. |

### 3.5 HearItScreen

| | |
|---|---|
| **Strengths** | Minimalist by design (giant letter, mascot, one play button) — already close to ideal ≤3-element compliance; correctly plays the *phonetic sound*, not the letter name, per Marungko principle; replay counter satisfies "autonomy" (SDT). |
| **Weaknesses** | None structural; token/component application only. |
| **Educational impact** | High (this is the first-exposure moment for every new phoneme) |
| **Effort** | Low |
| **Recommendation** | Apply Body Large (20sp) to mascot instruction text; apply new touch-target floor to `PlayButton`; otherwise preserve as-is — this screen is close to a model example of the design philosophy already. |

### 3.6 SayItScreen

| | |
|---|---|
| **Strengths** | Hold-to-record mic interaction is appropriately large (72dp, already exceeds new floor); attempt tracker and noise-level indicator show good attention to real classroom/rural-device conditions (ambient noise). |
| **Weaknesses** | `FeedbackCard` documented as "green or red" — direct Design System violation (§1.3 #1); no visual "listening" waveform confirmed beyond a generic "ListeningAnimation" label — the Gemini review explicitly flags this as a gap ("children need... distinct auditory/visual rewards," "create a robust visualizer that reacts to microphone input volumes"). |
| **Educational impact** | **High** — this is the only speech-production checkpoint in the entire app; if the child can't tell whether the app is listening, the whole module's trust breaks down. |
| **Effort** | Medium-High (mic-level visualizer is genuinely new work, not just token application) |
| **Recommendation** | Fix feedback color immediately (Correction Orange). Build a real amplitude-reactive waveform tied to `AudioCapture`'s PCM stream — this is the single highest-value net-new interaction improvement in the whole app, not just a polish item. |

### 3.7 FindItScreen

| | |
|---|---|
| **Strengths** | 5-card grid within the 3-target/2-distractor design supports retrieval practice well; reuses `HeartDisplay` from Say It (good architectural discipline already present). |
| **Weaknesses** | Same red-feedback violation as Say It ("flash red, disable image"); no explicit multi-touch lockout during feedback animation (Gemini review item #18 — "prevent multi-touch bugs... by disabling UI during audio playback"), which risks a child tapping a second card while feedback for the first is still resolving. |
| **Educational impact** | High (core discrimination/retrieval-practice loop, reused every letter) |
| **Effort** | Medium |
| **Recommendation** | Fix feedback color; add input-lock during the ~300–500ms feedback animation window. |

### 3.8 LetterCompleteScreen

| | |
|---|---|
| **Strengths** | Correctly uses Celebration Template already (mascot + stars + single Continue CTA); variable-reward psychology (chest-opening sticker) matches the research's "Action-Reward-Investment loop" recommendation directly. |
| **Weaknesses** | None structural. |
| **Educational impact** | High (this is the dopamine/motivation payoff moment the whole retention loop depends on) |
| **Effort** | Low |
| **Recommendation** | Apply tokens; verify celebratory sound volume is normalized (v1.0's own stated requirement — "Ensure celebratory sounds are normalized... to prevent sudden spikes that startle the child" — worth an explicit QA pass, not just a design note). |

### 3.9 BlendItScreen

| | |
|---|---|
| **Strengths** | Tap-based tile placement (rather than drag) is a defensible, arguably better-for-motor-skills deviation from the original drag-and-drop research recommendation (§1.2); word-image-audio grouping ("CAT" + cat illustration + speaker icon) satisfies dual-coding/spatial-contiguity. |
| **Weaknesses** | Same red-feedback violation as Say It/Find It; the pedagogical drift from phoneme-blending to whole-word spelling (§1.2) means the "blending" metaphor from the app's own name/branding isn't literally represented in this screen's interaction anymore. |
| **Educational impact** | High (checkpoint gating an entire letter group; also the screen most exposed to parent/adviser scrutiny given the module's explicit adviser-requested addition) |
| **Effort** | Medium |
| **Recommendation** | Fix feedback color. Add a lightweight "snap" animation/sound the moment the *last* correct letter is placed (a beat before the full-word audio plays) — this recreates a blending-style payoff moment without reverting the tap-based interaction model or touching `BlendItViewModel`. |

### 3.10 BlendItCompleteScreen

| | |
|---|---|
| **Strengths** | Correctly reuses Celebration Template and `StarAnimation`. |
| **Weaknesses** | None structural. |
| **Educational impact** | Medium-High |
| **Effort** | Low |
| **Recommendation** | Token application only. |

### 3.11 ParentDashboardScreen

| | |
|---|---|
| **Strengths** | Math-gate access control is a smart, appropriately low-friction guard for a device shared between a child and parent; profile-switcher dropdown, at-risk letter flagging, and PDF export are all genuinely useful, well-scoped features; correctly exempted from child-facing simplicity constraints. |
| **Weaknesses** | No `ReducedMotionToggle` composable exists in the SDD's component table despite this being the logical, and only sensible, place to put one (§2.13 gap); 7:1 contrast target needs explicit verification, not just a stated design-system aspiration. |
| **Educational impact** | Indirect but high — this is the interface that turns app data into a parent's actual involvement (research explicitly ties parental praise/involvement to retention). |
| **Effort** | Low-Medium |
| **Recommendation** | Add `ReducedMotionToggle` to `ParentDashboardScreen`'s component list; run a real contrast check on `LetterPerformanceTable`'s green/yellow/red status colors (note: this red is fine — the "avoid harsh red" rule is a *child-facing emotional-safety* rule, not a general prohibition, and adult analytics dashboards legitimately use red/yellow/green status semantics). |

### 3.12 ReportPreviewScreen

| | |
|---|---|
| **Strengths** | Certificate-style output, offline PDF generation via native `PdfDocument` API (no extra dependency) is a lean, appropriate technical choice; supports the explicit rural/offline-sharing constraint (Bluetooth/local save). |
| **Weaknesses** | None significant — this is an adult-facing, low-frequency, low-risk screen. |
| **Educational impact** | Low-Medium (indirect, via parent engagement) |
| **Effort** | Low |
| **Recommendation** | Polish-tier only; not a priority relative to child-facing screens. |

### 3.13 Summary Priority Ranking (by Educational Impact × Frequency)

1. **Map Screen** — highest frequency, status-color fix + progress bar
2. **Say It Screen** — highest net-new value (mic visualizer), highest-stakes feedback-color fix
3. **Find It / Blend It Screens** — feedback-color fix, input-lock, blending-cue
4. **Letter Complete / Blend It Complete** — sound-normalization QA
5. **Hear It Screen** — near-model already, light-touch only
6. **Parent Dashboard** — reduced-motion toggle addition
7. **Profile Select / Name Prompt / Splash / Report Preview** — token application only

---

## Phase 4 — Component Library Specification & Refactoring Plan

### 4.1 Duplication Found in the Current SDD Component Tables

Cross-referencing every module's Front-end/Back-end component tables in the SDD reveals the same UI pattern implemented independently, per-screen, at least four times:

| Pattern | Currently implemented as (per SDD) | Screens |
|---|---|---|
| Feedback card (correct/incorrect) | `FeedbackCard` (Say It), inline flash on `PictureCard` (Find It), `WordFeedbackCard` (Blend It) — **3 separate implementations** | Say It, Find It, Blend It |
| Mascot instruction bubble | `MascotBubble` — named consistently, but SDD gives no evidence of a shared composable vs. per-screen copies | Map, Hear It, Say It, Find It, Blend It |
| Heart pool display | `HeartDisplay` — SDD explicitly notes Find It and Blend It "reuse" the Say It instance/class, which is good backend discipline, but the *visual* composable reuse isn't confirmed | Say It, Find It, Blend It, (Map shows hearts icon too) |
| In-lesson progress indicator | `SubLevelProgressBar` (3-step pill) vs. `BlendItProgressIndicator` ("X/5 words") vs. Map's star/heart top bar — three different visual treatments of the same underlying idea | Hear It, Say It, Find It, Blend It, Map |
| Circular audio-trigger button | `PlayButton` (Hear It), `MicrophoneButton` (Say It), `WordAudioButton` (Blend It) — three named composables, likely three separate visual implementations | Hear It, Say It, Blend It |
| Buttons | No `PlayItButton` component referenced anywhere in the SDD — Primary/Success/Secondary styling appears to be defined only at the Design System level, never operationalized as a shared composable | All screens |

This is exactly the duplication pattern the Gemini review predicted from the outside ("If you find yourself repeatedly typing `Modifier.padding(16.dp).background(Color.Blue)`, your UI lacks reusable components") — the SDD's own documentation independently confirms it from the inside.

### 4.2 Proposed `core:ui` Package Structure

```
core/ui/
├── theme/
│   └── PlayItTheme.kt              // wraps MaterialTheme with all tokens below
├── tokens/
│   ├── Color.kt                    // §2.3/2.4
│   ├── Type.kt                     // §2.5
│   ├── Spacing.kt                  // §2.6
│   ├── Shape.kt                    // corner radii
│   ├── Elevation.kt                // §2.8 tonal system
│   └── Motion.kt                   // §2.12 durations + spring specs
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
│   │   └── MascotIdleScaffold.kt   // §2.11 10-second idle cue logic
│   ├── progress/
│   │   ├── SegmentedProgressBar.kt // replaces SubLevelProgressBar + BlendItProgressIndicator
│   │   └── TopStatsBar.kt
│   ├── hearts/
│   │   └── HeartDisplay.kt         // single shared visual, reused everywhere HeartManager is
│   ├── audio/
│   │   └── CircularAudioButton.kt  // base for PlayButton | MicrophoneButton | WordAudioButton
│   ├── map/
│   │   ├── MapNodeBase.kt          // shared base for LetterNode + BlendItChallengeNode
│   │   └── PathConnector.kt
│   ├── star/
│   │   └── StarAnimation.kt
│   └── dialog/
│       └── FullScreenDialog.kt     // per research: no partial modals for children
└── a11y/
    └── ReducedMotionState.kt       // §2.13 — new, backs ReducedMotionToggle in Parent Dashboard
```

### 4.3 Consolidation Priorities (highest leverage first)

| Priority | Component | Why first | Screens touched | Fixes |
|---|---|---|---|---|
| 1 | `FeedbackCard` | Directly fixes the red/orange Design System violation (§1.3 #1) in **one place** instead of three | Say It, Find It, Blend It | §1.3 #1 |
| 2 | `HeartDisplay` + `HeartManager` copy layer | Directly implements the hearts reframing ruling (§2.16) in one place | Say It, Find It, Blend It, Map | §1.3 #6 |
| 3 | `PlayItButton` | Highest-frequency component in the app; currently has zero shared implementation per the SDD | All 12 screens | General consistency, Gemini review #3 |
| 4 | `MascotBubble` + idle scaffold | Enables the new idle-cueing behavior (§2.11) to ship once, everywhere, instead of per-screen | Map, Hear It, Say It, Find It, Blend It | §1.3 #4 |
| 5 | `MapNodeBase` | Enables the 4-state color system (§2.4) to be enforced structurally rather than per-instance | Map | §1.3 #5 |
| 6 | `SegmentedProgressBar` | Consolidates 3 visual treatments into 1 | Hear It, Say It, Find It, Blend It | Consistency |
| 7 | `CircularAudioButton` | Lower urgency — functional differences (hold-to-record vs. tap-to-play) mean this is a shared *base*, not full unification | Hear It, Say It, Blend It | Consistency |

### 4.4 What Explicitly Does Not Change

To keep this plan low-risk for a capstone timeline, the following are **out of scope** and should not be touched:
- `PlayItDatabase`, all DAOs, `playit_prepopulated.db`, schema JSON — per the Gemini review's own explicit instruction, and reconfirmed here.
- `VoskRecognizer` internals — encapsulate behind its existing interface; do not modify the recognition pipeline itself.
- Any Repository interface or implementation.
- `SessionManager`, `UnlockManager`, `GroupUnlockManager`, `StreakTracker` business logic — these are Domain-layer and structurally sound per the audit; only their *rendered output* changes.
- ERD relationships and foreign keys.

---

## Phase 5 — Updated UI Implementation Roadmap

This replaces the prior execution roadmap's phase structure with one ordered by **user experience impact**, not build sequence. Every task assumes presentation-layer work only unless stated otherwise.

### Phase A — Foundation

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| A1. Build token files (`Color.kt`, `Type.kt`, `Spacing.kt`, `Shape.kt`, `Elevation.kt`, `Motion.kt`) | Single source of truth for every visual value | Indirect — enables every downstream fix | None | Low | Must Have |
| A2. Wrap app in `PlayItTheme` / `MaterialTheme`, remove hardcoded colors/fonts | Eliminate the duplication the Gemini review flagged | Consistent look across all screens on first load | A1 | Medium | Must Have |
| A3. Raise global minimum touch target 48dp → 54dp | Meet CCI-backed motor-skill floor | Fewer mis-taps for 5–7 year olds | A1 | Low | Must Have |
| A4. Correct SDD scope statement ("10 screens" → 12) | Documentation hygiene for capstone defense | N/A (academic correctness) | None | Low | Should Have |

### Phase B — Design System Integration

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| B1. Replace red feedback with Correction Orange across Say It / Find It / Blend It | Fix the highest-priority Design System violation found in this audit | Emotional safety during every single error moment in the app | A1–A2 | Medium | **Must Have** |
| B2. Reframe hearts per §2.16 ruling (copy, color, recovery weight, depletion tone) | Reconcile growth-mindset research with the existing mechanic | Failure feels safe, not punitive | A1–A2 | Medium | **Must Have** |
| B3. Migrate elevation to tonal system, retain shadow only on Primary CTA | Reduce visual noise per Material 3 / CLT research | Cleaner, less distracting screens | A1–A2 | Medium | Should Have |
| B4. Apply new typography scale (48/32/24/20/18/16sp) | Match children's visual-tracking needs | Easier reading for emergent readers | A1 | Low | Should Have |

### Phase C — Component Refactoring

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| C1. Build unified `FeedbackCard` (Correct/Retry variants) | Retire 3 separate implementations | Consistent, predictable feedback everywhere | B1 | Medium-High | **Must Have** |
| C2. Build unified `HeartDisplay` + copy layer | Retire per-screen heart visuals | Consistent hearts framing (B2) enforced structurally | B2 | Medium | Must Have |
| C3. Build `PlayItButton` (Primary/Secondary/Success) | Retire ad hoc button styling | Predictable CTA location/behavior (Fitts's Law) | A1–A2 | Medium | Must Have |
| C4. Build `MascotBubble` + `MascotIdleScaffold` (10s idle cue) | Ship idle-scaffolding once, everywhere | Non-judgmental help exactly when a child stalls | A1–A2 | Medium | Should Have |
| C5. Build `MapNodeBase` w/ 4-state color system | Enforce status-semantic node coloring | Clear at-a-glance sense of progress on the Map | B3 | Medium | Must Have |
| C6. Build `SegmentedProgressBar` | Consolidate 3 progress visual treatments | Consistent "how much is left" signal | A1–A2 | Low-Medium | Should Have |
| C7. Build `CircularAudioButton` shared base | Consistent tap animation/target size across Play/Mic/WordAudio buttons | Predictable audio-control behavior | A1–A2, A3 | Medium | Should Have |
| C8. Add `ReducedMotionToggle` to Parent Dashboard + wire to `ReducedMotionState` | Close the accessibility gap identified in §2.13 | Usable by neurodivergent/motion-sensitive children | A1–A2 | Low-Medium | Must Have |

### Phase D — High-Impact Screens

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| D1. Map Screen: apply `MapNodeBase` color system | Fix decorative→semantic node coloring | Child instantly understands progress state | C5 | Medium | Must Have |
| D2. Map Screen: gate parallax clouds behind reduced-motion state | Prevent sensory overload | Safer for motion-sensitive children | C8 | Low | Must Have |
| D3. Map Screen: add slim top-anchored "X/28 letters" secondary progress signal | Answer "are we there yet?" at the journey level, not just per-lesson | Reduced temporal anxiety | C6 | Medium | Should Have |
| D4. Profile Select / Name Prompt / Splash: apply tokens, verify 16dp spacing | Baseline consistency on first-run screens | Fewer accidental profile mis-taps | A1–A2 | Low | Should Have |

### Phase E — Lesson Screens

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| E1. Say It: build real amplitude-reactive mic-listening visualizer tied to `AudioCapture` PCM stream | Close the Gemini-review-flagged gap; child can see the app is listening | Trust in the speech-recognition loop; less confusion about silence/lag | A1–A2, C7 | **High** | **Must Have** |
| E2. Say It / Find It / Blend It: apply unified `FeedbackCard` | Retire per-screen implementations | Consistent, emotionally-safe feedback everywhere | C1 | Medium | Must Have |
| E3. Find It / Say It: lock UI input during feedback-animation window (~300–500ms) | Prevent multi-touch/race-condition mis-taps (Gemini review #18) | Fewer accidental double-answers | E2 | Low-Medium | Should Have |
| E4. Blend It: add "snap" animation + sound cue on final correct letter placement, before full-word audio | Restore a blending-moment payoff without touching `BlendItViewModel` | Reconnects the module to the Marungko blending metaphor | A1–A2 | Medium | Should Have |
| E5. Hear It: apply Body Large typography + new touch targets only | Preserve this screen's already-strong design; light touch | Marginal readability gain | B4, A3 | Low | Nice to Have |
| E6. Letter Complete / Blend It Complete: QA-verify celebratory sound normalization | Prevent startling volume spikes (v1.0's own stated requirement) | No jarring/scary loud moments during a reward | None (audio QA, not code) | Low | Should Have |

### Phase F — Dashboard

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| F1. Parent Dashboard: wire `ReducedMotionToggle` into a real Settings section | Give the accessibility toggle a home | Parents can act on motion-sensitivity needs | C8 | Low | Must Have |
| F2. Parent Dashboard: contrast-audit `LetterPerformanceTable` and stat cards against 7:1 | Meet the design system's own stricter adult-dashboard standard | Legibility for parents in bright outdoor/rural lighting conditions | A1 | Low-Medium | Should Have |
| F3. Report Preview: token application only | Visual consistency with the rest of the app | Minor polish | A1–A2 | Low | Nice to Have |

### Phase G — Polish

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| G1. Illustration/asset audit: confirm one `phonemeId → imagePath` mapping is enforced everywhere, no per-screen art variants | Protect the neural-association pedagogy (§2.10) | Faster, more reliable phoneme recognition over time | None (asset pipeline check) | Low-Medium | Should Have |
| G2. Background-music audit: confirm silence during active tasks, music only on Map/Celebration | Protect phonological working memory during phonics tasks | Less auditory competition during the actual learning moment | None (audio QA) | Low | Should Have |
| G3. Full celebration-screen animation pass (confetti, star drop/bounce/glow) | Maximize the variable-reward dopamine moment research identifies as key to retention | Stronger motivation loop | C1, B3 | Medium | Nice to Have |

### Phase H — Accessibility

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| H1. Full contrast audit across every screen (4.5:1 child-facing, 7:1 Parent Dashboard), adjust tokens (e.g. `learningBlueDeep`) where needed | Meet WCAG AA in practice, not just in the design-system doc | Usable by low-vision users and in bright outdoor lighting | A1, F2 | Medium | Must Have |
| H2. Reduced-motion audit end-to-end (Map parallax, celebration particles, mascot idle loop) | Ensure the toggle actually suppresses every motion source, not just some | Real protection for motion-sensitive/neurodivergent children | C8, D2 | Medium | Must Have |
| H3. Color-independence audit: every color-coded state has a matching icon/shape | Meet the design system's own stated rule in practice | Usable by color-blind children | C1, C5 | Medium | Must Have |

### Phase I — Final QA

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| I1. Hallway usability test with an actual Grade-1-aged child | Validate every assumption in this document against a real user before deployment | Directly de-risks the capstone defense and the deployment decision | All prior phases substantially complete | Low (process) | **Must Have** |
| I2. Multi-profile regression pass (create/switch/delete across up to 6 profiles) | Confirm `SessionManager` scoping holds under the new UI | No cross-contaminated progress between siblings/classmates sharing a device | None (verification only) | Medium | Must Have |
| I3. APK size / asset-compression audit; evaluate Play Asset Delivery (install-time mode only) | Manage deployment size for rural, often storage-constrained devices | Faster install, less storage pressure on shared school devices | None | Medium | Should Have |
| I4. Composable line-count / state-hoisting code review (flag any Composable >100 lines, confirm ViewModels hold no Context/Compose references) | Confirm the Gemini review's suspected code-smells are or aren't present | Maintainability for post-capstone iteration | I2 | Medium | Should Have |

---

## Phase 6 — Technical Refactoring Recommendations

These are architecture-level notes distinct from the UI roadmap above — they concern code health, not visual design, and are drawn from cross-referencing the Gemini repository review against what the SDD actually documents as built.

| Recommendation | Rationale | Risk if skipped |
|---|---|---|
| Encapsulate Vosk entirely behind its existing interface; do not modify `VoskRecognizer` internals during this UI pass | Offline speech recognition is notoriously fragile to get working at all — the Gemini review is explicit on this point, and the SDD shows a clean interface boundary already exists (`SpeechValidator`, `VoskRecognizer` as separate class) | Any regression here is expensive to debug and has no UI-visible payoff |
| Verify ViewModels hold no `Context` or Compose UI references | Standard MVVM hygiene; the SDD's documented contracts are clean on paper, but this can only be confirmed by reading actual source | Memory leaks, harder testing, violates the Clean Architecture boundary the project is otherwise praised for |
| Audit any Composable exceeding 100 lines and break it down | Directly enables the Section 4 component consolidation — a 100+ line screen composable is usually where the duplicate `FeedbackCard`/`MascotBubble` implementations are hiding | Component library adoption stalls if screens can't be decomposed into the new shared pieces |
| Hoist state to screen-level ViewModels, keep child composables stateless | Prerequisite for the shared component library in Section 4 — a stateless `FeedbackCard` can be reused across Say It/Find It/Blend It; a stateful one can't | Without this, "shared" components end up re-forked per screen anyway, defeating the consolidation |
| Evaluate Play Asset Delivery for Vosk models + audio assets — **install-time delivery mode only** | Reduces APK size (Gemini review item #12) without breaking the offline-first guarantee the SDD explicitly requires for rural deployment in Baleno | On-demand/fast-follow delivery modes require connectivity at first use — would silently violate "the app must open instantly offline" for a first-time user without signal |
| Leave `PlayItDatabase`, DAOs, and the prepopulated `.db`/schema JSON untouched | Explicitly instructed by the Gemini review, reconfirmed by this audit — the data layer is the strongest part of the codebase | Any schema churn this late risks breaking the ERD relationships that six modules depend on simultaneously |
| Disable UI input during audio playback / feedback animation windows (Gemini review item #18) | Prevents multi-touch race conditions where a child taps a second target while the first answer's feedback is still resolving | Data-integrity risk: a rushed second tap could log a false attempt against `SayItAttempt`/`FindItAttempt`, skewing the Parent Dashboard's accuracy analytics |
| Organize `assets/audio` by Marungko group/level if not already done (Gemini review item #13) | Matches the `LetterGroup`/`LetterGroupMember` structure already in the ERD — asset organization should mirror data organization | Harder to maintain and audit for anchor-image/audio permanence (§2.10) as the letter set grows |

---

## Phase 7 — Final Deployment Readiness Assessment

### 7.1 Readiness Scorecard

| Dimension | Rating | Basis |
|---|---|---|
| Architecture (Clean Architecture + MVVM) | **High** | Confirmed by both the Gemini review and independent cross-check against the SDD's layer diagrams; no changes recommended |
| Offline resilience | **High** | Room + prepopulated DB + on-device Vosk + native `PdfDocument` — core strength of the project, explicitly preserved throughout this blueprint |
| Data model / ERD | **High** | 12-entity schema is coherent, profile-scoped throughout, and requires zero changes per this audit |
| UI/UX polish | **Low → Improving** | This is the actual gap; every fix identified is presentation-layer only, which is the best-case scenario for a project at this stage |
| Design-system self-consistency | **Low currently, High after Phase A–C** | The red-feedback and hearts-framing contradictions (§1.3) are real, evidenced conflicts between the team's own documents — both are fixable without touching business logic |
| Accessibility | **Medium** | Reading support and audio-first design are already strong; touch-target floor, reduced-motion toggle, and contrast verification are the concrete gaps |
| Pedagogical fidelity to Marungko method | **Medium** | Core sequence, lowercase-first rule, and phonemic-sound playback are correctly implemented; the Blend It module's drift from syllable-blending to whole-word spelling (§1.2) is a conscious scope change worth explicitly ratifying (or lightly re-anchoring per E4) with your adviser before defense |
| Deployment-blocking risk | **Low** | No architectural rework, no schema migration, and no third-party integration risk stands between the current state and a defensible v2.0 |

### 7.2 Go / No-Go by Priority Tier

**Must Have (blocking for a confident deployment/defense):**
- B1 — Remove red from all feedback states (Design System compliance)
- B2 — Reframe hearts mechanic (growth-mindset alignment)
- A3 — Raise touch-target floor to 54dp
- C8 / H2 — Reduced-motion toggle, wired end-to-end
- E1 — Say It mic-listening visualizer (flagged independently by the Gemini review as a real UX gap, not just a nice-to-have)
- H1 / H3 — Contrast and color-independence audits
- I1 — Hallway usability test with an actual child
- I2 — Multi-profile regression pass

**Should Have (materially improves the product, not blocking):**
- D1–D3 — Map screen semantic color + progress signal
- E3, E4, E6 — input-lock, blending-cue, sound-normalization QA
- G1, G2 — asset/audio pipeline audits
- F1, F2 — Parent Dashboard accessibility wiring + contrast
- I3, I4 — APK size and code-health review

**Nice to Have (defer past initial deployment without real cost):**
- E5 — Hear It light-touch polish (already close to ideal)
- G3 — Full celebration animation pass
- F3 — Report Preview polish

### 7.3 Bottom Line

playIT does not need to be rebuilt to be deployment-ready — it needs to be made internally consistent with the design principles and educational research the team already produced. Every Must Have item above is a token, component, or QA-verification change; none require new Room entities, new repository interfaces, or changes to the Vosk pipeline. That is the most useful thing this audit can tell you going into a capstone defense: the hard problems were already solved correctly, and what remains is finishing work you're fully equipped to execute in the time you have.

---

*End of playIT Master Design & Engineering Blueprint v2.0.*
