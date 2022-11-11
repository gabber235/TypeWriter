import 'package:auto_route/auto_route.dart';
import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:rive/rive.dart';
import 'package:typewriter/app_router.dart';
import 'package:typewriter/hooks/route_state.dart';
import 'package:typewriter/main.dart';
import 'package:typewriter/models/book.dart';
import 'package:typewriter/models/page.dart';
import 'package:typewriter/models/page.dart' as model;
import 'package:typewriter/utils/extensions.dart';
import 'package:typewriter/widgets/filled_button.dart';

class PagesList extends StatelessWidget {
  const PagesList({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox.expand(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: const [
          _PagesSelector(),
          Expanded(child: AutoRouter()),
        ],
      ),
    );
  }
}

final _pageNamesProvider = Provider<List<String>>((ref) {
  return ref.watch(pagesProvider).map((e) => e.name).sorted().toList();
});

class _PagesSelector extends HookConsumerWidget {
  const _PagesSelector({
    Key? key,
  }) : super(key: key);

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
  final String name;

  const _PageTile({
    Key? key,
    required this.name,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = useRouteState(
      () => context.innerRouterOf(PagesListRoute.name),
      (controller) => controller.current.pathParams.getString("id", "") == name,
      defaultValue: false,
    );

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 2.0),
      child: Material(
        color: isSelected ? const Color(0xFF1e3f6f) : Colors.transparent,
        borderRadius: BorderRadius.circular(8),
        child: InkWell(
          onTap: () {
            if (!isSelected) {
              context.router.push(PageEditorRoute(pageId: name));
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
  }
}

class EmptyPageEditor extends HookConsumerWidget {
  const EmptyPageEditor({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      children: const [
        Spacer(),
        Expanded(
          flex: 2,
          child: RiveAnimation.asset(
            "assets/cute_robot.riv",
            stateMachines: ["Motion"],
          ),
        ),
        Text("Select a page to edit",
            style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold)),
        Spacer(),
      ],
    );
  }
}

// A button for adding a new page.
// Button has a outline.
class _AddPageButton extends HookConsumerWidget {
  const _AddPageButton({
    Key? key,
  }) : super(key: key);

  // Show a dialog where a name can be entered.
  Future<String?> _showAddPageDialog(BuildContext context) async {
    return showDialog<String>(
      context: context,
      builder: (context) {
        return const _AddPageDialogue();
      },
    );
  }

  // Add a new page with the given name.
  void _addPage(WidgetRef ref, String name) {
    ref.read(bookProvider.notifier).insertPage(model.Page(name: name));
  }

  // Show a dialog for adding a new page. If a name is entered, add a new page.
  Future<void> _addPageDialog(BuildContext context, WidgetRef ref) async {
    final name = await _showAddPageDialog(context);
    if (name != null && name.isNotEmpty) {
      _addPage(ref, name);
      ref.read(appRouter).push(PageEditorRoute(pageId: name));
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Material(
      color: Colors.transparent,
      shape: const RoundedRectangleBorder(
        side: BorderSide(color: Color(0xff214780)),
        borderRadius: BorderRadius.all(Radius.circular(8)),
      ),
      child: InkWell(
        onTap: () => _addPageDialog(context, ref),
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
}

class _AddPageDialogue extends HookConsumerWidget {
  const _AddPageDialogue({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final pagesNames = ref.watch(_pageNamesProvider);
    final controller = useTextEditingController();
    final hasTyped = useState(false);
    final error = useState("");

    final disabled = error.value.isNotEmpty || controller.text.isEmpty;

    useEffect(() {
      controller.addListener(() {
        var errorText = "";

        if (controller.text.isNotEmpty) {
          hasTyped.value = true;
        }

        if (controller.text.isEmpty && hasTyped.value) {
          errorText = "Name cannot be empty";
        } else if (pagesNames.contains(controller.text)) {
          errorText = "Page already exists";
        }

        error.value = errorText;
      });
      return null;
    }, [controller]);

    return AlertDialog(
      title: const Text("Add a new page"),
      content: TextField(
        controller: controller,
        autofocus: true,
        inputFormatters: [
          snakeCaseFormatter(),
          FilteringTextInputFormatter.singleLineFormatter,
          FilteringTextInputFormatter.allow(RegExp(r"[a-z0-9_]")),
        ],
        decoration: InputDecoration(
          hintText: "Page name",
          errorText: error.value.isEmpty ? null : error.value,
        ),
        onSubmitted: (value) {
          if (!disabled) {
            Navigator.of(context).pop(controller.text);
          }
        },
      ),
      actions: [
        TextButton(
          onPressed: () {
            Navigator.of(context).pop();
          },
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
