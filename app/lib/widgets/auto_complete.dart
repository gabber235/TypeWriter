import "package:flutter/material.dart";
import "package:flutter/services.dart";

class AutoCompleteField extends StatelessWidget {
  const AutoCompleteField({
    required this.text,
    required this.onQuery,
    required this.onChanged,
    this.icon,
    this.hintText = "Enter a value",
    this.inputFormatters,
    super.key,
  });
  final String text;
  final String hintText;
  final IconData? icon;

  final Iterable<String> Function(String) onQuery;
  final Function(String) onChanged;
  final List<TextInputFormatter>? inputFormatters;

  @override
  Widget build(BuildContext context) => Autocomplete<String>(
        initialValue: TextEditingValue(text: text),
        fieldViewBuilder: (context, textEditingController, focusNode, onFieldSubmitted) => TextField(
          controller: textEditingController,
          focusNode: focusNode,
          onSubmitted: (value) {
            onChanged(value);
            onFieldSubmitted();
          },
          onEditingComplete: () {
            onChanged(textEditingController.text);
            onFieldSubmitted();
          },
          onChanged: onChanged,
          textCapitalization: TextCapitalization.none,
          textInputAction: TextInputAction.done,
          maxLines: 1,
          inputFormatters: inputFormatters,
          decoration: InputDecoration(
            hintText: hintText,
            prefixIcon: icon != null ? Icon(icon, size: 18) : null,
          ),
        ),
        onSelected: onChanged,
        optionsBuilder: (textEditingValue) {
          if (textEditingValue.text.trim().isEmpty) return const Iterable.empty();
          return onQuery(textEditingValue.text).where((value) => value.contains(textEditingValue.text)).toSet();
        },
      );
}
