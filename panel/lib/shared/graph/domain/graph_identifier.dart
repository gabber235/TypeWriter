/// Stable identity used to connect graph elements, edges, and interaction
/// payloads without relying on widget identity.
class GraphIdentifier {
  const GraphIdentifier(this.id);

  final String id;

  @override
  String toString() => id;

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is GraphIdentifier && id == other.id;
  }

  @override
  int get hashCode => id.hashCode;
}
