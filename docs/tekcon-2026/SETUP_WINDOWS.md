# One-time setup on the Windows PC that runs Antigravity

Do this once. An agent can run these for you: open this folder in Antigravity and say *"Follow SETUP_WINDOWS.md step by step and report each self-test result."*

Commands are for PowerShell. Open a **new** terminal after installing anything so it is found.

## 1. Antigravity settings

- **Open this kit folder itself** (the one containing `AGENTS.md`), not a parent folder. Antigravity reads `AGENTS.md` and `.agents/` only from the opened folder and its parents.
- **Permission preset: Auto.** Routine commands run without asking; risky ones still pause. Avoid Turbo, which removes every safeguard.
- The project skill `playit-video` in `.agents/skills/` loads on its own when you ask about the video.

## 2. Programs

| Program | Install | Self-test |
|---|---|---|
| Node.js 22 or newer | `winget install --id OpenJS.NodeJS.LTS -e` | `node -v` |
| ffmpeg | `winget install --id Gyan.FFmpeg -e` | `ffmpeg -version` |
| Python 3.11 | `winget install --id Python.Python.3.11 -e` | `python --version` |
| yt-dlp (music only) | `winget install --id yt-dlp.yt-dlp -e` | `yt-dlp --version` |
| Fonts for the poster | Lexend and Sniglet from Google Fonts | Listed in PowerPoint's font menu |

Python 3.11 is deliberate: the voice packages lag behind the newest Python releases.

## 3. Project packages

```
cd tools
npm install
cd ..\video
npm install
cd ..\tools
python -m pip install -r voice\requirements.txt
```

The fonts used in the video are installed by `npm install`, so rendering needs no internet connection afterwards.

Voice cloning is a separate, large install. Do it only when you are ready for the final voice:

```
python -m pip install chatterbox-tts
```

## 4. Remotion's agent skills (optional, recommended)

These teach the agent current Remotion practice. They matter only if you ask for design changes to the video.

```
npx skills add remotion-dev/skills
```

Check that they landed in `.agents/skills/`. If the installer put them elsewhere, move the folders there.

Remotion's own connector (MCP server) was shut down on 26 October 2026; the skills replace it, so do not add it.

## 5. Optional connector

`.agents/mcp_config.json` already lists the Chrome DevTools connector, which lets the agent look at the video preview in a browser. Nothing depends on it. If Antigravity does not pick it up, add it through Customizations > Installed > MCP Servers.

## 6. First run

```
cd tools
npm run build
npm run video:prep
npm run video:stills
```

You should get 24 pictures in `video\out\stills\`, most of them showing dashed placeholders until your media is added.

If the first render fails with a download or timeout error, your network is blocking a browser download. Point the render at the Chrome already on the PC and run again:

```
$env:PITCH_BROWSER = "C:\Program Files\Google\Chrome\Application\chrome.exe"
npm run video:stills
```

## Notes

- **Remotion's licence** is free for individuals and for organisations of up to three people. A student team that is not a company appears to be covered, but the licence has no clause that names students. If in doubt, read https://github.com/remotion-dev/remotion/blob/main/LICENSE.md.
- **HyperFrames is not used.** It can do a similar job, but it is still an early version that changes often, and one renderer is easier for an agent to get right than two.
- **What was tested before this kit reached you:** the video build and a full two-minute render were run on Windows with Chrome. The voice and Whisper scripts were written from each tool's documentation but **could not be run** on the machine where the kit was built (no suitable Python there). Treat the first `npm run video:voice` as the test, and expect that a package name or option may need a small fix.
