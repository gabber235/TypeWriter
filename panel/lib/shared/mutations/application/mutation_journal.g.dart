// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'mutation_journal.dart';

// **************************************************************************
// RiverpodGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint, type=warning

@ProviderFor(mutationJournal)
final mutationJournalProvider = MutationJournalProvider._();

final class MutationJournalProvider
    extends
        $FunctionalProvider<MutationJournal, MutationJournal, MutationJournal>
    with $Provider<MutationJournal> {
  MutationJournalProvider._()
    : super(
        from: null,
        argument: null,
        retry: null,
        name: r'mutationJournalProvider',
        isAutoDispose: false,
        dependencies: null,
        $allTransitiveDependencies: null,
      );

  @override
  String debugGetCreateSourceHash() => _$mutationJournalHash();

  @$internal
  @override
  $ProviderElement<MutationJournal> $createElement($ProviderPointer pointer) =>
      $ProviderElement(pointer);

  @override
  MutationJournal create(Ref ref) {
    return mutationJournal(ref);
  }

  /// {@macro riverpod.override_with_value}
  Override overrideWithValue(MutationJournal value) {
    return $ProviderOverride(
      origin: this,
      providerOverride: $SyncValueProvider<MutationJournal>(value),
    );
  }
}

String _$mutationJournalHash() => r'151afbfd93fe6ddc708568024e9c1251db9b661c';
