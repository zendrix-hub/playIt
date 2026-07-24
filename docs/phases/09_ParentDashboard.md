# playIT — 09. Parent Dashboard & Report Preview
See `00_ProjectRules.md` for source priority and global constraints. Hosts the `ReducedMotionToggle` specified in `03_MotionSystem.md`. Both screens are exempt from the child-facing Screen Templates in `02_ComponentLibrary.md` §9 — adult information-density patterns apply.

---

## 1. ParentDashboardScreen Audit `[orig §3.11]`

| | |
|---|---|
| **Strengths** | Math-gate access control is a smart, appropriately low-friction guard for a device shared between a child and parent; profile-switcher dropdown, at-risk letter flagging, and PDF export are all genuinely useful, well-scoped features; correctly exempted from child-facing simplicity constraints. |
| **Weaknesses** | No `ReducedMotionToggle` composable exists in the SDD's component table despite this being the logical, and only sensible, place to put one (`01_Foundation.md` §5 gap); 7:1 contrast target needs explicit verification, not just a stated design-system aspiration. |
| **Educational impact** | Indirect but high — this is the interface that turns app data into a parent's actual involvement (research explicitly ties parental praise/involvement to retention). |
| **Effort** | Low-Medium |
| **Recommendation** | Add `ReducedMotionToggle` to `ParentDashboardScreen`'s component list (spec in `03_MotionSystem.md`); run a real contrast check on `LetterPerformanceTable`'s green/yellow/red status colors — note: this red is fine, since the "avoid harsh red" rule is a *child-facing emotional-safety* rule (`02_ComponentLibrary.md` §5), not a general prohibition, and adult analytics dashboards legitimately use red/yellow/green status semantics. |

---

## 2. ReportPreviewScreen Audit `[orig §3.12]`

| | |
|---|---|
| **Strengths** | Certificate-style output, offline PDF generation via native `PdfDocument` API (no extra dependency) is a lean, appropriate technical choice; supports the explicit rural/offline-sharing constraint (Bluetooth/local save). |
| **Weaknesses** | None significant — this is an adult-facing, low-frequency, low-risk screen. |
| **Educational impact** | Low-Medium (indirect, via parent engagement) |
| **Effort** | Low |
| **Recommendation** | Polish-tier only; not a priority relative to child-facing screens. |

---

## 3. Phase F — Dashboard Tasks

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| F1. Parent Dashboard: wire `ReducedMotionToggle` into a real Settings section | Give the accessibility toggle a home | Parents can act on motion-sensitivity needs | 03_MotionSystem.md C8 | Low | Must Have |
| F2. Parent Dashboard: contrast-audit `LetterPerformanceTable` and stat cards against 7:1 | Meet the design system's own stricter adult-dashboard standard | Legibility for parents in bright outdoor/rural lighting conditions | 01_Foundation.md A1 | Low-Medium | Should Have |
| F3. Report Preview: token application only | Visual consistency with the rest of the app | Minor polish | 01_Foundation.md A1–A2 | Low | Nice to Have |

---

## 4. Related Accessibility Task (full detail in `10_FinalPolish.md`)

| Task | Objective | User Benefit | Dependencies | Complexity | Priority |
|---|---|---|---|---|---|
| H1. Full contrast audit across every screen (4.5:1 child-facing, 7:1 Parent Dashboard), adjust tokens (e.g. `learningBlueDeep`) where needed | Meet WCAG AA in practice, not just in the design-system doc | Usable by low-vision users and in bright outdoor lighting | 01_Foundation.md A1, F2 above | Medium | Must Have |

---

## 5. Priority Context

Ranks #6 in the Summary Priority Ranking (`10_FinalPolish.md`): reduced-motion toggle addition. Report Preview / Profile Select / Name Prompt / Splash rank #7 (lowest): token application only.
