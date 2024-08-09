import "dart:async";
import "dart:math";

import "package:auto_size_text/auto_size_text.dart";
import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:tinycolor2/tinycolor2.dart";
import "package:typewriter/hooks/text_size.dart";
import "package:typewriter/main.dart";
import "package:typewriter/utils/fonts.dart";
import "package:typewriter/utils/icons.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/general/iconify.dart";

part "toasts.freezed.dart";

@freezed
class Toast with _$Toast {
  const factory Toast({
    required String id,
    required String message,
    String? description,
    @Default(Colors.blue) Color color,
    @Default(TWIcons.exclamation) String icon,
    DateTime? shownAt,
  }) = _Toast;

  const factory Toast.temporary({
    required String id,
    required String message,
    String? description,
    @Default(Colors.blue) Color color,
    @Default(TWIcons.exclamation) String icon,
    @Default(Duration(seconds: 10)) Duration duration,
    DateTime? shownAt,
  }) = TemporaryToast;
}

extension on Toast {
  double get shownPercentage {
    final now = DateTime.now();
    final shownAt = this.shownAt ?? now;
    final difference = now.difference(shownAt);

    return difference.inMilliseconds / 400;
  }

  Color get darkenColor =>
      TinyColor.fromColor(color).shade(50).desaturate(30).color;
}

extension on TemporaryToast {
  double get percentage {
    final now = DateTime.now();
    final shownAt = this.shownAt ?? now;
    final difference = now.difference(shownAt);

    return difference.inMilliseconds / duration.inMilliseconds;
  }
}

class Toasts extends StateNotifier<List<Toast>> {
  Toasts() : super([]) {
    _timer = Timer.periodic(1.seconds, (timer) {
      _checkExpiry();
    });
  }

  late final Timer _timer;

  void show(Toast toast) {
    state = [...state, toast];
    _refreshToast();
  }

  void hide(Toast toast) {
    state = state.where((t) => t != toast).toList();
    _refreshToast();
  }

  void _refreshToast() {
    final current = state;
    if (current.isEmpty) return;
    final first = current.first;
    if (first.shownAt != null) return;
    state = [
      first.copyWith(shownAt: DateTime.now()),
      ...current.skip(1),
    ];
  }

  void _checkExpiry() {
    final now = DateTime.now();
    final current = state;
    if (current.isEmpty) return;
    final first = current.first;
    if (first.shownAt == null) return;
    final expired = first.maybeMap(
      (value) => false,
      orElse: () => false,
      temporary: (toast) {
        final shownAt = toast.shownAt ?? now;
        final duration = toast.duration;
        final difference = now.difference(shownAt);

        return difference > duration;
      },
    );
    if (!expired) return;
    hide(first);
  }

  @override
  void dispose() {
    _timer.cancel();
    super.dispose();
  }

  static void showSuccess(
    PassingRef ref,
    String message, {
    String? description,
    Duration duration = const Duration(seconds: 10),
  }) {
    ref.read(toastsProvider.notifier).show(
          Toast.temporary(
            id: uuid.v4(),
            message: message,
            description: description,
            color: Colors.green,
            icon: TWIcons.checkCircle,
            duration: duration,
          ),
        );
  }

  static void showWarning(
    PassingRef ref,
    String message, {
    String? description,
    Duration duration = const Duration(seconds: 10),
  }) {
    ref.read(toastsProvider.notifier).show(
          Toast.temporary(
            id: uuid.v4(),
            message: message,
            description: description,
            color: Colors.orange,
            icon: TWIcons.exlamationCircle,
            duration: duration,
          ),
        );
  }

  static void showError(
    PassingRef ref,
    String message, {
    String? description,
    Duration duration = const Duration(seconds: 10),
  }) {
    ref.read(toastsProvider.notifier).show(
          Toast.temporary(
            id: uuid.v4(),
            message: message,
            description: description,
            color: Colors.red,
            icon: TWIcons.warning,
            duration: duration,
          ),
        );
  }
}

