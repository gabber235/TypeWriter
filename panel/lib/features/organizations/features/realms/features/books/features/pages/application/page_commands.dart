import "package:typewriter_panel/infrastructure/protocols/skir/skir.dart"
    as skir;
import "package:typewriter_panel/infrastructure/protocols/skir/skirout/library/v1/authoring.dart"
    as wire;
import "package:typewriter_panel/typewriter_panel.dart";

/// Builds page mutations at the shared authoring session boundary.
///
/// The session submits operations to the realm and later reconciles the
/// resulting confirmed snapshot. These commands do not maintain a second page
/// store, and patch fields carry expected values so stale metadata is rejected
/// rather than overwritten.
extension PageCommands on AuthoringSession {
  /// Creates [page] through the session's authoring batch protocol.
  Future<wire.ApplyAuthoringBatchResponse> createPage(wire.Page page) =>
      apply([wire.AuthoringOperation.createCreatePage(page: page)]);

  /// Deletes the page identified by [id] through the authoring boundary.
  Future<wire.ApplyAuthoringBatchResponse> deletePage(skir.RecordId id) =>
      apply([wire.AuthoringOperation.createDeletePage(id: id)]);

  /// Applies the supplied metadata changes if at least one field is present.
  ///
  /// Each non null change contains its expected old value. The returned wire
  /// outcome distinguishes application, rejection, and other boundary states.
  Future<wire.ApplyAuthoringBatchResponse> patchPage({
    required skir.RecordId id,
    wire.StringChange? name,
    wire.StringChange? chapter,
    wire.Int32Change? priority,
  }) {
    if (name == null && chapter == null && priority == null) {
      throw ApiException.badRequest("At least one page field is required");
    }
    return apply([
      wire.AuthoringOperation.createPatchPage(
        id: id,
        book: null,
        name: name,
        chapter: chapter,
        priority: priority,
      ),
    ]);
  }

  /// Renames a chapter path for every supplied page in one authoring batch.
  ///
  /// The page collection must include the complete subtree selected by the
  /// caller. Each operation expects the page's current chapter, making a
  /// concurrent change observable as a conflict.
  Future<wire.ApplyAuthoringBatchResponse> changePagesChapters(
    Iterable<Page> pages,
    String oldChapter,
    String newChapter,
  ) => apply([
    for (final page in pages)
      wire.AuthoringOperation.createPatchPage(
        id: page.pageId,
        book: null,
        name: null,
        chapter: wire.StringChange(
          expected: page.chapter,
          value: replacePageChapter(page.chapter, oldChapter, newChapter),
        ),
        priority: null,
      ),
  ]);
}

/// Rewrites one chapter path while preserving its descendant suffix.
///
/// A chapter belongs to [oldChapter] when it is equal to it or starts with
/// that path followed by a period. Otherwise this reports invalid caller input.
/// Removing the selected root returns the suffix without its leading period.
String replacePageChapter(
  String chapter,
  String oldChapter,
  String newChapter,
) {
  if (chapter != oldChapter && !chapter.startsWith("$oldChapter.")) {
    throw ApiException.badRequest("The page is not in the selected chapter");
  }
  final suffix = chapter.substring(oldChapter.length);
  if (newChapter.isEmpty && suffix.startsWith(".")) return suffix.substring(1);
  return "$newChapter$suffix";
}
