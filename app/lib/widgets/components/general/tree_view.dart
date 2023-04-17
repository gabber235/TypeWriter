import "package:freezed_annotation/freezed_annotation.dart";

part "tree_view.freezed.dart";

@freezed
class TreeNode<T> with _$TreeNode {
  const factory TreeNode.root({
    required List<TreeNode<T>> children,
  }) = RootTreeNode;

  const factory TreeNode.inner({
    required String name,
    required List<TreeNode<T>> children,
  }) = InnerTreeNode;

  const factory TreeNode.leaf({
    required T value,
  }) = LeafTreeNode;
}

/// Creates a tree node from a list of objects with paths.
/// It nests objects to create the most common ancestor between paths.
///
/// Example, Lets say we have the following paths:
/// - some.simple.path
/// - some.other.path
/// - some.other.path.too
///
/// The result will be:
/// - some
///  - simple
///    - path
///  - other.path
///    - too
RootTreeNode<T> createTreeNode<T>(List<T> elements, String Function(T) pathFetcher) {
  return const RootTreeNode(children: []);
}
