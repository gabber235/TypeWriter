import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/widgets/components/general/admonition.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/editors/field.dart";
import "package:typewriter/widgets/inspector/inspector.dart";
import "package:url_launcher/url_launcher_string.dart";

class GenericEditorFilter extends EditorFilter {
  @override
  bool canEdit(DataBlueprint dataBlueprint) =>
      dataBlueprint is CustomBlueprint && dataBlueprint.editor == "generic";

  @override
  Widget build(String path, DataBlueprint dataBlueprint) => GenericEditor(
        path: path,
        customBlueprint: dataBlueprint as CustomBlueprint,
      );
}

class GenericEditor extends HookConsumerWidget {
  const GenericEditor({
    required this.path,
    required this.customBlueprint,
    super.key,
  });

  final String path;
  final CustomBlueprint customBlueprint;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    DataBlueprint blueprint;

    final generic = Generic.maybeOf(context);
    if (generic == null) {
      final entry = ref.watch(inspectingEntryProvider);
      if (entry == null) return _buildError();

      final genericBlueprint = entry.genericBlueprint;
      if (genericBlueprint == null) return _buildError();
      blueprint = genericBlueprint;
    } else {
      blueprint = generic.dataBlueprint;
    }

    return FieldEditor(path: path, dataBlueprint: blueprint);
  }

  Widget _buildError() {
    return Admonition.danger(
      onTap: () => launchUrlString("https://discord.gg/uYfnzJAFZN"),
      child: Text.rich(
        TextSpan(
          text: "Could not find Generic information in the context, ",
          children: [
            TextSpan(
              text: "please report this in the Typewriter Discord",
              style: TextStyle(
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class Generic extends InheritedWidget {
  const Generic({
    required this.dataBlueprint,
    required super.child,
    super.key,
  });

  final DataBlueprint dataBlueprint;

  @override
  bool updateShouldNotify(covariant Generic oldWidget) {
    return dataBlueprint != oldWidget.dataBlueprint;
  }

  static Generic? maybeOf(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<Generic>();
}
