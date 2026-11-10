# Brief for Antigravity — images and clips for the PlayIT pitch video

**How to use this:** open the `PlayIT_TekCon_2026` folder in Antigravity, put your files in `assets/` (names in `assets/README.md`), then tell the agent: *"Follow 01_Video_Pitch/antigravity_brief_video.md. Do one item at a time and show me each result before moving on."*

## Instructions to the agent

You are making still images and short clips for a two-minute student pitch video about PlayIT, an offline phonics app for Grade 1 Filipino learners. Read `01_Video_Pitch/storyboard.md` first for the story.

**Reference images, always attach them:**
- `assets/lily_pixar.png` for Lily in the story world.
- `assets/lily_waving.png` for Lily in the app world.
- `assets/narrator_pixar.png` and `assets/team_pixar.png` for the art style of the story world.
- `assets/lily_transition_8s.mp4` as the motion reference for item V3.

**Style, story world:** 3D animated family film look. Soft rounded shapes, large expressive eyes, warm evening light from a single lamp, shallow depth of field, gentle film grain. A modest Filipino home: wooden table, capiz or jalousie window, a small lamp, a school bag. Match the look of the reference images rather than any named studio or existing film.

**Style, app world:** Lily exactly as in `lily_waving.png`: flat shapes, no outline, orange `#FA842C` with darker orange `#E96D25` shading, cream `#FFE8CE` belly, peach `#FEC484` rings round the eyes, pink `#FE9FA3` blush. Background a soft gradient from sky `#E1F3FA` to sand `#FFF2DC`.

**Characters:**
- **Mia:** a six-year-old Filipino girl, short dark hair with a small yellow clip, plain school blouse. Keep her face, hair and clothes identical in every image. Generate G1 first, then attach G1 as a reference for every later image of her.
- **Lily:** a Philippine tarsier. Never frowning, never sad, never scolding.

**Rules:**
- 16:9, 1920 x 1080 or larger. No text, letters, logos or watermarks inside any image, except the single letter M in the book where stated. All titles are added later in the editor.
- No real brands, no real children, no recognisable real people.
- Save stills to `01_Video_Pitch/generated/` as `G1.png`, `G2.png` and so on; clips as `V1.mp4` and so on.
- After each item, show it and wait for approval.

## Stills

| ID | Shot | Prompt |
|---|---|---|
| G1 | 1 | Wide establishing shot at dusk of a small Filipino house, one window glowing. Through the window, Mia sits at a wooden table with an open early reader, her finger under a large letter M. Warm lamp, cool blue evening outside. |
| G2 | 2 | Close-up of Mia's face in lamp light, lips pressed together as she tries to make the "mmm" sound, eyebrows unsure. The book edge is in soft focus at the bottom. |
| G3 | 3 | A crowded Grade 1 classroom seen from the back row: many small heads, one kind teacher far away at the board. Mia is small in the foreground with her hand half raised. |
| G4 | 3 | The same home at evening: an empty chair by the door, a hook where a work bag usually hangs, a wall clock. Quiet, a little lonely, not sad. |
| G5 | 3 | A phone lying on the wooden table, its screen showing an empty signal icon and nothing else. Mia's small hand rests beside it. |
| G6 | 4 | Mia gently closing her book, eyes down. The lamp light is dimmer. Hold the feeling of giving up for tonight. |
| G7 | 5 | The phone on the table lights up. Two huge round tarsier eyes and the top of Lily's head peek over the bottom edge of the screen. Mia looks up, surprised. Light from the screen on her face. Use `lily_pixar.png`. |
| G9 | 21 | The same table and evening as G1 but warmer and brighter. Mia reads aloud happily, finger under the words. The phone is propped against a glass beside the book with Lily on the screen. |
| G10 | 22 | Close two-shot: Mia smiling proudly mid-sound, and 3D Lily beside her on the table with both arms up, cheering. Use `lily_pixar.png`. |

## App-world plates (flat style, no characters, no text)

| ID | Shots | Prompt |
|---|---|---|
| B1 | 6 to 20 | Lily's island as a wide stage in the flat app style: a soft sky `#E1F3FA` at the top fading to warm sand `#FFF2DC` at the bottom, low rounded green hills, a few round clouds, a winding sand path running left to right across the lower third with four flat stepping stones along it. A round sun in the upper left, in the same spot the lamp occupies in G7. The centre of the frame is calm and empty, because a phone will be placed there. No outlines, no texture, no text. |
| B2 | 6 | The same composition as G7 (table, phone, lamp, window, Mia's position) redrawn in the flat app style and colours, with the table shape reading as the start of the island path and the lamp as the sun. This is the halfway picture for the morph. |

## Clips (4 to 6 seconds each, no camera shake)

| ID | From | Motion |
|---|---|---|
| V1 | G1 | Slow push in toward the lit window. Leaves move slightly. |
| V2 | G7 | Lily's eyes rise a little higher and blink once. Mia leans in. |
| V3 | Start frame G7, end frame B1. References: B2, `lily_pixar.png`, `lily_waving.png`, `lily_transition_8s.mp4` | **The morph, about 5 seconds.** One continuous shot. Lily hops out of the phone and waves. A wave of change spreads outward from the phone: 3D shading flattens into flat colour, the lamp swells into the sun, the wooden table stretches into the sand path, the blue evening brightens to sky and sand, and Lily settles into her flat in-app look, standing on the path. Smooth and steady, like paint spreading; no flash, no cut, no camera move. |
| V4 | G9 | Gentle push in. Mia's lips move as she reads. Lily bounces on the phone screen. |

**If V3 does not come out cleanly in three tries, stop and report.** The fallback needs no generation: in the editor, cross-fade G7 into B2 into B1 over five seconds with a slow zoom toward the phone, and lay the existing 8-second Lily clip on top.

## The demo is assembled in the editor, not generated

Shots 9 to 15 are an animation built from real parts: plate B1 as the stage, a phone frame (a dark rounded rectangle) at the centre, the real screen recordings inside it, and Lily's real pose pictures beside it. **Do not generate or redraw any app screen**, and do not generate Lily for these shots; use the pose files listed in `assets/README.md`. The only pieces to generate for the demo are B1 and, if asked, small flat props: a padlock, a sound-ring, a voice wave, a paper report page (transparent background, no text, saved as `prop_padlock.png` and so on).

## When finished

List every file made, and flag any image where Mia or Lily looks different from the others.
