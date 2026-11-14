# PlayIT Research Paper & Final Submission Handoff (Weeks 13–14)

> **For Claude Code & Erik Samuel Bien (`thatguyKire`)**  
> **Course Requirements**: Capstone 2 & Software Engineering 2  
> **Team Code**: `G56` (Group 56)  
> **Project Name**: PlayIT (Offline Phonics App for Grade 1 Filipino Learners)  
> **Git Branch**: `erik/research-paper-final`  
> **Google Drive Working Folder**: `G56` ([Drive ID: `1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc`](https://drive.google.com/drive/folders/1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc))  
> **CRITICAL RULE**: Do **NOT** modify the original shared source folders (`part9_Validation` or `PlayIT_MIDTERM DELIVERABLE`). All modifications, uploads, and file additions must be executed **solely inside the team's `G56` folder on Google Drive**.

---

## 1. Overview & Objectives (Weeks 13 and 14)

Weeks 13 and 14 focus on the preparation, revision, and final submission of the **Capstone Research Paper** and the finalization of the project repository on Google Drive.

The research paper must present a rigorous, coherent synthesis of the entire PlayIT project:
- The early literacy challenge in Grade 1 and the gap in letter-sound (phonemic) awareness. PlayIT teaches the sounds of the 26 English letters in Marungko order; do not describe it as a mother-tongue reading programme.
- Related literature and studies (synthetic phonics, mobile gamification, offline accessibility).
- System architecture, the lesson steps in the app's own order and names (Hear It, Say It, Find It, then Blend It at the end of each chapter), and Lily the mascot.
- Systematic testing and final user/stakeholder validation based on the **current survey gathered with 30 participants** ($N=30$).
- Discussion of SMART objective achievement and prioritized future improvements.

---

## 2. Frozen Survey Dataset ($N=30$ Participants)

All numbers reported in the research paper and final validation folders must draw directly and strictly from the frozen survey dataset in `round2_data.json` and the Google Drive master response sheets (`Cleaned Master (Responses)` and `Master (Responses)`):

* **Total Participants**: Exactly **30 participants** ($N=30$, status: `"actual"`).
* **Participant Breakdown**:
  * **16 Grade 1 Learners** (observed during play, then asked four smiley-face questions: 15 of 16 chose the happy face overall, 1 okay, 0 sad; 15 of 16 liked the characters; 14 of 16 would play again, 2 okay; 9 of 16 found it easy, 6 okay, 1 neutral). **No per-step results were recorded:** there is no Say It first-try count, no Find It or Blend It tally and no time on task. Those fields are `null` in `round2_data.json`.
  * **5 Parents / Supervising Adults** (System Usability Scale 75.5 out of 100; Grade 1 fit 4.6 of 5; would recommend 4.4 of 5; a seven-item accessibility checklist). The Round 2 questions on usefulness, ease of use, safety and offline benefit were not collected, so there are no means for them.
  * **9 Teachers** (up to 9 across three forms: a 12-item yes/no checklist answered by 4, an 18-item draft rubric answered by 4, and the final 16-item rubric answered by 5). The overall mean of 4.72 out of 5 is the final rubric's, from **5 of the 9 teachers**; always give that group size. Item means are in `round2_data.json` and in `docs/submission/E_TESTING_AND_QA_SURVEY_SYNTHESIS_30_PARTICIPANTS.md`.
  * **0 Technical Evaluators** (`technical = 0`).
* **30 participants, not 34.** The forms hold 34 responses (16 learners, 5 parents, 13 teacher responses across the three teacher forms). Teachers who answered more than one form are counted once, which gives up to 9 teachers and 30 participants.
* **The only quotation on record:** "The app is very useful and engaging for Grade 1 learners." (DepEd Grade 1 teacher, Round 1, T-1). Take any other quotation word for word from the response sheets, or leave it out.

> [!IMPORTANT]
> **Strict Reporting Constraints**:
> 1. Always distinguish learners from evaluators: children were observed and answered smileyometer feedback; parents and teachers evaluated usability and teaching quality as "the bridge to the child".
> 2. Never refer to "30 users" or call the children "respondents". Use "30 participants" with the explicit breakdown.
> 3. Never invent, round up, or adjust figures. Give counts with their group size ("15 of 16") and do not turn small groups into percentages. If a number is not in `round2_data.json`, it was not measured: say so instead of estimating.
> 4. **Never claim that children "learned to read better" or achieved higher reading test scores**. The study evaluated user reactions, engagement, usability, and stakeholder acceptance—not longitudinal literacy gains.

---

## 3. Prescribed ACM Research Paper Structure

Each team must submit a complete manuscript draft in **Week 13** and the finalized manuscript in **Week 14** using the official **ACM Conference Paper Template** (`.docx` or LaTeX). The paper must include:

### 3.1 Title, Authors, Abstract & Keywords
* **Title**: *PlayIT: An Offline Synthetic Phonics Mobile Application for Early Literacy Development among Grade 1 Filipino Learners*
* **Authors**: Zendrix Riva, Erik Samuel Bien, Ashlee Skye Durano, Kaysean Miel, John James Palis.
* **Adviser**: Mr. Joemarie C. Amparo.
* **Abstract**: ~150–200 words synthesizing the reading gap, the offline Android solution, the 4-step Marungko method, mascot co-player design, and empirical validation outcomes with 30 participants.
* **Keywords**: Synthetic Phonics, Mobile Assisted Language Learning (MALL), Offline EdTech, Early Childhood Literacy, Marungko Method, Gamification.

### 3.2 1. Introduction
* **Research Problem**: Early-grade reading difficulties and phonological awareness deficits among early Filipino learners; lack of accessible interactive phonics tools at home without costly internet connectivity.
* **Objectives & SMART Goals**: Develop an offline-first mobile phonics game and empirically validate its usability, engagement, and pedagogical alignment.
* **Scope & Delimitations**: The sounds of the 26 English letters in 7 chapters, in Marungko order (m, s, a, i first); N with tilde and NG are not taught. Android phones (Android 8.0 and later); fully offline.

### 3.3 2. Related Literature & Studies
* Synthetic phonics vs. whole-language approaches in early childhood education.
* Mother Tongue-Based Multilingual Education (MTB-MLE) and DepEd's Marungko approach.
* Mobile game-based learning and interactive feedback mechanisms for young children.
* **Research Gap**: Scarcity of culturally localized, offline-first synthetic phonics mobile apps tailored for early Filipino learners in resource-constrained environments.

### 3.4 3. Methodology
* **Development Approach**: Agile/Iterative prototyping with progressive user-informed refactoring.
* **Evaluation Framework**: Integration of ISO/IEC 25010 software quality characteristics (Usability, Functional Suitability, Performance Efficiency) and early pedagogical rubrics.
* **Participants & Setting**: Profile of the 30 participants (16 Grade 1 pupils, 5 parents, 9 teachers). State only the consents and permissions the team actually obtained; do not write "ethical clearance" unless the team holds one.
* **Instruments**: Observation of play with a smiley-face (smileyometer) questionnaire for learners; the System Usability Scale and an accessibility checklist for parents; the teacher checklist and rubrics (12-item yes/no, 18-item draft, 16-item final).
* **Data Analysis Procedures**: Descriptive statistics, task completion frequency analysis, and qualitative feedback categorization.

### 3.5 4. System Design & Implementation
* **System Architecture**: Native Android (Kotlin, Jetpack Compose), Clean Architecture with MVVM and Hilt, progress stored in a Room (SQLite) database on the device, on-device speech recognition with Vosk, no internet permission.
* **Pedagogical Engine (the app's own steps and order; see `README.md`)**:
  1. *Hear It*: Lily models the sound and the key word.
  2. *Say It*: the child says the key word; the phone judges it; a mouth-shape cue appears on the second miss. Say It never takes a heart.
  3. *Find It*: the child taps the three pictures, out of five, whose names start with the sound.
  4. *Blend It*: opens when a chapter's letters are done; the child builds words (SAM, SIS, AM ...) from letter tiles.
* **Mascot Co-player Design**: Lily the Philippine tarsier acting as an empathetic, encouraging companion.
* **Audio & Visual Design**: Flat, accessible visual language, high-contrast text, authentic voice assets.

### 3.6 5. Results & Evaluation
* **Learner Reactions (n = 16)**: 15 of 16 chose the happy face; 15 of 16 liked the characters; 14 of 16 would play again; 9 of 16 found it easy. Task completion and time on task were not recorded and cannot be reported.
* **Parent Usability Evaluation (n = 5)**: SUS 75.5 out of 100; Grade 1 fit 4.6; would recommend 4.4; accessibility checklist counts.
* **Teacher Pedagogical Evaluation**: final 16-item rubric, mean 4.72 out of 5 from 5 of the 9 teachers; item means from the data file.
* **Software Verification**: 697 automated tests: 691 passed, 6 skipped by design, 0 failed. Latency, battery and CPU use were **not** measured (STD: TC-PERF not run); do not claim them. Two defects were open in the STD (DEF-01, DEF-02).

### 3.7 6. Discussion
* Synthesis of technical performance and educational feedback.
* Fulfillment of pre-approved SMART objectives.
* Limits of the evidence: small groups, reactions rather than learning, no per-step observation, and speech-judge agreement with teachers not yet measured.

### 3.8 7. Conclusion & Future Work
* Summary of contributions: A verified, free, offline digital literacy tool bridging home and classroom learning.
* Remaining limitations: those in the README's "Known Limitations" (Find It distractors, the star rule, sounds awaiting the teacher audit, telemetry and spaced recall not built) and the limits of the study above.
* Future roadmap: the planned items (telemetry and CSV export, spaced recall, session pacing), the teacher audit of the sounds, and a study of reading gains over a school term. The app already covers all 26 letters; do not describe that as future work.

### 3.9 Acknowledgments & References
* Formal acknowledgments of school partners, participating teachers, parents, and learners.
* Full bibliographic citations adhering to ACM style.

---

## 4. Google Drive Submission Organization (`G56`)

All files must be kept organized inside `G56` (ID: `1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc`). Claude should ensure that the final submission folders contain:

```text
G56/
│
├── 01_Software_and_Deployment/
│   ├── Deployed APKs (playit-debug-design-9ce23e2.apk, playit-debug-H-0320385.apk)
│   ├── GITHUB_REPOSITORY_ACCESS.md (Repository link, branch setup, build guide)
│   ├── README.pdf
│   ├── DEPLOYED_SYSTEM_AND_ACCESS_INSTRUCTIONS.pdf
│   ├── CODE_REFACTORING_EVIDENCE.pdf
│   └── FEATURE_COMPLETION_MATRIX.pdf
│
├── 02_Updated_Documentation/
│   ├── playIT_SRS_v3.0.pdf & .docx
│   ├── playIT_SDD_v2.0.pdf & .docx
│   ├── playIT_SPMP_v2.0.pdf & .docx
│   ├── playIT_STD_v1.0.pdf & .docx
│   ├── playIT_Traceability_Matrix.pdf
│   └── playIT_Requirements_Inventory.pdf
│
├── 03_Testing_and_QA/
│   ├── FUNCTIONAL_TEST_RESULTS.pdf
│   ├── INTEGRATION_SYSTEM_AND_REGRESSION_TEST_RESULTS.pdf
│   ├── PERFORMANCE_TESTING_REPORT.pdf
│   ├── ACCEPTANCE_TESTING_AND_SIGNOFF_REPORT.pdf
│   ├── DEFECT_LOG_AND_RETEST_RECORD.pdf
│   ├── USABILITY_TESTING_AND_IMPROVEMENTS_REPORT.pdf
│   └── test_execution_evidence/
│
├── 04_Final-User_Validation/
│   ├── Approved_Proposal_and_SMART_Goals.pdf
│   ├── Final_Validation_Plan_and_Framework.pdf
│   ├── Final_Validation_Instrument.pdf
│   ├── Cleaned Master (Responses) & Master (Responses) (Google Sheets)
│   ├── Teacher & Parent Evaluation Forms (Google Forms)
│   ├── Testing Evidence Photos (16 Learners & Parents)
│   ├── round2_data.json ($N=30$ Frozen Survey Metrics)
│   ├── F2F_ADVISER_CONSULTATION_BRIEFING.pdf
│   └── Final_Validation_Results_and_Recommendations_Templates.pdf
│
├── 05_Research_Paper/
│   ├── 01_Research_Paper_Draft/ (Week 13 complete draft in ACM format)
│   ├── 02_Final_Editable_ACM_Manuscript/ (Week 14 editable ACM .docx or LaTeX zip)
│   ├── 03_Final_PDF_Copy/ (G56_Research_Paper_FINAL.pdf)
│   └── RESEARCH_PAPER_SUBMISSION_CHECKLIST.md
│
└── 07_TechCon_2026/
    ├── 01_Research_Poster/ (G56_Research_Poster.pdf, G56_Research_Poster_Source.pptx)
    ├── 02_Video_Pitch/ (G56_2-Minute_Pitch.mp4 - QR Code link preserved)
    └── 03_Software_Product_Logo/ (G56_Product_Logo_2000x2000.png, G56_Product_Logo_512x512.png)
```

> **Check before renaming or moving anything on Drive:** this tree, the seven-category tree in `CLAUDE_HANDOFF.md` and the instructor's 11 November layout (`06_TekCon_2026_Presentation`, `Research_Poster.pdf`) do not agree on folder numbers or file names. Confirm the instructor's latest announcement with the team lead first.
>
> **Note on Folder Numbering**: If the adviser evaluates under the 5-folder Capstone Final Submission layout (`01_Software_and_Deployment` through `05_Research_Paper`) or the 7-category TekCon layout, all corresponding folders and shortcuts are in place inside `G56`.

---

## 5. Working Rules for Claude

1. **Git Branch**: Keep all work and documentation on branch `erik/research-paper-final`.
2. **Never Modify Existing Source Files**: Do not modify app code, Gradle configs, or frozen json data files. Only create and update the documentation, paper drafts, and submission guides.
3. **Google Drive Scoping**: Always modify files inside `G56/` on Google Drive. **Never touch or edit the shared links for `part9_Validation` or `PlayIT_MIDTERM DELIVERABLE`**.
4. **Verification**: If editing any poster or pitch files, run `cd tools && npm run build` and ensure `0 failed`.
