import "package:flutter/material.dart";
import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";
import "package:widgetbook_workspace/stories/shared/editors/presentation/editor_commit_placement.stories.dart";

void main() {
  testWidgets(
    "controls move between inline and fallback without losing drafts",
    (tester) async {
      await tester.pumpWidget(Builder(builder: editorCommitPlacementStory));
      await tester.pumpAndSettle();
      final input = find
          .descendant(
            of: find.byWidgetPredicate(
              (widget) =>
                  widget is PresentationNodeRenderer &&
                  widget.node.id == "target",
            ),
            matching: find.byType(EditableText),
          )
          .first;
      await tester.enterText(input, "paper@1.23");
      await tester.pumpAndSettle();
      expect(
        tester.getTopLeft(find.text("Apply")).dy,
        lessThan(
          tester.getTopLeft(find.text("Content after configuration")).dy,
        ),
      );
      await tester.tap(find.byType(SwitchListTile));
      await tester.pumpAndSettle();
      expect(find.byType(EditorCommitControls), findsOneWidget);
      expect(tester.widget<EditableText>(input).controller.text, "paper@1.23");
      expect(
        tester.getTopLeft(find.text("Apply")).dy,
        greaterThan(
          tester.getTopLeft(find.text("Content after configuration")).dy,
        ),
      );
      await tester.tap(find.text("Apply"));
      await tester.pumpAndSettle();
      expect(find.text("Cancel"), findsNothing);
      expect(find.text("Apply"), findsNothing);
    },
  );
}
