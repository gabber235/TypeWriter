part of "route.dart";

/// Drag payload for moving one page between chapter paths.
///
/// The source chapter is retained as the expected value for the later
/// optimistic mutation.
class PageDrag {
  const PageDrag({required this.pageId, required this.chapter});

  final skir.RecordId pageId;
  final String chapter;
}

/// Drag payload for moving a chapter subtree.
///
/// The target uses the source path to reject dropping a chapter into itself or
/// one of its descendants.
class ChapterDrag {
  const ChapterDrag({required this.chapter});

  final String chapter;
}
