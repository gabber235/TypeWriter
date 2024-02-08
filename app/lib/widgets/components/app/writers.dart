import "package:flutter/material.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:flutter_svg/flutter_svg.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/extensions.dart";

class GlobalWriters extends HookConsumerWidget {
  const GlobalWriters({
    this.direction = Axis.horizontal,
    super.key,
  });

  final Axis direction;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final writers = ref.watch(writersProvider);

    return Writers(writers: writers);
  }
}

class WritersIndicator extends HookConsumerWidget {
  const WritersIndicator({
    required this.provider,
    this.child,
    this.builder,
    this.shift,
    this.offset,
    this.enabled = true,
    super.key,
  })  : assert(
          child != null || builder != null,
          "Either child or builder must be provided",
        ),
        assert(
          !(shift != null && offset != null),
          "Only one of shift or offset can be provided",
        );

  final ProviderBase<List<Writer>> provider;
  final Widget? child;
  final Widget Function(int)? builder;

  final Offset Function(int)? shift;
  final Offset? offset;
  final bool enabled;

  bool _needsUpdate(List<Writer> previous, List<Writer> current) {
    if (previous.length != current.length) return true;
    for (var i = 0; i < previous.length; i++) {
      if (previous[i] != current[i]) return true;
    }
    return false;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final writers = useState(<Writer>[]);
    final offset =
        this.offset ?? shift?.call(writers.value.length) ?? Offset.zero;

    useEffect(
      () {
        if (!enabled && writers.value.isNotEmpty) {
          writers.value = [];
        }
        return null;
      },
      [enabled],
    );

    if (enabled) {
      ref.listen(provider, (old, next) {
        if (_needsUpdate(writers.value, next)) {
          writers.value = next;
        }
      });
    }

    return Stack(
      clipBehavior: Clip.none,
      children: [
        child ?? builder?.call(writers.value.length) ?? const SizedBox(),
        if (writers.value.isNotEmpty)
          Positioned(
            right: -15 + offset.dx,
            top: -25 + offset.dy,
            child: Writers(writers: writers.value),
          ),
      ],
    );
  }
}

class Writers extends HookConsumerWidget {
  const Writers({
    required this.writers,
    this.direction = Axis.horizontal,
    super.key,
  });

  final Axis direction;
  final List<Writer> writers;

  double _placement(int index, {required bool hovering}) {
    return index * (hovering ? 37 : 15.0);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final hovering = useState(false);

    final mainSize = writers.length * 37.0;
    const crossSize = 40.0;

    return SizedBox(
      height: direction == Axis.horizontal ? crossSize : mainSize,
      width: direction == Axis.horizontal ? mainSize : crossSize,
      child: MouseRegion(
        onEnter: (_) => hovering.value = true,
        onExit: (_) => hovering.value = false,
        child: Stack(
          alignment: Alignment.centerRight,
          clipBehavior: Clip.none,
          children: [
            for (var i = 0; i < writers.length; i++) ...[
              AnimatedPositioned(
                duration: 1.seconds,
                curve: Curves.elasticOut,
                right: direction == Axis.horizontal
                    ? _placement(i, hovering: hovering.value)
                    : null,
                bottom: direction == Axis.vertical
                    ? _placement(i, hovering: hovering.value)
                    : null,
                child: WriterIcon(writer: writers[i]).animate().scale(
                      begin: const Offset(0.8, 0.8),
                      end: const Offset(1, 1),
                      duration: 1.seconds,
                      curve: Curves.elasticOut,
                    ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

class WriterIcon extends HookWidget {
  const WriterIcon({required this.writer, super.key});

  final Writer writer;

  Widget _icon(Color color) {
    if (writer.iconUrl == null) {
      return SvgPicture.network(
        "https://avatars.dicebear.com/api/adventurer-neutral/${writer.id}.svg?b=%23${color.value.toRadixString(16)}",
        height: 25,
      );
    }

    return Image.network(
      writer.iconUrl!,
      height: 25,
    );
  }

  @override
  Widget build(BuildContext context) {
    final color = useMemoized(() => writer.id.randomColor, [writer.id]);

    return Container(
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        border: Border.all(color: color, width: 2),
        color: Theme.of(context).scaffoldBackgroundColor,
      ),
      padding: const EdgeInsets.all(2),
      child: ClipOval(
        child: _icon(color),
      ),
    );
  }
}
