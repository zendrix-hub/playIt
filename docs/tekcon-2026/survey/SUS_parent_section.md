# SUS questions for the parent section of the Round 2 form

The System Usability Scale (Brooke, 1996) is ten fixed statements. Add them to **Section 3 (Parent & Guardian)** of the Google Form, after Q22b, as one new block.

## Rules that keep the score valid

- Keep all ten, in this order. Do not drop, merge or reorder them.
- Keep the wording. The only accepted change is the product name ("PlayIT" in place of "this system"). If your Round 1 form used slightly different wording, **copy the Round 1 wording** so the two rounds compare.
- Same answer scale for every item: 1 = Strongly Disagree … 5 = Strongly Agree.
- Make all ten required. A response with a missing item cannot be scored.
- The even-numbered items are negative on purpose. Do not "fix" them.

## Block heading for the form

**Using PlayIT yourself** — Think about setting up the app, helping your child start, and using the Parent Zone. Rate each statement from 1 (Strongly Disagree) to 5 (Strongly Agree).

| Code | Statement |
|---|---|
| SUS-01 | I think that I would like to use PlayIT frequently. |
| SUS-02 | I found PlayIT unnecessarily complex. |
| SUS-03 | I thought PlayIT was easy to use. |
| SUS-04 | I think that I would need the support of a technical person to be able to use PlayIT. |
| SUS-05 | I found the various functions in PlayIT were well integrated. |
| SUS-06 | I thought there was too much inconsistency in PlayIT. |
| SUS-07 | I would imagine that most people would learn to use PlayIT very quickly. |
| SUS-08 | I found PlayIT very cumbersome to use. |
| SUS-09 | I felt very confident using PlayIT. |
| SUS-10 | I needed to learn a lot of things before I could get going with PlayIT. |

In Google Forms: add a section break, then one "Multiple choice grid" question with the ten statements as rows and 1 to 5 as columns, with "Require a response in each row" switched on.

## Scoring, per parent

1. Odd items (1, 3, 5, 7, 9): answer minus 1.
2. Even items (2, 4, 6, 8, 10): 5 minus answer.
3. Add the ten results (0 to 40) and multiply by 2.5. That is the parent's score, 0 to 100.

In Google Sheets, with the ten answers in columns B to K of row 2:

```
=((B2-1)+(5-C2)+(D2-1)+(5-E2)+(F2-1)+(5-G2)+(H2-1)+(5-I2)+(J2-1)+(5-K2))*2.5
```

The study's SUS score is the **mean of the parents' scores**. Report it with the number of parents who answered ("SUS 78.1, n = 9"). 68 is the usual benchmark for average; your target is 75. Round 1 was 75.5 with n = 5.

## Parents who already answered

Do not guess or fill in their SUS answers. Send them a short follow-up form with only these ten items plus the same name or code they gave before, so each follow-up can be matched to their first response. Parents who have not answered yet get the full updated form.

If some earlier parents do not reply, report SUS for those who did, with that smaller n. Do not average in blanks.

## Also update

- Codebook: add columns `SUS_01` to `SUS_10` and `SUS_Score`.
- Method section: state that SUS was added during Round 2 and how earlier respondents were followed up.
- `round2_data.json`: when you have the result, fill in `parents.sus_score` and `parents.sus_n`.
