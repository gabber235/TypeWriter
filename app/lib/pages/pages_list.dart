import "dart:async";

import "package:auto_route/auto_route.dart";
import "package:collection/collection.dart";
import "package:flutter/material.dart" hide ContextMenuController, FilledButton;
import "package:flutter/services.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/components/app/empty_screen.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/components/general/dropdown.dart";
import "package:typewriter/widgets/components/general/filled_button.dart";
import "package:typewriter/widgets/components/general/formatted_text_field.dart";
import "package:typewriter/widgets/components/general/iconify.dart";
import "package:typewriter/widgets/components/general/tree_view.dart";
import "package:typewriter/widgets/components/general/validated_text_field.dart";

part "pages_list.freezed.dart";
part "pages_list.g.dart";

@freezed
class _PageData with _$PageData {
  const factory _PageData({
    required String name,
    required PageType type,
    required String chapter,
  }) = _$__PageData;
}

@riverpod
List<_PageData> _pagesData(_PagesDataRef ref) {
  return ref
      .watch(bookProvider)
      .pages
      .map(
        (page) => _PageData(
          name: page.pageName,
          type: page.type,
          chapter: page.chapter,
        ),
      )
      .toList();
}

@riverpod
RootTreeNode<_PageData> _pagesTree(_PagesTreeRef ref) {
  return createTreeNode(
    ref.watch(_pagesDataProvider),
    (e) => e.chapter,
  );
}

@riverpod
List<String> _pageNames(_PageNamesRef ref) {
  return ref.watch(
    _pagesDataProvider
        .select((pages) => pages.map((page) => page.name).toList()),
  );
}

@RoutePage(name: "PagesListRoute")
class PagesList extends StatelessWidget {
  const PagesList({super.key});

  @override
  Widget build(BuildContext context) {
    return const SizedBox.expand(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          _PagesSelector(),
          Expanded(child: AutoRouter()),
        ],
      ),
    );
  }
}

