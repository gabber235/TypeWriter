import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:google_fonts/google_fonts.dart";

class Dropdown<T> extends StatelessWidget {

  const Dropdown({
    super.key,
    required this.value,
    required this.values,
    required this.onChanged,
    this.filled = true,
    this.padding = const EdgeInsets.symmetric(horizontal: 16),
    this.builder,
    this.icon = FontAwesomeIcons.caretDown,
    this.borderRadius,
    this.alignment,
  });
  final T value;
  final List<T> values;
  final Function(T value) onChanged;

  final IconData? icon;
  final bool filled;
  final EdgeInsets padding;
  final BorderRadius? borderRadius;
  final AlignmentDirectional? alignment;

  final Widget Function(BuildContext context, T value)? builder;

  @override
  Widget build(BuildContext context) => Container(
      padding: padding,
      decoration: BoxDecoration(
        color: filled ? Theme.of(context).inputDecorationTheme.fillColor : null,
        borderRadius: borderRadius ?? BorderRadius.circular(8),
      ),
      child: DropdownButton<T>(
        icon: Icon(icon, size: icon != null ? 18 : 0),
        value: value,
        underline: Container(),
        alignment: alignment ?? AlignmentDirectional.centerStart,
        style: GoogleFonts.jetBrainsMono(
            textStyle:
                TextStyle(color: Theme.of(context).textTheme.bodyText1!.color),),
        borderRadius: borderRadius ?? BorderRadius.circular(8),
        items: values
            .map((e) => DropdownMenuItem(
                  alignment: alignment ?? AlignmentDirectional.centerStart,
                  value: e,
                  child: builder != null
                      ? builder!(context, e)
                      : Text(e.toString()),
                ),)
            .toList(),
        onChanged: (value) {
          if (value == null) return;
          onChanged(value);
        },
      ),
    );
}
