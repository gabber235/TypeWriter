import "package:typewriter_panel/typewriter_panel.dart";

/// The atomic outcome of comparing one local draft with a newer remote value.
///
/// `base` becomes the next canonical value and `draft` becomes the value shown
/// to the user. `revision` belongs to `base`. `dirtyPaths` are local choices
/// still requiring persistence, `confirmedPaths` are local choices already
/// accepted by the remote value, and `conflicts` preserve paths where both
/// sides changed different ways. Diagnostics report paths that could not be
/// applied safely. Consumers should install this result as one owner controlled
/// transition, never as unrelated field updates.
final class EditorReconciliationResult {
  const EditorReconciliationResult({
    required this.base,
    required this.draft,
    required this.revision,
    required this.dirtyPaths,
    required this.confirmedPaths,
    required this.conflicts,
    this.diagnostics = const [],
  });

  final DataValue base;
  final DataValue draft;
  final int revision;
  final Set<DataPath> dirtyPaths;
  final Set<DataPath> confirmedPaths;
  final Map<DataPath, EditorPathConflict> conflicts;
  final List<TypeDiagnostic> diagnostics;
}

/// Applies the editor's merge policy at every locally dirty path.
///
/// The comparison is three way: `base` is the last canonical value, `local`
/// is the unsaved draft, and `remote` is the newer canonical observation. An
/// unchanged remote path keeps the local edit. A remote value equal to local
/// confirms that edit. When both changed, `atomic` and `orderedList` conflict,
/// `record` descends by field, and `set` merges membership. Invalid paths are
/// retained as diagnostics instead of being silently discarded.
final class EditorReconciler {
  const EditorReconciler();

  EditorReconciliationResult reconcile({
    required DataValue base,
    required DataValue local,
    required DataValue remote,
    required int remoteRevision,
    required Set<DataPath> dirtyPaths,
    required Map<DataPath, EditorMergePolicy> mergePolicies,
  }) {
    var draft = remote;
    final remaining = <DataPath>{};
    final confirmed = <DataPath>{};
    final conflicts = <DataPath, EditorPathConflict>{};
    final diagnostics = <TypeDiagnostic>[];

    for (final path in dirtyPaths) {
      final baseValue = path.read(base).valueOrNull;
      final localValue = path.read(local).valueOrNull;
      final remoteValue = path.read(remote).valueOrNull;
      if (baseValue == null || localValue == null || remoteValue == null) {
        diagnostics.add(_invalidPath(path));
        continue;
      }
      if (remoteValue == baseValue) {
        draft = _replace(draft, path, localValue, diagnostics);
        remaining.add(path);
        continue;
      }
      if (remoteValue == localValue) {
        confirmed.add(path);
        continue;
      }

      final policy = _policyFor(path, baseValue, mergePolicies);
      final merged = _merge(
        path: path,
        base: baseValue,
        local: localValue,
        remote: remoteValue,
        policy: policy,
        mergePolicies: mergePolicies,
      );

      draft = _replace(draft, path, merged.value, diagnostics);

      remaining.addAll(merged.dirtyPaths);
      conflicts.addAll(merged.conflicts);
    }

    return EditorReconciliationResult(
      base: remote,
      draft: draft,
      revision: remoteRevision,
      dirtyPaths: remaining,
      confirmedPaths: confirmed,
      conflicts: conflicts,
      diagnostics: diagnostics,
    );
  }

  _MergeResult _merge({
    required DataPath path,
    required DataValue base,
    required DataValue local,
    required DataValue remote,
    required EditorMergePolicy policy,
    required Map<DataPath, EditorMergePolicy> mergePolicies,
  }) => switch (policy) {
    EditorMergePolicy.atomic ||
    EditorMergePolicy.orderedList => _conflict(path, base, local, remote),
    EditorMergePolicy.set => _mergeSet(path, base, local, remote),
    EditorMergePolicy.record => _mergeRecord(
      path,
      base,
      local,
      remote,
      mergePolicies,
    ),
  };

