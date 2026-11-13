# The pitch film

The two-minute video, drawn and animated in code in Lily's art style. This folder is the film; `../video/` only turns it into an MP4.

## Watch it

Double-click `film.html`. It opens in Chrome with a Play button, five buttons that jump to each part, and a slider. Sound plays in the browser.

## Make the MP4

Inside `../video/`:

```
set PITCH_BROWSER=C:\Program Files\Google\Chrome\Application\chrome.exe
node scripts\render_film.mjs draft          960x540, about 20 minutes on this PC   -> out\film_draft.mp4
node scripts\render_film.mjs draft 40 70    only seconds 40 to 70
node scripts\render_film.mjs still 24.8 61  single pictures at those seconds
node scripts\render_film.mjs final          1920x1080                              -> out\<TeamCode>_2-Minute_Pitch.mp4
```

## What is where

| File | What it holds |
|---|---|
| `film.html` | The drawing: Mia, Lily, the phone, the book, the scenery |
| `film.css` | Fonts, caption style, the motion that never stops (palms, flags, blinking) |
| `lib.js` | The engine: one clock, keyframes, captions, sound list, the browser player |
| `s1.js` | 0:00–0:28. Mia is stuck; Lily arrives |
| `s2.js` | 0:28–1:10. "This is PlayIT", then the demonstration |
| `s3.js` | 1:10–2:00. Results board, the close, the team, the logo |
| `lines.json` | The script: every spoken line and the second it starts |
| `data.js`, `data.json` | Built by `tools/make_data.mjs`. Do not edit |
| `voice/`, `sfx/`, `music/` | The narration, the app's own sounds, the music bed |
| `img/` | Lily's real poses and the real app screenshots |

## How the timing works

Nothing is timed by hand against a stopwatch. A scene asks for the second a word is spoken, for example `cue("b3_02", "hears")`, and places its action there. If the voice is re-recorded, run `node tools/make_data.mjs` and the picture follows the new voice.

Word times come from the voice files. Four lines (`b1_03`, `b2_03`, `b4_03`, `b5_03`) had no usable word times, so their words are placed between the pauses in the audio. They may be off by a fraction of a second.

## When the real survey numbers arrive

1. Put them in `../round2_data.json` and set `"status": "actual"`.
2. Change the numbers in `lines.json` for lines `b4_01` to `b4_03` so the script matches (and in `../templates/narration_script.template.md`).
3. Clone the new lines in the narrator's voice (see "Redoing a spoken line" below for making several takes and checking them). In `tools/`, with the voice Python:
   `python clone_lines.py b4_01,b4_02,b4_03` (about 2 minutes per line; the takes go to `voice_new/`), then
   `python align_new.py` (Whisper checks each take against the script and writes its word times).
4. Copy the good takes and their `.words.json` from `voice_new/` into `voice/`.
5. `node tools/fit_lines.mjs` shows whether Beat 4 still ends before 1:40; add `--write` to apply. Then `node tools/make_data.mjs`, check stills, and render.

## Redoing a spoken line

Cloning is not exact: the same line comes out differently each time, and now and then a word is slurred. In `tools/`, with the voice Python:

```
python clone_lines.py b2_03 --takes 4                 four takes -> ../voice_takes/b2_03_t1.wav ...
python hear.py ../voice_takes                         what is actually said in each take (Whisper is NOT shown the script)
python tidy_take.py ../voice_takes/b2_03_t2.wav ../voice_new/b2_03.wav     only if the take is too long: shortens its pauses
python align_new.py                                   checks ../voice_new against the script, writes the word times
```

Then copy the `.wav` and `.words.json` from `voice_new/` into `voice/`, and run `node tools/fit_lines.mjs`, `node tools/make_data.mjs` and `python tools/make_gemini_bed.py` (the music is lowered under each spoken line, so it follows the voice). A word that keeps coming out wrong can be respelled for the voice only: `--say "Dep-Ed's=Dep Ed's"`, or `SAY` in `../tools/voice/make_voice.py`. `clone_lines.py` looks for the voice tools in `../tools/voice`; set `KIT_VOICE` if they are somewhere else.

