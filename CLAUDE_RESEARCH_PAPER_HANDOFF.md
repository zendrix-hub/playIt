# PlayIT Research Paper & User Validation Final Handoff

> **Course**: IT411 — Capstone & Research 2 / Software Engineering 2  
> **Team Code**: `G56` (Group 56)  
> **Project Name**: PlayIT — Offline Synthetic Phonics Mobile Application for Grade 1 Filipino Learners  
> **Repository Branch**: `erik/research-paper-final`  
> **Google Drive Working Folder**: `G56` ([Drive ID: `1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc`](https://drive.google.com/drive/folders/1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc))  
> **Collaborator**: Erik Samuel Bien (`thatguyKire`)  
> **Lead Researcher**: Zendrix Riva (`rivazendrix@gmail.com`)  
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
* **16 Grade 1 Learners ($n=16$)**: Primary target end-users (ages 6–7, IDs `C-1` to `C-16`). Evaluated via **direct observation of interactive gameplay** (*Hear It, Find It, Blend It, Say It*) and a **3-point smileyometer visual scale**:
  * *How fun was the game?*: 15 😄 Happy, 1 🙂 Okay, 0 😐 Sad (**16/16 positive/acceptable**).
  * *Did you like the characters?*: 15 😄 Happy, 1 🙂 Okay, 0 😐 Sad (**15/16 top rating**).
  * *Would you play it again?*: 14 😄 Happy, 2 🙂 Okay, 0 😐 Sad (**16/16 positive**).
  * *Was it easy to play?*: 9 😄 Happy, 6 🙂 Okay, 1 😐 Neutral (**15/16 manageable**).
  * *Say It Speech Recognition Task*: **9 of 12 learners** passed the speech recognition pronunciation challenge on the first try (75% first-pass completion).
* **5 Parents / Supervising Adults ($n=5$)**: Home evaluators and "the bridge to the child" who oversee mobile phone usage at home:
  * **System Usability Scale (SUS) Score**: **75.5 / 100** (Grade B+ / Above Average Usability).
  * *Grade 1 Developmental Fit*: **4.60 / 5.0**.
  * *Willingness to Recommend*: **4.40 / 5.0**.
  * *Offline Benefit & Safety*: 5/5 unanimous confirmation of safety (no ads, zero internet permission required).
* **9 Teachers & Reading Specialists ($n=9$)**: Professional early literacy educators evaluating pedagogical validity and DepEd curriculum alignment:
  * *Round 1 Preliminary Validation ($n=4$)*: 11 of 12 items received unanimous "Yes" agreement.
  * *Round 2 Final Likert Rubric ($n=5$)*: Evaluated a rigorous 16-item rubric covering Marungko progression, phoneme articulation, scaffolding, speech fairness, and DepEd Order 8 progress metrics.
  * **Composite Mean Rating**: **4.72 / 5.0** (15 of 16 items rated 4.0 or higher by all educators).
* **0 Technical Evaluators (`technical = 0`)**: All evaluations focused strictly on user experience, pedagogy, and stakeholder utility.

> [!CAUTION]
> **Strict Ethical & Methodological Rules**:
> 1. **Differentiate Roles**: Children were *observed playing* and rated engagement via smiley faces. Parents and teachers were *evaluators* rating usability and curriculum alignment.
> 2. **Terminology**: Never write "30 users" or call children "respondents". Write **"30 participants (16 Grade 1 learners, 5 parents, 9 teachers)"**.
> 3. **No Fabricated Learning Gains**: Do **not** claim children "learned to read better" or had increased reading scores. The study measured engagement, usability, task completion, and stakeholder acceptance—not longitudinal literacy gains.
> 4. **Exact Figures**: Never adjust, round up, or alter figures.

---

## 3. Deliverable 1: User Validation (Individual Submission)

The following 5 comprehensive deliverables must be prepared and organized inside `05_Final_User_Validation_and_Evaluation/` (or `04_Final-User_Validation/`):

### 3.1 1. Final User/Stakeholder Validation Plan
* **Objectives & Research Questions**:
  * *RQ1*: To what extent does PlayIT adhere to the DepEd Marungko synthetic phonics sequence as evaluated by Grade 1 teachers?
  * *RQ2*: What are the task completion rates and engagement levels of Grade 1 learners during offline interactive phonics activities?
  * *RQ3*: How usable and acceptable is PlayIT for home-based practice as perceived by parents/guardians?
* **Target Respondents**: Clear demographic profiling of the 16 pupils, 5 parents, and 9 teachers, justifying their selection.
* **Validation Model / Framework**: ISO/IEC 25010 Software Quality Model (Usability, Functional Suitability, Performance Efficiency) integrated with Early Childhood Education (ECE) pedagogical evaluation rubrics.
* **Evaluation Instruments & Procedures**: Detailed protocol for direct classroom observation, parental home trials, and structured educator surveys.
* **Metrics, Success Criteria & Analysis Methods**: Pre-approved SMART criteria matrix (e.g., SUS &ge; 70, Teacher Mean &ge; 4.0/5.0, Learner Positive Engagement &ge; 80%).

### 3.2 2. Validation Instruments and Data Collection
* **Survey Questionnaires**: Official standardized instruments (Parent SUS questionnaire, 16-item Teacher Marungko Pedagogical Rubric).
* **User Task Scenarios & Protocols**: Step-by-step learner testing script covering *Hear It &rarr; Say It &rarr; Find It &rarr; Blend It*.
* **Interview Guides & Stakeholder Feedback Forms**: Open-ended feedback capture for teachers and parents.
* **Completed Session Records**: Direct links to raw responses (`Master (Responses)`) and standardized codebook (`Cleaned Master (Responses)`).

### 3.3 3. Actual User/Stakeholder Testing Evidence
* **Task Completion Rates & Time-on-Task**: Evidence of 9/12 first-pass speech recognition completion, sound-isolation progression, and session duration.
* **Standardized Questionnaire Results**: Item-by-item breakdown of parent SUS scores (75.5/100) and accessibility checks (text size, audio narration, tap controls).
* **Perceived Usefulness & Ease of Use**: Teacher evaluation of DepEd Order 8 progress tracking (4.8/5.0) and A4 PDF Class Record generation (4.6/5.0).
* **User Satisfaction & Qualitative Feedback**: Smileyometer distribution charts and direct stakeholder quotes:
  * *"The app is very useful and engaging for Grade 1 learners... interactive and child-friendly."* &mdash; DepEd Teacher (T-1).
* **Photographic Testing Evidence**: Links to photo documentation in `05_Validation_Evidence/` showing 16 learners engaged with the app and signed school permissions.

### 3.4 4. Final Validation Results and Analysis
* **Respondent Profile**: Statistical breakdown of the 30 participants with setting context (Masbate City Division, MTB-MLE context).
* **Validation Procedure & Timeline**: Two-stage evaluation chronology (Round 1: Sept 16–23, 2026; Round 2: Nov 10–12, 2026).
* **Results per Research Question & Metric**: In-depth analytical narrative with cross-tabulated tables verifying each SMART goal.
* **Identified Usability Issues & Limitations**:
  * Edge cases in speech recognition acoustic sensitivity with high-pitch child voices.
  * Distinction between letter name (/em/) vs. pure phonetic sound (/m/).
* **Achievement of System Objectives**: Objective verification demonstrating that all 4 learning steps, offline persistence, and parent progress reporting operate without internet access.

### 3.5 5. Recommendations and Post-Validation Improvements
* **Issues & Concerns Raised**: Teacher feedback on adding more letters beyond /m/ and parental requests for pronunciation guides.
* **Prioritized Improvement Backlog**:
  1. *Priority 1*: Expand letter library across all 28 Marungko sounds.
  2. *Priority 2*: Dynamic threshold calibration for child speech recognition.
  3. *Priority 3*: Exportable multi-pupil classroom analytics.
* **Actions Taken**: Documenting code refactorings between MVP and release (pure phoneme audio replacement, Roborazzi UI tests, Room database optimization).
* **Required Documentation Updates**: Traceability updates to SRS v3.0, SDD v2.0, and STD v1.0.
* **Future Roadmap**: Recommendation for formal longitudinal reading intervention studies over a full academic semester.

---

## 4. Deliverable 2: Research Paper Draft & Final Manuscript

The paper must strictly follow the official **ACM Conference Proceedings Template** (`.docx` or LaTeX) with the following structure:

### 4.1 Section Outline & Requirements

* **01 Title, Authors, and Abstract**:
  * **Title**: *PlayIT: An Offline-First Gamified Synthetic Phonics Mobile Application for Early Literacy Development among Grade 1 Filipino Learners*
  * **Authors**: Zendrix Riva (Lead), Erik Samuel Bien, Ashlee Skye Durano, Kaysean Miel, John James Palis.
  * **Adviser**: Mr. Joemarie C. Amparo.
  * **Affiliation**: Department of Computer Science / College of Computer Studies, Cebu Institute of Technology – University.
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
  * Research gap: Absence of offline, open-source, speech-interactive mobile phonics tools aligned with DepEd Grade 1 standards.

* **04 Methodology**:
  * Iterative Agile/Scrum development lifecycle with user-driven refactoring.
  * System evaluation model: ISO/IEC 25010 software quality characteristics combined with ECE instructional rubrics.
  * Participant cohort: 30 participants (16 Grade 1 learners, 5 parents, 9 teachers).
  * Instruments & data collection procedures: Direct observation, 3-point smileyometer, Parent SUS, Teacher Marungko Rubric.
  * Statistical and qualitative analysis techniques.

* **05 System Design and Implementation**:
  * Android architecture: Kotlin, Jetpack Compose, MVVM pattern, Room persistence, Vosk offline ASR engine.
  * The 4 Pedagogical Steps:
    1. *Hear It*: Pure phoneme pronunciation modeling with synchronized mouth-shape articulation visual cues.
    2. *Say It*: Offline speech recognition with encouraging, non-punitive feedback.
    3. *Find It*: Visual-auditory grapheme identification with dynamic distractors.
    4. *Blend It*: CVC blending mechanics with connected phonation rendering.
  * Mascot co-player: Lily the Philippine Tarsier providing emotional scaffolding.
  * Architecture refactoring highlights from MVP to Release (INC-01 to INC-07 defect resolution).

* **06 Results and Evaluation**:
  * Automated QA & verification: 691 unit tests (100% pass), 84 Roborazzi visual regression checks, touch-target compliance (&ge; 64 dp).
  * Learner observations: 16/16 positive engagement; 75% first-pass speech recognition completion.
  * Parent evaluation: SUS score of 75.5 / 100; high ratings for home safety and offline independence.
  * Teacher evaluation: Composite mean of 4.72 / 5.0 across 16 Marungko alignment criteria.

* **07 Discussion**:
  * Triangulation of technical performance, learner engagement, and educator endorsement.
  * Fulfillment of approved SMART objectives.
  * Technical insights: Offline speech recognition acoustic modeling for young voices, handling ambient noise in classroom settings.

* **08 Conclusion and Future Work**:
  * Synthesis of project success: A fully functional, zero-data synthetic phonics app validated by key stakeholders.
  * Scope limitations: Initial implementation covers phoneme /m/ and Chapter 1 letter progressions.
  * Future roadmap: Full 28-letter DepEd curriculum coverage, multi-child teacher dashboard, and longitudinal reading gain evaluations.

* **09 Acknowledgments and References**:
  * Acknowledgment of school administration, teachers, parents, and participating pupils.
  * Complete bibliographic references adhering strictly to **ACM citation style**.

---

## 5. File Location & Google Drive Tree (`G56`)

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
3. **Generate Complete Manuscripts**: When generating the Research Paper draft, write full, polished academic prose with proper ACM headings, in-text citations (`[1]`, `[2]`), and formal tables—do not leave placeholders or bullet-point outlines.
4. **Google Drive Syncing**: Save or sync generated files to the `G56` folder on Google Drive.
5. **Git Hygiene**: Commit all drafts and handoff updates to `erik/research-paper-final` with descriptive commit messages.
