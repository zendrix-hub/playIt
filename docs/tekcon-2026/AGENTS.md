# Instructions for any AI agent working in this folder

Read this whole file before changing anything. It applies to Antigravity with any Gemini model.

## What this folder is

The TekCon 2026 kit for **PlayIT**, a student capstone: an offline phonics app for Grade 1 Filipino learners, with a tarsier mascot named Lily. Two deliverables, both due **14 November 2026**:

1. A research poster, 22.5 x 28.5 in, portrait (`02_Research_Poster/`).
2. A video pitch of at most two minutes (`01_Video_Pitch/`).

The official requirements are summarised in `README.md`. The survey behind the results (Round 2, 30 participants) is still running, so the numbers will change.

## The one rule that matters most

**You edit four kinds of file. Scripts build everything else.**

| You may edit | What it holds |
|---|---|
| `round2_data.json` | Every survey number |
| `content.json` | Every word on the poster, and which figures are shown |
| `templates/*.template.md` | The wording of the narration script and storyboard |
| `video/timeline.json` | The shots of the pitch video: cues, assets, labels |

| Never edit by hand | Why |
|---|---|
| `02_Research_Poster/*.pptx` | Rebuilt from the files above; hand edits are lost |
| `01_Video_Pitch/narration_script.md`, `storyboard.md` | Generated; they say so on line 1 |
| `tools/*.js`, `video/src/` | Layout, scenes, font sizes and checks. Change only if the user asks for a design change |
| `video/public/`, `video/out/`, `video/src/generated/` | Produced by the scripts |

After **any** edit, run this and read the output:

```
cd tools
npm install        (first time only)
npm run build
```

`npm run build` rebuilds the poster and the pitch files, then runs the checker. **You are not finished until the checker prints no FAIL lines.** Report the WARN lines to the user; do not hide them. Never claim something is done without pasting the last two lines of the checker output.

If the build says the pptx is locked ("EACCES" or "permission denied"), the user has it open in PowerPoint. Ask them to close it. Do not close their programs.

## Rules about numbers

1. **Never invent, round up, or adjust a figure.** Numbers come only from the user or from their survey response sheet. If a number is missing, ask.
2. Counts of people must be whole numbers no larger than the group size. The checker enforces this.
3. Always show the group size with a figure ("9 of 12", "n = 10"). Do not convert small groups to percentages.
4. `"status"` in `round2_data.json` stays `"projected"` until the user says the survey is closed and the numbers are final. Only then set it to `"actual"`. While it is `"projected"`, the poster shows a yellow PROJECTED banner and the script shows numbers in [brackets]. Do not remove those any other way.
5. Never claim that children learned more or read better. The survey measures reactions to the app, not learning.
6. The teacher quote and the unit-test count are real and come from Round 1 and the app's README. Leave them unless the user gives replacements.

## Who the participants are

The app's **users are Grade 1 learners**. They are too young to fill in a questionnaire, so they are **observed while playing** and asked one smiley-face question. **Parents and teachers are evaluators, not users. They are the bridge to the child**: a parent hands over the phone and a teacher decides whether the app fits the lesson, so their acceptance matters. In short: parents rate usefulness and ease of use, and teachers rate teaching quality. The final design is **16 learners, 9 parents and 5 teachers (30 participants)**; there are no technical evaluators, so `technical` in the data file stays at zero. Keep that distinction everywhere:

- Children's figures are always shown first and described as observed ("9 of 12 passed Say It on the first try").
- Never write that "30 users" tested the app, and never call the children "respondents". Write "30 participants" and give the split.
- If the total or the split changes, edit `participants_total`, `groups` and each group's `n` in `round2_data.json`. Nothing else needs changing; the poster dots, labels and the script follow. The checker fails if the groups do not add up to the total.

## Choosing which figures to show

The poster shows exactly **3 people-counts** and **3 ratings** (`results.counts` and `results.ratings` in `content.json`), plus one headline figure in the title band. The pitch speaks the fields listed in `pitch_fields`.

