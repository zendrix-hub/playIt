# PlayIT Research Paper & User Validation Final Handoff

> **Course**: IT411 — Capstone & Research 2 / Software Engineering 2  
> **Team Code**: `G56` (Group 56)  
> **Project Name**: PlayIT — an offline phonics app for Grade 1 Filipino learners (it teaches the sounds of the 26 English letters in Marungko order)  
> **Repository Branch**: `erik/research-paper-final`  
> **Google Drive Working Folder**: `G56` ([Drive ID: `1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc`](https://drive.google.com/drive/folders/1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc))  
> **Collaborator**: Erik Samuel Bien (`thatguyKire`)  
> **Lead Researcher**: Zendrix Riva  
> **Adviser**: Mr. Joemarie C. Amparo  
> **CRITICAL DIRECTIVE**: Never modify original shared links (`part9_Validation` or `PlayIT_MIDTERM DELIVERABLE`). All file operations, additions, and updates must take place **strictly inside the team's `G56` folder on Google Drive**.

---

## 1. Overview & Scope of Work

This handoff provides the comprehensive specification for Claude Code to execute two major individual/team Capstone 2 deliverables:
1. **User/Stakeholder Validation Deliverables (Weeks 11–12)** — Full 5-part validation documentation and evidence package.
2. **Research Paper Draft & Final Manuscript (Weeks 13–14)** — Complete conference manuscript following the prescribed **ACM Conference Proceedings format**.

All numbers, evaluation tables, and claims must draw strictly from the **verified frozen survey dataset of 30 participants** ($N=30$: 16 Grade 1 learners, 5 parents/supervising adults, 9 teachers/reading specialists).

---

## 2. Verified Survey Dataset ($N=30$ Frozen / Actual)

The evaluation results are frozen and documented in `round2_data.json` and the Google Sheets workbooks (`Cleaned Master (Responses)` and `Master (Responses)`):

### 2.1 Participant Cohort Breakdown
* **16 Grade 1 Learners ($n=16$)**: Primary target end-users (ages 6–7, IDs `C-1` to `C-16`). Observed while playing (*Hear It, Say It, Find It, Blend It*), then asked four smiley-face (smileyometer) questions. The counts below are the frozen ones in `round2_data.json`; report them as counts with the group size, not as percentages:
  * *How fun was the game?*: **15 of 16** chose the happy face, 1 okay, 0 sad.
  * *Did you like the characters?*: **15 of 16** happy, 1 okay.
  * *Would you play it again?*: **14 of 16** happy, 2 okay.
  * *Was it easy to play?*: **9 of 16** happy, 6 okay, 1 neutral. This is the weakest learner result; say so.
  * **No per-step results exist.** The Say It first-try count, Find It and Blend It tallies and time on task were never recorded (`null` in `round2_data.json`). "75% first-pass" was the planned SMART *target* in the validation framework, and "9 of 12" is a formatting example in `AGENTS.md`. Neither is a result, and neither may appear as one.
* **5 Parents / Supervising Adults ($n=5$)**: Home evaluators and "the bridge to the child" who oversee mobile phone usage at home:
  * **System Usability Scale (SUS) Score**: **75.5 / 100** (n = 5; SD 14.62; 95% CI 57.3 to 93.7). Above the commonly cited average of 68 and at the SMART target of 75, but with five respondents neither difference is statistically significant (`docs/validation-package/04_MVP_VALIDATION_HIGHLIGHTS.md`). A "B" on the Sauro-Lewis scale.
  * *Grade 1 Developmental Fit*: **4.60 / 5.0**.
  * *Willingness to Recommend*: **4.40 / 5.0**.
  * *Accessibility checklist (yes out of 5)*: large text 5, audio narration 5, simple touch 5, colour contrast 5, Grade 1 language 5, motor accommodation 4, hearing accommodation 3. No survey item asked about offline benefit or safety, and the Round 2 parent questions (usefulness, ease of use, intention to use) were not collected: there are no figures for them.
* **9 Teachers & Reading Specialists ($n=9$)**: Professional early literacy educators evaluating pedagogical validity and DepEd curriculum alignment:
  * *Round 1 checklist ($n=4$)*: 11 of 12 yes/no items answered yes by all four. The twelfth, correct letter sounds, was flagged by 2 of 4; this led to the pure-phoneme audio fix.
  * *Round 2 draft rubric ($n=4$, 18 items)*: overall mean 3.81; 4.74 with straight-1 responses excluded.
  * *Round 2 Final Likert Rubric ($n=5$)*: Evaluated a rigorous 16-item rubric covering Marungko progression, phoneme articulation, scaffolding, speech fairness, and DepEd Order 8 progress metrics.
  * **Composite Mean Rating**: **4.72 / 5.0**, from **5 of the 9 teachers** (always give that group size); all five rated 4 or higher on 15 of 16 items.
* **30 participants, not 34.** The forms hold 34 responses (16 + 5 + 13 teacher responses across three forms). Teachers who answered more than one form are counted once: up to 9 teachers, 30 participants.
* **0 Technical Evaluators (`technical = 0`)**: All evaluations focused strictly on user experience, pedagogy, and stakeholder utility.

> [!CAUTION]
> **Strict Ethical & Methodological Rules**:
> 1. **Differentiate Roles**: Children were *observed playing* and rated engagement via smiley faces. Parents and teachers were *evaluators* rating usability and curriculum alignment.
> 2. **Terminology**: Never write "30 users" or call children "respondents". Write **"30 participants (16 Grade 1 learners, 5 parents, 9 teachers)"**.
> 3. **No Fabricated Learning Gains**: Do **not** claim children "learned to read better" or had increased reading scores. The study measured reactions, usability and stakeholder acceptance. It did not measure task completion or learning over time.
> 4. **Exact Figures**: Never adjust, round up, or alter figures. If a figure is not in `round2_data.json` or `docs/validation-package/04_MVP_VALIDATION_HIGHLIGHTS.md`, it was not measured: say so. The corrected tables are in `docs/submission/E_TESTING_AND_QA_SURVEY_SYNTHESIS_30_PARTICIPANTS.md`.
> 5. **Limits to state**: small convenience sample; one facilitator recorded 13 of the 16 child ratings and young children tend to choose the happiest face; no technical evaluators took part.

---

## 3. Deliverable 1: User Validation (Individual Submission)

The following 5 comprehensive deliverables must be prepared and organized inside `05_Final_User_Validation_and_Evaluation/` (or `04_Final-User_Validation/`):

### 3.1 1. Final User/Stakeholder Validation Plan
* **Objectives & Research Questions**:
  * *RQ1*: To what extent does PlayIT adhere to the DepEd Marungko synthetic phonics sequence as evaluated by Grade 1 teachers?
  * *RQ2*: How do Grade 1 learners react to the app after playing (enjoyment, liking of the characters, wish to play again, perceived ease)? Task completion rates were planned but not recorded, so RQ2 can only be answered for reactions.
  * *RQ3*: How usable and acceptable is PlayIT for home-based practice as perceived by parents/guardians?
* **Target Respondents**: Clear demographic profiling of the 16 pupils, 5 parents, and 9 teachers, justifying their selection.
* **Validation Model / Framework**: the three frameworks of the approved plan (`docs/validation-package/01_VALIDATION_FRAMEWORK_AND_SMART_MAPPING.md`): ISO 9241-11 usability, the Usability-Pedagogy-Accessibility (UPA) framework and the Technology Acceptance Model, with ISO/IEC 25010 for the software checks.
* **Evaluation Instruments & Procedures**: Detailed protocol for direct classroom observation, parental home trials, and structured educator surveys.
* **Metrics, Success Criteria & Analysis Methods**: the approved SMART matrix, with each target marked met, not met or **not measured**: task completion at least 85% and Say It first-pass at least 75% (not measured); SUS at least 75 (75.5, n = 5); TAM usefulness at least 4.2 and intention at least 4.0 (not collected); teachers at least 4.0 on every item (final rubric: 15 of 16 items, mean 4.72); offline persistence and report time by 3 technical evaluators (none took part).

### 3.2 2. Validation Instruments and Data Collection
* **Survey Questionnaires**: Official standardized instruments (Parent SUS questionnaire, 16-item Teacher Marungko Pedagogical Rubric).
* **User Task Scenarios & Protocols**: Step-by-step learner testing script covering *Hear It &rarr; Say It &rarr; Find It &rarr; Blend It*.
* **Interview Guides & Stakeholder Feedback Forms**: Open-ended feedback capture for teachers and parents.
* **Completed Session Records**: Direct links to raw responses (`Master (Responses)`) and standardized codebook (`Cleaned Master (Responses)`).

### 3.3 3. Actual User/Stakeholder Testing Evidence
* **Task Completion Rates & Time-on-Task**: not recorded. State that plainly; do not write figures.
* **Standardized Questionnaire Results**: Item-by-item breakdown of parent SUS scores (75.5/100) and accessibility checks (text size, audio narration, tap controls).
* **Perceived Usefulness & Ease of Use**: Teacher evaluation of DepEd Order 8 progress tracking (4.8/5.0) and A4 PDF Class Record generation (4.6/5.0).
* **User Satisfaction & Qualitative Feedback**: Smileyometer distribution charts and direct stakeholder quotes:
  * *"The app is very useful and engaging for Grade 1 learners... interactive and child-friendly."* &mdash; DepEd Teacher (T-1). The four Round 1 teacher comments are in `docs/validation-package/04_MVP_VALIDATION_HIGHLIGHTS.md`, section 5; quote them word for word, including T-1's and T-2's criticism of the letter sounds. No parent quotation is on record.
* **Photographic Testing Evidence**: Links to the photo documentation in `05_Validation_Evidence/`. Describe only the consents and permissions the folder actually holds.

### 3.4 4. Final Validation Results and Analysis
* **Respondent Profile**: Breakdown of the 30 participants. Round 1 was a convenience sample at classroom, home and community sites. Take the school, division and any other setting detail from the response sheets or the team lead; the repository does not record them.
* **Validation Procedure & Timeline**: Two rounds: Round 1, 16 to 23 September 2026; Round 2 (teacher rubrics), November 2026. Take the exact Round 2 dates from the response sheets.
* **Results per Research Question & Metric**: In-depth analytical narrative with cross-tabulated tables verifying each SMART goal.
* **Identified Usability Issues & Limitations**:
  * Round 1 findings on record: letter sounds that sounded like the letter name ("ma" for /m/), flagged by 2 of 4 teachers; children hesitating at the microphone; hearing accommodation rated yes by 3 of 5 caregivers.
  * Not yet measured: the speech judge's agreement with a teacher on children's voices, and latency on the device.
* **Achievement of System Objectives**: What the tests and field sessions show: the lesson steps and the parent report work with no internet permission in the build; 697 automated tests (691 passed, 6 skipped by design, 0 failed). Offline persistence on the device (NFR-REL-01) and latency were not run.

### 3.5 5. Recommendations and Post-Validation Improvements
* **Issues & Concerns Raised**: from the Round 1 comments: pure letter sounds (T-1, T-2), a listening indicator on the microphone, visual cues for hearing difficulty, a track for more advanced readers (T-2), a progress report for teachers.
* **Prioritized Improvement Backlog**:
  1. *Priority 1*: Finish the teacher audit of the letter sounds (several chapters still play MVP audio).
  2. *Priority 2*: Measure the speech judge against teachers on children's voices (STD TC-ASR-01 to 06) and fix DEF-01 and DEF-02.
  3. *Priority 3*: Telemetry and CSV export, spaced recall, session pacing (planned, not built).
  The app already covers all 26 letters; "more letters" is not an open item.
* **Actions Taken**: the changes on record between the MVP and the tested build: pure-phoneme audio, mouth-shape cues, the listening state on the microphone, 64 dp targets, and the seven closed incidents INC-01 to INC-07 (STD 7.1).
* **Required Documentation Updates**: Traceability updates to SRS v3.0, SDD v2.0, and STD v1.0.
* **Future Roadmap**: Recommendation for formal longitudinal reading intervention studies over a full academic semester.

---

## 4. Deliverable 2: Research Paper Draft & Final Manuscript

The paper must strictly follow the official **ACM Conference Proceedings Template** (`.docx` or LaTeX) with the following structure:

### 4.1 Section Outline & Requirements

* **01 Title, Authors, and Abstract**:
  * **Title**: *PlayIT: An Offline-First Gamified Synthetic Phonics Mobile Application for Early Literacy Development among Grade 1 Filipino Learners* (proposed here; the approved project title is *PlayIT: An Offline-First Gamified Early Literacy Mobile Application Using the DepEd Marungko Approach*. Confirm with the adviser which one the paper carries.)
  * **Authors**: Zendrix Riva (Lead), Erik Samuel Bien, Ashlee Skye Durano, Kaysean Miel, John James Palis.
  * **Adviser**: Mr. Joemarie C. Amparo.
  * **Affiliation**: College of Computer Studies, Cebu Institute of Technology – University (BS Information Technology).
  * **Abstract**: ~150–200 words summarizing the Philippine early literacy crisis, the offline-first design rationale, the 4-step Marungko pedagogy, Lily mascot mechanics, and empirical outcomes across the 30 participants.
  * **Keywords**: Synthetic Phonics, Mobile Assisted Language Learning (MALL), Offline EdTech, Marungko Approach, Early Childhood Literacy, Android.

* **02 Introduction**:
  * Early childhood literacy challenges in the Philippines; DepEd National Learning Recovery Program (NLRP); phonemic awareness deficits in early grades.
  * The digital divide: Lack of reliable internet connectivity in public school households.
  * Proposed solution: PlayIT as a zero-data, culturally localized, standalone phonics application.
  * Research objectives, significance, and contributions.

* **03 Related Literature and Studies**:
  * Theoretical foundation: Synthetic phonics vs. whole-language; Ehri's phase theory of word reading; Gonzalez-Frey & Ehri (2021) on connected phonation.
  * DepEd's Marungko approach (starting with m, s, a, i...) and Mother Tongue-Based Multilingual Education (MTB-MLE).
  * Mobile game-based learning, mascot co-player psychology, and immediate corrective feedback.
  * Research gap: among the apps the team reviewed, none combines offline use, Marungko letter order and on-device speech checking. (The repository is public but carries no open-source licence; do not call PlayIT open-source.)

* **04 Methodology**:
  * Iterative development with refactoring after each validation round (describe the process as the SPMP records it).
  * System evaluation model: ISO 9241-11, the UPA framework and TAM from the approved validation plan; ISO/IEC 25010 for the software checks.
  * Participant cohort: 30 participants (16 Grade 1 learners, 5 parents, 9 teachers).
  * Instruments & data collection procedures: observation of play, the smileyometer, the caregiver SUS and accessibility checklist, the teacher checklist and rubrics.
  * Statistical and qualitative analysis techniques.

* **05 System Design and Implementation**:
  * Android architecture: Kotlin, Jetpack Compose, MVVM pattern, Room persistence, Vosk offline ASR engine.
  * The 4 Pedagogical Steps:
    1. *Hear It*: Pure phoneme pronunciation modeling with synchronized mouth-shape articulation visual cues.
    2. *Say It*: Offline speech recognition with encouraging, non-punitive feedback.
    3. *Find It*: five pictures; the child taps the three whose names start with the sound.
    4. *Blend It*: at the end of each chapter, the child builds words from letter tiles that are sounded out one by one.
  * Mascot co-player: Lily the Philippine Tarsier providing emotional scaffolding.
  * Architecture refactoring highlights from MVP to Release (INC-01 to INC-07 defect resolution).

* **06 Results and Evaluation**:
  * Automated QA & verification: 697 tests (691 passed, 6 skipped by design, 0 failed); a layout matrix of 23 checks on 4 screen profiles (86 passed, 6 skipped); touch targets of at least 64 dp.
  * Learner reactions (n = 16): 15 of 16 happy face; 15 of 16 liked the characters; 14 of 16 would play again; 9 of 16 found it easy. No task-completion figure exists.
  * Parent evaluation (n = 5): SUS 75.5 / 100; Grade 1 fit 4.6; would recommend 4.4; accessibility checklist.
  * Teacher evaluation: mean 4.72 / 5.0 on the 16-item final rubric, from 5 of the 9 teachers.

* **07 Discussion**:
  * Triangulation of technical performance, learner engagement, and educator endorsement.
  * The approved SMART objectives one by one: met, not met, or not measured.
  * Limits of the evidence: small convenience sample, reactions and expert opinion rather than learning, facilitator effects in the child ratings, speech-judge accuracy and latency not yet measured.

* **08 Conclusion and Future Work**:
  * Synthesis of project success: A fully functional, zero-data synthetic phonics app validated by key stakeholders.
  * Scope limitations: those in the README's Known Limitations. The app covers all 26 letters in 7 chapters; it is not limited to /m/ or Chapter 1.
  * Future roadmap: the teacher audit of the sounds, the speech-judge agreement study, telemetry and spaced recall, and a study of reading gains over a school term.

* **09 Acknowledgments and References**:
  * Acknowledgment of the teachers, parents and pupils who took part (by role, not by name).
  * Complete bibliographic references adhering strictly to **ACM citation style**.

---

## 5. File Location & Google Drive Tree (`G56`)

> Folder numbers and file names differ between this tree, `CLAUDE_HANDOFF.md` and the instructor's 11 November layout. Confirm the instructor's latest announcement with the team lead before renaming or moving anything on Drive.

All completed files must be placed in their corresponding directories on Google Drive:

```text
G56/ (1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc)
│
├── 01_Software_and_Deployment/ (or 02_Software_and_Deployment/)
│   ├── A_SOFTWARE_FUNCTIONALITY_AND_COMPLETION.md
│   ├── B_DEPLOYMENT_AND_SOURCE_CODE_REPOSITORY.md
│   └── Deployed APKs & setup instructions
│
├── 04_Testing_and_Quality_Assurance/
│   ├── E_TESTING_AND_QA_SURVEY_SYNTHESIS_30_PARTICIPANTS.md
│   └── Test execution evidence & reports
│
├── 05_Final_User_Validation_and_Evaluation/ (or 04_Final-User_Validation/)
│   ├── 01_User_Validation_Plan/
│   ├── 02_Instruments_and_Data_Collection/
│   ├── 03_Testing_and_Evaluation_Evidence/
│   ├── 04_Results_and_Analysis/
│   ├── 05_Recommendations_and_Improvements/
│   └── E_TESTING_AND_QA_SURVEY_SYNTHESIS_30_PARTICIPANTS.md
│
├── 06_Research_Paper/ (or 05_Research_Paper/)
│   ├── 01_Research_Paper_Draft/ (Week 13 complete draft in ACM format)
│   ├── 02_Final_Editable_ACM_Manuscript/ (Week 14 editable .docx / LaTeX)
│   ├── 03_Final_PDF_Copy/ (G56_Research_Paper_FINAL.pdf)
│   └── RESEARCH_PAPER_SUBMISSION_CHECKLIST.md
│
└── 07_TechCon_2026/
    ├── 01_Research_Poster/ (G56_Research_Poster.pdf, G56_Research_Poster_Source.pptx)
    ├── 02_Video_Pitch/ (G56_2-Minute_Pitch.mp4)
    └── 03_Software_Product_Logo/ (G56_Product_Logo_2000x2000.png, G56_Product_Logo_512x512.png)
```

---

## 6. Execution Instructions for Claude

1. **Read Existing Sources**: Before writing, inspect `docs/submission/A_SOFTWARE_FUNCTIONALITY_AND_COMPLETION.md`, `docs/submission/E_TESTING_AND_QA_SURVEY_SYNTHESIS_30_PARTICIPANTS.md`, `round2_data.json`, and `playIT_SRS_v3.0.pdf` for exact architecture details and quotes.
2. **Never Edit Frozen Data**: Do not change `round2_data.json` or invent new respondent counts.
3. **Generate Complete Manuscripts**: When generating the Research Paper draft, write full, polished academic prose with proper ACM headings, in-text citations (`[1]`, `[2]`), and formal tables—do not leave placeholders or bullet-point outlines. Cite only sources whose details have been checked; do not invent references.
4. **Google Drive Syncing**: Save or sync generated files to the `G56` folder on Google Drive.
5. **Git Hygiene**: Commit all drafts and handoff updates to `erik/research-paper-final` with descriptive commit messages.
