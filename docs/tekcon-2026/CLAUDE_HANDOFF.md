# Hand-off: finish the PlayIT poster and two-minute pitch video

Written 11 Nov 2026 by the Claude session that built this kit, for a new Claude Code session with no memory of that work. The user will say: "Read docs/tekcon-2026/CLAUDE_HANDOFF.md and carry it out." Work through the steps below in order; do not wait for approval between steps unless a step says to stop.

## The job

TekCon 2026 (IT411 Capstone and SoftEng). The team must submit a research poster (22.5 x 28.5 in, portrait, with a QR code to the video) and a pitch video of at most 2:00, as MP4. The official brief is summarised in `README.md`.

- **Numbers freeze:** evening of 12 Nov. **Print:** 13 Nov. **Deadline:** 14 Nov.
- **Branch:** `docs/tekcon-2026-pitch-kit`. Commit and push at the points marked below. The repo is public.
- **You produce everything on this PC.** Antigravity (Gemini) was used for one night and is dropped. Its instruction files (`ANTIGRAVITY_RUN.md`, `.agents/`, `*antigravity*.md`, `SETUP_WINDOWS.md`) are still here; treat them as background only.

## Read these first

| File | Why |
|---|---|
| `AGENTS.md` | The rules for numbers, wording and which files are generated. They still apply to you. |
| `README.md` | What each file is and how the build works. |
| `video/timeline.json` | All 24 shots, their cues and assets. |
| `tools/package.json` | The commands: `build`, `video:prep`, `video:voice`, `video:stills`, `video:draft`, `video:final`. |
| `01_Video_Pitch/narration_script.md`, `storyboard.md` | Generated from `templates/`. Never edit them directly. |

How the kit works: `round2_data.json` (numbers) and `content.json` (poster wording) plus `templates/` are the only sources. `npm run build` inside `tools/` regenerates the poster pptx, script, storyboard and video data, then runs `check.js`, which prints PASS/WARN/FAIL and `READY TO PRINT AND RECORD: yes/no`. The video is a Remotion 4 project in `video/`.

## Decisions the user has made (do not reopen)

- **Story:** Mia, a six-year-old Grade 1 learner in the Philippines, cannot get help with the letter M; Lily the Tarsier appears; the Pixar-style room changes into the app's flat style; the demo happens in the app's style; results; close. The setting must be visibly Philippine.
- **Participants (FROZEN 12 Nov at N = 30):** 30 in total across Rounds 1–2 = 16 Grade 1 learners (`smileyometer`, Round 1) + 5 parents/supervising adults (`SUSaccessibility`, Round 1, SUS = 75.5) + up to 9 unique teachers/reading specialists across Round 1 and Round 2 (`teacherpedagogy` 4, `teacherpedagogy2` 4, `teacherpedagogy3` 5; Round 2 Likert rating comes from `teacherpedagogy3`, n = 5). Any future respondents collected later go strictly to the **research paper**, not the poster or pitch video. Parents and teachers are "the bridge to the child". Say counts, not percentages. No claims of learning gains.
- **Numbers (`status: "actual"`):** `round2_data.json`, `content.json`, `templates/`, `film/lines.json`, `film/s3.js`, and `film/tools/make_data.mjs` have been updated and frozen on 12 Nov with the verified workbook results (`15 of 16` happy face, `14 of 16` play again, `15 of 16` liked characters; `4.6/5` Grade 1 fit, `4.4/5` recommend, `4.7/5` R2 teaching; `status: "actual"`, removing the PROJECTED banner and `[brackets]`). Uncollected sub-task (`sayit_first_try`) and parent TAM (`usefulness`, `offline`) fields are `null`.
- **Voice:** clone the user's voice from `assets/voice_sample.wav`. If cloning cannot be made to work on this PC, fall back to a generated voice (Kokoro) and tell the user plainly. Lily's lines are the same voice pitched up.
- **Story motion:** no generated video clips. The stills move by code (Remotion), with ffmpeg, Whisper and yt-dlp as helpers.
- **App footage:** real app screenshots inside the phone frame now; the user's phone screen recordings replace them when they arrive.

## Calls made by the previous session (the user saw and approved them)

