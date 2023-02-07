import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";

class DecoratedTextField extends HookWidget {
  const DecoratedTextField({
    required this.focus,
    this.controller,
    this.text,
    this.onChanged,
    this.onSubmitted,
    this.inputFormatters,
    this.keyboardType = TextInputType.text,
    this.decoration,
    this.maxLines = 1,
    this.autofocus = false,
    super.key,
  }) : super();
  final TextEditingController? controller;
  final FocusNode focus;
  final String? text;
  final Function(String)? onChanged;
  final Function(String)? onSubmitted;
  final List<TextInputFormatter>? inputFormatters;
  final TextInputType keyboardType;
  final InputDecoration? decoration;
  final int? maxLines;
  final bool autofocus;

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

    return TextField(
      focusNode: focus,
      controller: controller,
      onEditingComplete: () {
        onSubmitted?.call(controller.text);
        onChanged?.call(controller.text);
      },
      onSubmitted: (value) {
        onSubmitted?.call(value);
        onChanged?.call(value);
      },
      onChanged: onChanged,
      textCapitalization: TextCapitalization.none,
      textInputAction: TextInputAction.done,
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
