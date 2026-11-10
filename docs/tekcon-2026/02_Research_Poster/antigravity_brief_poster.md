# Brief for Antigravity — fully illustrated version of the poster

**What this is:** the second version of the poster. The first is `G56_Research_Poster_Source.pptx`, which already has the correct text and numbers. Here the agent makes the artwork, and the text still comes from that file, because image models misspell small text and cannot make a working QR code.

**How to use this:** open the `PlayIT_TekCon_2026` folder in Antigravity and tell the agent: *"Follow 02_Research_Poster/antigravity_brief_poster.md."*

## Instructions to the agent

Read `AGENTS.md` first. The wording is in `content.json` and the layout is shown in `02_Research_Poster/poster_preview.png`. Attach `assets/team_pixar.png`, `assets/lily_pixar.png` and `assets/lily_waving.png` as style references.

**Do not put any words, numbers or QR codes in generated images.** Leave clean, calm areas where text panels will sit.

### Item P1 — full background illustration

Portrait, ratio 22.5 : 28.5 (for example 3600 x 4560 px or larger). One continuous scene read from top to bottom, "Lily's island":

- **Top fifth:** a soft sky, `#E1F3FA`, a few round clouds. Leave the top band calm for the title.
- **Upper middle, left:** a dim, cool-toned corner showing a child at a table with a closed book and a phone with no signal. This sits behind "The Problem".
- **Upper middle, right:** the same child now smiling, with Lily beside her. This sits behind "Our Solution".
- **Centre:** a winding island path with four stepping stones leading down the page, one for each step of the app. Rolling green hills in the distance.
- **Lower middle:** a sunny clearing with open space for six number tiles.
- **Bottom:** sand, `#FFF2DC`, with small shells and plants at the corners only.

Style: the 3D animated family film look of the reference images, soft and warm, low contrast so dark text placed on cream panels stays readable. Palette: teal `#0B7F7A`, sunflower `#FFC53D`, apricot `#F7922F`, leaf `#2E9A3E`, hibiscus `#F2545B`, sky `#3BA7D9`.

Save as `02_Research_Poster/generated/P1_background.png`.

### Item P2 — spot illustrations (transparent background, square, 1500 px)

| ID | Picture | Goes with |
|---|---|---|
| P2a | One teacher in front of many small desks | Problem 1 |
| P2b | A paper worksheet with a small sleepy face | Problem 2 |
| P2c | A phone with an empty signal icon | Problem 3 |
| P2d | An ear with sound waves | Hear It |
| P2e | A microphone with a small heart | Say It |
| P2f | Three picture cards, one being tapped | Find It |
| P2g | Three letter tiles snapping together | Blend It |
| P2h | A parent and child looking at a phone together | Impact |

Flat style to match `lily_waving.png`: no outlines, the palette above. Save as `generated/P2a.png` and so on.

### Item P3 — assemble

This version is assembled by hand, on a **copy** of the pptx (name it `..._Illustrated.pptx`), because the build script overwrites the original. Tell the user to: set `P1_background.png` as the slide background, set the cream cards to about 90% opacity so the scene shows through, and place the P2 pictures beside their sections. Do not flatten the text into an image. Make the copy only after the survey numbers are final.

### Check before finishing

- No text or digits anywhere in the generated images.
- Lily matches the references.
- The areas behind text are calm enough to read over.
