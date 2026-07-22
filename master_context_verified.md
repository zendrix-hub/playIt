# playIT Repository Status (Verified)

This document replaces the speculative **§7. Repository Status** in the Master Context with verified, code-level findings.

---

## 1. Database State
* **Prepopulated Asset Database (`assets/database/playit_prepopulated.db`):** Verifiably exists but is **0 bytes** (empty). Any attempt to read from it via `createFromAsset` will fail/crash. Seeding must be handled programmatically.
* **Competing Databases:**
  1. `com.playit.app.data.local.PlayItDatabase` (used by Application/Repository): In-memory seed of 5 letters (`m`, `a`, `s`, `i`, `o`).
  2. `com.playit.app.data.database.PlayITDatabase` (unused): Attempted `createFromAsset` configuration but is never invoked.
* **Entities & Schema:** The existing schemas are highly simplified and do not match the required 12 entities (Profiles, Attempts, Streaks, Achievements, etc.) from the SDD ERD.

---

## 2. Voice Recognition (Vosk) State
* **Acoustic Model:** The English small model `final.mdl` is present in `assets/model/am/` (15.9 MB).
* **Dependencies:** `vosk-android:0.3.38` and `jna:5.2.0` are configured in `app/build.gradle.kts`. Note that this is `0.3.38` rather than the recommended `0.3.47` (T-04 will evaluate/upgrade).
* **Code Integration:** `SayItViewModel` handles the copy and loading of this model from assets on first run.

---

## 3. Audio Assets
* **Audio Folder (`assets/audio/`):** Contains 23 audio files:
  - Letters: `a.mp3`, `i.mp3`, `m.mp3`, `o.mp3`, `s.mp3`.
  - Words (Blend/Word audio): `am`, `as`, `is`, `ma`, `mac`, `mass`, `mom`, `sam`, `sis`.
* **Missing Audio:** Audio for letters 6–28 and their corresponding blend words are not present. The system must degrade gracefully (e.g. play a default click/chime or disable missing letters if required).

---

## 4. UI & Flow Gaps
* **Profile Selection / Name Prompt / Letter Complete:** Currently missing. NavGraph directs straight to MapScreen.
* **Map Progression Sequence:** Map sequence is hardcoded to 33 nodes with letters grouped non-standardly (e.g. `o` in group 1, only 5 BLEND checkpoints instead of 7).
* **Find It:** Standard 3-item text card layout instead of the 5-image target-distractor grid with 3 targets required.
* **Blend It:** Audio player with no tile/spelling construction logic.
* **Parent Dashboard & PDF Export:** Completely missing.
