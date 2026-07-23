# playIT Execution Roadmap

**Document type:** Permanent operational manual for Antigravity IDE development sessions
**Status:** Authoritative — supersedes ad-hoc task selection
**Synthesized from:** `playIT_Master_Context_v1.0.md` · `engineering_review.md` (Build RC-1, 2026-07-21) · `playIT_UI_Implementation_Playbook.md v1.0`
**Owner posture:** This document assumes the role of Chief Architect / TPM / Principal Android Engineer / Senior Product Designer / EM for the remainder of playIT's development. It does not implement code — it tells Antigravity exactly what to implement, in what order, and how to know when to stop.

---

## 0. How to Read This Document

Every future Antigravity session starts here, not with the three source documents individually. This roadmap has already resolved:
- **What the three sources say**, merged into one execution order.
- **Where they disagree or overlap**, resolved explicitly (§0.3 below).
- **What "done" means** for every task, so no session has to guess.

Do not re-open `playIT_Master_Context_v1.0.md`, `engineering_review.md`, or `playIT_UI_Implementation_Playbook.md` to decide what to do next — this document already did that. Open them only if a task explicitly says "consult §X of [source]" for a detail too long to duplicate here (e.g., the full Design Token Quick Reference, or the full ERD).

### 0.1 Source Priority (when this roadmap's synthesis needs a tiebreak)

