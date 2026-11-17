# HANDOFF: PlayIT — PitchPerfect 2026 Morph-Driven Investor Pitch Deck

**Target Executor:** Claude Code (Windows + Microsoft PowerPoint Desktop COM Automation + Visual Verification)  
**Project:** **PlayIT** (IT411 Group 56 — CIT-University, Cebu)  
**Competition:** **PitchPerfect 2026** (MinebeaMitsumi × Wildcat Innovation Labs | TekCon 2026)  
**Submission Deadline:** **November 19, 2026 at 4:00 PM** via `https://forms.gle/DFuc7S4PEkgju26p7`  
**Live Stage Pitch:** **November 25, 2026** (CIT-U Learning Resource & Activity Center) — **Strictly 5:00 (300 seconds)**  
**Git Repository & Branch:** `C:\Users\Zendrix\Desktop\playIT` → branch **`docs/pitchperfect-2026-deck`** (already branched from `main` — stay on this branch!)  
**Primary Output Directory (On Branch):** `C:\Users\Zendrix\Desktop\playIT\docs\tekcon-2026\04_PitchPerfect_2026\` *(also mirror final `.pptx`, `.pdf`, and `.md` script to `c:\Users\Zendrix\Desktop\tekcon-2026\04_PitchPerfect_2026\` at completion)*

---

## 0. Non-Negotiable Ground Rules & Guardrails

1. **Git Branch & Workspace Isolation:**
   - **Verify branch first:** Run `git branch --show-current` in `C:\Users\Zendrix\Desktop\playIT` and confirm you are on **`docs/pitchperfect-2026-deck`**. Never commit to `main`!
   - **ONLY** create/edit files inside `docs/tekcon-2026/04_PitchPerfect_2026/` (and optionally mirror final deliverables to `c:\Users\Zendrix\Desktop\tekcon-2026\04_PitchPerfect_2026\`).
   - **DO NOT** modify `app/`, `docs/submission/`, `docs/std/`, `docs/tekcon-2026/round2_data.json`, `docs/tekcon-2026/content.json`, `docs/tekcon-2026/01_Video_Pitch/`, `docs/tekcon-2026/02_Research_Poster/`, or any files on Google Drive `G56/`.
   - **DO NOT** stage (`git add .`) blindly — keep the 4 existing untracked PDFs in `docs/submission/` untouched; stage only `docs/tekcon-2026/HANDOFF_PITCHPERFECT.md` and `docs/tekcon-2026/04_PitchPerfect_2026/`.
   - **DO NOT** launch interactive background windows or force-kill any existing PowerPoint window the user already has open (`if ($app.Presentations.Count -eq 0) { $app.Quit() }`).
2. **Real Product & Verified Data Only (No Hallucinated Specs):**
   - **Real 4-Step Pedagogical Loop:** **`1. Hear It`** → **`2. Say It`** → **`3. Find It`** → **`4. Blend It`** (followed by Parent Zone). *Do NOT use older draft names (`See & Hear`, `Blend & Tap`, etc.).*
   - **Real Tech Stack (`playIT/README.md`):** Android 8.0+ (API 26), Kotlin `1.9.23` + Jetpack Compose, Clean Architecture + MVVM + Hilt, Room (SQLite) on-device database, **Vosk `0.3.47` offline ASR**, **`no INTERNET permission`**, **`697` JUnit tests (`691` passed, `6` skipped by design, `0` failed)**.
   - **Never claim unmeasured speed metrics** (do **not** claim `"<150ms latency"` — highlight **`0 cloud inference fees`** and **`100% on-device speech check`** instead).
   - **Free Tier Reality:** Chapter 1 (**`M, S, A, I`** — 4 letters, words `SAM`, `SIS`, `AM`) out of 7 Chapters / 26 English letters. (Currently `Parent Zone` + A4 PDF report is built in the APK; interactive Teacher Analytics Dashboard & offline GCash unlock vouchers are **target commercial roadmap items** built on the existing Room + PDF engine.)
   - **Verified Field Validation (`N = 30`, Rounds 1–2, compiled Nov 12, 2026):**
     - **30 participants:** `16` Grade 1 learners + `5` parents/supervising adults + `9` DepEd teachers/reading specialists.
     - **Learners (`n = 16`, observed smileyometer):** **`15 of 16`** chose the happy face after playing, **`15 of 16`** liked the characters, **`14 of 16`** said they would play again. *(Note: children are observed learners, never "respondents"; never say children "learned more" — report engagement/usability/teacher pedagogy scores.)*
     - **Parents (`n = 5`):** **`4.6 / 5`** Grade 1 fit, **`4.4 / 5`** would recommend, **`75.5`** SUS usability score.
     - **Teachers (`n = 5` Round 2 16-item rubric; `9` unique teachers total):** **`4.73 / 5`** overall mean (`4.725`, `15 of 16` items rated `>= 4.0`). Round 1 Teacher Quote (T-1): *“The app is very useful and engaging for Grade 1 learners.”*
3. **Morph Is the Creative Heart of the Entire Deck:**
   - Every transition from Slide 2 through Slide 18 **must** use **PowerPoint Morph (`ppEffectMorphByObject = 3954`)** driven by matching `!!`-prefixed Shape Names.
   - Instead of fading unrelated slides in and out, **the same visual actors (`!!Gold_Hero`, `!!Tile_Blue`, `!!Tile_Amber`, `!!Tile_Green`, `!!Tile_Coral`, `!!Phone_Card`, `!!Lily_Guide`, `!!Bg_Canvas`)** transform physically across every beat—turning letter tiles into problem metrics, into the 4 Phonics Step nodes, into the Bridge to Mia pillars, into Revenue cards, into Competitor columns, into the climbing GTM staircase, into stacked financial bars, and finally into the closing QR card.

---

## 1. Visual System: "PlayIT — Lily's Island" Tactile App Theme

Derived directly from `C:\Users\Zendrix\Desktop\playIT\app\src\main\java\com\playit\app\presentation\theme\Color.kt` and `Type.kt`.

### 1.1 Exact App Color Palette & 3D Tactile Shadow Pairs

Every native interactive card, tile, button, pill, and diagram bar in Slides 3–17 **must look like a physical PlayIT game component**:
- **Face Fill:** Token fill color below (or `#FFFDF8` Shell White for text cards)
- **Crisp Cartoon Outline:** **`Lily Brown` `#4A2E18`** (`2.5pt`–`3pt` solid outline; use **`0.75pt`–`1.5pt`** on tiny badges)
- **Tactile 3D Bottom Lip (No soft fuzzy shadows!):** Hard offset shadow directly downward (`Blur = 0pt`, `Transparency = 0%`, `OffsetX = 0pt`, `OffsetY = 5–7pt`, Angle `90°`) using the exact `*Shadow` companion color below so the shape stays **1 single ungrouped Morphable PowerPoint shape**! *(Verify in PowerPoint PNG render that `Blur = 0` renders a crisp solid Duolingo-style bottom lip; if PowerPoint's `.Shadow` adds unintended antialiasing/margin issues, pair cleanly with an ungrouped matching `!!*_Lip` shadow shape moving identically.)*

| Token Name | Face Hex | 3D Bottom Shadow Lip Hex | Role in Deck |
| :--- | :--- | :--- | :--- |
| **Lagoon Teal (`PrimaryJoy`)** | `#0B7F7A` | `#075C58` (`PrimaryJoyShadow`) | Slide 2/17 dramatic backdrops, primary badges, dark header bars |
| **Sunflower Gold (`SunnyGold`)** | `#FFC53D` | `#DFA019` (`SunnyGoldShadow`) | `!!Gold_Hero`: `Mm` tile, `9 out of 10`, Bridge Beam, Price pills, Winning matrix column, Map pin, KPIs, Closing QR card |
| **Sea Sky (`AquaAdventure`)** | `#3BA7D9` | `#227AA3` (`AquaAdventureShadow`) | `!!Tile_Blue`: Step 1 **Hear It** / UVP 1 / Pillar 1 Parents / TAM / B2C Card / GTM Phase 1 |
| **Apricot Glow (`ApricotGlow`)** | `#F7922F` | `#C86A12` (`ApricotGlowShadow`) | `!!Tile_Amber`: Affordability Wall / Step 2 **Say It** / UVP 2 / Pillar 2 Schools / SAM / B2B Card / GTM Phase 2 |
| **Emerald Leaf (`EmeraldLeaf`)** | `#2E9A3E` | `#1F6E2B` (`EmeraldLeafShadow`) | `!!Tile_Green`: Step 3 **Find It** / UVP 3 / Pillar 3 LGUs & Learners / SOM / B2G Card / GTM Phase 3 |
| **Coral Berry (`CoralBerry`)** | `#F2545B` | `#C2383E` (`CoralBerryShadow`) | `!!Tile_Coral`: Connectivity Wall / Step 4 **Blend It** / UVP 4 / Foundation Footer / SOM Beachhead / CSR Card / GTM Phase 4 |
| **Shell White (`SurfaceCard`)** | `#FFFDF8` | `#EADCC4` (`SurfaceCardShadow`) | `!!Phone_Card`, content cards, table surfaces, quote bubbles |
| **Ground Gradient (`GroundColors`)** | Top `#E1F3FA` | Mid `#F3F5EC` → Bottom `#FFF2DC` | `!!Bg_Canvas` vertical linear gradient on all daylight/app-world slides (`S04`–`S16`) |
| **Text Midnight (`TextMidnight`)** | `#3B2416` | — | Primary dark brown ink on Shell White, Gold, and Sand surfaces (`14:1` contrast) |
| **Text Muted (`TextMuted`)** | `#6E5643` | — | Subtitles, citations, assumptions notes |

### 1.2 Typography & Font Installation / Embedding

Source TTFs are verified in `C:\Users\Zendrix\Desktop\playIT\app\src\main\res\font\`:
- `sniglet_extrabold.ttf`, `sniglet_regular.ttf` — **Hero numbers, playful slide headlines, badges, logo accents** (`Sniglet`, ExtraBold / Regular)
- `lexend_extrabold.ttf`, `lexend_bold.ttf`, `lexend_semibold.ttf`, `lexend_medium.ttf`, `lexend_regular.ttf` — **All card titles, UI pills, comparison cells, numbers, and sub-labels** (`Lexend` — matches the single-story `'a'` and `'g'` app font)
- `andika_regular.ttf` — Optional backup

**CRITICAL MACHINE CHECK:** Currently **`Sniglet` and `Lexend` are NOT installed in Windows Fonts** on this PC.
Before building the `.pptx` in PowerPoint:
1. Install all `.ttf` files from `C:\Users\Zendrix\Desktop\playIT\app\src\main\res\font\` for the current Windows user (`%LOCALAPPDATA%\Microsoft\Windows\Fonts` + `HKCU:\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts`, or Windows Shell `0x14` install), verify via `[System.Drawing.Text.InstalledFontCollection]` that PowerPoint sees **`Sniglet`** and **`Lexend`**.
2. **Embed fonts inside the final `.pptx`** so the pitch deck renders identically on the CIT-U venue computer on November 25 even if `Sniglet` and `Lexend` are absent there:
   - Either enable font embedding in PowerPoint (`embedTrueTypeFonts="1"` / PowerPoint Save Options -> Embed all characters) and verify inside the saved `.pptx` ZIP archive (`ppt/fonts/*.fntdata` entries present and `embedTrueTypeFonts="1"` in `ppt/presentation.xml`), AND also export a full **`PDF` backup**.

---

## 2. Verified Asset Inventory (Exact File Paths & IDs)

Copy all working media cleanly into `04_PitchPerfect_2026\assets\` before building so the `.pptx` build script uses reliable local paths.

### 2.1 Cinematic Story Bookends (from Google Drive Approved Folder `1JvWnREZEXgMcN68CEKZ3yDDunFK6hJW_`)
*(Also cached from earlier download in `C:\Users\Zendrix\.gemini\antigravity\brain\32fa1581-0da3-46db-afd9-328aad4e150e\scratch\storyboard_drive\batch1\`, or download fresh via `gdrive readonly download <ID> --out ...`):*
- `01_Slide01_Bohol_Dusk_Approved.jpg` (Drive ID: `1_KfCg3JtGQDRT-sfp9xYDUsMyNRtPieR`, `698.5 KB`) — **Used on `S01`–`S02`**
- `02_Slide02_Deep_Teal_Literacy_Gap_Approved.jpg` (Drive ID: `1q2d4H8u0AYSPw2eFVCleQ5qOXV-N-qj8`, `508.4 KB`) — **Used on `S03`**
- `06_Slide10_Bohol_Daylight_Approved.jpg` (Drive ID: `1MAoDcNTQWK3rfhApOm8PWKKO1aqqqqm5`, `681.8 KB`) — **Used on `S18`**

> **Note:** Do **not** use full-scene pre-rendered diagrams from `batch1` (`03_*`, `04_*`, `05_*`) or `batch2` (`Batch2_*`) on Slides 3–17. Those sections **must** be built as true PowerPoint native shapes + official 2D app assets so Morph animates cleanly.

### 2.2 Official PlayIT App Visuals (`c:\Users\Zendrix\Desktop\tekcon-2026\assets\`)
- **Lily Mascot Poses (`512x512` transparent PNGs):**
  - `lily_waving.png` — Welcome / Bridge arrival
  - `lily_pointing.png` — Step 1 (*Hear It*) & GTM Phase 1
  - `lily_listening.png` — Step 2 (*Say It*)
  - `lily_encouraging.png` — Step 3 (*Find It*) & GTM Phase 2
  - `lily_celebrating.png` — Step 4 (*Blend It*), Competitive column badge, GTM Summit
  - `lily_thinking.png` / `lily_idle.png` — Supporting/peeking cues
- **Real App Screenshots (`720x1480`, Parent `576x1200`):**
  - `screen_splash.png`, `screen_map.png`, `screen_hearit.png`, `screen_sayit.png`, `screen_sayit_cue.png`, `screen_findit.png`, `screen_blendit.png`, `screen_parent.png`
- **Brand, QR & Team:**
  - `product_logo.png` (`2000x2000`), `product_logo_face.png` (`2000x2000`)
  - `qr_pitch.png` (`1200x1200` — scans to 2-Minute Video Pitch)
  - `team_2d_clear.png` (`2149x1418` — transparent Team G56 portrait)
- **NEW QR Code to Generate (`04_PitchPerfect_2026\assets\qr_app.png`, high contrast `#3B2416` on `#FFFDF8`):**
  - Target URL: `https://drive.google.com/file/d/1pIr2360noHf64TOT-sAwk_uNlk2gSeQa/view?usp=drivesdk` *(Verified public `playit.apk`, `119,339,266` bytes, commit `67fe890`)*.

### 2.3 Authentic In-App Props (`C:\Users\Zendrix\Desktop\playIT\app\src\main\assets\images\`)
- `letters\letter_m.png` — PlayIT's native cartoon letter **`M`** asset (or build as a tactile Gold letter tile matching `screen_blendit.png`!)
- `mouth\mouth_lips_together.png` — Articulation cue shown in `S06` (*Say It*)
- `rewards\reward_star.png`, `rewards\reward_heart.png`, `rewards\reward_confetti_burst.png` — Game badges shown in `S07`, `S08`, `S16`
- `backgrounds\mapprop_crayon_bridge.png` (`512x512`) — Crayon bridge motif on `S10` (**Bridge to Mia**)
- `backgrounds\mapprop_nipa_hut.png`, `mapprop_palm_tree.png`, `mapprop_book_stack.png` (`512x512`) — Subtle island horizon accents on Ground gradient slides (`S04`, `S10`, `S14`–`S16`)

---

## 3. Slide-by-Slide Morph Map (`18 Slides` | `5:00` / `300s`)

**Slide Dimensions:** Widescreen 16:9 (`13.333 in × 7.5 in` = `960 pt × 540 pt`).

### 3.1 Master Persistent `!!` Actor Registry
Every actor below should exist across consecutive slides (placed neatly **off-stage outside `[0..960, 0..540]`** when resting rather than deleted, so Morph glides elements cleanly on and off screen!):

| Actor Name (`Shape.Name`) | What It Plays Across the Deck |
| :--- | :--- |
| `!!Bg_Canvas` | Dusk dark `#0E1013` (`S01–02`) → Deep Teal `#0B7F7A` (`S03`) → Morning Sky-to-Sand `#E1F3FA → #FFF2DC` (`S04–16`) → Deep Teal `#0B7F7A` (`S17`) → Warm Cream/Teal border (`S18`) |
| `!!Bg_Pixar` | Slides `S01–02` (Bohol Dusk full bleed) → `S03` (10 Kids Deep Teal full bleed) → parked off-stage left/faded during `S04–17` → `S18` (Bohol Daylight full bleed) |
| `!!Gold_Hero` | Gold **`Mm`** Tile (`S01–02`) → **`9 out of 10`** Hero Badge (`S03`) → Solution/Chapter 1 Pill (`S04`) → **`Blend It`** golden highlight / **Parent Zone PDF** focus (`S05–09`) → **Sunflower Bridge Beam** (`S10`) → **SOM Target Callout** (`S11`) → Split/Frame or **B2C Price Anchor** (`S12`) → **PlayIT Winning Matrix Column** (`S13`) → **GTM Target Map Pin** (`S14–16`) → **88% Gross Margin / KPI Hero** (`S17`) → **Closing Dual-QR Gold Card** (`S18`) |
| `!!Tile_Blue` | Tucked tab (`S01–02`) → Pre-entry bottom tab (`S03`) → UVP 1 (`100% Offline`) (`S04`) → **Step 1: Hear It** (expands `S05`, compact tab `S06–09`) → **Pillar 1: Parents (`4.6/5` Fit)** (`S10`) → **TAM Horizon Bar (`13.5M / ₱16.2B`)** (`S11`) → **B2C Card (`₱149/mo · ₱999`)** (`S12`) → **Cloud Apps Col** (`S13`) → **GTM Step 1 (`Q1 2027 Cebu Pilot`)** (`S14–16`) → **Year 1/2 Revenue Segment 1** (`S17`) |
| `!!Tile_Amber` | Tucked tab (`S01–02`) → **Affordability Wall** (`S03`) → UVP 2 (`Marungko Order`) (`S04`) → **Step 2: Say It** (compact `S05`, expands `S06`, compact `S07–09`) → **Pillar 2: Schools (`4.73/5` Teachers)** (`S10`) → **SAM Horizon Bar (`2.4M / ₱2.88B`)** (`S11`) → **B2B Card (`₱450/student`)** (`S12`) → **Flashcard Apps Col** (`S13`) → **GTM Step 2 (`Q2 2027 Zero-Data APK`)** (`S14–16`) → **Year 1/2 Revenue Segment 2** (`S17`) |
| `!!Tile_Green` | Tucked tab (`S01–02`) → Pre-entry bottom tab (`S03`) → UVP 3 (`Vosk On-Device AI`) (`S04`) → **Step 3: Find It** (compact `S05–06`, expands `S07`, compact `S08–09`) → **Pillar 3: LGUs/Learners (`15/16` Happy)** (`S10`) → **SOM Horizon Bar (`185K Region VII`)** (`S11`) → **B2G Card (`₱75K/Brgy`)** (`S12`) → **Offline Drills Col** (`S13`) → **GTM Step 3 (`Q3–Q4 2027 20 Schools`)** (`S14–16`) → **Year 1/2 Revenue Segment 3** (`S17`) |
| `!!Tile_Coral` | Tucked tab (`S01–02`) → **Connectivity Wall** (`S03`) → UVP 4 (`Android 8.0+ / No Net`) (`S04`) → **Step 4: Blend It** (compact `S05–07`, expands `S08`, compact `S09`) → **Bridge Ground / Policy Driver** (`S10`) → **Beachhead `₱18M–₱36M` Badge** (`S11`) → **CSR Card (`₱150K/School`)** (`S12`) → **Private Tutors (`₱3K/mo`) Col** (`S13`) → **GTM Step 4 (`2028+ National`)** (`S14–16`) → **Year 1/2 Revenue Segment 4** (`S17`) |
| `!!Phone_Card` | Mia's Dusk Scrim Card (`S01–02`) → Compact Context Card (`S03`) → **Chunky `#4A2E18` Android Phone Frame** holding real `screen_*.png` (`S04–09`) → Market Summary / Quote Card (`S10–11`) → **Shell-White Matrix Board** (`S13`) → Roadmap/KPI Deck Base (`S17`) → **Team G56 Closing Badge** (`S18`) |
| `!!Phone_Screen` | Off-stage hidden (`S01–03`) → Swaps real screenshots inside `!!Phone_Card` via Morph fade/scale (`screen_map` `S04` → `screen_hearit` `S05` → `screen_sayit_cue` `S06` → `screen_findit` `S07` → `screen_blendit` `S08` → `screen_parent` `S09`) |
| `!!Lily_Guide` | Off-stage hidden peeking corner (`S01–03`) → `lily_waving` (`S04`) → `lily_pointing` (`S05`) → `lily_listening` (`S06`) → `lily_encouraging` (`S07`) → `lily_celebrating` (`S08–09`) → `lily_waving` on Bridge (`S10`) → `lily_pointing` beside SOM (`S11`) → `lily_celebrating` on Matrix (`S13`) → **physically climbs Staircase Bars 1 → 2 → 4** (`S14 → S15 → S16`) → `lily_waving` KPIs (`S17`) → off-stage right (`S18` since 3D Lily is inside daylight render) |
| `!!Logo_Badge` | Small watermark top-left (`S01–03`) → Hero `product_logo.png` (`S04`) → Subtle header corner (`S05–17`) → Prominent beside Closing QR (`S18`) |

*(Tip for smooth image swaps on `!!Phone_Screen` and `!!Lily_Guide`: if PowerPoint replaces the bitmap of a `!!`-named picture shape, PowerPoint cross-dissolves + moves/scales cleanly between slides! Let's also set `Transition = ppEffectMorphByObject (3954)` with `Duration = 0.75` for rapid sub-slides and `0.95` for major beat transitions.)*

---

### 3.2 Complete 18-Slide Layout, Copy & Morph Choreography

#### BEAT 1: COMPELLING STORY (`0:00 – 0:30` | 2 Slides)
- **`S01` (`0:00–0:20`) — Meet Mia: The Nightly Reading Wall**
  - **Background:** Full-bleed `01_Slide01_Bohol_Dusk_Approved.jpg` (`!!Bg_Pixar`).
  - **On-Screen Elements:**
    - Top-left dark `#3B2416` (`82%` opacity) Shell-bordered card (`!!Phone_Card`) with **`!!Gold_Hero` (`Mm` tactile gold letter tile, `Sniglet 36pt`)** anchored on its left edge:
      - Title (`Sniglet 32pt White`): **`Meet Mia. Age 6. Grade 1.`**
      - Subtitle (`Lexend 16pt #FFF2DC`): **`Every night, one stuck letter sound stops reading before it starts.`**
    - Along the far right edge, 4 thin `0.25"` tactile colored bookmarks (`!!Tile_Blue`, `!!Tile_Amber`, `!!Tile_Green`, `!!Tile_Coral`) peek in silently.
- **`S02` (`0:20–0:30`, Click `[MORPH #1]`) — The Wall at Home**
  - **Morph Action:** **`!!Gold_Hero` (`Mm` tile)** slides horizontally across Mia's book and tilts `-6°`; **`!!Phone_Card`** shifts down-left and updates headline to: **`No 1-on-1 phonics coaching. Closed book. Tomorrow falls behind.`** While the 4 edge tabs slide slightly further onto the stage.

#### BEAT 2: THE LITERACY GAP (`0:30 – 1:00` | 1 Slide)
- **`S03` (`0:30–1:00`, Click `[MORPH #2]`) — 9 Out of 10: The Offline Gap**
  - **Morph Action:** Dusk fades into Deep Teal `#0B7F7A` (`02_Slide02_Deep_Teal_Literacy_Gap_Approved.jpg` fills screen via `!!Bg_Pixar`, spotlighting the 1 lit child on the right and 9 shadowed children).
    - **`!!Gold_Hero`** rockets from the small `Mm` tile into a massive upper-left **Sunflower Gold tactile hero badge (`9 out of 10`)** (`Sniglet 68pt #3B2416` + `#4A2E18` border + `#DFA019` 3D lip) with crisp Shell White subtitle banner below it: **`Filipino children at age 10 struggle to read simple text (World Bank).`**
    - Two edge bookmarks (`!!Tile_Coral` & `!!Tile_Amber`) shoot outward into **2 stacked tactile Barrier Cards** on the upper-right:
      - **`!!Tile_Coral`:** **`THE CONNECTIVITY WALL`** — *Cloud reading apps fail without steady Wi-Fi.*
      - **`!!Tile_Amber`:** **`THE AFFORDABILITY WALL`** — *₱400–₱600/mo paywalls & foreign accents.*
    - `!!Tile_Blue` & `!!Tile_Green` peek quietly at the bottom-left & bottom-right corners.

#### BEAT 3: SOLUTION REVEAL (`1:00 – 1:25` | 1 Slide)
- **`S04` (`1:00–1:25`, Click `[MORPH #3]`) — Dawn Breaks: Meet PlayIT & Lily**
  - **Morph Action (Showstopper Moment #1):** Deep Teal clears; **`!!Bg_Canvas`** floods the screen with **PlayIT's signature Morning Sky `#E1F3FA` $\rightarrow$ Sand `#FFF2DC` gradient** anchored by subtle bottom-corner `mapprop_nipa_hut.png` and `mapprop_palm_tree.png`.
  - **`!!Phone_Card`** morphs to dead-center as a sleek `#4A2E18` Android device bezel displaying **`screen_map.png`** (*Chapter 1: Chocolate Hills — M, S, A, I*).
  - **`!!Lily_Guide`** (`lily_waving.png`) hops in center-left beside **`!!Logo_Badge` (`product_logo.png` + `PlayIT` in `Sniglet`: `Play` Gold + `IT` Teal)**.
  - **`!!Gold_Hero`** morphs into top pill: **`"AN OFFLINE-FIRST PHONICS COMPANION"`**.
  - All 4 system tiles fan out smoothly around the phone (2 left, 2 right) as tactile PlayIT badges:
    - **`!!Tile_Blue`:** **`100% Offline`** *(No INTERNET permission)*
    - **`!!Tile_Amber`:** **`Listens Out Loud`** *(On-device Vosk AI)*
    - **`!!Tile_Green`:** **`DepEd Marungko`** *(M, S, A, I sequence first)*
    - **`!!Tile_Coral`:** **`Budget Android`** *(Android 8.0+ · Kotlin + Room)*

#### BEAT 4: PRODUCT DEMO — THE 4-STEP PHONICS LOOP (`1:25 – 2:10` | 5 Rapid Morph Sub-Slides)
*(Note: **`!!Phone_Card`** morphs smoothly to **Left-Center (`X = 0.85"`, large & readable)** while the 4 System Steps stack vertically on the **Right (`X = 5.0"` to `12.6"`)** as an interactive expanding accordion. Clicking through `S05 → S08` keeps the speaker's rhythm snappy!)*

- **`S05` (`1:25–1:35`, Click `[MORPH #4]`) — Step 1 Active: `Hear It`**
  - `!!Phone_Screen` = **`screen_hearit.png`** (`Mm — M is for Mouse / Sound: /m/`)
  - `!!Lily_Guide` = **`lily_pointing.png`**
  - **`!!Tile_Blue`** **expands large (`3.0"` tall):** **`1. HEAR IT`** — *Lily models the pure `/m/` phoneme and familiar anchor word (`Mouse`) with child-paced audio replay.*
  - `!!Tile_Amber` (`2. Say It`), `!!Tile_Green` (`3. Find It`), `!!Tile_Coral` (`4. Blend It`) compress into compact `0.7"` tactile step pills stacked underneath.
- **`S06` (`1:35–1:50`, Click `[MORPH #5]`) — Step 2 Active: `Say It` (Vosk Offline AI Spotlight)**
  - `!!Phone_Screen` = **`screen_sayit_cue.png`**
  - `!!Lily_Guide` = **`lily_listening.png`**
  - `!!Tile_Blue` shrinks to compact `0.7"` tab above; **`!!Tile_Amber` expands large:** **`2. SAY IT — OFFLINE VOSK SPEECH AI`** — *Checks child speech 100% on-device. Second miss triggers **`mouth_lips_together.png`** visual cue — **zero hearts lost, zero cloud fees**.* (Place `mouth_lips_together.png` popping right beside `!!Tile_Amber`!)
- **`S07` (`1:50–1:58`, Click `[MORPH #6]`) — Step 3 Active: `Find It`**
  - `!!Phone_Screen` = **`screen_findit.png`** (`Tap 3 pictures starting with /m/`)
  - `!!Lily_Guide` = **`lily_encouraging.png`**
  - **`!!Tile_Green` expands large:** **`3. FIND IT — SOUND-TO-PICTURE MASTERY`** — *Child identifies 3 matching Filipino-familiar words (`Mouse`, `Mom`, `Map`) with 5 playful hearts (`reward_heart.png`).*
- **`S08` (`1:58–2:05`, Click `[MORPH #7]`) — Step 4 Active: `Blend It`**
  - `!!Phone_Screen` = **`screen_blendit.png`** (`Word 1 of 3 — A, M, S tiles`)
  - `!!Lily_Guide` = **`lily_celebrating.png`**
  - **`!!Tile_Coral` expands large:** **`4. BLEND IT — INTERACTIVE CVC SYNTHESIS`** — *Scrambled golden tiles snap into `SAM`, `SIS`, `AM` using Room SQLite offline mastery tracking + 3-star rewards (`reward_star.png`).*
- **`S09` (`2:05–2:10`, Click `[MORPH #8]`) — Parent Zone & Verified Engineering**
  - `!!Phone_Screen` = **`screen_parent.png`** (`How your child is doing — 26 letters`)
  - `!!Gold_Hero` expands to a prominent Shell & Gold feature card on the right: **`PARENT ZONE + BUILT TODAY`** — *Plain-language mastery across all 26 letters, offline A4 PDF report export, **`691 / 691` unit tests passed**, **`no INTERNET permission`**.*

#### BEAT 5: VALIDATION + TARGET MARKET ("THE BRIDGE TO MIA") (`2:10 – 2:55` | 2 Slides)
- **`S10` (`2:10–2:30`, Click `[MORPH #9]`) — Proven Validation & The Bridge to Mia**
  - **Morph Action (Showstopper Moment #2):** The 4 step pills rotate & stand upright on the left/center as **3 Tactile Bridge Pillars** anchored above **`!!Tile_Coral`** (`Foundation Bar`), while **`!!Gold_Hero`** stretches across all 3 pillars into the **Sunflower Gold Bridge Beam: `"THE BRIDGE TO THE CHILD (PAYING CUSTOMERS)"`** leading right to **`!!Lily_Guide` (`lily_waving.png`)** + **`mapprop_crayon_bridge.png`**!
  - **Each Pillar Carries Real Verified `N = 30` Validation Proof:**
    - **`!!Tile_Blue` Pillar — PARENTS (B2C):** **`4.6 / 5`** Grade 1 Fit · **`75.5`** SUS *(n = 5)*
    - **`!!Tile_Amber` Pillar — SCHOOLS & TEACHERS (B2B):** **`4.73 / 5`** Pedagogy *(n = 5 R2; 9 teachers total)* · *“Very useful and engaging for Grade 1 learners.”*
    - **`!!Tile_Green` Pillar — LGUs & CSR + LEARNERS (B2G/Impact):** **`15 / 16`** Grade 1 Learners happy · **`14 / 16`** play again *(n = 16)*
- **`S11` (`2:30–2:55`, Click `[MORPH #10]`) — Market Opportunity: TAM / SAM / SOM**
  - **Morph Action:** The 3 upright Bridge Pillars smoothly tip/stretch sideways (`Left = 0.8"`) into **3 descending Horizontal Opportunity Bars** aligned along the left margin, while **`!!Gold_Hero`** becomes the macro tailwind callout badge (`DepEd NLRP & Catch-Up Fridays`):
    - **`!!Tile_Blue` (Full Bar `11.5"`):** **`TAM — 13.5M Early Learners (PH & SEA)` | `₱16.2B / yr`**
    - **`!!Tile_Amber` (Mid Bar `8.2"`):** **`SAM — 2.4M PH Grade 1 Learners` | `₱2.88B / yr`** *(Prepaid / patchy-Wi-Fi households)*
    - **`!!Tile_Green` + `!!Tile_Coral` (Focused Bar `4.8"`):** **`SOM — 185K Region VII Learners` | `₱18M–₱36M Beachhead`** *(5% Cebu & Visayas share)*

#### BEAT 6: BUSINESS MODEL & REVENUE STRATEGY (`2:55 – 3:30` | 1 Slide)
- **`S12` (`2:55–3:30`, Click `[MORPH #11]`) — Hybrid 4-Stream Revenue Model**
  - **Morph Action:** The 3 horizontal TAM/SAM/SOM bars + Coral beachhead chip **Morph upright into 4 symmetric vertical PlayIT Revenue Cards (`2.8"` wide × `4.4"` tall)** across the stage! **`!!Gold_Hero`** highlights the top **Price / Free-Tier Header Pill**: **`Chapter 1 (M, S, A, I) FREE Forever — Unlock All 7 Chapters Online or Offline`**:
    1. **`!!Tile_Blue` — B2C Family Pass:** **`₱149/mo`** or **`₱999 One-Time Pass`** *(GCash / ShopeePay offline voucher; less than 3 tutoring sessions)*.
    2. **`!!Tile_Amber` — B2B Private Schools:** **`₱450 / student / yr`** *(Annual school license + upcoming Teacher Analytics Dashboard)*.
    3. **`!!Tile_Green` — B2G LGU Kits:** **`₱75,000 / Barangay Kit`** *(Funded via Local School Board Special Education Fund — SEF)*.
    4. **`!!Tile_Coral` — Corporate CSR Sponsors:** **`₱150,000 / Adopted School`** *(Turnkey offline tablet/phone kits + co-branded impact report)*.

#### BEAT 7: COMPETITIVE ADVANTAGE (`3:30 – 4:00` | 1 Slide)
- **`S13` (`3:30–4:00`, Click `[MORPH #12]`) — Why PlayIT Wins: Offline Speech AI Matrix**
  - **Morph Action:** The 4 vertical cards condense into top column headers while **`!!Phone_Card`** expands into a clean Shell White (`#FFFDF8`) comparison board (`5 rows × 5 columns` or crisp native cards aligned in a matrix) and **`!!Gold_Hero`** slides right into the illuminated **Winner Column (`PlayIT`)** topped with **`lily_celebrating.png`**!
  - **Columns:** Feature | Cloud Global Apps (`!!Tile_Blue`) | Flashcard Apps (`!!Tile_Amber`) | Private Tutors (`!!Tile_Coral`) | **`PlayIT` (`!!Gold_Hero`, `₱149/mo · ₱999`)**
  - **Rows (Clean Check `✓` vs `✕`):**
    1. **Works 100% Offline** (`✕` | `✓` | N/A | `✓ Zero Net Permission`)
    2. **Evaluates Child Speech Out Loud** (`✓ Cloud` | `✕` | `✓` | `✓ Offline Vosk AI`)
    3. **DepEd Marungko + Filipino Vocab** (`✕` | `Partial` | `Varies` | `✓ 4.73/5 Teacher Score`)
    4. **Parent / Class Record PDF** (`✕` | `✕` | `Manual` | `✓ Built-In Offline PDF`)
    5. **Cloud AI Cost per User** (`High GPU Cost` | `₱0` | `₱3,000+/mo` | `✓ ₱0.00 / Learner`)

#### BEAT 8: GO-TO-MARKET STRATEGY — BOHOL/CEBU QUEST STAIRCASE (`4:00 – 4:30` | 3 Rapid Morph Sub-Slides)
- **`S14` (`4:00–4:10`, Click `[MORPH #13]`) — Phase 1: Classroom Door Launch (`Q1 2027`)**
  - **Morph Action (Showstopper Moment #3):** The 4 matrix header tags drop into the **4 Ascending Tactile GTM Staircase Bars** (`Blue 25%`, `Amber 45%`, `Green 65%`, `Coral 85%`), with **`!!Lily_Guide` (`lily_pointing.png`) standing on Bar 1 (`Blue`)** and **`!!Gold_Hero`** floating above Bar 4 as the **Destination Pin**!
  - **Spotlight Callout Card (`!!Phone_Card`):** **`Q1 2027 · CEBU 5-SCHOOL PILOT (300 LEARNERS)`** — *6-week longitudinal fluency study with partner Cebu elementary schools.*
- **`S15` (`4:10–4:20`, Click `[MORPH #14]`) — Phase 2: Zero-Data Parent Viral Loop (`Q2 2027`)**
  - **Morph Action:** **`!!Lily_Guide` (`lily_encouraging.png`) arcs up onto Bar 2 (`Amber`)!**
  - **Spotlight Callout Card:** **`Q2 2027 · BRIGADA ESKWELA & PTA NEARBY SHARE`** — *Teachers beam `playit.apk` phone-to-phone offline; parents install with **₱0 mobile data** (`₱0` paid ad spend).*
- **`S16` (`4:20–4:30`, Click `[MORPH #15]`) — Phases 3–4: Regional to National Scale (`Q3 2027 – 2028+`)**
  - **Morph Action:** **`!!Lily_Guide` (`lily_celebrating.png`) leaps to the top between Bar 3 (`Green`) and Bar 4 (`Coral`)** while **`!!Gold_Hero`** snaps down onto Bar 4 beside `reward_confetti_burst.png`!
  - **Spotlight Callout Card:** **`Q3 2027 → 2028+ · 20 PRIVATE SCHOOLS + 3 CSR PARTNERS → LGU SEF & BISAYA/TAGALOG`**

#### BEAT 9: FINANCIAL SUSTAINABILITY & UNIT ECONOMICS (`4:30 – 4:45` | 1 Slide)
- **`S17` (`4:30–4:45`, Click `[MORPH #16]`) — Zero Cloud Burn, Rapid Profitability**
  - **Morph Action:** **`!!Bg_Canvas`** shifts back to **Deep Teal `#0B7F7A`** for clean dark-backdrop investor punch!
  - The 4 vertical Staircase Bars (`!!Tile_Blue`, `!!Tile_Amber`, `!!Tile_Green`, `!!Tile_Coral`) rotate $90^\circ$ and snap horizontally side-by-side into **Two Clean Stacked Revenue Bars** across the bottom half:
    - **Year 1 Target (`₱1.45M`):** proportional 4-color stacked segments (B2C + B2B + B2G + CSR)
    - **Year 2 Target (`₱5.80M`):** `4×` longer proportional 4-color stacked segments
  - Above them, **`!!Gold_Hero` + `!!Phone_Card` + `!!Lily_Guide` (`lily_waving.png`)** form **3 Tactile Hero KPI Tiles** (`#FFFDF8` face + `#FFC53D` Gold header/border + `#DFA019` 3D lip):
    1. **`88% Gross Margin`** — *Verified `₱0.00` cloud ASR inference cost*
    2. **`4.8× LTV : CAC`** — *School-led distribution projects `₱245` CAC*
    3. **`Month 9 Break-Even`** — *Target profitability milestone (Year 1–2 projections)*

#### BEAT 10: CLOSING VISION & ACTION ASK (`4:45 – 5:00` | 1 Slide)
- **`S18` (`4:45–5:00`, Click `[MORPH #17]`) — Dusk to Daylight: Every Child Reading**
  - **Morph Action (Final Emotional Payoff):** Deep Teal sweeps away into full-bleed **`06_Slide10_Bohol_Daylight_Approved.jpg`** (`!!Bg_Pixar` — sunlit Bohol table with Mia reading happily beside her smiling mother and Lily!).
  - **`!!Gold_Hero`** & **`!!Phone_Card`** Morph into a clean, high-contrast **Right-Side Sunflower Gold & Shell White Closing Card** (covering only the right `36%` of the frame so Mia, her mother, and Lily stay unobstructed on the left/center!):
    - Top: **`!!Logo_Badge`** + **Tagline (`Sniglet 22pt #3B2416`):** **`One sound at a time.`**
    - Middle: **Dual Tactile QR Tiles:**
      - Left QR: **`qr_app.png`** — **`Try Live APK (playit.apk)`** *(Android 8.0+, 100% offline)*
      - Right QR: **`qr_pitch.png`** — **`Watch 2-Min Demo Pitch`**
    - Bottom Team Strip: Small circular/rounded **`team_2d_clear.png`** + **`Group 56 · CIT-University CCS`** *(Zendrix Riva, Erik Samuel Bien, Ashlee Skye Durano, Kaysean Miel, John James Palis · Adviser: Mr. Joemarie C. Amparo)*

---

## 4. Word-for-Word 5-Minute Stage Script (`~645 Words` = `~135 WPM` | `4:45–4:55`)

Write each slide's exact script block + `[MORPH CLICK]` cue directly into that slide's **PowerPoint Speaker Notes (`NotesPage`)** AND save a printable stage cue sheet as `04_PitchPerfect_2026\G56_PlayIT_5Min_Pitch_Script.md`.

* **`S01` (`0:00–0:20`, ~45w):** "Meet Mia. She's six years old, in Grade 1 right here in the Visayas. Every night after dinner, she opens her reading book and gets stuck on a single letter sound. Her mother works ten-hour days and wants to help, `[CLICK S02]`"
* **`S02` (`0:20–0:30`, ~25w):** "but has neither the energy nor phonics training to guide her. When Mia closes that book tonight, she falls another day behind. `[CLICK S03]`"
* **`S03` (`0:30–1:00`, ~65w):** "Mia isn't alone. According to the World Bank, 9 out of 10 Filipino children at age ten still struggle to read simple text. Why haven't global EdTech apps fixed this? Two walls: the Connectivity Wall—cloud apps die on patchy prepaid data—and the Affordability Wall—four to six hundred peso monthly paywalls built around foreign accents. `[CLICK S04]`"
* **`S04` (`1:00–1:25`, ~55w):** "That's why we built PlayIT—an offline-first Android phonics companion guided by Lily the Tarsier. Built in Kotlin and Jetpack Compose with zero internet permission, PlayIT turns any entry-level Android 8 phone parents already own into a patient phonics tutor following DepEd's exact Marungko sequence—M, S, A, I first. `[CLICK S05]`"
* **`S05` (`1:25–1:35`, ~25w):** "Here's how Lily guides Mia in four proven steps. One—Hear It: Lily models the pure phoneme `/m/` and anchor word, *Mouse*. `[CLICK S06]`"
* **`S06` (`1:35–1:50`, ~38w):** "Two—Say It: our embedded offline Vosk speech recognizer listens right on the phone and checks her pronunciation with zero cloud fees. Miss twice? Lily shows exact mouth-shape cues instead of scolding—never costing a heart. `[CLICK S07]`"
* **`S07` (`1:50–1:58`, ~20w):** "Three—Find It: Mia taps three Filipino-familiar pictures starting with `/m/`—Mouse, Mom, and Map. `[CLICK S08]`"
* **`S08` (`1:58–2:05`, ~22w):** "And four—Blend It: scrambled tiles snap together into real words like SAM, stored locally in Room SQLite. `[CLICK S09]`"
* **`S09` (`2:05–2:10`, ~25w):** "Plus a Parent Zone tracking all 26 letters with one-tap A4 PDF reports—verified by 691 passing automated tests. `[CLICK S10]`"
* **`S10` (`2:10–2:30`, ~60w):** "In our field evaluation across 30 participants, 15 of 16 Grade 1 learners loved playing and 14 would play again. Parents and teachers are the bridge to Mia: parents rated Grade 1 fit 4.6 out of 5 with a 75.5 usability score, and 9 DepEd teachers gave us 4.73 out of 5 on pedagogy. `[CLICK S11]`"
* **`S11` (`2:30–2:55`, ~55w):** "With 13.5 million early learners across the Philippines and Southeast Asia, that bridge unlocks a ₱16.2 billion Total Addressable Market, and ₱2.88 billion across 2.4 million Filipino Grade 1 learners alone. Focusing just five percent here in Central Visayas gives us an immediate ₱18 to ₱36 million Cebu beachhead. `[CLICK S12]`"
* **`S12` (`2:55–3:30`, ~65w):** "We monetize through four streams while keeping Chapter 1—M, S, A, I—free forever. Parents unlock all seven chapters for ₱149 a month or a ₱999 pass via GCash—cheaper than three tutoring hours. Private schools license at ₱450 per student yearly, while LGU Barangay Kits at ₱75,000 and ₱150,000 Corporate CSR school adoptions deliver high-margin bulk impact. `[CLICK S13]`"
* **`S13` (`3:30–4:00`, ~55w):** "Why do we win? Cloud apps charge ₱500 and break without Wi-Fi. Offline flashcard apps can't hear whether a child spoke right. Tutors cost ₱3,000 a month. PlayIT is the only Philippine solution uniting 100% offline speech AI, DepEd Marungko alignment, and zero cloud inference cost. `[CLICK S14]`"
* **`S14` (`4:00–4:10`, ~22w):** "We go to market through the classroom door. Q1 2027: a five-school, 300-student pilot right here in Cebu. `[CLICK S15]`"
* **`S15` (`4:10–4:20`, ~25w):** "Q2: during Brigada Eskwela and PTA nights, teachers beam PlayIT directly phone-to-phone—onboarding families at ₱0 customer data cost. `[CLICK S16]`"
* **`S16` (`4:20–4:30`, ~22w):** "From there, we scale to 20 Visayas schools and three CSR partners, then expand nationally with Bisaya and Tagalog modules. `[CLICK S17]`"
* **`S17` (`4:30–4:45`, ~40w):** "Because AI inference runs on the phone, our projected unit economics hit an 88% gross margin, 4.8x LTV-to-CAC at ₱245 CAC, Month 9 break-even, and ₱1.45 million projected Year 1 revenue. `[CLICK S18]`"
* **`S18` (`4:45–5:00`, ~35w):** "With PlayIT, that dim table where Mia got stuck becomes where she finds her voice. Working Android APK in hand, built in Cebu, tested with real classrooms—join us, one sound at a time. Thank you!"

*(Total: ~636 words — leaves ~15 seconds for click pacing, applause, and breath before the 5:00 bell!)*

---

## 5. Technical Build Instructions for Claude Code (PowerPoint Desktop COM)

### 5.1 Step-by-Step Build Pipeline

1. **Workspace & Media Staging:**
   - Create `C:\Users\Zendrix\Desktop\playIT\docs\tekcon-2026\04_PitchPerfect_2026\assets\` and `previews\`.
   - Copy all required `assets/` PNGs from `C:\Users\Zendrix\Desktop\playIT\docs\tekcon-2026\assets\` and `C:\Users\Zendrix\Desktop\playIT\app\src\main\assets\images\` (`letters\letter_m.png`, `mouth\mouth_lips_together.png`, `rewards\reward_*.png`, `backgrounds\mapprop_*.png`).
   - Download (or copy from scratch) the 3 Drive Bookend JPGs (`01_Slide01_Bohol_Dusk_Approved.jpg`, `02_Slide02_Deep_Teal_Literacy_Gap_Approved.jpg`, `06_Slide10_Bohol_Daylight_Approved.jpg`).
   - Generate **`docs\tekcon-2026\04_PitchPerfect_2026\assets\qr_app.png`** (`1200x1200` high-contrast `#3B2416` on `#FFFDF8`) encoding `https://drive.google.com/file/d/1pIr2360noHf64TOT-sAwk_uNlk2gSeQa/view?usp=drivesdk`. *(Use Node/Python QR generator cleanly.)*

2. **Install Fonts (`Sniglet` & `Lexend`) for Windows Session:**
   - Copy all 8 `.ttf` files from `C:\Users\Zendrix\Desktop\playIT\app\src\main\res\font\` into `%LOCALAPPDATA%\Microsoft\Windows\Fonts` and register in `HKCU:\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts` (or use Windows Shell `0x14` font folder install) so **PowerPoint Desktop renders exact glyph metrics**.

3. **Build via PowerShell PowerPoint COM (`New-Object -ComObject PowerPoint.Application`):**
   - Verified PowerPoint Office 16 path on this machine: `C:\Program Files\Microsoft Office\Root\Office16\POWERPNT.EXE`.
   - Verified `MSPPT.OLB` constants:
     - `ppEffectMorphByObject = 3954`
     - `ppSaveAsOpenXMLPresentation = 24`
     - `ppSaveAsPDF = 32`
     - `ppSaveAsMP4 = 39`
   - Set Widescreen 16:9 explicitly:
     ```powershell
     $pres.PageSetup.SlideSize = 16 # or custom:
     $pres.PageSetup.SlideWidth = 960   # 13.333 in
     $pres.PageSetup.SlideHeight = 540  # 7.5 in
     ```
   - Build Slides `1..18` cleanly in code so layout math is exact (`0` overlapping shapes, generous inner padding, clean alignment grids).
   - Assign **exact `!!`-prefixed `Shape.Name`** to every morphing element (`!!Bg_Canvas`, `!!Bg_Pixar`, `!!Gold_Hero`, `!!Tile_Blue`, `!!Tile_Amber`, `!!Tile_Green`, `!!Tile_Coral`, `!!Phone_Card`, `!!Phone_Screen`, `!!Lily_Guide`, `!!Logo_Badge`), keeping every `!!` object **ungrouped**!
   - For Slides `2..18`, apply **Morph (`EntryEffect = 3954`)**:
     ```powershell
     $slide.SlideShowTransition.EntryEffect = 3954
     $slide.SlideShowTransition.Duration = 0.85   # 0.70 for quick micro-morph steps
     $slide.SlideShowTransition.AdvanceOnClick = -1 # msoTrue
     ```
   - Write each slide's speaker script + `[CLICK Sxx]` cue into `$slide.NotesPage.Shapes.Placeholders(2).TextFrame.TextRange.Text`.

4. **Font Embedding Inside `.pptx`:**
   - Save first via PowerPoint (`$pres.SaveAs(..., 24)`), OR configure PowerPoint Desktop save embedding / verify `ppt/presentation.xml` has `embedTrueTypeFonts="1"` and `ppt/fonts/*.fntdata` entries inside the `.pptx` ZIP before final export. *(If PowerPoint COM doesn't expose a setter directly on `_Presentation`, toggle via Office Save Options in Registry `HKCU\Software\Microsoft\Office\16.0\PowerPoint\Options\EmbedTrueTypeFonts` = `1` **before** opening/saving in PowerPoint, then test-unzip `.pptx` to verify `ppt/fonts/` exists!)*

5. **Mandatory Multi-Modal Verification Before Finishing:**
   - **Export all 18 Slide PNGs:** `$slide.Export("previews\slide_01.png", "PNG", 1920, 1080)` for all 18 slides.
   - **Visually inspect EVERY single PNG (`slide_01.png` .. `slide_18.png`)** using image viewing to verify:
     - Zero text clipping, overflow, or colliding text boxes
     - Proper `Sniglet` & `Lexend` font rendering (NOT Arial/Calibri fallback!)
     - Crisp PlayIT `#4A2E18` borders & 3D shadow lips
     - Mia & Lily never obscured on `S01`, `S02`, or `S18`
     - Clean contrast on Deep Teal (`S03`, `S17`) and Morning Sky-to-Sand (`S04`–`S16`)
   - **Export PDF + Verification MP4:**
     - `$pres.SaveAs("04_PitchPerfect_2026\G56_PlayIT_PitchPerfect_2026_Deck.pdf", 32)`
     - Set a short rehearsal preview copy (`AdvanceOnTime = -1`, `AdvanceTime = 1.5`) in a temp presentation or export `$pres.SaveAs("04_PitchPerfect_2026\previews\morph_test.mp4", 39)` and extract intermediate transition frames with `ffmpeg` (check if installed) or inspect to confirm **Morph smoothly interpolates** `!!Gold_Hero`, `!!Tile_*`, `!!Phone_Card`, and `!!Lily_Guide` rather than hard-cutting! *(Keep AdvanceOnClick = True, AdvanceOnTime = False in the real deliverable `.pptx`!)*

6. **Deliverables Checklist inside `C:\Users\Zendrix\Desktop\playIT\docs\tekcon-2026\04_PitchPerfect_2026\` (and final `.pptx`/`.pdf`/`.md` mirrored to `c:\Users\Zendrix\Desktop\tekcon-2026\04_PitchPerfect_2026\`):**
   - `G56_PlayIT_PitchPerfect_2026_Deck.pptx` (18 Morph slides, embedded fonts, speaker notes)
   - `G56_PlayIT_PitchPerfect_2026_Deck.pdf` (Static judge submission / venue backup)
   - `G56_PlayIT_5Min_Pitch_Script.md` (Timed script + `[CLICK]` cues + Q&A Defense Cheat Sheet covering ElevenLabs non-commercial clips swap-out plan before launch, offline GCash unlock voucher architecture, B2G LGU SEF procurement, and `₱0` cloud speech economics)
   - `build_pitchperfect_com.ps1` (Reproducible builder script)
   - `previews/slide_01.png` .. `slide_18.png` (+ optional `previews/morph_test.mp4`)
   - Commit deliverables cleanly to branch **`docs/pitchperfect-2026-deck`** (`git add docs/tekcon-2026/04_PitchPerfect_2026/`).

---

## 6. Investor Q&A Defense Notes (Include in `G56_PlayIT_5Min_Pitch_Script.md`)

Prepare Team G56 for tough questions from **MinebeaMitsumi** and **Wildcat Innovation Labs** judges:
1. **Commercial IP / Audio Licensing Question:** Disclose transparently if asked that 4 words (`AM`, `TUB`, `YAM`, `/i/`) in the academic capstone APK used ElevenLabs Free Tier (non-commercial) and **will be swapped to Apache-2.0 `Kokoro-82M` synthetic voices before the Q1 2027 commercial pilot**.
2. **Offline Unlock Anti-Piracy Question:** B2C `₱149/mo` or `₱999` unlock uses signed offline activation codes bound to device ID / local Room state, redeemed via SMS/GCash merchant flow without requiring live cloud streaming after activation.
3. **Hardware/Storage Compatibility Question:** Current `playit.apk` is `~114 MB` (including the bundled `vosk-model`), requires Android `8.0+` (API 26) and zero mobile data once installed via Brigada Eskwela Nearby Share / APK sideload.
4. **Future Scope vs Built Today:** Clearly distinguish **Built & Tested Today** (`Hear It / Say It / Find It / Blend It`, 26 letters / 7 chapters, offline Vosk, Room SQLite, Parent Zone A4 PDF, `N = 30` validation, `691` unit tests) from **Commercial Roadmap** (B2B Teacher Multi-Student Sync Portal, offline voucher gateway, Bisaya/Tagalog modules).