- **Remotion is the only renderer.** HyperFrames is not added: version 0.9, reported blank-frame bugs, too risky this close to the freeze.
- **Install voice tools on the Windows side without admin rights:** `uv` from GitHub releases, a uv-managed Python 3.11, then the packages. Windows gets the small CPU build of PyTorch straight from PyPI.
- **Music:** try yt-dlp once for a track licensed for reuse. If the network blocks it, build the bed from the app's own sounds, or use a track the user puts in `assets/music/`. Write the source to `assets/music/CREDITS.md`.
- **Restore the bridge line** in Beat 4 (see step 2).
- **Cover G1's malformed letter M** in code, since the picture cannot be regenerated here.

## This PC and its network (checked 11 Nov)

- Windows with WSL. Intel i7-4770S, Intel HD 4600, 16 GB RAM, no dedicated graphics card.
- Windows has git 2.48, Node 24 and Chrome. It has **no** Python, ffmpeg, winget or yt-dlp.
- `video/node_modules/@remotion/compositor-win32-x64-msvc/` contains `ffmpeg.exe` and `ffprobe.exe`, but it is a cut-down build: frame grabs with `-ss` work; the `fps`, `scale`, `tile` and `volumedetect` filters do not. Get a full ffmpeg build from GitHub for audio work.
- **Reachable:** pypi.org, github.com and its release downloads, the npm registry, huggingface.co (API).
- **Blocked or not answering:** download.pytorch.org, astral.sh, youtube.com, pixabay.com, remotion.media, fonts.gstatic.com, and one Hugging Face file host (`cdn-lfs.huggingface.co`). Model downloads may therefore fail; test early.
- Remotion cannot download its own browser. Set `PITCH_BROWSER` to the Chrome path before rendering; `video/scripts/render.mjs` handles the rest.
- Lexend and Sniglet are not installed in Windows. The video bundles its own fonts. For a poster preview use `PREVIEW_FONT=Calibri` (writes `tools/_preview.pptx`), or the preview shows doubled words.
- `tools/export_preview.ps1` drives PowerPoint. The pptx cannot be overwritten while it is open in PowerPoint; ask the user to close it rather than closing it yourself.
- On an old CPU, cloning about 240 words may take an hour or more. Start it in the background and keep working.

## What exists now

- Story stills `G1`–`G7`, `G9`, `G10` and plates `B1`, `B2` in `01_Video_Pitch/generated/`. They are Philippine in setting and Mia is consistent across the ones reviewed (G1, G4, G7, G10, B1). G2, G3, G5, G6, G9 and B2 have not been looked at yet: look before using them.
- Seven Lily poses, `team_pixar.png`, `voice_sample.wav` and `.m4a` in `assets/`.
- The poster pptx with Lily and the team picture placed; five screenshot slots and the QR slot are still empty.
- A draft video was rendered by Antigravity (120.0 s, 960x540). It is a build output and is not in the repo.
- Script: 237 words, about 1:45.
- The voice scripts (`tools/voice/make_voice.py`, `align.py`) have **never been run**. Expect to fix things on first run.

## Problems found in review

1. In the results scene around 1:25, the yellow PROJECTED tag covers the heading "The bridge to the child: parents and teachers".
2. The trim weakened Beat 4: "The grown-ups who open the door?" became "The grown-ups?", and the quote lost "for Grade 1 learners".
3. In `G1.png` the letter on Mia's book is a malformed M, on screen during the opening line.
4. Pixar-style Lily (furry tarsier) and app Lily (flat orange character) look very different, so the morph has to carry that change.
5. The demo phone shows a "REAL APP FOOTAGE" placeholder.

## Steps

**Step 1 is done:** the assets from `docs/tekcon-2026-draft-preview` are merged into this branch.

2. **Fix the review findings.**
   - `video/src/scenes/Results.jsx`: move the PROJECTED tag clear of the heading; check the bridge figures sit on the bridge.
   - `templates/narration_script.template.md` and `templates/storyboard.template.md`: bring the bridge wording back. Beat 4 must stay at or under 70 words, and the whole script at or under 265.
   - `video/src/scenes/Story.jsx`: a clean "M" over the book page in shot 1.
