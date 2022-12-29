import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:flutter_svg/flutter_svg.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/extensions.dart";

class GlobalWriters extends HookConsumerWidget {
  const GlobalWriters({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final writers = ref.watch(writersProvider);

    return Writers(writers: writers);
  }
}

class WritersIndicator extends HookConsumerWidget {
  const WritersIndicator({
    required this.filter,
    this.child,
    this.builder,
    this.shift,
    this.offset,
    super.key,
  })  : assert(child != null || builder != null),
        assert(!(shift != null && offset != null));

  final bool Function(Writer) filter;
  final Widget? child;
  final Widget Function(int)? builder;

  final Offset Function(int)? shift;
  final Offset? offset;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final writers = ref.watch(writersProvider.select((list) => list.where(filter).toList()));

    final offset = this.offset ?? shift?.call(writers.length) ?? Offset.zero;

    return Stack(
      clipBehavior: Clip.none,
      children: [
        child ?? builder?.call(writers.length) ?? const SizedBox(),
        if (writers.isNotEmpty)
          Positioned(
            right: -15 + offset.dx,
            top: -25 + offset.dy,
            child: Writers(writers: writers),
          ),
      ],
    );
  }
}

class Writers extends HookConsumerWidget {
  const Writers({required this.writers, super.key});

  final List<Writer> writers;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final hovering = useState(false);

    return SizedBox(
      height: 40,
      width: writers.length * 37,
      child: MouseRegion(
        onEnter: (_) => hovering.value = true,
        onExit: (_) => hovering.value = false,
        child: Stack(
          alignment: Alignment.centerRight,
          clipBehavior: Clip.none,
          children: [
            for (var i = 0; i < writers.length; i++) ...[
              AnimatedPositioned(
                duration: const Duration(milliseconds: 1000),
                curve: Curves.elasticOut,
                right: i * (hovering.value ? 37 : 15.0),
                child: AnimatedAppear(
                  duration: const Duration(milliseconds: 1000),
                  curve: Curves.elasticOut,
                  child: WriterIcon(id: writers[i].id),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

class AnimatedAppear extends HookWidget {
  const AnimatedAppear({
    required this.child,
    this.duration = const Duration(milliseconds: 200),
    this.curve,
    super.key,
  });

  final Widget child;
  final Duration duration;
  final Curve? curve;

  @override
  Widget build(BuildContext context) {
    final animation = useAnimationController(duration: duration);
    final tween =
        Tween(begin: 0.0, end: 1.0).animate(CurvedAnimation(parent: animation, curve: curve ?? Curves.easeOut));

    animation.forward();

    return ScaleTransition(scale: tween, child: child);
  }
}

class WriterIcon extends HookWidget {
  const WriterIcon({required this.id, super.key});

  final String id;

  @override
  Widget build(BuildContext context) {
    final color = useMemoized(() => id.randomColor, [id]);

    return Container(
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        border: Border.all(color: color, width: 2),
        color: Theme.of(context).scaffoldBackgroundColor,
      ),
      padding: const EdgeInsets.all(2),
      child: ClipOval(
        child: SvgPicture.network(
          "https://avatars.dicebear.com/api/adventurer-neutral/$id.svg?b=%23${color.value.toRadixString(16)}",
          // color: color,
          height: 25,
        ),
      ),
    );
  }
}
