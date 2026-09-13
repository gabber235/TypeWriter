# Documentation quality standard

Good documentation gives the reader a working system model before local details.

## Hierarchy

### Capability or module

Explain the problem, boundary, vocabulary, principal collaborators, complete flow, and where authority lives.

### Owner type

Explain why the owner exists, which state it controls, lifecycle, invariants, consistency boundary, and adjacent alternatives.

### Operation

Explain caller intent, meaningful preconditions, observable result, side effects, and why a caller chooses it over nearby operations. Do not repeat context already established by its owner.

### Result or failure

Explain semantic meaning, recovery choices, and guarantees. Put transport behavior only on the transport abstraction that owns it.

## Review questions

Ask these before accepting documentation:

1. Can a new reader explain why this capability exists and where it sits in Typewriter?
2. Can the reader identify the authoritative owner of mutable state?
3. Are canonical state, drafts, projections, observations, desired state, and applied state distinguished?
4. Are revision, ordering, atomicity, idempotency, lifecycle, and failure claims proven?
5. Does each detail live on the declaration that owns it?
6. Would deleting a sentence lose noninferable knowledge?
7. Has existing documentation been corrected rather than layered with a second conflicting explanation?

Reject plausible prose without evidence. Reject one line summaries when their owner lacks system context. Reject long comments that explain implementation sequence instead of contract. Length follows explanatory need.

Use navigable Dartdoc, KDoc, and rustdoc references when they improve discovery. Examples must clarify correct use and remain cheap to maintain.
