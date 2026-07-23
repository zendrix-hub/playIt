# Hilt DI Adoption Disposition (Master Context T-15)

**Task ID:** CLEAN-11.05  
**Date:** 2026-07-23  
**Status:** Deferred — Manual DI remains sufficient  

## Background & Decision

Per Master Context §8 / §11 / §12 (T-15) and Engineering Review §1.2:
- The app currently uses manual factory and constructor dependency injection across `PlayItApplication`, ViewModel factories, repositories, domain use cases, DAOs, and speech validators.
- Manual DI is consistent, clean, fully testable, and appropriate for playIT's scope (12 screens, offline-first architecture, Room database, Vosk speech engine).
- Introducing Hilt/Dagger would add annotation-processing overhead, additional generated code, build-time complexity, and dependency setup without providing functional benefit or resolving any existing architectural bottleneck.

## Disposition

Hilt DI adoption is **DEFERRED** indefinitely. The current manual DI pattern remains standard for the codebase. Should future major architectural changes present concrete limitations with manual DI, Hilt adoption may be evaluated as a dedicated, fully-scoped roadmap addendum.
