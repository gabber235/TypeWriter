import "package:typewriter_panel/typewriter_panel.dart";

/// Removes duplicate result IDs from a snapshot tree.
///
/// The first depth first occurrence wins. Empty sections are removed after
/// their children have been filtered.
final class DistinctSearchSource extends DelegatingSearchSource {
  DistinctSearchSource({required super.source});

  @override
  void onSnapshot(SearchSourceSnapshot snapshot) {
    emit(snapshot.copyWith(nodes: _distinct(snapshot.nodes, <String>{})));
  }

  List<SearchNode> _distinct(List<SearchNode> nodes, Set<String> seen) {
    final distinct = <SearchNode>[];
    for (final node in nodes) {
      switch (node) {
        case SearchResultNode(:final result):
          if (seen.add(result.id)) distinct.add(node);
        case SearchSectionNode():
          final children = _distinct(node.children, seen);
          if (children.isNotEmpty) {
            distinct.add(node.copyWith(children: children));
          }
      }
    }
    return distinct;
  }
}

/// Adds first occurrence filtering to a source.
extension DistinctSearchSourceX on SearchSource {
  SearchSource distinct() {
    return DistinctSearchSource(source: this);
  }
}
