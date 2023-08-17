import "package:audioplayers/audioplayers.dart";
import "package:flutter/foundation.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:fuzzy/fuzzy.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/sounds.dart";
import "package:typewriter/utils/audio_player.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";
import "package:typewriter/widgets/components/general/focused_notifier.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/inspector.dart";

part "sound.g.dart";

@riverpod
Fuzzy<MinecraftSound> _fuzzySounds(_FuzzySoundsRef ref) {
  final provider = ref.watch(minecraftSoundsProvider);
  final sounds = provider.map(
    data: (data) => data.value.entries.toList(),
    error: (_) => <MinecraftSound>[],
    loading: (_) => <MinecraftSound>[],
  );

  return Fuzzy(
    sounds,
    options: FuzzyOptions(
      threshold: 0.2,
      keys: [
        WeightedKey(
          name: "name",
          getter: (entry) => entry.key.replaceAll(".", " ").replaceAll("_", " "),
          weight: 0.7,
        ),
        WeightedKey(
          name: "rawName",
          getter: (entry) => entry.key,
          weight: 0.7,
        ),
      ],
    ),
  );
}

class SoundsFetcher extends SearchFetcher {
  const SoundsFetcher({
    this.onSelect,
    this.disabled = false,
  });

  final FutureOr<bool?> Function(MinecraftSound)? onSelect;

  @override
  final bool disabled;

  @override
  String get title => "Sounds";

  @override
  List<SearchElement> fetch(PassingRef ref) {
    final search = ref.read(searchProvider);
    if (search == null) return [];
    final fuzzy = ref.read(_fuzzySoundsProvider);

    final results = fuzzy.search(search.query);

    return results
        .map(
          (result) => SoundSearchElement(sound: result.item, onSelect: onSelect),
        )
        .toList();
  }

  @override
  SoundsFetcher copyWith({
    bool? disabled,
  }) {
    return SoundsFetcher(
      onSelect: onSelect,
      disabled: disabled ?? this.disabled,
    );
  }
}

class SoundSearchElement extends SearchElement {
  const SoundSearchElement({
    required this.sound,
    required this.onSelect,
  });

  final MinecraftSound sound;
  final FutureOr<bool?> Function(MinecraftSound)? onSelect;

  @override
  String get title => sound.name.formatted;

  @override
  Color color(BuildContext context) {
    switch (sound.category) {
      case "block":
        return Colors.blue;
      case "entity":
        return Colors.red;
      case "item":
        return Colors.green;
      case "music":
        return Colors.orange;
      case "music_disc":
        return Colors.orange;
      case "ui":
        return Colors.yellow;
      case "weather":
        return Colors.teal;
      case "ambient":
        return Colors.cyan;
      case "enchant":
        return Colors.purple;
      case "particle":
        return Colors.pink;
      case "event":
        return Colors.indigoAccent;
      default:
        return Colors.grey;
    }
  }

  @override
  Widget icon(BuildContext context) => _FocusedAudioPlayer(sound: sound);

  @override
  Widget suffixIcon(BuildContext context) => const Icon(FontAwesomeIcons.upRightFromSquare);

  @override
  String description(BuildContext context) {
    if (sound.value.length > 1) {
      return "${sound.category.formatted} (${sound.value.length} Sound ${sound.value.length.pluralize("track")})";
    } else {
      return sound.category.formatted;
    }
  }

  @override
  List<SearchAction> actions(PassingRef ref) {
    return [
      const SearchAction(
        "Select",
        FontAwesomeIcons.check,
        SingleActivator(LogicalKeyboardKey.enter),
      ),
    ];
  }

  @override
  Future<bool> activate(BuildContext context, PassingRef ref) async {
    return await onSelect?.call(sound) ?? true;
  }
}

class _FocusedAudioPlayer extends HookConsumerWidget {
  const _FocusedAudioPlayer({
    required this.sound,
    super.key,
  });

  final MinecraftSound sound;

  void _playSound(PassingRef ref) {
    if (!kIsWeb) return;
    final audioPlayer = ref.read(audioPlayerProvider);
    final url = sound.value.pickRandomSoundUrl();
    final source = UrlSource(url);
    audioPlayer.play(source);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useAnimationController(duration: 1.seconds);
    final hovering = useState(false);
    final hasFocus = FocusedNotifier.isFocused(context);
    final audioPlayer = ref.watch(audioPlayerProvider);

    final showPlaying = hasFocus || hovering.value;

    useEffect(
      () {
        if (showPlaying) {
          _playSound(ref.passing);
          controller.forward(from: 0);
        } else if (!showPlaying) {
          audioPlayer.stop();
          controller.reset();
        }
        return null;
      },
      [hasFocus, hovering.value],
    );

    return AspectRatio(
      aspectRatio: 1 / 2,
      child: MouseRegion(
        onEnter: (_) => hovering.value = true,
        onExit: (_) => hovering.value = false,
        child: Icon(showPlaying ? FontAwesomeIcons.volumeHigh : FontAwesomeIcons.play, size: 16)
            .animate(controller: controller, autoPlay: false)
            .scaleXY(duration: 300.ms, begin: 1, end: 1.2)
            .shake(delay: 100.ms, duration: 500.ms, curve: Curves.easeInOutCubicEmphasized)
            .scaleXY(delay: 600.ms, duration: 300.ms, begin: 1.2, end: 1),
      ),
    );
  }
}

extension _SearchBuilderX on SearchBuilder {
  void fetchSounds({
    FutureOr<bool?> Function(MinecraftSound)? onSelect,
    bool disabled = false,
  }) {
    fetch(SoundsFetcher(onSelect: onSelect, disabled: disabled));
  }
}

