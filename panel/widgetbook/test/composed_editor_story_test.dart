import "package:flutter/material.dart";
import "package:flutter_test/flutter_test.dart";
import "package:widgetbook_workspace/stories/shared/editors/presentation/composed_editor.stories.dart";

void main() {
  testWidgets(
    "runtime reports preserve the edited value through a delayed save",
    (tester) async {
      await tester.pumpWidget(Builder(builder: composedEditorUseCase));
      await tester.pumpAndSettle();
      final name = find.byType(TextField).first;
      await tester.enterText(name, "quest_host");
      await tester.tap(find.text("Receive runtime report"));
      await tester.pump();
      expect(find.text("Failed. Report 2"), findsOneWidget);
      expect(tester.widget<TextField>(name).controller!.text, "quest_host");
      await tester.testTextInput.receiveAction(TextInputAction.done);
      await tester.pump(const Duration(milliseconds: 300));
      await tester.pump(const Duration(milliseconds: 1300));
      await tester.pumpAndSettle();
      expect(tester.widget<TextField>(name).controller!.text, "quest_host");
      expect(find.text("Saved"), findsNothing);
      expect(tester.takeException(), isNull);
    },
  );
}
