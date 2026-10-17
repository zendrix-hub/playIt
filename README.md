# PlayIT — Offline English Phonics Learning Companion

<p align="center">
  <strong>An Offline-First Android Educational Platform for Grade 1 Filipino Learners (Ages 6–7) Powered by the Marungko Approach, Real-Time Edge Speech Recognition, and Pediatric Interaction Design.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android_8.0%2B_(API_26%2B)-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform: Android"/>
  <img src="https://img.shields.io/badge/Language-Kotlin_1.9.23-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Language: Kotlin"/>
  <img src="https://img.shields.io/badge/UI-Jetpack_Compose_Material_3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="UI: Jetpack Compose"/>
  <img src="https://img.shields.io/badge/ASR-Vosk_Offline_0.3.47-FF6F00?style=for-the-badge" alt="ASR: Vosk"/>
  <img src="https://img.shields.io/badge/Architecture-Clean_%2B_MVVM-009688?style=for-the-badge" alt="Architecture: Clean + MVVM"/>
  <img src="https://img.shields.io/badge/Unit_Tests-433%20Passing%20(100%25)-brightgreen?style=for-the-badge" alt="Unit Tests: 433 Passing"/>
  <img src="https://img.shields.io/badge/Security-Zero_Cloud_Egress-blue?style=for-the-badge" alt="Security: Zero Cloud Egress"/>
</p>

---

## Visual Showcase

<table>
  <tr>
    <td align="center" width="25%">
      <strong>1. Profile Selection</strong><br/>
      <img src="screenshots/profileselect_live.png" alt="Profile Selection" width="200"/><br/>
      <em>Multi-child profile isolation (up to 6)</em>
    </td>
    <td align="center" width="25%">
      <strong>2. Phonics World Map</strong><br/>
      <img src="screenshots/map_alive_scrolled1.png" alt="Phonics World Map" width="200"/><br/>
      <em>Bohol Chocolate Hills progression</em>
    </td>
    <td align="center" width="25%">
      <strong>3. Hear It (Listen)</strong><br/>
      <img src="screenshots/hearit_live.png" alt="Hear It Screen" width="200"/><br/>
      <em>Native phoneme audio modeling</em>
    </td>
    <td align="center" width="25%">
      <strong>4. Say It (Speak)</strong><br/>
      <img src="screenshots/sayit_live.png" alt="Say It Screen" width="200"/><br/>
      <em>Edge Vosk speech evaluation</em>
    </td>
  </tr>
  <tr>
    <td align="center" width="25%">
      <strong>5. Find It (Discriminate)</strong><br/>
      <img src="screenshots/findit_live.png" alt="Find It Screen" width="200"/><br/>
      <em>5-card sound discrimination grid</em>
    </td>
    <td align="center" width="25%">
      <strong>6. Blend It (Synthesize)</strong><br/>
      <img src="screenshots/blendit_live.png" alt="Blend It Screen" width="200"/><br/>
      <em>CVC word construction tiles</em>
    </td>
    <td align="center" width="25%">
      <strong>7. Parent Dashboard</strong><br/>
      <img src="screenshots/parent_dashboard_redesigned_live.png" alt="Parent Dashboard" width="200"/><br/>
      <em>Diagnostic phoneme heatmap</em>
    </td>
    <td align="center" width="25%">
      <strong>8. Report Export</strong><br/>
      <img src="screenshots/report_preview_live.png" alt="PDF Report Preview" width="200"/><br/>
      <em>On-device PDF generation</em>
    </td>
  </tr>
</table>

---

## 1. Executive Summary & Research Motivation

In the Philippines, early literacy development remains a critical national challenge. According to the World Bank and DepEd assessments, over 80% of Filipino Grade 1 learners struggle with foundational reading and phonemic awareness. Existing commercial educational apps (e.g., ABCmouse, Khan Academy Kids) require continuous high-speed internet connections, introduce uncalibrated foreign accents, and assume pre-existing phonetic literacy.

**PlayIT** is an offline-first, pediatric-calibrated Android application engineered to solve this crisis. Grounded in the Department of Education's **Marungko Approach**, PlayIT guides children through a phono-syllabic learning sequence (28 phonemes across 7 progression chapters). The system combines on-device speech recognition via a localized **Vosk acoustic model**, calm Duolingo ABC-inspired activity stages, and culturally contextualized Filipino imagery (Bohol Chocolate Hills, tarsier companion mascot Lily).

### Core Research Contributions
1. **Edge AI Speech Evaluation**: Offline phoneme-level speech judge providing instantaneous auditory modeling and non-punitive feedback without cloud APIs.
2. **Pediatric Ergonomic Calibration**: Validated against Cognitive Load Theory, establishing a 64dp minimum touch target floor, zero-harsh-red corrections, and zero visual emojis.
3. **100% Data Privacy Guarantee**: Complete offline SQLite/Room architecture with zero network egress, fully complying with RA 10173 (Philippine Data Privacy Act) and COPPA.

---

## 2. The Marungko Pedagogical Framework

PlayIT sequences phonics instruction according to the proven **Marungko Method**, introducing sounds by phonemic distinctiveness rather than alphabetical order:

