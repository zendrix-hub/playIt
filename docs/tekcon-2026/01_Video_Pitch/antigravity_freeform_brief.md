# Freeform brief — make the pitch video your own way

**For the agent.** This is the open route. No tools are prescribed here: use whatever you judge best for making video, including any image, video, voice or music generation available to you. The coded video in `video/` is a separate route and stays untouched. Save everything you make for this route in `video_freeform/`.

Read `AGENTS.md` first. Its rules about numbers and honesty apply in full.

## The job

Produce one finished MP4, at most 2:00 long, that makes a judge at a university research conference think: **"Kids would love this, and it really works."**

It is the pitch for **PlayIT**, an Android phonics game for Grade 1 Filipino learners. A tarsier named Lily teaches letter sounds in the DepEd Marungko order, listens to the child say them using speech recognition that runs on the phone, and works with no internet at all.

## What you have to work with

- The story, shot by shot: `01_Video_Pitch/storyboard.md`. Treat it as a strong suggestion, not a script you must follow.
- The words: `01_Video_Pitch/narration_script.md`.
- The numbers: `round2_data.json`.
- The look of the app: colours and fonts in `video/src/theme.js`; Lily's real drawings in `assets/` (`lily_*.png`).
- The team's own art in `assets/`: the Pixar-style group image, the narrator character, Lily in Pixar style, and an 8-second clip of Lily changing from Pixar style to app style.
- Real app footage in `assets/recordings/`.

## What must be true of the result

These come from the conference brief and are not open to interpretation.

1. **Two minutes or less.** MP4.
2. **This order, at about these times:** the problem (to 0:20), the solution (to 0:40), a demonstration (to 1:10), results and validation (to 1:40), impact and closing (to 2:00).
3. **The demonstration shows the real, working app.** Use the footage in `assets/recordings/`. You may frame it, animate around it and add labels. You may not generate, redraw or imitate app screens.
4. **Narration is clear and easy to understand**, with captions on screen, because many people will watch on a phone in a noisy hall.
5. **Every number matches `round2_data.json`**, is shown with its group size ("12 of 16", "n = 9"), and is never rounded up. While that file says `"status": "projected"`, mark the results as projected on screen.
6. **Children are the users; parents and teachers are the bridge to them.** Show the children's results first. Never say "30 users".
7. **No claim that children learned to read better.** The study measured reactions to the app.
8. **Nothing you do not have the right to use:** no copyrighted music or footage, no real brands, no recognisable real people other than the team's own art, no real child's face.
9. **A cloned voice only with its owner's agreement.**

## What is yours to decide

Everything else. The storyboard's story (a girl named Mia stuck on one sound, Lily arriving, the room changing from a Pixar look into the app's flat look) is one answer. If you see a better opening, a better way to show the change of style, a better way to make thirty people and three ratings feel like something, do that. Choose the tools, the pacing inside each section, the music, the transitions, the voice.

Two things the team cares about, in their words: the move from the problem into the solution should be **smooth, like a morph or a fade, not a cut**, and the demo should feel **animated in PlayIT's own style**, not like a plain screen capture.

## How to work

1. Before making anything, write a one-page plan in `video_freeform/PLAN.md`: your concept in three sentences, a shot list with times, which tools you will use for what, and anything you need from the user. Show it and wait for a yes.
2. Make the first 40 seconds (problem and solution) and show them before going further. The style is decided there.
3. Finish the rest, then check your own work against the nine points above, one by one, and write the result in `video_freeform/CHECK.md`. Say plainly which points you could not verify.
4. Measure the final length and state it. If you cannot measure it, say so.
5. Deliver `video_freeform/<TeamCode>_2-Minute_Pitch_freeform.mp4`.

## Stop and ask the user when

- Real app footage is missing. Do not fill the gap with anything generated.
- A number is missing, or the figures are still projected and a final version is requested.
- You cannot produce a finished MP4 with the tools you have. Say what you can deliver instead (for example scenes to join, or a shot-by-shot package) rather than claiming a result you did not make.
