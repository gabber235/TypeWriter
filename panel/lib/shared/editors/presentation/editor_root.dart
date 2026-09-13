import "package:flutter/widgets.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Provides one editor source to a subtree and disposes it with that subtree.
///
/// Use this boundary when the source is created from Riverpod dependencies.
/// Descendants such as [TypedEditor] read the source without owning its
/// lifecycle. Rebuilding the child does not recreate the source; the nested
/// provider scope establishes the ownership boundary.
final editorProvider = Provider<EditorSource?>((ref) => null, dependencies: []);

/// Establishes the provider scope for a typed editor subtree.
class EditorRoot extends ConsumerWidget {
  const EditorRoot({required this.create, required this.child, super.key});

  final EditorSource Function(Ref ref) create;
  final Widget child;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return ProviderScope(
      overrides: [
        editorProvider.overrideWith((ref) {
          final source = create(ref);
          ref.onDispose(source.dispose);
          return source;
        }),
      ],
      child: child,
    );
  }
}