| Chapter | Title & Biome | Phoneme Progression | Milestone Challenge |
| :---: | :--- | :--- | :--- |
| **Unit 1** | Bohol Hills Horizon | **M, S, A, I, O** | CVC Blending: *AM, SAM, MAT* |
| **Unit 2** | Coconut Grove Sands | **B, E, U, T, K** | CVC Blending: *BAT, TUB, KIT* |
| **Unit 3** | Chocolate Hills Summit | **L, Y, N, G, R** | CVC Blending: *YAM, RUG, NET* |
| **Unit 4** | Mangrove River Crossing | **P, NG, D, H, W** | CVC Blending: *PIG, HEN, WEB* |
| **Unit 5** | Rice Terraces Overlook | **C, F, J, Ñ, V** | CVC Blending: *CAT, FAN, VAN* |
| **Unit 6** | Coral Reef Cove | **X, Z, Q** | CVC Blending: *BOX, ZIP, QUIZ* |
| **Unit 7** | Mount Pulag Summit | **Consolidation** | Full 28-Phoneme Mastery & Story Blending |

### The 3-Sublevel Phonics Loop
Each letter node implements a rigorous 3-step mastery cycle:
1. **Hear It (Auditory Modeling)**: Child listens to the pure phoneme sound, followed by the keyword picture and carrier line.
2. **Say It (Expressive Production)**: Child speaks the target sound into the microphone. Vosk evaluates accuracy against foil-scoped grammars. If the child misses twice, an articulation mouth-shape guide appears beside the microphone.
3. **Find It (Receptive Discrimination)**: Child identifies the target sound among 4 distractors in a high-contrast 5-card gummy grid.

---

## 3. Software Architecture & Clean Engineering Standards

PlayIT adheres strictly to **Clean Architecture** and the **MVVM (Model-View-ViewModel)** design pattern:

```
┌──────────────────────────────────────────────────────────┐
│                   Presentation Layer                     │
│  Jetpack Compose M3 • ViewModels • Navigation Graph      │
└────────────────────────────┬─────────────────────────────┘
                             │ (Uni-Directional Data Flow)
┌────────────────────────────▼─────────────────────────────┐
│                      Domain Layer                        │
│  Pure Kotlin (Zero Android Imports) • Use Cases •        │
│  Calculators • SpeechValidator • Business Rules          │
└────────────────────────────▲─────────────────────────────┘
                             │ (Dependency Inversion)
┌────────────────────────────┴─────────────────────────────┐
│                      Data Layer                          │
│  Room Database (SQLite) • Repositories • Vosk ASR Engine │
│  AudioPlayer (MediaPlayer) • Android Native PdfExporter  │
└──────────────────────────────────────────────────────────┘
```

- **Domain Layer Isolation**: `com.playit.app.domain` is pure Kotlin containing zero `android.*` dependencies.
- **Repository Pattern**: All 9 repositories implement structured `try-catch` error handling with fallback StateFlows.
- **On-Device Vosk Pipeline**: Scopes acoustic grammars per phoneme foil, eliminating false positive letter-name substitutions ('em' for /m/).

---

## 4. Evaluation Guide for Midterm Reviewers

### A. Quick Start Installation
1. Download `playIT-debug.apk` from the `01_Software_and_Deployment/` folder.
2. Install via ADB:
   ```bash
   adb install -r playIT-debug.apk
   ```
   *(Or copy the APK to your Android device and install via the native File Manager).*
3. Launch **PlayIT**.

### B. Demo Walkthrough Instructions
- **No Credentials Needed**: The app is completely offline.
- **Learner Profiles**: Select profile **'Zendrix'** to review completed progress, or tap **'+'** to test the child-friendly avatar onboarding flow.
- **Parent Zone Access**: Tap the top-left padlock icon. Solve the adult arithmetic problem (e.g. `24 + 19 = 43`) on the on-screen keypad to enter the educator analytics dashboard and test PDF export.

---

## 5. Build & Verification Instructions

### Prerequisites
- JDK 17 (Azul Zulu or OpenJDK)
- Android SDK 34 (Command line tools or Android Studio Iguana+)

### Compile & Build APK
```bash
./gradlew assembleDebug
# Generated APK: app/build/outputs/apk/debug/app-debug.apk
```

### Run Automated Unit Test Suite
```bash
./gradlew testDebugUnitTest
# 433 Tests Executed: 427 Passed, 6 Skipped by design, 0 Failed.
```

---

## 6. Research Team & Acknowledgements

### Capstone Research Team (Group 56 — IT411)
- **Riva, Zendrix** — Team Lead / Systems & Mobile Architect
- **Palis, John James** — Frontend / UI Integration Engineer
- **Miel, Kaysean** — Database & Persistence Specialist
- **Durano, Ashlee Skye** — QA & Pedagogical Evaluator
- **Bien, Erik Samuel** — Documentation & Research Analyst

**Capstone Adviser**: Mr. Joemarie C. Amparo  
*College of Computer Studies, Cebu Institute of Technology – University*

### AI Assistance & Tooling Disclosure
In accordance with CIT-U CCS academic integrity guidelines, the team discloses that:
- **Anthropic Claude**: Utilized as an automated code reviewer, test generator, and technical documentation assistant under human architectural guidance.
- **Google Gemini & Argon 4**: Utilized for text-to-image 2D vector asset generation conforming to the project's style guide and #4A2E18 outline constraints.
- **Vosk Speech Recognition**: Embedded open-source offline speech recognition engine (Apache 2.0).
