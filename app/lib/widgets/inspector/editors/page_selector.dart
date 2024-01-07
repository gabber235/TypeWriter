import "package:flutter/material.dart" hide Page;
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/smart_single_activator.dart";
import "package:typewriter/widgets/components/app/page_search.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

class PageSelectorEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is PrimitiveField &&
      info.type == PrimitiveFieldType.string &&
      info.hasModifier("page");

  @override
  Widget build(String path, FieldInfo info) =>
      PageSelectorEditor(path: path, field: info as PrimitiveField);
}

class PageSelectorEditor extends HookConsumerWidget {
  const PageSelectorEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final PrimitiveField field;

  bool _update(PassingRef ref, Page? page) {
    if (page == null) return false;
    ref
        .read(inspectingEntryDefinitionProvider)
        ?.updateField(ref, path, page.pageName);
    return true;
  }

  void _select(PassingRef ref, PageType type) {
    ref.read(searchProvider.notifier).asBuilder()
      ..pageType(type, canRemove: false)
      ..fetchPage(onSelect: (page) => _update(ref, page))
      ..fetchAddPage(onAdded: (page) => _update(ref, page))
      ..open();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final typeTag = field.get("page");
    final type = typeTag == null ? null : PageType.fromName(typeTag);

    if (type == null) {
      return const Text("Invalid page type");
    }

    final pageId = ref.watch(fieldValueProvider(path, ""));
    final hasPage = ref.watch(pageExistsProvider(pageId));

    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: ContextMenuRegion(
        builder: (context) {
          return [
            if (hasPage) ...[
              ContextMenuTile.button(
                title: "Navigate to entry",
                icon: FontAwesomeIcons.pencil,
                onTap: () {
                  ref.read(appRouter).navigateToPage(ref.passing, pageId);
                },
              ),
              ContextMenuTile.button(
                title: "Remove reference",
                icon: FontAwesomeIcons.solidSquareMinus,
                color: Colors.redAccent,
                onTap: () {
                  ref
                      .read(inspectingEntryDefinitionProvider)
                      ?.updateField(ref.passing, path, null);
                },
              ),
            ],
            if (!hasPage) ...[
              ContextMenuTile.button(
                title: "Select entry",
                icon: FontAwesomeIcons.magnifyingGlass,
                onTap: () {
                  _select(ref.passing, type);
                },
              ),
            ],
          ];
        },
        child: InkWell(
          onTap: () {
            if (hasOverrideDown && hasPage) {
              ref.read(appRouter).navigateToPage(ref.passing, pageId);
              return;
            }
            _select(ref.passing, type);
          },
          borderRadius: BorderRadius.circular(8),
          child: Padding(
            padding: EdgeInsets.only(
                left: hasPage ? 4 : 12,
                right: 16,
                top: hasPage ? 4 : 12,
                bottom: hasPage ? 4 : 12),
            child: Row(
              children: [
                if (!hasPage) ...[
                  FaIcon(
                    FontAwesomeIcons.database,
                    size: 16,
                    color:
                        Theme.of(context).inputDecorationTheme.hintStyle?.color,
                  ),
                  const SizedBox(width: 12),
                ],
                if (hasPage)
                  Expanded(child: _SelectedPage(id: pageId))
                else
                  Expanded(
                    child: Text("Select a ${type.tag} page",
                        style:
                            Theme.of(context).inputDecorationTheme.hintStyle),
                  ),
                const SizedBox(width: 12),
                FaIcon(
                  FontAwesomeIcons.caretDown,
                  size: 16,
                  color:
                      Theme.of(context).inputDecorationTheme.hintStyle?.color,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _SelectedPage extends HookConsumerWidget {
  const _SelectedPage({
    required this.id,
  });

  final String id;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pageType = ref.watch(pageTypeProvider(id));
    const chapter = "some.chapter"; // TODO: Get chapter from page
    return Material(
      borderRadius: BorderRadius.circular(4),
      color: pageType.color,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
        child: Row(
          children: [
            Icon(pageType.icon, color: Colors.white, size: 18),
            const SizedBox(width: 8),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  id.formatted,
                  style: const TextStyle(color: Colors.white, fontSize: 13),
                ),
                Text(
                  chapter.formatted,
                  style: const TextStyle(color: Colors.white70, fontSize: 11),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
