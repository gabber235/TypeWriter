import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";

class DecoratedTextField extends HookWidget {
  const DecoratedTextField({
    required this.focus,
    this.controller,
    this.text,
    this.onChanged,
    this.onDone,
    this.onSubmitted,
    this.style,
    this.inputFormatters,
    this.keyboardType = TextInputType.text,
    this.decoration,
    this.maxLines = 1,
    this.autofocus = false,
    this.textAlign = TextAlign.start,
    super.key,
  }) : super();
  final TextEditingController? controller;
  final FocusNode focus;
  final String? text;

  /// Called any time the text changes.
  final Function(String)? onChanged;

  /// Called when the user is done editing. Either by pressing done, or by losing focus.
  final Function(String)? onDone;

  /// Called when the user presses done.
  final Function(String)? onSubmitted;
  final TextStyle? style;
  final List<TextInputFormatter>? inputFormatters;
  final TextInputType keyboardType;
  final InputDecoration? decoration;
  final int? maxLines;
  final bool autofocus;
  final TextAlign textAlign;

  @override
  Widget build(BuildContext context) {
    final controller = this.controller ?? useTextEditingController(text: text);

    // When we are not focused, we want to update the controller with the latest.
    // Since other people may update the text and we want that reflected.
    // However, when we are focused, we don't want to update the controller as this causes the cursor to jump.
    useEffect(
      () {
        if (!focus.hasFocus) {
          controller.text = text ?? "";
        }
        return null;
      },
      [text],
    );

    useFocusedChange(focus, ({required hasFocus}) {
      if (!hasFocus) {
        onDone?.call(controller.text);
      }
    });

    return TextField(
      focusNode: focus,
      controller: controller,
      onEditingComplete: () {
        onDone?.call(controller.text);
        onChanged?.call(controller.text);
        onSubmitted?.call(controller.text);
      },
      onSubmitted: (value) {
        onDone?.call(value);
        onChanged?.call(value);
        onSubmitted?.call(value);
      },
      onChanged: onChanged,
      style: style,
      textCapitalization: TextCapitalization.none,
      textInputAction:
          maxLines == 1 ? TextInputAction.done : TextInputAction.newline,
      textAlign: textAlign,
      maxLines: maxLines,
      autofocus: autofocus,
      keyboardType: keyboardType,
      inputFormatters: [
        if (inputFormatters != null) ...inputFormatters!,
      ],
      decoration: decoration,
    );
  }
}
