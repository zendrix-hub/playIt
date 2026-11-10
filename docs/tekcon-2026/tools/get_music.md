# Background music

The video is shared by a public link, so use only music you are allowed to reuse. A popular song copied from YouTube can get the video taken down or marked against you.

## Where to get a track that is safe to use

| Source | How to get it | What to record |
|---|---|---|
| YouTube Audio Library | Download from YouTube Studio > Audio Library | Track, artist, and whether attribution is required |
| Pixabay Music | Download button on the track page | Track, artist, page link |
| A YouTube upload marked Creative Commons (CC BY) | yt-dlp, command below | Track, artist, link, licence |

## With yt-dlp

Only for a video whose description states a licence that allows reuse.

```
yt-dlp -x --audio-format mp3 -o "assets/music/track.%(ext)s" "<link to the video>"
```

Run it from the kit folder. Keep one file in `assets/music/`; the build uses the first one it finds.

## Always

Write the credit in `assets/music/CREDITS.md`: title, artist, link, licence, date downloaded. If the licence asks for attribution, put the same line in the video description on Google Drive.

The music plays quietly under the voice (about 12% volume). Pick something light and instrumental with a Filipino flavour (bandurria or rondalla strings, ukulele, light kulintang-style percussion); lyrics fight with narration.
