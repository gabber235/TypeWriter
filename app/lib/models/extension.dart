import "package:flutter/foundation.dart";
import "package:freezed_annotation/freezed_annotation.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/entry_blueprint.dart";

part "extension.freezed.dart";
part "extension.g.dart";

/// A generated provider to fetch and cache a list of [Extension]s.
@riverpod
List<Extension> extensions(ExtensionsRef ref) =>
    ref.watch(bookProvider).extensions;

/// A data model that represents an extension.
@freezed
class Extension with _$Extension {
  const factory Extension({
    required ExtensionInfo extension,
    required List<EntryBlueprint> entries,
  }) = _Extension;

  factory Extension.fromJson(Map<String, dynamic> json) =>
      _$ExtensionFromJson(json);
}

@freezed
class ExtensionInfo with _$ExtensionInfo {
  const factory ExtensionInfo({
    required String name,
    required String shortDescription,
    required String description,
    required String version,
  }) = _ExtensionInfo;

  factory ExtensionInfo.fromJson(Map<String, dynamic> json) =>
      _$ExtensionInfoFromJson(json);
}
