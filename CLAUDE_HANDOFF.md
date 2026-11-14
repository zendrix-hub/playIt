# PlayIT TekCon 2026 kit: hand-off

Rewritten 14 Nov 2026. For a teammate (Erik Samuel Bien, `thatguyKire`) and their Claude Code session who pulls this branch to **make the pitch better and complete the final deliverable folders**. It says what was made, what the team lead (Zendrix) decided and why, how each piece is built, what went wrong on the way, and, in sections 2.1 and 8, the exact remaining tasks for Claude to generate new files from the gathered survey data and improve the pitch.

**To the Claude session reading this:**
1. Start by reading `TEKCON_SUBMISSION_GUIDE.md` for the official department rubrics, file naming standards, and automated checker requirements.
2. Read Section 2.1 below: your core assignment is to **generate and update new documentation and evaluation files for the remaining subfolders in Google Drive (`G56/`) using the current survey gathered**.
3. Inspect the film (`film/film.html`, `film/lines.json`, poster preview in `02_Research_Poster/poster_preview.png`) and compare with section 8.

Branch: `erik/film-fixes` (synced with `docs/tekcon-2026-pitch-kit`). The repo is public.

---

## 1. What has to be handed in

Due **14 November 2026**. One printed poster on cartolina, plus the final standardized 7-category Google Drive repository under team folder `G56/` (announced 14 Nov, replacing earlier 3-folder layouts):

```text
G56/
├── 01_MVP_Validation/
├── 02_Software_and_Deployment/
├── 03_Updated_Technical_Documentation/
├── 04_Testing_and_Quality_Assurance/
├── 05_Final_User_Validation_and_Evaluation/
├── 06_Research_Paper/
└── 07_TechCon_2026/
    ├── 01_Research_Poster/        G56_Research_Poster.pdf, G56_Research_Poster_Source.pptx
    ├── 02_Video_Pitch/            G56_2-Minute_Pitch.mp4
    └── 03_Software_Product_Logo/  G56_Product_Logo_2000x2000.png, G56_Product_Logo_512x512.png
```

- **Poster:** 22.5 x 28.5 in, portrait, with a QR code labelled "Scan to Watch Our 2-Minute Project Pitch" linking directly to `G56_2-Minute_Pitch.mp4` on Google Drive.
- **Video:** MP4, 2:00 or less, in five parts: problem (to 0:20), solution (to 0:40), demonstration of the real app (to 1:10), results (to 1:40), impact and close (to 2:00).
- **Logo:** PNG at 2000 x 2000 and 512 x 512, transparent background, RGB.
- **Team code:** **G56** ("Group 56"). Team: Zendrix Riva (Lead), Erik Samuel Bien, Ashlee Skye Durano, Kaysean Miel, John James Palis. Adviser: Mr. Joemarie C. Amparo.
- Full folder tree and rubric specifications are detailed in `TEKCON_SUBMISSION_GUIDE.md`.

---

## 2. Where things stand (14 Nov, current state)

**Done**
- **Drive Structure Created:** All 7 standardized categories and their subfolders have been created under `G56` (Google Drive ID: `1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc`).
- **Category 07 Populated & Renamed:**
  - `01_Research_Poster/`: `G56_Research_Poster.pdf`, `G56_Research_Poster_Source.pptx`.
  - `02_Video_Pitch/`: `G56_2-Minute_Pitch.mp4` (ID preserved: `1eh_bAiRz3SDQyLK6NRydxCmiMQoWYgYT` - QR code on poster verified active).
  - `03_Software_Product_Logo/`: `G56_Product_Logo_2000x2000.png` ($2000\times2000$), `G56_Product_Logo_512x512.png` ($512\times512$).
- **Categories 01, 02, 03, 04 Populated:** All midterm deliverables, updated technical documents (SRS v3.0, SDD v2.0, SPMP v2.0, STD v1.0, RTM), testing reports, APKs, and `part9_Validation` assets (forms, master responses, photo evidence) are populated.
- **Survey Dataset Frozen:** `round2_data.json` ($N=30$: 16 Grade 1 learners, 5 parents, 9 teachers) with `"status": "actual"`.
- **Collaborator Access:** Erik Samuel Bien (`thatguyKire`) has full direct collaborator access on GitHub on branch `erik/film-fixes`.

---

## 2.1 Mission for Claude: Updating Remaining Folders with Current Survey Data

