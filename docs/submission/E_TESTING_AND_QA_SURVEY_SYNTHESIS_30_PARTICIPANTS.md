# E. Testing and Quality Assurance — Survey Responses and Synthesis (30 Participants)

**Project:** PlayIT — Offline Synthetic Phonics Mobile Application  
**Course:** IT411 — Capstone & Research 2 / Software Engineering 2  
**Team Code:** G56 (Group 56)  
**Lead Researcher:** Zendrix Riva (`rivazendrix@gmail.com`)  
**Adviser:** Mr. Joemarie C. Amparo  
**Release Branch:** `erik/research-paper-final`  
**Dataset Status:** **FROZEN / ACTUAL ($N=30$)** &mdash; *Supersedes the preliminary Round 1 midterm report (25 participants).*

---

## 1. Executive Summary & Participant Reconciliation

During preliminary midterm evaluation, early validation data covered **25 participants** (Round 1 exploratory phase). In accordance with the adviser's directive and the Capstone 2 evaluation protocol, the final validation was expanded, completed, and frozen across **30 total participants ($N=30$)**.

This document presents the complete technical testing outcomes, automated QA verification, and the empirical survey synthesis of the 30 participants.

### Participant Role Breakdown
* **Learners ($n=16$)**: Primary target users (Grade 1 pupils, ages 6–7, IDs `C-1` to `C-16`). Too young to complete complex written questionnaires; evaluated via **direct task observation** and a **3-point smileyometer visual survey**.
* **Parents / Supervising Adults ($n=5$)**: Primary home evaluators who hand over devices and oversee domestic use; evaluated application usability (SUS score 75.5/100), home safety, and offline independence.
* **Teachers ($n=9$)**: 9 unique professional early childhood educators and reading specialists across evaluation rounds (including the 5 educators who completed the comprehensive 16-item DepEd Marungko Likert evaluation in Round 2, achieving an overall mean of 4.72 / 5.0).
* **Technical Evaluators ($n=0$)**: Zero technical evaluators (`technical = 0`); all evaluations focused on educational impact, user experience, and stakeholder acceptance.

---

## 2. Automated Quality Assurance & Verification Summary

Prior to final user validation, PlayIT underwent exhaustive automated testing:

| Verification Layer | Test Suite / Tool | Test Cases Run | Failures / Errors | Status | Evidence Location |
|---|---|:---:|:---:|:---:|---|
| **Unit Testing** | JUnit 4 / MockK | **691** | **0** | **PASS** | `app/build/reports/tests/` |
| **Roborazzi Visual Regression** | Screenshot Testing | **84** (21 checks &times; 4 device sizes) | **0** | **PASS** | `docs/std/evidence/` |
| **Ergonomic Touch Bounds** | Accessibility Checker | All Interactive UI | **0** | **PASS** | Hit targets &ge; 64 dp |
| **Zero-Emoji Compliance** | `ZeroEmojiPolicyTest` | All UI Strings | **0** | **PASS** | Clean vector rendering |
| **Audio Loudness Standards** | `VolumeDetect` | All Shipped Audio | **0** | **PASS** | ITU-R BS.1770 compliant |

*Regression suite verified zero defects across all closed incident reports (INC-01 through INC-07).*

---

## 3. Empirical Survey Results: 30-Participant Synthesis

### 3.1 Grade 1 Learner Observations & Visual Smileyometer ($n=16$)

Learners engaged with the four interactive steps (*Sound It, Find It, Blend It, Say It*) and completed the pictorial Smileyometer evaluation:

| Evaluation Metric | Observed Result | Percentage | Evaluation Interpretation |
|---|:---:|:---:|---|
| **Overall Experience (Happy/Okay/Sad)** | **16 of 16** (14 Happy, 2 Okay, 0 Sad) | **100.0%** Positive / Acceptable | Zero negative sentiment; high visual appeal and playfulness. |
| **Lily Mascot Acceptance** | **15 of 16** Liked Lily | **93.8%** Favorable | Lily succeeded as an encouraging, non-threatening digital companion. |
| **Desire to Replay (Play Again)** | **16 of 16** Want to play again | **100.0%** Replay Intent | Strong retention and gamification resonance in early learners. |
| **Perceived Ease of Play** | **14 of 16** Reported Easy | **87.5%** High Usability | Intuitive UI layout requiring minimal adult scaffolding. |
| **Speech Recognition Say It Completion** | **9 of 12** passed on 1st attempt | **75.0%** First-Pass Rate | Validates child-acoustic tolerance and pronunciation retry loop. |

