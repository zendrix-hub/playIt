# PlayIT — TekCon 2026 pitch kit

A research poster and a two-minute pitch video, both built by scripts from a few plain files. Deadline: **14 November 2026**.

## Quickest start with Antigravity

Open this folder in Antigravity and send: **"Read ANTIGRAVITY_RUN.md and execute it."** The agent sets up, builds, generates art, drafts the video, and stops only where it needs you. Send the same message again to continue.

## Read this first

**The Round 2 numbers are projections.** The survey is still running. The poster shows a yellow PROJECTED banner and the script shows those numbers in [brackets]. The brief asks for actual results, so put the real numbers in before you print or record. The reasoning behind each projection is in `round2_projection_notes.md`.

## How the kit works

You change three things; scripts build the rest.

| Edit this | To change |
|---|---|
| `round2_data.json` | Any survey number |
| `content.json` | Any wording on the poster, and which figures are shown |
| `templates/` | What the narrator says and the storyboard |
| `video/timeline.json` | The shots of the video: cues, recordings, poses, labels |
| `assets/` | Pictures (drop them in with the names in `assets/README.md`) |

Then run:

```
cd tools
npm install        (first time only; needs Node.js)
npm run build
```

That rebuilds the poster and both pitch files, then checks everything: the numbers add up, the text fits, the narration is under two minutes. The last line says whether the kit is ready to print and record.

## The pitch video

The video is rendered from code, so the agent produces a finished MP4 and you review it.

```
cd tools
npm run video:prep      prepare your media
npm run video:voice     generate the narration and caption timing
npm run video:stills    one picture per shot, to check before rendering
npm run video:draft     quick half-size video
npm run video:final     full-size MP4, then the checker
```

- **It always renders.** Anything not supplied yet shows as a dashed orange placeholder naming the file needed, so you can judge the pacing before the art exists.
- **The picture follows the voice.** Each shot starts when the narrator reaches its cue words.
- **Real footage is required.** The brief asks for actual recordings or demonstrations of the working system. Put screen recordings (or a filmed phone) in `assets/recordings/`; they play inside the phone on screen.
- **Setup:** `SETUP_WINDOWS.md`. **How to work on it:** `.agents/skills/playit-video/SKILL.md`.
- **A second route:** `01_Video_Pitch/antigravity_freeform_brief.md` gives Antigravity the goal and the rules but no tools, and lets it make the video its own way. Its output goes in `video_freeform/` so you can compare the two and submit the better one.

**With Antigravity:** open this folder and say *"Read AGENTS.md, then …"* followed by what you want, for example *"… update the survey numbers to these"*. `AGENTS.md` holds the rules the agent must follow.

**Close the poster in PowerPoint before building.** An open file cannot be overwritten.

## What is here

| File | What it is |
|---|---|
| `ANTIGRAVITY_RUN.md` | The runbook: one file the agent reads and executes, phase by phase |
| `AGENTS.md` | Rules for an AI agent editing this kit |
| `01_Video_Pitch/storyboard.md` | 24 shots across the five required beats (generated) |
| `01_Video_Pitch/narration_script.md` | The spoken words only (generated) |
| `01_Video_Pitch/screen_recording_shotlist.md` | Which app screens to record |
| `01_Video_Pitch/antigravity_brief_video.md` | Brief for generating the story images and clips |
| `02_Research_Poster/G56_Research_Poster_Source.pptx` | The poster, editable, 22.5 x 28.5 in portrait (generated) |
| `02_Research_Poster/poster_preview.png` | A picture of the poster, rendered with a stand-in font |
| `02_Research_Poster/poster_content.md` | Where each line on the poster comes from |
| `02_Research_Poster/antigravity_brief_poster.md` | Brief for the fully illustrated version |
| `round2_projection_notes.md` | The reasoning behind each projected number |
| `01_Video_Pitch/antigravity_freeform_brief.md` | Brief for the instruction-only route: the agent chooses its own tools |
| `video/` | The video project (Remotion) and its timeline |
| `SETUP_WINDOWS.md` | One-time installation on the Antigravity PC |
| `.agents/` | The video skill and an optional connector, read by Antigravity |
| `survey/` | The SUS questions for the parent section of the form |
| `tools/` | The build, voice and check scripts |

## On the PC with your assets

1. Copy this whole folder across.
2. Install the two fonts (free, Google Fonts): **Lexend** and **Sniglet**. Without them PowerPoint substitutes a font and lines wrap badly.
3. Follow `SETUP_WINDOWS.md` (Node.js, ffmpeg, Python, and the project packages).
4. Put your pictures in `assets/`, run the build, and open the pptx.
5. Take fresh app screenshots. The ones in the repo are older than the new Lily and the name prompt.

## If Node.js is not available

You can still finish by hand in PowerPoint:

1. Type the real numbers into the results section, and count the filled dots in each row to match.
2. Drag the right end of each teal bar so it matches the rating. The bar spans 1 to 5.
3. Delete the yellow PROJECTED banner.
4. Insert each picture over its dashed box, then delete the box.
5. Update the bracketed numbers in `01_Video_Pitch/narration_script.md` by hand.

After that, do not run the build again, or the hand edits are overwritten.

## Letting people try the app (optional)

The app is not on the Play Store, so the only way to hand it out is the installer file. If you want that on the poster:

1. Upload the APK to Google Drive, set sharing to "Anyone with the link", make a QR code from the link, and save it as `assets/qr_app.png`.
2. Rebuild. A small second code labelled "Try the app (Android)" appears beside the video QR. The required video QR and its label do not change.

Know the limits before relying on it:
- Android only, and the person must allow installs from unknown sources and accept a Play Protect warning.
- Google Drive warns that it cannot scan large files for viruses, and the app includes a speech model, so it is large.
- Check your university's policy on sharing installers publicly.

More reliable on the day: one or two phones at your poster with the app already installed.

## Dates

- **12 Nov, evening:** survey numbers frozen and signed off by the team lead.
- **13 Nov:** print.
- **14 Nov:** submit.

## Before you print

- [ ] `npm run build` ends with `READY TO PRINT AND RECORD: yes`
- [ ] No PROJECTED banner; numbers match your response sheet
- [ ] Names, adviser and team code checked
- [ ] Video uploaded to Google Drive, sharing set to "Anyone with the link – Viewer"
- [ ] QR code made from that link, saved as `assets/qr_pitch.png`, and scanned from a second phone
- [ ] Exported to PDF (File > Export > PDF) at 22.5 x 28.5 in
- [ ] Video is 2:00 or shorter, MP4, with no orange placeholder anywhere and no PROJECTED tag

## Submission names

`G56` is a stand-in. Set the real code in `content.json` (`team_code`).

```
TeamCode/
└── 06_TekCon_2026_Presentation/
    ├── 01_Research_Poster/
    │   ├── TeamCode_Research_Poster.pdf
    │   └── TeamCode_Research_Poster_Source      (the pptx)
    └── 02_Video_Pitch/
        └── TeamCode_2-Minute_Pitch.mp4
```

Plus one printed copy on cartolina.
