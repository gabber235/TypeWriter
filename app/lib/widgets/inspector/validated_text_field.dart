import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/inspector.dart";
import "package:typewriter/widgets/inspector/current_editing_field.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/formatted_text_field.dart";

/// The current state of the duration editor
enum _State {
  initial,
  valid,
  invalid,
}

class ValidatedTextField<T> extends HookConsumerWidget {
  const ValidatedTextField({
    required this.path,
    required this.defaultValue,
    required this.deserialize,
    required this.serialize,
    required this.formatted,
    this.icon = Icons.text_fields,
    this.inputFormatters = const [],
    this.keepValidVisibleWhileFocused = false,
    super.key,
  });
  final String path;
  final T defaultValue;
  final String Function(T) deserialize;
  final T Function(String) serialize;
  final String Function(T) formatted;
  final IconData icon;
  final List<TextInputFormatter> inputFormatters;
  final bool keepValidVisibleWhileFocused;

  T? _parse(String value) {
    try {
      return serialize(value);
    } on FormatException catch (_) {
      return null;
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final focus = useFocusNode();
    useFocusedBasedCurrentEditingField(focus, ref, path);
    final state = useState(_State.initial);

    final value = ref.watch(fieldValueProvider(path, defaultValue));
    final formattedValue = deserialize(value);

    useFocusedChange(focus, (hasFocus) {
      if (!hasFocus) state.value = _State.initial;

      if (hasFocus && keepValidVisibleWhileFocused && state.value == _State.initial) {
        final parsed = _parse(formattedValue);
        state.value = parsed == null ? _State.invalid : _State.valid;
      }
    });

    final name = ref.watch(pathDisplayNameProvider(path));

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        DecoratedTextField(
          focus: focus,
          text: formattedValue,
          inputFormatters: inputFormatters,
          decoration: InputDecoration(
            prefixIcon: Icon(
              icon,
              size: 18,
              color: state.value == _State.invalid ? Colors.redAccent : null,
            ),
            hintText: "Enter a $name: ${deserialize(defaultValue)}",
            errorText: state.value == _State.invalid ? "Invalid $name" : null,
          ),
          onChanged: (value) {
            final parsed = _parse(value);
            state.value = parsed == null ? _State.invalid : _State.valid;
            if (parsed != null) ref.read(entryDefinitionProvider)?.updateField(ref.passing, path, parsed);
          },
        ),
        _StateText(
          name: name,
          state: state.value,
          value: formatted(value),
          keepValidVisible: keepValidVisibleWhileFocused,
        ),
      ],
    );
  }
}

class _StateText extends HookWidget {
  const _StateText({
    required this.name,
    required this.state,
    required this.value,
    this.keepValidVisible = false,
    super.key,
  });

  final String name;
  final _State state;
  final String value;
  final bool keepValidVisible;

  @override
  Widget build(BuildContext context) {
    final animationFinished = useState(false);

    useEffect(
      () {
        if (state != _State.valid) animationFinished.value = false;
        return null;
      },
      [state],
    );

    if (state != _State.valid) return Container();

    return AnimatedPadding(
      padding: EdgeInsets.only(left: 8.0, top: animationFinished.value && !keepValidVisible ? 0.0 : 4.0),
      duration: const Duration(milliseconds: 300),
      curve: Curves.easeInOut,
      child: Text(
        "Valid $name: $value",
        style: Theme.of(context).textTheme.caption?.copyWith(color: Colors.green),
      ).animate(
        effects: [
          MoveEffect(begin: const Offset(0, -4), duration: 300.ms, curve: Curves.easeIn),
          FadeEffect(begin: 0.0, end: 1.0, duration: 300.ms, curve: Curves.easeIn),
          ThenEffect(delay: 300.ms),
          ShimmerEffect(duration: 1.5.seconds, curve: Curves.easeInOut),
          if (!keepValidVisible) ...[
            ThenEffect(delay: 300.ms),
            MoveEffect(end: const Offset(0, -4), duration: 300.ms, curve: Curves.easeOut),
            FadeEffect(begin: 1.0, end: 0.0, duration: 300.ms, curve: Curves.easeOut),
          ],
        ],
      ).callback(callback: (_) => animationFinished.value = true),
    );
  }
}
