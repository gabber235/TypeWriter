# Campaign procedure

## Establish the baseline

Capture Git status and the candidate inventory. Exclude generated output, tests, build trees, legacy directories, and already sufficient files. Search repository history and current docs before inventing vocabulary.

Maintain a living plan for broad work. Partition by ownership and usage, not arbitrary file count.

## Parallel workers

Use small exclusive edit scopes. Typical scope is one crate, two to six contracts, or four to twelve closely related Dart or Kotlin files. Investigation may cross the repository, but edits may not.

Each worker must receive:

1. Exact owned files.
2. Read only context paths.
3. The quality standard.
4. Comment only restriction.
5. Required focused validation.
6. A report format covering evidence, uncertainty, exclusions, and checks.

Stabilize shared vocabulary in contracts and owner modules before consumer waves.

Workers must not run repository wide generators concurrently. The coordinator owns global generation, cleanup, final diff purity, and cross language review.

## Coordinator review

Review files individually. Check claims most likely to be invented: authority, atomicity, ordering, retry, idempotency, side effects, security, revision meaning, and error recovery.

Reject a worker pass when it deletes executable declarations, duplicates comment layers, places overview text between imports, narrates fields, or introduces unsupported guarantees. Restore only paths proven clean at baseline.

After each accepted wave, run focused checks and update the living plan. Before completion, trace representative workflows end to end across Skir, Rust, Kotlin, and Dart. Confirm the final diff contains comments and formatter induced whitespace only.

## Completion report

Report audited files, changed files, explicit exclusions, repaired stale documentation, validation results, unresolved repository baseline failures, preserved unrelated changes, and whether commits were created.
