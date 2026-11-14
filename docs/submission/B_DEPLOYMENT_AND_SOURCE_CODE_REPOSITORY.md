# B. Deployment and Source-Code Repository Guide

**Project:** PlayIT — Offline Synthetic Phonics Mobile Application  
**Course:** IT411 — Capstone & Research 2 / Software Engineering 2  
**Team Code:** G56 (Group 56)  
**Team Lead:** Zendrix Riva (`rivazendrix@gmail.com`)  
**Collaborator:** Erik Samuel Bien (`thatguyKire`)  
**Adviser:** Mr. Joemarie C. Amparo  
**Release Branch:** `erik/research-paper-final`

---

## 1. Public Source-Code Repository

The complete source code, asset pipelines, testing suites, and documentation are hosted on GitHub:

* **Repository URL**: [https://github.com/zendrix-hub/playIt](https://github.com/zendrix-hub/playIt)
* **Access Visibility**: Public Academic Repository (Collaborator: Erik Samuel Bien, `thatguyKire`)
* **Active Working Branches**:
  * `erik/research-paper-final` &mdash; **Official Final Submission Branch** (contains full ACM research paper handoff, updated $N=30$ validation synthesis, and verified test suites).
  * `main` &mdash; Production release branch.
  * `erik/film-fixes` &mdash; Final presentation video and poster kit build.

### Clone and Checkout Commands
```bash
git clone https://github.com/zendrix-hub/playIt.git
cd playIt
git checkout erik/research-paper-final
```

---

## 2. Deployed System Artifacts (Android Packages)

The application is deployed as standalone Android Package Kits (APKs), requiring no backend cloud infrastructure or server provisioning:

| Artifact Name | Version / Commit | File Size | Description & Verification |
|---|---|---|---|
| **`playit-debug-design-9ce23e2.apk`** | Card 34 / `9ce23e2` | ~119.1 MB | **Final Release Build**: Full visual depth refresh, updated Lily mascot animations, finalized Marungko sound assets, and refined touch targets. |
| **`playit-debug-H-0320385.apk`** | Card 31 / `0320385` | ~108.3 MB | **Core Acceptance Build**: Baseline build evaluated during the extensive automated STD testing run (433+ test cases). |

*Both APK files are archived in Google Drive under `G56/01_Software_and_Deployment/04_Deployment_Evidence/`.*

---

## 3. Installation, Environment & Hardware Requirements

### 3.1 Target Device Specifications
* **Minimum Android OS**: Android 8.0 (API Level 26 - Oreo)
* **Target Android OS**: Android 14.0 (API Level 34)
* **Tested Hardware Form Factors**:
  * Samsung Galaxy A21s (Primary physical test device; 720 &times; 1600 px)
  * Standard 360 &times; 640 dp entry-level phone profile
  * Standard 411 &times; 891 dp modern phone profile
  * 800 &times; 1280 dp tablet profile

### 3.2 Installation via Sideloading (Step-by-Step)
1. Download `playit-debug-design-9ce23e2.apk` to the Android device.
2. Open the file via the device File Manager.
3. If prompted, enable "Install unknown apps" for the browser/file manager in Device Settings.
4. Tap **Install** and launch **PlayIT** from the app drawer.

### 3.3 Security, Permissions & Offline Verification
* **Zero Network Permission**: The `AndroidManifest.xml` purposefully omits the `android.permission.INTERNET` manifest tag. The application cannot transmit telemetry, user voice recordings, or personal data off the device.
* **Audio Hardware Access**: Requires only `android.permission.RECORD_AUDIO` to enable local, on-device phoneme pronunciation checks in Step 4 (*Say It*). No audio bytes are ever cached permanently or transmitted externally.

---

## 4. Local Build and Verification Instructions

To build the project from source code:

### 4.1 Prerequisites
* Java Development Kit (JDK) 17 or higher
* Android SDK (API 34, Build Tools 34.0.0)
* Android Studio Iguana / Jellyfish (recommended)

### 4.2 Build Commands (Gradle Wrapper)
```bash
# Assemble Debug APK
./gradlew assembleDebug

# Execute Automated Unit & Integration Tests
./gradlew testDebugUnitTest

# Run Roborazzi Screenshot Verification
./gradlew verifyRoborazziDebug
```
*Expected Build Output: `BUILD SUCCESSFUL` with all unit tests passing.*
