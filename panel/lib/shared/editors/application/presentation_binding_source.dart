import "package:typewriter_panel/typewriter_panel.dart";

/// Exposes a live editor through the domain binding inspection contract.
///
/// The source never owns or disposes the editor. [prefix] selects the editor
/// subtree represented by the binding. Value state and writability remain live.
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