  _MergeResult _mergeRecord(
    DataPath path,
    DataValue base,
    DataValue local,
    DataValue remote,
    Map<DataPath, EditorMergePolicy> policies,
  ) {
    if (base is! RecordValue ||
        local is! RecordValue ||
        remote is! RecordValue) {
      return _conflict(path, base, local, remote);
    }
    final names = {
      ...base.fields.keys,
      ...local.fields.keys,
      ...remote.fields.keys,
    };
    final fields = Map<String, DataValue>.of(remote.fields);
    final dirty = <DataPath>{};

    final conflicts = <DataPath, EditorPathConflict>{};
    for (final name in names) {
      final childPath = path.field(name);
      final baseChild = base.fields[name];
      final localChild = local.fields[name];
      final remoteChild = remote.fields[name];
      if (baseChild == null || localChild == null || remoteChild == null) {
        return _conflict(path, base, local, remote);
      }

      if (localChild == baseChild) continue;
      if (remoteChild == baseChild) {
        fields[name] = localChild;
        dirty.add(childPath);
        continue;
      }

      if (remoteChild == localChild) continue;
      final merged = _merge(
        path: childPath,
        base: baseChild,
        local: localChild,
        remote: remoteChild,
        policy: _policyFor(childPath, baseChild, policies),
        mergePolicies: policies,
      );

      fields[name] = merged.value;

      dirty.addAll(merged.dirtyPaths);
      conflicts.addAll(merged.conflicts);
    }
    return _MergeResult(
      value: RecordValue(fields),
      dirtyPaths: dirty,
      conflicts: conflicts,
    );
  }

  _MergeResult _mergeSet(
    DataPath path,
    DataValue base,
    DataValue local,
    DataValue remote,
  ) {
    if (base is! ListValue || local is! ListValue || remote is! ListValue) {
      return _conflict(path, base, local, remote);
    }
    final ordered = [...local.values, ...remote.values];
    final merged = <DataValue>[];
    for (final candidate in ordered) {
      if (merged.contains(candidate)) continue;
      final inBase = base.values.contains(candidate);
      final inLocal = local.values.contains(candidate);
      final inRemote = remote.values.contains(candidate);

      final localChanged = inLocal != inBase;

      final present = localChanged ? inLocal : inRemote;
      if (present) merged.add(candidate);
    }
    return _MergeResult(
      value: ListValue(merged),
      dirtyPaths: {path},
      conflicts: const {},
    );
  }

  _MergeResult _conflict(
    DataPath path,
    DataValue base,
    DataValue local,
    DataValue remote,
  ) => _MergeResult(
    value: local,
    dirtyPaths: {path},
    conflicts: {
      path: EditorPathConflict(base: base, local: local, remote: remote),
    },
  );

  EditorMergePolicy _policyFor(
    DataPath path,
    DataValue value,
    Map<DataPath, EditorMergePolicy> configured,
  ) {
    final selected = configured[path];
    if (selected != null) return selected;
    if (value is RecordValue) return EditorMergePolicy.record;
    if (value is ListValue) return EditorMergePolicy.orderedList;
    return EditorMergePolicy.atomic;
  }

  DataValue _replace(
    DataValue root,
    DataPath path,
    DataValue value,
    List<TypeDiagnostic> diagnostics,
  ) {
    final replaced = path.replace(root, value);
    if (replaced case TypeFailure(diagnostics: final failureDiagnostics)) {
      diagnostics.addAll(failureDiagnostics);
      return root;
    }
    return replaced.valueOrNull!;
  }
}

final class _MergeResult {
  const _MergeResult({
    required this.value,
    required this.dirtyPaths,
    required this.conflicts,
  });

  final DataValue value;
  final Set<DataPath> dirtyPaths;
  final Map<DataPath, EditorPathConflict> conflicts;
}

/// Describes a remote shape that cannot be applied at a locally tracked path.
TypeDiagnostic _invalidPath(DataPath path) => TypeDiagnostic(
  code: TypeDiagnosticCode.invalidPath,
  message: "Remote value cannot be reconciled at this path",
  path: path,
);