1. **Design System v1.0** (as encoded into Master Context §6 and the UI Playbook's Design Token Quick Reference)
2. **SRS v2.0 / SDD v1.0** (as encoded into Master Context §1–§9)
3. **Engineering Review (RC-1)** — authoritative on *actual, code-verified* defects and their fixes
4. **UI Implementation Playbook v1.0** — authoritative on UI/UX polish direction, but its own audit entries are explicitly self-described as **hypotheses grounded in spec, not confirmed code findings** ("Antigravity should confirm each violation against the real file before fixing it — some may already be correct"). Where a Playbook hypothesis and an Engineering Review finding describe the same code, **the Engineering Review wins** because it is based on an actual review pass; the Playbook task is then re-scoped to "verify and complete" rather than "implement from scratch."
5. **Master Context §8 (Repository B ideas)** — ideas only, never a spec; already fully triaged (Adopt/Adapt/Avoid) inside Master Context itself.

### 0.2 Reconciling Project State Across the Three Documents

This is the single most important synthesis judgment in this roadmap, so it is stated explicitly rather than left implicit:

- **Master Context v1.0** was written when repository state was **unverified** — its own §7 says "Implemented: None confirmed" and treats its Engineering Backlog (§12, T-01–T-15) as *future* work.
- **The Engineering Review (RC-1, dated 2026-07-21)** is a **real, code-level review** of a feature-complete build — MVVM implemented, all 12 screens present, Room schema wired, Vosk integrated, domain use cases tested. This means **Master Context §12's T-01 through T-14 are functionally complete** — the repository has moved from "planned" to "built, with defects" since Master Context was authored.
- **The UI Implementation Playbook** was written *on top of* that same premise — "this playbook proceeds on the instruction that core functionality is complete and verified" — and treats T-01 (verify actual repo state) as a prerequisite if that verification hasn't happened.

**Conclusion this roadmap acts on:** playIT is past the greenfield-implementation stage and inside the **release-candidate hardening stage**. Phase 0 below is a fast confirmation pass, not a rebuild of Master Context's original backlog. Antigravity should not re-implement T-01–T-14; it should fix what RC-1 review found wrong with them, then polish the UI layer on top, per Phases 1–14.

### 0.3 Known Cross-Document Overlaps (resolved here so no session re-litigates them)

| Overlap | Sources | Resolution |
|---|---|---|
| Color token centralization | Engineering Review **ISSUE-10** (color tokens duplicated across `SayItScreen`/`BlendItScreen`/`HearItScreen`/`PlayItLearningScaffold`) vs. UI Playbook **P-01** (Establish color token file) | Single task. Execute as **UI-3.01**, which subsumes ISSUE-10. Do not implement ISSUE-10 separately in Phase 2 — it is explicitly deferred to Phase 3 with a cross-reference, to avoid the same file being touched twice for the same reason. |
| `contentDescription` gaps | Engineering Review **ISSUE-12** (map nodes), **IMPROVEMENT-09** (heart row) vs. UI Playbook **P-40** (full screen-reader sweep) | Fix the two known, specific instances immediately in Phase 2 (**ENG-2.10**, **ENG-2.17**) since they're already diagnosed defects with a one-line fix. P-40 (**UI-8.04**) remains the comprehensive, later sweep that catches everything else across all 12 screens — it is not redundant, it is broader. |
| Hardcoded route strings | Engineering Review **IMPROVEMENT-03** vs. UI Playbook §4.1 Navigation audit | Single task, **ENG-2.11**, done once in Phase 2 (it's an engineering/maintainability concern, not a visual one — doing it before Phase 5's screen polish means every later screen task references clean route constants). |
| `activeHearts` nullability | Engineering Review **IMPROVEMENT-04** vs. UI Playbook `HearItScreen`/`PlayItLearningScaffold` audit (§3.5) | Single task, **ENG-2.12**, done in Phase 2 since it's a ViewModel/scaffold API shape change, not a visual one. Phase 5's `HearItScreen`/`PlayItLearningScaffold` polish tasks (**UI-5.05**) build on top of the corrected API. |
| Dual unlock logic (SQL + Kotlin) | Engineering Review **ISSUE-06** | Engineering-only; no UI Playbook overlap. Stays in Phase 2. |
| Reduced-motion mode | Master Context §6 (Design System requirement) vs. UI Playbook §4.3 (no dedicated Settings screen) vs. **P-37** (implementation task) | Confirmed: lives inside `ParentDashboardScreen`, DataStore-backed, not Room. No new screen. Implemented once, in Phase 8 (**UI-8.01**), consumed by every earlier animated component built in Phases 4/6/7 (each of those tasks stubs a `reducedMotion` parameter defaulted `false` until UI-8.01 lands, per the Playbook's own sequencing note). |
| Say It progression gating contract | Engineering Review **ISSUE-04** (`SayItViewModel` writes `isCompleted=false`) vs `FindItViewModel` gating | Resolved in **ENG-2.01**: Find It is the sole gate for lesson unlock; `SayItViewModel` records `SayItAttempt` data for analytics/dashboard reporting but does not perform dummy `lesson_progress` writes. |


---

## 1. Execution Philosophy (applies to every session, every task, no exceptions)

```
        ┌─────────┐
        │ Analyze │  Read this roadmap's current task card in full. Confirm the
        └────┬────┘  files it names still match the real project structure.
             ▼
        ┌─────────┐
        │  Plan   │  Restate the task's Acceptance Criteria before touching code.
        └────┬────┘  If a Ground Rule (§2) would be violated, stop and flag — do not improvise.
             ▼
        ┌───────────────────────┐
        │ Implement ONE task    │  Exactly one Task ID. Never bundle two IDs into
        │ (one logical unit)    │  one session, even if they look related.
        └────┬──────────────────┘
             ▼
        ┌─────────┐
        │  Build  │  ./gradlew assembleDebug (minimum). Must be green before proceeding.
        └────┬────┘
             ▼
        ┌───────────────┐
        │ Runtime Test  │  Execute the task's "Android Studio Runtime Tests" exactly
        └────┬──────────┘  as written. Do not skip because "it looks fine."
             ▼
        ┌─────────┐
        │ Review  │  Run the task's Acceptance Criteria and Verification Checklist
        └────┬────┘  against what was actually built. Use Claude Review Mode (§8) here.
             ▼
        ┌───────┐
        │  Fix  │  Address anything Review flags before committing. Do not commit
        └───┬───┘  with known Critical/High findings open.
             ▼
        ┌────────┐
        │ Commit │  Use the task's exact Git Commit Message (or its documented variant).
        └────┬───┘  One task = one commit. No bundled commits.
             ▼
          Repeat → return to Analyze for the next Task ID in sequence.
```

**Never perform multiple unrelated improvements in one implementation cycle.** If, while implementing Task ID `X`, an unrelated defect in Task ID `Y` is discovered, note it in the commit message body as "Observed but not fixed (see Task Y)" and leave it for its own cycle. This applies even when the fix looks trivial — trivial fixes bundled into unrelated commits are exactly how regression risk and unreviewable diffs accumulate.

---

## 2. Ground Rules (binding on every phase, every task)

These are inherited directly from Master Context §11 and UI Playbook §0, merged into one list. They override any instinct to "improve while you're in there."

1. **No architectural redesign.** MVVM + Clean Architecture (Presentation / Domain / Data, per Master Context §5) stays exactly as specified, for the entire roadmap — including all UI phases.
2. **No feature redesign.** The 12-screen inventory (§3 below), the gamification math, the unlock sequence, and the heart/star rules are frozen. UI phases change how a screen looks, moves, and feels — never what it does.
3. **No Room schema changes** outside Phase 1/2 tasks that explicitly call for one (e.g., **ENG-2.06**'s unique constraint). No UI-phase task ever touches an entity or DAO.
4. **No repository interface/implementation changes** outside explicitly scoped Phase 1/2 engineering tasks.
5. **ViewModels are touched only when a task explicitly requires new state exposure** (e.g., exposing `PlaybackState`, exposing `reducedMotionEnabled`). Never add or change business logic inside a ViewModel under a UI-phase task.
6. **The Design System is non-negotiable.** Every UI task is pre-checked against Design System v1.0 tokens. Where an existing screen conflicts with a token, log it as a **Design System Violation** in the commit body and correct it within that task's scope only.
7. **Shared composables stay shared.** `HeartDisplay`, `MascotBubble`, `SubLevelProgressBar`, `AudioPlayer` (per Master Context §10) are each used across 2+ modules. Any task touching one of these edits the single shared composable — never a per-screen fork.
8. **Reduced-motion mode is a Design System requirement, not a new feature** (§0.3 above). No new screen is created for it.
9. **No invented Settings or Home screen.** `MapScreen` is the de facto Home; `ParentDashboardScreen` is the de facto Settings surface. Nothing in this roadmap proposes a 13th screen.
10. **Never invent quality/completion metrics.** Every status statement in this roadmap (including the Engineering Dashboard, §9) is grounded in the reviewed evidence in the three source documents, or explicitly marked as an estimate pending re-verification. Do not carry forward or fabricate scores.
11. **One task, one commit, then stop for review** — this is Antigravity's default operating mode for this entire roadmap, not just the UI phases.
12. **Conventional Commits style** (`feat:`, `fix:`, `chore:`, `test:`, `docs:`, `refactor:`, `style:`, `perf:`) — every commit message in this roadmap already follows this; do not deviate.
13. **When a new conflict or ambiguity is discovered mid-implementation**, resolve it using §0.1's priority order and log it as a new row appended to §0.3's table — don't silently pick a behavior and move on.

---

## 3. Canonical Reference (do not alter — reproduced here so sessions don't need to re-open Master Context)

**12 Screens:** `SplashScreen` → `ProfileSelectScreen` → `NamePromptScreen` → `MapScreen` (Home) → `HearItScreen` / `SayItScreen` / `FindItScreen` → `LetterCompleteScreen` → `BlendItScreen` → `BlendItCompleteScreen` → `ParentDashboardScreen` (Settings) → `ReportPreviewScreen`.

**Design tokens (full quick reference — consult Master Context §6 or UI Playbook §2 only if a task needs more detail than this):**
- **Color:** Learning Blue `#4A90E2` · Growth Green `#4CAF50` · Achievement Gold `#FFC107` · Energy Orange `#FF9800` · Friendly Purple `#8E7DF2` · Soft Sky `#EAF6FF` · Cream White `#FFFDF8` · Gentle Correction Orange `#FFB74D` (never harsh red, never flashing) · Text Primary `#2D3748` · Text Secondary `#718096` · Border `#E2E8F0` · Disabled `#CBD5E0`.
- **Type:** Nunito primary / Poppins fallback. Display Large 40sp ExtraBold · Heading 28sp Bold · Subheading 22sp SemiBold · Body 18sp Medium · Caption 16sp Regular. Never below 16sp.
- **Spacing (base 8dp):** 4/8/16/24/32/48/64dp.
- **Touch targets:** 48dp min / 56dp recommended / 64dp+ primary actions.
- **Animation:** micro 150–250ms · standard 300–500ms · celebration 600–1200ms. Tap feedback 100%→92%→100%. No flashing, ever.
- **Sound:** correct→chime · incorrect→soft pop · heart loss→whoosh · heart recovery→sparkle · node unlock→magical chime · level complete→fanfare.
- **Mascot states:** Happy / Excited / Thinking / Encouraging / Celebrating. Every line is text **and** audio. Banned phrases: "Wrong!", "You lost a heart."
- **Accessibility:** text+audio always · 48dp min targets · never color-only · contrast ≥4.5:1 (≥7:1 Parent Dashboard) · reduced-motion mode · one primary goal/CTA per screen.

---

## 4. Phase Map (execute strictly top to bottom)

| Phase | Name | Task Count | Source | Gate Before Entry |
|---|---|---|---|---|
| 0 | Repository Health Check & Verification | 3 | Master Context §7/§12 T-01 | None — entry point |
| 1 | Critical Engineering Fixes | 5 | Engineering Review — GA-blocking per Final Verdict | Phase 0 green |
| 2 | High-Priority Engineering Improvements | 18 | Engineering Review — remaining findings | Phase 1 green |
| 3 | Design Token & Theme Foundation | 6 | UI Playbook Phase 1 (P-01–P-06) | Phase 2 green |
| 4 | Component Library Standardization | 11 | UI Playbook Phase 2 (P-07–P-17) | Phase 3 green |
| 5 | Screen-by-Screen UI Refinement | 12 | UI Playbook Phase 3 (P-18–P-29) | Phase 4 green |
| 6 | Gamification & Reward Polish | 3 | UI Playbook Phase 4 (P-30–P-32) | Phase 5 green |
| 7 | Animation & Motion | 4 | UI Playbook Phase 5 (P-33–P-36) | Phase 6 green |
| 8 | Accessibility Improvements | 4 | UI Playbook Phase 6 (P-37–P-40) | Phase 7 green |
| 9 | Microinteractions & Final UI QA | 3 | UI Playbook Phase 7 (P-41–P-43) | Phase 8 green |
| 10 | Testing & Quality Assurance | 6 | Master Context §12 T-14 + Engineering Review §6 + UI Playbook Phase 8 (P-44–P-47) | Phase 9 green |
| 11 | Codebase Cleanup & Build Hardening | 5 | Engineering Review §10 + Master Context §12 T-15 (optional) | Phase 10 green |
| 12 | Deployment Preparation | 1 checklist | Master Context §13 + user's Deployment Roadmap ask | Phase 11 green |
| 13 | Portfolio Preparation | 1 checklist | Master Context §13 | Phase 12 green |
| 14 | Production Release | 1 checklist | All prior phases | Phase 13 green |

**Total: 80 discrete engineering/UI Task IDs across Phases 0–11, plus 23 checklist items across Phases 12–14 (103 actionable items total)**, sequenced so nothing is ever ambiguous about what comes next. Total UI Playbook prompt coverage: all 47 (P-01–P-47) are mapped 1:1 into Phases 3–10 below; none are dropped, renumbered arbitrarily, or reordered relative to each other — only regrouped under the Phase headers this roadmap uses.

---

## 5. PHASE 0 — Repository Health Check & Verification

**Purpose:** Confirm, with real tooling output (not folder-listing inference), that the codebase the Engineering Review examined is the codebase Antigravity is now working in, before spending any effort on fixes.

**Objectives:**
- Re-run Master Context §12 **T-01** in its fast form — this is a confirmation pass, not a rebuild, because RC-1's Engineering Review already constitutes a real code-level review.
- Establish a clean, known-green baseline build before Phase 1 touches anything.
- Confirm the 9 blocking bugs and 18 additional findings from the Engineering Review still exist at their cited file/line locations (code may have drifted since the review's date).

**Expected Deliverables:** A short `docs/repo-health-check.md` note (or equivalent commit message record) confirming build status, test status, and a line-by-line spot-check of the Engineering Review's cited locations.

**Dependencies:** None — this is the entry point.

**Risks:** If the codebase has drifted materially from what RC-1 reviewed (e.g., partial fixes already applied), Phase 1 task cards below may reference stale line numbers. This phase exists specifically to catch that before it wastes a cycle.

**Definition of Done:** Clean `./gradlew assembleDebug` and `./gradlew test` baseline established; every Phase 1 task's "Files likely affected" confirmed to still exist and roughly match.

**Estimated Complexity:** Low. **Priority:** P0 (blocks everything).

### Task ENG-0.01 — Clean baseline build and test run
- **Objective:** Establish a green baseline before any fix work begins.
- **Why it matters:** Every later phase's Definition of Done assumes "still builds green" as a delta from a known-good state. Without this, a broken build in Phase 3 can't be attributed to Phase 3's own change.
- **Files likely affected:** None (verification only).
- **Implementation Rules:** Run `./gradlew clean assembleDebug` and `./gradlew test`. Record pass/fail per module. Do not fix anything found here — just record it.
- **Acceptance Criteria:** Build and existing test suite results are recorded, whether green or not.
- **Verification Checklist:** Build output archived in the session's commit-message-equivalent notes; no code changed.
- **Android Studio Runtime Tests:** Launch the app on an emulator (API 26 min-SDK target, plus one modern API level) and confirm it reaches `ProfileSelectScreen` without crashing.
- **Git Commit Message:** `docs: record RC-1 baseline build and test status`
- **Estimated Difficulty:** Low. **Expected Duration:** 0.5h.
- **Stop Condition:** Stop immediately after recording results — do not attempt fixes even if the build is red; escalate a red baseline before proceeding to Phase 1.

### Task ENG-0.02 — Spot-check Engineering Review citations against current source
- **Objective:** Confirm the file/line locations cited for all 9 blocking bugs (§11 of the Engineering Review) still match current source.
- **Why it matters:** Phase 1 task cards below quote exact locations (e.g., "line 147") from the RC-1 review. If the file has changed since 2026-07-21, those references need a one-time correction before Phase 1 starts, not mid-fix.
- **Files likely affected:** `ProfileViewModel.kt`, `PlayItDatabase.kt`, `BlendItViewModel.kt`, `FindItViewModel.kt`, `PhonemeAudioPlayer.kt`, `SayItViewModel.kt`, `ProgressReportPdfGenerator.kt`, `app/build.gradle.kts`.
- **Implementation Rules:** Open each cited file, confirm the described defect is still present in roughly the described location. Do not fix — only confirm or flag drift.
- **Acceptance Criteria:** Each of the 9 blocking bugs is confirmed present, or explicitly flagged as "already fixed" / "location moved to X" in session notes.
- **Verification Checklist:** All 9 locations checked; discrepancies noted for Phase 1 task cards to be manually adjusted before execution.
- **Android Studio Runtime Tests:** None (static read-only check).
- **Git Commit Message:** `docs: confirm RC-1 defect locations against current source` (only if notes are committed; otherwise this is a no-diff session).
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once all 9 are confirmed or flagged — do not begin fixing in this task even if the fix looks trivial.

### Task ENG-0.03 — Confirm Master Context T-01–T-14 completion status
- **Objective:** Formally close out Master Context §12's original backlog (T-01–T-14) as "built, RC-1 quality" rather than leaving those rows in an ambiguous "planned" state that could confuse a future session re-reading Master Context directly.
- **Why it matters:** Prevents a future Antigravity session from re-implementing something that already exists, per §0.2 above.
- **Files likely affected:** None (documentation/status only).
- **Implementation Rules:** Cross-reference each of T-01–T-14 against what the Engineering Review actually examined (architecture, data, domain, UI, audio, testing, security, performance, build config, accessibility all reviewed = T-01 through T-13 all have real, built counterparts; T-14's testing is **partially** done — see Engineering Review §6 coverage gaps, carried forward to Phase 10).
- **Acceptance Criteria:** A short status line per T-01–T-14 recorded (Done / Done-with-defects-see-Phase-1-2 / Partially done-see-Phase-10).
- **Verification Checklist:** All 14 rows accounted for.
- **Android Studio Runtime Tests:** None.
- **Git Commit Message:** `docs: close out Master Context T-01-T-14 against RC-1 verified state`
- **Estimated Difficulty:** Low. **Expected Duration:** 0.5h.
- **Stop Condition:** Stop after status is recorded — this task never touches code.

**Quality Gate 0 → 1:** [x] Baseline build recorded [x] All 9 blocking-bug locations confirmed or flagged [x] T-01–T-14 status recorded [x] Git commit(s) made.
---

## 6. PHASE 1 — Critical Engineering Fixes (GA-Blocking)

**Purpose:** Resolve exactly the 5 defects the Engineering Review's Final Verdict names as blocking approval: *"Approve after resolving: BUG-04, BUG-05, BUG-06, BUILD-01, and ISSUE-02."* Nothing else is in scope for this phase.

**Objectives:** Zero data loss on schema change. Zero silent progress-save failure in Blend It. Zero unobfuscated release APK. Zero session-state loss on process death.

**Expected Deliverables:** 5 committed fixes, each independently buildable and testable.

**Dependencies:** Phase 0 complete.

**Risks:** BUG-06 and BUG-04 both touch data persistence — regression here can silently corrupt or lose child progress data, which is the single worst possible outcome for this app's user base. Test thoroughly before commit, not just "builds green."

**Definition of Done:** All 5 tasks committed; app installable as a debug build with `fallbackToDestructiveMigration()` or real migrations in place, ProGuard-enabled release build compiles, Blend It progress verifiably persists, and profile session survives process death.

**Estimated Complexity:** Medium-High (BUG-06 and BUILD-01 carry real risk). **Priority:** P0.

### Task ENG-1.01 — Fix BlendItViewModel.completeSession() groupId corruption
- **Objective:** Stop `completeSession()` from writing the raw `phonemeId` (e.g., `"BLEND_1"`) into `BlendItProgress.groupId`, which violates the FK constraint against `letter_groups.groupId` (which stores `"1"`, `"2"`, etc.) and causes the upsert to fail silently.
- **Why it matters:** This is the single most severe defect in the review — Blend It progress and the "Word Blender" achievement **never save**, for every user, silently. A feature-complete module is functionally broken end-to-end.
- **Files likely affected:** `BlendItViewModel.kt` (line ~147, inside `completeSession()`).
- **Implementation Rules:** Apply the same `phonemeId.replace("BLEND_", "")` transform already used correctly in `loadWordsForPhoneme` to the `groupId` value passed into `upsertBlendItProgress`. Do not touch any other logic in `completeSession()`. Do not change the `BlendItProgress` entity or DAO.
- **Acceptance Criteria:** `upsertBlendItProgress` receives a `groupId` matching `letter_groups.groupId` format (e.g., `"1"`, not `"BLEND_1"`); the FK constraint no longer silently rejects the write; "Word Blender" achievement and streak update fire correctly on session completion.
- **Verification Checklist:** ☐ Code diff limited to the groupId transform ☐ Build green ☐ Room FK constraint no longer violated (confirm via logcat — no `SQLiteConstraintException` on session complete).
- **Android Studio Runtime Tests:** Complete a full Blend It session for a test profile; verify (via Room DB inspector in Android Studio, or the Parent Dashboard) that `BlendItProgress` now has a row with `isCompleted = true` and the group's letters show as progressed. Repeat for at least 2 different groups to confirm the transform isn't group-1-specific.
- **Git Commit Message:** `fix: strip BLEND_ prefix from groupId in BlendItViewModel.completeSession()`
- **Estimated Difficulty:** Low (one-line fix, high-value). **Expected Duration:** 0.5–1h including verification.
- **Stop Condition:** Stop once the DB-inspector check confirms a real, persisted row for 2+ groups. Do not proceed to touch `loadWordsForPhoneme` again — it's already correct.

### Task ENG-1.02 — Enable release-build minification and ProGuard rules
- **Objective:** Set `isMinifyEnabled = true` for the release build variant and add the necessary keep rules so Vosk, Room, and Compose survive obfuscation.
- **Why it matters:** The current release APK ships fully unobfuscated — every class, method, and reflection-accessible internal is exposed. For a children's app that stores profile data locally, this is a privacy/security exposure with no upside, and it's a one-line-plus-rules fix.
- **Files likely affected:** `app/build.gradle.kts` (`buildTypes.release`), `app/proguard-rules.pro`.
- **Implementation Rules:** Set `isMinifyEnabled = true` and `isShrinkResources = true` on `release`. Add `-keep class org.vosk.** { *; }` (Vosk reflection-heavy JNI bridge), plus standard Room/Compose keep rules (Room's own consumer ProGuard rules generally suffice, but verify no `NoSuchMethodError` at runtime post-shrink). Do not enable minification on `debug`.
- **Acceptance Criteria:** Release build variant compiles with minification on; app installs and runs through a full session (Splash → Map → Hear It → Say It → Find It → Complete) on a release build without a crash attributable to over-aggressive shrinking.
- **Verification Checklist:** ☐ `./gradlew assembleRelease` succeeds ☐ APK size decreases (evidence minification actually ran) ☐ Vosk speech recognition still functions in Say It on the release build specifically (this is the highest-risk shrink target since it's JNI/reflection-based).
- **Android Studio Runtime Tests:** Install the **release** APK (not debug) on a physical device or emulator; complete one full Hear It → Say It → Find It cycle, confirming Vosk still recognizes speech correctly (a false negative here — Vosk silently failing post-shrink — is the main regression risk of this task).
- **Git Commit Message:** `build: enable release minification with Vosk/Room/Compose ProGuard rules`
- **Estimated Difficulty:** Medium (risk is in verification, not the config change itself). **Expected Duration:** 2–3h including full release-build runtime testing.
- **Stop Condition:** Stop once a release-build Say It session successfully recognizes at least one correct and one incorrect utterance. If Vosk fails on the release build, do not ship this commit — add a more targeted keep rule and retest before committing, rather than committing a broken release config.

### Task ENG-1.03 — Add Room migration strategy
- **Objective:** Prevent data loss on any future schema change by adding `fallbackToDestructiveMigration()` now (pre-release safety net) and documenting the path to real migration scripts before true GA.
- **Why it matters:** `version = 1` with no migration path means any future schema change (even adding one column) crashes with "Room cannot verify the data integrity" and destroys all local child progress data — the app's only copy of that data, since it's offline-first by design.
- **Files likely affected:** `PlayItDatabase.kt` (`Room.databaseBuilder(...)` call site).
- **Implementation Rules:** Add `.fallbackToDestructiveMigration()` to the builder chain immediately (RC-safe minimum). Add a code comment flagging that before true GA, this must be replaced with explicit `Migration` objects once the schema is expected to change again, and set `exportSchema = true` (ties to **ENG-2.14** in Phase 2 / codebase cleanup) so future migrations have a schema baseline to diff against.
- **Acceptance Criteria:** Builder chain includes the fallback; a forced schema-version bump in a local test build no longer crashes on open (destructive fallback recreates the DB instead).
- **Verification Checklist:** ☐ Build green ☐ Manual test: bump `version` by 1 locally, reinstall over existing data, confirm app opens without a crash (data loss is expected/acceptable at this fallback tier — that's the point, no crash).
- **Android Studio Runtime Tests:** Install app, create a profile, force-bump the schema version in a local branch, reinstall, confirm clean re-seed rather than a crash. Revert the local version bump before committing.
- **Git Commit Message:** `fix: add fallbackToDestructiveMigration to prevent crash-on-schema-change`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once the forced-bump test confirms no crash. Do not write real `Migration` objects in this task — that's explicitly deferred (see Phase 11 note on `exportSchema`).

### Task ENG-1.04 — Fix database seed race condition
- **Objective:** Eliminate the race where `DatabaseCallback.onCreate` seeds the database on a raw `CoroutineScope(Dispatchers.IO).launch { ... }`, which can allow a second `getInstance()` caller to query an empty, not-yet-seeded database.
- **Why it matters:** On fresh install, if `repository` is accessed immediately after `database` in `PlayItApplication`, a query can race the seed coroutine and return empty results — surfacing as "no phonemes found" or a blank Map on first launch, intermittently and hard to reproduce.
- **Files likely affected:** `PlayItDatabase.kt` (the `RoomDatabase.Callback`), `PlayItApplication.kt` (database/repository initialization order).
- **Implementation Rules:** Replace the raw `CoroutineScope(Dispatchers.IO).launch {}` seed call with a `withContext(Dispatchers.IO)` block executed synchronously as part of database creation (e.g., via `database.openHelper.writableDatabase` accessed before `PlayItApplication` releases its lazy-init lock), or gate `repository`'s first real access behind a `suspend fun ensureSeeded()` guard that awaits seed completion. Do not change the seed data itself.
- **Acceptance Criteria:** Repeated fresh-install tests (cold install → immediate first query) never return an empty phoneme/group list.
- **Verification Checklist:** ☐ Build green ☐ 5 consecutive fresh-install-and-immediately-open test cycles show correctly seeded data every time (uninstall fully between each, not just clear data, to guarantee cold `onCreate`).
- **Android Studio Runtime Tests:** Uninstall app completely, reinstall, immediately navigate to `MapScreen` and confirm all 28 letter nodes render (not a blank/partial map). Repeat 5×.
- **Git Commit Message:** `fix: eliminate database seed race condition on fresh install`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h including repeated fresh-install verification.
- **Stop Condition:** Stop once 5/5 fresh-install cycles pass. If any cycle still shows a partial map, the race isn't fully closed — do not commit a partial fix.

### Task ENG-1.05 — Persist activeProfileId via DataStore
- **Objective:** Replace the fragile in-memory `SessionManager` `object` singleton's storage of `activeProfileId` with a `DataStore<Preferences>`-backed value, so it survives process death.
- **Why it matters:** If the OS reclaims the Activity under low memory and the user re-enters via a deep link, notification, or task-switcher resume, `activeProfileId` silently reverts to `-1L`, causing every subsequent query to return empty or crash — with no visible error explaining why.
- **Files likely affected:** `data/preferences/SessionManager.kt`.
- **Implementation Rules:** Back `activeProfileId` with `DataStore<Preferences>`, read it on cold start before any profile-scoped query runs. Keep `SessionManager`'s existing public API surface unchanged (per the review's own recommendation) so no call site elsewhere in the app needs to change — this is an internal storage swap, not an API change. Do not touch `BlendItViewModel`'s unrelated `ISSUE-03` boundary violation in this task (that's **ENG-2.13**, its own task).
- **Acceptance Criteria:** `activeProfileId` correctly survives a simulated process death (Android Studio's "Kill process" during a backgrounded session) and resumes with the correct profile scoped, not `-1L`.
- **Verification Checklist:** ☐ Build green ☐ `SessionManager`'s public method signatures unchanged (confirm no other file needed a call-site edit) ☐ Process-death test passes.
- **Android Studio Runtime Tests:** Select a profile, background the app, use Android Studio's "Terminate Application" (or `adb shell am kill`) to simulate low-memory reclaim (not just Home-button backgrounding, which doesn't kill the process), relaunch via the launcher icon, and confirm the correct profile's Map/progress loads rather than reverting to `-1L`/profile-select.
- **Git Commit Message:** `fix: persist activeProfileId via DataStore to survive process death`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the process-death test confirms correct profile restoration. Do not migrate any other `SessionManager`-adjacent state in this task — scope is `activeProfileId` only.

**Quality Gate 1 → 2:** [x] All 5 tasks committed individually [x] `./gradlew assembleRelease` succeeds with minification on [x] Full manual regression walkthrough passes [x] Engineering Review self-check: re-read the Final Verdict paragraph and confirm all 5 named blockers are closed.

---

## 7. PHASE 2 — High-Priority Engineering Improvements

**Purpose:** Close every remaining Engineering Review finding not required for GA approval but explicitly flagged as "may be addressed in the UI polish phase" — doing them *before* Phase 3–10's UI work, since several are prerequisites for clean UI-layer code (e.g., §0.3's cross-references) and none require a design pass to fix.

**Objectives:** Zero remaining architecture-boundary violations, zero remaining silent-failure risks, zero remaining performance anti-patterns, test coverage gaps closed where trivial.

**Expected Deliverables:** 18 committed fixes.

**Dependencies:** Phase 1 complete and gated.

**Risks:** ENG-2.05 (Say It progression contract) requires a product decision, not just a code fix — flagged explicitly below; don't let it block the rest of the phase.

**Definition of Done:** All 18 tasks committed; no open Engineering Review finding remains uncategorized (either fixed here, or explicitly deferred to a named later phase per §0.3).

**Estimated Complexity:** Medium (mostly small, isolated fixes; ENG-2.05 and ENG-2.04 carry the most judgment). **Priority:** P1.

*(Ordered per the Engineering Review's own §12 priority list first, then remaining findings in review-document order.)*

### Task ENG-2.01 — Clarify and document Say It progression contract
- **Objective:** Resolve whether `SayItViewModel` is meant to gate progression (write `isCompleted = true`) or not — currently it always writes `isCompleted = false`/`starsEarned = 0`, meaning only `FindItViewModel` actually gates unlock via `getUnlockedPhonemes`.
- **Why it matters:** This is an undocumented design ambiguity, not a clear bug — it needs a product decision (does Say It gate progression or not?) before a "fix" can be correct rather than merely different.
- **Files likely affected:** `SayItViewModel.kt`.
- **Implementation Rules:** **This task requires a decision, not just code.** Per Master Context §11's conflict-resolution rule, resolve using source priority: if SRS/SDD are silent on whether Say It individually gates, treat "only Find It gates, per the observed dual-gate pattern where only FindItViewModel sets isCompleted=true" as the resolved behavior (matches current dominant pattern), and make `SayItViewModel` **stop writing to `lesson_progress` entirely** if Say It isn't meant to gate — writing incomplete rows there is otherwise pure noise. Document the decision as a code comment at the top of `SayItViewModel`, and add a row to §0.3 of this roadmap recording the resolution.
- **Acceptance Criteria:** `SayItViewModel`'s relationship to `lesson_progress`/unlock gating is unambiguous in code and documented; no dead/misleading writes remain.
- **Verification Checklist:** ☐ Build green ☐ Comment added explaining the resolved contract ☐ Unlock behavior re-tested end-to-end (Hear It → Say It → Find It → letter unlock) to confirm no regression from removing/adjusting the write.
- **Android Studio Runtime Tests:** Complete a full letter cycle and confirm the letter still unlocks correctly afterward exactly as before this change.
- **Git Commit Message:** `fix: clarify and document Say It progression contract (Find It gates, Say It does not)`
- **Estimated Difficulty:** Medium (judgment call). **Expected Duration:** 1.5h.
- **Stop Condition:** If the decision genuinely can't be resolved from available source priority (i.e., it's a true open product question), stop, log it explicitly as an open question in §0.3, and escalate to a human product decision rather than guessing.

### Task ENG-2.02 — Gate map node breathing animation to active node only
*(= Engineering Review PERFORMANCE-01)*
- **Objective:** Stop `rememberInfiniteTransition` from running on all ~35 map nodes simultaneously; hoist it to `MapScreen` level and pass a computed `scale` down only to the active node.
- **Why it matters:** Every node currently schedules a continuous animation frame loop regardless of lock/active state — a real, measurable performance cost that scales with map size.
- **Files likely affected:** `MapScreen.kt`, `LetterNode.kt`/`MapNode.kt`, `BlendItChallengeNode.kt`.
- **Implementation Rules:** Hoist `infiniteTransition` to `MapScreen`; pass computed `scale = 1f` to all non-active nodes; only the single `isActiveNode == true` node receives the animated value. Do not remove the breathing-pulse effect itself — that's a Design-System-mandated signature moment (this is a performance fix, not a feature removal), and its final polish is Phase 7's **UI-7.03**.
- **Acceptance Criteria:** Only one node animates at a time; all others render statically at `scale = 1f`.
- **Verification Checklist:** ☐ Build green ☐ Profiler/frame-timing check on Map with all 35 nodes rendered shows no continuous animation cost from locked/inactive nodes.
- **Android Studio Runtime Tests:** Open Map with a partially-progressed profile (mix of locked/unlocked/completed/active nodes); use Android Studio's Layout Inspector or a frame-timing tool to confirm only the active node is animating.
- **Git Commit Message:** `perf: hoist breathing-pulse animation to active map node only`
- **Estimated Difficulty:** Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once profiling confirms single-node animation. Full animation *polish* (timing/easing) is out of scope here — that's Phase 7.

### Task ENG-2.03 — Replace N+1 phoneme progress query in ParentViewModel
*(= Engineering Review PERFORMANCE-02)*
- **Objective:** Replace `ParentViewModel.loadData()`'s per-phoneme query loop (up to 28 queries every time the phoneme list changes) with a single `Flow`-based query.
- **Why it matters:** N+1 query patterns scale badly and add real latency/battery cost on the one screen (Dashboard) that needs to feel fast and reliable for a parent audience.
- **Files likely affected:** `ParentViewModel.kt`, `ProgressDao.kt`.
- **Implementation Rules:** Add `getCompletedLessonsForProfile(profileId): Flow<List<LessonProgress>>` to `ProgressDao` (convert the existing `suspend` version to a `Flow`-returning one, per the review's own recommendation). Replace the per-phoneme loop in `loadData()` with a single collection of this new query.
- **Acceptance Criteria:** Dashboard load issues 1 query for lesson progress, not up to 28.
- **Verification Checklist:** ☐ Build green ☐ Query count verified via Room's query logging or a manual count of DAO calls during a Dashboard load.
- **Android Studio Runtime Tests:** Open Parent Dashboard for a profile with progress across multiple letters; confirm data renders correctly (no regression in what's displayed) and load feels immediate.
- **Git Commit Message:** `perf: replace N+1 phoneme progress query with single Flow query`
- **Estimated Difficulty:** Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once Dashboard renders identical data via the new single-query path.

### Task ENG-2.04 — Complete pronunciation fallback map for all 28 phonemes
*(= Engineering Review ISSUE-07)*
- **Objective:** Move the hardcoded 3-phoneme pronunciation fallback map out of `SayItViewModel.evaluateSpeech()` into a complete, 28-phoneme `PhonemePronunciationMap` domain constant.
- **Why it matters:** Currently a child saying a valid phonetic variant for any of the other 25 phonemes will always fail recognition — this silently breaks Say It for the majority of the curriculum.
- **Files likely affected:** New `domain/usecase/PhonemePronunciationMap.kt`; `SayItViewModel.kt`; `SpeechValidator.kt` (injection point).
- **Implementation Rules:** Define the complete map (all 28 Marungko phonemes, each with realistic phonetic fallback variants, following the existing pattern e.g. `"m" to listOf("m", "em", "um", "am")`) as a `domain/usecase` constant — this is content/data work requiring correct phonetic knowledge of the Marungko sequence, not architecture work; flag to a human reviewer for phonetic accuracy if uncertain about any entry rather than guessing. Inject into `SpeechValidator` rather than hardcoding in the ViewModel.
- **Acceptance Criteria:** All 28 phonemes have at least 2 phonetic fallback variants; `SayItViewModel` no longer contains a hardcoded map.
- **Verification Checklist:** ☐ Build green ☐ All 28 letters spot-checked in `SpeechValidatorTest.kt` coverage (extend the existing test, don't replace it) ☐ Manual Say It test for at least 5 phonemes outside the original 3 (`m`, `s`, `a`) confirms recognition now succeeds for a valid variant pronunciation.
- **Android Studio Runtime Tests:** Run Say It for letters `t`, `n`, `r`, `f`, `q` (a spread across the 7 groups) and confirm the mic correctly accepts a valid phonetic variant, not just the exact base phoneme.
- **Git Commit Message:** `feat: complete pronunciation fallback map for all 28 Marungko phonemes`
- **Estimated Difficulty:** Medium-High (content accuracy matters). **Expected Duration:** 3h.
- **Stop Condition:** If phonetic accuracy for any specific phoneme is genuinely uncertain, flag it in the commit body as "needs phonetic review" rather than guessing and shipping a wrong fallback.

### Task ENG-2.05 — [Deferred to Phase 3] Centralize design token colors
*(= Engineering Review ISSUE-10 — see §0.3)*
- **Status:** **Not executed here.** This finding is functionally identical to Phase 3's **UI-3.01** (Establish color token file). Executing it twice would mean touching `SayItScreen.kt`/`BlendItScreen.kt`/`HearItScreen.kt`/`PlayItLearningScaffold.kt` in two separate, overlapping commits.
- **Action for this task slot:** Add a one-line note to the Phase 3 gate confirming ISSUE-10 is considered resolved once **UI-3.01** lands and its Acceptance Criteria ("no other file in the project defines a color literal outside `Color.kt`") passes.
- **Stop Condition:** Do not implement a separate color-centralization commit here — proceed directly to ENG-2.06.

### Task ENG-2.06 — Add contentDescription to map nodes
*(= Engineering Review ISSUE-12, first half)*
- **Objective:** Add `contentDescription = "${node.label} - ${if (node.isUnlocked) "unlocked" else "locked"}"` to `LetterNode` and `BlendItChallengeNode`.
- **Why it matters:** Screen readers currently announce these as generic "clickable" with no context — a hard accessibility gap on the app's primary navigation surface.
- **Files likely affected:** `LetterNode.kt`/`MapNode.kt`, `BlendItChallengeNode.kt`.
- **Implementation Rules:** Add the exact contentDescription pattern above to both node types. Leave decorative star icons (`contentDescription = null`) as-is per the review's own note that this is acceptable — but confirm earned star *count* is still conveyed some other way (verbally via the node's own description, e.g., append star count to the label).
- **Acceptance Criteria:** TalkBack announces node label + lock state + star count for every map node.
- **Verification Checklist:** ☐ Build green ☐ TalkBack manual walkthrough of Map confirms all node types announce correctly.
- **Android Studio Runtime Tests:** Enable TalkBack, navigate Map screen, confirm each node type (locked/unlocked/completed/active, letter and Blend-It checkpoint) announces meaningfully.
- **Git Commit Message:** `fix: add contentDescription to map letter and Blend-It nodes`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once TalkBack confirms all node types on the map announce correctly.

### Task ENG-2.07 — Move LetterCard to domain/model
*(= Engineering Review ISSUE-08)*
- **Objective:** Move the `LetterCard` data class out of `domain/usecase/SpellingEngine.kt` into `domain/model/Models.kt`, alongside `BlendItWord`, `Profile`, etc.
- **Why it matters:** A data model living inside a use-case file is a real Clean Architecture boundary violation — `BlendItUiState` in the UI layer currently imports from `domain/usecase` just to get a data class, which is backwards.
- **Files likely affected:** `domain/usecase/SpellingEngine.kt`, `domain/model/Models.kt`, any file importing `LetterCard` (update import paths only).
- **Implementation Rules:** Move the class definition; update all import statements; do not change `LetterCard`'s fields or behavior.
- **Acceptance Criteria:** `LetterCard` lives in `domain/model/Models.kt`; `SpellingEngine.kt` contains only use-case logic; all call sites compile with updated imports.
- **Verification Checklist:** ☐ Build green ☐ No remaining reference to `LetterCard` inside `domain/usecase`.
- **Android Studio Runtime Tests:** Run Blend It end-to-end once to confirm no runtime regression from the move (this is a pure refactor, so a single smoke test suffices).
- **Git Commit Message:** `refactor: move LetterCard from domain/usecase to domain/model`
- **Estimated Difficulty:** Low. **Expected Duration:** 0.5h.
- **Stop Condition:** Stop once build is green and one Blend It smoke test passes.

### Task ENG-2.08 — Add unique constraint on BlendItWordEntity
*(= Engineering Review IMPROVEMENT-01)*
- **Objective:** Add `indices = [Index(value = ["groupId", "word"], unique = true)]` to `BlendItWordEntity` to prevent duplicate spelling cards on re-seed/migration.
- **Why it matters:** Without this, a re-seed or migration can silently insert duplicate words for a group, corrupting the Blend It word pool.
- **Files likely affected:** `BlendItWordEntity.kt`.
- **Implementation Rules:** Add the index annotation exactly as specified. This is a schema change — confirm it doesn't conflict with **ENG-1.03**'s `fallbackToDestructiveMigration()` (it won't, since that fallback already handles any schema delta by destructive recreation at this RC stage).
- **Acceptance Criteria:** Duplicate `(groupId, word)` inserts now fail/are ignored per Room's `OnConflictStrategy`, rather than silently succeeding.
- **Verification Checklist:** ☐ Build green ☐ Manual test: attempt to insert a duplicate word for an existing group in a debug build, confirm it's rejected/ignored rather than duplicated.
- **Android Studio Runtime Tests:** Use Android Studio's Database Inspector to confirm no duplicate `(groupId, word)` rows exist post-fresh-seed, and that a forced duplicate insert attempt doesn't create a second row.
- **Git Commit Message:** `fix: add unique constraint on BlendItWordEntity(groupId, word)`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once the Database Inspector confirms no duplicates and the constraint is enforced.

### Task ENG-2.09 — Add FindItViewModel unit test with MainDispatcherRule
*(= Engineering Review IMPROVEMENT-06)*
- **Objective:** Add a `MainDispatcherRule` test utility and at least one `FindItViewModel` test covering the `onCardTapped` happy path and the heart-reset-to-3 edge case.
- **Why it matters:** The heart-reset-to-3 (not 0) behavior on failure is a non-obvious business rule with no current test coverage — exactly the kind of rule that regresses silently during later refactors.
- **Files likely affected:** New `test/.../MainDispatcherRule.kt`; new or extended `FindItViewModelTest.kt`.
- **Implementation Rules:** Build the standard `MainDispatcherRule` JUnit rule (swaps `Dispatchers.Main` for a `TestDispatcher`). Write tests for: (1) correct tap on a target image advances progress correctly, (2) hearts reaching 0 resets to 3 (not 0), matching the documented business rule.
- **Acceptance Criteria:** Both scenarios have passing unit tests.
- **Verification Checklist:** ☐ `./gradlew test` green including the new tests ☐ Test file follows the existing project's test-naming conventions (`UnlockManagersTest.kt`-style).
- **Android Studio Runtime Tests:** N/A (unit test task) — run via `./gradlew test`, not the emulator.
- **Git Commit Message:** `test: add FindItViewModel unit tests with MainDispatcherRule`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once both scenarios pass; broader `FindItViewModel` coverage is addressed later in Phase 10.

### Task ENG-2.10 — Fix ProfileViewModel/AndroidViewModel factory mismatch
*(= Engineering Review BUG-01)*
- **Objective:** Resolve the redundant pattern where `ProfileViewModel` extends `AndroidViewModel` but a manually written factory duplicates what `AndroidViewModel` already provides.
- **Why it matters:** No current runtime crash, but it's misleading and will break silently if the ViewModel is ever refactored without noticing the duplication.
- **Files likely affected:** `ProfileViewModel.kt`, its `ViewModelProvider.Factory`.
- **Implementation Rules:** Either (a) switch to `ViewModelProvider.AndroidViewModelFactory`, removing the manual factory, or (b) keep the manual factory but change `ProfileViewModel` to extend plain `ViewModel` instead of `AndroidViewModel`. Choose whichever requires the smaller diff against the existing factory-injection pattern used by other ViewModels in the app (per Master Context §10's manual-DI convention) — prefer consistency with the rest of the codebase over textbook-correctness if they conflict.
- **Acceptance Criteria:** No duplicated Application-provisioning logic between the base class and the factory.
- **Verification Checklist:** ☐ Build green ☐ Profile creation/selection flow smoke-tested, no regression.
- **Android Studio Runtime Tests:** Create a new profile and select an existing one; confirm both work identically to before.
- **Git Commit Message:** `refactor: resolve ProfileViewModel/AndroidViewModel factory duplication`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once profile creation/selection is confirmed unregressed.

### Task ENG-2.11 — Centralize hardcoded navigation route strings
*(= Engineering Review IMPROVEMENT-03)*
- **Objective:** Replace raw route string literals (`"splash"`, `"map"`, `"hear_it/{phonemeId}"`, etc.) scattered across `PlayItNavGraph.kt` with a `Routes` sealed class or object of constants.
- **Why it matters:** Raw literals carry real typo risk with no compiler safety net; doing this before Phase 5's screen-by-screen polish means every later navigation-adjacent task references clean constants instead of literals.
- **Files likely affected:** `PlayItNavGraph.kt`, a new `Routes.kt`.
- **Implementation Rules:** Define a `Routes` object/sealed class covering all current route strings, including parameterized ones (`hear_it/{phonemeId}`) with helper functions for building concrete navigation calls (e.g., `Routes.hearIt(phonemeId)`). Replace every literal in `PlayItNavGraph.kt` and any `navController.navigate(...)` call site.
- **Acceptance Criteria:** Zero raw route string literals remain outside `Routes.kt`.
- **Verification Checklist:** ☐ Build green ☐ Full navigation smoke test (every screen reachable, back-stack behaves correctly) ☐ grep confirms no stray route literals remain.
- **Android Studio Runtime Tests:** Walk the entire navigation graph once (Splash → Profile → Name Prompt → Map → each sublevel → Complete → Blend It → Blend It Complete → Dashboard → Report Preview) confirming no broken route.
- **Git Commit Message:** `refactor: centralize navigation route strings into Routes object`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the full navigation walkthrough passes with zero broken routes.

### Task ENG-2.12 — Make activeHearts nullable in PlayItLearningScaffold
*(= Engineering Review IMPROVEMENT-04)*
- **Objective:** Change `activeHearts: Int` to `activeHearts: Int?` in `PlayItLearningScaffold`, hiding the heart container entirely when `null` instead of rendering an empty pill for Hear It's `activeHearts = 0`.
- **Why it matters:** Currently `HearItScreen` passes `0`, which renders an empty white pill rather than cleanly hiding the (inapplicable) heart section — a small but real semantic/visual bug.
- **Files likely affected:** `PlayItLearningScaffold.kt`, `HearItScreen.kt` (call-site update to pass `null` instead of `0`).
- **Implementation Rules:** Change the parameter type and add the null-hides-container branch. Update `HearItScreen`'s call site to pass `null`. `SayItScreen`/`FindItScreen`/`BlendItScreen` continue passing real `Int` values, unchanged.
- **Acceptance Criteria:** Hear It renders with no heart-container artifact at all; other screens unaffected.
- **Verification Checklist:** ☐ Build green ☐ Visual check on Hear It confirms no empty pill ☐ Visual check on Say It/Find It/Blend It confirms hearts still render exactly as before.
- **Android Studio Runtime Tests:** Visit Hear It, Say It, Find It, Blend It in one session and confirm the heart container behaves correctly on each.
- **Git Commit Message:** `fix: make activeHearts nullable to cleanly hide heart container on Hear It`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once all 4 screens are visually confirmed correct.

### Task ENG-2.13 — Fix BlendItViewModel SessionManager boundary violation
*(= Engineering Review ISSUE-03)*
- **Objective:** Replace `BlendItViewModel`'s direct, fully-qualified `SessionManager.activeProfileId` reference (inside a `viewModelScope.launch` block, line ~141) with constructor-injected `profileId`, matching the pattern already used correctly by `FindItViewModel`, `SayItViewModel`, and `MapViewModel`.
- **Why it matters:** This is a Clean Architecture boundary break inside a file that's otherwise consistent — an inconsistency within a single file is exactly the kind of drift that compounds over time.
- **Files likely affected:** `BlendItViewModel.kt`, its factory/constructor call site.
- **Implementation Rules:** Add `profileId` as a constructor parameter; remove the direct `SessionManager` import/reference from inside the `viewModelScope.launch` block; update the factory to pass it in, matching the sibling ViewModels' pattern exactly.
- **Acceptance Criteria:** No `SessionManager` reference remains inside `BlendItViewModel`; `profileId` is injected identically to the 3 sibling ViewModels.
- **Verification Checklist:** ☐ Build green ☐ grep confirms zero `SessionManager` references in `BlendItViewModel.kt`.
- **Android Studio Runtime Tests:** Run a full Blend It session and confirm profile-scoped data (progress, hearts, achievements) still resolves to the correct profile.
- **Git Commit Message:** `refactor: inject profileId into BlendItViewModel instead of direct SessionManager access`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once the Blend It session test confirms correct profile scoping.

### Task ENG-2.14 — Resolve dual unlock logic (SQL + Kotlin) in cross-group unlocking
*(= Engineering Review ISSUE-06)*
- **Objective:** Consolidate the cross-group phoneme-unlock logic, currently split between a clever-but-fragile SQL subquery in `PhonemeDao.getUnlockedPhonemes` and separate Kotlin logic in `GroupUnlockManager`, into a single source of truth.
- **Why it matters:** The current pattern only works because of a specific `ORDER BY groupNumber DESC, position DESC LIMIT 1` clause interacting correctly with Kotlin-side logic for the `BLEND_1` case — a long-term maintenance hazard where either side changing independently silently breaks unlock correctness.
- **Files likely affected:** `PhonemeDao.kt`, `GroupUnlockManager.kt`, `UnlockManager.kt`.
- **Implementation Rules:** Per the review's own recommendation, choose one: (a) move all unlock logic to Kotlin (DAO returns raw completion status only, ViewModel/UseCase computes unlock state), or (b) move all logic into SQL. **Recommend (a)** — it keeps business logic testable in the domain layer per Master Context §10's testing philosophy, consistent with `UnlockManager`/`GroupUnlockManager` already being pure, well-tested Kotlin classes. Simplify `getUnlockedPhonemes` to a raw completion-status query; move the cross-group boundary logic into `GroupUnlockManager`.
- **Acceptance Criteria:** Unlock logic lives in exactly one place; existing `UnlockManagersTest.kt` coverage still passes and is extended to cover the cross-group boundary case explicitly (last phoneme of group N → first phoneme of group N+1).
- **Verification Checklist:** ☐ Build green ☐ `./gradlew test` green including extended `UnlockManagersTest.kt` ☐ Manual test: complete the last phoneme of a group and confirm the first phoneme of the next group unlocks correctly.
- **Android Studio Runtime Tests:** Complete letter `i` (last of group 1) and confirm letter `o` (first of group 2) and `BLEND_1` both unlock correctly on the Map.
- **Git Commit Message:** `refactor: consolidate cross-group unlock logic into GroupUnlockManager, simplify SQL`
- **Estimated Difficulty:** Medium-High. **Expected Duration:** 3h.
- **Stop Condition:** Stop once the group-boundary manual test and extended unit test both pass. Do not touch same-group unlock logic — it's already correct and out of scope.

### Task ENG-2.15 — Guard PhonemeAudioPlayer against mid-chain ViewModel clear
*(= Engineering Review BUG-08)*
- **Objective:** Add an `isReleased: Boolean` guard to `PhonemeAudioPlayer`, checked at the top of `playAssetAudio`, to prevent a new `MediaPlayer` being created (and never released) if `onCleared()` runs between two chained audio calls in a Blend It sequence.
- **Why it matters:** Currently a race between `onCleared()` and a chained `playAssetAudio` call (e.g., blend-sound → word-sound) can leak a `MediaPlayer` instance.
- **Files likely affected:** `PhonemeAudioPlayer.kt`, `BlendItViewModel.kt` (`onCleared()`).
- **Implementation Rules:** Add `private var isReleased = false`; set `true` inside `release()`; guard the top of `playAssetAudio` to no-op if `isReleased == true`.
- **Acceptance Criteria:** No new `MediaPlayer` is created after `release()` has been called.
- **Verification Checklist:** ☐ Build green ☐ Manual test: rapidly navigate away from Blend It mid-audio-chain (e.g., tap back during a word-blend playback) repeatedly, confirm no `MediaPlayer`-related leak warnings in logcat/profiler.
- **Android Studio Runtime Tests:** Use the Android Studio Memory Profiler while rapidly entering/exiting Blend It mid-audio-playback ~10 times; confirm no accumulating unreleased `MediaPlayer` instances.
- **Git Commit Message:** `fix: guard PhonemeAudioPlayer against mid-chain playback after release`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once the memory profiler shows no leaked instances across 10 rapid navigation cycles.

### Task ENG-2.16 — Fix FindItViewModel onCardTapped race condition
*(= Engineering Review BUG-07)*
- **Objective:** Move the `viewModelScope.launch {}` block currently nested inside `_uiState.update {}` out to be captured-and-launched after the update returns, preventing duplicate `insertFindItAttempt` calls on rapid double-tap.
- **Why it matters:** Two rapid taps can queue two coroutines that both read the same pre-update `hearts` value before either DB write completes, producing duplicate attempt records.
- **Files likely affected:** `FindItViewModel.kt` (`onCardTapped`).
- **Implementation Rules:** Capture needed values (e.g., `hearts`, tapped card state) **before** calling `_uiState.update {}`; move the `viewModelScope.launch {}` block to execute after the update call returns, using the captured values rather than re-reading state inside the coroutine.
- **Acceptance Criteria:** Rapid double-tap on the same or different cards never produces duplicate `insertFindItAttempt` rows for a single logical tap.
- **Verification Checklist:** ☐ Build green ☐ This directly overlaps **ENG-2.09**'s new test coverage — extend that same `FindItViewModelTest.kt` with a rapid-double-tap scenario rather than writing a separate test file.
- **Android Studio Runtime Tests:** Rapidly double-tap a Find It image in the emulator (or use a scripted rapid-tap test) and confirm via Database Inspector that only one `FindItAttempt` row is created per logical tap.
- **Git Commit Message:** `fix: eliminate race condition in FindItViewModel.onCardTapped`
- **Estimated Difficulty:** Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once Database Inspector confirms no duplicate rows across 5 rapid-tap test attempts.

### Task ENG-2.17 — Fix PDF emoji rendering risk on API 26
*(= Engineering Review ISSUE-11, and folds in IMPROVEMENT-09's heart-row contentDescription while touching adjacent accessibility surface)*
- **Objective:** Replace emoji characters in `ProgressReportPdfGenerator`'s `Paint.drawText` calls (e.g., `"⭐"`, `"🔥"`) with text equivalents or `BitmapDrawable`-based icons, since `android.graphics.Paint` emoji rendering isn't guaranteed on the min-SDK-26 floor.
- **Why it matters:** On older API 26 devices, these can render as an empty box (`□`) in the exported PDF — a visible defect in a report a parent may print or share.
- **Files likely affected:** `ProgressReportPdfGenerator.kt`.
- **Implementation Rules:** Replace `"${profile.totalStars} ⭐"` → `"${profile.totalStars} stars"`, `"🔥 days"` → `"days streak"` (or equivalent text phrasing that reads naturally), OR draw a small bitmap star/flame icon via `Canvas.drawBitmap` if visual iconography is preferred over the text-equivalent approach — either satisfies the fix; prefer text-equivalents for simplicity unless a bitmap asset already exists for this purpose.
- **Acceptance Criteria:** No emoji glyphs remain in `Paint.drawText` calls within the PDF generator.
- **Verification Checklist:** ☐ Build green ☐ Generate a report on an API 26 emulator specifically and visually confirm no box glyphs.
- **Android Studio Runtime Tests:** Generate a PDF report on an API 26 emulator (the min-SDK floor, where this risk is highest) and open it, confirming all star/streak text renders correctly.
- **Git Commit Message:** `fix: replace emoji with text equivalents in PDF report generation for API 26 compatibility`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once the API 26 PDF render is visually confirmed clean.

### Task ENG-2.18 — Housekeeping: move VoskGrammarBuilder, delete ExampleUnitTest, widen parent-gate math range
*(= Engineering Review IMPROVEMENT-02, IMPROVEMENT-05, IMPROVEMENT-07 — grouped as one low-risk housekeeping cycle since each is a single-file, sub-15-minute change with no shared surface)*
- **Objective:** (a) Move `VoskGrammarBuilder` from `data/speech` to `domain/usecase` (it's a pure, stateless utility with no Android dependency). (b) Delete `ExampleUnitTest.kt` (placeholder, demonstrates nothing, risks confusing future contributors). (c) Widen the parent-gate arithmetic factors from `(2..9).random()` to ensure at least one factor draws from `7..12`, so the gate isn't trivially solvable by the target age group (e.g., `4*4=16`).
- **Why it matters:** Three small, independent, low-risk correctness/cleanliness items — bundling them into one reviewed cycle is more efficient than three separate near-empty commits, and none touches shared surface with any other Phase 2 task.
- **Files likely affected:** `VoskGrammarBuilder.kt` (move), `ExampleUnitTest.kt` (delete), the parent-gate math generator (likely inside `ParentDashboardScreen.kt` or a small `ParentGate.kt` util).
- **Implementation Rules:** Move `VoskGrammarBuilder` with import updates only, no logic change. Delete `ExampleUnitTest.kt` outright. Change the math-gate generator so at least one of the two factors is drawn from `7..12` rather than both from `2..9`.
- **Acceptance Criteria:** All three sub-items complete; build green; no remaining reference to the deleted test file anywhere (e.g., in a CI config).
- **Verification Checklist:** ☐ Build green ☐ `./gradlew test` green (one fewer, meaningless test now absent) ☐ Manual check: trigger the parent gate 5× and confirm no trivially-easy product like `4×4` appears.
- **Android Studio Runtime Tests:** Trigger a destructive action (in a test profile) 5 times and eyeball the arithmetic challenges generated.
- **Git Commit Message:** `chore: relocate VoskGrammarBuilder to domain layer, remove placeholder test, widen parent-gate difficulty`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h total.
- **Stop Condition:** Stop once all three sub-items are verified independently — do not let this task expand into a broader cleanup pass (that's Phase 11).

**Quality Gate 2 → 3:** [x] All 17 executed tasks committed (ENG-2.05 deliberately deferred, not "skipped without reason") [x] Full regression walkthrough repeated [x] `./gradlew test` green [x] No Engineering Review finding remains unaccounted-for in either Phase 1, Phase 2, or an explicit §0.3 deferral note.

---

## 8. PHASE 3 — Design Token & Theme Foundation

**Purpose:** Establish every visual primitive (color, type, spacing, shape, elevation, touch-target enforcement) as a single centralized source of truth before any component or screen consumes it. This also closes **ISSUE-10** (§0.3, deferred here from Phase 2).

**Objectives:** Zero hardcoded color/type/spacing/shape literals surviving anywhere in the codebase once this phase and its Phase 5 consumers land.

**Expected Deliverables:** `ui/theme/Color.kt`, `Type.kt`, `Spacing.kt`, `Shape.kt`, `Elevation.kt`, `TouchTarget.kt` + enforcement modifier — all correct, all centralized.

**Dependencies:** Phase 2 complete and gated.

**Risks:** Low technical risk; the main risk is scope creep into retrofitting existing screens (explicitly out of scope here — that's Phase 5).

**Definition of Done:** All 6 tasks committed; `./gradlew assembleDebug` green; a project-wide grep for raw hex/sp/dp literals outside `ui/theme/` returns nothing new introduced by this phase (existing screen-level literals are Phase 5's job to migrate).

**Estimated Complexity:** Low. **Priority:** P1.

### Task UI-3.01 — Establish color token file *(closes ISSUE-10)*
- **Objective:** Create/correct `ui/theme/Color.kt` with exactly the 12 named Design System colors at exact hex values, wired into `Theme.kt`'s `MaterialTheme` color scheme.
- **Why it matters:** Foundational — every later task in this roadmap's UI phases assumes these tokens exist and are correctly named. Also directly resolves Engineering Review ISSUE-10 (color duplication across `SayItScreen`/`BlendItScreen`/`HearItScreen`/`PlayItLearningScaffold`).
- **Files affected:** `ui/theme/Color.kt`, `ui/theme/Theme.kt`.
- **Implementation Rules:** Define exactly `LearningBlue #4A90E2`, `GrowthGreen #4CAF50`, `AchievementGold #FFC107`, `EnergyOrange #FF9800`, `FriendlyPurple #8E7DF2`, `SoftSky #EAF6FF`, `CreamWhite #FFFDF8`, `GentleCorrectionOrange #FFB74D`, `TextPrimary #2D3748`, `TextSecondary #718096`, `Border #E2E8F0`, `Disabled #CBD5E0`. No raw system red anywhere. If duplicate private `val`s already exist per-screen, consolidate into this file rather than leaving both.
- **Acceptance Criteria:** All 12 tokens present, byte-exact hex; no other file defines a color literal (spot-check `HearItScreen`, `SayItScreen`, `ParentDashboardScreen`).
- **Verification Checklist:** ☐ `./gradlew assembleDebug` succeeds with zero new warnings ☐ Launch app, smoke-check Splash/ProfileSelect for color regressions.
- **Android Studio Runtime Tests:** Compose Preview of a screen using the new tokens; visual smoke test on device.
- **Git Commit Message:** `chore: establish design-system color tokens`
- **Estimated Difficulty:** Low. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once all 12 tokens exist and compile — do not migrate screens yet (Phase 5).

### Task UI-3.02 — Establish typography scale
- **Objective:** Create `ui/theme/Type.kt` with the 5-tier Compose `Typography`, Nunito primary / Poppins fallback.
- **Why it matters:** "Never below 16sp" is the single most-repeated accessibility rule in the spec; centralizing removes the chance of a stray small font size anywhere.
- **Files affected:** `ui/theme/Type.kt`, `res/font/` (Nunito + Poppins family files if not bundled).
- **Implementation Rules:** Bundle Nunito as primary `FontFamily` with Poppins as fallback chain (not a separate unused definition). Map Display Large 40sp ExtraBold / Heading 28sp Bold / Subheading 22sp SemiBold / Body 18sp Medium / Caption 16sp Regular to named `TextStyle`s. Add a code comment enforcing the 16sp floor.
- **Acceptance Criteria:** All 5 tiers correct; no raw `TextStyle`/`fontSize` literal exists outside this file after this task (screens migrate in Phase 5).
- **Verification Checklist:** ☐ Build green ☐ Render a text-heavy screen (`ParentDashboardScreen`) and confirm fonts load without system-default fallback glyphs.
- **Android Studio Runtime Tests:** Preview render check on device/emulator, confirm Nunito glyphs actually render (not a fallback).
- **Git Commit Message:** `chore: establish design-system typography scale`
- **Estimated Difficulty:** Low. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once all 5 tiers compile and render correctly in one preview.

### Task UI-3.03 — Establish spacing scale
- **Objective:** Create `ui/theme/Spacing.kt` (`PlayItSpacing` object) with the 7-value 8dp-base scale.
- **Why it matters:** Centralization is the whole point — Phase 5 needs one object to consume, not scattered `Modifier.padding(16.dp)` literals.
- **Files affected:** `ui/theme/Spacing.kt`.
- **Implementation Rules:** Define `tiny=4.dp, small=8.dp, default=16.dp, cardPadding=24.dp, section=32.dp, largeSeparation=48.dp, celebration=64.dp` (naming may follow existing convention; semantics must match exactly).
- **Acceptance Criteria:** All 7 values defined and semantically named.
- **Verification Checklist:** ☐ Build green.
- **Android Studio Runtime Tests:** None required beyond build.
- **Git Commit Message:** `chore: establish design-system spacing scale`
- **Estimated Difficulty:** Low. **Expected Duration:** 0.5h.
- **Stop Condition:** Stop once the object compiles.

### Task UI-3.04 — Establish shape system
- **Objective:** Create `ui/theme/Shape.kt` with named `RoundedCornerShape` values for buttons (28dp), Learning Card (24dp), Reward Card (32dp).
- **Why it matters:** Prevents ad-hoc corner radii drifting per screen.
- **Files affected:** `ui/theme/Shape.kt`.
- **Implementation Rules:** `buttonShape=28.dp`, `learningCardShape=24.dp`, `rewardCardShape=32.dp`. Wire into `MaterialTheme.shapes` where reasonable but keep explicit named references available (Material's generic small/medium/large slots aren't self-documenting enough here).
- **Acceptance Criteria:** 3 named shapes, correct radii.
- **Verification Checklist:** ☐ Build green.
- **Android Studio Runtime Tests:** None required beyond build.
- **Git Commit Message:** `chore: establish design-system shape tokens`
- **Estimated Difficulty:** Low. **Expected Duration:** 0.5h.
- **Stop Condition:** Stop once 3 shapes compile.

### Task UI-3.05 — Establish elevation tokens
- **Objective:** Create `ui/theme/Elevation.kt` with `cardElevation = 4.dp` and a small, soft `buttonElevation` (recommend 2–3dp).
- **Why it matters:** "Soft elevation" is currently implementation-defined and easy to drift toward a harsh default Material shadow.
- **Files affected:** `ui/theme/Elevation.kt`.
- **Implementation Rules:** `cardElevation = 4.dp` exactly (spec-mandated). `buttonElevation` at 2–3dp with a code comment flagging it as intentionally subtle, not to be increased without a Design System update.
- **Acceptance Criteria:** Both tokens defined; card elevation exactly 4dp.
- **Verification Checklist:** ☐ Build green.
- **Android Studio Runtime Tests:** None required beyond build.
- **Git Commit Message:** `chore: establish design-system elevation tokens`
- **Estimated Difficulty:** Low. **Expected Duration:** 0.5h.
- **Stop Condition:** Stop once both tokens compile.

### Task UI-3.06 — Establish touch-target constants and enforcement modifier
- **Objective:** Create `TouchTarget` constants (48/56/64dp) and a `Modifier.minTouchTarget()` helper that enforces the minimum by construction.
- **Why it matters:** The single most-repeated, most-violatable rule in the whole spec — solving it once centrally, rather than relying on manual per-screen review, is the highest-leverage token task in this phase.
- **Files affected:** `ui/theme/TouchTarget.kt`, `ui/util/Modifiers.kt`.
- **Implementation Rules:** `TouchTarget.MINIMUM=48.dp`, `.RECOMMENDED=56.dp`, `.IMPORTANT=64.dp`. `Modifier.minTouchTarget(size: Dp = TouchTarget.MINIMUM)` applying `sizeIn(minWidth=size, minHeight=size)`. Do **not** retrofit existing tappables in this task — that's Phase 5's job; this task only creates the primitive.
- **Acceptance Criteria:** Constants and modifier compile; a Compose Preview confirms the modifier actually enforces minimum size on a small test composable.
- **Verification Checklist:** ☐ Build green ☐ Preview check confirms enforcement.
- **Android Studio Runtime Tests:** Compose Preview check only.
- **Git Commit Message:** `chore: establish touch-target constants and enforcement modifier`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once the preview confirms enforcement works on a test case.

**Quality Gate 3 → 4:** [x] All 5 Phase 3 tasks committed [x] `./gradlew assembleDebug` green [x] All tokens referenced in theme

---

## 9. PHASE 4 — Component Library Standardization

**Purpose:** Build every shared composable that the 12 screens will consume in Phase 5, so screens are polished by *adopting* correct shared components, never by hand-rolling one-off UI.

**Objectives:** One canonical implementation each of `PrimaryButton`, `SecondaryButton`, `SuccessButton`, `LearningCard`, `RewardCard`, `HeartDisplay`, `MascotBubble`, `SubLevelProgressBar`, `AudioPlaybackIndicator`, `StarRating`, and the shared `tapFeedback` modifier.

**Expected Deliverables:** 11 components/utilities, each with a Compose `@Preview` demonstrating its states.

**Dependencies:** Phase 3 complete and gated (every component here consumes Phase 3's tokens).

**Risks:** `HeartDisplay`/`MascotBubble`/`SubLevelProgressBar` consolidation tasks (UI-4.06–4.08) may find genuine per-screen forks already in the codebase — budget extra time for those three specifically.

**Definition of Done:** All 11 tasks committed; each shared component used in at least a Preview; Phase 5 can begin consuming them.

**Estimated Complexity:** Medium. **Priority:** P1.

### Task UI-4.01 — Build PrimaryButton composable
- **Objective:** The single shared Primary Button for Continue/Start/Next actions app-wide (56dp height, 28dp radius, Learning Blue, white text, soft elevation, spring-bounce tap).
- **Why it matters:** Reused everywhere a primary action exists — must never be re-implemented per screen.
- **Files affected:** `ui/components/PrimaryButton.kt` (new).
- **Implementation Rules:** Consume Phase 3 tokens (`LearningBlue`, `buttonShape`, `buttonElevation`, `TouchTarget.RECOMMENDED`). Spring-bounce 100%→92%→100% via `animateFloatAsState`, micro tier (150–250ms). Expose `enabled`, `onClick`, text/content slot, optional leading icon. Disabled state uses `Disabled` token + reduced opacity, not color alone.
- **Acceptance Criteria:** Renders at exact spec dimensions/colors; tap produces exact bounce timing; disabled state visually distinct beyond color.
- **Verification Checklist:** ☐ Build green ☐ `@Preview` shows enabled + disabled side by side.
- **Android Studio Runtime Tests:** Tap-test in a host screen or preview interaction mode, confirm bounce feel.
- **Git Commit Message:** `feat: add shared PrimaryButton component`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once Preview shows both states correctly.

### Task UI-4.02 — Build SecondaryButton composable
- **Objective:** Shared Secondary Button (Cream White bg, 2dp Learning Blue border) primarily for "Replay Audio."
- **Why it matters:** Consistency with Primary Button's tap feel via a shared spring spec, not a duplicated one.
- **Files affected:** `ui/components/SecondaryButton.kt` (new).
- **Implementation Rules:** Cream White bg, 2dp Learning Blue border, same radius family as Primary, 48dp min/56dp recommended target. Reuse Primary's exact tap-feedback spec (shared constant, not copy-pasted). Support icon-only mode (speaker icon) while still meeting the touch-target floor.
- **Acceptance Criteria:** Visually matches spec; tap timing identical to PrimaryButton; icon-only variant still ≥48dp.
- **Verification Checklist:** ☐ Build green ☐ Preview shows text + icon-only variants.
- **Android Studio Runtime Tests:** Preview interaction check.
- **Git Commit Message:** `feat: add shared SecondaryButton component`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once both variants preview correctly.

### Task UI-4.03 — Build SuccessButton composable
- **Objective:** Shared Success Button (Achievement Gold bg, dark gray text) for "Claim Reward" actions.
- **Why it matters:** Must be reserved exclusively for reward-claim contexts once wired in Phase 5/6, not general navigation.
- **Files affected:** `ui/components/SuccessButton.kt` (new).
- **Implementation Rules:** `AchievementGold` bg, `TextPrimary` text, same height/radius family as Primary. Slightly more pronounced tap feedback acceptable (follows a win) but stays within micro-to-standard duration — no new duration band invented.
- **Acceptance Criteria:** Visually matches spec.
- **Verification Checklist:** ☐ Build green ☐ Preview renders against both Cream White and Gold-gradient backgrounds (its two real contexts).
- **Android Studio Runtime Tests:** Preview check only.
- **Git Commit Message:** `feat: add shared SuccessButton component`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once both background contexts preview correctly.

### Task UI-4.04 — Build LearningCard composable
- **Objective:** Shared card container (24dp radius, 4dp elevation, 24dp padding, Cream White) for letter/phoneme/feedback cards.
- **Why it matters:** Used across nearly every child-facing screen; a single implementation prevents visual drift.
- **Files affected:** `ui/components/LearningCard.kt` (new).
- **Implementation Rules:** Consume Phase 3's `learningCardShape`, `cardElevation`, `CreamWhite`, `cardPadding` tokens exactly. Content slot generic enough for text, images, or mixed content.
- **Acceptance Criteria:** Matches spec exactly at all 4 dimensions (radius/elevation/padding/color).
- **Verification Checklist:** ☐ Build green ☐ Preview with sample content.
- **Android Studio Runtime Tests:** Preview check only.
- **Git Commit Message:** `feat: add shared LearningCard component`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once Preview matches spec dimensions.

### Task UI-4.05 — Build RewardCard composable
- **Objective:** Shared reward container (32dp radius, Achievement Gold gradient) for completion/star screens.
- **Why it matters:** `LetterCompleteScreen` and `BlendItCompleteScreen` must share this rather than each rolling a similar-but-different card.
- **Files affected:** `ui/components/RewardCard.kt` (new).
- **Implementation Rules:** 32dp radius, Gold gradient background, content slot for star rating + copy + CTA.
- **Acceptance Criteria:** Matches spec; visually distinct from `LearningCard` (bigger radius, gradient vs. flat).
- **Verification Checklist:** ☐ Build green ☐ Preview.
- **Android Studio Runtime Tests:** Preview check only.
- **Git Commit Message:** `feat: add shared RewardCard component`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once Preview confirms correct radius/gradient.

### Task UI-4.06 — Consolidate/correct HeartDisplay composable
- **Objective:** Ensure exactly one `HeartDisplay.kt` exists, used identically by Say It, Find It, and Blend It.
- **Why it matters:** Master Context §10 explicitly names this as a required shared composable; per-screen forks are a direct spec violation.
- **Files affected:** `ui/components/HeartDisplay.kt`, `SayItScreen.kt`, `FindItScreen.kt`, `BlendItScreen.kt` (consolidate call sites).
- **Implementation Rules:** Locate any existing per-screen heart-rendering code; consolidate into one component. Must not rely on color alone — pair full/empty hearts with a shape distinction (filled vs. outlined icon), not just tint.
- **Acceptance Criteria:** Exactly one `HeartDisplay.kt`; all 3 consuming screens visually pixel-consistent.
- **Verification Checklist:** ☐ Build green ☐ Manual check across all 3 screens confirms pixel-consistent rendering.
- **Android Studio Runtime Tests:** Navigate to Say It, Find It, Blend It in one session; compare heart rendering side by side.
- **Git Commit Message:** `refactor: consolidate HeartDisplay into single shared component`
- **Estimated Difficulty:** Medium (consolidation risk). **Expected Duration:** 2h.
- **Stop Condition:** Stop once all 3 screens are confirmed visually identical.

### Task UI-4.07 — Consolidate/correct MascotBubble composable
- **Objective:** Exactly one `MascotBubble`, supporting all 5 emotional states, with the text+audio Voice Rule enforced **structurally** (required, non-nullable `audioResId` parameter).
- **Why it matters:** Making audio required at the type level makes a text-only mascot line a compile error, not a review-catchable mistake.
- **Files affected:** `ui/components/MascotBubble.kt`, any existing per-screen mascot implementations (consolidate).
- **Implementation Rules:** Params: `state: MascotState` (`Happy`/`Excited`/`Thinking`/`Encouraging`/`Celebrating`), `message: String`, **`audioResId` non-nullable/required**. Distinct idle animation/pose per state. Audio auto-plays on appearance (respecting any global mute preference). Code comment listing the two banned phrases as a guardrail.
- **Acceptance Criteria:** One `MascotBubble.kt`; `audioResId` compiler-enforced required; all 5 states visually distinct in preview.
- **Verification Checklist:** ☐ Build green ☐ Preview renders all 5 states.
- **Android Studio Runtime Tests:** Preview check; confirm compiler rejects a call site omitting `audioResId`.
- **Git Commit Message:** `refactor: consolidate MascotBubble into single shared component with required audio`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once all 5 states preview correctly and the required-audio constraint is compiler-verified.

### Task UI-4.08 — Consolidate/correct SubLevelProgressBar composable
- **Objective:** One shared progress bar for "how far through Hear It/Say It/Find It," identical across all three.
- **Why it matters:** Named explicitly in Master Context §10 as required-shared.
- **Files affected:** `ui/components/SubLevelProgressBar.kt`, `HearItScreen.kt`, `SayItScreen.kt`, `FindItScreen.kt`.
- **Implementation Rules:** Purely presentational, no sublevel-specific logic. Must not rely on color alone — pair filled/incomplete segments with a shape/icon distinction (filled vs. outlined dot). Use `GrowthGreen` for completed, `Border`/`Disabled` for incomplete.
- **Acceptance Criteria:** One shared component, identical across 3 screens; legible in a desaturated/grayscale screenshot.
- **Verification Checklist:** ☐ Build green ☐ Visual identity check across the 3 sublevels ☐ grayscale legibility spot-check.
- **Android Studio Runtime Tests:** Visual comparison across Hear It/Say It/Find It.
- **Git Commit Message:** `refactor: consolidate SubLevelProgressBar into single shared component`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once grayscale legibility and cross-screen consistency are both confirmed.

### Task UI-4.09 — Extend shared AudioPlayer with a PlaybackState indicator
- **Objective:** Add a `StateFlow<PlaybackState>` to `AudioPlayer` (additive only) and a small reusable "currently playing" indicator composable.
- **Why it matters:** Pre-approved by Master Context §8 as an adaptation of a Repository-B idea — not a new architectural decision, just execution of an already-approved plan.
- **Files affected:** `data/audio/AudioPlayer.kt` (extend additively — the one Phase-4 task touching a `data`-layer class), `ui/components/AudioPlaybackIndicator.kt` (new).
- **Implementation Rules:** If `AudioPlayer` doesn't expose `StateFlow<PlaybackState>` (idle/playing/finished) yet, add it without changing any existing method signature/behavior. Build a small waveform/pulse indicator that animates only while `Playing`. Use it at every `AudioPlayer` call site from UI (Hear It replay, mascot audio, Blend It picture-replay).
- **Acceptance Criteria:** `AudioPlayer`'s existing public methods unchanged in signature/behavior; indicator animates only during real playback (verified against an actual clip).
- **Verification Checklist:** ☐ Build green ☐ Manual playback test on Hear It confirms indicator animates for the clip's duration and stops correctly.
- **Android Studio Runtime Tests:** Trigger Hear It replay, observe indicator start/stop timing against actual audio duration.
- **Git Commit Message:** `feat: expose PlaybackState from AudioPlayer and add shared playback indicator`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once indicator timing is confirmed correct on one real clip.

### Task UI-4.10 — Build StarRating composable
- **Objective:** Shared 1–3 star display for `LetterCompleteScreen` and `BlendItCompleteScreen`.
- **Why it matters:** Both screens use identical star logic per Master Context §5 — the display must be equally shared.
- **Files affected:** `ui/components/StarRating.kt` (new).
- **Implementation Rules:** Takes `starsEarned: Int` (1–3), renders that many filled `AchievementGold` stars plus outlined/empty stars for the remainder. Never render a "0 stars" state as punitive-looking — that's handled by supportive copy at the call site, not by this component. Expose an `animate: Boolean` hook for the drop-bounce-glow sequence (built fully in Phase 7's UI-7.04) — this task builds the correct static/structural component first.
- **Acceptance Criteria:** Renders 1/2/3-star states identically wherever used; no hardcoded gold literal (references the Phase 3 token).
- **Verification Checklist:** ☐ Build green ☐ Preview shows all 3 states.
- **Android Studio Runtime Tests:** Preview check only.
- **Git Commit Message:** `feat: add shared StarRating component`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once all 3 states preview correctly — do not build the animation sequence yet (Phase 7).

### Task UI-4.11 — Build global tapFeedback modifier
- **Objective:** Extract the 100%→92%→100% spring-bounce into one reusable `Modifier.tapFeedback()`, and refactor UI-4.01–4.03's buttons to consume it instead of each having its own copy.
- **Why it matters:** Spec requires this exact animation on "every tappable component," not just the 3 button types — this is the primitive Phase 5 will apply to letter tiles, picture cards, and map nodes.
- **Files affected:** `ui/util/TapFeedbackModifier.kt` (new), `PrimaryButton.kt`, `SecondaryButton.kt`, `SuccessButton.kt` (refactor to delegate).
- **Implementation Rules:** `Modifier.tapFeedback(onClick: () -> Unit)` wrapping `animateFloatAsState` with a spring spec producing the exact scale sequence within 150–250ms. Refactor the 3 buttons to use this shared modifier internally, closing any drift risk between them.
- **Acceptance Criteria:** One canonical implementation; all 3 buttons delegate to it; timing verified within 150–250ms.
- **Verification Checklist:** ☐ Build green ☐ Manual tap-test on all 3 buttons confirms identical bounce feel post-refactor.
- **Android Studio Runtime Tests:** Tap each of the 3 buttons in sequence, confirm no perceptible timing difference.
- **Git Commit Message:** `refactor: extract shared tap-feedback modifier and adopt it in button components`
- **Estimated Difficulty:** Medium. **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once all 3 buttons are confirmed identical post-refactor. Do not apply this modifier to map nodes/tiles yet — that's Phase 5.

**Quality Gate 4 → 5:** [x] All 11 components committed [x] Each has a passing `@Preview` [x] `HeartDisplay`/`MascotBubble`/`SubLevelProgressBar` confirmed genuinely single-sourced (no orphaned per-screen forks remaining) [x] Build green.
---

## 10. PHASE 5 — Screen-by-Screen UI Refinement

**Purpose:** Apply Phases 3–4's tokens and components to all 12 canonical screens. This is the bulk of the app's visible improvement. Each task below corresponds 1:1 to a canonical screen and to UI Playbook P-18–P-29.

**Objectives:** Every screen fully token-compliant, fully component-adopting, and matching its individual audit findings from the UI Playbook.

**Expected Deliverables:** 12 polished screens, each independently committed.

**Dependencies:** Phase 4 complete and gated.

**Risks:** `MapScreen` (UI-5.04) is the highest-complexity single task in this phase — 35 nodes, camera/scroll behavior, and it's the app's de facto Home screen. Budget extra time.

**Definition of Done:** All 12 screens use only Phase 3 tokens and Phase 4 components (no raw literals, no orphaned per-screen UI forks); each screen's Design System Violations list from the source audit is closed.

**Estimated Complexity:** High (largest phase by scope). **Priority:** P1.

### Task UI-5.01 — Polish SplashScreen
- **Objective:** Bring the launch screen to full Design System compliance with a branded, non-generic identity (not a default Android splash).
- **Why it matters:** Highest-visibility branding moment in the app — seen on every single launch — currently under-designed relative to its exposure.
- **Files affected:** `SplashScreen.kt`.
- **Implementation Rules:** Soft Sky `#EAF6FF` background (not raw white/default Material). Mascot idle/breathing animation during load. Fade+upward transition (200–300ms) into ProfileSelect, not a hard cut. If load time varies (cold start, DB open per **ENG-1.04**, Vosk model check), give the screen visible state feedback so it never reads as a freeze. Screen remains non-interactive by design — no CTA added.
- **Acceptance Criteria:** Soft Sky background confirmed; mascot present; fade+upward transition into ProfileSelect confirmed; no red/system-default error states visible on slow load.
- **Verification Checklist:** ☐ Build green ☐ Visual check against Soft Sky token ☐ Transition timing checked (200–300ms).
- **Android Studio Runtime Tests:** Cold-launch the app repeatedly (including with an artificially slowed DB open, if feasible) and confirm the screen never reads as frozen.
- **Git Commit Message:** `style: polish SplashScreen to Design System v1.0`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the branded transition is confirmed on 3 consecutive cold launches.

### Task UI-5.02 — Polish ProfileSelectScreen
- **Objective:** Bring the profile grid to full token/component compliance with clear, legible tiles for pre-readers.
- **Why it matters:** Profile grid depends heavily on avatar/name legibility, not text alone, for a 6–7-year-old audience.
- **Files affected:** `ProfileSelectScreen.kt`.
- **Implementation Rules:** Profile tiles use `LearningCard` (UI-4.04), not raw Material `Card`. "Add profile" CTA uses `PrimaryButton` (UI-4.01) styling, visually distinct from existing-profile tiles at a glance. Spring-bounce tap feedback (`tapFeedback`, UI-4.11) on tile selection; fade+upward entry. Each tile spoken on selection (text+audio). Touch target ≥56dp (primary navigational action). Long-press/explicit edit icon for managing an existing profile (gated same as other destructive actions).
- **Acceptance Criteria:** Tiles use `LearningCard`; CTA uses `PrimaryButton`; all tiles ≥56dp; tap feedback present; spoken confirmation on selection.
- **Verification Checklist:** ☐ Build green ☐ TalkBack spot-check on tile selection ☐ Touch-target measurement on at least 2 tiles.
- **Android Studio Runtime Tests:** Select an existing profile and create a new one, confirming both flows feel consistent and responsive.
- **Git Commit Message:** `style: polish ProfileSelectScreen to Design System v1.0`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once both profile-selection and profile-creation entry points are confirmed compliant.

### Task UI-5.03 — Polish NamePromptScreen
- **Objective:** Full token/component compliance for the name+avatar entry form, with layout resilient to keyboard overlap.
- **Why it matters:** The one screen in the app where a pre-reader genuinely needs adult help — design should accommodate that, not assume child-only use.
- **Files affected:** `NamePromptScreen.kt`.
- **Implementation Rules:** `PrimaryButton` (UI-4.01) for "Continue" (not a default `TextButton`). Avatar picker uses 64dp+ tappable tiles (`TouchTarget.IMPORTANT`, UI-3.06). Confirm scroll/resize behavior when keyboard covers the avatar picker on small devices. Input field label/placeholder at 16sp+/4.5:1 contrast. Real-time avatar preview next to the name field. Mascot "excited" reaction (`MascotBubble`, UI-4.07) on confirmation.
- **Acceptance Criteria:** `PrimaryButton` used for Continue; avatar tiles ≥64dp; keyboard-overlap tested on a small-screen device/emulator profile.
- **Verification Checklist:** ☐ Build green ☐ Small-screen emulator profile keyboard-overlap check ☐ Contrast spot-check on input label.
- **Android Studio Runtime Tests:** Create a new profile on a small-screen emulator config, confirming the avatar picker and keyboard don't visually collide.
- **Git Commit Message:** `style: polish NamePromptScreen to Design System v1.0`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the small-screen keyboard-overlap case is confirmed resolved.

### Task UI-5.04 — Polish MapScreen
- **Objective:** Full compliance for the 35-node progression map, with correct default scroll/focus, distinct lock/unlock/completed/active states, and gentle feedback on locked-node taps.
- **Why it matters:** This is the app's de facto Home screen (highest-frequency screen per session) and the highest legibility/scroll-behavior risk in the app — a child can get lost on the path if this isn't right.
- **Files affected:** `MapScreen.kt`, `LetterNode.kt`/`MapNode.kt`, `BlendItChallengeNode.kt`.
- **Implementation Rules:** Default scroll/focus target on entry is the current node, not path start. Locked/unlocked/completed/active states unmistakably distinct beyond color (pairs with **ENG-2.06**'s contentDescription work). Tapping a locked node gives gentle shake + `MascotBubble` "Thinking" line about what's needed, rather than doing nothing. Streak flame + total-star count visible in a persistent header (data already exists on `Profile`). Group boundaries (7 groups of 4) visually chunked on the path. All nodes ≥48dp even at dense zoom. Node completion uses `GrowthGreen` + checkmark/star shape, never green fill alone. This task builds on **ENG-2.02**'s performance fix (single-node animation already hoisted) — do not re-introduce all-node animation here.
- **Acceptance Criteria:** Correct default scroll target; all 4 node states visually distinct without color; locked-tap gives feedback; group chunking visible; header shows streak/stars.
- **Verification Checklist:** ☐ Build green ☐ Manual scroll-target check on a partially-progressed profile ☐ Grayscale screenshot legibility check on node states.
- **Android Studio Runtime Tests:** Open Map with a mid-progress profile; confirm scroll lands on current node; tap a locked node and confirm feedback; visually confirm group boundaries.
- **Git Commit Message:** `style: polish MapScreen to Design System v1.0`
- **Estimated Difficulty:** High. **Expected Duration:** 4h.
- **Stop Condition:** Stop once all 4 node-state visuals and the default-scroll behavior are confirmed on a real mid-progress test profile — this is the single highest-complexity screen task in the roadmap; do not rush verification.

### Task UI-5.05 — Polish HearItScreen
- **Objective:** Full compliance for the ungated audio-modeling sublevel, making the letter the clear visual hero and the replay control impossible to miss.
- **Why it matters:** With no pass/fail gate, the screen risks feeling too passive if the visual design doesn't give "listening" something to look at.
- **Files affected:** `HearItScreen.kt`.
- **Implementation Rules:** Letter card uses `LearningCard` (UI-4.04); letter itself at Display Large 40sp ExtraBold, not competing with secondary chrome. Replay control uses `SecondaryButton` (UI-4.02), not a bare icon button, and is large/impossible to miss. `activeHearts = null` (per **ENG-2.12**'s fix) so no empty pill renders. "Next" disabled pre-play has a *visible* reason (caption or `MascotBubble` "Thinking" line: "Tap the letter to hear it first"), not just a grayed button. Playback-state animation via `AudioPlaybackIndicator` (UI-4.09). `exampleWord` (already on the `Phoneme` entity) surfaced visually alongside audio.
- **Acceptance Criteria:** Letter is the clear visual hero; replay control unmissable; disabled-Next reason is visible, not just implied by grey; no empty heart pill.
- **Verification Checklist:** ☐ Build green ☐ Visual check confirms no heart-container artifact (regression check against ENG-2.12) ☐ Confirm "Next" gating reason is visible.
- **Android Studio Runtime Tests:** Visit Hear It for a new letter; confirm Next stays disabled with a visible reason until first play, then enables.
- **Git Commit Message:** `style: polish HearItScreen to Design System v1.0`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the Next-gating visibility and letter-hero treatment are both confirmed.

### Task UI-5.06 — Polish SayItScreen
- **Objective:** Full compliance for the speech-production sublevel, with clearly conveyed recording state and constructively-framed noise alerts.
- **Why it matters:** The most technically failure-prone screen (mic permission, ambient noise, recognizer latency) — the one most likely to *feel* punitive if error states aren't carefully designed.
- **Files affected:** `SayItScreen.kt`.
- **Implementation Rules:** `HeartDisplay` (UI-4.06) renders identically to Find It/Blend It — confirm no per-screen fork survived. Visible "Listening…" state (icon + label) — recording state (listening/processing/done) must not rely on the child inferring it from mic behavior alone. Mic-active pulsing ring. Noise alert (>40dB, per Master Context §5 NFR) framed constructively ("It's a little noisy — let's find a quiet spot"), never as a blocking error. One-tap "try again," always in the same place. Mascot shifts to Encouraging (not disappointed) on heart loss, copy checked against the banned-phrase list.
- **Acceptance Criteria:** Recording state always visually conveyed; noise alert copy is constructive, not alarming; `HeartDisplay` pixel-identical to Find It/Blend It; no banned phrases present.
- **Verification Checklist:** ☐ Build green ☐ Manual mic-permission and noise-alert flow test ☐ Banned-phrase grep across this screen's copy.
- **Android Studio Runtime Tests:** Run Say It with deliberate background noise (or simulated >40dB input) to trigger the alert; confirm listening/processing states are visually distinguishable.
- **Git Commit Message:** `style: polish SayItScreen to Design System v1.0`
- **Estimated Difficulty:** Medium-High. **Expected Duration:** 3h.
- **Stop Condition:** Stop once the noise-alert flow and recording-state visibility are both confirmed on-device.

### Task UI-5.07 — Polish FindItScreen
- **Objective:** Full compliance for the 5-image discrimination grid, with reliable 48dp+ spacing across 5 simultaneous targets and clear disabled-image treatment.
- **Why it matters:** The dense 5-image grid is the highest touch-target-collision risk in the app.
- **Files affected:** `FindItScreen.kt`.
- **Implementation Rules:** Grid spacing uses 16dp default/24dp card-padding scale (Phase 3 tokens), not an ad-hoc gap. Images sit inside `LearningCard` containers, not bare `Image` composables. Disabled (wrong-tapped) images get Disabled `#CBD5E0` + reduced opacity + icon (not color dim alone). Each image gets an audio label on long-press/first render. Live "2 of 3 found" progress indicator, icon-based not text-reliant. Debounce taps on already-resolved images to prevent double-tap races (ties to **ENG-2.16**'s race-condition fix — confirm no regression). Never a flashing screen effect.
- **Acceptance Criteria:** All 5 images independently clear 48dp+; disabled state legible without color; progress indicator present; no flash effect under any condition.
- **Verification Checklist:** ☐ Build green ☐ Touch-target measurement on all 5 grid positions ☐ Grayscale legibility check on disabled-image treatment.
- **Android Studio Runtime Tests:** Play a full Find It round, deliberately tapping a distractor first, confirming disabled treatment and progress indicator both update correctly.
- **Git Commit Message:** `style: polish FindItScreen to Design System v1.0`
- **Estimated Difficulty:** Medium-High (density risk). **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once all 5 grid positions are confirmed ≥48dp and disabled-state legibility passes the grayscale check.

### Task UI-5.08 — Polish LetterCompleteScreen
- **Objective:** Full compliance for the highest-leverage single celebration moment in the app — a screen seen ~28 times over the app's arc.
- **Why it matters:** Repetition fatigue is a real risk without enough variation; tone must stay celebratory even at 1-star results.
- **Files affected:** `LetterCompleteScreen.kt`.
- **Implementation Rules:** `RewardCard` (UI-4.05) styling, not a reused `LearningCard`. Exactly one primary CTA ("Continue" to Map) — no competing secondary actions. Star count announced verbally ("You earned 3 stars!"), not shown as icons alone. Auto-focus Continue with a subtle idle pulse. 1-star result stays celebratory — no dimmed-star "loss" styling. Brief map-progress teaser ("2 more letters to unlock Word Challenge!"). Optional recap of the mastered letter's example word. Star animation itself is stubbed here (`StarRating` from UI-4.10) — full drop-bounce-glow sequencing lands in Phase 7 (UI-7.04).
- **Acceptance Criteria:** `RewardCard` used; exactly one CTA; star count is spoken; 1-star tone verified non-punitive.
- **Verification Checklist:** ☐ Build green ☐ Manual test at all 3 star tiers, specifically checking 1-star tone/copy.
- **Android Studio Runtime Tests:** Complete a letter at 1-star, 2-star, and 3-star outcomes (achievable by varying accuracy/hearts lost) and confirm tone stays celebratory at all three.
- **Git Commit Message:** `style: polish LetterCompleteScreen to Design System v1.0`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once all 3 star-tier outcomes are confirmed to read as celebratory, not graded.

### Task UI-5.09 — Polish BlendItScreen
- **Objective:** Full compliance for the word-construction challenge, with generous hit-testing on drag/tap tile interaction and a visible "why" moment for the auto-hint.
- **Why it matters:** The most motor-skill-demanding screen in the app, and the one place where a 0-heart outcome yields 0 stars with no partial credit — the design carries more responsibility than usual for keeping tone encouraging.
- **Files affected:** `BlendItScreen.kt`.
- **Implementation Rules:** Friendly Purple theme applied consistently (background/accents), not just a purple app-bar over otherwise-Learning-Blue components. Letter tiles ≥56dp (`TouchTarget.RECOMMENDED`) with generous hit-testing margins beyond visual bounds. Auto-hint (after 2 wrong attempts) gets its own visible "assist" animation distinct from correct/incorrect states, so the tile-lock doesn't read as a silent, confusing state change. Picture-replay-audio control permanently visible, not hidden in a menu. Incorrect feedback uses Gentle Correction Orange, never harsh red, even though this module's failure mode is harsher (immediate 0-heart session end). Exit copy on a 0-heart outcome is unusually warm.
- **Acceptance Criteria:** Purple theme consistent throughout; tiles ≥56dp with generous hit-testing; auto-hint has a distinct visible animation; 0-heart exit copy verified warm in tone.
- **Verification Checklist:** ☐ Build green ☐ Manual test forcing 2 wrong attempts to trigger auto-hint, confirming visible distinct feedback ☐ Manual test forcing a 0-heart session end, confirming copy tone.
- **Android Studio Runtime Tests:** Play a Blend It session deliberately to trigger both the auto-hint and a 0-heart failure; confirm both moments read correctly.
- **Git Commit Message:** `style: polish BlendItScreen to Design System v1.0`
- **Estimated Difficulty:** High. **Expected Duration:** 3.5h.
- **Stop Condition:** Stop once both the auto-hint moment and the 0-heart exit flow are confirmed correct in tone and visual clarity.

### Task UI-5.10 — Polish BlendItCompleteScreen
- **Objective:** Full compliance for the group-tier celebration screen, visually distinguished from `LetterCompleteScreen` as a "bigger milestone."
- **Why it matters:** Currently risks looking identical to `LetterCompleteScreen` despite being a higher-stakes checkpoint.
- **Files affected:** `BlendItCompleteScreen.kt`.
- **Implementation Rules:** `RewardCard` (Gold base) plus Friendly Purple accents inherited from `BlendItScreen`, so the celebration still visually belongs to "Word Challenge." Session summary (words correct, hearts used) at Body 18sp minimum, not icon-only. Single clear "Back to Map" CTA — no competing "Retry" CTA. 0-star/session-ended-early outcome clearly distinguished from a dead end, with warm acknowledgment-of-effort copy. Slightly bigger celebration treatment than `LetterCompleteScreen` (still within 600–1200ms celebration band — full sequencing in Phase 7).
- **Acceptance Criteria:** Visually distinct from `LetterCompleteScreen` via Purple accents; single CTA; 0-star outcome copy verified warm, not a dead end.
- **Verification Checklist:** ☐ Build green ☐ Side-by-side visual comparison against `LetterCompleteScreen` confirms distinction ☐ 0-star outcome manually tested.
- **Android Studio Runtime Tests:** Complete a Blend It session normally and again via forced 0-heart failure; confirm both outcomes render correctly and distinctly from a letter completion.
- **Git Commit Message:** `style: polish BlendItCompleteScreen to Design System v1.0`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once the side-by-side distinction from `LetterCompleteScreen` and the 0-star copy are both confirmed.

### Task UI-5.11 — Polish ParentDashboardScreen
- **Objective:** Full compliance for the one adult-facing screen in the app, hitting the stricter 7:1 contrast target and clear information hierarchy across 28 letters of status data.
- **Why it matters:** Most likely screen to still be using child-oriented chrome where a parent instead needs density and scannability; the one screen explicitly required to test at 7:1, not 4.5:1.
- **Files affected:** `ParentDashboardScreen.kt`.
- **Implementation Rules:** Clear grouping/hierarchy for 28-letter status data, not one long list. Status color-coding (Green ≥80% / Yellow 50–79% / Red <50% or ≥3 failed) paired with text/icon labels ("Mastered"/"Developing"/"At risk") — never color alone. Body copy never below 16sp; verify 7:1 contrast specifically (not just the app-wide 4.5:1 floor). Minimal, purposeful animation (standard tier at most — no celebration-tier bounce/confetti here). Clear entry point to arithmetic-gated destructive actions, positioned away from routine navigation to prevent misclicks. Retention score gets a one-line plain-language explanation. Streak/badge status echoed here. **This is also where UI-8.01 (Phase 8) will add the reduced-motion toggle** — leave a sensible layout slot for it, but do not implement the toggle itself in this task.
- **Acceptance Criteria:** 7:1 contrast verified (tool-assisted spot-check); status color-coding paired with labels; destructive-action entry point isolated from routine nav.
- **Verification Checklist:** ☐ Build green ☐ Contrast-checking tool run specifically at 7:1 threshold ☐ Grayscale legibility check on status indicators.
- **Android Studio Runtime Tests:** View dashboard for a profile with mixed mastery levels across letters; confirm hierarchy is scannable and status labels are legible without color.
- **Git Commit Message:** `style: polish ParentDashboardScreen to Design System v1.0 (7:1 contrast)`
- **Estimated Difficulty:** Medium-High. **Expected Duration:** 3h.
- **Stop Condition:** Stop once 7:1 contrast is tool-verified and status color-coding passes the grayscale check.

### Task UI-5.12 — Polish ReportPreviewScreen
- **Objective:** Full compliance for the PDF preview/export screen, with clear generating-state feedback and an unambiguous save/share flow.
- **Why it matters:** PDF preview UIs are easy to leave as a bare, unstyled system viewer; a silent generation gap reads as a freeze to a low-to-moderate-technical-literacy parent.
- **Files affected:** `ReportPreviewScreen.kt`.
- **Implementation Rules:** Chrome around the PDF preview (buttons, header) follows Phase 3/4 tokens/components even though the PDF content itself is fixed. Clear loading/generating state between "tap export" and preview appearing (this is where **ENG-2.17**'s emoji fix and Phase 9's branded loading component, UI-9.02, both land). Save/Share use `PrimaryButton`/`SecondaryButton`, ≥48dp, clear labels; spoken confirmation on save (not just a visual toast). Explicit, unambiguous save-location/share-sheet flow. One-line explainer of what the report covers.
- **Acceptance Criteria:** Save/Share use shared button components; spoken save-confirmation present; generating-state feedback present (even if using a temporary spinner ahead of UI-9.02's branded version).
- **Verification Checklist:** ☐ Build green ☐ Manual export test confirms visible generating-state and spoken save confirmation.
- **Android Studio Runtime Tests:** Export a report for a test profile; confirm the flow from tap to saved/shared file feels complete and non-silent at every step.
- **Git Commit Message:** `style: polish ReportPreviewScreen to Design System v1.0`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the full export flow is confirmed non-silent end to end.

**Quality Gate 5 → 6:** [x] All 12 screens committed individually [x] Full app walkthrough (every screen visited at least once) shows zero raw Material defaults, zero hardcoded token literals [x] Each screen's specific Design System Violations list (from the source audit) confirmed closed [x] Build green.
```

---

## 11. PHASE 6 — Gamification & Reward Polish

**Purpose:** Give existing streak/badge data a consistent visual system, ensure the mascot's 5 emotional states are actually invoked everywhere they matter (not just on showcase screens), and componentize celebration effects.

**Objectives:** Streak/badge display unified; mascot presence audited and filled across the full core loop; confetti/celebration effects extracted into one reduced-motion-aware component.

**Expected Deliverables:** `StreakBadge.kt`, mascot-invocation audit-and-fill across 7 screens, `CelebrationEffect.kt`.

**Dependencies:** Phase 5 complete and gated (this phase wires into already-polished screens, not raw ones).

**Risks:** UI-6.02 (mascot wiring) is an audit-and-fill task across many files — easy to under-scope if treated as "add mascot to 1-2 screens" instead of the full 7-screen sweep it requires.

**Definition of Done:** All 3 tasks committed; a manual walkthrough of one full letter cycle demonstrates all 5 mascot states firing at the correct moments.

**Estimated Complexity:** Medium. **Priority:** P1.

### Task UI-6.01 — Build streak/badge UI system
- **Objective:** Give existing streak/badge data (5/10/15/20-day milestones) consistent, motivating display across Map and Parent Dashboard.
- **Why it matters:** Streak logic already exists (Master Context T-11) — this is purely the display layer, previously absent.
- **Files affected:** `ui/components/StreakBadge.kt` (new), `MapScreen.kt`, `ParentDashboardScreen.kt` (wire in, no logic changes).
- **Implementation Rules:** One shared `StreakBadge`/`StreakFlame` composable using `EnergyOrange`. Milestone badges (5/10/15/20 days) with distinct unlocked/locked art, locked paired with a lock icon (not color alone). Present in Map's persistent header and echoed on Dashboard. Do not alter `StreakTracker` or the 24h-reset logic — display only.
- **Acceptance Criteria:** One shared component in both locations; locked/unlocked distinguishable without color.
- **Verification Checklist:** ☐ Build green ☐ Manual check against a profile with a known streak count.
- **Android Studio Runtime Tests:** View a profile with an active streak on both Map and Dashboard; confirm identical streak count display.
- **Git Commit Message:** `feat: add shared streak and badge display components`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once both locations show identical, correct streak data.

### Task UI-6.02 — Wire mascot emotional states across all modules
- **Objective:** Audit and fill missing mascot invocations across all 7 core-loop screens so the mascot's coaching role is present where it matters most (Say It/Find It/Blend It), not just on completion screens.
- **Why it matters:** Flagged risk: mascot presence concentrating on "showcase" screens while thin elsewhere undermines the single highest-leverage system for the app's core "encouragement before correction" philosophy.
- **Files affected:** `HearItScreen.kt`, `SayItScreen.kt`, `FindItScreen.kt`, `BlendItScreen.kt`, `LetterCompleteScreen.kt`, `BlendItCompleteScreen.kt`, `MapScreen.kt`.
- **Implementation Rules:** Apply this mapping consistently: correct answer→Happy; milestone/badge/streak→Excited; hint/locked node→Thinking; wrong answer/heart loss→Encouraging; level/group complete→Celebrating. This is an audit-and-fill task, not a mascot-behavior redesign. Every invocation uses `MascotBubble` (UI-4.07) with its required audio — no exceptions.
- **Acceptance Criteria:** All 5 states demonstrably invoked somewhere in the app; no core-loop screen missing at least one mascot moment.
- **Verification Checklist:** ☐ Build green ☐ Full walkthrough checklist (one row per screen × state) completed.
- **Android Studio Runtime Tests:** Walk one full letter cycle (Hear It → Say It → Find It → Complete), confirming mascot appears appropriately at each stage, including at least one deliberately-wrong answer to trigger Encouraging.
- **Git Commit Message:** `feat: wire mascot emotional states across all core learning screens`
- **Estimated Difficulty:** Medium-High (breadth, not depth). **Expected Duration:** 3h.
- **Stop Condition:** Stop once the full walkthrough checklist confirms zero missing invocations across all 7 screens.

### Task UI-6.03 — Componentize the celebration/confetti system
- **Objective:** Extract confetti-burst and celebration effects into one reusable, reduced-motion-aware component with small (correct-answer) and large (level/group-complete) intensity presets.
- **Why it matters:** Confetti is spec-required on correct answers and level completion; without componentization it's a prime candidate for inconsistent per-screen implementation.
- **Files affected:** `ui/components/CelebrationEffect.kt` (new), any existing per-screen confetti implementations (consolidate).
- **Implementation Rules:** Two presets: small burst (micro/standard timing, correct-answer moments) and large burst (celebration timing 600–1200ms, level/group-complete). Structurally wired to a `reducedMotion: Boolean` param, defaulted `false` until Phase 8's UI-8.01 lands (no visual regression before then). No harsh flashing under any preset.
- **Acceptance Criteria:** One shared component powers all confetti moments; `reducedMotion=true` produces a fade-only fallback with no particles, verified in preview.
- **Verification Checklist:** ☐ Build green ☐ Preview both presets and the reduced-motion fallback.
- **Android Studio Runtime Tests:** Trigger both a correct-answer and a level-complete moment, confirming visually distinct burst sizes.
- **Git Commit Message:** `feat: extract reusable, reduced-motion-aware celebration effect component`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once both presets and the reduced-motion fallback preview correctly.

**Quality Gate 6 → 7:** [x] All 3 tasks committed [x] Full-letter-cycle mascot walkthrough passes [x] Streak display consistent Map↔Dashboard [x] Build green.
```

---

## 12. PHASE 7 — Animation & Motion

**Purpose:** Layer final motion polish on top of now-stable visual/component work — screen transitions, the correct/incorrect feedback system, the map's signature breathing pulse, and the star-reveal celebration sequence.

**Objectives:** One consistent transition spec app-wide; one shared, spec-exact answer-feedback animation used identically by Say It/Find It/Blend It; the map's highest-visibility animation moment finalized; the star-reveal sequence finalized.

**Expected Deliverables:** `NavHost` transition spec, `AnswerFeedback.kt`, finalized `MapNode.kt` animation, finalized `StarRating.kt` animation.

**Dependencies:** Phase 6 complete and gated.

**Risks:** UI-7.02 (`AnswerFeedback`) is consumed by 3 screens simultaneously — a defect here surfaces app-wide, not screen-locally. Test all 3 consuming screens, not just one.

**Definition of Done:** All 4 tasks committed; screen transitions, answer feedback, map pulse, and star reveal are each provably single-sourced (no per-screen forks) and within their correct duration bands.

**Estimated Complexity:** Medium-High. **Priority:** P1.

### Task UI-7.01 — Standardize screen-entry transitions
- **Objective:** Apply one consistent fade+upward-motion entry transition (200–300ms) across the entire `NavHost`.
- **Why it matters:** Inconsistent per-screen transition timing reads as subtly "off" even when each individually seems fine.
- **Files affected:** `ui/navigation/PlayItNavHost.kt` (or equivalent central `NavHost` definition).
- **Implementation Rules:** One `enterTransition`/`exitTransition` pair (fade + slight upward `slideInVertically`) at 200–300ms, applied at the `NavHost` level so every screen inherits it automatically — not per-`composable()` call. Confirm no screen overrides it with a conflicting custom transition unless documented (e.g., a modal dialog transition, a different acceptable pattern).
- **Acceptance Criteria:** All 12 screens use the identical entry transition.
- **Verification Checklist:** ☐ Build green ☐ Manual pass through full navigation graph confirms consistent motion.
- **Android Studio Runtime Tests:** Navigate Map → sublevel → Complete → Map loop; confirm consistent motion feel throughout.
- **Git Commit Message:** `feat: standardize fade+upward screen-entry transition across NavHost`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the full navigation graph walkthrough confirms consistency across all 12 screens.

### Task UI-7.02 — Implement correct/incorrect answer animation system
- **Objective:** Build one reusable, spec-exact `AnswerFeedback` component (correct: scale-up + confetti + sound hook; incorrect: gentle shake + encouragement hook, no flashing), used identically across Say It, Find It, Blend It.
- **Why it matters:** Independently flagged as needed on all 3 screens' audits — building it once here prevents 3 slightly different implementations.
- **Files affected:** `ui/components/AnswerFeedback.kt` (new), `SayItScreen.kt`, `FindItScreen.kt`, `BlendItScreen.kt` (adopt, no business-logic change).
- **Implementation Rules:** `AnswerFeedback(isCorrect: Boolean, content: @Composable () -> Unit)` wrapping tappable content. Correct: subtle scale-up (distinct from the tap-feedback bounce) + `CelebrationEffect` (UI-6.03) small preset + sound hook (wired fully in Phase 10's UI-10.02). Incorrect: gentle shake (horizontal offset oscillation, standard-tier) + Gentle Correction Orange accent — explicitly no flash/strobe. All 3 consuming screens use this exact component — no fourth bespoke implementation.
- **Acceptance Criteria:** Identical animation feel across all 3 screens; no flashing under any condition.
- **Verification Checklist:** ☐ Build green ☐ Manual trigger of both outcomes on all 3 screens, side by side.
- **Android Studio Runtime Tests:** Deliberately trigger a correct and an incorrect answer on Say It, Find It, and Blend It (6 total triggers) and confirm identical feel per outcome type.
- **Git Commit Message:** `feat: add shared correct/incorrect AnswerFeedback animation component`
- **Estimated Difficulty:** High (3-screen consumption surface). **Expected Duration:** 3h.
- **Stop Condition:** Stop once all 6 manual triggers (3 screens × 2 outcomes) are confirmed identical per outcome type.

### Task UI-7.03 — Implement map node breathing pulse and unlock animation
- **Objective:** Finalize the infinite breathing-pulse on the active node (structural single-node targeting already done in **ENG-2.02**) and the one-shot magical-chime unlock animation.
- **Why it matters:** Flagged as the single highest-visibility signature animation moment in the app.
- **Files affected:** `ui/components/MapNode.kt`.
- **Implementation Rules:** Active node: infinite, gentle scale/opacity pulse loop (standard-tier per cycle, loops indefinitely) — must respect `reducedMotion` once UI-8.01 lands (fade-only alternative). Unlock transition: locked→unlocked plays a one-shot glow/sparkle burst synced with the sound cue wired in UI-10.02. Gate the unlock animation to fire only on an actual state transition (`LaunchedEffect` keyed to unlock state, not general recomposition) — never replay on a simple screen revisit.
- **Acceptance Criteria:** Pulse loops without restarting/stuttering on recomposition; unlock animation fires exactly once per real unlock event.
- **Verification Checklist:** ☐ Build green ☐ Manual test: unlock a letter, revisit Map afterward, confirm animation does not replay.
- **Android Studio Runtime Tests:** Unlock a letter, navigate away and back to Map, confirm the unlock animation only played once (at the actual unlock moment), and the pulse continues smoothly on the new active node.
- **Git Commit Message:** `feat: implement active-node breathing pulse and one-shot unlock animation`
- **Estimated Difficulty:** Medium-High. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once the revisit-does-not-replay behavior is confirmed.

### Task UI-7.04 — Implement star reward drop-bounce-glow animation
- **Objective:** Finalize the mandated star-reveal animation on `StarRating` (structural component from UI-4.10) for both completion screens.
- **Why it matters:** Flagged as the highest-leverage single animation in the app; must land in the celebration duration band.
- **Files affected:** `ui/components/StarRating.kt`.
- **Implementation Rules:** Sequenced per-star reveal: drop-in (translateY + ease) → bounce (small overshoot spring) → glow (brief radial highlight), staggered slightly across multiple stars so a 3-star result builds up rather than popping simultaneously. Total sequence within 600–1200ms. Respect `reducedMotion` (fall back to simple fade-in of final star state, no drop/bounce/glow) once UI-8.01 lands. Reused identically by both completion screens.
- **Acceptance Criteria:** Full sequence timing within 600–1200ms (measured); reduced-motion fallback skips drop/bounce/glow entirely.
- **Verification Checklist:** ☐ Build green ☐ Timing measured via animation inspector or manual stopwatch on both completion screens.
- **Android Studio Runtime Tests:** Trigger the animation on `LetterCompleteScreen` and `BlendItCompleteScreen`, confirming identical timing/behavior on both.
- **Git Commit Message:** `feat: implement celebration-tier star drop-bounce-glow animation`
- **Estimated Difficulty:** Medium-High. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once timing is confirmed within band on both completion screens.

**Quality Gate 7 → 8:** [x] All 4 tasks committed [x] Full navigation walkthrough shows consistent transitions [x] Answer feedback confirmed identical across 3 screens [x] Map pulse/unlock and star-reveal animations timed within their correct bands [x] Build green.

---

## 13. PHASE 8 — Accessibility Improvements

**Purpose:** The dedicated compliance sweep across all interactive surfaces now that they exist in final form — reduced-motion wiring, contrast, color-independence, and screen-reader support.

**Objectives:** Reduced-motion mode implemented and wired everywhere; zero sub-4.5:1 (sub-7:1 dashboard) contrast pairs; zero color-only signaling; zero missing/generic content descriptions.

**Expected Deliverables:** `UiPreferences.kt` (DataStore), reduced-motion toggle in Parent Dashboard, contrast fix pass, color-independence fix pass, TalkBack-verified content-description pass.

**Dependencies:** Phase 7 complete and gated (every animated component built in Phases 4/6/7 must exist before it can be wired to the preference).

**Risks:** UI-8.01 is the single task most likely to touch multiple files minimally (threading a `Boolean` preference through ~7 components) — keep each touch strictly to "read and pass through," per its own implementation rule.

**Definition of Done:** All 4 tasks committed; toggling reduced-motion visibly changes behavior on at least button tap feedback, star reveal, map pulse, and celebration confetti; a full TalkBack walkthrough of the core loop passes with zero missing/generic labels.

**Estimated Complexity:** Medium-High. **Priority:** P1 (this phase directly closes several Engineering Review and Design System compliance gaps that are otherwise the biggest outstanding risk once Phases 3–7 are done).

### Task UI-8.01 — Implement reduced-motion preference and global fallback wiring
- **Objective:** Add the Design-System-mandated reduced-motion mode as a `ParentDashboardScreen`-hosted, DataStore-backed UI preference, and wire every animated component from Phases 4/6/7 to respect it.
- **Why it matters:** Repeatedly flagged as the single biggest outstanding compliance gap once other phases are done — reduced-motion mode itself is an accessibility requirement, not a nice-to-have.
- **Files affected:** `data/preferences/UiPreferences.kt` (new, DataStore — explicitly not Room), `ParentDashboardScreen.kt` (toggle row, in the slot reserved by UI-5.11), `PrimaryButton.kt`, `SecondaryButton.kt`, `SuccessButton.kt`, `CelebrationEffect.kt`, `MapNode.kt`, `StarRating.kt`, `AnswerFeedback.kt` (consume the preference).
- **Implementation Rules:** New `UiPreferences` DataStore exposing `Flow<Boolean> reducedMotionEnabled`. Toggle row in `ParentDashboardScreen` — the approved home per Ground Rule 8/9, no new screen. Every component with a `reducedMotion` parameter currently defaulted `false` now reads the real preference via `CompositionLocal` or hoisted state (whichever fits existing project convention). When enabled: bounce/scale/particle animations replaced with simple fades everywhere. Keep each ViewModel/component touch to "read and pass through a `Boolean`" — nothing more.
- **Acceptance Criteria:** Toggling in Parent Dashboard visibly changes behavior on at least button tap feedback, star reveal, map node pulse, celebration confetti; no animated component from Phases 4/6/7 left un-audited.
- **Verification Checklist:** ☐ Build green ☐ Manual toggle-and-walkthrough (Map → Hear It → correct/incorrect answer → Letter Complete) confirms fade-only behavior throughout when enabled.
- **Android Studio Runtime Tests:** Toggle reduced-motion on; walk the full loop above; toggle off and repeat, confirming animations return.
- **Git Commit Message:** `feat: implement reduced-motion preference and wire into all animated components`
- **Estimated Difficulty:** High (breadth). **Expected Duration:** 4h.
- **Stop Condition:** Stop once the full walkthrough passes with the toggle both on and off, and every component from the file list is confirmed audited.

### Task UI-8.02 — Contrast audit and fix pass
- **Objective:** Verify and correct text/background contrast across all 12 screens against 4.5:1 (7:1 on Parent Dashboard, cross-referencing UI-5.11).
- **Why it matters:** Comprehensive spec is only as good as consistent enforcement — the realistic risk is partial compliance (e.g., 10 of 12 screens correct).
- **Files affected:** Any screen file where an issue is found (likely candidates: Caption-tier text on Soft Sky backgrounds, disabled-state text).
- **Implementation Rules:** Systematically check every `Text` composable's color/background pair across all 12 screens. Where a combination fails, adjust to the nearest compliant existing token rather than inventing a new ad-hoc color; flag to design as a token-level gap only if genuinely no compliant pairing exists. Document every fix (screen + element + before/after) in the commit body.
- **Acceptance Criteria:** Zero known sub-4.5:1 (sub-7:1 dashboard) pairs remain.
- **Verification Checklist:** ☐ Build green ☐ Contrast-checking tool spot-check across all 12 screens.
- **Android Studio Runtime Tests:** N/A beyond visual/tool-assisted contrast check — this is primarily a static audit task.
- **Git Commit Message:** `fix: contrast compliance pass across all screens`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once all 12 screens pass the contrast tool check, with every fix logged in the commit body.

### Task UI-8.03 — Color-independence pass
- **Objective:** Verify and correct every color-coded state in the app to also carry an icon, shape, label, or animation cue.
- **Why it matters:** "Never rely on color alone" is explicit and repeatedly emphasized; most component work from earlier phases should already comply — this task catches true remaining gaps.
- **Files affected:** `MapNode.kt`, `FindItScreen.kt`, `ParentDashboardScreen.kt`, `HeartDisplay.kt`, `AnswerFeedback.kt` — any component carrying color-coded meaning.
- **Implementation Rules:** Build a checklist of every color-coded state in the app (locked/unlocked/completed map nodes; correct/incorrect/disabled Find-It images; Green/Yellow/Red dashboard status; heart-full/heart-empty) and confirm each has a non-color differentiator already in place from earlier phases. Fix any true gap found.
- **Acceptance Criteria:** Every checklist item passes a grayscale-screenshot legibility test.
- **Verification Checklist:** ☐ Build green ☐ Grayscale screenshot check specifically on Map, Find It, Parent Dashboard.
- **Android Studio Runtime Tests:** Take screenshots of Map, Find It, and Parent Dashboard in representative states; desaturate and confirm legibility.
- **Git Commit Message:** `fix: color-independence pass — pair all color-coded states with icon/shape/label cues`
- **Estimated Difficulty:** Low-Medium (mostly verification). **Expected Duration:** 2h.
- **Stop Condition:** Stop once the grayscale test passes on all 3 highest-risk screens plus any others carrying color-coded state.

### Task UI-8.04 — Screen-reader / content-description pass
- **Objective:** Ensure every interactive element across all 12 screens has an appropriate `contentDescription`/semantics label, and every instructional text has an audio counterpart.
- **Why it matters:** "Text+audio always" is the single most repeated accessibility rule in the spec; this is the dedicated sweep confirming it holds everywhere, not just on mascot lines. Builds on the two specific fixes already made in **ENG-2.06**/IMPROVEMENT-09.
- **Files affected:** All 12 screen files (audit pass).
- **Implementation Rules:** Every tappable component gets a non-null, non-generic `contentDescription`/`Modifier.semantics`. Every instructional text block (not just mascot lines) checked for an audio counterpart — flag and fix any text-only instructional copy. Use TalkBack to manually walk at least one full core-loop session.
- **Acceptance Criteria:** No interactive element found with a missing or generic (`"Button"`, `"Image"`) description during the TalkBack walkthrough; no instructional text found without an audio counterpart.
- **Verification Checklist:** ☐ Build green ☐ Full TalkBack walkthrough of Map → Hear It → Say It → Find It → Complete, documented pass/fail per screen.
- **Android Studio Runtime Tests:** Enable TalkBack, complete the full core-loop walkthrough, documenting any gap found and fixed.
- **Git Commit Message:** `fix: screen-reader content-description and text+audio compliance pass`
- **Estimated Difficulty:** Medium-High (breadth). **Expected Duration:** 3h.
- **Stop Condition:** Stop once the full TalkBack walkthrough passes with zero gaps across the entire core loop.

**Quality Gate 8 → 9:** [x] All 4 tasks committed [x] Reduced-motion toggle confirmed working end-to-end [x] Contrast tool passes on all 12 screens [x] Grayscale legibility passes [x] TalkBack walkthrough passes [x] Build green. **This is the Design System's accessibility mandate closed in full — treat as a hard compliance gate.**

---

## 14. PHASE 9 — Microinteractions & Final UI QA

**Purpose:** The smaller, high-polish details that are easy to skip if attempted too early — tap-feedback consistency, branded loading states, and empty/error states.

**Objectives:** Zero bespoke tap animations remaining outside the shared modifier; zero default Material spinners; graceful, on-brand empty and error states everywhere they're needed.

**Expected Deliverables:** `LoadingIndicator.kt`, `EmptyState.kt`, `ErrorState.kt`, a tap-animation consistency sweep.

**Dependencies:** Phase 8 complete and gated.

**Risks:** Low — this phase is mostly verification-and-fill on top of already-solid Phase 4/5 work.

**Definition of Done:** All 3 tasks committed; zero non-conforming tap animations; zero default spinners; first-launch and at least one simulated asset-failure path both use the new components.

**Estimated Complexity:** Low-Medium. **Priority:** P2.

### Task UI-9.01 — Button press micro-interaction consistency pass
- **Objective:** Verify every button/tile/tappable uses the shared `tapFeedback` modifier (UI-4.11) rather than a bespoke or missing tap animation.
- **Why it matters:** Even after Phase 4's components exist, not every Phase 5 tappable may have been fully migrated — subtle per-screen drift risk.
- **Files affected:** Any remaining screen/component found with non-conforming or missing tap animation.
- **Implementation Rules:** Grep the project for manual `animateFloatAsState`/scale-related tap-animation code outside `ui/util/TapFeedbackModifier.kt`; migrate each hit. Cross-check against UI-8.04's content-description sweep — the two checklists largely overlap.
- **Acceptance Criteria:** Zero duplicate/bespoke tap-animation implementations remain outside the shared modifier.
- **Verification Checklist:** ☐ Build green ☐ Manual tap-through of all 12 screens confirms consistent feel.
- **Android Studio Runtime Tests:** Tap through every interactive element on all 12 screens once, confirming uniform bounce feel.
- **Git Commit Message:** `refactor: migrate remaining tap animations to shared tapFeedback modifier`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once the grep returns zero remaining bespoke implementations.

### Task UI-9.02 — Build branded loading-state component
- **Objective:** Replace any remaining default `CircularProgressIndicator` (or silent loading gap) with one shared, branded loading component.
- **Why it matters:** A generic spinner visually clashes with the app's rounded, friendly identity; silent gaps read as freezes.
- **Files affected:** `ui/components/LoadingIndicator.kt` (new), `SplashScreen.kt` (UI-5.01), `HearItScreen.kt` (audio load), `ReportPreviewScreen.kt` (UI-5.12, PDF generation), any other screen with a silent async gap (e.g., Coil image loads).
- **Implementation Rules:** One shared composable using a brand shape (pulsing star or letter-tile motif) at micro-to-standard duration per loop, not a generic spinner. Includes a spoken/announced label parameter ("Loading sound…") for screen readers. Wire into Splash, Hear It audio load, and Report Preview PDF generation at minimum; audit other screens for silent gaps.
- **Acceptance Criteria:** No default Material spinner remains anywhere; every usage includes an accessible label.
- **Verification Checklist:** ☐ Build green ☐ Manual export test on Report Preview confirms the branded loading state appears.
- **Android Studio Runtime Tests:** Trigger a report export and confirm the branded (not default) loading indicator appears with an announced label.
- **Git Commit Message:** `feat: add shared branded loading-state component`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once all 3+ named usages are confirmed using the new component, with accessible labels present.

### Task UI-9.03 — Build empty-state and error-state components
- **Objective:** Build shared, on-brand `EmptyState` and `ErrorState` components for zero-profiles-on-first-launch and graceful missing/corrupt-asset handling.
- **Why it matters:** Master Context §10 explicitly requires "never crash on a missing/corrupt asset — degrade gracefully"; this task gives that requirement a proper UI, not just a non-crash fallback.
- **Files affected:** `ui/components/EmptyState.kt`, `ui/components/ErrorState.kt` (new), `ProfileSelectScreen.kt` (zero-profile empty state), any asset-loading call site with error handling.
- **Implementation Rules:** `EmptyState`: mascot + message + single CTA slot, fade-tier entrance, used for first-launch ProfileSelect and any dashboard-with-no-data context. `ErrorState`: friendly, non-alarming icon (not a system error glyph or red triangle), text+audio message framed as the app's problem, paired with a retry/alternative-action CTA — never a dead end. Distinct entrance animation from `AnswerFeedback`'s incorrect-answer state (UI-7.02) so children don't conflate "you made a mistake" with "something broke." Does not change any underlying error-handling logic — visual only, except where a ViewModel needs to expose an error state it doesn't yet expose (Ground Rule 5 exception).
- **Acceptance Criteria:** Zero-profile first launch uses `EmptyState`, not a blank grid; at least one real asset-loading error path uses `ErrorState` instead of silent failure/crash (verified by manually removing/renaming a test asset in a debug build).
- **Verification Checklist:** [x] Build green [x] Manual test of zero-profile first-launch path [x] Manual test of one simulated missing-asset path.
- **Android Studio Runtime Tests:** Fresh-install with zero profiles, confirm `EmptyState` renders; rename/remove one test audio asset in a debug build and confirm `ErrorState` renders instead of a crash.
- **Git Commit Message:** `feat: add shared EmptyState and ErrorState components with graceful asset-failure handling`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once both the zero-profile and simulated-missing-asset paths are confirmed non-crashing and on-brand.

**Quality Gate 9 → 10:** [x] All 3 tasks committed [x] Zero bespoke tap animations remain [x] Zero default spinners remain [x] Empty/error states confirmed non-crashing on-brand [x] Build green.

---

## 15. PHASE 10 — Testing & Quality Assurance

**Purpose:** Close the remaining UI Playbook prompts (P-44–P-47: dialogs, sound wiring, cross-screen consistency, animation performance) and Master Context's testing backlog (T-14) and Engineering Review's testing coverage gaps together, since both converge on the same "is this actually solid" question before deployment prep begins.

**Objectives:** Dialogs fully compliant; all 6 spec-mandated sounds correctly and distinctly wired; a full cross-screen consistency sweep with zero open findings; no animation jank on the two highest-risk hotspots (Map, celebration screens); domain-layer test coverage materially improved; instrumented DAO tests added.

**Expected Deliverables:** Polished `ArithmeticGateDialog`, complete `SoundManager` wiring, a documented consistency QA pass, a documented performance profile, expanded unit test suite, new instrumented tests.

**Dependencies:** Phase 9 complete and gated.

**Risks:** UI-10.03 (full consistency QA pass) is explicitly audit-first, fix-second — resist the urge to let it become an unscoped re-polish of everything; spin out non-trivial findings as follow-up tasks per its own implementation rule.

**Definition of Done:** All 6 tasks committed; `./gradlew test` green with materially improved coverage; full manual regression walkthrough passes; no dropped-frame jank observed on Map or celebration sequences.

**Estimated Complexity:** High (breadth + the highest-stakes pre-deployment gate). **Priority:** P1.

### Task UI-10.01 — Polish dialogs (destructive-action arithmetic gate and others)
- **Objective:** Bring all dialogs, especially the destructive-action arithmetic gate, to full Design System compliance.
- **Why it matters:** Dialogs are easy to leave as bare system `AlertDialog`s, visually disconnected from the rest of the app — including on the app's one truly high-stakes confirmation moment (deleting a child's progress).
- **Files affected:** `ui/components/ArithmeticGateDialog.kt` (locate or create), `ParentDashboardScreen.kt` (delete profile / reset progress entry points).
- **Implementation Rules:** Dialog container uses `LearningCard`-family styling, not a default `AlertDialog` shape. States the consequence in plain text+audio before presenting the arithmetic challenge. Cancel at least as visually prominent as Confirm; numeric input meets 48dp+. `GentleCorrectionOrange` + warning icon for severity, never harsh red. Does not change arithmetic-gate logic — display/interaction only.
- **Acceptance Criteria:** Dialog visually matches app design language; Cancel not visually subordinate to Confirm.
- **Verification Checklist:** [x] Build green [x] Manual trigger of a destructive action on a test profile confirms full compliant flow.
- **Android Studio Runtime Tests:** Trigger delete-profile and reset-progress flows on a disposable test profile, confirming dialog styling and Cancel/Confirm balance.
- **Git Commit Message:** `style: polish destructive-action arithmetic gate dialog to Design System v1.0`
- **Estimated Difficulty:** Low-Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once both destructive-action entry points are confirmed compliant on a test profile.

### Task UI-10.02 — Sound design wiring pass
- **Objective:** Verify and complete wiring of all 6 spec-mandated sounds to their exact events, with no shared/reused sound files across distinct events.
- **Why it matters:** Easy to under-implement (e.g., reusing one "ding" for both correct-answer and heart-recovery) since the gap isn't visible in a UI-only review; this closes every sound hook left pending from UI-7.02, UI-7.03, UI-5.08/UI-5.10.
- **Files affected:** `data/audio/SoundManager.kt` (or equivalent), `AnswerFeedback.kt`, `HeartDisplay.kt`, `MapNode.kt`, `LetterCompleteScreen.kt`, `BlendItCompleteScreen.kt`.
- **Implementation Rules:** Confirm 6 distinct assets exist, mapped 1:1: correct→reward chime; incorrect→soft pop; heart loss→gentle whoosh; heart recovery→bright sparkle; node unlock→magical chime; level complete→celebration fanfare. Fix any two-events-one-file case found. Use the shared `AudioPlayer`'s overlap-prevention (Master Context §8) so simultaneous events don't collide (e.g., heart-recovery sparkle firing mid-chime).
- **Acceptance Criteria:** All 6 events produce audibly distinct sounds; no overlapping/garbled audio on near-simultaneous events.
- **Verification Checklist:** ☐ Build green ☐ Manual audio walkthrough triggering all 6, including one deliberate near-simultaneous pair.
- **Android Studio Runtime Tests:** Trigger all 6 events across a full session, including forcing a correct-answer-immediately-followed-by-heart-recovery overlap case.
- **Git Commit Message:** `feat: complete sound design wiring pass for all six spec-mandated events`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2.5h.
- **Stop Condition:** Stop once all 6 events are confirmed distinct and the overlap case is confirmed non-garbled.

### Task UI-10.03 — Full cross-screen consistency QA pass
- **Objective:** Run one comprehensive consistency checklist across all 12 screens and every shared component, catching any drift left by Phases 3–9.
- **Why it matters:** The cumulative risk across ~47 UI tasks is small inconsistencies surviving individual phase reviews; this is the dedicated integration check.
- **Files affected:** Potentially any file, depending on findings — audit-first, fix-second.
- **Implementation Rules:** Walk one checklist covering: token usage (re-grep the whole project for stray color/spacing/type literals), shared-component adoption (every component from Phase 4/6/9 used everywhere applicable, no orphaned duplicates), touch-target compliance, one-primary-CTA-per-screen compliance, banned-phrase absence. Log every finding with screen + fix in the commit body (this doubles as the project's final compliance record). Any finding requiring more than a trivial one-line fix is spun out as a follow-up task, not scope-crept into this pass.
- **Acceptance Criteria:** Checklist fully walked across all 12 screens with zero open findings, or all findings explicitly logged as follow-up tasks.
- **Verification Checklist:** ☐ `./gradlew assembleDebug` succeeds ☐ Full manual walkthrough of the entire app, screen by screen, following the checklist.
- **Android Studio Runtime Tests:** Full app walkthrough, screen by screen, checklist item by checklist item.
- **Git Commit Message:** `chore: cross-screen Design System consistency QA pass`
- **Estimated Difficulty:** High (breadth). **Expected Duration:** 4h.
- **Stop Condition:** Stop once the checklist is fully walked and every finding is either fixed trivially or logged as a named follow-up task — do not let this expand into open-ended re-polish.

### Task UI-10.04 — Animation and rendering performance pass
- **Objective:** Verify no animation introduced across Phases 4–9 causes dropped frames, particularly on Map (infinite pulse + up to 35 nodes) and celebration screens (confetti + star sequence).
- **Why it matters:** Motion was layered onto already-complex screens across many phases; this confirms it's actually smooth on representative hardware, not just correct on paper.
- **Files affected:** `MapNode.kt`, `CelebrationEffect.kt`, `StarRating.kt` (likely hotspots; others as profiling indicates).
- **Implementation Rules:** Profile Map with a fully-unlocked, worst-case node-state profile loaded. Profile `LetterCompleteScreen`/`BlendItCompleteScreen` during the full celebration sequence (confetti + star reveal simultaneously). Where jank is found, prefer reducing simultaneous animated element count or `graphicsLayer`/hardware-layer hints over cutting animation quality; only simplify an animation if performance genuinely can't be recovered otherwise. Do not change timing/spec values that are already correct per Design System — fixes are implementation-level (recomposition scope, layer hints), not spec regressions.
- **Acceptance Criteria:** No dropped-frame jank on Map at worst-case node count during the pulse loop; none during the combined confetti+star-reveal sequence.
- **Verification Checklist:** ☐ `./gradlew assembleDebug` succeeds ☐ Frame-timing profile captured for both hotspots, findings noted in commit body (even "profiled, no jank observed at N fps" is acceptable if no fix was needed).
- **Android Studio Runtime Tests:** Use Android Studio's frame-timing/profiling tools on Map (worst-case unlocked state) and on both completion screens during full celebration.
- **Git Commit Message:** `perf: verify and tune animation performance on Map and celebration screens`
- **Estimated Difficulty:** Medium-High. **Expected Duration:** 3h.
- **Stop Condition:** Stop once both hotspots are profiled and confirmed jank-free (or fixed until they are).

### Task UI-10.05 — Expand domain-layer unit test coverage
*(closes Master Context T-14 + Engineering Review §6 coverage gaps beyond ENG-2.09)*
- **Objective:** Extend unit test coverage across the domain classes named in Master Context §10's testing philosophy (`HeartManager`, `StarCalculator`, `UnlockManager`, `GroupUnlockManager`, `SpeechValidator`, `StreakTracker`, `LetterStatusCalculator`, `RetentionCalculator`) toward the T-14 target of ≥80% domain-layer coverage.
- **Why it matters:** The Engineering Review confirms strong existing coverage on `UnlockManager`/`GroupUnlockManager`/star/streak/`SpeechValidator`/`SpellingEngine`, but flags no integration tests for `PlayItRepositoryImpl` and no instrumented UI tests — this task closes the domain-layer gap specifically (integration/instrumented tests are UI-10.06).
- **Files affected:** Existing test files under `test/`, extended; new test files for any domain class currently untested.
- **Implementation Rules:** Audit current coverage per-class against the Master Context §10 list; write tests for any gap, prioritizing classes with non-obvious business rules (e.g., `LetterStatusCalculator`'s Green/Yellow/Red thresholds, `RetentionCalculator`'s 7-day windowing). Reuse the `MainDispatcherRule` established in **ENG-2.09** where relevant.
- **Acceptance Criteria:** Domain-layer coverage reaches or exceeds 80% (Master Context T-14's own acceptance criterion), measured via the project's coverage tooling.
- **Verification Checklist:** ☐ `./gradlew test` green ☐ Coverage report generated and checked against the 80% target.
- **Android Studio Runtime Tests:** N/A — unit test task, run via `./gradlew test` + coverage tooling.
- **Git Commit Message:** `test: expand domain-layer unit test coverage toward 80% target`
- **Estimated Difficulty:** Medium-High. **Expected Duration:** 4h.
- **Stop Condition:** Stop once coverage tooling confirms the 80% domain-layer target is met, or once every named class in Master Context §10 has at least one meaningful test — whichever gives more real signal; do not pad coverage with trivial assertions purely to hit a number.

### Task UI-10.06 — Add instrumented Room DAO tests
*(closes Engineering Review §6: "No integration tests for PlayItRepositoryImpl")*
- **Objective:** Add `androidTest` instrumented tests for the Room DAOs and `PlayItRepositoryImpl`, since Master Context §10 explicitly calls for instrumented tests on DAOs and none currently exist.
- **Why it matters:** Unit tests alone can't catch real SQL/FK/migration behavior — this is the gap most likely to hide a regression like **ENG-1.01**'s groupId bug in the future if left permanently untested.
- **Files affected:** New files under `androidTest/`.
- **Implementation Rules:** Use an in-memory Room database for instrumented tests. Cover at minimum: `PhonemeDao.getUnlockedPhonemes` (the consolidated logic from **ENG-2.14**), `BlendItProgress` upsert (regression-guards **ENG-1.01**'s fix specifically), the unique constraint added in **ENG-2.08**.
- **Acceptance Criteria:** At least 3 instrumented DAO/repository tests exist and pass, covering the 3 areas above.
- **Verification Checklist:** ☐ `./gradlew connectedAndroidTest` (or equivalent) green.
- **Android Studio Runtime Tests:** Run the instrumented test suite on an emulator/device.
- **Git Commit Message:** `test: add instrumented Room DAO and repository tests`
- **Estimated Difficulty:** Medium. **Expected Duration:** 3h.
- **Stop Condition:** Stop once the 3 minimum-coverage areas each have a passing instrumented test — specifically regression-guarding the BUG-06/groupId fix is non-negotiable given its severity.

**Quality Gate 10 → 11:** ☐ All 6 tasks committed ☐ `./gradlew test` and instrumented tests both green ☐ Domain-layer coverage ≥80% or fully justified otherwise ☐ Full manual regression walkthrough passes ☐ No animation jank on Map/celebration hotspots ☐ Cross-screen QA checklist fully walked. **This is the last gate before the app is considered feature-and-polish-complete.**

---

## 16. PHASE 11 — Codebase Cleanup & Build Hardening

**Purpose:** Close the remaining Engineering Review Build Configuration findings and any follow-up tasks logged during Phase 10's consistency pass, before deployment prep begins.

**Objectives:** `exportSchema = true` with a checked-in schema baseline; Compose BOM updated; `kapt`→`ksp` migration evaluated; any Phase 10 follow-up findings closed; T-15 (optional Hilt DI) explicitly dispositioned, not left ambiguous.

**Expected Deliverables:** Updated build config, schema JSON checked in, a documented decision on Hilt DI.

**Dependencies:** Phase 10 complete and gated.

**Risks:** `kapt`→`ksp` migration can surface unrelated annotation-processing issues — treat as its own isolated, revertible task, not bundled with other cleanup.

**Definition of Done:** All 5 tasks committed; build config matches Engineering Review §10's "must enable for GA"/pre-GA recommendations in full; T-15 has an explicit recorded decision either way.

**Estimated Complexity:** Medium. **Priority:** P2.

### Task CLEAN-11.01 — Enable exportSchema and check in schema baseline
- **Objective:** Set `exportSchema = true` and check the generated schema JSON into version control, giving **ENG-1.03**'s `fallbackToDestructiveMigration()` safety net a real baseline to eventually diff real migrations against.
- **Why it matters:** Flagged in Engineering Review §10 as "change to true before GA."
- **Files affected:** `PlayItDatabase.kt` (annotation), `build.gradle.kts` (schema output directory config), new `schemas/` directory.
- **Implementation Rules:** Set `exportSchema = true`; configure the KSP/kapt schema-location argument; commit the generated JSON.
- **Acceptance Criteria:** Schema JSON exists in version control and regenerates correctly on a clean build.
- **Verification Checklist:** ☐ Build green ☐ Schema file present and non-empty after a clean build.
- **Android Studio Runtime Tests:** None beyond build verification.
- **Git Commit Message:** `chore: enable Room schema export and check in baseline schema`
- **Estimated Difficulty:** Low. **Expected Duration:** 1h.
- **Stop Condition:** Stop once the schema file is confirmed generated and committed.

### Task CLEAN-11.02 — Update Compose BOM
- **Objective:** Move off the dated `2023.10.01` Compose BOM to a current 2024.x+ release.
- **Why it matters:** Flagged as "slightly dated" in Engineering Review §10 — low urgency but a clean, low-risk win before deployment.
- **Files affected:** `app/build.gradle.kts` (or version catalog).
- **Implementation Rules:** Bump the BOM version; resolve any resulting API deprecation warnings introduced by the bump. Do not adopt new Compose APIs beyond what's needed to fix deprecations — this is a version-currency task, not a rewrite.
- **Acceptance Criteria:** Build green on the new BOM; no new deprecation warnings introduced.
- **Verification Checklist:** ☐ Build green ☐ Full manual regression smoke test (one pass through the core loop) confirms no visual/behavioral regression from the bump.
- **Android Studio Runtime Tests:** One full core-loop walkthrough post-bump.
- **Git Commit Message:** `chore: update Compose BOM to current stable release`
- **Estimated Difficulty:** Low-Medium (depends on deprecation surface). **Expected Duration:** 1.5h.
- **Stop Condition:** Stop once the build is green and the smoke test shows no regression. If the bump surfaces a large deprecation surface, stop and flag rather than mass-migrating APIs under this task's scope.

### Task CLEAN-11.03 — Evaluate and execute kapt→ksp migration for Room
- **Objective:** Migrate Room's annotation processing from `kapt` to `ksp` for faster builds, per Engineering Review §10's "consider migrating."
- **Why it matters:** Build-speed improvement with no behavior change, but with real risk of surfacing annotation-processing edge cases — worth isolating as its own revertible task.
- **Files affected:** `build.gradle.kts` (plugin declarations, dependency configuration for Room's compiler).
- **Implementation Rules:** Replace `kapt("androidx.room:room-compiler:...")` with `ksp("androidx.room:room-compiler:...")` and the corresponding plugin swap. If any other library in the project still requires `kapt`, both can coexist — do not force-migrate unrelated processors in this task.
- **Acceptance Criteria:** Build green with `ksp` generating Room's code; build time measurably improved (log wall-clock time before/after).
- **Verification Checklist:** ☐ `./gradlew clean assembleDebug` succeeds ☐ Full core-loop smoke test confirms no behavior change from the processor swap.
- **Android Studio Runtime Tests:** One full core-loop walkthrough post-migration.
- **Git Commit Message:** `build: migrate Room annotation processing from kapt to ksp`
- **Estimated Difficulty:** Medium. **Expected Duration:** 2h.
- **Stop Condition:** Stop once build is green and the smoke test passes. If `ksp` surfaces a Room-specific incompatibility, revert this single commit rather than debugging indefinitely — it's a speed optimization, not a blocker.

### Task CLEAN-11.04 — Close any Phase 10 follow-up findings
- **Objective:** Execute any non-trivial findings logged as follow-ups during **UI-10.03**'s consistency QA pass.
- **Why it matters:** Those findings were deliberately deferred rather than scope-creeping Phase 10 — this task is where they're actually closed, not left permanently open.
- **Files affected:** Whatever UI-10.03's findings named.
- **Implementation Rules:** Treat each follow-up as its own mini-task within this slot: one finding, one fix, verify, note in the shared commit body. If UI-10.03 found zero non-trivial follow-ups, this task is a no-op — record that explicitly rather than silently skipping.
- **Acceptance Criteria:** Every follow-up from UI-10.03 is closed or explicitly re-deferred with a stated reason.
- **Verification Checklist:** ☐ Build green ☐ Each follow-up finding re-verified fixed.
- **Android Studio Runtime Tests:** Scoped to whatever the specific findings require.
- **Git Commit Message:** `fix: close outstanding Phase 10 consistency QA follow-up findings`
- **Estimated Difficulty:** Variable (depends on findings). **Expected Duration:** 1–3h.
- **Stop Condition:** Stop once every named follow-up is closed or explicitly re-deferred with a documented reason — never silently drop one.

### Task CLEAN-11.05 — Disposition Hilt DI adoption (Master Context T-15)
- **Objective:** Make and record an explicit decision on whether to adopt Hilt DI, per Master Context §8/§11/§12 T-15 — "not part of the current tech stack... needs an explicit decision before use."
- **Why it matters:** Master Context explicitly flags this as requiring sign-off rather than silent adoption or silent permanent deferral; leaving it ambiguous violates the roadmap's own conflict-resolution discipline (Ground Rule 13).
- **Files affected:** None if the decision is "defer" (this task produces a documented decision, not necessarily code). If the decision is "adopt," this becomes a separately-scoped follow-up roadmap addendum — not silently absorbed into this cleanup task.
- **Implementation Rules:** Given the manual-DI pattern is already consistent and correct throughout the reviewed codebase (Engineering Review §1.2: "No Hilt/DI framework used. Manual factory injection is consistent and intentional. Acceptable for this scope"), **the recommended default disposition is: defer Hilt adoption, no behavior change, revisit only if a future session finds manual DI genuinely limiting.** Record this decision explicitly rather than leaving T-15 open indefinitely.
- **Acceptance Criteria:** A recorded decision exists (adopt-later-as-new-roadmap-item, or defer-indefinitely-documented) — either is acceptable; silence is not.
- **Verification Checklist:** ☐ Decision recorded in commit message or a short `docs/` note.
- **Android Studio Runtime Tests:** None (decision-recording task).
- **Git Commit Message:** `docs: disposition Hilt DI adoption (T-15) — deferred, manual DI remains sufficient`
- **Estimated Difficulty:** Low. **Expected Duration:** 0.5h.
- **Stop Condition:** Stop once the decision is recorded — do not begin a Hilt migration under this task even if the decision leans "adopt"; that would need its own fully-scoped roadmap phase.

**Quality Gate 11 → 12:** ☐ All 5 tasks committed ☐ `exportSchema` schema file present ☐ Compose BOM current ☐ Build config matches all Engineering Review §10 pre-GA recommendations ☐ T-15 has a recorded decision ☐ Build green.

---

## 17. PHASE 12 — Deployment Preparation

**Purpose:** Take the app from "feature-and-polish-complete, cleanly built" to "installable, signed, releasable artifact with a professional public presentation."

**Objectives:** Signed release APK/AAB; clean repository; professional README; release notes; a GitHub Release.

**Expected Deliverables:** Signed release build, `README.md` rewrite, architecture diagram, `CHANGELOG.md`/release notes, tagged GitHub Release.

**Dependencies:** Phase 11 complete and gated.

**Risks:** Signing-key handling is the one genuinely irreversible step here (a lost/rotated signing key breaks future update paths) — treat key generation/storage with extra care and document the process, don't just execute it silently.

**Definition of Done:** All checklist items below complete; a fresh clone of the repository, followed by the documented build steps, produces a working release artifact.

**Estimated Complexity:** Medium. **Priority:** P1.

### Deployment Checklist

- [ ] **DEPLOY-01 — Repository cleanup.** Remove dead code, commented-out blocks, stray debug logging (`Log.d` calls left from development), unused resources/assets. Confirm `.gitignore` covers local `local.properties`, build outputs, and signing material appropriately.
- [ ] **DEPLOY-02 — README rewrite.** Cover: project purpose (grounded in Master Context §1–§2, not invented claims per §13's rule), tech stack, architecture overview, setup/build instructions, screenshots (see DEPLOY-04), and a link to this roadmap for anyone continuing development.
- [ ] **DEPLOY-03 — Architecture diagram.** A visual (Mermaid, or an exported diagram image) of the MVVM + Clean Architecture layering per Master Context §5 — Presentation/Domain/Data, with the actual shared components and repository pattern shown. This is a documentation artifact, not new UI.
- [ ] **DEPLOY-04 — Screenshots.** Capture polished, post-Phase-9 screenshots of at minimum: Map, Hear It, Say It, Find It, a Letter Complete celebration, Blend It, Parent Dashboard. Use a consistent device frame/resolution across all captures.
- [ ] **DEPLOY-05 — Feature GIFs.** Short (5–10s) screen-recordings of: the map unlock animation, the star-reveal celebration, and one full Hear It→Say It→Find It cycle. Convert to GIF or short MP4 for README/portfolio embedding.
- [ ] **DEPLOY-06 — Signing configuration.** Generate (or confirm existing) a release signing key; document the secure storage process (e.g., not committed to the repo); wire `signingConfig` into `build.gradle.kts`'s release variant.
- [ ] **DEPLOY-07 — APK/AAB generation.** Produce a final signed release build (`./gradlew bundleRelease` for Play-Store-track AAB, `./gradlew assembleRelease` for a directly-distributable APK). Confirm the Phase 1 minification/ProGuard setup (**ENG-1.02**) is active in this build.
- [ ] **DEPLOY-08 — Versioning.** Set a real `versionName`/`versionCode` (e.g., `1.0.0` / `1`) in `build.gradle.kts`, following semantic versioning for any future updates.
- [ ] **DEPLOY-09 — Release notes / CHANGELOG.** Document what's in this release, grounded in what was actually built/fixed across Phases 0–11 of this roadmap — not invented feature claims.
- [ ] **DEPLOY-10 — GitHub Release.** Tag the release commit, attach the signed APK (or a note on where to obtain the AAB), publish release notes.

**Quality Gate 12 → 13:** ☐ All 10 checklist items complete ☐ A fresh clone + documented build steps produces a working signed release artifact ☐ GitHub Release published.

---

## 18. PHASE 13 — Portfolio Preparation

**Purpose:** Translate the now-verified, production-quality codebase into resume/interview-ready material — strictly grounded in what was actually built and reviewed, per Master Context §13's explicit rule against invented metrics.

**Objectives:** Resume bullet(s) ready to use; a portfolio write-up; interview talking points grounded in real architecture decisions and real defects fixed (which are themselves good interview material — "I found and fixed a silent data-loss bug in a Room FK constraint" is a stronger story than a feature list).

**Expected Deliverables:** Portfolio project page/write-up, resume bullet(s), a short list of defensible interview talking points.

**Dependencies:** Phase 12 complete and gated (portfolio material should describe a shipped artifact, not an in-progress one).

**Risks:** The main risk is the one Master Context §13 explicitly names — reverting to invented or unverified completion/quality claims. This roadmap's entire Phase 0–11 verification trail exists precisely to prevent that; use it.

**Definition of Done:** All checklist items complete; every claim in the portfolio write-up and resume bullet(s) is traceable to a specific phase/task in this roadmap or a specific finding in the Engineering Review.

**Estimated Complexity:** Low-Medium. **Priority:** P2.

### Portfolio Checklist

- [ ] **PORT-01 — Portfolio write-up.** Describe playIT using the Master Context §13 candidate framing as a base: *"Architected an offline-first Android application (Clean Architecture, MVVM, Room, Kotlin Coroutines/Flow) delivering three-stage phonics instruction with on-device speech recognition — zero network dependency, zero child voice data leaving the device."* Extend with specifics now verifiable post-roadmap: the RC-1 defects found and fixed (real engineering-review-driven hardening is a legitimate, strong portfolio narrative), the 47-task UI/UX polish pass, and the accessibility compliance work.
- [ ] **PORT-02 — Portfolio screenshots.** Reuse DEPLOY-04's screenshots, or capture a curated subset optimized for a portfolio site's layout (fewer, higher-impact images vs. README's more complete set).
- [ ] **PORT-03 — Demo video.** A 60–90s narrated or captioned walkthrough: Profile creation → Map → one full Hear It/Say It/Find It cycle → Letter Complete celebration → a glimpse of the Parent Dashboard. Should visually demonstrate the polish work from Phases 3–9, not just functionality.
- [ ] **PORT-04 — Resume bullet(s).** 1–2 lines, grounded in Master Context §13's framing plus one concrete, verifiable metric from this roadmap (e.g., domain-layer test coverage % from **UI-10.05**, or "resolved N release-candidate defects across a full engineering review" — a real, defensible number from §11 of the Engineering Review, not an invented one).
- [ ] **PORT-05 — Interview talking points.** A short prepared list: (1) the Clean Architecture boundary decisions and why they matter, (2) the offline speech-recognition privacy angle, (3) at least one specific bug found/fixed during this roadmap (e.g., **ENG-1.01**'s FK-violation-causing-silent-data-loss) as a concrete "tell me about a bug you fixed" story, (4) the accessibility compliance work as a concrete "tell me about inclusive design" story.
- [ ] **PORT-06 — Application-ready assets.** A single folder/zip bundling: resume bullet text, portfolio write-up, screenshots, demo video, and a link to the GitHub Release — ready to attach to internship/job applications without last-minute assembly.
- [ ] **PORT-07 — Future roadmap note.** A short, honest "what's next" section (e.g., T-15's Hilt decision, any Phase 10 follow-ups still open, potential content/curriculum expansion) — shows ongoing engineering judgment rather than implying the project is "finished forever."

**Quality Gate 13 → 14:** ☐ All 7 checklist items complete ☐ Every claim in PORT-01/PORT-04 spot-checked against this roadmap's actual completed tasks — zero invented metrics, per Ground Rule 10 and Master Context §13.

---

## 19. PHASE 14 — Production Release

**Purpose:** The final go/no-go gate and release execution.

**Objectives:** Confirmed production-ready artifact, published and available.

**Expected Deliverables:** A live release (GitHub Release at minimum; app-store submission if that's the chosen distribution channel — not specified in the source documents, so this roadmap does not assume one).

**Dependencies:** Phase 13 complete and gated.

**Risks:** None new — this phase is a gate-check and publish action, not new engineering work. If any earlier gate was skipped or force-passed, that risk surfaces here.

**Definition of Done:** All prior 13 phases' quality gates re-confirmed in one final pass; artifact published.

**Estimated Complexity:** Low. **Priority:** P0 (the actual finish line).

### Production Release Checklist

- [ ] **RELEASE-01** — Re-confirm Phase 1's gate: all 5 GA-blocking defects verifiably fixed (re-read the Engineering Review's Final Verdict paragraph one final time).
- [ ] **RELEASE-02** — Re-confirm Phase 10's gate: tests green, coverage target met or justified, no animation jank on the two hotspot screens.
- [ ] **RELEASE-03** — Re-confirm Phase 12's gate: signed artifact builds cleanly from a fresh clone.
- [ ] **RELEASE-04** — Final full manual regression walkthrough on a physical device (not just emulator) covering the entire core loop plus Parent Dashboard/Report export.
- [ ] **RELEASE-05** — Publish the GitHub Release (if not already done in DEPLOY-10) and/or submit to the chosen distribution channel.
- [ ] **RELEASE-06** — Tag this roadmap document itself (e.g., `docs: mark playIT_Execution_Roadmap.md phases 0-14 complete`) so the repository's history reflects the full journey from RC-1 review to production release.

**No further quality gate follows this phase — this is the roadmap's terminal state.**

---

## 20. Quality Gates — Consolidated Reference

Every phase transition above has its own inline gate. This table is the fast-lookup version — check it before declaring any phase complete.

| Gate | Engineering Review | Build Green | Runtime Verify | Compose Preview | Accessibility | Performance | Manual Test | Regression Test | Git Commit |
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| 0→1 | — | ✅ | ✅ | — | — | — | — | ✅ (baseline) | ✅ |
| 1→2 | ✅ (Final Verdict re-check) | ✅ (release) | ✅ | — | — | — | ✅ | ✅ (full) | ✅ ×5 |
| 2→3 | ✅ (findings closed/deferred) | ✅ | ✅ | — | — | ✅ (N+1, animation) | ✅ | ✅ | ✅ ×17 |
| 3→4 | — | ✅ | — | ✅ | — | — | — | — | ✅ ×6 |
| 4→5 | — | ✅ | ✅ | ✅ | — | — | ✅ | — | ✅ ×11 |
| 5→6 | — | ✅ | ✅ | — | — | — | ✅ (per screen) | ✅ | ✅ ×12 |
| 6→7 | — | ✅ | ✅ | — | — | — | ✅ | — | ✅ ×3 |
| 7→8 | — | ✅ | ✅ | — | — | — | ✅ | ✅ | ✅ ×4 |
| 8→9 | — | ✅ | ✅ | — | ✅ (hard gate) | — | ✅ (TalkBack) | — | ✅ ×4 |
| 9→10 | — | ✅ | ✅ | — | — | — | ✅ | — | ✅ ×3 |
| 10→11 | ✅ (T-14 closure) | ✅ | ✅ | — | — | ✅ (hard gate) | ✅ | ✅ (full) | ✅ ×6 |
| 11→12 | ✅ (§10 config) | ✅ | — | — | — | — | ✅ | — | ✅ ×5 |
| 12→13 | — | ✅ | — | — | — | — | — | — | ✅ (release) |
| 13→14 | ✅ (§13 metrics check) | — | — | — | — | — | — | — | — |
| 14 (final) | ✅ | ✅ | ✅ (physical device) | — | — | — | ✅ | ✅ (full) | ✅ (tag) |

---

## 21. Antigravity Execution Mode — Reusable Session Prompt

Paste this verbatim into Antigravity at the start of **every** development session.

```
You are executing against playIT_Execution_Roadmap.md, the single source of
truth for this project's remaining work. Do the following, in order:

1. Open playIT_Execution_Roadmap.md and identify the current phase by finding
   the first phase whose Quality Gate is NOT fully checked off. Within that
   phase, identify the first Task ID not yet committed.

2. State the Task ID, its Title, and its Stop Condition back to me before
   writing any code, so we both agree on scope before you start.

3. Implement ONLY that task. Do not implement any other Task ID, even if it
   looks related, even if a fix seems trivial. If implementing this task
   reveals that a Ground Rule (§2) would be violated to complete it as
   written, STOP and flag it — do not improvise past it or expand scope.

4. Build the project (./gradlew assembleDebug at minimum, or the specific
   build/test command the task's Verification Checklist names).

5. Verify compilation succeeded and run the task's exact "Android Studio
   Runtime Tests" as written — do not skip these because the change "looks
   fine."

6. Explain what you changed and why, in plain terms, referencing the Task
   ID.

7. Confirm every item in the task's Acceptance Criteria and Verification
   Checklist against what you actually built — call out any that don't
   pass rather than glossing over them.

8. Suggest the exact Git commit message from the task card (or a documented,
   minor variant if the actual diff meaningfully differs from what the task
   card anticipated).

9. STOP. Do not proceed to the next Task ID. Wait for my review and explicit
   go-ahead before starting again from step 1.

If the task's "Files likely affected" don't match the real project
structure, locate the equivalent files by the project's naming conventions
(XScreen, XViewModel, XRepository/XRepositoryImpl) rather than guessing or
creating a parallel/duplicate file structure — flag the discrepancy to me
either way.

If you discover a new conflict, ambiguity, or a defect not already tracked
by this roadmap, do not silently fix it as a side effect of the current
task. Log it clearly in your explanation (step 6) so I can decide whether it
becomes a new Task ID.
```

---

## 22. Claude Review Mode — Reusable Session Prompt

Paste this into Claude immediately after Antigravity completes a session, before approving the commit.

```
Review ONLY the implementation completed in the Antigravity session I'm
about to paste below (or the diff/commit I'm about to share). Do not review
or comment on any other part of the codebase, and do not suggest unrelated
improvements — this is a scoped review of one Task ID against
playIT_Execution_Roadmap.md.

Evaluate the change against these dimensions, referencing the specific Task
ID's Acceptance Criteria, Implementation Rules, and Ground Rules (§2 of the
roadmap) as your standard — not general best practice in isolation from
the task's actual spec:

- Architecture — MVVM + Clean Architecture boundaries respected (Presentation
  / Domain / Data); no business logic leaked into a Composable; no Android
  framework type leaked into Domain.
- Compose — correct state hoisting, no unnecessary recomposition, correct
  use of remember/LaunchedEffect/derivedStateOf where relevant.
- Material 3 / Design System compliance — exact token usage (color, type,
  spacing, shape, elevation), no raw literals reintroduced.
- Accessibility — touch targets, contentDescription, contrast, color-
  independence, text+audio pairing, reduced-motion respect, as applicable
  to this specific task.
- Performance — no obvious new N+1 pattern, no unbounded recomposition, no
  unreleased resource, no jank-inducing animation.
- Naming — matches the project's established conventions (XScreen,
  XViewModel, XRepository/XRepositoryImpl, PascalCase Composables).
- Readability & Maintainability — would a future session understand this
  change without re-reading the whole file.
- UI Consistency — matches how equivalent components/screens already look
  and behave elsewhere in the app (no per-screen fork of a shared pattern).
- Design System Compliance — any Design System Violation the task was
  supposed to close is actually closed; no new violation introduced.
- Portfolio Quality — would this diff, shown in an interview or code
  review, read as deliberate, professional engineering — not just "it
  works."

Categorize every finding as Critical / High / Medium / Low:
- Critical: breaks a Ground Rule, reintroduces a GA-blocking-class defect,
  or causes data loss/crash risk.
- High: violates the specific task's Acceptance Criteria or Verification
  Checklist.
- Medium: Design System or consistency violation not covered by Acceptance
  Criteria but reasonably expected given the Ground Rules.
- Low: style/readability nit with no functional impact.

End with an explicit approval decision: APPROVE, APPROVE WITH FOLLOW-UPS
(list them as candidate new Task IDs, not silent fixes), or REJECT — REWORK
REQUIRED (list exactly what must change before re-review).

Do not approve a commit with any open Critical or High finding.
```

---

## 23. Engineering Dashboard

> **On these numbers:** Per Ground Rule 10 and Master Context §13, nothing below is invented. Each figure is derived directly from the evidence in the three source documents (what's confirmed built and reviewed vs. what remains as unexecuted roadmap tasks) as of this roadmap's authoring. Every number here should be treated as a **snapshot to re-verify at Phase 0**, not a permanent scoreboard — update this section at the end of every phase, not just at the end of the project.

| Metric | Value | Basis |
|---|---|---|
| **Overall Project Completion** | **~45%** | Core implementation (Master Context §12 T-01–T-14) is confirmed built end-to-end per the Engineering Review's RC-1 pass — architecture, data layer, all 12 screens, audio, domain use cases. However, that represents the *implementation* stage only. The *hardening-to-production* stage — Phases 1–14 of this roadmap, 103 items — has not yet been executed against this roadmap. Weighting implementation and hardening roughly evenly (a defensible split given the hardening phases include a full 47-task UI/UX pass, full accessibility compliance, testing, and deployment), ~45% is the fair estimate. |
| **Remaining Tasks** | **103** (80 Task IDs across Phases 0–11 + 23 checklist items across Phases 12–14) | Direct count from §4's Phase Map, none yet marked complete. |
| **Current Phase** | **Phase 0 — Repository Health Check & Verification** | No task in this roadmap has yet been executed against the current codebase; Phase 0 is the mandatory entry point per §4's dependency chain. |
| **Current Priority** | **P0 — Confirm baseline, then proceed immediately to Phase 1's 5 GA-blocking fixes** | Per the Engineering Review's own Final Verdict, GA approval is withheld specifically on BUG-04, BUG-05, BUG-06, BUILD-01, ISSUE-02 — nothing else in this roadmap outranks closing those five. |
| **Known Risks** | See list below | — |
| **Blockers** | **None architectural.** The practical blocker to GA-approved status is simply that Phase 1's 5 tasks haven't been executed yet — they are well-specified, low-ambiguity fixes, not open design questions. One task, **ENG-2.01** (Say It progression contract), may require a human product decision rather than a pure engineering judgment call if this roadmap's own recommended resolution is rejected. | Engineering Review Final Verdict; §0.3/ENG-2.01 task card. |
| **Next Recommended Task** | **ENG-0.01 — Clean baseline build and test run** | First uncompleted Task ID in Phase 0, per §4's Phase Map and §5. |
| **Deployment Readiness** | **~10%** | Phase 12's 10-item checklist is entirely unexecuted; Phase 1's GA blockers (a hard prerequisite to any responsible deployment) are also open. |
| **Portfolio Readiness** | **~20%** | The underlying architecture and privacy story (offline-first, on-device speech recognition, Clean Architecture) is genuinely strong and independently verified by the Engineering Review — a real asset. But per Master Context §13's explicit rule, none of Phase 13's presentation assets (screenshots, demo video, write-up) exist yet, and claiming portfolio-readiness before Phase 1's defects are fixed would misrepresent the project's actual state. |
| **Production Readiness** | **~5%** | Gated entirely on Phase 1 (0/5 complete) per the Engineering Review's Final Verdict — this number moves fastest and first once Phase 1 executes, and should be re-scored immediately after Quality Gate 1→2 passes. |

### Known Risks (detailed)

1. **Data-integrity risk until Phase 1 closes.** BUG-06 (silent Blend It progress loss) and BUG-04 (no migration strategy) both carry real data-loss potential for the app's only copy of a child's progress. Treat Phase 1 as strictly higher priority than any UI-phase work, even if UI work looks more immediately rewarding.
2. **Vosk shrink risk (ENG-1.02).** Enabling release minification is the task most likely to introduce a false "it builds" pass that actually breaks speech recognition at runtime via over-aggressive ProGuard shrinking of Vosk's JNI/reflection surface. Its Android Studio Runtime Tests are not optional.
3. **MapScreen complexity (UI-5.04).** The single highest-complexity UI task in the roadmap — 35 nodes, camera/scroll behavior, the app's de facto Home screen. Budget real time; don't compress it to fit a session.
4. **AnswerFeedback's 3-screen consumption surface (UI-7.02).** A defect here surfaces simultaneously on Say It, Find It, and Blend It — verify all three, not just the first one tested.
5. **Reduced-motion wiring breadth (UI-8.01).** Touches ~7 files across components built in 3 different earlier phases — the highest file-count single task in the roadmap. Keep every touch minimal (read-and-pass-through only) per its own implementation rule to avoid scope creep.
6. **Consistency QA scope creep (UI-10.03).** Explicitly audit-first, fix-second — the task card's own stop condition exists specifically to prevent this from becoming an unscoped re-polish of the entire app.
7. **Say It progression contract (ENG-2.01).** This roadmap provides a recommended resolution, but it is genuinely a product decision, not a pure engineering fact — confirm before treating it as closed.

---

## 24. Closing Note

This roadmap is the operating manual, not a one-time plan. At the end of every phase:
1. Update §23's Engineering Dashboard with real numbers.
2. Check off that phase's Quality Gate in §20.
3. If a new conflict, defect, or ambiguity was discovered, log it as a new row in §0.3.
4. Proceed to the next phase only when its gate is fully checked.

Antigravity should never need to consult `playIT_Master_Context_v1.0.md`, `engineering_review.md`, or `playIT_UI_Implementation_Playbook.md` directly to decide what to build next — every decision those three documents implied has already been resolved into the 103 items above. Open them only for the deep-reference detail a specific task card points to.

**playIT is currently a well-architected, feature-complete release candidate with 5 known, well-specified, GA-blocking defects and a fully-scoped path to production-quality polish. Nothing in this roadmap is ambiguous about what happens next — it starts at §5, Task `ENG-0.01`.**