class SoundSelectorEditorFilter extends EditorFilter {
  @override
  bool canEdit(FieldInfo info) =>
      info is PrimitiveField && info.type == PrimitiveFieldType.string && info.hasModifier("sound");

  @override
  Widget build(String path, FieldInfo info) => SoundSelectorEditor(path: path, field: info as PrimitiveField);
}

class SoundSelectorEditor extends HookConsumerWidget {
  const SoundSelectorEditor({
    required this.path,
    required this.field,
    super.key,
  });

  final String path;
  final PrimitiveField field;

  bool? _update(PassingRef ref, String value) {
    ref.read(inspectingEntryDefinitionProvider)?.updateField(ref, path, value);
    return null;
  }

  void _select(PassingRef ref) {
    ref.read(searchProvider.notifier).asBuilder()
      ..fetchSounds(onSelect: (sound) => _update(ref, sound.key))
      ..open();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(fieldValueProvider(path, ""));

    final sound = ref.watch(minecraftSoundProvider(value));

    return sound.map(
      data: (data) => _buildSelector(context, ref, data.value),
      loading: (_) => _buildLoading(context),
      error: (error) {
        debugPrint(error.error.toString());
        debugPrintStack(label: "SoundSelectorEditor", maxFrames: 10, stackTrace: error.stackTrace);
        return _buildError(context);
      },
    );
  }

  Widget _buildLoading(BuildContext context) {
    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: Padding(
        padding: const EdgeInsets.only(right: 16, left: 12, top: 12, bottom: 12),
        child: Row(
          children: [
            const CircularProgressIndicator(),
            const SizedBox(width: 8),
            Expanded(child: Text("Loading Sounds...", style: Theme.of(context).inputDecorationTheme.hintStyle)),
            const SizedBox(width: 12),
            FaIcon(
              FontAwesomeIcons.xmark,
              size: 16,
              color: Theme.of(context).inputDecorationTheme.hintStyle?.color,
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildError(BuildContext context) {
    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: Padding(
        padding: const EdgeInsets.only(right: 16, left: 12, top: 12, bottom: 12),
        child: Row(
          children: [
            const Icon(Icons.error, color: Colors.redAccent),
            const SizedBox(width: 8),
            Expanded(
                child: Text("Failed to load sounds",
                    style: Theme.of(context).inputDecorationTheme.hintStyle?.copyWith(color: Colors.redAccent),),),
            const SizedBox(width: 12),
            const FaIcon(
              FontAwesomeIcons.xmark,
              size: 16,
              color: Colors.redAccent,
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSelector(BuildContext context, WidgetRef ref, MinecraftSound? sound) {
    return Material(
      color: Theme.of(context).inputDecorationTheme.fillColor,
      borderRadius: BorderRadius.circular(8),
      child: InkWell(
        onTap: () => _select(ref.passing),
        borderRadius: BorderRadius.circular(8),
        child: Padding(
          padding: const EdgeInsets.only(right: 16),
          child: Row(
            children: [
              if (sound != null) Expanded(child: _ChosenSound(sound: sound)) else _buildEmpty(context),
              const SizedBox(width: 12),
              FaIcon(
                FontAwesomeIcons.caretDown,
                size: 16,
                color: Theme.of(context).inputDecorationTheme.hintStyle?.color,
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildEmpty(BuildContext context) {
    return Expanded(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
        child: Row(
          children: [
            Icon(Icons.music_note, color: Theme.of(context).inputDecorationTheme.hintStyle?.color),
            const SizedBox(width: 8),
            Text("Select a sound", style: Theme.of(context).inputDecorationTheme.hintStyle),
          ],
        ),
      ),
    );
  }
}

class _ChosenSound extends HookConsumerWidget {
  const _ChosenSound({
    required this.sound,
    super.key,
  });

  final MinecraftSound sound;

  void _playSound(PassingRef ref) {
    if (!kIsWeb) return;
    final audioPlayer = ref.read(audioPlayerProvider);
    final url = sound.value.pickRandomSoundUrl();
    final source = UrlSource(url);
    audioPlayer.play(source);
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useAnimationController(duration: 1.seconds);
    final hovering = useState(false);
    final audioPlayer = ref.watch(audioPlayerProvider);

    final showPlaying = hovering.value;

    useEffect(
      () {
        if (showPlaying) {
          _playSound(ref.passing);
          controller.forward(from: 0);
        } else {
          audioPlayer.stop();
          controller.reset();
        }
        return null;
      },
      [hovering.value],
    );

    return Padding(
      padding: const EdgeInsets.only(left: 16, top: 12, bottom: 12),
      child: Row(
        children: [
          MouseRegion(
            onEnter: (_) => hovering.value = true,
            onExit: (_) => hovering.value = false,
            child: Icon(showPlaying ? FontAwesomeIcons.volumeHigh : FontAwesomeIcons.play, size: 16)
                .animate(controller: controller, autoPlay: false)
                .scaleXY(duration: 300.ms, begin: 1, end: 1.2)
                .shake(delay: 100.ms, duration: 500.ms, curve: Curves.easeInOutCubicEmphasized)
                .tint(color: Colors.amber)
                .scaleXY(delay: 600.ms, duration: 300.ms, begin: 1.2, end: 1),
          ),
          const SizedBox(width: 16),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(sound.name.formatted, style: Theme.of(context).textTheme.bodyLarge),
              Text(sound.category.formatted, style: Theme.of(context).textTheme.bodySmall),
            ],
          ),
        ],
      ),
    );
  }
}
