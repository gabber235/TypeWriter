/// Auto saves when the book provider changes.
/// It has a 1 second debounce to avoid saving too often.
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';
import 'package:typewriter/models/book.dart';
import 'package:typewriter/utils/debouncer.dart';

part 'auto_saver.freezed.dart';

part 'auto_saver.g.dart';

final _debouncerProvider =
    Provider<Debouncer>((ref) => Debouncer(milliseconds: 1000));

@riverpod
Future<SavedInfo> autoSaver(AutoSaverRef ref) async {
  final debouncer = ref.watch(_debouncerProvider);
  ref.watch(bookProvider);
  await debouncer.run(() async {
    await ref.read(bookProvider.notifier).save();
  });
  return SavedInfo(time: DateTime.now());
}

@freezed
class SavedInfo with _$SavedInfo {
  const factory SavedInfo({
    required DateTime time,
  }) = _SavedInfo;
}