Claude is tasked with generating and updating the new files needed to complete the remaining subfolders in Google Drive (`G56/`), drawing directly from the **current survey gathered** ($N=30$) in `round2_data.json` and the master responses (`part9_Validation/responses`):

### 1. In `05_Final_User_Validation_and_Evaluation/`
Generate detailed evaluation reports and evidence documents for the empty/partial subfolders:

- **`03_Testing_and_Evaluation_Evidence/`**:
  - `01_Task_Completion_Rates_and_Time_on_Task/`: Generate a report detailing learner task completion rates (e.g. 9 of 12 passing Say It on the first try, Hear It and Blend It progression, time-on-task observations).
  - `02_Questionnaire_Scores/`: Generate an item-by-item statistical report of the Teacher Pedagogical Checklist (mean 4.72/5.0 across items) and Parent Usability ratings (75.5/100).
  - `03_User_Satisfaction_and_Qualitative_Feedback/`: Create an analysis summarizing the 3-point smileyometer results (16/16 happy/okay, 15/16 liked characters, 16/16 play again) along with qualitative quotes from parents and teachers.
  - `04_Success_Criteria_Achievement_Evidence/`: Compile a validation matrix matching observed results against the pre-approved SMART criteria.

- **`04_Results_and_Analysis/`**:
  - `01_Respondent_Profile_and_Participant_Count/`: Demographic and participant breakdown report ($N=30$: 16 Grade 1 learners observed, 5 parents, 9 teachers; clearly stating zero technical evaluators).
  - `02_Validation_Procedure_and_Data_Collection_Period/`: Chronological report of the testing protocols, school location, parent consent, and ethics compliance.
  - `03_Evaluation_Framework_and_Instruments_Used/`: Technical report detailing the ISO/IEC 25010 software quality model and Marungko pedagogical framework integration.
  - `05_Results_According_to_Research_Questions_and_Objectives/`: Comprehensive analysis answering each capstone research question with empirical data.
  - `06_Identified_Issues_and_Functional_Limitations/`: Documented issues identified during testing (offline voice recognition threshold, speech acoustic variance in young learners, device screen variations).
  - `07_Achievement_of_System_Objectives/`: Final evaluation synthesis demonstrating how PlayIT fulfilled its core system objectives.

- **`05_Recommendations_and_Improvements/`**:
  - `01_User_and_Stakeholder_Concerns/`: Detailed compilation of teacher feedback on classroom integration and parent feedback on independent home use.
  - `02_Prioritized_Recommended_Improvements/`: Prioritized feature backlog (expanding Marungko letter progression beyond M, adding teacher dashboards).
  - `03_Actions_Taken_or_Planned/`: Summary of refactoring and UI polish implemented between MVP validation and the final release.
  - `04_Required_Software_and_Documentation_Updates/`: Change log connecting evaluation feedback to updates in the SRS and SDD.
  - `05_Remaining_Limitations_and_Future_Development/`: Long-term recommendations for formal reading gain studies over an academic term.

### 2. In `06_Research_Paper/`
- Prepare and organize:
  - `01_Research_Paper_Draft/`: The working draft of the research paper integrating the final $N=30$ validation results.
  - `02_Final_Editable_ACM_Manuscript/`: The editable ACM manuscript (`.docx` or LaTeX zip archive).
  - `03_Final_PDF_Copy/`: The final publication-ready PDF copy (`G56_Research_Paper_FINAL.pdf`).
*(Follow `RESEARCH_PAPER_SUBMISSION_CHECKLIST.md` located inside Category 06).*

### 3. In `07_TechCon_2026/` (Optional Pitch Re-render)
- If Erik or Claude records live human voice or replaces video clips (per Section 8), re-render `01_Video_Pitch/G56_2-Minute_Pitch.mp4` via `render_film.mjs final` and update the file in `02_Video_Pitch/` on Drive.

---

### Strict Guidelines Claude Must Uphold:
1. **Never Invent Figures:** All figures must match `round2_data.json` exactly.
2. **Participant Integrity:** Always distinguish learners (16 observed) from evaluators (5 parents, 9 teachers). Never say "30 users" or call children "respondents".
3. **Honest Outcomes:** Measure user reactions, engagement, and usability—never claim that children "learned to read better" or had increased reading scores.
4. **Verification:** After modifying any poster or pitch files, run `cd tools && npm run build` and ensure `0 failed`. Report all warnings.

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
