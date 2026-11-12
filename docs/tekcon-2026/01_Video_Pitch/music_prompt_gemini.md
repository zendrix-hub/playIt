# Music for the pitch film: prompts for Gemini

The film runs exactly 2:00 and has a narrator speaking almost all the way through, so the music must stay light, leave room for the voice, and have **no singing**. Copy one of the prompts below into Gemini.

Gemini may only make short clips (about 30 seconds). If so, use the **three short prompts** in Option B; I will join, loop and time them to the film and make the music dip under the voice.

When you have the music, put the files in `assets/music/` and tell me. Keep the names below, and note which Gemini model made them for the credits.

---

## Option A: one full track (if Gemini can make 2 minutes)

```
Create an original instrumental background track for a children's educational app video, exactly 2 minutes long, no vocals, no singing, no humming, no lyrics.

Mood: warm, playful, hopeful, gentle. It should feel like a happy Filipino morning and make young kids smile. Think of a cheerful toddler learning show, but soft enough to sit under a narrator's voice the whole time.

Instruments: ukulele, marimba, glockenspiel, light plucked strings with a Filipino rondalla / bandurria flavour, soft pizzicato strings, a gentle bass, light shaker and soft hand claps. No heavy drums, no electric guitar, no synths that sound harsh.

Key: major (C or G). Tempo: about 100 to 105 BPM.

Structure, follow these times:
- 0:00 to 0:17: very soft and slow, a few gentle plucked notes on ukulele and glockenspiel, slightly wistful, evening feeling, lots of space.
- 0:17 to 0:20: almost silent, one held soft note fading away.
- 0:20 to 0:24: quiet, curious rising notes on glockenspiel, like something small peeking in.
- 0:24: a bright sparkle and a happy lift as the music comes alive: the main playful theme starts with ukulele and marimba.
- 0:28 to 0:40: the main theme, bright and bouncy.
- 0:40 to 1:08: a lighter, steady version of the theme, simple and repetitive, quieter, with space in the middle frequencies for a voice.
- 1:08 to 1:40: steady and warm, a little more confident, still light, no big changes.
- 1:40 to 1:56: fuller and joyful, the theme returns with all instruments, swelling gently.
- 1:56 to 2:00: a short happy ending on one final chord that rings and stops cleanly at 2:00.

Keep the volume even, with no sudden loud hits except the lift at 0:24. Mix it soft and clean, leaving room for a spoken narrator.
```

Save as `assets/music/bed_gemini.wav` (or `.mp3`).

---

## Option B: three short pieces (if Gemini only makes about 30 seconds)

**1. Evening (for 0:00 to 0:20).** Save as `assets/music/part1_evening.wav`.

```
Create an original 25-second instrumental for a children's video, no vocals. Very soft and slow, a few gentle plucked notes on ukulele and glockenspiel with a light Filipino rondalla string colour, slightly wistful, quiet evening feeling, lots of space between notes. Major key, about 70 BPM. It should end by fading away to silence. Mix it soft, to sit under a narrator's voice.
```

**2. Playful theme (for 0:24 to 1:40, I will loop it).** Save as `assets/music/part2_playful.wav`.

```
Create an original 30-second instrumental loop for a children's educational app video, no vocals, no singing. Warm, playful, bouncy and hopeful, like a happy Filipino morning in a cheerful toddler learning show. Ukulele, marimba, glockenspiel, light plucked bandurria / rondalla strings, soft pizzicato, gentle bass, light shaker and soft hand claps. No heavy drums. Major key (C or G), about 100 to 105 BPM. Simple and repetitive so it can loop smoothly: the end must lead straight back into the start. Mix it soft and even, leaving room in the middle frequencies for a spoken narrator.
```

**3. Happy finale (for 1:40 to 2:00).** Save as `assets/music/part3_finale.wav`.

```
Create an original 22-second instrumental finale for a children's educational app video, no vocals. Same style as a playful ukulele and marimba theme with glockenspiel, light Filipino rondalla strings and soft hand claps, about 100 to 105 BPM, major key. It starts warm and builds to a fuller, joyful sound, then ends with a short happy flourish on one final chord that rings briefly and stops cleanly. Mix it soft enough to sit under a narrator's voice.
```

---

## Tips

- If a result has singing or humming, add "strictly instrumental" and try again.
- If it sounds too busy, add "sparse, fewer notes, more space".
- If it sounds too sleepy, add "a bit more bounce and energy".
- Make two or three versions of each and keep the ones you like best; I can't listen to them, so your ear decides.
- Write down which Gemini model and the date for `assets/music/CREDITS.md`.
