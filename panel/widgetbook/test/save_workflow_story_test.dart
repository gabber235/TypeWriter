import "package:flutter/material.dart";
import "package:flutter_test/flutter_test.dart";
import "package:widgetbook_workspace/stories/shared/editors/presentation/save_workflow.stories.dart";

void main() {
  testWidgets("shared activity shows Apply and preserves uncertain evidence", (
    tester,
  ) async {
    await tester.binding.setSurfaceSize(const Size(1100, 800));
    addTearDown(() => tester.binding.setSurfaceSize(null));
    await tester.pumpWidget(Builder(builder: mutationActivityButtonStory));
    await tester.pumpAndSettle();
    await tester.enterText(find.byType(EditableText), "paper@1.23");
    await tester.pumpAndSettle();
    expect(find.text("1 draft"), findsOneWidget);
    await tester.tap(find.text("Apply"));
    await tester.pump();
    expect(find.text("Saving"), findsWidgets);
    expect(
      tester.widget<EditableText>(find.byType(EditableText)).readOnly,
      isTrue,
    );
    await tester.pump(const Duration(seconds: 2));
    await tester.pumpAndSettle();
    expect(find.text("Saved"), findsOneWidget);
    await tester.tap(find.byType(SwitchListTile));
    await tester.enterText(find.byType(EditableText), "paper@1.24");
    await tester.pumpAndSettle();
    await tester.tap(find.text("Apply"));
    await tester.pump(const Duration(seconds: 2));
    await tester.pumpAndSettle();
    expect(find.text("Needs attention"), findsOneWidget);
    await tester.tap(find.text("Needs attention"));
    await tester.pumpAndSettle();
    expect(find.text("Retry captured request"), findsNothing);
    expect(
      tester
          .widget<TextButton>(find.widgetWithText(TextButton, "Discard"))
          .onPressed,
      isNull,
    );
    expect(
      find.text("Outcome unknown. Verify before submitting again."),
      findsOneWidget,
    );
  });
}
