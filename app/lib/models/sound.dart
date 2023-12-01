import "package:freezed_annotation/freezed_annotation.dart";

part "sound.freezed.dart";
part "sound.g.dart";

@Freezed(unionKey: "type")
class SoundId with _$SoundId {
  const factory SoundId({
    @JsonKey(name: "value") required String id,
  }) = DefaultSoundId;
  const factory SoundId.entry({
    @JsonKey(name: "value") required String entryId,
  }) = EntrySoundId;

  factory SoundId.fromJson(Map<String, dynamic> json) =>
      _$SoundIdFromJson(json);
}