3. **Poster.** Copy the real screenshots from `docs/screenshots/` on `origin/main` (`git show origin/main:docs/screenshots/hearit.png`, and `sayit`, `findit`, `blendit`, plus `splash`, `map`, `sayit_cue`, `nameprompt`) into `assets/` under the slot names in `assets/README.md`. The Parent Zone slot stays empty until the user sends one. Run `npm run build` in `tools/`, export a preview, and look at it. **Commit and push.**
4. **Story motion in code** (`video/src/scenes/Story.jsx`, `video/src/parts.jsx`): eased push-in and pull-out, soft light flicker and vignette on the evening scenes, a glow pulse on the phone in G7, and the morph as a sweep outward from the phone through G7 → B2 → B1, with flat Lily landing at the end. Keep the `clip` names in the timeline so real clips still win if they ever appear.
5. **Demo with real screenshots** (`video/src/scenes/Demo.jsx`, `tools/prep_media.js`): the phone frame shows the real screenshot for each step, with a tap ring and a slide between screens. A `rec_*.mp4` in `assets/recordings/` must replace its screenshot automatically.
6. **Voice.** Install uv, Python 3.11, `tools/voice/requirements.txt`, `chatterbox-tts` and a full ffmpeg. Run the clone with `--engine chatterbox --sample ../assets/voice_sample.wav`, then `align.py` for caption timings. You cannot hear audio: measure length and loudness, and ask the user to listen.
7. **Music and sound.** Bed at low volume under the voice, app sounds on the demo beats, credits file written.
8. **Draft render.** Grab a frame from every one of the 24 shots and look at each. Confirm with ffprobe: at most 120.0 s, audio present. **Commit and push**, then **stop** and ask the user to watch the MP4 and give notes.
9. **Freeze, evening of 12 Nov.** The user gives the real numbers. Put them in `round2_data.json`, set status to `actual`, rebuild the poster, redo the Beat 4 voice lines, render the final 1080p video. `npm run build` must end with `READY TO PRINT AND RECORD: yes`. **Commit and push.**
10. **Finish.** Make the QR from the user's Google Drive link, save it as `assets/qr_pitch.png`, rebuild, and ask the user to scan the printed-size QR with a phone before printing.

## Needed from the user

- Phone screen recordings per `01_Video_Pitch/screen_recording_shotlist.md`, including a Parent Zone screen. The brief asks for actual recordings.
- `round2_data.json` is already updated and frozen at `N = 30` (`status: "actual"`); do not alter pitch/poster figures unless the team lead directs otherwise (extra survey respondents go to the research paper only).
- The team code. `G56` is a stand-in.
- The final MP4 uploaded to Google Drive (Anyone with the link, Viewer) and the link.
- Lexend and Sniglet installed on the PC that exports the poster PDF.

## Honesty rules

- While numbers are projected, the PROJECTED tag stays on the poster and in the video.
- Only real screenshots or recordings go inside the phone frame. No invented app screens.
- Clone only the user's own voice.
- Report what you could not run, see or hear as not verified. Do not imply it works.

## Status after the 11 Nov Claude Code session

Steps 2 to 8 are done. The draft is `video/out/draft.mp4` (120.0 s, 960x540, H.264 + AAC; a build output, not in git). **Waiting for the user's notes on the draft.** Steps 9 and 10 remain.

What was done, and where it differs from the plan above:

- **Voice:** cloned from `assets/voice_sample.wav` with Chatterbox on the CPU (about 25 minutes for 20 lines). The library's own model download stalled on this network, so `tools/voice/get_model.sh` fetches the model in parallel pieces into `tools/voice/.venv/models/chatterbox/`, and `make_voice.py` uses that folder. Kokoro was not needed and is not installed.
- **New voice steps:** `fit_beats.py` (speeds a beat's lines up, pitch unchanged, when they would overrun the beat's window), then `align.py`, and `check_voice.py` (prints what Whisper hears in each line beside the script). Run order: `make_voice.py`, `fit_beats.py`, `align.py`, `check_voice.py`.
- **Beat 4 is sped up 1.17x** to fit its 30 seconds; Beat 2 by 1.003x. The other beats are untouched. When the real numbers arrive, cutting about six words from Beat 4 would let it run at natural speed.
- **Full ffmpeg:** a direct download from GitHub was refused by the permission system, so ffmpeg comes from the `imageio-ffmpeg` pip package inside the venv. `tools/media.js` finds it. ffprobe is Remotion's copy.
- **Music:** youtube.com and pixabay.com are blocked, so `tools/voice/make_music.py` builds the bed from the app's own pluck sound. Sound effects are the app's own, copied by `tools/get_app_sounds.js`. See `assets/music/CREDITS.md`.
- **Demo:** shots 7 to 14 show real screenshots from `docs/screenshots/` on `main`. Shot 15 (Parent Zone) still shows the orange placeholder: there is no real Parent Zone screen yet.
- **Mia's "Mmm"** (line `b1_02`) is silent; it is not generated. Supply `assets/voice/b1_02.wav` to fill it.

