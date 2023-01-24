import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";

/// Returns the size of the text with the given [style] in the given [context].
Size useTextSize(BuildContext context, String text, [TextStyle? style]) {
  return useMemoized(
    () {
      final textPainter = TextPainter(
        text: TextSpan(
          text: text,
          style: style ?? DefaultTextStyle.of(context).style,
        ),
        maxLines: 1,
        textScaleFactor: MediaQuery.of(context).textScaleFactor,
        textDirection: TextDirection.ltr,
      )..layout(minWidth: 0, maxWidth: double.infinity);
      return textPainter.size;
    },
    [text, style],
  );
}
