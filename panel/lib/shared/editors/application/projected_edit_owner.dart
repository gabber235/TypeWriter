import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// A view into an owner. It owns no draft, revision, or submission.
final class ProjectedEditOwner implements EditOwner {
  ProjectedEditOwner(this.owner, this.path);

  final EditOwner owner;
  final DataPath path;

  @override
  TypeExpression get rootType => owner.rootType
      .resolvePath(path, registry: TypeRegistry(typeCatalog))
      .valueOrNull!;

  @override
  TypeCatalog get typeCatalog => owner.typeCatalog;

  @override
  bool get readOnly => owner.readOnly;

  @override
  EditorValue value(DataPath path) => owner.value(this.path.followedBy(path));

  @override
  EditorMutationResult validate(DataPath path, DataValue value) =>
      owner.validate(this.path.followedBy(path), value);
  @override
  EditorMutationResult update(
    DataPath path,
    DataValue value, {
    EditorStructuralMutation? structuralMutation,
  }) => owner.update(
    this.path.followedBy(path),
    value,
    structuralMutation: structuralMutation?.prefixedBy(this.path),
  );

  @override
  EditorInteractionSession beginInteraction(DataPath path) =>
      owner.beginInteraction(this.path.followedBy(path));

  @override
  void addListener(VoidCallback listener) => owner.addListener(listener);

  @override
  void removeListener(VoidCallback listener) => owner.removeListener(listener);

  @override
  void dispose() {}
}
