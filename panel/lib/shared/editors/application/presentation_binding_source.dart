import "package:typewriter_panel/typewriter_panel.dart";

/// Exposes a live editor subtree to expression evaluation.
///
/// The editor remains authoritative for both the current draft and
/// writability. This adapter only translates a binding path through [prefix]
/// and reports the editor's current value, so it never snapshots, mutates, or
/// disposes [owner]. [revision] is supplied by the caller as the revision
/// associated with the exposed observation.
final class EditOwnerBindingSource implements BindingSource {
  const EditOwnerBindingSource({
    required this.owner,
    required this.prefix,
    required this.revision,
  });

  final EditOwner owner;
  final DataPath prefix;

  @override
  final int revision;

  @override
  bool get writable => !owner.readOnly;

  @override
  TypeResult<BindingSourceState> inspect(
    DataPath path, {
    TypeRegistry? registry,
  }) {
    final target = prefix.followedBy(path);
    final type = owner.rootType.resolvePath(target, registry: registry);
    if (type case TypeFailure(:final diagnostics)) {
      return TypeResult.failure(diagnostics);
    }
    return TypeResult.success(
      BindingSourceState(type: type.valueOrNull!, value: owner.value(target)),
    );
  }
}
