import "package:flutter/material.dart" hide FilledButton;
import "package:flutter_animate/flutter_animate.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/general/filled_button.dart";

part "select_entries.freezed.dart";
part "select_entries.g.dart";

final entrySelectionProvider = StateNotifierProvider.autoDispose<
    EntriesSelectionNotifier, EntriesSelection?>((ref) {
  return EntriesSelectionNotifier(ref);
});

@riverpod
bool isSelectingEntries(IsSelectingEntriesRef ref) {
  return ref.watch(entrySelectionProvider) != null;
}

@riverpod
bool hasEntryInSelection(HasEntryInSelectionRef ref, String id) {
  final selection = ref.watch(entrySelectionProvider);
  return selection != null && selection.selectedEntries.contains(id);
}

@riverpod
String selectingTag(SelectingTagRef ref) {
  final selection = ref.watch(entrySelectionProvider);
  return selection?.tag ?? "";
}

@riverpod
bool canSelectEntry(CanSelectEntryRef ref, String id) {
  final selection = ref.watch(entrySelectionProvider);
  if (selection == null) {
    return false;
  }
  if (selection.excludedEntries.contains(id)) {
    return false;
  }
  final entryType =
      ref.watch(globalEntryProvider(id).select((entry) => entry?.type));
  if (entryType == null) {
    return false;
  }
  final blueprint = ref.watch(entryBlueprintProvider(entryType));
  if (blueprint == null) {
    return false;
  }
  return blueprint.tags.contains(selection.tag);
}

@freezed
class EntriesSelection with _$EntriesSelection {
  const factory EntriesSelection({
    required String tag,
    required List<String> selectedEntries,
    @Default([]) List<String> excludedEntries,
    Function(Ref<dynamic>, List<String>)? onSelectionChanged,
  }) = _EntriesSelection;
}

class EntriesSelectionNotifier extends StateNotifier<EntriesSelection?> {
  EntriesSelectionNotifier(this.ref) : super(null);

  final Ref<dynamic> ref;

  void startSelection(
    String tag, {
    List<String> selectedEntries = const [],
    List<String> excludedEntries = const [],
    Function(Ref<dynamic>, List<String>)? onSelectionChanged,
  }) {
    if (state != null) {
      throw StateError("Already selecting entries");
    }

    state = EntriesSelection(
      tag: tag,
      selectedEntries: selectedEntries,
      excludedEntries: excludedEntries,
      onSelectionChanged: onSelectionChanged,
    );
  }

  void finishSelection() {
    if (state == null) {
      throw StateError("Not selecting entries");
    }
    state?.onSelectionChanged?.call(ref, state!.selectedEntries);
    state = null;
  }

  void stopSelection() {
    if (state == null) {
      throw StateError("Not selecting entries");
    }
    state = null;
  }

  void _modifySelection(EntriesSelection Function(EntriesSelection) modifier) {
    final currentSelection = state;
    if (currentSelection == null) {
      return;
    }
    state = modifier(currentSelection);
  }

  void selectEntry(String entryId) {
    _modifySelection(
      (selection) => selection.copyWith(
        selectedEntries: [...selection.selectedEntries, entryId],
      ),
    );
  }

  void unselectEntry(String entryId) {
    _modifySelection(
      (selection) => selection.copyWith(
        selectedEntries:
            selection.selectedEntries.where((id) => id != entryId).toList(),
      ),
    );
  }

  void toggleEntrySelection(String entryId) {
    if (state?.selectedEntries.contains(entryId) ?? false) {
      unselectEntry(entryId);
    } else {
      selectEntry(entryId);
    }
  }

  void clearSelection() {
    state = null;
  }

  void setTag(String tag) {
    _modifySelection((selection) => selection.copyWith(tag: tag));
  }
}

/// When selecting entries, this widget will block parts of the UI to prevent
/// the user from interacting with it.
/// See [isSelectingEntries] to know if the user is currently selecting entries.
class SelectingEntriesBlocker extends HookConsumerWidget {
  const SelectingEntriesBlocker({required this.child, super.key});

  final Widget child;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelecting = ref.watch(isSelectingEntriesProvider);
    return AnimatedOpacity(
      opacity: isSelecting ? 0.6 : 1,
      duration: 200.ms,
      curve: Curves.easeOut,
      child: MouseRegion(
        cursor: isSelecting ? SystemMouseCursors.forbidden : MouseCursor.defer,
        child: AbsorbPointer(
          absorbing: isSelecting,
          child: child,
        ),
      ),
    );
  }
}

class EntriesSelectorInspector extends HookConsumerWidget {
  const EntriesSelectorInspector({super.key});

  void _cancel(WidgetRef ref) {
    ref.read(entrySelectionProvider.notifier).stopSelection();
  }

  void _finish(WidgetRef ref) {
    ref.read(entrySelectionProvider.notifier).finishSelection();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tag = ref.watch(selectingTagProvider).formatted;
    return Actions(
      actions: {
        ActivateIntent: CallbackAction(onInvoke: (intent) => _finish(ref)),
        ButtonActivateIntent:
            CallbackAction(onInvoke: (intent) => _finish(ref)),
        DismissIntent: CallbackAction(onInvoke: (intent) => _cancel(ref)),
      },
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16),
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 300),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 16),
              Text(
                "Selected $tag",
                style: Theme.of(context).textTheme.titleLarge,
              ),
              const SizedBox(height: 12),
              Text(
                "Click on any $tag in the editor to (un)select it.\n\nHere you can see the list of selected ${tag}s:",
                style: Theme.of(context).textTheme.bodySmall,
              ),
              const SizedBox(height: 12),
              const Expanded(child: _EntriesSelectorList()),
              const SizedBox(height: 12),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  FilledButton.icon(
                    icon: const Icon(FontAwesomeIcons.x),
                    label: const Text("Cancel"),
                    color: Colors.redAccent,
                    onPressed: () => _cancel(ref),
                  ),
                  FilledButton.icon(
                    icon: const Icon(FontAwesomeIcons.check),
                    label: const Text("Finish"),
                    color: Colors.green,
                    onPressed: () => _finish(ref),
                  ),
                ],
              ),
              const SizedBox(height: 16),
            ],
          ),
        ),
      ),
    );
  }
}

class _EntriesSelectorList extends HookConsumerWidget {
  const _EntriesSelectorList();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedEntries = ref.watch(entrySelectionProvider)?.selectedEntries;
    final page = ref.watch(currentPageProvider);
    if (selectedEntries == null || page == null) {
      return const SizedBox.shrink();
    }

    return ListView.builder(
      shrinkWrap: true,
      itemCount: selectedEntries.length,
      itemBuilder: (context, index) {
        final entryId = selectedEntries[index];

        return Padding(
          padding: const EdgeInsets.symmetric(vertical: 5),
          child: MouseRegion(
            cursor: SystemMouseCursors.disappearing,
            child: FakeEntryNode(entryId: entryId),
          ),
        );
      },
    );
  }
}
