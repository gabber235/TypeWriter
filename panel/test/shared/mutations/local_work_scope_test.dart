import "dart:async";

import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/typewriter_panel.dart";
import "package:typewriter_testkit/typewriter_testkit.dart";

skir.RecordId _id(String table, String key) =>
    skir.RecordId(table: table, key: skir.RecordIdKey.wrapString(key));

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  late ProviderContainer container;
  late skir.RecordId organization;
  late skir.RecordId realm;

  setUp(() async {
    organization = _id("organization", "first");
    realm = _id("realm", "first");
    container = ProviderContainer.test(
      overrides: [
        userIdProvider.overrideWith((ref) async => "user"),
        organizationIdProvider.overrideWith((ref) => organization),
        realmIdProvider.overrideWith((ref) => realm),
      ],
    );
    await container.read(userIdProvider.future);
  });

  Future<void> switchOrganization() async {
    organization = _id("organization", "second");
    container.invalidate(organizationIdProvider);
    await container.pump();
  }

  test(
    "realm navigation retains drafts and organization navigation drops them",
    () async {
      final workspace = container.read(localWorkProvider);
      final target = fakeEditorTarget(
        targetId: "resource",
        label: "Draft",
        scope: (organization, realm),
        document: const EditorDocument(
          rootType: StringType(),
          typeCatalog: TypeCatalog([]),
          confirmedValue: StringValue("Original"),
          revision: 1,
        ),
        commitPolicy: EditorCommitPolicy.applyResource,
        commit: (_) async => throw StateError("No save expected"),
      );
      final source = workspace.editor(target)
        ..update(DataPath.root, const StringValue("Draft"));

      realm = _id("realm", "second");
      container.invalidate(realmIdProvider);
      await container.pump();
      expect(container.read(localWorkProvider), same(workspace));
      expect(
        source.value(DataPath.root).valueOrNull,
        const StringValue("Draft"),
      );

      await switchOrganization();
      final next = container.read(localWorkProvider);
      expect(next, isNot(same(workspace)));
      expect(next.resources, isEmpty);
      expect(workspace.resources, isEmpty);
      expect(await source.flush(), isA<MutationUnavailable>());
    },
  );

  test(
    "switching forgets a sent request and ignores its late response",
    () async {
      final workspace = container.read(localWorkProvider);
      final response = Completer<SubmissionResult<String>>();
      final sent = Completer<void>();
      var integrations = 0;
      final submission = workspace.start(
        PreparedCommit<String>(
          id: "sent",
          label: "Sent request",
          send: () {
            sent.complete();
            return response.future;
          },
          integrate: (_) async {
            integrations++;
          },
        ),
      );
      final result = submission.run();

      await sent.future;

      await switchOrganization();
      expect(container.read(localWorkProvider).submissions, isEmpty);
      expect(workspace.submissions, isEmpty);
      response.complete(const SubmissionConfirmed("Applied remotely"));
      expect(await result, isA<SubmissionConfirmed<String>>());
      expect(integrations, 0);

      expect(submission.run, throwsStateError);
    },
  );

  test(
    "switching forgets uncertain requests and disables their replay",
    () async {
      final workspace = container.read(localWorkProvider);
      final submission = workspace.start(
        PreparedCommit<String>(
          id: "uncertain",
          label: "Uncertain request",
          replay: SubmissionReplay.identicalRequest,
          send: () async => SubmissionUncertain(
            message: "Lost response",
            cause: TimeoutException("Lost response"),
            stackTrace: StackTrace.current,
          ),
        ),
      );
      expect(await submission.run(), isA<SubmissionUncertain<String>>());

      await switchOrganization();
      expect(container.read(localWorkProvider).submissions, isEmpty);
      expect(workspace.submissions, isEmpty);
      expect(submission.run, throwsStateError);
    },
  );
}
