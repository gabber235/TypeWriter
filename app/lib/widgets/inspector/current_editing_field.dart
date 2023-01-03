import "dart:async";

import "package:flutter/cupertino.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/widgets/inspector.dart";

final currentEditingFieldProvider =
    StateNotifierProvider<CurrentEditingFieldNotifier, String>(CurrentEditingFieldNotifier.new);

class CurrentEditingFieldNotifier extends StateNotifier<String> {
  CurrentEditingFieldNotifier(this.ref) : super("") {
    // Always reset the state when another entry is selected
    ref.listen(
      selectedEntryIdProvider,
      (_, __) => state = "",
    );
  }
  final Ref<dynamic> ref;

  Timer? _debounceTimer;

  set path(String path) {
    state = path;
  }

  void clearIfSame(String value) {
    // We need to debounce the clear to avoid clearing the field when the user has selected a new field
    _debounceTimer = Timer(const Duration(milliseconds: 100), () {
      if (state == value) {
        state = "";
      }
    });
  }

  @override
  void dispose() {
    _debounceTimer?.cancel();
    super.dispose();
  }
}

void useFocusedBasedCurrentEditingField(FocusNode focus, WidgetRef ref, String path) {
  useEffect(
    () {
      void onFocusChange() {
        if (focus.hasFocus) {
          ref.read(currentEditingFieldProvider.notifier).path = path;
        } else {
          ref.read(currentEditingFieldProvider.notifier).clearIfSame(path);
        }
      }

      focus.addListener(onFocusChange);
      return () => focus.removeListener(onFocusChange);
    },
    [focus, path],
  );
}