## The screen recording

The phone in the demonstration (0:42 to 1:09) plays Erik's real recording, `../assets/recordings/demo.mp4`, with the status bar and the Android navigation bar cropped off.

- `recording_plan.json` says which seconds of the recording play when, and how fast (1.5x for the routine steps, 1x around Mia's "Mouse!"). The green check lands at 0:47.80, half a second after "Mouse!".
- `tools/make_rec_frames.py` (voice Python) cuts the recording into one picture per film frame in `rec/`, plus `rec/rec.js`. Run it again after any change to the plan.
- `recording_check.jpg` shows the first, middle and last picture of every part.
- If `rec/` is missing, the film falls back to the screenshots in `img/`.

The recorder saves no frames while the screen is still, so a single picture taken at a given second can show a later moment. Check times on the cut frames in `rec/`, not with one-off seeks.

- The phone's screen is drawn 75 x 158, the recording's own shape, so the whole app shows (nothing cut off at the top or bottom). The camera dot sits in the bezel. Tap rings are placed through `at()` in `s2.js`.
- `recording_plan.json` also tells the cutter to paint over the recording phone's edge-panel handle (`edge_bar`, a grey bar on the right edge that is not part of PlayIT) and to smooth over one crossfade frame where the old name was still faintly visible (`erase`).
- When the recording is not playing, the phone shows the app's start screen (`img/screen_splash.png`), from the first second of the film.

## Mia's arms and hands

- At rest her forearms lie on the table with a hand on each side of the book, never on it. The arms are drawn behind the book.
- Each hand has two drawings: open (`#fingL/#fingR`, four fingers and a thumb) and pointing (`#pointL/#pointR`, for taps).
- A pose is two angles, shoulder and elbow. `lib.js` ("Mia's arms") has the helpers: `reach()` finds the pose that puts the hand or fingertip on a spot, `armK()` sets poses over time, `pointing()` switches to the pointing hand, `front()` shows the copy of the forearm that is in front of the book (used while she closes or opens it).
- When she traces the letter, her forearm tucks behind the book and a separate hand (`#traceHand`) comes over the book's top edge; `tracing()` in `s1.js` moves its fingertip along the letter.
- The arm markup in `film.html` is written by `tools/gen_arms.py`; its numbers (shoulder, elbow, wrist, hand length) must match `ARM` in `lib.js`.

## One line under the logo

"a phonics game for Grade 1 learners" (0:31) is shown once, under the PlayIT logo, lighting up word by word. `window.CAP_PLACE` in `s2.js` tells `captions()` to put that phrase there instead of at the bottom.

## The voice

- Narrator: the user's own voice, cloned with Chatterbox from `../assets/voice_sample_2.wav`, seconds 13.5 to 23.3 ("Her teacher has a full classroom. Her nanay is still at work..."). Re-cloned on 12 Nov.
- Lily: her real recordings from the app: `b2_02` is `vo_welcome_01.wav` ("Hi there! I'm so happy you're here..."), `b5_04` is `sfx_opening_playit.wav` ("Play it!").
- Mia's "Mmm" (`b1_02`) has no audio.

The PROJECTED tag on the results board disappears by itself once the status is `actual`.

## Rules the film keeps

- The phone only ever shows the real app: the screen recording, or screenshots from the repo.
- Lily is the app's own art, including its talking drawings.
- Numbers are read from `round2_data.json`, never typed into a scene.
- The story pictures and clips in `../assets/` were a guide for the look. They are not used in the film. The one exception is the team's own picture `team_2d.jpg` (Beat 5), copied into `img/`. The learners and the parents and teachers in Beat 4 are redrawn in code from `learners.jpg` and `teachers.jpg`, which are references only (user's decision, 12 Nov).
- Beat 4 plays at natural speed (the 1.17x speed-up was removed with the "691 tests pass" line on 12 Nov). The results board appears at 1:08.7, about a second before the 1:10 mark, so it fits.