final toastsProvider = StateNotifierProvider<Toasts, List<Toast>>(
  (ref) => Toasts(),
  name: "toastsProvider",
);

@immutable
class ToastDisplay extends HookConsumerWidget {
  const ToastDisplay({
    this.child,
    super.key,
  });

  final Widget? child;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final toast =
        ref.watch(toastsProvider.select((value) => value.firstOrNull));
    return Stack(
      children: [
        if (child != null) child!,
        Positioned(
          top: 0,
          right: 4,
          child: toast?.maybeMap(
                (_) => Container(),
                orElse: Container.new,
                temporary: (toast) => _ToastShowAnimation(
                  toast: toast,
                  child: _TemporaryToast(toast: toast),
                ),
              ) ??
              Container(),
        ),
      ],
    );
  }
}

class _ToastShowAnimation extends HookConsumerWidget {
  const _ToastShowAnimation({
    required this.child,
    required this.toast,
  });

  final Widget child;
  final Toast toast;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useAnimationController(duration: 400.ms);

    useEffect(() {
      controller.forward(from: toast.shownPercentage);
      return null;
    });

    return child
        .animate(
          key: ValueKey(toast.id),
          controller: controller,
          autoPlay: false,
        )
        .scaleXY(begin: 0.7, duration: 400.ms, curve: Curves.elasticOut)
        .moveX(begin: -100, duration: 400.ms, curve: Curves.elasticOut)
        .fadeIn(duration: 200.ms, curve: Curves.easeIn);
  }
}

class _TemporaryToast extends HookConsumerWidget {
  const _TemporaryToast({
    required this.toast,
  });

  final TemporaryToast toast;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useAnimationController(duration: toast.duration);

    useEffect(() {
      controller.forward(from: toast.percentage);
      return null;
    });

    final messageSize = useTextSize(
      context,
      toast.message,
      const TextStyle(
        color: Colors.white,
        fontSize: 16.0,
        fontVariations: [extraBoldWeight],
      ),
    );

    final width = max(300.0, messageSize.width + 12 + 28 + 12 + 16);

    return Card(
      color: toast.color,
      elevation: 4.0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(8.0),
      ),
      clipBehavior: Clip.antiAlias,
      child: SizedBox(
        width: width,
        child: InkWell(
          onTap: () => ref.read(toastsProvider.notifier).hide(toast),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _TemporaryToastProgress(toast: toast, width: width),
              Padding(
                padding: const EdgeInsets.only(
                  top: 8,
                  bottom: 8,
                  left: 12,
                  right: 16,
                ),
                child: Row(
                  children: [
                    Iconify(
                      toast.icon,
                      color: toast.darkenColor,
                      size: 28.0,
                    ),
                    const SizedBox(width: 12.0),
                    Flexible(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            toast.message,
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 16.0,
                              fontVariations: [extraBoldWeight],
                            ),
                          ),
                          if (toast.description != null) ...[
                            const SizedBox(height: 4.0),
                            AutoSizeText(
                              // minFontSize: 10,
                              toast.description!,
                              style: TextStyle(
                                color: toast.darkenColor,
                              ),
                            ),
                          ],
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    )
        .animate(
          key: ValueKey(toast.id),
          autoPlay: false,
          controller: controller,
        )
        .fadeOut(
          delay: toast.duration - 200.ms,
          duration: 200.ms,
          curve: Curves.easeOut,
        );
  }
}

class _TemporaryToastProgress extends HookConsumerWidget {
  const _TemporaryToastProgress({
    required this.toast,
    required this.width,
  });

  final TemporaryToast toast;
  final double width;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller =
        useAnimationController(duration: toast.duration - 200.ms);

    useEffect(() {
      controller.forward(from: toast.percentage);
      return null;
    });

    useAnimation(controller);

    return Container(
      width: width * (1 - controller.value),
      height: 4.0,
      decoration: BoxDecoration(
        color: toast.darkenColor,
        borderRadius: BorderRadius.circular(2.0),
      ),
    );
  }
}
