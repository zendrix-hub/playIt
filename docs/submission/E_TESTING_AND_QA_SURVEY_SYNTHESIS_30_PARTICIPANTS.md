# E. Testing and Quality Assurance: Survey Responses and Synthesis (30 Participants)

**Project:** PlayIT, an offline phonics app for Grade 1 learners  
**Course:** IT411 Capstone & Research 2 / Software Engineering 2  
**Team Code:** G56 (Group 56)  
**Lead Researcher:** Zendrix Riva  
**Adviser:** Mr. Joemarie C. Amparo  
**Branch:** `main`  
**Dataset status:** frozen, actual (N = 30). Source: `docs/tekcon-2026/round2_data.json`, compiled 12 November 2026 from the team's Google Sheet workbook.

Every figure in this document is copied from `round2_data.json` or from the test evidence in `docs/std/`. Where something was not measured, the document says so. It does not estimate.

---

## 1. Participants

Rounds 1 and 2 combined, with repeat respondents counted once, give **30 participants**. Round 1 alone had 25.

| Group | n | How they took part |
|---|:---:|---|
| Grade 1 learners | 16 | Observed while playing, then asked four smiley-face questions (Round 1, 16 to 21 September 2026) |
| Parents or supervising adults | 5 | System Usability Scale (SUS) and accessibility questionnaire (Round 1, 20 to 23 September 2026) |
| Teachers and reading specialists | 9 | Three forms across both rounds (see 3.3) |
| Technical evaluators | 0 | None |

**Why 30 and not 34.** The forms hold 34 responses: 16 learners, 5 parents, and 13 teacher responses (4 + 4 + 5 across the three teacher forms). Some teachers answered more than one form. Counted once each, there are up to 9 teachers, so the number of participants is 16 + 5 + 9 = 30. Report 30 participants, never 34.

The learners are the app's users. Parents and teachers are evaluators: the bridge to the child. Do not write "30 users", and do not call the children "respondents".

---

## 2. Automated Quality Assurance

| Check | Tool | Result | Evidence |
|---|---|---|---|
| Unit and component tests | JUnit 4, Robolectric, MockK | **697 run: 691 passed, 6 skipped by design, 0 failed** | `./gradlew testDebugUnitTest`; re-run on a second PC on 14 November 2026 with the same result |
| Layout matrix | Robolectric and Roborazzi on 4 screen profiles | 23 checks x 4 profiles = 92 runs: 86 passed, 6 skipped by design | `LayoutMatrix*Test` in the same run |
| Touch-target floor | `DimensTest`, `TopStatsBarLayoutTest` and others | Every child-facing control is at least 64 dp | STD TC-ACC-02; incident INC-06 closed |
| Zero-emoji policy | `ZeroEmojiPolicyTest` | Pass | STD TC-POL-01 |
| Audio and picture release gate | `AudioResolverTest`, `PictureAssetsTest` | The app's files match the reviewed release manifests | `docs/audio-release/`, `docs/image-release/` |

The six skipped tests are the font-scale 1.3 checks on the 360 x 640 profile, which is below the supported floor.

The Software Test Document (STD v1.0, `docs/submission/playIT_STD_v1.0.pdf`) records the midterm build, APK H: 433 tests, 427 passed, 6 skipped, 0 failed; 7 incidents found and closed (INC-01 to INC-07); 2 open defects (DEF-01, DEF-02). The suite has grown to 697 tests since then.

**Not measured by the automated tests:** latency on the device (TC-PERF), teacher agreement with the speech judge (TC-ASR-01 to 06), and independent completion by a child (TC-IND-03). The STD lists these as field measures.

---

## 3. Survey Results

### 3.1 Grade 1 learners (n = 16, smiley-face questions)

| Question | Happy | Okay | Neutral or sad |
|---|:---:|:---:|:---:|
| How did you feel after playing? | **15 of 16** | 1 | 0 |
| Did you like the characters? | **15 of 16** | 1 | 0 |
| Would you play it again? | **14 of 16** | 2 | 0 |
| Was it easy to play? | **9 of 16** | 6 | 1 |

Counts are given with the group size and are not turned into percentages, because the group is small.

**Not recorded.** The workbook holds no per-step observations: no first-try pass count for Say It, no hearts lost in Find It, no Blend It retries, and no time on task. Those fields are empty (`null`) in `round2_data.json`. No figure for them may appear in the paper.

### 3.2 Parents and supervising adults (n = 5)

