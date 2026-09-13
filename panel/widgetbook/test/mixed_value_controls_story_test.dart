import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";
import "package:widgetbook_workspace/stories/shared/editors/presentation/mixed_value_controls.stories.dart";

void main() {
  testWidgets("mixed story preserves every specialized control", (
    tester,
  ) async {
    await tester.pumpWidget(const MixedValueControls(mixed: true));
    await tester.pumpAndSettle();

    expect(find.byType(MixedColorSwatch), findsOneWidget);
    expect(find.byType(MixedSlider), findsOneWidget);
    expect(
      tester.widget<ColorPickerField>(find.byType(ColorPickerField)).color,
      isNull,
    );
    expect(
      tester
          .widget<DateTimePickerField>(find.byType(DateTimePickerField))
          .value,
      isNull,
    );
    expect(find.text(mixedValueReplacementMessage), findsNWidgets(5));
  });

  testWidgets("shared story renders concrete specialized controls", (
    tester,
  ) async {
    await tester.pumpWidget(const MixedValueControls(mixed: false));
    await tester.pumpAndSettle();

    expect(find.byType(MixedColorSwatch), findsNothing);
    expect(find.byType(MixedSlider), findsNothing);
    expect(
      tester.widget<ColorPickerField>(find.byType(ColorPickerField)).color,
      isNotNull,
    );
    expect(
      tester
          .widget<DateTimePickerField>(find.byType(DateTimePickerField))
          .value,
      isNotNull,
    );
    expect(find.text(mixedValueReplacementMessage), findsNothing);
  });
}
