import "dart:async";

import "package:flutter/widgets.dart";

/// Initializes a controlled input at most once per mounted instance.
///
/// A nonnull [selected] consumes initialization, even if absent from [choices].
/// For a null selection, an available [defaultValue] wins; otherwise the sole
/// distinct choice is used. Disabled inputs, missing callbacks, and ambiguous
/// choices wait for a later update without consuming initialization.
///
/// The candidate is delivered through [onSelected] in a microtask, using the
/// caller's normal edit and commit policy. Delivery consumes initialization even
/// if the caller rejects the value. Updates cancel and reconsider pending work;
/// disposal cancels it. Later updates never reinitialize a consumed instance.
class SelectionInitialization<T extends Object> extends StatefulWidget {
  const SelectionInitialization({
    required this.selected,
    required this.choices,
    required this.onSelected,
    required this.child,
    this.defaultValue,
    this.enabled = true,
    super.key,
  });

  final T? selected;
  final T? defaultValue;
  final Iterable<T> choices;
  final ValueChanged<T?>? onSelected;
  final bool enabled;
  final Widget child;

  @override
  State<SelectionInitialization<T>> createState() =>
      _SelectionInitializationState<T>();
}

class _SelectionInitializationState<T extends Object>
    extends State<SelectionInitialization<T>> {
  bool _initialized = false;
  int _generation = 0;

  @override
  void initState() {
    super.initState();
    _initialize();
  }

  @override
  void didUpdateWidget(SelectionInitialization<T> oldWidget) {
    super.didUpdateWidget(oldWidget);
    _initialize();
  }

  void _initialize() {
    final generation = ++_generation;
    if (widget.selected != null) _initialized = true;
    if (_initialized || !widget.enabled || widget.onSelected == null) return;
    final choices = widget.choices.toSet();

    final preferred = widget.defaultValue;
    final candidate = choices.contains(preferred)
        ? preferred
        : choices.length == 1
        ? choices.single
        : null;

    if (candidate == null) return;
    scheduleMicrotask(() {
      if (generation != _generation) return;
      _initialized = true;
      widget.onSelected!(candidate);
    });
  }

  @override
  void dispose() {
    _generation++;
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => widget.child;
}
