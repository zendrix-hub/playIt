# PlayIT at the booth: short pitch and likely questions

Facts here come from the app's README, the frozen survey results (`round2_data.json`, 12 Nov) and the poster. Anything in **[brackets]** must be filled in or checked by the team before the event.

---

## The 30-second pitch

> Many Grade 1 learners get stuck on letter sounds. Their teacher has a full class, a parent may be at work, and the internet at home is unreliable.
>
> PlayIT is an Android phonics game that works fully offline. Lily, a Philippine tarsier, teaches each letter in the DepEd Marungko order: the child hears the sound, says it, finds pictures that start with it, then blends letters into words. The phone itself listens to the child's voice; nothing leaves the device.
>
> Across two rounds we tested with 30 people: 16 Grade 1 learners, 5 parents and 9 teachers. 15 of 16 children chose the happy face, 14 of 16 would play it again, and teachers rated its teaching 4.7 out of 5.
>
> Want to try it? It's on this phone, with Wi-Fi and data off.

## The 10-second version (when people are passing)

> PlayIT teaches Grade 1 letter sounds with a tarsier named Lily. It listens to the child and needs no internet at all. Want to try it?

## At the booth

- One phone with PlayIT installed, charged, **Wi-Fi and data off** (airplane mode), opened on the map. Let visitors tap and say "mouse" themselves. This is the live proof that the demo is real.
- A second phone or a power bank in case the first runs out.
- The Parent Zone ready to show (the arithmetic question, then the progress and the PDF report).
- Each member knows the 30-second pitch and one section of the questions below.

---

## Likely questions

### The problem and the idea

**Why Grade 1, and why letter sounds?**
Letter sounds are the first step to reading. If a child does not connect the letter to its sound, blending and reading words stall. Grade 1 is where that is taught, and where a child who falls behind is easiest to help.

**Why offline? Most children have phones now.**
Having a phone is not the same as having data. Many homes run on prepaid mobile data or a patchy signal. An offline app works the same at home, in school, and on the jeepney, and costs nothing to run.

**Why a tarsier?**
Lily is a Philippine tarsier, from Bohol, where the app's map is set. Children respond to a friendly guide, and a local animal makes it feel like theirs.

**Isn't this just another phonics app?**
Three things together are rare: it follows the Marungko order that DepEd teachers already use, it listens to the child's own voice and responds, and it needs no internet, no account and has no ads.

### Teaching

**What is the Marungko approach, and how does PlayIT follow it?**
Marungko teaches letters in a set order, starting with sounds that are easy to hear and say (m, s, a, i), and moves from sound to letter to word. PlayIT uses that order and routine: Hear It, Say It, Find It, then Blend It once a chapter's letters are done. 26 letters in 7 chapters; every Blend It word can be sounded out with letters already taught.

**Marungko is used for Filipino reading. Why English letter sounds?**
We adapted the Marungko order and routine to the 26 letters of the alphabet that Grade 1 learners also study in English. Teachers rated that adaptation 4.8 out of 5 in the Round 2 rubric (CUR-02, 5 teachers). Ñ and NG are not taught in this version.

**Did children actually learn to read better?**
We do not claim that yet. Our two rounds measured whether children enjoyed it and want to play again, and whether parents and teachers accept it. Measuring reading gains needs a longer study with a pre-test and post-test over a term, which is our next step.

**What happens when a child gets it wrong?**
Lily never scolds. A miss gets a spoken correction for that specific error; a second miss shows the mouth shape; the third time she says it together with the child and moves on. Say It never takes away a heart.

### The technology

**How does it check the child's voice without the internet?**
It uses Vosk, an offline speech recognizer, with its small English model stored inside the app. For each word it listens only for the right answer and a few likely mistakes (the letter's name, an added vowel), which makes it accurate enough on a phone.

**Does it work with children's voices and a Filipino accent?**
That is the hardest part. Narrowing the choices to a few words helps a lot, and the app is forgiving: it gives hints and never punishes. Teachers rated speech recognition fairness 4.8 out of 5 (MOD-04). We did not record a per-child first-try rate, so we don't quote one.

**Is the children's data safe?**
The app does not have the internet permission at all, so it cannot send anything anywhere. Voices are checked on the phone and never recorded or uploaded. The Parent Zone is behind a simple arithmetic question so a child cannot open it.

**What phones does it run on? How big is it?**
Android 8.0 or newer. The app is [X] MB including the speech model. [Check the APK size.]

**How did you build and test it?**
Kotlin and Jetpack Compose, Clean Architecture with MVVM, a Room database on the phone. 691 automated unit tests pass, 0 fail. It was tested against our Software Test Document.

**Did you use AI?**
Yes, and we disclose it. AI tools helped with design review, code and test generation, and the synthetic voices and pictures. The team set the requirements, made the design decisions, approved every picture and sound, and is responsible for the result.

### The results

**Only 30 participants, across two rounds. Is that enough?**
For an acceptance and usability study it is a reasonable first sample, and we report counts with the group size ("15 of 16") instead of percentages so nothing looks bigger than it is. It is not enough to prove learning gains, and we do not claim that. The children's and parents' answers are from Round 1 (September); the teachers' 4.7 is the Round 2 rubric, answered by 5 of the 9 teachers.

**Why didn't the children fill in the survey?**
Six-year-olds cannot reliably answer a questionnaire. We observed them while they played and asked three smiley-face questions (did you like it, would you play again, was it easy). Parents and supervising adults answered a usability survey (SUS 75.5, above the usual 68 benchmark); teachers rated the teaching with a rubric.

**Why survey parents and teachers if children are the users?**
They are the bridge to the child. A parent decides whether the app goes on the phone at home, and a teacher decides whether it fits the lesson. If they don't accept it, the child never gets it.

**Where and when did you test?**
Children: 16–21 Sept 2026; parents and supervising adults: 20–23 Sept; teachers: September (yes/no checklist) and November (the 16-item rubric). [Add the schools or barangays and how parents gave consent.]

**What did people not like?**
Ease of play was the weakest smiley answer: 9 of 16 children chose the top face, 6 the middle and 1 the lowest. Teachers' lowest item was the DepEd transmuted grades (4.2 of 5). [Add one or two parent or teacher comments. Be honest; judges trust a team that knows its weak spots.]

### Limits and next steps

**What doesn't it do yet?**
- The sounds of most letters still need a teacher audit.
- Find It sometimes mixes in letters the child hasn't learned yet.
- No progress export beyond the PDF report.

**What's next?**
A term-long study with a reading pre-test and post-test; the teacher audit of all letter sounds; more chapters, including Filipino letters such as Ñ and NG; easy offline sharing of the app between phones.

**How would schools get it?**
As a free APK shared offline (by USB or phone-to-phone), so even a school without internet can install it on many phones. [Confirm the team's plan.]

**Who did what in the team?**
[One line per member: Zendrix Riva (lead, ...), Erik Samuel Bien (...), Ashlee Skye Durano (...), Kaysean Miel (...), John James Palis (...).]

### About the video

**Is the app in the video real?**
Yes. Every screen inside the phone is a real screenshot of the working app; the characters around it are animated to show what the child does. You can try the same screens on this phone right now.
