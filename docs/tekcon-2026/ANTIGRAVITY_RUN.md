# Runbook for Antigravity — PlayIT poster and pitch video

**To the user:** open this kit folder in Antigravity and send one message:

> Read ANTIGRAVITY_RUN.md and execute it.

Send the same message again whenever you have added files or want to continue. The agent works out where things stand and carries on from there.

---

**To the agent:** everything below is for you. Work through the phases in order. Each phase starts with a check; if the check already passes, skip the phase and say so in one line.

## Ground rules

1. Read `AGENTS.md` now, in full. Its rules about numbers, wording and honesty override anything here.
2. For any video work, also read `.agents/skills/playit-video/SKILL.md`.
3. Run commands from inside `tools/` unless a step says otherwise.
4. **Stop only at the points marked STOP.** Everywhere else, keep going without asking.
5. At a STOP, tell the user exactly what you need, in a short list, and end your turn.
6. Never invent a number, never generate or redraw an app screen, never hide a checker warning.
7. If a command fails, read the error, try the fix listed in the skill's "If something fails" section once, and if it still fails, STOP and show the error.
8. End every turn with the status block described at the bottom.

## Phase 0 — Where things stand

Run these and read the results before doing anything else:

```
node -v
cd tools
npm run check
```

- If `node` is missing or `npm run check` cannot start, go to Phase 1.
- Otherwise note every WARN and FAIL line. They tell you which phases still have work.

## Phase 1 — Setup (once)

**Check:** `node -v`, `ffmpeg -version` and `python --version` all print a version, and both `tools/node_modules` and `video/node_modules` exist.

If not, follow `SETUP_WINDOWS.md` sections 2 and 3, then run its section 6 "First run". Report each self-test result in a table.

If the first render fails with a download or timeout error, set `PITCH_BROWSER` to the Chrome path as that file describes, and run it again.

Do not install voice cloning (`chatterbox-tts`) yet.

## Phase 2 — What the user must supply

**Check:** list which of these exist.

| Needed | Location | Used for |
|---|---|---|
| Lily's poses (`lily_waving.png`, `lily_pointing.png`, `lily_listening.png`, `lily_celebrating.png`, `lily_encouraging.png`, `lily_idle.png`) | `assets/` | Poster hero, video demo |
| Pixar-style group image `team_pixar.png` | `assets/` | Poster, team reveal |
| Pixar-style Lily `lily_pixar.png` and narrator `narrator_pixar.png` | `assets/` | Style references for generated art |
| Five fresh app screenshots (`screen_hearit.png`, `screen_sayit.png`, `screen_findit.png`, `screen_blendit.png`, `screen_parent.png`) | `assets/` | Poster |
| Nine real app recordings (names in `01_Video_Pitch/screen_recording_shotlist.md`) | `assets/recordings/` | Video demo |
| Narrator voice sample `voice_sample.wav`, 10 to 20 seconds | `assets/` | Final voice |

Lily's poses can be copied from the app repository if it is on this PC: `app/src/main/assets/images/mascot/`. Do that yourself if you can find it.

Whatever is missing, carry on to Phase 3 so the user can see the placeholders, then ask for the files there.

## Phase 3 — Build and first look

```
npm run build
npm run video:prep
npm run video:stills
```

Look at `02_Research_Poster/poster_preview.png` if you can render it (see `AGENTS.md`, "Checking your own work") and at every picture in `video/out/stills/`. If you cannot view images, say so.

Report: which poster slots and which video shots still show placeholders.

**STOP if anything in the Phase 2 table is missing.** Ask for it as a checklist: one line per missing file, with the exact file name and the folder it goes in, and what it is used for. Say which items you can make yourself (the generated story art in Phase 4) and which only the user can supply (their own art, the app recordings, the voice sample, fresh screenshots). End with: "Add what you can, then send the same message again. To go ahead without the rest, say: continue without them."

When the user sends the message again, re-check. If they said to continue without them, go on to Phase 4 and do not ask again for the same files until Phase 7 or 8, where real recordings and pictures are required.

## Phase 4 — Story art (generated)

**Check:** which of `G1`–`G7`, `G9`, `G10`, `B1`, `B2` (png) and `V1`–`V4` (mp4) exist in `01_Video_Pitch/generated/`.

Follow `01_Video_Pitch/antigravity_brief_video.md`. The prompts are already written there; use them as they are and do not ask the user to write prompts. When you show a result, show the prompt you used beside it, so the user can ask for a change in plain words ("make the room darker", "Mia should look younger"). Apply such changes by editing that prompt in the brief, then regenerate. Order matters:

Every image is set in the Philippines; follow the Philippine details in the brief and redo any image that looks like a generic Western setting.

1. **G7** (Lily peeking over the phone) and **B1** (the island stage). These are the start and end of the morph.

   **STOP — show G7 and B1 and wait for approval.** The whole look depends on these two.

