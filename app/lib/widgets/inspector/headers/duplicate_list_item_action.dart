import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class DuplicateListItemAction extends HookConsumerWidget {
  const DuplicateListItemAction(
    this.parentPath,
    this.path,
    this.field, {
    super.key,
  });

  final String parentPath;
  final String path;
  final FieldInfo field;

  void _duplicate(PassingRef ref) {
    final parentValue = ref.read(fieldValueProvider(parentPath, []));
    final value = ref.read(fieldValueProvider(path, field.defaultValue));

    ref.read(inspectingEntryDefinitionProvider)?.updateField(
      ref,
      parentPath,
      [...parentValue, value],
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final name = ref.watch(pathDisplayNameProvider(path)).singular;
    return IconButton(
      icon: const Iconify(TWIcons.duplicate, size: 12),
      color: Colors.green,
      tooltip: "Duplicate $name",
      onPressed: () => _duplicate(ref.passing),
    );
  }
}