| Measure | Result |
|---|:---:|
| System Usability Scale | **75.5 out of 100** (n = 5) |
| Fits a Grade 1 learner (1 to 5) | **4.6** |
| Would recommend (1 to 5) | **4.4** |

Accessibility checklist, number answering yes out of 5:

| Item | Yes |
|---|:---:|
| Large text | 5 |
| Audio narration | 5 |
| Simple touch controls | 5 |
| Colour contrast | 5 |
| Language suited to Grade 1 | 5 |
| Usable with limited motor control | 4 |
| Options for hearing difficulty | 3 |

**Not collected.** The Round 2 questions on usefulness, ease of use, dashboard clarity, the arithmetic gate, offline benefit, safety and intention to keep using the app were not in the workbook. There are no means for them.

### 3.3 Teachers and reading specialists (up to 9 across three forms)

| Form | When | Teachers | Result |
|---|---|:---:|---|
| Yes/No checklist, 12 items | Round 1, September | 4 | 11 of 12 items answered yes by all four |
| Draft rubric, 18 items | Round 2, November | 4 | Overall mean 3.81; 4.74 with straight-1 responses excluded |
| **Final rubric, 16 items** | Round 2, November | **5** | **Overall mean 4.72 out of 5**; all five rated 4 or higher on 15 of 16 items |

The 4.7 shown on the poster and in the pitch film is the final rubric's mean, from **5 of the 9 teachers**. Always state that group size.

Final rubric, mean per item (n = 5):

| Item | Mean |
|---|:---:|
| CUR-01 Marungko letter sequence (m, s, a, i ...) | 4.8 |
| CUR-02 Excluding NG and N-tilde for 26 English letters | 4.8 |
| CUR-03 Chapter pacing (3 to 4 letters per chapter) | 4.8 |
| CUR-04 Culturally familiar Filipino vocabulary | 4.8 |
| MOD-01 Hear It pure phoneme pronunciation | 4.6 |
| MOD-02 Articulation modeling (mouth shapes) | 4.8 |
| MOD-03 Say It anchor word practice | 4.8 |
| MOD-04 Speech recognition fairness | 4.8 |
| MOD-05 Find It progressive label scaffolding | 4.6 |
| MOD-06 Diagnostic Sound Check before Blend It | 4.8 |
| MOD-07 Blend It 3-letter CVC synthesis | 4.8 |
| DEP-01 DepEd Order 8 progress levels | 4.8 |
| DEP-02 DepEd transmuted grades (60% = 75) | 4.2 |
| DEP-03 Teacher A4 PDF Class Record | 4.6 |
| DEP-04 Independent learner usability | 4.8 |
| DEP-05 Gamification balance (hearts and stars) | 4.8 |

**Quotations on record.** The four Round 1 teachers' written comments (T-1 to T-4) are given word for word in `docs/validation-package/04_MVP_VALIDATION_HIGHLIGHTS.md`, section 5. The one used on the poster is T-1's: "The app is very useful and engaging for Grade 1 learners." T-1 and T-2 also wrote that some letter sounds sounded like the letter name ("ma" for /m/). No parent quotation is on record; add one only from the response sheets, word for word.

---

## 4. Changes Recorded Between the MVP and the Tested Build

From the STD's incident log (section 7.1). Each was found in testing, fixed and re-tested:

1. **INC-01:** three Blend It words were silent; the clips were released into the app.
2. **INC-02:** several letters showed the wrong mouth shape; the letter-to-mouth pairs were remapped.
3. **INC-03:** a title and the Say It attempt dots did not fit on small phones; fixed and checked on four screen sizes.
4. **INC-04:** the previous letter's picture stayed on screen; the picture loader now restarts.
5. **INC-05:** stars and hearts did not follow the child's results; corrected.
6. **INC-06:** some controls were smaller than 64 dp; every child-facing control is now at least 64 dp.
7. **INC-07:** Blend It could deal the tiles already in the answer's order; it no longer does.

Open at the time of the STD: Find It draws its wrong pictures from every letter, not only letters already learned (DEF-01); stars follow hearts lost only, without the SRS's accuracy term (DEF-02).

---

## 5. What These Results Do and Do Not Show

- They show how learners, parents and teachers **reacted** to the app: enjoyment, wish to play again, usability, and teachers' rating of its teaching design.
- They do **not** show that children learned to read better. No reading test was given, and nothing here measures learning over time.
- Per-step performance (Say It, Find It, Blend It), time on task, and the Round 2 parent ratings were not recorded and must not be reported.
