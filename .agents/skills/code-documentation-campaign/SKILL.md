---
name: code-documentation-campaign
description: Audit and improve Typewriter code documentation across current handwritten production code. Use for repository wide or domain scoped documentation work that must add missing context, repair stale claims, and improve weak existing docs without changing behavior.
---

# Code Documentation Campaign

Document Typewriter as a connected system. Improve existing documentation before adding more prose. Remove or rewrite stale, speculative, misplaced, and signature narrating comments.

Read the `coding-principles` skill completely before editing. For quality decisions, also read [quality-standard.md](references/quality-standard.md). For broad campaigns or parallel work, read [campaign-procedure.md](references/campaign-procedure.md).

## Scope

Work only in current handwritten production code under `services/`, `backend/`, `panel/`, and `skir-src/` unless the user names a narrower scope.

Never edit legacy `engine/`, `extensions/`, `module-plugin/`, or `app/`. Never edit `docs/adapters/` or generated Skir, Freezed, Riverpod, or other generated output by hand. Tests are evidence unless the user explicitly requests test documentation.

Inspect Git status before editing. Preserve unrelated work. Documentation work authorizes comment changes and formatter induced whitespace only. Any executable change requires separate user approval.

## Required outcome

For every owned domain:

1. Trace definitions, constructors, callers, state transitions, failures, tests, and cross language contracts.
2. Identify missing public contracts, weak existing docs, stale claims, duplicated explanations, and misleading placement.
3. Establish capability context on the highest stable module or owner.
4. Document public declarations whose purpose or contract is not obvious.
5. Document private architecture anchors only when they explain behavior across a file or capability.
6. Remove documentation that merely narrates names, signatures, fields, or local implementation.
7. Verify every behavioral claim against current code.

Completeness means every handwritten production file was audited, not that every declaration received a comment. Record explicit exclusions and reasons.

## Validation

Run formatters only on owned files. Run the narrowest analyzer, compiler, documentation, and tests appropriate to each domain. Run `git diff --check` and prove task owned diffs contain no executable changes.

Run Skir generation or snapshot validation only once from the coordinator after parallel contract workers finish. Its commands inspect the whole contract tree and cleanup can erase another worker's edits.

Do not commit unless the user requests it.
