import "dart:math";

import "package:collection/collection.dart";
import "package:collection_ext/all.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:google_fonts/google_fonts.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/hooks/global_key.dart";
import "package:typewriter/hooks/text_size.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/icons.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/models/segment.dart";
import "package:typewriter/pages/page_editor.dart";
import "package:typewriter/utils/color_converter.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/empty_screen.dart";
import "package:typewriter/widgets/components/app/entry_node.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";

part "cinematic_view.freezed.dart";
part "cinematic_view.g.dart";

@riverpod
List<String> _cinematicEntryIds(_CinematicEntryIdsRef ref) {
  final page = ref.watch(currentPageProvider);
  if (page == null) return [];

  return page.entries
      .where((entry) {
        final tags = ref.watch(entryTagsProvider(entry.type));
        return tags.contains("cinematic");
      })
      .map((entry) => entry.id)
      .toList();
}

class CinematicView extends HookConsumerWidget {
  const CinematicView({super.key});

  Widget _proxyDecorator(Widget child, int index, Animation<double> animation) {
    return AnimatedBuilder(
      animation: animation,
      builder: (context, child) {
        return Material(
          color: Colors.transparent,
          child: child,
        );
      },
      child: child,
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final entryIds = ref.watch(_cinematicEntryIdsProvider);

    if (entryIds.isEmpty) {
      return EmptyScreen(
        title: "There are no cinematic entries on this page.",
        buttonText: "Add Entry",
        onButtonPressed: () => ref.read(searchProvider.notifier).asBuilder()
          ..fetchNewEntry()
          ..tag("cinematic", canRemove: false)
          ..open(),
      );
    }

    return ReorderableListView.builder(
      itemCount: entryIds.length,
      buildDefaultDragHandles: false,
      proxyDecorator: _proxyDecorator,
      header: const _Heading(),
      itemBuilder: (context, index) {
        final entryId = entryIds[index];
        return _EntryRow(index: index, entryId: entryId, key: Key(entryId));
      },
      onReorder: (oldIndex, newIndex) {
        final page = ref.read(currentPageProvider);
        if (page == null) return;
        page.reorderEntry(ref.passing, entryIds[oldIndex], newIndex);
      },
    );
  }
}

@riverpod
String _longestEntryName(_LongestEntryNameRef ref) {
  final entryIds = ref.watch(_cinematicEntryIdsProvider);
  final names = entryIds.map((entryId) => ref.watch(entryNameProvider(entryId))).whereNotNull().toList();
  return names.isEmpty ? "" : names.reduce((a, b) => a.length > b.length ? a : b);
}

class _Heading extends HookConsumerWidget {
  const _Heading();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final longestName = ref.watch(_longestEntryNameProvider);
    final longestNameSize = useTextSize(context, longestName, GoogleFonts.jetBrainsMono(fontSize: 13));
    return SizedBox(
      height: 36,
      child: Row(
        children: [
          SizedBox(width: longestNameSize.width + 31),
          Text("Track", style: GoogleFonts.jetBrainsMono(fontWeight: FontWeight.bold, fontSize: 15)),
          const SizedBox(width: 8),
          VerticalDivider(color: Colors.grey.shade900, thickness: 2),
          const Expanded(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                _TrackSliderTrack(),
                SizedBox(height: 4),
                _TrackTimings(),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _EntryRow extends HookConsumerWidget {
  const _EntryRow({
    required this.index,
    required this.entryId,
    super.key,
  });
  final int index;
  final String entryId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return SizedBox(
      height: 64,
      child: Row(
        children: [
          const SizedBox(width: 4),
          MouseRegion(
            cursor: SystemMouseCursors.grab,
            child: ReorderableDragStartListener(
              index: index,
              child: const Icon(FontAwesomeIcons.barsStaggered, size: 12),
            ),
          ),
          const SizedBox(width: 8),
          EntryNode(entryId: entryId),
          _SmartSpacer(entryId: entryId),
          VerticalDivider(color: Colors.grey.shade900, thickness: 2),
          Expanded(child: _EntryTrack(entryId: entryId)),
        ],
      ),
    );
  }
}

class _SmartSpacer extends HookConsumerWidget {
  const _SmartSpacer({
    required this.entryId,
  });

  final String entryId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final longestName = ref.watch(_longestEntryNameProvider);
    final longestNameSize = useTextSize(context, longestName, GoogleFonts.jetBrainsMono(fontSize: 13));
    final entryName = ref.watch(entryNameProvider(entryId));
    final entryNameSize = useTextSize(context, entryName ?? "", GoogleFonts.jetBrainsMono(fontSize: 13));
    return SizedBox(
      width: longestNameSize.width - entryNameSize.width,
    );
  }
}

@riverpod
Map<String, Modifier> _segmentPaths(_SegmentPathsRef ref, String entryId) {
  final type = ref.watch(entryTypeProvider(entryId));
  if (type == null) return {};
  final blueprint = ref.watch(entryBlueprintProvider(type));
  if (blueprint == null) return {};

  return blueprint.fieldsWithModifier("segment");
}

const colorConverter = NullableColorConverter();

@riverpod
List<Segment> _segments(_SegmentsRef ref, String entryId, String path) {
  final paths = ref.watch(_segmentPathsProvider(entryId));
  final modifier = paths[path];
  if (modifier == null) return [];

  final entry = ref.watch(globalEntryProvider(entryId));
  if (entry == null) return [];

  final modifierData = modifier.data;

  final hexColor = modifierData["color"] as String? ?? "#000000";
  final color = colorConverter.fromJson(hexColor) ?? Colors.teal;

  final iconName = modifierData["icon"] as String? ?? "star";
  final icon = icons[iconName] ?? FontAwesomeIcons.star;

  return entry.getAll(path).mapIndexed((index, segment) {
    final startFrame = segment["startFrame"] as int? ?? 0;
    final endFrame = segment["endFrame"] as int? ?? 0;

    return Segment(
      path: path,
      index: index,
      color: color,
      icon: icon,
      startFrame: startFrame,
      endFrame: startFrame > endFrame ? startFrame : endFrame,
      data: segment,
    );
  }).toList();
}

@riverpod
List<Segment> _allSegments(_AllSegmentsRef ref, String entryId) {
  final paths = ref.watch(_segmentPathsProvider(entryId));
  return paths.keys.map((path) => ref.watch(_segmentsProvider(entryId, path))).whereNotNull().expand((x) => x).toList();
}

@riverpod
int _endingFrame(_EndingFrameRef ref, String entryId) {
  final segments = ref.watch(_allSegmentsProvider(entryId));
  return segments.map((segment) => segment.endFrame).maxOrNull ?? 0;
}

@riverpod
int _totalSequenceFrames(_TotalSequenceFramesRef ref) {
  final entryIds = ref.watch(_cinematicEntryIdsProvider);
  final frames = entryIds.map((entryId) => ref.watch(_endingFrameProvider(entryId))).whereNotNull().toList();
  return frames.map((frame) => frame).maxOrNull ?? 0;
}

@freezed
class _TrackState with _$_TrackState {
  const factory _TrackState({
    @Default(0) double start,
    @Default(1) double end,
    @Default(0) int totalFrames,
    @Default(0) double width,
  }) = _$__TrackState;
}

extension on _TrackState {
  int get startFrame => (start * totalFrames).round().clamp(0, totalFrames);
  int get endFrame => (end * totalFrames).round().clamp(0, totalFrames);
  int get duration => endFrame - startFrame;
}

class _TrackStateProvider extends StateNotifier<_TrackState> {
  _TrackStateProvider(this.ref) : super(const _TrackState()) {
    state = state.copyWith(totalFrames: max(100, ref.read(_totalSequenceFramesProvider)));
  }
  final AutoDisposeStateNotifierProviderRef<_TrackStateProvider, _TrackState> ref;

  static const _minWidth = 40;
  double calculateDelta(double delta) => delta / state.width;

  void updateStart(double newStart) {
    if (newStart < 0) return;
    if (newStart > state.end) return;
    final size = (state.end - newStart) * state.width;
    if (size < _minWidth) return;
    state = state.copyWith(start: newStart);
  }

  void updateEnd(double newEnd) {
    if (newEnd > 1) return;
    if (newEnd < state.start) return;
    final size = (newEnd - state.start) * state.width;
    if (size < _minWidth) return;
    state = state.copyWith(end: newEnd);
  }

  void drag(DragUpdateDetails details) {
    if (details.delta.dx == 0) return;

    final widthDelta = calculateDelta(details.delta.dx);
    final newStart = state.start + widthDelta;
    final newEnd = state.end + widthDelta;
    if (newStart < 0) return;
    if (newEnd > 1) return;
    state = state.copyWith(start: newStart, end: newEnd);
  }

  void widthChanged(double width) {
    if (width == state.width) return;
    print("width changed: $width");
    state = state.copyWith(width: width, start: 0, end: 1);
  }
}

final _trackStateProvider =
    StateNotifierProvider.autoDispose<_TrackStateProvider, _TrackState>(_TrackStateProvider.new);

@riverpod
double _frameSpacing(_FrameSpacingRef ref) {
  final trackState = ref.watch(_trackStateProvider);
  final totalFrames = trackState.totalFrames;
  final width = trackState.width - 16;

  if (totalFrames == 0) return 0;
  if (width <= 0) return 0;

  final lookingAt = max(1, trackState.endFrame - trackState.startFrame);
  final spacing = width / lookingAt;
  return spacing;
}

@riverpod
double _trackSize(_TrackSizeRef ref) {
  final totalFrames = ref.watch(_trackStateProvider.select((state) => state.totalFrames));
  final spacing = ref.watch(_frameSpacingProvider);

  if (totalFrames == 0) return 0;

  return totalFrames * spacing + 16;
}

@riverpod
double _trackOffset(_TrackOffsetRef ref) {
  final trackState = ref.watch(_trackStateProvider);
  final totalFrames = trackState.totalFrames;

  if (totalFrames == 0) return 0;

  final spacing = ref.watch(_frameSpacingProvider);
  final offset = trackState.startFrame * spacing;
  return offset;
}

class _TrackSliderTrack extends HookConsumerWidget {
  const _TrackSliderTrack();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final outerKey = useGlobalKey();
    MediaQuery.of(context); // force rebuild on size change
    useDelayedExecution(
      () {
        ref.read(_trackStateProvider.notifier).widthChanged(context.size!.width);
      },
      runEveryBuild: true,
    );

    return SizedBox(
      height: 16,
      child: Stack(
        alignment: Alignment.center,
        children: [
          Positioned(
            key: outerKey,
            height: 16,
            left: 0,
            right: 0,
            child: Container(
              height: 16,
              decoration: BoxDecoration(
                color: Theme.of(context).inputDecorationTheme.fillColor,
                borderRadius: BorderRadius.circular(8),
              ),
            ),
          ),
          _TrackSlider(outerKey: outerKey),
        ],
      ),
    );
  }
}

@riverpod
double _sliderStartOffset(_SliderStartOffsetRef ref) {
  final start = ref.watch(_trackStateProvider.select((state) => state.start));
  final width = ref.watch(_trackStateProvider.select((state) => state.width));
  return start * width;
}

@riverpod
double _sliderEndOffset(_SliderEndOffsetRef ref) {
  final end = ref.watch(_trackStateProvider.select((state) => state.end));
  final width = ref.watch(_trackStateProvider.select((state) => state.width));
  return (1 - end) * width;
}

class _TrackSlider extends HookConsumerWidget {
  const _TrackSlider({
    required this.outerKey,
  });

  final GlobalKey outerKey;

  bool _isWithinSlider(
    GlobalKey key,
    DragUpdateDetails details, {
    double innerMargin = 0,
  }) {
    final renderBox = key.currentContext?.findRenderObject() as RenderBox?;
    if (renderBox == null) return false;
    final local = renderBox.globalToLocal(details.globalPosition);
    return local.dx >= innerMargin && local.dx <= renderBox.size.width - innerMargin;
  }

  double _percentage(
    GlobalKey key,
    DragUpdateDetails details, {
    double innerMargin = 0,
  }) {
    final renderBox = key.currentContext?.findRenderObject() as RenderBox?;
    if (renderBox == null) return 0;
    final local = renderBox.globalToLocal(details.globalPosition);
    final width = renderBox.size.width;
    final percentage = (local.dx - innerMargin) / width;
    return percentage.clamp(0, 1);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final start = ref.watch(_sliderStartOffsetProvider);
    final end = ref.watch(_sliderEndOffsetProvider);

    final grabbing = useState(false);

    return Positioned(
      height: 16,
      left: start,
      right: end,
      child: Container(
        height: 16,
        decoration: BoxDecoration(
          color: Colors.blue,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          children: [
            const SizedBox(width: 3),
            _buildThumb(
              onDragUpdate: (details) {
                final percentage = _percentage(outerKey, details, innerMargin: 10);
                ref.read(_trackStateProvider.notifier).updateStart(percentage);
              },
            ),
            Expanded(
              child: GestureDetector(
                onHorizontalDragStart: (details) => grabbing.value = true,
                onHorizontalDragEnd: (details) => grabbing.value = false,
                onHorizontalDragUpdate: (details) {
                  if (!_isWithinSlider(outerKey, details, innerMargin: 20)) return;
                  ref.read(_trackStateProvider.notifier).drag(details);
                },
                child: MouseRegion(
                  cursor: grabbing.value ? SystemMouseCursors.grabbing : SystemMouseCursors.grab,
                  child: Container(),
                ),
              ),
            ),
            _buildThumb(
              onDragUpdate: (details) {
                final percentage = _percentage(outerKey, details, innerMargin: -10);
                ref.read(_trackStateProvider.notifier).updateEnd(percentage);
              },
            ),
            const SizedBox(width: 3),
          ],
        ),
      ),
    );
  }

  Widget _buildThumb({required Function(DragUpdateDetails) onDragUpdate}) {
    return MouseRegion(
      cursor: SystemMouseCursors.resizeColumn,
      child: GestureDetector(
        onHorizontalDragUpdate: (details) {
          onDragUpdate(details);
        },
        child: Container(
          height: 10,
          width: 10,
          decoration: const BoxDecoration(
            color: Colors.white,
            shape: BoxShape.circle,
          ),
        ),
      ),
    );
  }
}

const _possibleFractions = [1, 2, 5, 10, 20, 50, 100, 200, 500, 1000];
const _minimalFractionSize = 30.0;

@riverpod
int _timeFractions(_TimeFractionsRef ref) {
  final duration = ref.watch(_trackStateProvider.select((state) => state.duration));
  final width = ref.watch(_trackStateProvider.select((state) => state.width));
  return _possibleFractions.firstWhereOrNull((fraction) => (_minimalFractionSize * duration) / fraction < width) ??
      1000;
}

@riverpod
List<int> _timeFractionFrames(_TimeFractionFramesRef ref, {double fractionModifier = 1.0}) {
  final startFrame = ref.watch(_trackStateProvider.select((state) => state.startFrame));
  final endFrame = ref.watch(_trackStateProvider.select((state) => state.endFrame));
  final fractions = (ref.watch(_timeFractionsProvider) * fractionModifier).ceil();

  final firstFractionFrame = (startFrame / fractions).ceil() * fractions;
  return firstFractionFrame.upTo(endFrame, step: fractions).toList();
}

@riverpod
double _timePointOffset(_TimePointOffsetRef ref, int frame, double widgetWidth) {
  final startFrame = ref.watch(_trackStateProvider.select((state) => state.startFrame));
  final frameSpacing = ref.watch(_frameSpacingProvider);

  if (frame < startFrame) return 0.0;

  return (frame - startFrame) * frameSpacing - widgetWidth / 2;
}

class _TrackTimings extends HookConsumerWidget {
  const _TrackTimings();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final fractionFrames = ref.watch(_timeFractionFramesProvider());
    final oneCharWidth = useTextSize(context, "0", GoogleFonts.jetBrainsMono(fontSize: 10)).width;

    return SizedBox(
      height: 16,
      child: Stack(
        clipBehavior: Clip.none,
        children: [
          for (final frame in fractionFrames)
            Positioned(
              left: ref.watch(_timePointOffsetProvider(frame, oneCharWidth * frame.toString().length)),
              child: Text(frame.toString(),
                  style: GoogleFonts.jetBrainsMono(fontSize: 10, color: Theme.of(context).hintColor)),
            ),
        ],
      ),
    );
  }
}

class _EntryTrack extends HookConsumerWidget {
  const _EntryTrack({
    required this.entryId,
  });

