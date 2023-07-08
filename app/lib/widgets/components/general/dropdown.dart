import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:google_fonts/google_fonts.dart";

class Dropdown<T> extends HookWidget {
  const Dropdown({
    required this.value,
    required this.values,
    required this.onChanged,
    this.focusNode,
    this.filled = true,
    this.padding = const EdgeInsets.symmetric(horizontal: 12),
    this.builder,
    this.icon = FontAwesomeIcons.list,
    this.borderRadius,
    this.alignment,
    super.key,
  });
  final T value;
  final List<T> values;
  final Function(T value) onChanged;

  final FocusNode? focusNode;
  final IconData? icon;
  final bool filled;
  final EdgeInsets padding;
  final BorderRadius? borderRadius;
  final AlignmentDirectional? alignment;

  final Widget Function(BuildContext context, T value)? builder;

  @override
  Widget build(BuildContext context) {
    final focusNode = this.focusNode ?? useFocusNode();

    return Container(
      padding: padding,
      decoration: BoxDecoration(
        color: filled ? Theme.of(context).inputDecorationTheme.fillColor : null,
        borderRadius: borderRadius ?? BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          if (icon != null) ...[
            Icon(icon, color: const Color(0xFFBEBEBE), size: 16),
            const SizedBox(width: 12),
          ],
          Expanded(
            child: DropdownButton<T>(
              focusNode: focusNode,
              value: value,
              icon: Container(),
              underline: Container(),
              alignment: alignment ?? AlignmentDirectional.centerStart,
              style: GoogleFonts.jetBrainsMono(
                textStyle: TextStyle(
                  color: Theme.of(context).textTheme.bodyLarge!.color,
                ),
              ),
              focusColor: Colors.transparent,
              borderRadius: borderRadius ?? BorderRadius.circular(8),
              items: values
                  .map(
                    (e) => DropdownMenuItem(
                      alignment: alignment ?? AlignmentDirectional.centerStart,
                      value: e,
                      child: builder != null ? builder!(context, e) : Text(e.toString()),
                    ),
                  )
                  .toList(),
              onChanged: (value) {
                if (value == null) return;
                onChanged(value);
              },
            ),
          ),
          const Icon(
            FontAwesomeIcons.caretDown,
            size: 16,
            color: Color(0xFFBEBEBE),
          ),
        ],
      ),
    );
  }
}
