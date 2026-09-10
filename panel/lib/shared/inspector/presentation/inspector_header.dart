import "package:flutter/widgets.dart" hide Title;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Header for a displaying title and identifier.
class InspectorHeader extends HookWidget {
  const InspectorHeader({
    required this.id,
    required this.name,
    required this.color,
    super.key,
  });

  final String id;
  final String name;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Title(title: name, color: color),
        SizedBox(height: context.spacing.space2),
        Identifier(id: id),
      ],
    );
  }
}

String _stringFormatted(String name) => name.formatted;

class ManagedInspectorHeader extends HookWidget {
  const ManagedInspectorHeader({
    required this.id,
    required this.owner,
    required this.fallbackName,
    required this.fallbackColor,
    this.nameField = "name",
    this.colorField = "color",
    this.nameFormatter = _stringFormatted,
    super.key,
  });

  final String id;
  final EditOwner owner;
  final String fallbackName;
  final Color fallbackColor;
  final String nameField;
  final String? colorField;
  final String Function(String)? nameFormatter;

  @override
  Widget build(BuildContext context) {
    useListenable(owner);

    final nameValue = owner.value(DataPath.root.field(nameField));
    final field = colorField;
    final colorValue = field == null
        ? null
        : owner.value(DataPath.root.field(field));

    final name = nameValue.valueOrNull?.asStringOrNull ?? fallbackName;
    final formattedName = nameFormatter?.call(name) ?? name;

    return InspectorHeader(
      id: id,
      name: formattedName,
      color: colorValue?.valueOrNull?.asColorOrNull ?? fallbackColor,
    );
  }
}
