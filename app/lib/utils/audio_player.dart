import "package:audioplayers/audioplayers.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";

part "audio_player.g.dart";

@riverpod
AudioPlayer audioPlayer(Ref ref) {
  return AudioPlayer();
}
