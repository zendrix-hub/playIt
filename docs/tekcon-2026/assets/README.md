# Assets to drop into this folder

Copy your files here on the PC that has them, using these exact names. The Antigravity briefs and the poster slots look for these names.

| File name | What it is | Used in |
|---|---|---|
| `team_pixar.png` | The Pixar-style image of the group | Poster slot A, video shot 23 |
| `narrator_pixar.png` | Your own Pixar-style character on its own | Video reference for the narrator |
| `lily_pixar.png` | Lily in the Pixar style | Video shots 5, 6, 21, 22 |
| `lily_waving.png` | Lily in her real in-app flat style, transparent background | Poster slot B, video shots 16, 24 |
| `lily_transition_8s.mp4` | Your 8-second clip, Pixar style changing to app style | Reference for video shot 6 |
| `screen_hearit.png` `screen_sayit.png` `screen_findit.png` `screen_blendit.png` `screen_parent.png` | Fresh screenshots from the newest build | Poster slots C1 to C5 |
| `qr_pitch.png` | QR code pointing to the uploaded video | Poster slot Q |

## For the pitch video

| Folder or file | What goes in it |
|---|---|
| `recordings/` | Real app footage, portrait, named as in `01_Video_Pitch/screen_recording_shotlist.md` (`rec_splash.mp4`, `rec_sayit_pass.mp4`, …). A screen recording or a filmed phone both count. |
| `voice_sample.wav` | 10 to 20 seconds of the narrator speaking clearly, for voice cloning |
| `voice/` | Hand-supplied voice lines named by line id (for example `b2_02.wav` for Lily's "Mabuhay!" taken from the app) |
| `music/` | One licensed track, plus `CREDITS.md` |
| `qr_app.png` | Optional second QR code for the poster, linking to the app installer |

Generated stills and clips go in `01_Video_Pitch/generated/`, not here.

## Lily's poses for the animated demo

Copy these from the app repo, folder `app/src/main/assets/images/mascot/`, into `assets/` without renaming: `lily_idle.png`, `lily_waving.png`, `lily_pointing.png`, `lily_listening.png`, `lily_celebrating.png`, `lily_encouraging.png`, `lily_thinking.png`. They are the real in-app drawings, so the demo matches the app exactly. The mouth-shape cue pictures are in `app/src/main/assets/images/mouth/`.