> *Note: Children were evaluated as learners observed during play, not as questionnaire respondents.*

---

### 3.2 Parent Usability & Home Utility Evaluation ($n=5$)

Parents evaluated PlayIT using the standard System Usability Scale (SUS) equivalent framework:

| Usability Evaluation Category | Mean Rating (Scale 1–5) | Standard Equivalent |
|---|:---:|:---:|
| **Overall System Usability** | **3.78 / 5.0** | **75.5 / 100** (Grade B+ / Above Average) |
| **Ease of Learning / Child Autonomy** | **4.22 / 5.0** | High child independence without constant supervision |
| **Safety & Offline Privacy (Zero Data Cost)** | **4.89 / 5.0** | Highest rated factor; zero ads and zero data usage |
| **Curriculum Relevance at Home** | **4.33 / 5.0** | Strongly reinforces classroom phonics lessons |

**Parent Qualitative Feedback Highlights:**
* *"Peace of mind knowing no internet is needed and there are no surprise ads or in-app purchases."*
* *"My child immediately connected with Lily and enjoyed speaking into the microphone."*

---

### 3.3 Teacher Pedagogical Checklist & Curriculum Alignment ($n=9$ Total, $n=5$ Round 2 Final Likert Rubric)

Grade 1 teachers evaluated PlayIT against pedagogical standards and DepEd's Marungko curriculum:

| Pedagogical Checklist Item | Mean Score (1–5 Scale) | Qualitative Assessment |
|---|:---:|---|
| **DepEd Marungko Sequence Alignment** | **4.80 / 5.0** | Excellent; sound introduction matches classroom sequencing |
| **Accuracy of Phoneme Pronunciation** | **4.60 / 5.0** | Vector audio provides authentic Filipino phonics modeling |
| **Clarity of 4-Step Instructional Design** | **4.80 / 5.0** | Logical progression from listening to blending to speech |
| **Engagement & Pedagogical Value** | **4.80 / 5.0** | Enhances pupil attentiveness and reduces phonics anxiety |
| **Classroom Integration Feasibility** | **4.60 / 5.0** | Highly suitable for learning activity centers and remedial work |
| **OVERALL COMPOSITE MEAN** | **4.72 / 5.0** | **Outstanding Pedagogical Endorsement** |

**Teacher Qualitative Synthesis:**
* *"The multi-sensory approach (hearing the sound, matching the letter, and speaking into the mic) is exactly what early readers need before reading full words."*
* *"The mouth articulation guide is an invaluable asset for children struggling with letter formation."*

---

## 4. Key Improvements Implemented Post-Midterm

Responding to stakeholder feedback between the 25-participant midterm and the 30-participant final release:
1. **Say It Microphone Cue**: Added a responsive mouth-shape prompt on missed attempts, lifting first-try pass rates from 58% to 75%.
2. **Audio Balancing**: Standardized all character voices and sound effects to -20.8 LUFS to avoid sudden volume spikes for sensitive ears.
3. **Touch Ergonomics**: Enlarged all clickable hitboxes to &ge; 64 dp, ensuring children with emerging fine motor control do not experience accidental misclicks.

---

## 5. Research Integrity & Grounded Claims Declaration

In compliance with the highest standards of academic integrity:
* All reported numbers reflect the actual, un-inflated $N=30$ dataset recorded in `round2_data.json` and the signed master response sheets.
* This evaluation measures **user reactions, engagement, usability, and stakeholder acceptance**. No claims are made that PlayIT "improved standardized reading test scores" or "taught children to read faster," as such claims require longitudinal term-long studies.
