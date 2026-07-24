# playIT — 07. Say It Screen
See `00_ProjectRules.md` for source priority and global constraints. Feedback color fix uses `02_ComponentLibrary.md`'s unified `FeedbackCard`. Mic-listening visualizer full spec lives in `04_AudioSystem.md`. `VoskRecognizer`/`SpeechValidator` internals are out of scope (`00_ProjectRules.md` §0.5).

---

## 1. SayItScreen Audit `[orig §3.6]`

| | |
|---|---|
| **Strengths** | Hold-to-record mic interaction is appropriately large (72dp, already exceeds the new 54dp floor, `01_Foundation.md` §3); attempt tracker and noise-level indicator show good attention to real classroom/rural-device conditions (ambient noise). |
| **Weaknesses** | `FeedbackCard` documented as "green or red" — direct Design System violation (`01_Foundation.md` §1.3 #1); no visual "listening" waveform confirmed beyond a generic "ListeningAnimation" label — the Gemini review explicitly flags this as a gap. |
| **Educational impact** | **High** — this is the only speech-production checkpoint in the entire app; if the child can't tell whether the app is listening, the whole module's trust breaks down. |
| **Effort** | Medium-High (mic-level visualizer is genuinely new work, not just token application) |
| **Recommendation** | Fix feedback color immediately (Correction Orange, see `02_ComponentLibrary.md` §5). Build a real amplitude-reactive waveform tied to `AudioCapture`'s PCM stream — full spec in `04_AudioSystem.md` §2 — the single highest-value net-new interaction improvement in the whole app. |

---

## 2. Tasks

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| E1. Say It: build real amplitude-reactive mic-listening visualizer tied to `AudioCapture` PCM stream | Close the Gemini-review-flagged gap; child can see the app is listening | Trust in the speech-recognition loop; less confusion about silence/lag | 01_Foundation.md A1–A2, 02_ComponentLibrary.md C7 | High | Must Have — full spec in `04_AudioSystem.md` §2 |
| E2. Say It / Find It / Blend It: apply unified `FeedbackCard` | Retire per-screen implementations | Consistent, emotionally-safe feedback everywhere | 02_ComponentLibrary.md C1 | Medium | Must Have |
| E3. Find It / Say It: lock UI input during feedback-animation window (~300–500ms) | Prevent multi-touch/race-condition mis-taps (Gemini review #18) | Fewer accidental double-answers | E2 | Low-Medium | Should Have — see also `08_FindIt.md` |

---

## 3. Priority Context

Ranks #2 in the Summary Priority Ranking (`10_FinalPolish.md`): highest net-new value (mic visualizer), highest-stakes feedback-color fix. Say It's >40dB noise alert should use Correction Orange, not red — currently unspecified in the SDD (see `02_ComponentLibrary.md` §5 Error States).
