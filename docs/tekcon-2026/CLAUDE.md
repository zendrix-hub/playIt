# PlayIT TekCon 2026 kit (docs/tekcon-2026)

Poster, two-minute pitch film and product logo for the PlayIT capstone (team G56). **Read `CLAUDE_HANDOFF.md` before doing anything here**: it holds the current state, the team lead's decisions, how each piece is built and what has already gone wrong. `film/README.md` covers the film in detail; `AGENTS.md` covers numbers and poster wording.

The aim of work here is to make the pitch better, not only to finish it. Look at the film and poster yourself first and form your own view; section 8 of the hand-off lists the known weak spots, what is fixed, what is open to change, and how to tell whether a change is an improvement.

Rules that always apply in this folder:

- The video is the film in `film/` (a web page animated from one clock; `video/scripts/render_film.mjs` renders it). The slideshow in `video/src/scenes/` and the Antigravity files are old and unused.
- Numbers come only from `round2_data.json`, which is frozen. Show every figure with its group size. Never say "30 users" or claim children learned to read better.
- The phone in the film shows only the real app (the screen recording or real screenshots).
- Clone only the team lead's own voice. Lily uses her real recordings from the app.
- After any change to a voice file: `node film/tools/make_data.mjs`, then `python film/tools/make_gemini_bed.py`, then check stills.
- You cannot hear audio. Say plainly what you could not run, see or hear, and ask a person to listen.
- Look at stills after every picture change. Show the team lead a mock-up or stills before building anything large.
- Commit your own improvements on your own branch as you go. Do not replace what gets submitted (final render, poster PDF, Drive files) until the team lead has seen the change and agreed. Never close someone's PowerPoint; ask them to.
- The repo is public: no personal email addresses and no editable Drive links in files.
