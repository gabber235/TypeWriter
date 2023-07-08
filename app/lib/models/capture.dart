import "package:collection_ext/all.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/utils/passing_reference.dart";

part "capture.freezed.dart";
part "capture.g.dart";

@freezed
class CaptureRequest with _$CaptureRequest {
  const factory CaptureRequest({
    required String capturerClassPath,
    required String entryId,
    required String fieldPath,
    required dynamic fieldValue,
    String? cinematic,
    @IntRangeConverter() IntRange? cinematicRange,
  }) = _CaptureRequest;

  factory CaptureRequest.fromJson(Map<String, dynamic> json) =>
      _$CaptureRequestFromJson(json);
}

extension CaptureRequestX on CaptureRequest {
  Future<Response> requestCapture(PassingRef ref) {
    final communicator = ref.read(communicatorProvider);
    return communicator.requestCapture(toJson());
  }
}

class IntRangeConverter implements JsonConverter<IntRange?, dynamic> {
  const IntRangeConverter();

  @override
  IntRange? fromJson(dynamic json) {
    if (json == null) {
      return null;
    }
    if (json is! Map<String, dynamic>) {
      return null;
    }
    if (!json.containsKey("first") || !json.containsKey("last")) {
      return null;
    }
    return IntRange(json["first"], json["last"]);
  }

  @override
  dynamic toJson(IntRange? object) {
    if (object == null) {
      return null;
    }
    return {
      "first": object.from,
      "last": object.to,
    };
  }
}
