import "package:flutter_test/flutter_test.dart";
import "package:widgetbook_workspace/stories/shared/ui/components/selection_initialization.stories.dart";

void main() {
  testWidgets(
    "story uses the sole option then preserves it when defaults change",
    (tester) async {
      await tester.pumpWidget(const SelectionInitializationStory());
      await tester.pumpAndSettle();
      expect(find.text("Draft value: 1"), findsOneWidget);
      await tester.pumpWidget(
        const SelectionInitializationStory(count: 3, preferred: 2),
      );
      await tester.pumpAndSettle();
      expect(find.text("Draft value: 1"), findsOneWidget);
      expect(tester.takeException(), isNull);
    },
  );
}
