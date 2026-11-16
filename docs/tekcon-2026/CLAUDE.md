# CLAUDE.md — PlayIT Capstone Defense & Validation Handoff

## Active Scope: Final 99%+ Submission-Readiness Audit (November 15, 2026)

All primary deliverables for **PlayIT** (Group **G56**, **IT411 Capstone & Research 2**) have been completed, consolidated on the **`main`** branch (`github.com/zendrix-hub/playIt`), formatted with custom architecture and empirical diagrams, and synchronized to the official Google Drive folder **`G56`** (`1RzafWRNSvACgXG0hSAZntJzwIxv2GpVc`).

Your role in this session is to perform an **independent, rigorous defense-readiness and integrity audit** across both the Git repository and Google Drive deliverables to verify **99%+ submission reliability** before adviser consultation and final defense.

### Start With These Two Handoff Guides
1. **`CLAUDE_RESEARCH_PAPER_HANDOFF.md`** — Audit brief covering the **Adviser Consultation Pack** (`A`, `B`, `E`, `DEPLOYMENT_AND_PERMISSIONS_INSPECTION`), the **18 Week 11–12 Validation PDFs**, the **Week 13–14 ACM Research Paper** (`.md`, `.docx`, `.pdf`), exact Google Drive file IDs, verified APK checksums, and formal presentation standards.
2. **`CLAUDE_HANDOFF.md`** (also under `docs/tekcon-2026/`) — Complete production reference for the **TekCon 2026** deliverables (`01_Video_Pitch/`, `02_Research_Poster/`, `03_Software_Product_Logo/`), Remotion/Chrome rendering workflow, and poster checker (`cd docs/tekcon-2026/tools && npm run build`).

### Verified Active APK Artifacts (`04_Deployment_Evidence`)
- **Current Release APK:** [`playit.apk`](https://drive.google.com/file/d/1pIr2360noHf64TOT-sAwk_uNlk2gSeQa/view?usp=sharing) (`1pIr2360noHf64TOT-sAwk_uNlk2gSeQa`) — Commit `67fe890` (`08 Nov 2026`), `119,339,266` bytes, SHA-256 `A6D4560F0F593F0162D8B3A6A2A21EA173701966E54835CB27B64753579B184F`, **697 unit tests (691 passed, 6 skipped by design, 0 failed)**, **0 `INTERNET` permissions** verified via `aapt`. Integrates the child-friendly visual refresh, animated mouth-shape cues, animal avatar onboarding, and post-Round 1 refinements.
- **Midterm Baseline APK:** [`playit-debug-H-0320385.apk`](https://drive.google.com/file/d/1F-_kQS-M1TgwVd1CUBwMCXXqqFf_fmog/view?usp=sharing) (`1F-_kQS-M1TgwVd1CUBwMCXXqqFf_fmog`) — Commit `0320385` (`18 Oct 2026`), `108,317,310` bytes, **433 unit tests (427 passed, 6 skipped, 0 failed)**, preserved solely for baseline comparison.
- *Note:* The intermediate visual refresh APK (`playit-debug-design-9ce23e2.apk`) was removed from Drive because `playit.apk` supersedes it with all visual refresh updates intact.
