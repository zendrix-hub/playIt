# PlayIT Capstone 2 & TekCon 2026 Kit (Branch: erik/research-paper-final)

Welcome to the PlayIT Capstone 2, SoftEng 2, and TekCon 2026 repository (Team G56).

**IMPORTANT: Read the handoff documents before doing anything here**:
1. **`CLAUDE_RESEARCH_PAPER_HANDOFF.md`**: Official guide for Weeks 13 and 14 Capstone Research Paper (ACM format) and final deliverable foldering in Google Drive (`G56/`).
2. **`TEKCON_SUBMISSION_GUIDE.md`**: Official department guidelines for the Research Poster, 2-Minute Video Pitch, and Product Logos.
3. **`CLAUDE_HANDOFF.md`**: Complete pitch video, animation, and mascot development background.

Rules that always apply:
- **Never Modify Existing Source Code/Assets**: Only update and generate the documentation, paper drafts, and submission files.
- **Google Drive Operations**: All file operations must be performed **solely inside the team's `G56` folder** (`1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc`). Never modify the external links (`part9_Validation` or `PlayIT_MIDTERM DELIVERABLE`).
- **Survey Dataset**: Use the frozen $N=30$ survey numbers (`round2_data.json`: 16 learners observed, 5 parents, 9 teachers). Never invent figures and never claim learning gains.
- **Git Branch**: Keep all work committed on `erik/research-paper-final`. Collaborator: Erik Samuel Bien (`thatguyKire`).

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
