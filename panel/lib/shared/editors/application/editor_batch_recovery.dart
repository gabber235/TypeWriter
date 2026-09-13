part of "transactional_editor_source.dart";

extension _BatchRecovery on EditorBatch {
  Future<Map<TransactionalEditorSource, TypedMutationResult>> _retryRejected() {
    if (_recovery case final active?) return active;
    if (_paths.entries.any(
      (entry) =>
          entry.key._disposed ||
          entry.key._deleted ||
          !identical(entry.key._rejectedBatch, this) ||
          entry.value.any((path) => entry.key.value(path).valueOrNull == null),
    )) {
      return Future.value({
        for (final source in _paths.keys)
          source: _unavailable(
            "A resource in this batch is no longer available",
          ),
      });
    }

    final operation = EditorBatch._flushPrepared(_paths, send: _send);
    _recovery = operation;

    return operation.whenComplete(() => _recovery = null);
  }

  Future<Map<TransactionalEditorSource, TypedMutationResult>> _retry() {
    if (_recovery case final active?) return active;
    final operation = _recover();
    _recovery = operation;
    return operation.whenComplete(() => _recovery = null);
  }

  Future<Map<TransactionalEditorSource, TypedMutationResult>> _recover() async {
    final uncertain = _results.entries
        .where((entry) => entry.value is MutationUncertain)
        .toList();
    if (uncertain.isEmpty) return _results;
    final first = uncertain.first;
    final result = await (first.value as MutationUncertain).replay!();

    if (result is MutationUncertain) return _results;
    _results = {
      first.key: result,
      for (final entry in uncertain.skip(1))
        entry.key: await (entry.value as MutationUncertain).replay!(),
    };

    _settle();
    return _results;
  }
}
