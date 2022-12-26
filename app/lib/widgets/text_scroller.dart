import "dart:async";

import "package:collection/collection.dart";
import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

/// Switches between the given texts in a loop.
class TextScroller extends StatefulHookConsumerWidget {
  const TextScroller({
    required this.texts,
    this.style,
    this.duration = const Duration(seconds: 2),
    this.transitionDuration = const Duration(milliseconds: 300),
    this.curve = Curves.easeOut,
    super.key,
  }) : super();

  final List<String> texts;
  final TextStyle? style;
  final Duration duration;
  final Duration transitionDuration;
  final Curve curve;

  @override
  TextScrollerState createState() => TextScrollerState();
}

class TextScrollerState extends ConsumerState<TextScroller> {
  final _controller = PageController(initialPage: 1);
  Timer? _timer;
  late Size _size;

  @override
  void initState() {
    _timer = Timer.periodic(widget.duration, (_) {
      if (_controller.page == widget.texts.length) {
        _controller.jumpToPage(0);
      }
      _controller.nextPage(duration: widget.transitionDuration, curve: widget.curve);
    });
    super.initState();
  }

  @override
  void didChangeDependencies() {
    final text = maxBy<String, int>(widget.texts, (s) => s.length);
    _size = (TextPainter(
      text: TextSpan(
        text: text,
        style: widget.style ?? DefaultTextStyle.of(context).style,
      ),
      maxLines: 1,
      textScaleFactor: MediaQuery.of(context).textScaleFactor,
      textDirection: TextDirection.ltr,
    )..layout())
        .size;
    super.didChangeDependencies();
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: _size.height,
      width: _size.width,
      child: PageView(
        controller: _controller,
        scrollDirection: Axis.vertical,
        physics: const NeverScrollableScrollPhysics(),
        children: [
          for (final text in [widget.texts.last, ...widget.texts])
            Text(
              text,
              textAlign: TextAlign.center,
              style: widget.style,
            ),
        ],
      ),
    );
  }
}
