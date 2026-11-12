# Music and sound credits

## Gemini-generated music & stings: `assets/music/` (added 12 Nov 2026)

- **`bed_gemini.mp3`** (~121.3s, 44.1 kHz stereo MP3): user-generated in Gemini on 12 Nov 2026 from Option A in `01_Video_Pitch/music_prompt_gemini.md` (original instrumental bed for PlayIT pitch).
- **`sting_lily.mp3`** (~62.4s, 44.1 kHz stereo MP3): user-generated in Gemini on 12 Nov 2026 from Option D1 prompt (needs trimming to ~3s sparkle clip if used at 0:24).
- **`sting_end.mp3`** (~59.7s, 44.1 kHz stereo MP3): user-generated in Gemini on 12 Nov 2026 from Option D2 prompt (needs trimming to ~3s ending flourish if used at 1:57).
- **Note:** Confirm specific Gemini tool/model version if required by your citation format.

## Fallback music bed: `film/music/bed.wav`

- **Source:** made for this video by `tools/voice/make_music.py` on 11 Nov 2026.
- **What it is:** a short pattern of notes built from PlayIT's own opening pluck sound
  (`app/src/main/assets/audio/ui/sfx_opening_pluck.wav`), moved up and down in pitch.
- **Licence:** the team's own material. Nothing to attribute, nothing downloaded.
- **Why not a library track:** the one attempt allowed at a reuse-licensed track was not possible,
  because this network blocks youtube.com and pixabay.com.
- To use a different track, delete `bed.wav`, put one licensed file in this folder, and add its
  title, artist, link, licence and download date here.

## Sound effects: `assets/sfx/`

All copied from the PlayIT app itself (`app/src/main/assets/audio/` on the `main` branch) by
`tools/get_app_sounds.js`: the opening pluck, the correct chime, the unlock chimes, the sparkle,
and Lily's /m/ sound (`ph_m.wav`). They are the team's own material.
