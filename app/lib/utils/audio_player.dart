import "package:audioplayers/audioplayers.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";

part "audio_player.g.dart";

@riverpod
AudioPlayer audioPlayer(AudioPlayerRef ref) {
  return AudioPlayer();
}
