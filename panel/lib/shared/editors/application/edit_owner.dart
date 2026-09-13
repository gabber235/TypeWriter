import "package:flutter/foundation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Owns mutable typed values and reversible interactions, independently of saving.
abstract interface class EditOwner implements Listenable {
  TypeExpression get rootType;
  TypeCatalog get typeCatalog;
  bool get readOnly;
  EditorValue value(DataPath path);
  EditorMutationResult validate(DataPath path, DataValue value);
  EditorMutationResult update(
    DataPath path,
    DataValue value, {
    EditorStructuralMutation? structuralMutation,
  });
  EditorInteractionSession beginInteraction(DataPath path);
  void dispose();
}