class _PagesSelector extends HookConsumerWidget {
  const _PagesSelector();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final hovering = useState(false);
    return MouseRegion(
      onEnter: (_) => hovering.value = true,
      onExit: (_) {
        if (ContextMenuController.hasContextMenu) return;
        hovering.value = false;
      },
      child: Container(
        color: const Color(0xFF163260),
        // width: 230,
        height: double.infinity,
        child: AnimatedSize(
          duration: 200.ms,
          curve: Curves.easeInOut,
          alignment: Alignment.topLeft,
          child: IntrinsicWidth(
            child: Padding(
              padding: const EdgeInsets.all(12.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const SizedBox(height: 12),
                  Text(
                    "Pages",
                    style: Theme.of(context)
                        .textTheme
                        .titleMedium
                        ?.copyWith(color: Colors.white),
                  ),
                  const SizedBox(height: 12),
                  Expanded(
                    child: SingleChildScrollView(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          _PagesTree(showFull: hovering.value),
                          const SizedBox(height: 12),
                          if (hovering.value) const _AddPageButton(),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _PagesTree extends HookConsumerWidget {
  const _PagesTree({
    required this.showFull,
  });

  final bool showFull;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tree = ref.watch(_pagesTreeProvider);
    return _TreeChildren(children: tree.children, showFull: showFull);
  }
}

class _TreeChildren extends HookWidget {
  const _TreeChildren({
    required this.children,
    required this.showFull,
  });

  final List<TreeNode<_PageData>> children;
  final bool showFull;

  @override
  Widget build(BuildContext context) {
    final sorted = useMemoized(
      () => children.sorted((a, b) {
        if (a is LeafTreeNode<_PageData> && b is LeafTreeNode<_PageData>) {
          return a.value.name.compareTo(b.value.name);
        } else if (a is InnerTreeNode<_PageData> &&
            b is InnerTreeNode<_PageData>) {
          return a.name.compareTo(b.name);
        } else if (a is LeafTreeNode<_PageData>) {
          return 1;
        } else if (b is LeafTreeNode<_PageData>) {
          return -1;
        } else {
          return 0;
        }
      }),
      children,
    );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        for (final child in sorted) _TreeItem(node: child, showFull: showFull),
      ],
    );
  }
}

class _TreeItem extends HookWidget {
  const _TreeItem({
    required this.node,
    required this.showFull,
  });

  final TreeNode<_PageData> node;
  final bool showFull;

  @override
  Widget build(BuildContext context) {
    // Flutter stupid where final fields are not auto-casted.
    final node = this.node;
    if (node is LeafTreeNode<_PageData>) {
      if (showFull) {
        return _PageTile(
          pageData: node.value,
        );
      } else {
        return _SmallPageTile(
          pageData: node.value,
        );
      }
    } else if (node is InnerTreeNode<_PageData>) {
      return _TreeCategory(node: node, showFull: showFull);
    } else {
      throw UnimplementedError();
    }
  }
}

class _TreeCategory extends HookConsumerWidget {
  const _TreeCategory({
    required this.node,
    required this.showFull,
  });

  final InnerTreeNode<_PageData> node;
  final bool showFull;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isExpanded = useState(false);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        ContextMenuRegion(
          builder: (context) {
            return [
              ContextMenuTile.button(
                title: "New Page",
                icon: TWIcons.plus,
                onTap: () => showDialog(
                  context: context,
                  builder: (_) => AddPageDialogue(chapter: node.path),
                ),
              ),
            ];
          },
          child: DragTarget<PageDrag>(
            onWillAcceptWithDetails: (details) {
              return ref.read(_pageNamesProvider).contains(details.data.pageId);
            },
            onAcceptWithDetails: (details) {
              final pageId = details.data.pageId;
              ref
                  .read(pageProvider(pageId))
                  ?.changeChapter(ref.passing, node.path);
            },
            builder: (context, candidateData, rejectedData) {
              final isAccepting = candidateData.isNotEmpty;
              final isRejecting = rejectedData.isNotEmpty;
              return Material(
                color: Colors.transparent,
                shape: RoundedRectangleBorder(
                  side: isAccepting || isRejecting
                      ? BorderSide(
                          color: isAccepting
                              ? Theme.of(context).colorScheme.primary
                              : Theme.of(context).colorScheme.error,
                          width: 2,
                        )
                      : BorderSide.none,
                  borderRadius: const BorderRadius.all(Radius.circular(8)),
                ),
                child: InkWell(
                  onTap: () => isExpanded.value = !isExpanded.value,
                  borderRadius: BorderRadius.circular(8),
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Row(
                      children: [
                        if (showFull) const SizedBox(width: 4),
                        Padding(
                          padding:
                              EdgeInsets.symmetric(vertical: showFull ? 4 : 0),
                          child: Iconify(
                            isExpanded.value
                                ? TWIcons.chevronDown
                                : TWIcons.chevronRight,
                            size: 11,
                            color: Colors.white,
                          ),
                        ),
                        if (showFull) ...[
                          const SizedBox(width: 8),
                          Expanded(
                            child: Text(
                              node.name.formatted,
                              style: Theme.of(context)
                                  .textTheme
                                  .bodySmall
                                  ?.copyWith(
                                    color: Colors.white,
                                  ),
                            ),
                          ),
                        ],
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
        ),
        AnimatedSize(
          duration: 200.ms,
          curve: Curves.easeInOut,
          alignment: Alignment.topLeft,
          child: isExpanded.value
              ? IntrinsicHeight(
                  child: Row(
                    children: [
                      Container(
                        width: 6,
                        margin: EdgeInsets.only(left: showFull ? 14 : 10),
                        decoration: BoxDecoration(
                          color: Colors.black.withOpacity(0.2),
                          borderRadius: BorderRadius.circular(2),
                        ),
                      ),
                      Expanded(
                        child: _TreeChildren(
                          children: node.children,
                          showFull: showFull,
                        ),
                      ),
                    ],
                  ),
                )
              : const SizedBox(height: 0),
        ),
      ],
    );
  }
}

@riverpod
List<Writer> _writers(_WritersRef ref, String pageId) {
  return ref
      .watch(writersProvider)
      .where((writer) => writer.pageId.hasValue && writer.pageId == pageId)
      .toList();
}

class _PageTile extends HookConsumerWidget {
  const _PageTile({
    required this.pageData,
  });
  final _PageData pageData;

  String get pageId => pageData.name;
  String get chapter => pageData.chapter;

  bool _needsShift(int amount) {
    // If the amount of writer is 0, we never need to shift
    if (amount == 0) return false;
    return true;
  }

  List<ContextMenuTile> _contextMenuItems(
    BuildContext context,
    WidgetRef ref,
    bool isSelected,
  ) {
    return [
      ContextMenuTile.button(
        title: "Rename",
        icon: TWIcons.pencil,
        onTap: () => showDialog(
          context: context,
          builder: (_) => RenamePageDialogue(old: pageId),
        ),
      ),
      ContextMenuTile.button(
        title: "Change Chapter",
        icon: TWIcons.bookMarker,
        onTap: () => showDialog(
          context: context,
          builder: (_) =>
              ChangeChapterDialogue(pageId: pageId, chapter: chapter),
        ),
      ),
      ContextMenuTile.button(
        title: "Change Priority",
        icon: TWIcons.priority,
        onTap: () => showDialog(
          context: context,
          builder: (_) => ChangePagePriorityDialogue(pageId: pageId),
        ),
      ),
      ContextMenuTile.divider(),
      ContextMenuTile.button(
        title: "Delete",
        icon: TWIcons.trash,
        color: Colors.redAccent,
        onTap: () => showPageDeletionDialogue(context, ref.passing, pageId),
      ),
    ];
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected =
        ref.watch(currentPageIdProvider.select((e) => e == pageId));

    return WritersIndicator(
      enabled: !isSelected,
      provider: _writersProvider(pageId),
      shift: (amount) =>
          _needsShift(amount) ? const Offset(4, 30) : Offset.zero,
      builder: (amount) {
        return AnimatedPadding(
          duration: 200.ms,
          curve: Curves.easeInOut,
          padding: EdgeInsets.only(
            top: _needsShift(amount) ? 30 : 0,
          ),
          child: DragTarget<EntryDrag>(
            onWillAcceptWithDetails: (details) {
              final entryId = details.data.entryId;
              final entryType = ref.read(entryTypeProvider(entryId));
              if (entryType == null) return false;
              final entryPageType =
                  ref.read(entryBlueprintPageTypeProvider(entryType));
              return entryPageType == pageData.type;
            },
            onAcceptWithDetails: (details) {
              final entryId = details.data.entryId;
              final fromPageId = ref.read(entryPageIdProvider(entryId));
              if (fromPageId == null) return;
              ref
                  .read(bookProvider.notifier)
                  .moveEntry(entryId, fromPageId, pageId);
            },
            builder: (context, entryCandidateData, entryRejectedData) {
              return DragTarget<PageDrag>(
                onWillAcceptWithDetails: (details) {
                  return ref
                      .read(_pageNamesProvider)
                      .contains(details.data.pageId);
                },
                onAcceptWithDetails: (details) {
                  final pageId = details.data.pageId;
                  ref
                      .read(pageProvider(pageId))
                      ?.changeChapter(ref.passing, chapter);
                },
                builder: (context, pageCandidateData, rejectedData) {
                  final isAccepting = entryCandidateData.isNotEmpty ||
                      pageCandidateData.isNotEmpty;
                  final isRejecting =
                      entryRejectedData.isNotEmpty || rejectedData.isNotEmpty;
                  return Material(
                    color: isSelected
                        ? const Color(0xFF1e3f6f)
                        : Colors.transparent,
                    shape: RoundedRectangleBorder(
                      side: isAccepting || isRejecting
                          ? BorderSide(
                              color: isAccepting
                                  ? Theme.of(context).colorScheme.primary
                                  : Theme.of(context).colorScheme.error,
                              width: 2,
                            )
                          : BorderSide.none,
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: ContextMenuRegion(
                      builder: (context) =>
                          _contextMenuItems(context, ref, isSelected),
                      child: Draggable<PageDrag>(
                        data: PageDrag(pageId: pageId),
                        feedback: Material(
                          color: Colors.transparent,
                          child: DecoratedBox(
                            decoration: BoxDecoration(
                              color: const Color(0xFF1e3f6f),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Row(
                                children: [
                                  Iconify(
                                    pageData.type.icon,
                                    size: 11,
                                    color: Colors.white,
                                  ),
                                  const SizedBox(width: 8),
                                  Text(
                                    pageId.formatted,
                                    style: Theme.of(context)
                                        .textTheme
                                        .bodySmall
                                        ?.copyWith(color: Colors.white),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                        child: InkWell(
                          onTap: () {
                            if (isSelected) return;
                            ref
                                .read(appRouter)
                                .push(PageEditorRoute(id: pageId));
                          },
                          borderRadius: BorderRadius.circular(8),
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Row(
                              children: [
                                const SizedBox(width: 4),
                                Iconify(
                                  pageData.type.icon,
                                  size: 11,
                                  color: Colors.white,
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: Text(
                                    pageId.formatted,
                                    style: Theme.of(context)
                                        .textTheme
                                        .bodySmall
                                        ?.copyWith(
                                          color: Colors.white,
                                        ),
                                  ),
                                ),
                                const SizedBox(width: 8),
                                const Icon(
                                  Icons.chevron_right,
                                  size: 16,
                                  color: Colors.white,
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ),
                  );
                },
              );
            },
          ),
        );
      },
    );
  }
}

class _SmallPageTile extends HookConsumerWidget {
  const _SmallPageTile({
    required this.pageData,
  });

  final _PageData pageData;

  String get pageId => pageData.name;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected =
        ref.watch(currentPageIdProvider.select((e) => e == pageId));

    return Material(
      color: isSelected ? const Color(0xFF1e3f6f) : Colors.transparent,
      borderRadius: BorderRadius.circular(8),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Iconify(
          pageData.type.icon,
          size: 11,
          color: Colors.white,
        ),
      ),
    );
  }
}

@RoutePage(name: "EmptyPageEditorRoute")
class EmptyPageEditor extends HookConsumerWidget {
  const EmptyPageEditor({
    super.key,
  });

  Future<String?> _showAddPageDialog(BuildContext context) async => showDialog(
        context: context,
        builder: (context) => const AddPageDialogue(),
      );

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return EmptyScreen(
      title: "Select a page to edit or",
      buttonText: "Add Page",
      onButtonPressed: () => _showAddPageDialog(context),
    );
  }
}

// A button for adding a new page.
// Button has a outline.
class _AddPageButton extends HookConsumerWidget {
  const _AddPageButton();

  Future<String?> _showAddPageDialog(BuildContext context) async => showDialog(
        context: context,
        builder: (context) => const AddPageDialogue(),
      );

  @override
  Widget build(BuildContext context, WidgetRef ref) => Material(
        color: Colors.transparent,
        shape: const RoundedRectangleBorder(
          side: BorderSide(color: Color(0xff214780)),
          borderRadius: BorderRadius.all(Radius.circular(8)),
        ),
        child: InkWell(
          onTap: () => _showAddPageDialog(context),
          borderRadius: BorderRadius.circular(8),
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    "Add page",
                    style: Theme.of(context).textTheme.bodySmall?.copyWith(
                          color: Colors.white,
                        ),
                  ),
                ),
                const SizedBox(width: 8),
                const Icon(Icons.add, size: 16, color: Colors.white),
              ],
            ),
          ),
        ),
      );
}

class AddPageDialogue extends HookConsumerWidget {
  const AddPageDialogue({
    this.fixedType,
    this.autoNavigate = true,
    this.chapter = "",
    super.key,
  });

  final String chapter;
  final PageType? fixedType;
  final bool autoNavigate;

  Future<void> _addPage(WidgetRef ref, String name, PageType type) async {
    await ref.read(bookProvider.notifier).createPage(name, type, chapter);

    if (!autoNavigate) return;
    unawaited(ref.read(appRouter).push(PageEditorRoute(id: name)));
  }

  /// Validates the proposed name for a page.
  /// A name is invalid if it is empty or if it already exists.
  String? _validateName(
    String text,
    List<String> pagesNames,
  ) {
    if (text.isEmpty) {
      return "Name cannot be empty";
    }
    if (pagesNames.contains(text)) {
      return "Page already exists";
    }
    return null;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pagesNames = ref.watch(_pageNamesProvider);
    final controller = useTextEditingController();
    final isNameValid = useState(false);
    final type = useState(fixedType ?? PageType.sequence);

    return AlertDialog(
      title: Text(
        fixedType != null
            ? "Add a new ${fixedType!.tag} page"
            : "Add a new page",
      ),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          ValidatedTextField<String>(
            value: "",
            controller: controller,
            name: "Page Name",
            icon: TWIcons.book,
            validator: (value) {
              final validation = _validateName(value, pagesNames);
              isNameValid.value = validation == null;
              return validation;
            },
            inputFormatters: [
              snakeCaseFormatter(),
              FilteringTextInputFormatter.singleLineFormatter,
              FilteringTextInputFormatter.allow(RegExp("[a-z0-9_]")),
            ],
          ),
          if (fixedType == null) ...[
            const SizedBox(height: 12),
            Dropdown<PageType>(
              value: type.value,
              values: PageType.values,
              builder: (context, value) {
                return Row(
                  children: [
                    Iconify(value.icon, size: 18),
                    const SizedBox(width: 8),
                    Text(value.name.formatted),
                  ],
                );
              },
              icon: null,
              onChanged: (value) => type.value = value,
            ),
          ],
        ],
      ),
      actions: [
        TextButton.icon(
          icon: const Iconify(TWIcons.x),
          label: const Text("Cancel"),
          style: TextButton.styleFrom(
            foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
          ),
          onPressed: () => Navigator.of(context).pop(),
        ),
        FilledButton.icon(
          onPressed: !isNameValid.value
              ? null
              : () async {
                  final navigator = Navigator.of(context);
                  await _addPage(ref, controller.text, type.value);
                  navigator.pop(controller.text);
                },
          label: const Text("Add"),
          icon: const Iconify(TWIcons.plus),
        ),
      ],
    );
  }
}

class RenamePageDialogue extends HookConsumerWidget {
  const RenamePageDialogue({
    required this.old,
    super.key,
  });

  final String old;

  Future<void> _renamePage(WidgetRef ref, String newName) async {
    await ref.read(bookProvider.notifier).renamePage(old, newName);
    unawaited(ref.read(appRouter).push(PageEditorRoute(id: newName)));
  }

  /// Validates the proposed name for a page.
  /// A name is invalid if it is empty or if it already exists.
  String? _validateName(
    String text,
    List<String> pagesNames,
  ) {
    if (text.isEmpty) {
      return "Name cannot be empty";
    }

    if (text == old) {
      return "Name cannot be the same";
    }

    if (pagesNames.contains(text)) {
      return "Page already exists";
    }

    return null;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pagesNames = ref.watch(_pageNamesProvider);
    final controller = useTextEditingController(text: old);
    final isNameValid = useState(false);

    return AlertDialog(
      title: Text("Rename ${old.formatted}"),
      content: ValidatedTextField<String>(
        value: old,
        controller: controller,
        name: "Page Name",
        icon: TWIcons.book,
        validator: (value) {
          final validation = _validateName(value, pagesNames);
          isNameValid.value = validation == null;
          return validation;
        },
        inputFormatters: [
          snakeCaseFormatter(),
          FilteringTextInputFormatter.singleLineFormatter,
          FilteringTextInputFormatter.allow(RegExp("[a-z0-9_]")),
        ],
        onSubmitted: (value) async {
          final navigator = Navigator.of(context);
          await _renamePage(ref, value);
          navigator.pop(true);
        },
      ),
      actions: [
        TextButton.icon(
          icon: const Iconify(TWIcons.x),
          label: const Text("Cancel"),
          style: TextButton.styleFrom(
            foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
          ),
          onPressed: () => Navigator.of(context).pop(false),
        ),
        FilledButton.icon(
          onPressed: !isNameValid.value
              ? null
              : () async {
                  final navigator = Navigator.of(context);
                  await _renamePage(ref, controller.text);
                  navigator.pop(true);
                },
          label: const Text("Rename"),
          icon: const Iconify(TWIcons.pencil),
          color: Colors.orange,
        ),
      ],
    );
  }
}

class ChangeChapterDialogue extends HookConsumerWidget {
  const ChangeChapterDialogue({
    required this.pageId,
    required this.chapter,
    super.key,
  });

  final String pageId;
  final String chapter;

  Future<void> _changeChapter(
    BuildContext context,
    PassingRef ref,
    String newName,
    ValueNotifier<bool> changed,
  ) async {
    if (changed.value) return;
    changed.value = true;

    final navigator = Navigator.of(context);
    await ref.read(pageProvider(pageId))?.changeChapter(ref, newName);
    navigator.pop(true);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useTextEditingController();
    final focusNode = useFocusNode();
    final changed = useState(false);

    useDelayedExecution(focusNode.requestFocus);

    return AlertDialog(
      title: Text("Change chapter of ${pageId.formatted}"),
      content: FormattedTextField(
        controller: controller,
        focus: focusNode,
        text: chapter,
        hintText: "Chapter Name",
        icon: TWIcons.book,
        inputFormatters: [
          TextInputFormatter.withFunction(
            (oldValue, newValue) => newValue.copyWith(
              text: newValue.text
                  .toLowerCase()
                  .replaceAll(" ", ".")
                  .replaceAll("_", ".")
                  .replaceAll("-", "."),
            ),
          ),
          FilteringTextInputFormatter.singleLineFormatter,
          FilteringTextInputFormatter.allow(RegExp("[a-z0-9.]")),
        ],
        onSubmitted: (value) async =>
            _changeChapter(context, ref.passing, value, changed),
      ),
      actions: [
        TextButton.icon(
          icon: const Iconify(TWIcons.x),
          label: const Text("Cancel"),
          style: TextButton.styleFrom(
            foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
          ),
          onPressed: () => Navigator.of(context).pop(false),
        ),
        FilledButton.icon(
          onPressed: () async => _changeChapter(
            context,
            ref.passing,
            controller.text,
            changed,
          ),
          label: const Text("Change"),
          icon: const Iconify(TWIcons.pencil),
          color: Colors.orange,
        ),
      ],
    );
  }
}

class ChangePagePriorityDialogue extends HookConsumerWidget {
  const ChangePagePriorityDialogue({
    required this.pageId,
    super.key,
  });

  final String pageId;

  Future<void> _changePriority(
    BuildContext context,
    PassingRef ref,
    int newPriority,
    ValueNotifier<bool> changed,
  ) async {
    if (changed.value) return;
    changed.value = true;

    final navigator = Navigator.of(context);
    await ref.read(pageProvider(pageId))?.changePriority(ref, newPriority);
    navigator.pop(true);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final priority = ref.watch(pagePriorityProvider(pageId));
    final controller = useTextEditingController();
    final focusNode = useFocusNode();
    final changed = useState(false);

    useDelayedExecution(focusNode.requestFocus);

    return AlertDialog(
      title: Text("Change priority of ${pageId.formatted}"),
      content: FormattedTextField(
        controller: controller,
        focus: focusNode,
        text: priority.toString(),
        hintText: "Priority",
        icon: TWIcons.book,
        inputFormatters: [
          FilteringTextInputFormatter.digitsOnly,
        ],
        onSubmitted: (value) async =>
            _changePriority(context, ref.passing, int.parse(value), changed),
      ),
      actions: [
        TextButton.icon(
          icon: const Iconify(TWIcons.x),
          label: const Text("Cancel"),
          style: TextButton.styleFrom(
            foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
          ),
          onPressed: () => Navigator.of(context).pop(false),
        ),
        FilledButton.icon(
          onPressed: () async => _changePriority(
            context,
            ref.passing,
            int.parse(controller.text),
            changed,
          ),
          label: const Text("Change"),
          icon: const Iconify(TWIcons.pencil),
          color: Colors.orange,
        ),
      ],
    );
  }
}

Future<bool> showPageDeletionDialogue(
  BuildContext context,
  PassingRef ref,
  String pageId,
) {
  return showConfirmationDialogue(
    context: context,
    title: "Delete ${pageId.formatted}?",
    content:
        "This will delete the page and all its content.\nTHIS CANNOT BE UNDONE.",
    delayConfirm: 3.seconds,
    confirmText: "Delete",
    confirmIcon: TWIcons.trash,
    onConfirm: () async {
      await ref.read(bookProvider.notifier).deletePage(pageId);
      unawaited(
        ref.read(appRouter).replace(const EmptyPageEditorRoute()),
      );
    },
  );
}

class PageDrag {
  const PageDrag({
    required this.pageId,
  });

  final String pageId;
}
