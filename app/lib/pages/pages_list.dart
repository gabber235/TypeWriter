import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/book.dart";
import 'package:typewriter/models/communicator.dart';
import "package:typewriter/models/page.dart" as model;
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/empty_screen.dart";
import "package:typewriter/widgets/filled_button.dart";
import 'package:typewriter/widgets/writers.dart';

part "pages_list.g.dart";

class PagesList extends StatelessWidget {
  const PagesList({super.key});

  @override
  Widget build(BuildContext context) => SizedBox.expand(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: const [
            _PagesSelector(),
            Expanded(child: AutoRouter()),
          ],
        ),
      );
}

@riverpod
List<String> _pageNames(_PageNamesRef ref) {
  return ref.watch(bookProvider).pages.map((e) => e.name).toList();
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
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 12),
            Text("Pages", style: Theme.of(context).textTheme.subtitle1),
            const SizedBox(height: 12),
            for (final name in pageNames) _PageTile(name: name),
            const SizedBox(height: 12),
            const _AddPageButton(),
          ],
        ),
      ),
    );
  }
}

class _PageTile extends HookConsumerWidget {
  const _PageTile({
    required this.name,
  });
  final String name;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = ref.watch(currentPageIdProvider.select((e) => e == name));

    return WritersIndicator(
      filter: (writer) => writer.pageId.hasValue && writer.pageId == name && !isSelected,
      shift: (amount) => amount > 0 ? const Offset(4, 30) : Offset.zero,
      builder: (amount) {
        return AnimatedPadding(
          duration: const Duration(milliseconds: 200),
          curve: Curves.easeInOut,
          padding: EdgeInsets.only(left: 2.0, right: 2.0, top: amount > 0 ? 30 : 0),
          child: Material(
            color: isSelected ? const Color(0xFF1e3f6f) : Colors.transparent,
            borderRadius: BorderRadius.circular(8),
            child: InkWell(
              onTap: () {
                if (!isSelected) {
                  context.router.push(PageEditorRoute(id: name));
                }
              },
              borderRadius: BorderRadius.circular(8),
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Row(
                  children: [
                    const SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        name.formatted,
                        style: Theme.of(context).textTheme.caption?.copyWith(
                              color: Colors.white,
                            ),
                      ),
                    ),
                    const SizedBox(width: 8),
                    const Icon(Icons.chevron_right, size: 16),
                  ],
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
                    style: Theme.of(context).textTheme.caption?.copyWith(
                          color: Colors.white,
                        ),
                  ),
                ),
                const SizedBox(width: 8),
                const Icon(Icons.add, size: 16),
              ],
            ),
          ),
        ),
      );
}

class _AddPageDialogue extends HookConsumerWidget {
  const _AddPageDialogue();

  Future<void> _addPage(WidgetRef ref, String name) async {
    await ref.read(communicatorProvider).createPage(name);
    ref.read(bookProvider.notifier).insertPage(model.Page(name: name));
    await ref.read(appRouter).push(PageEditorRoute(id: name));
  }

  /// Validates the proposed name for a page.
  /// A name is invalid if it is empty or if it already exists.
  void _validateName(
    TextEditingController controller,
    ValueNotifier<bool> hasTyped,
    List<String> pagesNames,
    ValueNotifier<String> error,
  ) {
    var errorText = "";

    if (controller.text.isNotEmpty) {
      hasTyped.value = true;
    }

    // We don't want to scare the user with an error message if they haven't typed anything yet.
    if (controller.text.isEmpty && hasTyped.value) {
      errorText = "Name cannot be empty";
    } else if (pagesNames.contains(controller.text)) {
      errorText = "Page already exists";
    }

    error.value = errorText;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pagesNames = ref.watch(_pageNamesProvider);
    final controller = useTextEditingController();
    final hasTyped = useState(false);
    final error = useState("");

    final disabled = error.value.isNotEmpty || controller.text.isEmpty;

    useEffect(
      () {
        void validator() => _validateName(controller, hasTyped, pagesNames, error);
        controller.addListener(validator);
        return () => controller.removeListener(validator);
      },
      [controller, pagesNames],
    );

    return AlertDialog(
      title: const Text("Add a new page"),
      content: TextField(
        controller: controller,
        autofocus: true,
        inputFormatters: [
          snakeCaseFormatter(),
          FilteringTextInputFormatter.singleLineFormatter,
          FilteringTextInputFormatter.allow(RegExp("[a-z0-9_]")),
        ],
        decoration: InputDecoration(
          hintText: "Page name",
          errorText: error.value.isEmpty ? null : error.value,
        ),
        onSubmitted: (value) {
          if (!disabled) {
            _addPage(ref, value);
            Navigator.of(context).pop(controller.text);
          }
        },
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text("Cancel"),
        ),
        FilledButton.icon(
          onPressed: disabled
              ? null
              : () {
                  Navigator.of(context).pop(controller.text);
                },
          label: const Text("Add"),
          icon: const Icon(FontAwesomeIcons.plus),
        ),
      ],
    );
  }
}
