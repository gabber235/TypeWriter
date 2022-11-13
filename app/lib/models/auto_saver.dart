/// Auto saves when the book provider changes.
/// It has a 1 second debounce to avoid saving too often.
import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/models/book.dart';
import 'package:typewriter/utils/debouncer.dart';

part 'auto_saver.freezed.dart';

final autoSaverProvider =
    StateNotifierProvider.autoDispose<_AutoSaverNotifier, AutoSaverState>(
        (ref) {
  final autoSaver = _AutoSaverNotifier(ref);
  ref.listen(bookProvider, (previous, next) => autoSaver.onBookChanged());
  return autoSaver;
});

class _AutoSaverNotifier extends StateNotifier<AutoSaverState> {
  final Ref _ref;
  final Debouncer _debouncer = Debouncer(milliseconds: 5000);

  _AutoSaverNotifier(this._ref)
      : super(AutoSaverState.saved(time: DateTime.utc(0)));

  void onBookChanged() {
    state = const AutoSaverState.saving();
    _debouncer.run(() async {
      try {
        await _ref.read(bookProvider.notifier).save();
        state = AutoSaverState.saved(time: DateTime.now());
      } catch (e) {
        state = AutoSaverState.error(e.toString());
      }
    });
  }

  @override
  void dispose() {
    _ref.read(bookProvider.notifier).save();
    super.dispose();
  }
}

@freezed
class AutoSaverState with _$AutoSaverState {
  const factory AutoSaverState.saving() = SavingState;

  const factory AutoSaverState.saved({
    required DateTime time,
  }) = SavedInfo;

  const factory AutoSaverState.error(String error) = ErrorState;
}
