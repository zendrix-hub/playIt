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
2. Change the numbers in `lines.json` for lines `b4_01` to `b4_03` so the script matches.
3. Make new voice files for those lines and save them in `voice/` under the same names.
4. Run `node tools/make_data.mjs`, then render.

The PROJECTED tag on the results board disappears by itself once the status is `actual`.

## Rules the film keeps

- The phone only ever shows real app screenshots from the repo.
- Lily is the app's own art, including its talking drawings.
- Numbers are read from `round2_data.json`, never typed into a scene.
- The story pictures and clips in `../assets/` were a guide for the look. They are not used in the film.
