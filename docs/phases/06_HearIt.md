# playIT — 06. Hear It Screen
See `00_ProjectRules.md` for source priority and global constraints. Uses the Focus Template from `02_ComponentLibrary.md` §9, typography scale from `01_Foundation.md` §2.2.

---

## 1. HearItScreen Audit `[orig §3.5]`

| | |
|---|---|
| **Strengths** | Minimalist by design (giant letter, mascot, one play button) — already close to ideal ≤3-element compliance (`00_ProjectRules.md` §0.4); correctly plays the *phonetic sound*, not the letter name, per Marungko principle; replay counter satisfies "autonomy" (Self-Determination Theory). |
| **Weaknesses** | None structural; token/component application only. |
| **Educational impact** | High (this is the first-exposure moment for every new phoneme) |
| **Effort** | Low |
| **Recommendation** | Apply Body Large (20sp) to mascot instruction text; apply the new touch-target floor (54dp, `01_Foundation.md` §3) to `PlayButton`; otherwise preserve as-is — this screen is close to a model example of the design philosophy already. |

---

## 2. Task

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| E5. Hear It: apply Body Large typography + new touch targets only | Preserve this screen's already-strong design; light touch | Marginal readability gain | 01_Foundation.md B4, A3 | Low | Nice to Have |

---

## 3. Priority Context

Ranks #5 in the Summary Priority Ranking (`10_FinalPolish.md`): near-model already, light-touch only. `PlayButton` shares its base component with `CircularAudioButton` (see `02_ComponentLibrary.md` §2, C7) — the same base used by Say It's mic button (`07_SayIt.md`) and Blend It's word-audio button (`10_FinalPolish.md`).
