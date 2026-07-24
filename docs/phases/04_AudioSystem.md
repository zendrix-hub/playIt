# playIT — 04. Audio System
### Audio Guidelines, Mic Visualizer Spec, Sound-Normalization QA
See `00_ProjectRules.md` for source priority and global constraints. `VoskRecognizer` internals are explicitly out of scope (see `00_ProjectRules.md` §0.5) — everything here is presentation-layer, wrapping the existing `AudioCapture` PCM stream.

---

## 1. Audio Guidelines (carried forward, tightened) `[orig §2.19]`

- Warm human voiceover — already correctly implemented via pre-recorded assets.
- Background music restricted to Map/Celebration only, disabled during active tasks — **needs verification**, not documented either way in the SDD. See task G2 below.
- Distinct, consistent micro-sounds per action per v1.0's Sound Design table — enforce, don't redesign.
- Say It's >40dB noise alert uses Correction Orange visually, never red (see `02_ComponentLibrary.md` §5 Error States and `07_SayIt.md`).

---

## 2. Say It — Mic-Listening Visualizer (net-new work, not token application) `[orig §3.6, §5 Phase E1]`

This is flagged independently by the Gemini repository review as a real UX gap: *"children need... distinct auditory/visual rewards"*, *"create a robust visualizer that reacts to microphone input volumes."*

**Current state:** SayItScreen's hold-to-record mic interaction is appropriately large (72dp, already exceeds the 54dp floor from `01_Foundation.md` §3). Attempt tracker and noise-level indicator show good attention to real classroom/rural-device conditions. However, no visual "listening" waveform is confirmed beyond a generic "ListeningAnimation" label.

**Required build:** a real amplitude-reactive waveform tied to `AudioCapture`'s PCM stream. This is the single highest-value net-new interaction improvement in the whole app, not just a polish item — if the child can't tell whether the app is listening, the whole module's trust breaks down.

**Component ownership:** base visual sits on `CircularAudioButton` (see `02_ComponentLibrary.md` §2, C7) as the shared audio-trigger foundation, but the amplitude-reactive rendering itself is Say-It-specific logic layered on top — does not require touching `VoskRecognizer` or `SpeechValidator`.

---

## 3. Sound-Normalization QA `[orig §3.8, task E6]`

v1.0's own stated requirement: *"Ensure celebratory sounds are normalized... to prevent sudden spikes that startle the child."* This applies at minimum to LetterCompleteScreen and BlendItCompleteScreen (see `10_FinalPolish.md`). Treat as an explicit QA pass, not just a design note — audio QA, not code.

---

## 4. Background-Music Audit `[orig §5 Phase G2]`

Confirm silence during active tasks (Hear It, Say It, Find It, Blend It); music only on Map/Celebration screens. Protects phonological working memory during phonics tasks — less auditory competition during the actual learning moment. Audio QA, no code dependency.

---

## 5. Task Summary

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| E1. Say It: build real amplitude-reactive mic-listening visualizer tied to `AudioCapture` PCM stream | Close the Gemini-review-flagged gap | Trust in the speech-recognition loop; less confusion about silence/lag | 01_Foundation.md A1–A2, 02_ComponentLibrary.md C7 | High | Must Have |
| E6. Letter Complete / Blend It Complete: QA-verify celebratory sound normalization | Prevent startling volume spikes | No jarring/scary loud moments during a reward | None (audio QA, not code) | Low | Should Have |
| G2. Background-music audit: confirm silence during active tasks, music only on Map/Celebration | Protect phonological working memory | Less auditory competition during learning | None (audio QA) | Low | Should Have |

Full priority context and dependency chain: `10_FinalPolish.md` (E6, G2), `07_SayIt.md` (E1 screen-level detail).