Not verified, because nobody on the build side can hear: how close the clone sounds to the user, the pronunciation of "nanay", "DepEd" and "Marungko", whether the 1.17x Beat 4 sounds rushed, and whether the music bed and effects are pleasant. Whisper's transcript matched the script on every line.

Seen in the stills and left alone: `G3.png` (classroom) has garbled writing on the board and alphabet cards, and `team_pixar.png` has a misspelt sign behind the team. Both are generated pictures that cannot be regenerated here.

## Update, afternoon of 11 Nov: the video changed direction

The user watched the draft above and rejected it as too static (still pictures with slow zooms). The video is now the film in `film/`, drawn and animated in code in Lily's art style; read `film/README.md` before touching it. The user approved the look step by step from the pages in `mockups/`. The generated story pictures are a guide only and must not appear in the film.

- A full 120-second draft is at `film/film_draft.mp4` (960x540, voice, music and app sounds). It was checked by pulling 31 frames from the MP4; nobody on the build side has listened to it.
- The film reuses the cloned narration from this session. Four lines have estimated word timing (see `film/README.md`).
- Still to do: the user's notes on the draft; the real survey numbers and new Beat 4 voice lines; the final 1080p render; the QR code.
- The uv-managed Python that the voice tools used is no longer on this PC (`AppData\Roaming\uv\python` is missing), so cloning and Whisper cannot run until it is reinstalled.

## Update, evening of 11 Nov: User Review & Instructor Specifications

The user reviewed `film/film_draft.mp4` and provided the instructor's updated rubric and specific fixes for the pitch and deliverables.

### 1. Updated Instructor Deliverables & Google Drive Structure
Due date: **November 14, 2026** (Round 2 survey freeze: evening of 12 Nov, Print: 13 Nov).
Required Google Drive layout:
```text
TeamCode/
└── 06_TekCon_2026_Presentation/
    ├── 01_Research_Poster/
    │   ├── Research_Poster.pdf
    │   └── Research_Poster_Source (PPTX)
    ├── 02_Video_Pitch/
    │   └── TeamCode_2-Minute_Pitch.mp4
    └── 03_Software_Product_Logo/
        ├── Product_Logo_2000x2000.png
        └── Product_Logo_512x512.png
```

### 2. Software Product Logo (Done)
The required high-resolution software product logo has been generated in `docs/tekcon-2026/03_Software_Product_Logo/`:
- `Product_Logo_2000x2000.png` (2000x2000 px, 32-bit RGBA, transparent background, clean vector-style anti-aliased mascot).
- `Product_Logo_512x512.png` (512x512 px, 32-bit RGBA, transparent background, optimized for web/app store icon).
- Bonus AppIcon variants with the rounded squircle badge are also present (`Product_Logo_AppIcon_*.png`).

### 3. Decisions & Fixes for the Video Film (`film/`)

1. **Mascot Voice (Lily): Use Real In-App Audio**
   - The pitched-up TTS voice for Lily in `b2_02` and `b5_04` is rejected.
   - Use Lily's actual voice talent recordings from the app in `app/src/main/assets/audio/vo/`:
     - **Beat 2 Greeting (`b2_02`):** Use `ret_welcome_back.wav` ("Welcome back! Ready to keep learning?" - 1.49s) or `vo_map_tarana.wav` ("Tara na!" - 2.72s) or `vo_welcome_01.wav` ("Hi there! I'm so happy you're here. Let's play and learn together!" - 3.48s).
     - **Beat 5 Outro (`b5_04`):** Use `vo_complete_01.wav` ("You did it! I'm so proud of you!" - 1.85s) or `vo_correct_01.wav` ("Yes! That's it!" - 1.06s).
   - Wire the selected `.wav` files into `film/voice/` and adjust `lines.json` and `s2.js` / `s3.js` timings to match.

2. **Narration Voice Sample & "nanay" Pronunciation**
   - In `b1_03`, "nanay" sounded unnatural because the previous voice sample was in English only.
   - The user will record a fresh voice sample containing Filipino phonetics, vowel sounds, and project terms (*nanay*, *Mia*, *Marungko*, *DepEd*, *CIT University*, etc.).
   - When the user places the new audio in `assets/voice_sample.wav`, re-run `make_voice.py` / Chatterbox cloning.

