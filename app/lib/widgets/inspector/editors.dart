import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';
import 'package:typewriter/models/adapter.dart';
import 'package:typewriter/widgets/inspector.dart';
import 'package:typewriter/widgets/inspector/editors/boolean.dart';
import 'package:typewriter/widgets/inspector/editors/enum.dart';
import 'package:typewriter/widgets/inspector/editors/number.dart';
import 'package:typewriter/widgets/inspector/editors/object.dart';
import 'package:typewriter/widgets/inspector/editors/string.dart';

part 'editors.g.dart';

@riverpod
dynamic fieldValue(FieldValueRef ref, String path, [dynamic defaultValue]) {
  final entry = ref.watch(selectedEntryProvider);
  return entry?.get(path, defaultValue) ?? defaultValue;
}

@riverpod
List<EditorFilter> editorFilters(EditorFiltersRef ref) {
  return [
    StringEditorFilter(),
    NumberEditorFilter(),
    BooleanEditorFilter(),
    EnumEditorFilter(),
    ObjectEditorFilter(),
  ];
}

abstract class EditorFilter {
  bool canFilter(FieldType type);

  Widget build(String path, FieldType type);
}
