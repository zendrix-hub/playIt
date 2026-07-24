# playIT — 00. Project Rules & Index
### Split from: playIT Master Design & Engineering Blueprint v2.0 (BSIT Capstone, IT332-56, Cebu Institute of Technology University)

This is the entry-point document for the phase-split blueprint. Read this file first in every Antigravity session — it carries the governing rules, the source-priority order, and the map of which file owns which specification. No implementation detail lives only here; this file indexes and constrains, it does not duplicate.

---

## 0.1 Document Map

| File | Owns |
|---|---|
| `00_ProjectRules.md` | This file — rules, priorities, cross-reference index |
| `01_Foundation.md` | Repository audit findings, Design Tokens, Color, Typography, Spacing, Touch Targets, Elevation, Phase A tasks |
| `02_ComponentLibrary.md` | Component inventory, `core:ui` package structure, Iconography, Illustration rules, Hearts ruling, Error/Empty states, Phase C tasks |
| `03_MotionSystem.md` | Motion Language & Animation Standards, Reduced-Motion system, Mascot idle scaffolding |
| `04_AudioSystem.md` | Audio Guidelines, Say It mic visualizer spec, sound-normalization QA |
| `05_MapScreen.md` | MapScreen audit + Phase D tasks |
| `06_HearIt.md` | HearItScreen audit |
| `07_SayIt.md` | SayItScreen audit |
| `08_FindIt.md` | FindItScreen audit |
| `09_ParentDashboard.md` | ParentDashboardScreen + ReportPreviewScreen audits, Phase F tasks |
| `10_FinalPolish.md` | LetterComplete / BlendIt / BlendItComplete audits, Phase G/H/I tasks, Deployment Readiness Assessment |

Screens not broken into their own file (SplashScreen, ProfileSelectScreen, NamePromptScreen) are documented in `01_Foundation.md` under Phase D groundwork, since their fixes are token-application-only and share dependencies with the Foundation phase.

**Cross-reference convention:** every spec below is tagged with its original section number from Master Blueprint v2.0 (e.g. `[orig §2.4]`) so any file can be traced back to the source of truth. When a rule in one phase file depends on a component or token defined in another, the dependency is stated explicitly rather than restated in full — follow the pointer, don't fork the spec.

---

## 0.2 Source Priority (applies to all phase files)

1. Repository implementation (SDD v1.0, as the documented source of truth for what is built)
2. UX/Educational Research (`playIT Design Bible and UX Research Report`)
3. Software Design Description (SRS-derived architecture)
4. Existing Design System v1.0

Where these four sources conflict, every phase file states the conflict explicitly, explains the reasoning, and gives one resolved recommendation — never a silent pick.

---

## 0.3 Executive Summary (carried forward unchanged)

playIT's architecture is genuinely strong: Clean Architecture + MVVM, a fully offline Room schema, Vosk-based on-device speech recognition, and a profile-scoped data model are all appropriate, well-documented, and — per the repository review — implemented to a "Functional Prototype (Alpha)" standard with **High** architectural quality. The gap is entirely in the presentation layer: no enforced design system, ad-hoc styling, and several places where the actual implementation (as documented in the SDD) contradicts the project's own Design System v1.0 and the educational-psychology research the team already produced.

**Every recommendation across all phase files is presentation-layer only.** Nothing here asks you to touch `PlayItDatabase`, the Vosk integration, repository interfaces, or the ERD. The hard, invisible 80% is correct. This blueprint set is about making the visible 20% match the quality of the engineering underneath it.

Four findings drive most of what follows (full detail in `01_Foundation.md` §1.3):

1. Design System v1.0 bans harsh red for incorrect answers, but the SDD describes feedback as "green or red" — a direct contradiction. See `02_ComponentLibrary.md` for the `FeedbackCard` resolution.
2. UX research recommends against lives/hearts systems, yet hearts are a core, heavily-implemented mechanic. See `02_ComponentLibrary.md` §Hearts Ruling.
3. Touch target minimums disagree across the team's own documents (48dp vs. 54–64dp). Resolved in `01_Foundation.md`.
4. Elevation strategy disagrees (drop shadow vs. tonal). Resolved in `01_Foundation.md`.

None of these require new features. All are token-level or component-level fixes.

---

## 0.4 Non-Negotiable Constraints (apply to every screen, every phase file)

These are treated as constraints, not preferences, because they map directly to measurable working-memory and motivation effects in 5–7 year olds `[orig §2.2]`:

1. **≤3 interactable elements per screen.** Working memory in this age range holds 2–3 chunks. Any screen with more than 3 simultaneous tap targets (excluding passive/decorative elements) is a cognitive-load bug, not a design choice.
2. **Extraneous load → zero.** Every pixel that isn't the lesson itself is competing directly with phonics comprehension for the same limited cognitive budget.
3. **Growth mindset over performance framing.** No letter grades, no "Wrong!," no visible penalty language. Failure states must read as *"try again,"* never as *"you lost."*
4. **Dual-coding + spatial contiguity.** Every phoneme/word must pair sound + image + text in tight physical proximity (CTML).

Design Philosophy governing all seven v1.0 priorities (clarity before decoration, encouragement before correction, audio before reading, progress visibility, consistency, large touch targets, cultural familiarity) is carried forward unchanged `[orig §2.1]`. Core principle: *"Every interaction should make a child feel successful, capable, and motivated to continue learning."*

---

## 0.5 What Explicitly Does Not Change (applies globally)

To keep this plan low-risk for a capstone timeline, the following are out of scope in **every** phase file and must not be touched by any task described in this document set `[orig §4.4, §6]`:

- `PlayItDatabase`, all DAOs, `playit_prepopulated.db`, schema JSON.
- `VoskRecognizer` internals — encapsulate behind its existing interface; do not modify the recognition pipeline itself.
- Any Repository interface or implementation.
- `SessionManager`, `UnlockManager`, `GroupUnlockManager`, `StreakTracker` business logic — Domain-layer, structurally sound; only their *rendered output* changes.
- ERD relationships and foreign keys.
- `BlendItViewModel` (interaction model changes are presentation-only per `10_FinalPolish.md` E4).

Additional technical-hygiene rules (non-visual, but binding on any implementation work touched by this blueprint) — full detail in `10_FinalPolish.md` Phase I:

- Verify ViewModels hold no `Context` or Compose UI references.
- Audit any Composable exceeding 100 lines and break it down.
- Hoist state to screen-level ViewModels, keep child composables stateless.
- Evaluate Play Asset Delivery for Vosk models + audio assets — **install-time delivery mode only** (on-demand/fast-follow would silently break the offline-first guarantee).
- Disable UI input during audio playback / feedback animation windows.
- Organize `assets/audio` by Marungko group/level.

---

## 0.6 Priority Legend (used consistently across all phase files)

- **Must Have** — blocking for a confident deployment/defense.
- **Should Have** — materially improves the product, not blocking.
- **Nice to Have** — defer past initial deployment without real cost.

See `10_FinalPolish.md` §Go/No-Go for the full consolidated priority tier list across every task in the plan.

---

## 0.7 Screen Inventory (12 screens, resolves scope-doc inconsistency)

Scope §1.2 states 10 screens; the SDD's actual Screen Inventory lists 12: Splash, ProfileSelect, NamePrompt, Map, HearIt, SayIt, FindIt, LetterComplete, BlendIt, BlendItComplete, ParentDashboard, ReportPreview. Treat 12 as authoritative; correcting the stale scope statement is task A4 in `01_Foundation.md` (Should Have, documentation hygiene only).
