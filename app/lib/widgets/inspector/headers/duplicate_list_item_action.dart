import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class DuplicateListItemAction extends HookConsumerWidget {
  const DuplicateListItemAction({
    required this.parentPath,
    required this.path,
    required this.dataBlueprint,
    super.key,
  });

  final String parentPath;
  final String path;
  final DataBlueprint dataBlueprint;

  void _duplicate(PassingRef ref) {
    final parentValue = ref.read(fieldValueProvider(parentPath, []));
    final value =
        ref.read(fieldValueProvider(path, dataBlueprint.defaultValue()));

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
