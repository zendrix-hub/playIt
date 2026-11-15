# PlayIT: APK Deployment and Permissions Inspection Report

**Course:** IT411 Capstone & Research 2 / Software Engineering 2  
**Date:** November 15, 2026  
**Team Code:** G56 (Group 56)  
**Team Lead:** Zendrix Riva  
**Collaborator:** Erik Samuel Bien  
**Primary Current Build:** `playit-debug-adviser-67fe890.apk`  
**Google Drive Folder:** `G56/02_Software_and_Deployment/04_Deployment_Evidence/`  

---

## 1. File Verification & Build Matrix

The table below contrasts the submitted builds across development phases. The primary, active release package submitted for final evaluation and F2F consultation is **`playit-debug-adviser-67fe890.apk`**.

| Attribute | Current Release Build (`playit-debug-adviser-67fe890.apk`) | Design Refresh Build (`playit-debug-design-9ce23e2.apk`) | Midterm Baseline Build (`playit-debug-H-0320385.apk`) |
| :--- | :--- | :--- | :--- |
| **Commit Hash** | `67fe890` (Adviser feedback) | `9ce23e2` (Visual refresh) | `0320385` (Midterm baseline) |
| **Date** | 08 November 2026 | 29 October 2026 | 18 October 2026 |
| **File Size (Bytes)** | **119,339,266 bytes** (~113.8 MiB) | 119,161,284 bytes (~113.6 MB) | 108,317,310 bytes (~103.3 MB) |
| **SHA-256 Checksum** | `A6D4560F0F593F0162D8B3A6A2A21EA173701966E54835CB27B64753579B184F` | Recorded in Drive | `b5a54c18d1405c3851b7fec5c110f03f1aeaaa899a343b12f1796e04398c42d4` |
| **Unit Test Suite** | **697 tests: 691 passed, 6 skipped, 0 failed** | 482 tests: 476 passed, 6 skipped, 0 failed | 433 tests: 427 passed, 6 skipped, 0 failed |
| **Package ID** | `com.playit.app` | `com.playit.app` | `com.playit.app` |
| **Version Code / Name** | `1` / `1.0.0` | `1` / `1.0.0` | `1` / `1.0.0` |
| **Min / Target SDK** | API 26 (Android 8.0) / API 34 (Android 14) | API 26 / API 34 | API 26 / API 34 |
| **INTERNET Permission** | **NONE (0% Network, 100% Offline)** | **NONE (100% Offline)** | **NONE (100% Offline)** |
| **Drive File ID** | `1pIr2360noHf64TOT-sAwk_uNlk2gSeQa` | `1alFzoNOlFLUVZCS6u-7Cxa0B3txgqzdJ` | `1F-_kQS-M1TgwVd1CUBwMCXXqqFf_fmog` |

---

## 2. Core Architectural Boundaries & Permissions Inspection

PlayIT is engineered as an entirely self-contained educational tool for young Filipino learners, operating without dependencies on remote cloud infrastructure:

1. **Speech Recognition Architecture (Say It Module):**
   - Implemented using embedded Vosk (`vosk-android 0.3.47`) paired with a local phonetic model (`assets/vosk-model/`).
   - Acoustic inference executes on-device via local native libraries (`libvosk.so`, `libjnidispatch.so`).
   - Requires only `android.permission.RECORD_AUDIO`. Zero audio telemetry is transmitted off the phone.

2. **Haptic & Sensory Policy:**
   - No vibration or jarring haptics are requested (`android.permission.VIBRATE` is absent).
   - Validated by automated architecture policies (`PerformancePolicyTest`, TC-POL-02).

3. **Storage & Privacy Isolation:**
   - Storage permissions (`READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`) are strictly scoped with `maxSdkVersion='32'` and `maxSdkVersion='28'` respectively to comply with modern scoped storage on Android 10+.
   - Learner gameplay progress and animal avatar selection reside exclusively in sandboxed internal SQLite / Room database storage.

---

## 3. Official `aapt` Inspection Dump (Current Release Build)

The following inspection dump was executed using Android SDK Build Tools 34.0.0 against `playit-debug-adviser-67fe890.apk`:

```text
================================================================================
AAPT PERMISSIONS INSPECTION
File: playit-debug-adviser-67fe890.apk
Package: com.playit.app
================================================================================
package: com.playit.app
uses-permission: name='android.permission.RECORD_AUDIO'
uses-permission: name='android.permission.READ_EXTERNAL_STORAGE' maxSdkVersion='32'
uses-permission: name='android.permission.WRITE_EXTERNAL_STORAGE' maxSdkVersion='28'
permission: com.playit.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION
uses-permission: name='com.playit.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION'

INTERNET permission present? NO (Confirmed 100% Offline)

================================================================================
AAPT BADGING & SDK TARGETS
================================================================================
package: name='com.playit.app' versionCode='1' versionName='1.0.0' platformBuildVersionName='14' platformBuildVersionCode='34' compileSdkVersion='34' compileSdkVersionCodename='14'
sdkVersion:'26'
targetSdkVersion:'34'
application-label:'playIT'
application-icon-160:'res/mipmap-mdpi/ic_launcher.png'
application-icon-240:'res/mipmap-hdpi/ic_launcher.png'
application-icon-320:'res/mipmap-xhdpi/ic_launcher.png'
application-icon-480:'res/mipmap-xxhdpi/ic_launcher.png'
application-icon-640:'res/mipmap-xxxhdpi/ic_launcher.png'

================================================================================
HASH INTEGRITY VERIFICATION
================================================================================
File: playit-debug-adviser-67fe890.apk
Bytes: 119,339,266
SHA-256: A6D4560F0F593F0162D8B3A6A2A21EA173701966E54835CB27B64753579B184F
Status: VERIFIED MATCH with Git Repository app/ HEAD (commit 67fe890)
```
