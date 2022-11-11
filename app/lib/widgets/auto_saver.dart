import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:rive/rive.dart';
import 'package:typewriter/models/auto_saver.dart';

class AutoSaver extends HookConsumerWidget {
  const AutoSaver({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final saver = ref.watch(autoSaverProvider);

    return saver.when(
      loading: () {
        return Row(
          children: const [
            SizedBox(
              width: 12,
              height: 12,
              child: CircularProgressIndicator(strokeWidth: 2),
            ),
            SizedBox(width: 8),
            Text('Saving...'),
          ],
        );
      },
      error: (error, st) {
        return Text("Unable to save: $error",
            style: const TextStyle(color: Colors.red));
      },
      data: (info) => _Success(info: info),
    );
  }
}

class _Success extends HookConsumerWidget {
  final SavedInfo info;

  const _Success({
    super.key,
    required this.info,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useAnimationController(
      duration: const Duration(milliseconds: 3000),
    );

    final animation = Tween(begin: 1.0, end: 0.0).animate(CurvedAnimation(
      parent: controller,
      curve: const Interval(0.75, 1, curve: Curves.easeIn),
    ));

    useEffect(() {
      if (DateTime.now().difference(info.time).inSeconds > 1) {
        controller.value = 1;
      } else {
        controller.forward();
      }
      return null;
    }, [info]);

    useAnimation(controller);

    return Opacity(
      opacity: animation.value,
      child: Row(
        children: const [
          SizedBox(
            width: 35,
            height: 35,
            child: RiveAnimation.asset(
              "assets/check.riv",
              animations: ["Success"],
            ),
          ),
          Text('Saved'),
        ],
      ),
    );
  }
}
