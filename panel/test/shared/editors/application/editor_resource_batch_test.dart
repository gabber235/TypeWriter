import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

EditorSnapshot _snapshot() => const DocumentEditorSnapshot(
  EditorDocument(
    rootType: StringType(),
    typeCatalog: TypeCatalog([]),
    confirmedValue: StringValue("Original"),
    revision: 1,
  ),
);

typedef _Operation = (EditorResourceKey, EditorCommit);

final class _BatchResource implements EditableResource {
  _BatchResource(String id, this.combiner)
    : key = EditorResourceKey(scope: "realm", identity: id);
  final MutationCombiner<_Operation, List<_Operation>> combiner;
  @override
  final EditorResourceKey key;
  @override
  Set<Object> get reservations => {key};
  int reads = 0;
  bool unavailable = false;

  @override
  Future<EditorSnapshot?> refresh() async {
    reads++;
    if (unavailable) throw StateError("Unavailable");
    return _snapshot();
  }

  @override
  MutationIntent prepare(
    EditorSnapshot snapshot,
    EditorCommit commit,
    void Function(TypedMutationResult) accept,
  ) => CombinedMutation<_Operation, List<_Operation>>(
    combiner: combiner,
    resources: reservations,
    prepare: () => MutationContribution(
      operation: (key, commit),
      integrate: (result) async {
        switch (result) {
          case SubmissionConfirmed():
            accept(MutationSuccess(revision: 2, value: commit.rootValue));
          case SubmissionRejected(:final message):
            accept(
              MutationUnavailable([
                TypeDiagnostic(
                  code: TypeDiagnosticCode.invalidValue,
                  message: message,
                ),
              ]),
            );
          default:
            break;
        }
      },
    ),
  );
}

TransactionalEditorSource _source(
  LocalWork workspace,
  _BatchResource resource,
) {
  final source =
      workspace.editor(
            ResourceEditorTarget(
              targetId: resource.key.identity,
              label: "Resource",
              resource: resource,
              snapshot: _snapshot(),
              commitPolicy: EditorCommitPolicy.applyResource,
            ),
          )
          as TransactionalEditorSource;
  workspace.retain(resource.key);
  return source;
}

void main() {
  for (final failRefresh in [true, false]) {
    test(
      "retrying one member preserves the complete batch after ${failRefresh ? "refresh failure" : "rejection"}",
      () async {
        final workspace = LocalWork();
        addTearDown(workspace.dispose);
        final requests = <List<_Operation>>[];
        var reject = !failRefresh;
        final combiner = MutationCombiner<_Operation, List<_Operation>>(
          prepare: (operations) => PreparedCommit(
            id: Object(),
            label: "Batch",
            resources: operations.map((operation) => operation.$1).toSet(),
            send: () async {
              requests.add(operations);
              if (reject) return const SubmissionRejected(message: "Try again");
              return SubmissionConfirmed(operations);
            },
          ),
        );
        final firstResource = _BatchResource("first", combiner);
        final secondResource = _BatchResource("second", combiner)
          ..unavailable = failRefresh;
        final first = _source(workspace, firstResource);
        final second = _source(workspace, secondResource);
        final result = await EditorBatch.submit(
          changes: {
            first: {DataPath.root: const StringValue("First")},
            second: {DataPath.root: const StringValue("Second")},
          },
        );
        expect(result.values, everyElement(isA<MutationUnavailable>()));
        expect(requests, hasLength(failRefresh ? 0 : 1));
        expect(first.hasWork, isTrue);
        expect(second.hasWork, isTrue);

        secondResource.unavailable = false;
        reject = false;
        expect(await first.flush(), isA<MutationSuccess>());
        expect(requests.last.map((operation) => operation.$1), [
          firstResource.key,
          secondResource.key,
        ]);
        expect(requests.last.map((operation) => operation.$2.rootValue), [
          const StringValue("First"),
          const StringValue("Second"),
        ]);
        expect(firstResource.reads, 2);
        expect(secondResource.reads, 2);
        expect(first.hasWork, isFalse);
        expect(second.hasWork, isFalse);
      },
    );
  }

  test(
    "one uncertain batch replays the same request and settles every member",
    () async {
      final workspace = LocalWork();
      addTearDown(workspace.dispose);
      final requests = <List<_Operation>>[];
      final combiner = MutationCombiner<_Operation, List<_Operation>>(
        prepare: (operations) => PreparedCommit(
          id: Object(),
          label: "Batch",
          resources: operations.map((operation) => operation.$1).toSet(),
          replay: SubmissionReplay.identicalRequest,
          send: () async {
            requests.add(operations);
            if (requests.length == 1) {
              return SubmissionUncertain(
                message: "Lost response",
                cause: StateError("Lost response"),
                stackTrace: StackTrace.current,
              );
            }
            return SubmissionConfirmed(operations);
          },
        ),
      );
      final firstResource = _BatchResource("first", combiner);
      final secondResource = _BatchResource("second", combiner);
      final first = _source(workspace, firstResource);
      final second = _source(workspace, secondResource);
      final result = await EditorBatch.submit(
        changes: {
          first: {DataPath.root: const StringValue("First")},
          second: {DataPath.root: const StringValue("Second")},
        },
      );
      expect(result.values, everyElement(isA<MutationUncertain>()));
      expect(await second.flush(), isA<MutationSuccess>());
      expect(requests, hasLength(2));
      expect(requests.last, same(requests.first));
      expect(firstResource.reads, 1);
      expect(secondResource.reads, 1);
      expect(first.hasWork, isFalse);
      expect(second.hasWork, isFalse);
    },
  );
}
