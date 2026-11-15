# PlayIT Capstone Final Validation & Research Paper Audit Handoff

**Project Title:** *PlayIT: An Offline-First Gamified Early Literacy Mobile Application Using the DepEd Marungko Approach*  
**Course:** IT411 Capstone & Research 2  
**Team Code:** G56 (Group 56)  
**Team Members:** Zendrix Riva (Lead), Erik Samuel Bien, Ashlee Skye Durano, Kaysean Miel, John James Palis  
**Adviser:** Mr. Joemarie C. Amparo  
**Institution:** College of Computer Studies, Cebu Institute of Technology – University (BS Information Technology)  
**Primary Branch:** `main` ([github.com/zendrix-hub/playIt](https://github.com/zendrix-hub/playIt))  
**Google Drive Root (`G56/`):** [1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc](https://drive.google.com/drive/folders/1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc)  
**Last Updated:** November 15, 2026

---

## 1. Handoff Purpose: Final Defense-Readiness Validation Audit

All capstone consultation documents, Week 11–12 user validation PDFs, and Week 13–14 ACM research paper deliverables have been completed, visually illustrated, cross-linked to primary sources, and synchronized to Google Drive and `main`.

Claude's assignment in this phase is **not** to draft from scratch, but to execute a thorough **pre-defense quality, numerical consistency, traceability, and formatting audit** across all completed files to confirm **99%+ submission readiness** for final adviser consultation and capstone defense.

---

## 2. Verified Submission Baseline & Source-of-Truth Metrics

Auditors must verify that all documents align strictly with these primary records:

### 2.1 Deployed Builds (`02_Software_and_Deployment/04_Deployment_Evidence/`, Folder `1EsE5EoWxNN4gNl3EMTa_HPSo86touDZG`)
- **Current Release APK:** [`playit.apk`](https://drive.google.com/file/d/1pIr2360noHf64TOT-sAwk_uNlk2gSeQa/view?usp=sharing) (Drive ID: `1pIr2360noHf64TOT-sAwk_uNlk2gSeQa`)
  - **Commit:** `67fe890` (08 November 2026, matches `app/` on `main`)
  - **Size & Hash:** `119,339,266` bytes (~113.8 MiB) | SHA-256: `A6D4560F0F593F0162D8B3A6A2A21EA173701966E54835CB27B64753579B184F`
  - **Package & SDK:** `com.playit.app`, Min SDK 26 (Android 8.0), Target SDK 34 (Android 14)
  - **Privacy / Offline Status:** Verified **0 `INTERNET` permissions** via `aapt` dump (`RECORD_AUDIO` only for on-device Vosk ASR; scoped storage permissions capped for legacy Android).
  - **Scope:** Full 7-chapter, 26-letter DepEd Marungko system (*Hear It*, *Say It*, *Find It*, *Blend It*), Tarsier guide Lily, animal avatar + name onboarding, mouth-movement video cues, and post-Round 1 refinements (already incorporates the 29 Oct visual refresh).
  - **Automated Verification:** **697 unit/component tests run (691 passed, 6 skipped by design, 0 failed)**; Layout Matrix 23 checks x 4 screen profiles = 92 runs (86 passed, 6 skipped by design on `360 x 640 dp` font-scale 1.3); >= 64 dp child touch target floor verified.
- **Midterm Baseline APK (Comparison Only):** [`playit-debug-H-0320385.apk`](https://drive.google.com/file/d/1F-_kQS-M1TgwVd1CUBwMCXXqqFf_fmog/view?usp=sharing) (Drive ID: `1F-_kQS-M1TgwVd1CUBwMCXXqqFf_fmog` | [Midterm Portal Mirror](https://drive.google.com/file/d/1SeIXJS1z2YJwvh5mfNbVmIP9cExbRvkI/view?usp=sharing))
  - **Commit:** `0320385` (18 October 2026), `108,317,310` bytes (~103.3 MiB), SHA-256: `b5a54c18d1405c3851b7fec5c110f03f1aeaaa899a343b12f1796e04398c42d4`, 433 tests (427 passed, 6 skipped, 0 failed), documented in `STD v1.0`.

### 2.2 Deduplicated Participant Cohort & Validation Results (`N = 30`)
Across all instruments, 34 raw form submissions were collected (16 learner observations, 5 parent questionnaires, and 13 teacher forms across the three pedagogical instruments). Deduplicating four teachers who participated in more than one evaluation wave yields **9 unique teachers** and a verified cohort of **30 participants (`16 learners + 5 parents + 9 teachers`)**:
- **Grade 1 Learners (`n = 16`, observed child end-users):**
  - Enjoyment (happiest face after playing): **15 of 16**
  - Character affinity (liked Lily and animal friends): **15 of 16**
  - Willingness to play again: **14 of 16**
  - Perceived ease of play (very easy): **9 of 16**
- **Parents / Supervising Adults (`n = 5`, bridge evaluators):**
  - Mean System Usability Scale (SUS): **75.5 / 100** (Good / Grade B usability)
  - Grade 1 appropriateness mean: **4.60 / 5.00**
  - Recommendation intent mean: **4.40 / 5.00**
  - Child-accessible & offline privacy checklist (`n = 5`): **100% Yes** across large/readable design, simple screens, safe child-friendly tone, and clear sounds
- **Teachers & Specialists (`n = 9` unique teachers across R1 + R2):**
  - Round 1 checklist (`n = 4`): **100% alignment** on DepEd Marungko sequence and child usability alongside qualitative fixes applied before Round 2
  - Round 2 final 16-item rubric (`n = 5` unique teachers): **4.72 / 5.00** overall mean reported in `G56_Research_Paper_Draft.md` (`4.725 / 5.00` unrounded across domains; `MOD-01` Phoneme Audio Clarity `4.60 / 5`, `MOD-02` Say It Fairness `4.80 / 5`, lowest item `4.20 / 5` on DepEd DO 8 transmuted grade conversion)
- **Critical Claim Boundaries:**
  - Findings measure **stakeholder reception, usability, and expert pedagogical design soundness**. Do **not** claim longitudinal reading-achievement gains because no pre/post reading comprehension test was conducted.
  - Child task completion rates, timed speech-judge latency/WER, and latency telemetry were not instrumentally logged in this capstone cycle and are framed transparently as planned future work / methodology boundaries.

---

## 3. Inventory of Completed Deliverables for Audit

### 3.1 Adviser Consultation Pack (`docs/submission/` & Google Drive)
1. **`A_SOFTWARE_FUNCTIONALITY_AND_COMPLETION.md` / `.pdf`** (`1WjNZlApE_W51hmHD6wyRjFVfh9LH8d0e` / `1oXU-4dHwG7DN1r0tK_D-oIdZkSH-SXY6` in `02_Software_and_Deployment`) — Full feature completion overview, Marungko instructional flow, Clean Architecture pipeline, screenshots from `playit.apk`, and primary Drive evidence links.
2. **`B_DEPLOYMENT_AND_SOURCE_CODE_REPOSITORY.md` / `.pdf`** (`1ZYu2ZWD-mJNqvj9Z1PspJ5V18-1mOXKi` / `1W5OTJv-ZblurEISgUS6MCvvK_NEjXVaS` in `02_Software_and_Deployment`) — Single `main`-branch Git repository guide, 2-APK table (`playit.apk` and `playit-debug-H-0320385.apk`), device compatibility matrix, offline permissions, and Gradle build commands.
3. **`DEPLOYMENT_AND_PERMISSIONS_INSPECTION.md` / `.pdf`** (`1sb9tSvGZcMRKTMxjLshEb_Cn2rrtbNhz` / `1QW9g32vd7QYzZE6wSlKAvwgVVX-lzu2C` in `04_Deployment_Evidence`) — Side-by-side matrix of `playit.apk` (`67fe890`) vs. `playit-debug-H-0320385.apk` (`0320385`), `aapt` manifest permissions dump confirming zero `INTERNET` permission, and SHA-256 verification.
4. **`E_TESTING_AND_QA_SURVEY_SYNTHESIS_30_PARTICIPANTS.md` / `.pdf`** (`1WkXEYR-yQ6eI7jcAEtRcAI9S7xOXOH87` & `1l83n2xEt_vahT0BtiTvQniGvJwYm_7GR` / `1WiMrHq0VHPTGlkFUN8igugD5ot4hfDEl` in `04_Testing_and_Quality_Assurance` + `05_Final_User_Validation_and_Evaluation`) — Consolidated QA evidence (697 tests, 92 layout-matrix checks), deduplicated 30-participant methodology, quantitative tables, embedded charts, and direct links to Drive evidence folders.

### 3.2 Generated High-Resolution Diagrams (`docs/diagrams/`)
Verify visual legibility and text consistency across all five embedded PNG figures:
- `marungko_lesson_flow.png`
- `clean_architecture_pipeline.png`
- `build_progression_flow.png` (updated 2-build progression: `playit-debug-H-0320385.apk` -> `playit.apk`)
- `triadic_validation_framework.png`
- `empirical_results_charts.png`

### 3.3 Week 11–12 Final User Validation PDFs (`05_Final_User_Validation_and_Evaluation/`, Folder `1Uu0jv9wGbIWL8yfNBwG-xeRUxtgWJZ48`)
All 18 formal PDFs have been authored, illustrated with app screenshots and diagrams, and uploaded in-place across the five Week 11–12 folders:
- **`01_User_Validation_Plan/` (`1_-LwzpmquJU2Xy5uMPtaNeA0-74lxsXR`)** —
  - `02_Target_Respondents_and_Justification`: `TARGET_RESPONDENTS_AND_SAMPLE_JUSTIFICATION.pdf` (`180uOlu9uVKWKZv_XaNSRbwlqeK0YR61x`)
  - `04_Evaluation_Instruments_and_Procedures`: `EVALUATION_INSTRUMENTS_AND_PROCEDURES_GUIDE.pdf` (`17m6mIZqHbh1nmYAc-Fy0RnBT92yiOGfE`)
  - `05_Evaluation_Metrics_and_Success_Criteria`: `EVALUATION_METRICS_AND_SUCCESS_CRITERIA_MATRIX.pdf` (`1ZVYumNbgJS3Dk-M24FtO3iKxCn-TIoap`)
- **`02_Instruments_and_Data_Collection/` (`1bfkjvGXOLLyTrAN_fJ8dEiKyAkkNvci7`)** —
  - `02_User_Task_Scenarios_and_Testing_Procedures`: `USER_TASK_SCENARIOS_AND_STANDARDIZED_PROCEDURES.pdf` (`1Ktw06Enrikzm_6dx_8Qmz5wwHhUv2Qb5`)
  - `03_Interview_Guides_and_Feedback_Forms`: `INTERVIEW_GUIDES_AND_OBSERVATION_PROTOCOLS.pdf` (`1587tWBAsY-QGM-dEJiVzycHWhvcQCbik`)
- **`03_Testing_and_Evaluation_Evidence/` (`1GlJnqMXS9cecT4wynqd2Jccsjk8uL4DX`)** —
  - `01_Task_Completion_Rates_and_Time_on_Task`: `TASK_COMPLETION_RATES_AND_TIME_ANALYSIS.pdf` (`19LORJdtY-9j4B7LfLp0_Zknn3WZ98ZG0`)
  - `02_Questionnaire_Scores`: `QUESTIONNAIRE_SCORES_AND_USABILITY_RATINGS.pdf` (`1pfMa3lzeo4Kufk10Dl6Y44UyML6IYd1W`)
- **`04_Results_and_Analysis/` (`12-1NSwvHD1jgXwQDRVJIxQZ_cp7JX-9z`)** —
  - `01_Respondent_Profile_and_Participant_Count`: `RESPONDENT_PROFILE_AND_PARTICIPANT_COUNT.pdf` (`1U_YZZVAQQRCcaahbhDZDB-wz-LCcBKy9`)
  - `02_Validation_Procedure_and_Data_Collection_Period`: `VALIDATION_PROCEDURE_AND_DATA_COLLECTION_PERIOD.pdf` (`1-A3H8N280MgtWGDVx6qpHSEZ6ffFfUnG`)
  - `03_Evaluation_Framework_and_Instruments_Used`: `EVALUATION_FRAMEWORK_AND_INSTRUMENTS_USED.pdf` (`1NzYKfBlXUxUiIzcIFQcedH_1tVmo4ewo`)
  - `05_Results_According_to_Research_Questions_and_Objectives`: `RESULTS_BY_RESEARCH_QUESTIONS_AND_OBJECTIVES.pdf` (`1a7h3l672YLe9Yx0XOjyMKAZW6VV9sVfo`)
  - `06_Identified_Issues_and_Functional_Limitations`: `IDENTIFIED_USABILITY_ISSUES_AND_TECHNICAL_LIMITATIONS.pdf` (`1sVJCSqp6520gkBVHGi31l_kiX3TJpxZ-`)
  - `07_Achievement_of_System_Objectives`: `SYSTEM_OBJECTIVES_ACHIEVEMENT_EVALUATION.pdf` (`1-Vqb-rd4i3hV56LGcy88muSVweE5uq2V`)
- **`05_Recommendations_and_Improvements/` (`11XvUEapFLsp-vq_zw48sakJssOPQC233`)** —
  - `01_User_and_Stakeholder_Concerns`: `USER_AND_STAKEHOLDER_CONCERNS_SYNTHESIS.pdf` (`1w9SBf_Kgk7PejE-Qn1t63Bn0IIjzdRgp`)
  - `02_Prioritized_Recommended_Improvements`: `PRIORITIZED_RECOMMENDED_IMPROVEMENTS.pdf` (`12fxvyI5X3g_NEH_jJf1Tglg2i1RoOlKc`)
  - `03_Actions_Taken_or_Planned`: `ACTIONS_TAKEN_AND_IMPLEMENTATION_STATUS.pdf` (`1BXBJ7_iNGe0t5tHlCM0dxGYzIGavUPm2`)
  - `04_Required_Software_and_Documentation_Updates`: `REQUIRED_SOFTWARE_AND_DOCUMENTATION_UPDATES.pdf` (`1VwBaRHWCg5lHlDXCr52hqcyHK1x_CNPU`)
  - `05_Remaining_Limitations_and_Future_Development`: `REMAINING_LIMITATIONS_AND_FUTURE_ROADMAP.pdf` (`19sQF-Z_nkKOJU2FGbgl_VpUF8ie0h5wf`)

### 3.4 Week 13–14 Research Paper (`docs/research-paper/` & `06_Research_Paper/`, Folder `1GZ-YiDcHyoGYFJ5WrikGGKKm0WEXygJp`)
Formatted in formal **ACM Conference Proceedings** structure (`G56_Research_Paper_Draft.md`, `.docx`, `.pdf`) and synced to:
- `01_Research_Paper_Draft/` (`191PcWPVRCm7BgM3Jw6yIWr2gZESIhVQm`): `.docx` (`13hu0Sdx4klR609lqBtXKyYwPo9p-6h1F`), `.md` (`1ACvLH4Er_lrISd190HKjJaTYp0EMF_u6`), `.pdf` (`16wIt3Cu-kNtqaKyx_ShazpcbDtAWAMm9`)
- `02_Final_Editable_ACM_Manuscript/` (`1QR6QgXD5W9FY0RHUc24pWJcvdqTbpJs0`): `G56_Final_Editable_ACM_Manuscript.docx` (`1ovtDVjPHX7dfytdZes9HFYp2Q_JEutP_`) & `.md` (`1CGV0Af2jNX-av3aRQ6jwbNbKYdgUaTQC`)
- `03_Final_PDF_Copy/` (`1VBWwdcA54t5zwo4RXaPgo15FKHzUhOSM`): `G56_Final_Research_Paper.pdf` (`10h0n_wzTzRfX0Yi-OR1GIxgw3ZTJuYYx`)

### 3.5 TekCon 2026 Kit (`docs/tekcon-2026/` & `07_TechCon_2026/`, Folder `1i5cwHsEJi6J9wtvKMbD69Lq2TKiRp4QZ`)
- `01_Research_Poster/` (`1Vt1b6a8LttqVPXs9Mp25DEfFFAJYrlD4`)
- `02_Video_Pitch/` (`1CnDth_WJxj12brFLvHOXoXnecNhv-d8_`)
- `03_Software_Product_Logo/` (`1gKq1PWo5ESfFRRJkTeHZHvb6LwNtqGC4`)

---

## 4. Audit Checklist for Claude (99%+ Defense Readiness Verification)

When reviewing this repository and Google Drive package for the user, inspect and report on:
1. **Cross-Document Numerical Consistency:** Confirm all participant counts (`N = 30`: `16 learners`, `5 parents`, `9 unique teachers` from `34` submissions), Learner Smileyometer figures (`15/16`, `15/16`, `14/16`, `9/16`), Parent SUS (`75.5/100`), Teacher R2 rubric (`4.725/5.00` or `4.72`), and unit test counts (`697` total / `691` passed / `6` skipped / `0` failed) match across `A`, `B`, `E`, the 18 Validation PDFs, `G56_Research_Paper_Draft.md`, and `docs/tekcon-2026/round2_data.json`.
2. **Formal Tone & Clean Submission Check:** Confirm no informal internal AI instructions, no `"frozen"` jargon, no extraneous course suffix (`"/ Software Engineering 2"`), no leftover references to the removed intermediate visual-refresh APK (`playit-debug-design-9ce23e2.apk`), and no references to `playit-debug-adviser-67fe890.apk` in active submission documents.
3. **Link & Drive Integrity:** Spot-check that all clickable GitHub links point to `main` (`github.com/zendrix-hub/playIt`) and all APK links point cleanly to `playit.apk` (`1pIr2360noHf64TOT-sAwk_uNlk2gSeQa`) and `playit-debug-H-0320385.apk` (`1F-_kQS-M1TgwVd1CUBwMCXXqqFf_fmog` / `1SeIXJS1z2YJwvh5mfNbVmIP9cExbRvkI`).
4. **Research Defensibility & Methodology Transparency:** Verify that no document claims longitudinal reading gains, that limitations (convenience sampling, lack of pre/post reading test, facilitator presence in child Smileyometer, unmeasured ASR timing/WER) are clearly articulated in both the Research Paper and Validation Reports, and that any remaining defense Q&A risks are highlighted with concise, truthful talking points.
5. **ACM Manuscript & Poster Checker Verification:** Confirm `G56_Research_Paper_Draft.pdf` formatting, figure crispness, citation numbering (`[1]`..`[n]`), and run `cd docs/tekcon-2026/tools && npm run build` to verify zero checker errors.
