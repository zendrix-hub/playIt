# PlayIT TekCon 2026 kit: hand-off

Rewritten 13 Nov 2026. For a teammate (and their Claude Code session) who pulls this branch to **make the pitch better**, not only to finish or copy it. It says what was made, what the team lead (Zendrix) decided and why, how each piece is built, what went wrong on the way, and, in section 8, where the film and poster are still weak and how to tell whether a change is an improvement.

**To the Claude session reading this:** do not treat this file as a list of steps to carry out. Start by looking at the film yourself (stills across the two minutes, the script in `film/lines.json`, the poster preview) and form your own view of what works and what does not. Then compare it with section 8. Disagree with this file where you have a reason, and show the reason as a mock-up or stills.

To start a session: open the repo in Claude Code and say **"Read docs/tekcon-2026/CLAUDE_HANDOFF.md, then …"** followed by what you want. `film/README.md` has the detail for the film; `AGENTS.md` has the rules for numbers and poster wording. The older step-by-step plans that used to be in this file are in git history (before 13 Nov).

Branch: `docs/tekcon-2026-pitch-kit`. The repo is public.

---

## 1. What has to be handed in

Due **14 November 2026**. One printed poster on cartolina, plus this Google Drive layout (the instructor's, 11 Nov):

```
G56/
└── 06_TekCon_2026_Presentation/
    ├── 01_Research_Poster/        Research_Poster.pdf, Research_Poster_Source.pptx
    ├── 02_Video_Pitch/            G56_2-Minute_Pitch.mp4
    └── 03_Software_Product_Logo/  Product_Logo_2000x2000.png, Product_Logo_512x512.png
```

- **Poster:** 22.5 x 28.5 in, portrait, with a QR code labelled "Scan to Watch Our 2-Minute Project Pitch".
- **Video:** MP4, 2:00 or less, in five parts: problem (to 0:20), solution (to 0:40), demonstration of the real app (to 1:10), results (to 1:40), impact and close (to 2:00).
- **Logo:** PNG at 2000 x 2000 and 512 x 512.
- Team code **G56** ("Group 56") is confirmed. Team: Zendrix Riva (Lead), Erik Samuel Bien, Ashlee Skye Durano, Kaysean Miel, John James Palis. Adviser: Mr. Joemarie C. Amparo.
- The instructor's original brief and rubric are **not** in this folder. What is here is the team's summary of it (the ten rules in section 6). If you have the rubric, check the film against it.

## 2. Where things stand (13 Nov, midday)

**Done**

- Survey numbers frozen and real (`round2_data.json`, `status: "actual"`).
- Poster built from the real numbers (`02_Research_Poster/Research_Poster_Source.pptx`). It is missing only its QR code.
- Film complete in `film/`: picture, cloned narration, Lily's real voice, the real screen recording in the phone, music, captions. Exactly 120.0 s.
- Logo: the app's launcher icon rebuilt in HD (`03_Software_Product_Logo/`).
- Drive folders created by the team lead. Their links are with him; they are left out of this file because the repo is public.

**Left, in this order**

1. The team lead watches and listens to `film/film.html` and gives notes.
2. Final render at 1080p (section 5, "Render"). The 1080p file made on 12 Nov is out of date: it has the old music, the old takes of two lines and none of the 13 Nov picture fixes. `film/film_draft.mp4` is that old render at half size.
3. Upload the MP4 to `02_Video_Pitch` on Drive; make the QR from its link; save it as `assets/qr_pitch.png`; rebuild the poster; scan the QR from a second phone.
4. Export `Research_Poster.pdf`, upload the poster files, and replace the two logo files on Drive if they were uploaded before the evening of 12 Nov (those were the old face-only logo).
5. Set the Drive folder to "Anyone with the link: Viewer". Print.

Nobody on the build side can hear audio. Everything about how the voice and music *sound* is unverified until a person listens.

## 3. What the team lead decided, and why

These are the current answers, each reached by looking at mock-ups and drafts. They are here so you know what was tried and why, not to stop you. Section 8 says which things are fixed and which are open; anything open can be changed if you show the team lead something better.

**The film's look**
- The first draft (generated story pictures with slow zooms) was rejected as "too static". The film is now drawn and animated in code, in the flat style of Lily, the app's mascot: no outlines, two-tone shading, big glossy eyes.
- Text looks like a toddler learning video: chunky outlined letters, sing-along captions where each word lights up as it is said, pop-up labels.
- The generated pictures and clips in `assets/` and `01_Video_Pitch/generated/` are references only and must not appear. The one picture used is the team's own `team_2d.jpg`. The learners, parents and teachers on the results board are redrawn in code.
- The setting is the Philippines throughout (bahay kubo, palms, banderitas, "nanay", school uniform).

**The story**
- Mia, six, is stuck on the sound of M at home at night: her teacher has a full class, her nanay is at work, there is no signal. She closes the book. Lily peeks out of the phone, the scene turns to day, PlayIT is introduced, Mia uses it, results, close.
- The move from problem to solution is a smooth change of light, not a cut.
- During the demonstration the far background blurs and fades so Mia, the phone and Lily are the focus.
- A phrase is not shown twice at once: "a phonics game for Grade 1 learners" appears once, under the logo, lighting up as it is spoken.

**The phone and the app**
- The phone only ever shows the real app. In the demonstration it plays Erik's screen recording, cropped to remove the status bar (Wi-Fi and LTE icons). Otherwise it shows the app's start screen, including in the problem scene.
- The recording's profile was named "Dj"; the lead asked for it to read "Mia". This is the only edit to the real footage. If a judge asks, say so plainly.
- The whole app screen must be visible inside the phone: nothing cut off, nothing drawn over it.
- No airplane-mode switch; a "No Wi-Fi needed" chip stays up for the demo instead.
- "691 tests pass" is cut from the film as developer jargon. The poster keeps it.

**Mia**
- Short puff sleeves and skin arms that bend at the elbow. Hands have four fingers and a thumb. At rest they lie on the table beside the book and must not sit on the book.

**Voices and music**
- Narrator: the team lead's own voice, cloned. Clone only the voice of someone who agreed.
- Lily speaks only with her real recordings from the app. A pitched-up clone was rejected.
- Mia's "Mouse!" is the lead's cloned voice, timed so the app's green check appears half a second after it. Her "Mmm." has no sound, by design.
- Music: an instrumental the lead generated with Gemini (`assets/music/bed_gemini.mp3`), kept low under the voice.

**Numbers**
- 30 participants across Rounds 1 and 2: 16 Grade 1 learners, 5 parents or supervising adults, 9 teachers. Frozen on 12 Nov. Answers collected later go to the research paper only.
- Children are the users and come first. Parents and teachers are "the bridge to the child". Never "30 users".
- Every figure shows its group size. The teaching score 4.7 comes from 5 of the 9 teachers, and the film and poster both say so.

**Poster and logo**
- No product logo on the poster (Lily is already there). File names exactly as in section 1.
- The logo must match the app's actual launcher icon, clear and HD.

**How he works**
- Show a mock-up or stills before building anything large; he decides by looking.
- Nothing replaces the submitted film or poster until he has seen it and agreed.

## 4. How the kit is built

```
round2_data.json          every survey number (frozen)
content.json              every word on the poster, and which figures it shows
templates/                wording of the narration script and storyboard
tools/                    npm run build -> poster pptx, script, storyboard, then check.js
02_Research_Poster/       the poster (generated; never edit the pptx by hand)
03_Software_Product_Logo/ the two logo files; _previous_versions/ stays out of Drive
assets/                   pictures, the voice sample, the screen recording, music sources
film/                     THE VIDEO. A web page animated from one clock. Read film/README.md
video/                    only turns film/ into an MP4 (Remotion). scripts/render_film.mjs
mockups/                  the look tests the lead approved, in order
```

- **The film** is `film/film.html` plus `lib.js` (engine), `s1.js` (0:00–0:28), `s2.js` (0:28–1:10), `s3.js` (1:10–2:00). Scenes never use stopwatch times: `cue("b3_02", "hears")` returns the second that word is spoken, so the picture follows the voice.
- **The script** is `film/lines.json`. `node film/tools/make_data.mjs` reads it, the voice files and the survey numbers, and writes `film/data.json` and `data.js`.
- **Not used any more:** `video/src/scenes/`, `video/timeline.json` and the `video:*` commands in `tools/package.json` (the rejected slideshow), and the Antigravity files (`ANTIGRAVITY_RUN.md`, `.agents/`, `*antigravity*.md`, `SETUP_WINDOWS.md`). Three of the four warnings from `check.js` come from that old slideshow and can be ignored; the fourth is the missing QR.

## 5. How to do things

Commands are for Windows (Command Prompt or Git Bash). "Voice Python" means the Python environment with the voice tools; see "Set up" below.

**Set up on a new PC**
- Node.js, Chrome, PowerPoint. Fonts Lexend and Sniglet installed for the poster (the film bundles its own).
- `npm install` inside `tools/` and inside `video/`.
- Voice tools (only if you will redo voice lines, re-cut the recording or rebuild the logo): the steps in `README.md` under "The pitch video" (`uv`, Python 3.11, `tools/voice/requirements.txt`, `chatterbox-tts`, `bash tools/voice/get_model.sh` for the 3 GB model).

**Watch the film**
- Double-click `film/film.html`. It plays with sound in Chrome and has a slider.
- One still without rendering: `chrome --headless=new --screenshot=shot.png --window-size=1280,720 --virtual-time-budget=6000 "file:///…/film/film.html?embed=1&t=47.9"`. Always look at stills after a change; put several on one sheet.

**Change the picture**
- Edit `film.html` (the drawing) or the scene file for that part. Open `film.html` and scrub to check.
- `K(selector, [[second, {css}], …])` sets keyframes. It holds the first value from 0:00 and the last to the end, so a list that starts late still shows its first value from the start. That caused a stray screenshot on the phone for the whole first minute.
- Mia's arms: helpers in `lib.js` ("Mia's arms"); the markup is written by `film/tools/gen_arms.py`.

**Redo a spoken line** (in `film/tools/`, with the voice Python)
1. Change the words in `../lines.json` if needed. Keep each part inside its window; the checker counts words per part.
2. `python clone_lines.py b2_03 --takes 4`. Cloning is not exact; make several takes.
3. `python hear.py ../voice_takes` writes down what is actually said, without showing Whisper the script. Pick the take whose words are right. Names such as "DepEd's Marungko" never match in spelling; judge them by whether they sound right ("Deped's Marunko" is right, "depth at smarunko" is not).
4. If a word keeps coming out wrong, respell it for the voice: `--say "Dep-Ed's=Dep Ed's"`, or add it to `SAY` in `tools/voice/make_voice.py`.
5. If the best take is too long: `python tidy_take.py ../voice_takes/b3_03_t2.wav ../voice_new/b3_03.wav` shortens the pauses. Otherwise copy the take to `../voice_new/<id>.wav`.
6. `python align_new.py` checks the take against the script and writes its word times. Copy the `.wav` and `.words.json` into `../voice/`.
7. `node fit_lines.mjs` (does each part still end in time?), `node make_data.mjs`, `python make_gemini_bed.py` (the music is lowered under each spoken line, so it must be rebuilt), then stills at the words that drive the picture.
8. Ask a person to listen.

**Change the screen recording** (`film/recording_plan.json`, then `python make_rec_frames.py` in `film/tools/`)
- The plan says which seconds of `assets/recordings/demo.mp4` play when and how fast, the crop, the "Dj" to "Mia" relabel and the clean-ups.
- The recording has a variable frame rate and stores no frames while the screen is still. A single picture grabbed at a given second can show a later moment. Check times on the cut frames in `film/rec/`.

**Music**
- Replace `assets/music/bed_gemini.mp3`, run `python film/tools/make_gemini_bed.py`, update `assets/music/CREDITS.md`. Loudness is `vol` in `film/lib.js` (`music/bed.wav`, 0.12); the script's own targets are at its top.

**Render** (inside `video/`)
```
set PITCH_BROWSER=C:\Program Files\Google\Chrome\Application\chrome.exe
node scripts\render_film.mjs draft 40 70     a part, 960x540, to check
node scripts\render_film.mjs final           1920x1080 -> out\G56_2-Minute_Pitch.mp4
```
Then confirm 120.0 s or less, sound present, and look at a frame every few seconds.

**Poster**
- Edit `content.json` (words) only; numbers come from `round2_data.json`. Then `npm run build` inside `tools/`. It must end with 0 failed.
- Do not shrink fonts to make text fit; shorten the words. The checker gives each field a length limit.
- Preview picture: `powershell -File export_preview.ps1 -Source ..\02_Research_Poster\Research_Poster_Source.pptx` (PowerPoint must be closed; ask the person to close it, never close it for them).
- QR: save the code for the video's Drive link as `assets/qr_pitch.png`, build again.
- PDF: open the pptx in PowerPoint, File > Export > PDF. Check the page is 22.5 x 28.5 in.

**Logo**
- `python tools/logo/make_logo.py 2000 out.png` (voice Python) rebuilds it at any size.

**Survey numbers**
- Frozen. Change `round2_data.json` only if the team lead says so. Then the Beat 4 lines in `film/lines.json` and their voice files must change with it, and the poster must be rebuilt.

## 6. Rules

From the conference brief, as the team summarised it:

1. Two minutes or less, MP4.
2. Five parts in the order and at about the times in section 1.
3. The demonstration shows the real, working app. No generated or redrawn app screens.
4. Clear narration, with captions.
5. Every number matches `round2_data.json`, is shown with its group size, and is never rounded up.
6. Children are the users; parents and teachers are the bridge. Children's results first. Never "30 users".
7. No claim that children learned to read better. The study measured reactions to the app.
8. The setting is the Philippines.
9. Nothing the team has no right to use: no copyrighted music or footage, no real brands, no real child's face.
10. A cloned voice only with its owner's agreement.

For anyone working in this folder:

- Report what you could not run, see or hear as not verified.
- A teammate improving the pitch can commit freely on their own branch. What gets submitted (the final render, the poster PDF, the files on Drive) changes only with the team lead's agreement. In a session on the team lead's own PC, commit or push only when he asks, and do not quit his programs.
- When copying between a working folder and the repo, never mirror with deletes; copy changed files and let `git status` show the result.
- Keep private things out of the repo (it is public): personal email addresses, and Drive links that anyone can edit.
- AI help is disclosed if asked; `02_Research_Poster/booth_script_and_QA.md` has the answer. If the instructor wants it stated in the video, add a credit line to the end card.

## 7. What went wrong, and what fixed it

- **A cloned line can be wrong while every check says it is right.** Whisper, given the script as a hint, writes the script back. Listening without the hint showed "DepEd's Marungko" was "depth at smarunko". Fix: several takes, `hear.py`, pick by ear or by the blind transcript.
- **The best take ran a second too long.** Shortening only the pauses fitted it without touching the words.
- **Times read off the recording were off by 0.4 s** because of the missing still frames. Cutting one picture per film frame and reading times from those fixed it.
- **A screenshot sat on the phone through the whole problem scene**, from a keyframe list that started at 1:05 (see "Change the picture").
- **The app looked cut off in the phone** because the drawn screen was wider than a real one. The screen now has the recording's shape, 75 x 158.
- **Mia's forearm came off her arm** after the arms were nested: `film.css` gives any element with its own turning point `transform-box: fill-box`, except those listed for `view-box`. New arm groups must be added to that list.
- **Hands on the table disappeared**: skin and wood are nearly the same colour. A shadow under the hand and thin lines between the fingers fixed it.
- **A tracing arm that came in from the side of the book looked like someone else's.** A hand coming over the top edge of the book reads as hers.
- **The teaching score looked like it came from all 9 teachers.** The cards now carry their group sizes.
- **The 1080p render was made too early** and had to be thrown away after the music and voice changed. Render last.
- **On the lead's PC,** Remotion's bundled ffmpeg lacks most filters (no `fps`, `setpts`, `volumedetect`), and several download sites are blocked; see section 9.

## 8. Making it better

### What is fixed and what is open

**Fixed** (changing these breaks the brief or the team's word):
- The ten rules in section 6, the 2:00 limit, the five parts and their times, the file names.
- The survey numbers. You may choose *which* real figures to show; you may not change one.
- Only the real app inside the phone. A cloned voice only of someone who agreed.

**Open** (the team lead decides, by seeing it):
- The wording of the script, inside the word limits the checker enforces per part (45, 50, 68, 70, 40).
- Every shot, pose, colour, caption, sound and piece of music, and the pacing inside each part.
- Which figures the results board shows, the poster's wording and layout.
- Whether the narration stays a clone or is recorded for real.

### Where it is weakest, most valuable first

These are the build side's own judgements, made from stills and measurements. None of the sound points has been confirmed by ear.

**Sound** (probably the biggest gain, and the least checked)
1. **Record the narration for real.** The clone is flat in places and slurs some words. `b4_03`, the three scores, is the sentence judges care about most; heard blind it says "opened the door" and "Grade 1 fits". `b3_02b` has a hiss on "leaves". A phone recording of the team lead in a quiet room, one WAV per line named by its id, drops straight into the same steps ("Redo a spoken line", from step 6). Recording even just Beat 4 would help.
2. **Loudness.** The 12 Nov render measured -20.8 LUFS, which is quiet; online video is usually around -14 to -16. In a noisy hall on a phone speaker it may not carry. Measure the final file and bring it up with a limiter (a full ffmpeg has `loudnorm`; Remotion's bundled one does not, the voice Python's `imageio-ffmpeg` does).
3. **Music.** It sits about 20 dB under the voice and may be inaudible. Listen on a phone speaker, not headphones. The two Gemini stings in `assets/music/` came out a minute long and are unused; a 3-second cut of each could mark Lily's entrance and the end.

**Reading it on a phone** (most judges will arrive by the QR code)
4. **Captions.** The words sit close together, and the word being spoken grows into its neighbours ("It followsDepEd'sMarungko order" in stills). More space between words (`.cap .w` in `film/film.css`) would help; then check the longest captions still fit.
5. **The app inside the phone is small.** At phone size its text is hard to read. The lips card and the green check are enlarged beside the phone; the Hear It card and the progress report are not. Enlarge those too, or push the camera in further.
6. **Mia's resting hands** are subtle against the table.

**The results part**
7. At 1:09 the screen is empty for about a second before the "30" pops in.
8. Three ratings in ten seconds is a lot to take in. Consider a slower reveal or one figure fewer. Unused real figures exist: usability score 75.5 from the 5 parents, and 15 of 16 children liked the characters.

**The argument**
9. **The problem has no evidence.** It is told as a story, and the poster's three problem statements have no source either; the poster's references are about method, not about the problem. If the rubric rewards a grounded problem, add one sourced fact. Use a real citation the team has read. Do not invent one.
10. **The close has no "what next".** The poster says "Next: measure reading gains over a full term"; the film does not. The last part has about six words to spare.

**Not done at all**
11. The instructor's rubric has not been read against the film. Get it and score each criterion.
12. Nobody outside the team has watched it.

### How to tell a change made it better

- **Cold viewers.** Show it once to two or three people who have not seen it. Ask: what does the app do, who is it for, name one result, would a child like it. If they cannot answer, that part is not working, however good it looks.
- **The real conditions.** Watch it on a phone, speaker at arm's length, with background noise.
- **Before and after.** Keep the old version (old takes go in a folder, pictures in `film/_shots/`) and put the two side by side.
- **Still inside the rules.** `npm run build` in `tools/` ends with 0 failed; the render is 120.0 s or less; every number still matches `round2_data.json`.

### How to change things without losing what works

- One change at a time, on small commits, so any of them can be undone.
- For a picture change, make stills or a short draft render of that part (`render_film.mjs draft 40 70`) and show the team lead before going further.
- After any voice change, rebuild the data and the music (section 5), then check the stills at the words that drive the picture.
- Write down what you changed and why in section 10, and move anything you fixed out of this list.

## 9. Notes on the team lead's PC

Only relevant if you work on that machine (Windows with WSL, old i7, no graphics card).

- The working copy there is `Desktop\PlayIT_TekCon_2026\`, which is not a git folder; it is copied to and from this repo by hand.
- The voice Python is `C:\Users\riva.zn\playIt\docs\tekcon-2026\tools\voice\.venv\Scripts\python.exe`. Set `KIT_VOICE` to that `tools\voice` folder before `clone_lines.py`, because the model lives there.
- A full draft render takes about 20 minutes; cloning takes one to two minutes per line.
- Blocked on that network: youtube.com, pixabay.com, download.pytorch.org, astral.sh, remotion.media, fonts.gstatic.com. Set `PITCH_BROWSER` to Chrome because Remotion cannot download its own browser.

## 10. Short history

- **10–11 Nov:** kit and poster built; a slideshow video drafted with Antigravity, then with Claude Code; rejected as too static. The film in `film/` replaced it the same day, approved step by step from `mockups/`.
- **12 Nov:** real survey numbers frozen; Beat 4 re-voiced; Erik's recording wired into the phone; "Dj" relabelled "Mia"; arms redrawn; logo rebuilt as the app icon; Gemini music added. A cloud session rendered 1080p from that day's state.
- **13 Nov:** two lines re-cloned (`b2_03`, `b3_03`); group sizes on the rating cards; phone shows the start screen; one line moved under the logo; screens fitted to the phone; hands redrawn. Final render, QR, PDF and upload still to do.