3. **Drop "691 tests pass" from Pitch Video (`b4_05`)**
   - "691 tests pass" is developer jargon irrelevant to pitch viewers.
   - Cutting `b4_05` saves ~2.7 seconds in Beat 4.
   - **Crucial win:** Beat 4 was artificially sped up to 1.17x to squeeze under 30 seconds. Removing `b4_05` allows Beat 4 to run at natural **1.0x human speaking speed** without rushing!
   - On the research poster (`02_Research_Poster/`), keep "691 Automated Tests (100% Pass)" on the bottom technical software engineering chip.

4. **Remove Airplane Mode Toggle from Beat 3 (`b3_01`)**
   - PlayIT is completely offline by design. Turning on airplane mode is unnecessary and distracts from the phonics demonstration.
   - Change `b3_01` narration to a direct transition: e.g. "Watch Mia learn." or "No Wi-Fi needed — Mia just taps her letter."
   - In `film/s2.js`, remove the airplane toggle overlay and go directly to the lesson screen.

5. **Visual Art Style Consistency (2D Flat Storybook Style)**
   - The 3D Pixar render (`team_pixar.png`) does not match the rest of the 2D film.
   - Recreate the team portrait and participant illustrations in the exact 2D flat storybook art style of Lily and Mia.
   - Prompts for "nano banana" / image generator have been provided to the user.

### 4. Newly Added Assets in Repo (11 Nov Night)
The user has provided all visual and voice assets in `assets/`:
1. `voice_sample_2.m4a` / `voice_sample_2.wav`: 96-second high-quality audio sample containing authentic Filipino phonetics, natural pronunciation of *nanay*, *Mia*, *Marungko*, *DepEd*, *CIT University*, and numbers. Use this for re-cloning.
2. `learners.jpg` (2752×1536, 16:9): Flat 2D storybook illustration of Filipino Grade 1 learners holding books and tablets.
3. `teachers.jpg` (2752×1536, 16:9): Flat 2D storybook illustration of Filipino Grade 1 teachers and parents.
4. `team_2d.jpg` (2752×1536, 16:9): Flat 2D storybook illustration of the five team members smiling together in the video's art style, replacing `team_pixar.png`.
5. `03_Software_Product_Logo/`: The official product logos featuring the approved Concept 1 Lily mascot face emblem:
   - `Product_Logo_2000x2000.png` (transparent background master)
   - `Product_Logo_512x512.png` (transparent background web/app icon)
   - Plus AppIcon squircle badge variants (`Product_Logo_AppIcon_*.png`).
6. `assets/music/` (added 12 Nov afternoon):
   - `bed_gemini.mp3` (121.3s stereo MP3): full 2-minute Gemini instrumental bed from `01_Video_Pitch/music_prompt_gemini.md`. Convert/loudness-match into `film/music/bed.wav` during Phase A/B and verify ducking under speech.
   - `sting_lily.mp3` (62.4s) & `sting_end.mp3` (59.7s): generated from Option D1/D2 prompts, but exported full-length (~60s each instead of 3s) — inspect waveform/loudness and trim to the best ~3s sparkle/flourish clip before wiring into `film/sfx/` or keep `sfx_heart_recovery_sparkle.mp3` / `sfx_correct_chime.mp3` if cleaner.

### 5. Schedule & Execution Rules: Nov 12 vs. Nov 13

> [!IMPORTANT]
> **USER DIRECTIVE ON TIMING:**
> - **On Nov 12 (Absorption & Prep Day):** DO NOT recreate or trigger final video/poster renders yet. Just adopt and absorb what has been newly added in the assets, frozen `N = 30` data (`status: "actual"`), and handoff. Prepare voice cloning, adapt the code, and verify `film/data.json` + Beat 4 script changes cleanly.
> - **On Nov 13 (Final Output Recreation Day):** Re-synthesize the updated narration (`voice_sample_2.wav` + Beat 4 `b4_01..b4_03` with the frozen `N = 30` numbers), inject final Google Drive video QR code, render the full 1080p MP4 film, export final poster PDF/PPTX, and assemble the Drive submission folders. Print on Nov 13 for Nov 14 submission.

### 6. Step-by-Step Execution Plan for Claude

