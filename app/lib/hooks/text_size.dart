import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";

/// For some reason the differences between the Text widget and the TextPainter
/// are not the same. This is a correction factor to make them the same.
const flutterPainterErrorCorrection = 1.035;

/// Returns the size of the text with the given [style] in the given [context].
Size useTextSize(BuildContext context, String text, [TextStyle? style]) {
  return useMemoized(
    () {
      final defaultTextStyle = DefaultTextStyle.of(context).style;
      final defaultFontFamily = defaultTextStyle.fontFamily;

      final textPainter = TextPainter(
        text: TextSpan(
          text: text,
          style:
              style?.apply(fontFamily: style.fontFamily ?? defaultFontFamily) ??
                  DefaultTextStyle.of(context).style,
        ),
        maxLines: 1,
        textScaler: MediaQuery.of(context).textScaler,
        textDirection: TextDirection.ltr,
      )..layout(minWidth: 0, maxWidth: double.infinity);

      // TODO: Remove this once the bug is fixed
      final size = textPainter.size;
      return Size(
        (size.width * flutterPainterErrorCorrection).roundToDouble(),
        size.height,
      );
    },
    [text, style],
  );
}