When real results arrive, the user wants the **six strongest real figures**. To swap one:

- Change the `value` path (and `of` for counts) to another entry in `round2_data.json`, and rewrite the `label` to match what that figure means.
- Keep `headline` pointing at the strongest child result.
- Show at least one figure from children, one from parents and one from teachers.
- If a figure misses its goal, it may still be shown; do not delete the `goal` to hide the miss.
- For the pitch, add the new field to `pitch_fields`, then use it as `{{name}}` in the templates, and rewrite the sentence in Beat 4.

Tell the user which figures you chose and which you left out.

## Rules about words and design

- **Do not shrink fonts to make text fit.** Shorten the words. The checker gives a character limit for every field.
- The layout holds a fixed number of items (3 problems, 3 badges, 4 steps, 4 tech chips, 3 counts, 3 ratings, 2 impact points, 4 references). The checker fails if you add or remove one.
- Colours: teal leads, sunflower highlights, cream cards. The four step colours are used only on the four steps. Do not add colours.
- Fonts: Lexend for text, Sniglet for titles. Both must be installed on the PC or PowerPoint substitutes them and lines wrap badly.
- The QR label must read exactly: `Scan to Watch Our 2-Minute Project Pitch`.
- The narration must stay within 265 words and 120 seconds; each beat has its own limit. The checker enforces this.

## Pictures

Put pictures in `assets/` with the exact file names in `assets/README.md`, then rebuild. The build places each one in its slot, fitted and centred. A missing picture leaves a dashed orange box. Do not paste pictures into the pptx by hand.

To generate art, follow `01_Video_Pitch/antigravity_brief_video.md` and `02_Research_Poster/antigravity_brief_poster.md`, one item at a time, and show each result before continuing. **Never put words, numbers or a QR code inside a generated image**; image models misspell them.

## Checking your own work

The checker covers arithmetic, text length, timing and file freshness. It cannot see the poster. After a build, if PowerPoint is installed on Windows, render a picture and look at it:

```
cd tools
powershell -ExecutionPolicy Bypass -File export_preview.ps1 -Source ..\02_Research_Poster\G56_Research_Poster_Source.pptx
```

Then open `02_Research_Poster/poster_preview.png` and check: no text runs outside its card, no text overlaps a picture, nothing is cut off at the bottom edge. If you cannot view images, say so and ask the user to look.

## The pitch video

The video is rendered from code. For anything about it, use the project skill `playit-video` (`.agents/skills/playit-video/SKILL.md`): where media goes, the working loop, how to change timing, voice and music, and when to stop and ask. One-time installation is in `SETUP_WINDOWS.md`.

There is also a second, separate route: `01_Video_Pitch/antigravity_freeform_brief.md` asks you to make the video your own way, with no prescribed tools. Use it only when the user asks for the freeform version, and keep its output in `video_freeform/` so the two can be compared.

## Common tasks

**"Here are the new survey numbers."** Edit `round2_data.json` only. Run the build. Report what changed and any FAIL or WARN lines.

**"The survey is closed, these are final."** Enter the numbers, set `"status": "actual"`, choose the six strongest figures (rules above), run the build, render the preview, and confirm the PROJECTED banner is gone.

**"Change this sentence on the poster."** Edit `content.json`. Run the build.

**"Change what the narrator says."** Edit `templates/narration_script.template.md` and the matching row in `templates/storyboard.template.md`. Run the build.

**"Change the team code."** Edit `team_code` in `content.json`, run the build, and delete the old pptx with the previous name.

## When to stop and ask the user

- A number is missing, or two sources disagree.
- A real result is weak and you are unsure whether to show it.
- The checker fails and you cannot fix it by shortening text or correcting a number.
- Any change to layout, colours, fonts or the story of the video.
- The freeze: survey numbers are due to be final on the evening of **12 November**, signed off by the team lead, with printing on the 13th. After the freeze, change numbers only on the team lead's instruction.