2. After approval: **G1** (it fixes Mia's appearance), then G2 to G6, G9, G10, attaching G1 as the reference for Mia each time.
3. **B2**, then the clips: **V3** (the morph) first, then V1, V2, V4. Make each clip by image-to-video from its approved still, as the brief says, so it shows exactly that picture in motion. If you have no image-to-video tool, STOP and tell the user; they can make the clips elsewhere from the same stills and save them as `V1.mp4`–`V4.mp4` in `01_Video_Pitch/generated/`.
4. If V3 or any clip is not clean after three tries, leave it out. The video falls back to a still with a camera move, and to a coded blend for the morph. Say which ones fell back.

Then run `npm run video:prep` and `npm run video:stills`, and look again.

If you have no image or video generation available, say so and skip this phase; the placeholders stay.

## Phase 5 — Draft voice and draft video

**Check:** `video/public/voice/` holds a `.wav` for each spoken line, and `video/out/draft.mp4` is newer than `video/timeline.json`.

```
npm run video:voice
npm run video:draft
```

This is the first real run of the voice scripts. If a Python package or option is wrong, fix the script, say what you changed, and continue.

Read the checker lines about narration. If a beat overruns, cut words in `templates/narration_script.template.md` (and the matching storyboard row), run `npm run build`, then repeat this phase. Never move a beat boundary.

**STOP — tell the user `video/out/draft.mp4` is ready to watch, list what is still a placeholder, and ask for changes.** Apply changes through `video/timeline.json` or the templates as the skill describes, re-render, and stop here again until the user says the draft is good.

## Phase 6 — Final numbers

**Check:** `round2_data.json` has `"status": "actual"`.

If it is still `"projected"`:

**STOP — ask the user for the final survey figures.** Ask for exactly these, and nothing may be estimated:

- Total participants and the split: learners, parents, teachers.
- Learners: how many chose the happy, okay and sad face; how many tapped the first letter unaided; finished a letter with no adult help; passed Say It on the first try, and within two tries; Find It outcomes; Blend It outcomes.
- Parents: the mean for usefulness, ease of use, dashboard clarity, maths gate, offline benefit, safety, and intention to keep using; home internet counts; SUS score and how many parents answered it, if collected.
- Teachers: the mean for each of the seven items.
- Confirmation from the team lead that these are final.

When you have them: enter them in `round2_data.json`, set `"status": "actual"`, choose the six strongest figures by the rules in `AGENTS.md`, update `pitch_fields` and the templates if the spoken figures change, and run `npm run build`. Tell the user which figures you chose and which you left out.

## Phase 7 — Final poster

**Check:** `npm run check` shows no FAIL, no missing-picture warning, and no projected warning.

1. Confirm the team code with the user if `content.json` still says `G56`.
2. The QR code needs the video link, so the poster is finished after Phase 8. Until then the QR slot stays a placeholder.
3. Run `npm run build`, render the preview, look at it, and report anything that overlaps or is cut off.

## Phase 8 — Final video

Do this only when every recording is present, the story art is settled, the numbers are actual, and the user has approved the draft.

1. Final voice: `python -m pip install chatterbox-tts`, then
   `python voice/make_voice.py --engine chatterbox --sample ../assets/voice_sample.wav --force`,
   then `python voice/align.py --force`. This is slow without a graphics card; warn the user before starting. If cloning fails or sounds wrong, keep the draft voice and say so.
2. Music: if `assets/music/` is empty, ask the user for a licensed track (see `tools/get_music.md`). The video is fine without music.
3. `npm run video:stills`, look at every still: no orange placeholder, no PROJECTED tag.
4. `npm run video:final`.

**STOP — report the file path and its measured length, paste the last two lines of the checker, and tell the user what is left for them:**

- Watch the final video once, start to finish.
- Upload it to Google Drive with sharing set to "Anyone with the link – Viewer".
- Make a QR code from that link, save it as `assets/qr_pitch.png`, and scan it from a second phone.
- Send "Read ANTIGRAVITY_RUN.md and execute it" once more so the poster is rebuilt with the QR code.
- Export the poster to PDF from PowerPoint at 22.5 x 28.5 in, and print it.

## Phase 9 — Finish the poster

**Check:** `assets/qr_pitch.png` exists.

Run `npm run build`, render the preview, look at it. When the checker's last line reads `READY TO PRINT AND RECORD: yes`, say so. If it does not, list what remains.

## Optional — the freeform video

Only if the user asks for "the freeform version": follow `01_Video_Pitch/antigravity_freeform_brief.md`. It is a separate video made your own way, saved in `video_freeform/`. It does not replace any phase above.

## Status block (end every turn with this)

```
PHASE: <number and name you stopped in>
DONE THIS TURN: <one line per thing completed>
CHECKER: <the last two lines of npm run check>
PLACEHOLDERS LEFT: poster <n>, video shots <list>
NOT VERIFIED: <anything you could not check, for example "could not view images">
NEEDED FROM YOU: <short list, or "nothing">
```
