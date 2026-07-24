# playIT — 08. Find It Screen
See `00_ProjectRules.md` for source priority and global constraints. Feedback color fix uses `02_ComponentLibrary.md`'s unified `FeedbackCard`. Shares `HeartDisplay` with Say It (`07_SayIt.md`, `02_ComponentLibrary.md`).

---

## 1. FindItScreen Audit `[orig §3.7]`

| | |
|---|---|
| **Strengths** | 5-card grid within the 3-target/2-distractor design supports retrieval practice well; reuses `HeartDisplay` from Say It (good architectural discipline already present). |
| **Weaknesses** | Same red-feedback violation as Say It ("flash red, disable image") — see `01_Foundation.md` §1.3 #1; no explicit multi-touch lockout during feedback animation (Gemini review item #18 — "prevent multi-touch bugs... by disabling UI during audio playback"), which risks a child tapping a second card while feedback for the first is still resolving. |
| **Educational impact** | High (core discrimination/retrieval-practice loop, reused every letter) |
| **Effort** | Medium |
| **Recommendation** | Fix feedback color (`02_ComponentLibrary.md` §5); add input-lock during the ~300–500ms feedback animation window. |

---

## 2. Tasks

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| E2. Say It / Find It / Blend It: apply unified `FeedbackCard` | Retire per-screen implementations | Consistent, emotionally-safe feedback everywhere | 02_ComponentLibrary.md C1 | Medium | Must Have |
| E3. Find It / Say It: lock UI input during feedback-animation window (~300–500ms) | Prevent multi-touch/race-condition mis-taps (Gemini review #18) | Fewer accidental double-answers | E2 | Low-Medium | Should Have |

---

## 3. Priority Context

Ranks #3 in the Summary Priority Ranking (`10_FinalPolish.md`), tied with Blend It: feedback-color fix, input-lock, blending-cue. See `10_FinalPolish.md` for Blend It's parallel treatment.
