# PlayIT — TekCon 2026 pitch kit

A storyboard for the two-minute video and a research poster. Deadline: **14 November 2026**.

## Read this first

**The Round 2 numbers are projections.** The survey is still running. The poster shows a yellow PROJECTED banner and the script shows those numbers in [brackets]. The brief asks for actual results, so put the real numbers in before you print or record. The reasoning behind each projection is in `round2_projection_notes.md`.

## How the kit works

You change three things; scripts build the rest.

| Edit this | To change |
|---|---|
| `round2_data.json` | Any survey number |
| `content.json` | Any wording on the poster, and which figures are shown |
| `templates/` | What the narrator says and the storyboard |
| `assets/` | Pictures (drop them in with the names in `assets/README.md`) |

Then run:

```
cd tools
npm install        (first time only; needs Node.js)
npm run build
```

That rebuilds the poster and both pitch files, then checks everything: the numbers add up, the text fits, the narration is under two minutes. The last line says whether the kit is ready to print and record.

**With Antigravity:** open this folder and say *"Read AGENTS.md, then …"* followed by what you want, for example *"… update the survey numbers to these"*. `AGENTS.md` holds the rules the agent must follow.

**Close the poster in PowerPoint before building.** An open file cannot be overwritten.

## What is here

| File | What it is |
|---|---|
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
| `tools/` | The build and check scripts |

## On the PC with your assets

1. Copy this whole folder across.
2. Install the two fonts (free, Google Fonts): **Lexend** and **Sniglet**. Without them PowerPoint substitutes a font and lines wrap badly.
3. Install Node.js if it is not there (nodejs.org, the LTS version).
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
- [ ] Video is 2:00 or shorter, MP4

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