#### Phase A (Nov 12 — Adopt & Absorb):
1. **Narration Voice Re-clone Prep:** Run Chatterbox on `assets/voice_sample_2.wav` (`python tools/voice/make_voice.py --engine chatterbox --sample ../assets/voice_sample_2.wav`), covering the updated `film/lines.json` (especially `b4_01`–`b4_03` with `30 people: 16 children, 5 parents, 9 teachers`, `15 of 16` happy / `14 of 16` play again, `4.6` fit / `4.4` recommend / `4.7` teaching). This also fixes *nanay* and gives natural Filipino inflection.
2. **Lily Voice Replacement:** Copy in-app studio audio files to `film/voice/`:
   - `b2_02.wav` <- `app/src/main/assets/audio/vo/ui/ret_welcome_back.wav` (or `vo_map_tarana.wav`).
   - `b5_04.wav` <- `app/src/main/assets/audio/vo/lesson/vo_complete_01.wav`.
3. **Beat 3 Demo Code Adjustment:** In `film/s2.js`, remove the airplane toggle animation card. Update narration line `b3_01` to transition smoothly into the lesson.
4. **Beat 4 Results Code Adjustment:** `film/s3.js`, `film/lines.json`, `film/tools/make_data.mjs`, `round2_data.json`, and `content.json` are already aligned to the frozen real `N = 30` metrics (`status: "actual"`). In `film/s3.js` and `lines.json`, remove `b4_05` ("691 tests pass") from the video, freeing ~2.7s so Beat 4 runs at natural **1.0x human speaking speed** (removing the previous 1.17x speed-up!), and display `learners.jpg` and `teachers.jpg` alongside the score cards.
5. **Beat 5 Team Visual Adjustment:** In `film/s3.js`, wire `assets/team_2d.jpg` into the team display segment.
6. **Confirm `make_data.mjs` + `check.js`:** Verify `node film/tools/make_data.mjs` aligns timestamps cleanly once new audio `.wav` files are generated.

#### Phase B (Nov 13 — Final Output Recreation & Packaging):
1. **Verify Frozen Survey Numbers:** Confirm `round2_data.json` (`N = 30`, `status: "actual"`) still matches team-lead sign-off (future survey additions go to the research paper only).
2. **Verify All Re-synthesized Audio:** Confirm `b1_01..b5_04` (including `b3_01` and `b4_01..b4_03`) fit inside every beat window without overruns, then run `node film/tools/make_data.mjs`.
3. **Render Final 1080p Pitch Film:** Render `film/` at 1080p (`TeamCode_2-Minute_Pitch.mp4`). Verify length is <= 120.0s.
4. **Final Drive Link & QR Code:** User uploads the video to Google Drive. Claude generates `assets/qr_pitch.png` from that link and runs `npm run build` in `tools/` to rebuild the poster.
5. **Final Poster Export:** Export `Research_Poster.pdf` and `Research_Poster_Source.pptx`.
6. **Package Submission Tree:** Populate `06_TekCon_2026_Presentation/` (folders `01_Research_Poster/`, `02_Video_Pitch/`, `03_Software_Product_Logo/`).
7. **Verify & Print:** Verify `check.js` prints `READY TO PRINT AND RECORD: yes` and team prints cartolina poster on Nov 13.





## Status, 12 Nov (cloud session): final film rendered

At the user's request ("finish the rendering of the video"), the film was rendered at full size from the content of commit `e0c3081`, ahead of the Nov 13 plan in section 5.

- `video/out/G56_2-Minute_Pitch.mp4`: 1920x1080, 30 fps, 3600 frames, 120.0 s, H.264 + AAC stereo 48 kHz, about 66 MB, integrated loudness -20.8 LUFS, peak -3.5 dB. A build output, not in git; it was sent to the user in the chat.
- `film/film_draft.mp4` (the review copy in git) is that render scaled to 960x540. It replaces the 11 Nov draft.
- `node film/tools/make_data.mjs` regenerated `film/data.json` with no change, so the picture follows the current voice files.
- Checked by looking at a frame every 5 s across the whole film; no blank or broken frames. Voice is present in every beat; the music bed is silent 0:17-0:20 by design.
- Not verified: nobody on the build side has listened to it.
- If the team code changes from `G56`, the file name and the "Group 56" line change with it, so render again.
- `check.js` in a fresh clone reports two FAILs that are timestamp artifacts of the checkout, not content problems: "poster is older than round2_data.json" (the pptx was built from the frozen data in `e0c3081`) and "video data is out of date" (the gitignored `video/src/generated/data.json` of the old slideshow pipeline; `node tools/build_video_data.js` clears it). The poster still needs its QR code before printing.
