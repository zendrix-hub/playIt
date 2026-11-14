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
- The early literacy challenge and mother-tongue phonemic awareness gap in Grade 1.
- Related literature and studies (synthetic phonics, mobile gamification, offline accessibility).
- System architecture, 4-step synthetic phonics mechanics (Sound It, Find It, Blend It, Say It), and Lily the mascot.
- Systematic testing and final user/stakeholder validation based on the **current survey gathered with 30 participants** ($N=30$).
- Discussion of SMART objective achievement and prioritized future improvements.

---

## 2. Frozen Survey Dataset ($N=30$ Participants)

All numbers reported in the research paper and final validation folders must draw directly and strictly from the frozen survey dataset in `round2_data.json` and the Google Drive master response sheets (`Cleaned Master (Responses)` and `Master (Responses)`):

* **Total Participants**: Exactly **30 participants** ($N=30$, status: `"actual"`).
* **Participant Breakdown**:
  * **16 Grade 1 Learners** (observed during interactive gameplay; 3-point smileyometer responses: 16/16 happy/okay, 15/16 liked characters, 16/16 want to play again; 9 of 12 learners passed the speech-recognition Say It challenge on the first attempt).
  * **5 Parents / Supervising Adults** (evaluated home usability, safety, and offline usefulness; overall SUS usability score of 75.5/100).
  * **9 Teachers** (evaluated pedagogical soundness, DepEd Marungko order alignment, and classroom utility across rounds, with the 5 Round 2 educators evaluating the final 16-item rubric achieving an overall mean rating of 4.72 / 5.0).
  * **0 Technical Evaluators** (`technical = 0`).

> [!IMPORTANT]
> **Strict Reporting Constraints**:
> 1. Always distinguish learners from evaluators: children were observed and answered smileyometer feedback; parents and teachers evaluated usability and teaching quality as "the bridge to the child".
> 2. Never refer to "30 users" or call the children "respondents". Use "30 participants" with the explicit breakdown.
> 3. Never invent, round up, or adjust figures.
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
* **Scope & Delimitations**: Focus on initial letter sounds (starting with phoneme /m/ following DepEd's Marungko sequence) on Android smartphones; offline standalone operation.

### 3.3 2. Related Literature & Studies
* Synthetic phonics vs. whole-language approaches in early childhood education.
* Mother Tongue-Based Multilingual Education (MTB-MLE) and DepEd's Marungko approach.
* Mobile game-based learning and interactive feedback mechanisms for young children.
* **Research Gap**: Scarcity of culturally localized, offline-first synthetic phonics mobile apps tailored for early Filipino learners in resource-constrained environments.

### 3.4 3. Methodology
* **Development Approach**: Agile/Iterative prototyping with progressive user-informed refactoring.
* **Evaluation Framework**: Integration of ISO/IEC 25010 software quality characteristics (Usability, Functional Suitability, Performance Efficiency) and early pedagogical rubrics.
* **Participants & Setting**: Detailed profile of the 30 participants (16 Grade 1 pupils, 5 parents, 9 teachers) conducted under ethical clearance and school permission.
* **Instruments**: Direct task observation protocol, 3-point smileyometer, Parent Usability Survey, and DepEd Teacher Pedagogical Evaluation checklist.
* **Data Analysis Procedures**: Descriptive statistics, task completion frequency analysis, and qualitative feedback categorization.

### 3.5 4. System Design & Implementation
* **System Architecture**: Native Android architecture, offline asset pipeline, local SQLite/SharedPreferences progress storage.
* **Pedagogical Engine (The 4 Steps)**:
  1. *Sound It (Hear It)*: Phoneme isolation and auditory modeling.
  2. *Find It*: Visual-auditory grapheme identification.
  3. *Blend It*: CVC blending mechanics.
  4. *Say It*: Speech recognition and pronunciation practice.
* **Mascot Co-player Design**: Lily the Philippine tarsier acting as an empathetic, encouraging companion.
* **Audio & Visual Design**: Flat, accessible visual language, high-contrast text, authentic voice assets.

### 3.6 5. Results & Evaluation
* **Actual Learner Observations**: Task completion metrics (e.g. 75% first-pass rate on speech recognition), high engagement, and 100% positive/acceptable smileyometer ratings.
* **Parent Usability Evaluation**: Strong satisfaction regarding safety, zero internet requirement, and independent home play (mean 75.5/100).
* **Teacher Pedagogical Evaluation**: Strong endorsement for Marungko order alignment and instructional appropriateness (mean 4.72/5.0).
* **Software Verification**: 691 passing unit tests, zero critical crashes, low battery/CPU overhead on entry-level Android devices.

### 3.7 6. Discussion
* Synthesis of technical performance and educational feedback.
* Fulfillment of pre-approved SMART objectives.
* Analysis of identified edge cases: acoustic variations in children's speech, background noise handling, and letter-pacing considerations.

### 3.8 7. Conclusion & Future Work
* Summary of contributions: A verified, free, offline digital literacy tool bridging home and classroom learning.
* Remaining limitations: Limited phoneme coverage in the initial prototype.
* Future roadmap: Expansion across the full 28-letter Marungko curriculum, teacher management dashboards, and longitudinal studies tracking reading gains over an entire school semester.

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

> **Note on Folder Numbering**: If the adviser evaluates under the 5-folder Capstone Final Submission layout (`01_Software_and_Deployment` through `05_Research_Paper`) or the 7-category TekCon layout, all corresponding folders and shortcuts are in place inside `G56`.

---

## 5. Working Rules for Claude

1. **Git Branch**: Keep all work and documentation on branch `erik/research-paper-final`.
2. **Never Modify Existing Source Files**: Do not modify app code, Gradle configs, or frozen json data files. Only create and update the documentation, paper drafts, and submission guides.
3. **Google Drive Scoping**: Always modify files inside `G56/` on Google Drive. **Never touch or edit the shared links for `part9_Validation` or `PlayIT_MIDTERM DELIVERABLE`**.
4. **Verification**: If editing any poster or pitch files, run `cd tools && npm run build` and ensure `0 failed`.
