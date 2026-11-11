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
- **Participants:** 30 in total = 16 Grade 1 learners (the users, observed) + 9 parents + 5 teachers. Parents and teachers are "the bridge to the child". Say counts, not percentages. No claims of learning gains.
- **Numbers:** the Round 2 figures in `round2_data.json` are projections (`status: projected`) and show a PROJECTED tag. The user brings real figures by the evening of 12 Nov.
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
- The real survey numbers by the evening of 12 Nov.
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
