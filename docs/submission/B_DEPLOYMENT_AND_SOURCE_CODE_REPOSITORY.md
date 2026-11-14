# B. Deployment and Source-Code Repository Guide

**Project:** PlayIT, an offline phonics app for Grade 1 learners  
**Course:** IT411 Capstone & Research 2 / Software Engineering 2  
**Team Code:** G56 (Group 56)  
**Team Lead:** Zendrix Riva  
**Collaborator:** Erik Samuel Bien (GitHub `thatguyKire`)  
**Adviser:** Mr. Joemarie C. Amparo  
**Branch:** `erik/research-paper-final`

---

## 1. Source-Code Repository

- **Repository:** https://github.com/zendrix-hub/playIt (public)
- **Branches:**
  - `erik/research-paper-final`: research paper hand-off and the final submission documents.
  - `main`: the app's code and its documents.
  - `docs/tekcon-2026-pitch-kit` and `erik/film-fixes`: the TekCon poster, pitch film and logo.

```bash
git clone https://github.com/zendrix-hub/playIt.git
cd playIt
git checkout erik/research-paper-final
```

---

## 2. Deployed Builds (Android packages)

PlayIT is installed from an APK file. It needs no server.

| File | Commit | Size | What it is |
|---|---|---|---|
| `playit-debug-H-0320385.apk` | `0320385` (card 31) | 108,317,310 bytes (about 108.3 MB) | **APK H, the midterm build.** The build tested in STD v1.0: 433 tests, 427 passed, 6 skipped by design, 0 failed. SHA-256 and `aapt` inspection: `docs/std/evidence/2026-10-18-submitted-apk/apk_inspection.txt`. |
| `playit-debug-design-9ce23e2.apk` | `9ce23e2` | Not recorded in the repository | A later build with the visual refresh (`docs/design/visual-refresh.md`). Its size and test record are not in the repository; add them from the file itself before citing them. |

The team lead keeps both files in the team's Google Drive folder.

---

## 3. Installing and Running

### 3.1 Device
- **Minimum:** Android 8.0 (API 26). **Target:** Android 14 (API 34).
- **Manual tests:** Samsung Galaxy A21s, the team's test phone (STD 5.5).
- **Automated layout checks:** four screen profiles: 360 x 640 dp (compact), 360 x 740 dp (A21s), 411 x 891 dp (phone), 800 x 1280 dp (tablet).

### 3.2 Installing the APK
1. Copy the APK to the phone and open it from the file manager.
2. If asked, allow "Install unknown apps" for the file manager.
3. Tap **Install**, then open **PlayIT**.

No account and no internet connection are needed. On "Who is playing?", tap **Add Profile**, pick an animal friend and tap **Let's Play**.

### 3.3 Permissions (from `AndroidManifest.xml` and the APK inspection)
- **No `INTERNET` permission.** The app cannot send anything off the device.
- `RECORD_AUDIO`: for Say It. The speech recognizer runs on the phone.
- `READ_EXTERNAL_STORAGE` (Android 12L and lower only) and `WRITE_EXTERNAL_STORAGE` (Android 9 and lower only): declared for older Android versions.

---

## 4. Building From Source

### 4.1 Requirements
- JDK 17
- Android SDK with API 34 and Build Tools 34.0.0
- Gradle 8.9 comes with the wrapper; Android Gradle Plugin 8.4.0, Kotlin 1.9.23

Point Gradle at the SDK with `ANDROID_HOME`, or a `local.properties` file holding `sdk.dir=...` (use forward slashes on Windows).

### 4.2 Commands
```bash
./gradlew assembleDebug        # app/build/outputs/apk/debug/app-debug.apk
./gradlew testDebugUnitTest    # 697 tests: 691 passed, 6 skipped by design, 0 failed
```

Checked on 14 November 2026 on a second Windows PC (OpenJDK 17.0.16): `BUILD SUCCESSFUL`, 697 tests, 691 passed, 6 skipped, 0 failed. The six skipped tests are the font-scale 1.3 checks on the 360 x 640 profile. Results are written to `app/build/test-results/testDebugUnitTest/`.

A build from this repository is not APK H: 387 files under `app/` changed after that build.