  final String entryId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final paths = ref.watch(_segmentPathsProvider(entryId));
    if (paths.isEmpty) return const SizedBox.shrink();

    final offset = ref.watch(_trackOffsetProvider);
    final controller = useScrollController(initialScrollOffset: offset);

    useEffect(
      () {
        if (!controller.hasClients) return;
        controller.jumpTo(offset);
        return null;
      },
      [offset],
    );

    return Stack(
      children: [
        const _TrackBackground(),
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          controller: controller,
          child: _child(paths),
        ),
      ],
    );
  }

  Widget _child(Map<String, Modifier> paths) {
    if (paths.length == 1) {
      return _SegmentsTrack(
        entryId: entryId,
        path: paths.keys.first,
        segmentBuilder: (context, segment) {
          return _SegmentWidget(
            key: Key(segment.truePath),
            entryId: entryId,
            segment: segment,
          );
        },
      );
    } else {
      return Text(" ${paths.keys.join(", ")}");
    }
  }
}

@riverpod
double _trackBackgroundFractionModifier(_TrackBackgroundFractionModifierRef ref) {
  final fractions = ref.watch(_timeFractionsProvider);
  if (fractions == 1) return 1.0;
  if (fractions == 5) return 1.0;
  return 1 / 2;
}

class _TrackBackground extends HookConsumerWidget {
  const _TrackBackground();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final fractions = ref.watch(_timeFractionsProvider);
    final fractionModifier = ref.watch(_trackBackgroundFractionModifierProvider);
    final fractionFrames = ref.watch(_timeFractionFramesProvider(fractionModifier: fractionModifier));
    return Stack(
      children: [
        for (final frame in fractionFrames)
          Positioned(
            left: ref.watch(_timePointOffsetProvider(frame, 1)),
            top: 0,
            bottom: 0,
            child: Container(
              width: 1,
              color:
                  frame % fractions == 0 ? Theme.of(context).hintColor : Theme.of(context).hintColor.withOpacity(0.2),
            ),
          ),
      ],
    );
  }
}

@riverpod
double _frameStartOffset(_FrameStartOffsetRef ref, int frame) {
  final frameSpacing = ref.watch(_frameSpacingProvider);
  return frame * frameSpacing;
}

class _SegmentsTrack extends HookConsumerWidget {
  const _SegmentsTrack({
    required this.entryId,
    required this.path,
    required this.segmentBuilder,
  });

  final String entryId;
  final String path;
  final Widget Function(BuildContext, Segment) segmentBuilder;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final segments = ref.watch(_segmentsProvider(entryId, path));
    return Stack(
      alignment: Alignment.center,
      children: [
        SizedBox(width: ref.watch(_trackSizeProvider)),
      ],
    );
  }
}

class _SegmentWidget extends HookConsumerWidget {
  const _SegmentWidget({
    required this.entryId,
    required this.segment,
    super.key,
  });

  final String entryId;
  final Segment segment;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final duration = max(1, segment.endFrame - segment.startFrame) + 1;
    final frameSpacing = ref.watch(_frameSpacingProvider);
    return Container(
      height: 20,
      width: duration * frameSpacing,
      decoration: BoxDecoration(
        color: segment.color,
        borderRadius: BorderRadius.circular(12),
      ),
    );
  }
}
