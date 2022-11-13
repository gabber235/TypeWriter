import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';

class SingleLineTextField extends HookWidget {
  final String? text;
  final Function(String)? onChanged;
  final List<TextInputFormatter>? inputFormatters;
  final TextInputType keyboardType;
  final String? hintText;
  final IconData? icon;

  const SingleLineTextField({
    super.key,
    this.text,
    this.onChanged,
    this.inputFormatters,
    this.keyboardType = TextInputType.text,
    this.hintText,
    this.icon,
  }) : super();

  @override
  Widget build(BuildContext context) {
    final controller = useTextEditingController(text: text);
    return TextField(
      controller: controller,
      onEditingComplete: () => onChanged?.call(controller.text),
      onSubmitted: onChanged,
      onChanged: onChanged,
      textCapitalization: TextCapitalization.none,
      textInputAction: TextInputAction.done,
      maxLines: 1,
      keyboardType: keyboardType,
      inputFormatters: [
        FilteringTextInputFormatter.singleLineFormatter,
        if (inputFormatters != null) ...inputFormatters!,
      ],
      decoration: InputDecoration(
        prefixIcon: icon != null ? Icon(icon, size: 18) : null,
        hintText: hintText,
      ),
    );
  }
}
