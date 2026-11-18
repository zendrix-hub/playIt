# PlayIT · PitchPerfect 2026 · 5-minute stage script

**Team:** Group 56, IT411 Capstone & Research 2, CIT-U College of Computer Studies
**Event:** PitchPerfect 2026 (MinebeaMitsumi × Wildcat Innovation Labs, TekCon 2026)
**Deck submission:** 19 November 2026, 4:00 PM · **Live pitch:** 25 November 2026, strictly 5:00

**Files in this folder**
- `G56_PlayIT_PitchPerfect_2026_Deck.pptx`: 18 slides with Morph transitions and speaker notes. Sniglet and Lexend are embedded, so it looks the same on the venue computer.
- `G56_PlayIT_PitchPerfect_2026_Deck.pdf`: a static backup for the judges' form and in case PowerPoint fails at the venue.
- `build_pitchperfect_com.ps1`: rebuilds the deck, the PNG previews and the PDF from `assets/` and `speaker_notes.txt`.

## Running the deck
- Every slide from 2 to 18 uses **Morph**. There are 17 clicks in total, one per `[CLICK]` below. Nothing advances on its own.
- Open the `.pptx` in PowerPoint 2019 or Microsoft 365. Older versions show a fade instead of Morph. Keep the PDF on a USB drive as a backup.
- The script is about 640 words, which takes about 4:45 at a calm pace. That leaves about 15 seconds of slack before the bell.

## Script with click cues

| Slide | Time | Say this | Then |
|---|---|---|---|
| S01 Meet Mia | 0:00–0:20 | Meet Mia. She's six years old, in Grade 1 right here in Bohol. Every night after dinner, her reading book stays open on a single stuck letter sound—while her mother works late at the sewing machine and wants to help, | CLICK → S02 |
| S02 Curiosity & Wall | 0:20–0:30 | What happens when one letter sound has no one at home to answer it? Without one-on-one phonics coaching, when Mia closes that book tonight, she falls another day behind. | CLICK → S03 |
| S03 9 out of 10 | 0:30–1:00 | Mia isn't alone. According to the June 2022 World Bank and UNESCO Learning Poverty Brief, 90.9%—or 9 out of 10—Filipino children at age ten still struggle to read a simple text. Why haven't global EdTech apps fixed this? Two walls: the Connectivity Wall—cloud apps die on patchy prepaid data—and the Affordability Wall—four to six hundred peso monthly paywalls built around foreign accents. | CLICK → S04 |
| S04 Meet PlayIT | 1:00–1:25 | That's why we built PlayIT—an offline-first Android phonics companion guided by Lily the Tarsier. Built in Kotlin and Jetpack Compose with no internet permission at all, PlayIT turns an entry-level Android 8 phone that parents already own into a patient phonics tutor that follows DepEd's Marungko sequence—M, S, A, I first. | CLICK → S05 |
| S05 Hear It | 1:25–1:35 | Here's how Lily guides Mia in four steps. One—Hear It: Lily models the pure /m/ sound and the anchor word, Mouse. | CLICK → S06 |
| S06 Say It | 1:35–1:50 | Two—Say It: our built-in offline Vosk speech recognizer listens right on the phone and checks what she said, with zero cloud fees. Miss twice? Lily shows the mouth shape instead of scolding—and it never costs a heart. | CLICK → S07 |
| S07 Find It | 1:50–1:58 | Three—Find It: Mia taps the three familiar pictures that start with /m/—Mouse, Mom and Map. | CLICK → S08 |
| S08 Blend It | 1:58–2:05 | And four—Blend It: letter tiles snap together into real words like SAM, saved on the phone in a local database. | CLICK → S09 |
| S09 Parent Zone | 2:05–2:10 | Plus a Parent Zone that tracks all 26 letters with a one-tap A4 PDF report—backed by 697 automated tests with zero failures. | CLICK → S10 |
| S10 The bridge to Mia | 2:10–2:30 | In our field evaluation with 30 participants, 15 of 16 Grade 1 learners picked the happiest face and 14 would play again. Parents and teachers are the bridge to Mia: parents rated Grade 1 fit 4.6 out of 5 with a 75.5 usability score, and five teachers on our final 16-item rubric rated our teaching design 4.73 out of 5 (exact mean 4.725; 9 teachers took part across both rounds). | CLICK → S11 |
| S11 Market | 2:30–2:55 | With 13.5 million early learners across the Philippines and Southeast Asia (UNESCO UIS cohort), that bridge opens a ₱16.2 billion total addressable market, and ₱2.88 billion across 2.4 million Filipino Grade 1 learners (DepEd LIS and PSA cohort). In Region VII alone, 185,000 learners represent a ₱222 million regional market—where capturing an 8 to 16 percent Cebu beachhead unlocks ₱18 to ₱36 million. | CLICK → S12 |
| S12 Revenue | 2:55–3:30 | We earn through four streams while keeping Chapter 1—M, S, A, I—free forever. Parents unlock all seven chapters for ₱149 a month or a ₱999 pass through GCash—less than three tutoring sessions. Private schools license at ₱450 per student a year, while LGU barangay kits at ₱75,000 funded via the Special Education Fund (RA 7160 Sec. 272) and ₱150,000 corporate CSR school adoptions deliver high-margin bulk impact. | CLICK → S13 |
| S13 Why we win | 3:30–4:00 | Why do we win? Cloud apps charge around ₱500 a month and break without Wi-Fi. Offline flashcard apps can't hear whether a child said the word. Tutors cost ₱3,000 a month. Of seven reading apps we reviewed, none combined all three: working offline, the Marungko order, and checking the child's speech on the phone. PlayIT does—at zero cloud cost. | CLICK → S14 |
| S14 GTM Phase 1 | 4:00–4:10 | We go to market through the classroom door. Phase 1 is our proposed pilot: partnering with Cebu elementary schools for a 6-week reading-fluency study. | CLICK → S15 |
| S15 GTM Phase 2 | 4:10–4:20 | Phase 2: during Brigada Eskwela and PTA nights, teachers share PlayIT phone-to-phone via Nearby Share—families install it at zero mobile data cost. | CLICK → S16 |
| S16 GTM Phases 3–4 | 4:20–4:30 | Phases 3 and 4: we scale to 20 Visayas schools and three CSR partners, then expand nationally through LGU Special Education Fund kits and Bisaya and Tagalog modules. | CLICK → S17 |
| S17 Unit economics | 4:30–4:45 | Because speech is checked on the phone at ₱0 cloud cost per learner, our projected unit economics reach an 88% gross margin and 4.8 times LTV to CAC. We need just 985 paid pass equivalents—about 82 a month, or two schools plus one LGU kit—to break even, scaling to a ₱1.45 million bottom-up Year 1 target. | CLICK → S18 |
| S18 Every child reading | 4:45–5:00 | With PlayIT, the dim table where Mia got stuck becomes the place she finds her voice. A working Android app in hand, built in Cebu, tested with real learners, parents and teachers—we are now seeking Cebu school partners for our pilot, one sound at a time. Thank you! | (end) |

