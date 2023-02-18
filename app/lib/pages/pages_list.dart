import "dart:async";

import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart" hide FilledButton;
import "package:flutter/services.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/popups.dart";
import "package:typewriter/widgets/components/app/empty_screen.dart";
import "package:typewriter/widgets/components/app/select_entries.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/components/general/context_menu_region.dart";
import "package:typewriter/widgets/components/general/filled_button.dart";
import "package:typewriter/widgets/components/general/validated_text_field.dart";

part "pages_list.g.dart";

@riverpod
List<String> _pageNames(_PageNamesRef ref) {
  return ref.watch(bookProvider).pages.map((e) => e.name).toList();
}

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
    final pageNames = ref.watch(_pageNamesProvider);
    return Container(
      color: const Color(0xFF163260),
      width: 170,
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        // When selecting entries we don't want to be able to switch pages or add new ones
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 12),
            Text("Pages", style: Theme.of(context).textTheme.titleMedium?.copyWith(color: Colors.white)),
            const SizedBox(height: 12),
            for (var i = 0; i < pageNames.length; i++) _PageTile(index: i, pageId: pageNames[i]),
            const SizedBox(height: 12),
            const SelectingEntriesBlocker(
              child: _AddPageButton(),
            ),
          ],
        ),
      ),
    );
  }
}

class _PageTile extends HookConsumerWidget {
  const _PageTile({
    required this.index,
    required this.pageId,
    super.key,
  });
  final int index;
  final String pageId;

  bool _needsShift(int amount) {
    // If the amount of writer is 0, we never need to shift
    if (amount == 0) return false;
    // If we are the first page, we have some space above us, thus we don't need to shift if there are not a lot of writers
    if (index == 0) return amount > 2;
    return true;
  }

  List<ContextMenuTile> _contextMenuItems(BuildContext context, WidgetRef ref, bool isSelected) {
    return [
      ContextMenuTile.button(
        title: "Rename",
        icon: FontAwesomeIcons.pen,
        onTap: () => showDialog(context: context, builder: (_) => _RenamePageDialogue(old: pageId)),
      ),
      ContextMenuTile.divider(),
      ContextMenuTile.button(
        title: "Delete",
        icon: FontAwesomeIcons.trash,
        color: Colors.redAccent,
        onTap: () => showConfirmationDialogue(
          context: context,
          title: "Delete ${pageId.formatted}?",
          content: "This will delete the page and all its content.\nTHIS CANNOT BE UNDONE.",
          delayConfirm: 3.seconds,
          confirmText: "Delete",
          confirmIcon: FontAwesomeIcons.trash,
          onConfirm: () async {
            await ref.read(bookProvider.notifier).deletePage(pageId);
            if (!isSelected) return;
            unawaited(ref.read(appRouter).replace(const EmptyPageEditorRoute()));
          },
        ),
      ),
    ];
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = ref.watch(currentPageIdProvider.select((e) => e == pageId));

    return WritersIndicator(
      filter: (writer) => writer.pageId.hasValue && writer.pageId == pageId && !isSelected,
      shift: (amount) => _needsShift(amount) ? const Offset(4, 30) : Offset.zero,
      builder: (amount) {
        return AnimatedPadding(
          duration: 200.ms,
          curve: Curves.easeInOut,
          padding: EdgeInsets.only(left: 2.0, right: 2.0, top: _needsShift(amount) ? 30 : 0),
          child: Material(
            color: isSelected ? const Color(0xFF1e3f6f) : Colors.transparent,
            borderRadius: BorderRadius.circular(8),
            child: ContextMenuRegion(
              builder: (context) => _contextMenuItems(context, ref, isSelected),
              child: InkWell(
                onTap: () {
                  if (isSelected) return;
                  ref.read(appRouter).push(PageEditorRoute(id: pageId));
                },
                borderRadius: BorderRadius.circular(8),
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Row(
                    children: [
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          pageId.formatted,
                          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                color: Colors.white,
                              ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      const Icon(Icons.chevron_right, size: 16, color: Colors.white),
                    ],
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}

class EmptyPageEditor extends HookConsumerWidget {
  const EmptyPageEditor({
    super.key,
  });

  Future<String?> _showAddPageDialog(BuildContext context) async => showDialog(
        context: context,
        builder: (context) => const _AddPageDialogue(),
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
        builder: (context) => const _AddPageDialogue(),
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

class _AddPageDialogue extends HookConsumerWidget {
  const _AddPageDialogue();

  Future<void> _addPage(WidgetRef ref, String name) async {
    await ref.read(bookProvider.notifier).createPage(name);
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

    return AlertDialog(
      title: const Text("Add a new page"),
      content: ValidatedTextField<String>(
        value: "",
        controller: controller,
        name: "Page name",
        icon: FontAwesomeIcons.book,
        validator: (value) => _validateName(value, pagesNames),
        inputFormatters: [
          snakeCaseFormatter(),
          FilteringTextInputFormatter.singleLineFormatter,
          FilteringTextInputFormatter.allow(RegExp("[a-z0-9_]")),
        ],
        onChanged: (value) {
          isNameValid.value = _validateName(value, pagesNames) == null;
        },
        onSubmitted: (value) async {
          final navigator = Navigator.of(context);
          await _addPage(ref, value);
          navigator.pop();
        },
      ),
      actions: [
        TextButton.icon(
          icon: const Icon(FontAwesomeIcons.xmark),
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
                  await _addPage(ref, controller.text);
                  navigator.pop();
                },
          label: const Text("Add"),
          icon: const Icon(FontAwesomeIcons.plus),
        ),
      ],
    );
  }
}

class _RenamePageDialogue extends HookConsumerWidget {
  const _RenamePageDialogue({
    required this.old,
    super.key,
  });

  final String old;

  Future<void> _renamePage(WidgetRef ref, String newName) async {
    await ref.read(communicatorProvider).renamePage(old, newName);
    ref.read(bookProvider.notifier).renamePage(old, newName);
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

    return AlertDialog(
      title: Text("Rename ${old.formatted}"),
      content: ValidatedTextField<String>(
        value: old,
        controller: controller,
        name: "Page name",
        icon: FontAwesomeIcons.book,
        inputFormatters: [
          snakeCaseFormatter(),
          FilteringTextInputFormatter.singleLineFormatter,
          FilteringTextInputFormatter.allow(RegExp("[a-z0-9_]")),
        ],
        validator: (value) => _validateName(value, pagesNames),
        onSubmitted: (value) async {
          final navigator = Navigator.of(context);
          await _renamePage(ref, value);
          navigator.pop();
        },
      ),
      actions: [
        TextButton.icon(
          icon: const Icon(FontAwesomeIcons.xmark),
          label: const Text("Cancel"),
          style: TextButton.styleFrom(
            foregroundColor: Theme.of(context).textTheme.bodySmall?.color,
          ),
          onPressed: () => Navigator.of(context).pop(),
        ),
        FilledButton.icon(
          onPressed: _validateName(controller.text, pagesNames) != null
              ? null
              : () async {
                  final navigator = Navigator.of(context);
                  await _renamePage(ref, controller.text);
                  navigator.pop();
                },
          label: const Text("Rename"),
          icon: const Icon(FontAwesomeIcons.pen),
          color: Colors.orange,
        ),
      ],
    );
  }
}
