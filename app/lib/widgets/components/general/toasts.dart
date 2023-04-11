import "dart:async";

import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/utils/passing_reference.dart";

part "toasts.freezed.dart";

@freezed
class Toast with _$Toast {
  const factory Toast({
    required String message,
    String? description,
    @Default(Colors.blue) Color color,
    @Default(FontAwesomeIcons.exclamation) IconData icon,
  }) = _Toast;

  const factory Toast.temporary({
    required String message,
    String? description,
    @Default(Colors.blue) Color color,
    @Default(FontAwesomeIcons.exclamation) IconData icon,
    @Default(Duration(seconds: 5)) Duration duration,
    DateTime? shownAt,
  }) = TemporaryToast;
}

class Toasts extends StateNotifier<List<Toast>> {
  Toasts() : super([]) {
    _timer = Timer.periodic(1.seconds, (timer) {
      _checkExpiry();
    });
  }

  late final Timer _timer;

  void show(Toast toast) {
    if (toast is TemporaryToast) {
      final now = DateTime.now();
      final shownAt = toast.shownAt ?? now;
      final duration = toast.duration;
      final difference = now.difference(shownAt);
      if (difference > duration) {
        state = [...state, toast];
      }
    } else {
      state = [...state, toast];
    }
  }

  static void showWarning(PassingRef ref, String message, {String? description}) {
    ref.read(toastsProvider.notifier).show(
          Toast.temporary(
            message: message,
            description: description,
            color: Colors.orange,
            icon: FontAwesomeIcons.circleExclamation,
          ),
        );
  }

  static void showError(PassingRef ref, String message, {String? description}) {
    ref.read(toastsProvider.notifier).show(
          Toast.temporary(
            message: message,
            description: description,
            color: Colors.red,
            icon: FontAwesomeIcons.triangleExclamation,
          ),
        );
  }

  void hide(Toast toast) {
    state = state.where((t) => t != toast).toList();
  }

  void _checkExpiry() {
    final now = DateTime.now();
    final expired = state.where((toast) {
      if (toast is TemporaryToast) {
        final shownAt = toast.shownAt ?? now;
        final duration = toast.duration;
        final difference = now.difference(shownAt);
        return difference > duration;
      } else {
        return false;
      }
    }).toList();
    state = state.where((toast) => !expired.contains(toast)).toList();
  }

  @override
  void dispose() {
    _timer.cancel();
    super.dispose();
  }
}

final toastsProvider = StateNotifierProvider<Toasts, List<Toast>>((ref) => Toasts());

@immutable
class ToastDisplay extends HookConsumerWidget {
  const ToastDisplay({
    this.child,
    super.key,
  });

  final Widget? child;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final toasts = ref.watch(toastsProvider);
    return Stack(
      children: [
        if (child != null) child!,
        ...toasts.mapIndexed((index, toast) {
          return Positioned(
            top: 16.0 + (index * 64.0),
            right: 16.0,
            child: toast is TemporaryToast ? _TemporaryToast(toast: toast) : Container(),
          );
        }),
      ],
    );
  }
}

class _TemporaryToast extends HookConsumerWidget {
  const _TemporaryToast({
    required this.toast,
  });

  final TemporaryToast toast;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Card(
      color: toast.color,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Row(
          children: [
            Icon(
              toast.icon,
              color: Colors.white,
            ),
            const SizedBox(width: 8.0),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  toast.message,
                  style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                if (toast.description != null)
                  Text(
                    toast.description!,
                    style: const TextStyle(
                      color: Colors.white,
                    ),
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