The same text is in each slide's speaker notes.

## Q&A defense notes

**Is the audio licensed for commercial use?** Not all of it yet. Four clips in the capstone APK (the Blend It words AM, TUB, YAM and the short /i/) were made with the ElevenLabs Free plan, which allows non-commercial use with attribution (credited in the Parent Zone). We will replace them before a commercial release or school pilot, for example with Apache-2.0 Kokoro-82M voices put through the same review as the rest of the audio. One Kokoro take of TUB failed that review before, so the swap needs its own check.

**How do you sell an unlock to a phone that is offline?** This is planned, not built. The idea is a signed activation code tied to the device and stored with the child's local data, bought through a GCash merchant or by SMS. After activation nothing needs the internet. The capstone build has no payment system.

**Will it fit on a cheap phone?** `playit.apk` is 119,339,266 bytes (about 119 MB), including the bundled Vosk speech model. It needs Android 8.0 (API 26) or newer, and no mobile data once installed by Nearby Share or sideloading.

**What is built today, and what is roadmap?**
- **Built and tested today:** Hear It, Say It, Find It and Blend It for 26 letters in 7 chapters; offline Vosk speech checking; local progress in a Room (SQLite) database; the Parent Zone with an A4 PDF report; 697 automated tests (691 passed, 6 skipped by design, 0 failed); a field evaluation with 30 participants.
- **Roadmap:** the Teacher Analytics Dashboard and multi-student sync, the offline voucher unlock, and Bisaya and Tagalog modules.

**How would LGUs pay?** Through the Local School Board's Special Education Fund (SEF), following the LGU's own procurement process. Check the current DepEd–DBM–DILG rules on what SEF may buy before the pitch.

**Why is the speech check free to run?** Vosk runs on the phone. There is no cloud speech API, so there is no per-learner inference fee. The remaining costs are distribution, support and new content.

**Did children learn to read better?** We do not claim that. The evaluation measured reactions and expert ratings. A future school pilot would be the first study to measure reading fluency over time.

**Where does 4.73 come from?** From 5 teachers who completed the final 16-item rubric (exact mean 4.725). Nine teachers took part across both rounds. 15 of 16 learners chose the happiest face, but young children tend to choose that face; ease of play was lower (9 of 16).

**How accurate and fast is the speech check?** Neither was measured in the field. It is the next measurement, together with how often the app agrees with a teacher.

## Figures to confirm before 19 November

These figures come from the hand-off, not from the validation data. Check each before the deck is submitted.

1. **The Cebu beachhead.** The hand-off says the ₱18M–₱36M beachhead is a "5% share", but 5% of 185K learners is 9,250 learners, worth ₱9.2M at ₱999 or ₱16.5M at ₱149 a month. ₱18M–₱36M is about 10–20% of 185K learners paying ₱999. The slide and script leave the share out until the team confirms its model.
2. **Sources for the market and price figures:** 13.5M and 2.4M learners, 185K in Region VII, ₱1,200 per learner per year (what the TAM and SAM imply), cloud-app paywalls of ₱400–₱600 a month, tutors at ₱3,000 a month. Have the source for each ready.
3. **The projections on S17:** 88% gross margin, 4.8× LTV:CAC, ₱245 CAC, break-even in month 9, ₱1.45M and ₱5.80M revenue. Keep the spreadsheet behind them at hand. The hand-off asked for revenue bars split by stream, but gave no split, so the slide shows totals with a legend.
4. **4.73 or 4.72.** The deck uses 4.73, the usual rounding of 4.725. The research paper and the E report say 4.72. Pick one for all documents.
5. **The comparison matrix** reflects the team's review of 7 apps. "Cloud global apps: works offline" is shown as *Limited* rather than ✕, because some global apps do have offline modes.
