import "package:flutter/material.dart";

class InputField extends StatelessWidget {
  const InputField({
    required this.child,
    super.key,
  });

  InputField.icon({
    required Widget child,
    required Widget icon,
    Key? key,
  }) : this(
          child: _IconInputField(
            icon: icon,
            child: child,
          ),
          key: key,
        );

  final Widget child;
  @override
  Widget build(BuildContext context) {
    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: child,
    );
  }
}

class _IconInputField extends StatelessWidget {
  const _IconInputField({
    required this.icon,
    required this.child,
  });

  final Widget icon;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      child: Row(
        children: [
          IconTheme(
            data: IconThemeData(
              color: Theme.of(context).inputDecorationTheme.hintStyle?.color,
            ),
            child: icon,
          ),
          const SizedBox(width: 8),
          child,
        ],
      ),
    );
  }
}
