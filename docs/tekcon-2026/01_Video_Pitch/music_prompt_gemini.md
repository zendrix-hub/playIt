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

## Option C: backup styles (if A and B don't feel right)

Each is a full 2-minute version; if Gemini cuts it short, ask for "a 30-second loopable version" of the same prompt and I will loop it.

**C1. Folk village, more Filipino.** Save as `assets/music/backup_folk.wav`.

```
Create an original 2-minute instrumental for a children's educational video set in the Philippines, no vocals. A light Filipino folk feel: rondalla ensemble with bandurria, octavina and guitar, plus soft marimba and a gentle bamboo percussion shaker. Happy, sunny, village-fiesta feeling, but calm enough to sit under a narrator. Write an original melody; do not copy any existing song. Major key, about 96 BPM. Start soft and simple, become brighter after 20 seconds, stay steady and light through the middle, and end on a clear final chord at exactly 2 minutes. No drums, no singing.
```

**C2. Toddler-show bounce, simpler.** Save as `assets/music/backup_bounce.wav`.

```
Create an original 2-minute instrumental for a preschool learning video, no vocals. Cheerful and simple like a toddler learning show: ukulele strumming, xylophone melody, soft hand claps on beats 2 and 4, a light bass. Short, catchy, friendly melody repeated with small changes. Major key, about 110 BPM. Keep it soft and even so a narrator can talk over it the whole time. End neatly on a final chord at exactly 2 minutes. No singing, no humming.
```

**C3. Soft and warm, very calm.** Save as `assets/music/backup_calm.wav`.

```
Create an original 2-minute instrumental for a heartwarming children's education video, no vocals. Soft piano and celesta with gentle plucked ukulele and light pizzicato strings, warm and hopeful, like a storybook coming to life. Major key, about 88 BPM. Sparse, with lots of space for a narrator's voice. Gently lifts after 20 seconds and grows a little fuller in the last 20 seconds, ending on a soft final chord at exactly 2 minutes. No drums, no singing.
```

**C4. Island morning, light tropical.** Save as `assets/music/backup_island.wav`.

```
Create an original 2-minute instrumental for a children's app video set on a sunny tropical island in the Philippines, no vocals. Ukulele, steel-string guitar picking, marimba, soft bongos and a light shaker. Breezy, happy, relaxed, like a bright morning by the sea. Major key, about 100 BPM. Simple repeating melody, mixed soft to sit under a narrator. Ends on a clear final chord at exactly 2 minutes. No heavy drums, no singing.
```

---

## Option D: two short sound moments (nice to have)

These play on top of whichever music we use.

**D1. Lily appears (0:24).** Save as `assets/music/sting_lily.wav`.

```
Create a 3-second original magical sparkle sound for a children's cartoon: a quick rising glockenspiel and chime glissando with a soft shimmer, ending bright and happy. No voice.
```

**D2. Happy ending (1:57).** Save as `assets/music/sting_end.wav`.

```
Create a 3-second original happy ending flourish for a children's cartoon: ukulele strum and xylophone run landing on one bright major chord that rings and fades. No voice.
```

---

## If Gemini will not make music

The same prompts work in other tools. Keep "instrumental" switched on where the tool has that option:

- **Suno** (custom mode, Instrumental on): paste the prompt into the style box, or shorten it to a style line such as `instrumental, children's, playful, ukulele, marimba, glockenspiel, Filipino rondalla, 100 bpm, soft, warm`.
- **Udio**: use the same short style line, Instrumental on, then extend to 2 minutes.
- **Stable Audio** or **Google MusicFX**: paste the short pieces from Option B; they work best with 30 to 45 seconds.

Check each tool's terms allow use in a school project, and note the tool and date in `assets/music/CREDITS.md`.

---

## Tips

- If a result has singing or humming, add "strictly instrumental" and try again.
- If it sounds too busy, add "sparse, fewer notes, more space".
- If it sounds too sleepy, add "a bit more bounce and energy".
- Make two or three versions of each and keep the ones you like best; I can't listen to them, so your ear decides.
- Write down which Gemini model and the date for `assets/music/CREDITS.md`.
