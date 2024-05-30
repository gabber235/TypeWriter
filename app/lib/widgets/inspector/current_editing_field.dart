import "package:flutter/cupertino.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/utils/debouncer.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

final currentEditingFieldProvider =
    StateNotifierProvider<CurrentEditingFieldNotifier, String>(
  CurrentEditingFieldNotifier.new,
  name: "currentEditingFieldProvider",
);

class CurrentEditingFieldNotifier extends StateNotifier<String> {
  CurrentEditingFieldNotifier(this.ref) : super("") {
    // Always reset the state when another entry is selected
    ref.listen(
      inspectingEntryIdProvider,
      (_, __) => state = "",
    );

    _debouncer = Debouncer(duration: 100.ms, callback: _clearIfSame);
  }
  final Ref<dynamic> ref;

  late Debouncer<String> _debouncer;

  String get path => state;
  set path(String path) {
    state = path;
  }

  void clearIfSame(String value) {
    // We need to debounce the clear to avoid clearing the field when the user has selected a new field
    _debouncer.run(value);
  }

  void _clearIfSame(String value) {
    if (state == value) {
      state = "";
    }
  }

  @override
  void dispose() {
    _debouncer.cancel();
    super.dispose();
  }
}

void useFocusedChange(
  FocusNode focus,
  Function({required bool hasFocus}) onChange, [
  List<Object?>? keys,
]) {
  useEffect(
    () {
      void onFocusChange() {
        onChange(hasFocus: focus.hasFocus);
      }

      focus.addListener(onFocusChange);
      return () => focus.removeListener(onFocusChange);
    },
    keys ?? [],
  );
}

void useFocusedBasedCurrentEditingField(
  FocusNode focus,
  PassingRef ref,
  String path,
) {
  useFocusedChange(
    focus,
    ({required hasFocus}) {
      if (hasFocus) {
        ref.read(currentEditingFieldProvider.notifier).path = path;
      } else {
        ref.read(currentEditingFieldProvider.notifier).clearIfSame(path);
      }
    },
    [path],
  );
}
