import "dart:async";
import "dart:ui" show PointerDeviceKind;
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";
import "../../support/test_utils.dart";

void main() {
  const screenshotDirectory = String.fromEnvironment(
    "SAVE_ACTIVITY_SCREENSHOTS",
  );
  if (screenshotDirectory.isNotEmpty) {
    TestWidgetsFlutterBinding.ensureInitialized();
    setUpAll(() async {
      for (final (family, asset) in [
        (
          "JetBrainsMono",
          "assets/fonts/JetBrains_Mono/JetBrainsMono-VariableFont_wght.ttf",
        ),
        ("Lilex", "assets/fonts/Lilex/Lilex-VariableFont_wght.ttf"),
        ("MaterialIcons", "fonts/MaterialIcons-Regular.otf"),
      ]) {
        await (FontLoader(family)..addFont(rootBundle.load(asset))).load();
      }
    });
  }
  testWidgets(
    "successful feedback expires after ten seconds and later saves appear again",
    (tester) async {
      final workspace = LocalWork();
      final journal = workspace;

      addTearDown(workspace.dispose);

      tester.view.devicePixelRatio = 1;
      tester.view.physicalSize = const Size(1280, 800);
      addTearDown(tester.view.resetPhysicalSize);
      addTearDown(tester.view.resetDevicePixelRatio);
      await tester.binding.setSurfaceSize(const Size(1280, 800));
      addTearDown(() => tester.binding.setSurfaceSize(null));

      await tester.pumpTestApp(
        child: Scaffold(
          appBar: AppBar(actions: [MutationActivityView(workspace: workspace)]),
        ),
      );

      expect(find.text("Saved"), findsNothing);
      Future<void> save(int id) async {
        final submission = MutationSubmission<int>(
          id: id,
          label: "Save host",
          send: () async => SubmissionResult.confirmed(id),
        );
        journal.track(submission);
        await submission.run();
      }

      await save(1);
      await tester.pumpAndSettle();
      expect(find.text("Saved"), findsOneWidget);

      final button = tester.widget<TextButton>(
        find.widgetWithText(TextButton, "Saved"),
      );
      final context = tester.element(find.widgetWithText(TextButton, "Saved"));
      expect(
        button.style!.foregroundColor!.resolve({}),
        context.colors.success,
      );

      await tester.tap(find.text("Saved"));
      await tester.pumpAndSettle();
      expect(find.text("Save activity"), findsOneWidget);
      await tester.pump(const Duration(seconds: 5));
      expect(journal.submissions, hasLength(1));
      await tester.pump(const Duration(seconds: 5));

      await tester.pumpAndSettle();
      expect(journal.submissions, isEmpty);
      expect(find.text("Saved"), findsNothing);
      expect(find.text("All saved"), findsNothing);
      expect(find.text("No pending changes"), findsOneWidget);

      await tester.tap(find.byTooltip("Close"));
      await tester.pumpAndSettle();
      expect(find.text("Save activity"), findsNothing);
      expect(tester.takeException(), isNull);

      await save(2);
      await tester.pumpAndSettle();
      expect(find.text("Saved"), findsOneWidget);
      await tester.pump(savedFeedbackDuration);
      await tester.pumpAndSettle();
    },
  );

  for (final mobile in [false, true]) {
    testWidgets(
      "activity uses ${mobile ? "a padded mobile sheet" : "an anchored desktop popup"}",
      (tester) async {
        tester.view.devicePixelRatio = 1;
        tester.view.physicalSize = Size(mobile ? 390 : 1280, 800);
        addTearDown(tester.view.resetPhysicalSize);
        addTearDown(tester.view.resetDevicePixelRatio);
        await tester.binding.setSurfaceSize(Size(mobile ? 390 : 1280, 800));
        addTearDown(() => tester.binding.setSurfaceSize(null));

        final workspace = LocalWork();
        final journal = workspace;

        addTearDown(workspace.dispose);
        final submission = MutationSubmission<void>(
          id: "failed",
          label: "Gabbers Little Corner: configuration",
          send: () async => const SubmissionResult.rejected(
            message: "Choose a supported engine target",
          ),
        );
        journal.track(submission);
        await submission.run();
        await tester.pumpTestApp(
          child: Scaffold(
            appBar: AppBar(
              actions: [MutationActivityView(workspace: workspace)],
            ),
          ),
        );
        if (mobile) {
          await tester.tap(find.byTooltip("Needs attention"));
        } else {
          await tester.tap(find.text("Needs attention"));
        }

        await tester.pumpAndSettle();
        expect(find.text("Save activity"), findsOneWidget);
        expect(
          find.byType(BottomSheet),
          mobile ? findsOneWidget : findsNothing,
        );
        expect(
          find.text("Gabbers Little Corner: configuration"),
          findsOneWidget,
        );
        expect(tester.takeException(), isNull);
        if (!mobile) {
          final mouse = await tester.createGesture(
            kind: PointerDeviceKind.mouse,
          );
          await mouse.addPointer(location: Offset.zero);
          await mouse.moveTo(tester.getCenter(find.byTooltip("Close")));
          await tester.pump(const Duration(seconds: 1));
          await tester.pump();
          expect(tester.takeException(), isNull);

          await mouse.removePointer();
          await tester.pumpAndSettle();
        }

        if (screenshotDirectory.isNotEmpty) {
          await tester.captureScreenshot(
            mobile ? "save-activity-mobile" : "save-activity-desktop",
            directory: screenshotDirectory,
          );
        }
        if (mobile) {
          expect(
            tester.getTopLeft(find.text("Save activity")).dx,
            greaterThan(16),
          );
          await tester.tap(find.byTooltip("Close"));
        } else {
          await tester.sendKeyEvent(LogicalKeyboardKey.escape);
        }
        await tester.pumpAndSettle();
        expect(find.text("Save activity"), findsNothing);
        await tester.pump(const Duration(seconds: 20));
        expect(journal.submissions, hasLength(1));
      },
    );
  }

  testWidgets(
    "attention outranks drafts and stays visible while other work saves",
    (tester) async {
      final failed = MutationSubmission<void>(
        id: 1,
        label: "Failed",
        send: () async => const SubmissionResult.rejected(message: "Rejected"),
      );
      final response = Completer<SubmissionResult<void>>();
      final saving = MutationSubmission<void>(
        id: 2,
        label: "Saving",
        send: () => response.future,
      );
      addTearDown(failed.dispose);
      addTearDown(saving.dispose);
      await failed.run();

      final operation = saving.run();
      expect(
        MutationActivityPhase.resolve([failed, saving], const []),
        MutationActivityPhase.savingWithAttention,
      );
      response.complete(const SubmissionResult.confirmed(null));
      await operation;
      expect(
        MutationActivityPhase.resolve([failed, saving], const []),
        MutationActivityPhase.needsAttention,
      );
    },
  );

  testWidgets(
    "already confirmed submissions expire but uncertain outcomes do not",
    (tester) async {
      final journal = LocalWork();

      final confirmed = MutationSubmission<void>(
        id: 1,
        label: "Saved",
        send: () async => const SubmissionResult.confirmed(null),
      );
      final uncertain = MutationSubmission<void>(
        id: 2,
        label: "Unknown",
        send: () async => throw StateError("Lost reply"),
      );
      addTearDown(journal.dispose);
      await confirmed.run();
      await uncertain.run();
      journal
        ..track(confirmed)
        ..track(uncertain);

      await tester.pump(savedFeedbackDuration);
      expect(journal.submissions, [uncertain]);
      journal.dismiss(uncertain.id);
      expect(journal.submissions, [uncertain]);
    },
  );
}
