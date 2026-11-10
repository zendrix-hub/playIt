---
name: playit-video
description: Build, review, change or render the PlayIT two-minute pitch video (Remotion project in video/, driven by video/timeline.json). Use for any request about the pitch video, its shots, voice, captions, music, screen recordings, or rendering the MP4.
---

# Building the PlayIT pitch video

Read `AGENTS.md` first; its rules about numbers and honesty apply here too. One-time installation is in `SETUP_WINDOWS.md`.

## What this is

A two-minute video rendered from code. You do not edit video by hand and you do not write new scene code unless the user asks for a design change. You supply media, edit one timeline file, run commands, and look at the results.

| Part | Where | Who changes it |
|---|---|---|
| Shot list, cues, assets, labels | `video/timeline.json` | You |
| What the narrator says | `templates/narration_script.template.md` | You, then `npm run build` |
| Survey numbers | `round2_data.json` | You, from the user's figures only |
| Scene code | `video/src/` | Only on a design-change request |
| Everything in `video/public/`, `video/out/`, `video/src/generated/` | generated | Never by hand |

## The five beats are fixed

0:00 Problem · 0:20 Solution · 0:40 Demonstration · 1:10 Results and validation · 1:40 Impact and close · 2:00 end. The brief requires this flow and a maximum of two minutes. Never move a beat boundary. If the voice does not fit a beat, cut words; do not stretch the beat.

## The setting is the Philippines

Every scene is set in the Philippines: a Filipino home and public-school classroom in the story world, and Lily's island (Chocolate Hills of Bohol, coconut palms, a bahay kubo, fiesta banderitas) in the app world. Generated art must follow the Philippine details in `01_Video_Pitch/antigravity_brief_video.md`; redo any image that could pass for a generic Western setting. Music, if used, should be a light instrumental with a Filipino flavour. Do not use the national flag or emblems as decoration.

## Where media goes

| Put this | Here | Becomes |
|---|---|---|
| Lily's poses, team image, other pictures | `assets/` | Lily on the stage, team reveal |
| **Real app footage** (screen recordings or a filmed phone), named as in `01_Video_Pitch/screen_recording_shotlist.md` | `assets/recordings/` | What plays inside the phone |
| Generated stills `G1.png`…, plates `B1.png`, `B2.png`, clips `V1.mp4`… | `01_Video_Pitch/generated/` | The Pixar-style story shots and the morph |
| A hand-supplied voice line, named by line id, for example `b2_02.wav` | `assets/voice/` | Used instead of a generated line |
| A 10 to 20 second sample of the narrator's voice | `assets/voice_sample.wav` | Voice cloning |
| One licensed music track | `assets/music/` | Quiet background music |

A missing file never stops a render. It shows as a dashed orange placeholder that names the file needed.

**Generated clips are the first choice for the story shots.** A shot uses its clip (`V*.mp4`) if present, otherwise its still with a slow camera move, otherwise a placeholder. The morph (shot 6) uses `V3.mp4` if present, otherwise a coded blend through `B2.png`. Generate the stills and clips by following `01_Video_Pitch/antigravity_brief_video.md`.

**The demo shows only real footage inside the phone.** Never generate, redraw or mock up an app screen.

## The working loop

Run everything from inside `tools/`.

1. `npm run video:prep` — copies and converts media into the project.
2. `npm run video:voice` — generates the narration and word timings. Skip until the user wants voice.
3. `npm run video:stills` — one picture per shot in `video/out/stills/`. **Look at every still** (open them in the browser, or ask the user to look if you cannot view images). Check: nothing overlaps the captions, no placeholder is left where media was supplied, text is readable.
4. `npm run video:draft` — a quick half-size video at `video/out/draft.mp4`. Tell the user it is ready to watch.
5. Apply the user's changes, then repeat from the step the change affects.
6. `npm run video:final` — the full-size MP4, named `<TeamCode>_2-Minute_Pitch.mp4`, followed by the checker.

Never say the video is finished without pasting the last two lines of the checker output.

To render only some shots while fixing them: `npm --prefix ../video run stills -- --shots=12,13`.

## Changing things

**Timing.** A shot with a `cue` starts when the narrator reaches those words, so the picture follows the voice automatically. To make a shot start earlier or later, change its cue to different words from the script, or to `@line_id` for the start of a line. The `start` and `end` numbers are only the plan used when no cue is given. Line ids are in `video/src/generated/narration.json`.

**Which recording, pose or label a shot uses.** Edit that shot in `video/timeline.json`. Poses: `idle`, `waving`, `pointing`, `listening`, `celebrating`, `encouraging`, `thinking`.

**The words.** Edit `templates/narration_script.template.md` and the matching row in `templates/storyboard.template.md`, run `npm run build`, then `npm run video:voice`. If you changed words a cue relies on, fix the cue.

**The voice.**
- Draft voice, fast: `python voice/make_voice.py`
- The user's cloned voice: `python voice/make_voice.py --engine chatterbox --sample ../assets/voice_sample.wav --force`. Slow on a PC with no graphics card; run it once, near the end. Needs `python -m pip install chatterbox-tts` first.
- Redo some lines: add `--only b4_01,b4_02`.
- Lily's lines are pitched up automatically. If the app has a real recording of the line, put it in `assets/voice/` under the line id and it is used instead.
- Clone only a voice whose owner has agreed.
- After any voice change run `python voice/align.py` and then `node build_video_data.js` (`npm run video:voice` does all three).

**Music.** Follow `tools/get_music.md`. Licensed tracks only, and record the credit.

**A design change** (a new scene, a different look). Stop and confirm with the user first. If they agree, the Remotion skills installed in `.agents/skills/` describe current Remotion practice; keep colours and fonts from `video/src/theme.js`, keep all numbers coming from `generated/data.json`, and keep the placeholder behaviour.

## What the tools are for

| Tool | Used for | Run by |
|---|---|---|
| Remotion | Drawing and rendering the video | `video:stills`, `video:draft`, `video:final` |
| ffmpeg | Converting recordings and clips, evening out loudness, measuring length | `video:prep`, the checker |
| Whisper (faster-whisper) | Timing each spoken word for captions | `voice/align.py` |
| Kokoro / Chatterbox | Generated or cloned narration | `voice/make_voice.py` |
| yt-dlp | Fetching a licensed music track | by hand, see `tools/get_music.md` |
| Chrome DevTools connector (optional) | Looking at the Remotion preview | `.agents/mcp_config.json` |

`npm run video:studio` opens Remotion's live preview in the browser if you want to scrub through the video.

## If something fails

- **"Timeout … Loading Lexend and Sniglet" or a browser download error:** the network is blocking a download. Set the environment variable `PITCH_BROWSER` to the full path of Chrome (for example `C:\Program Files\Google\Chrome\Application\chrome.exe`) and run again.
- **"ffmpeg not found":** install it (see `SETUP_WINDOWS.md`) and open a new terminal. Until then media is copied without conversion.
- **A cue is "not found":** the words in the cue no longer appear in the script. Fix the cue in `video/timeline.json`.
- **"narration runs past its beat":** shorten that beat's words in the template. Do not move the beat.
- **A recording looks cropped or stretched:** phone footage should be portrait. Re-record, or film the phone upright.

## Stop and ask the user when

- A real app recording is missing and the final render is requested. A placeholder must never ship.
- The numbers are still marked projected and the user asks for the final video.
- A requested change would alter the five-beat flow, exceed two minutes, or show anything in the demo that is not the real app.
- A generated clip of the morph or of Mia does not come out cleanly after three tries.
