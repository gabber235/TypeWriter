import "package:collection/collection.dart";
import "package:flutter/foundation.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:typewriter/hooks/text_size.dart";

typedef ContextMenuBuilder = List<ContextMenuTile> Function(BuildContext);

class ContextMenuRegion extends StatefulWidget {
  const ContextMenuRegion({
    required this.child,
    required this.builder,
    this.enabled = true,
    super.key,
  });

  final Widget child;
  final ContextMenuBuilder builder;
  final bool enabled;

  @override
  State<ContextMenuRegion> createState() => _ContextMenuRegionState();
}

class _ContextMenuRegionState extends State<ContextMenuRegion> {
  Offset? _longPressOffset;

  final ContextMenuController _contextMenuController = ContextMenuController();

  static bool get _longPressEnabled {
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
      case TargetPlatform.iOS:
        return true;
      case TargetPlatform.macOS:
      case TargetPlatform.fuchsia:
      case TargetPlatform.linux:
      case TargetPlatform.windows:
        return false;
    }
  }

  void _onSecondaryTapUp(TapUpDetails details) {
    _show(details.globalPosition);
  }

  void _onTap() {
    if (!_contextMenuController.isShown) {
      return;
    }
    _hide();
  }

  void _onLongPressStart(LongPressStartDetails details) {
    _longPressOffset = details.globalPosition;
  }

  void _onLongPress() {
    assert(_longPressOffset != null);
    _show(_longPressOffset!);
    _longPressOffset = null;
  }

  void _show(Offset position) {
    final tiles = widget.builder(context);
    if (tiles.isEmpty) {
      return;
    }

    final box = context.findRenderObject() as RenderBox?;

    if (box == null) {
      return;
    }

    // Get on which part of the screen the user tapped. The four options are TopLeft, TopRight, BottomLeft, BottomRight
    final quadrant = _getQuadrant(box);

    _contextMenuController.show(
      context: context,
      contextMenuBuilder: (context) {
        return _ContextMenu(
          controller: _contextMenuController,
          quadrant: quadrant,
          position: position,
          tiles: tiles,
        );
      },
    );
  }

  /// Get on which part of the screen the button is. The four options are TopLeft, TopRight, BottomLeft, BottomRight
  /// This is used to determine where the context menu should be shown
  Quadrant _getQuadrant(RenderBox box) {
    final boxSize = box.size;
    final boxPosition = box.localToGlobal(Offset.zero);
    final boxCenter =
        boxPosition + Offset(boxSize.width / 2, boxSize.height / 2);

    final screenSize = MediaQuery.of(context).size;

    final isTop = boxCenter.dy < screenSize.height / 2;
    final isLeft = boxCenter.dx < screenSize.width / 2;

    if (isTop && isLeft) {
      return Quadrant.topLeft;
    } else if (isTop && !isLeft) {
      return Quadrant.topRight;
    } else if (!isTop && isLeft) {
      return Quadrant.bottomLeft;
    } else {
      return Quadrant.bottomRight;
    }
  }

  void _hide() {
    _contextMenuController.remove();
  }

  @override
  void dispose() {
    _hide();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (!widget.enabled) {
      return widget.child;
    }
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onSecondaryTapUp: _onSecondaryTapUp,
      onTap: _onTap,
      onLongPress: _longPressEnabled ? _onLongPress : null,
      onLongPressStart: _longPressEnabled ? _onLongPressStart : null,
      child: widget.child,
    );
  }
}

enum Quadrant {
  topLeft,
  topRight,
  bottomLeft,
  bottomRight,
}

class ContextMenuTile {
  const ContextMenuTile();

  factory ContextMenuTile.button({
    required String title,
    VoidCallback? onTap,
    IconData? icon,
    Color? color,
  }) = ContextMenuButton;

  factory ContextMenuTile.divider() = ContextMenuDivider;

  String get largestText => "";
}

class ContextMenuButton extends ContextMenuTile {
  const ContextMenuButton({
    required this.title,
    this.onTap,
    this.icon,
    this.color,
  });

  final String title;
  final VoidCallback? onTap;
  final IconData? icon;
  final Color? color;

  @override
  String get largestText => title;
}

class ContextMenuDivider extends ContextMenuTile {}

class _ContextMenu extends HookWidget {
  const _ContextMenu({
    required this.controller,
    required this.quadrant,
    required this.position,
    required this.tiles,
  });

  final ContextMenuController controller;

  final Quadrant quadrant;
  final Offset position;
  final List<ContextMenuTile> tiles;

  static const _spacingWidth = 80;

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;

    final largestText =
        maxBy(tiles.map((e) => e.largestText), (text) => text.length) ?? "";
    final textSize = useTextSize(
      context,
      largestText,
      const TextStyle(fontSize: 13, fontFamily: "JetBrainsMono"),
    );
    final maxWidth = textSize.width + _spacingWidth;

    return Positioned(
      left: quadrant == Quadrant.topLeft || quadrant == Quadrant.bottomLeft
          ? position.dx
          : null,
      right: quadrant == Quadrant.topRight || quadrant == Quadrant.bottomRight
          ? size.width - position.dx
          : null,
      top: quadrant == Quadrant.topLeft || quadrant == Quadrant.topRight
          ? position.dy
          : null,
      bottom:
          quadrant == Quadrant.bottomLeft || quadrant == Quadrant.bottomRight
              ? size.height - position.dy
              : null,
      child: Listener(
        onPointerUp: (_) => controller.remove(),
        child: ConstrainedBox(
          constraints: BoxConstraints(
            // Don't let the menu go off screen
            maxHeight:
                quadrant == Quadrant.topLeft || quadrant == Quadrant.topRight
                    ? size.height - position.dy - 10
                    : position.dy - 10,
            maxWidth: maxWidth,
          ),
          child: Material(
            borderRadius: BorderRadius.circular(8),
            elevation: 3,
            color: Theme.of(context).cardColor,
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: SingleChildScrollView(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    for (final tile in tiles)
                      if (tile is ContextMenuButton)
                        _ContextMenuButton(tile: tile)
                      else if (tile is ContextMenuDivider)
                        const Divider(),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _ContextMenuButton extends StatelessWidget {
  const _ContextMenuButton({
    required this.tile,
  });

  final ContextMenuButton tile;

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      borderRadius: BorderRadius.circular(8),
      child: InkWell(
        onTap: tile.onTap,
        hoverColor: tile.color?.withOpacity(0.15),
        highlightColor: tile.color?.withOpacity(0.2),
        splashColor: tile.color?.withOpacity(0.3),
        borderRadius: BorderRadius.circular(8),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          child: Row(
            children: [
              Icon(tile.icon, color: tile.color, size: 16),
              const SizedBox(width: 10),
              Text(
                tile.title,
                style: TextStyle(
                  fontSize: 13,
                  color: tile.color,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

/// Builds and manages a context menu at a given location.
///
/// There can only ever be one context menu shown at a given time in the entire
/// app.
///
/// {@tool dartpad}
/// This example shows how to use a GestureDetector to show a context menu
/// anywhere in a widget subtree that receives a right click or long press.
///
/// ** See code in examples/api/lib/material/context_menu/context_menu_controller.0.dart **
/// {@end-tool}
class ContextMenuController {
  /// Creates a context menu that can be shown with [show].
  ContextMenuController({
    this.onRemove,
  });

  /// Called when this menu is removed.
  final VoidCallback? onRemove;

  /// The currently shown instance, if any.
  static ContextMenuController? _shownInstance;

  /// True if and only if there is a context menu currently being shown.
  static bool get hasContextMenu => _shownInstance != null;

  // The OverlayEntry is static because only one context menu can be displayed
  // at one time.
  static OverlayEntry? _menuOverlayEntry;

  /// Shows the given context menu.
  ///
  /// Since there can only be one shown context menu at a time, calling this
  /// will also remove any other context menu that is visible.
  void show({
    required BuildContext context,
    required WidgetBuilder contextMenuBuilder,
    Widget? debugRequiredFor,
  }) {
    removeAny();
    final overlayState = Overlay.of(
      context,
      rootOverlay: true,
      debugRequiredFor: debugRequiredFor,
    );
    final capturedThemes = InheritedTheme.capture(
      from: context,
      to: Navigator.maybeOf(context)?.context,
    );

    _menuOverlayEntry = OverlayEntry(
      builder: (context) {
        return capturedThemes.wrap(
          Stack(
            fit: StackFit.expand,
            children: [
              Listener(
                behavior: HitTestBehavior.translucent,
                onPointerDown: (_) => removeAny(),
              ),
              contextMenuBuilder(context),
            ],
          ),
        );
      },
    );
    overlayState.insert(_menuOverlayEntry!);
    _shownInstance = this;
  }

  /// Remove the currently shown context menu from the UI.
  ///
  /// Does nothing if no context menu is currently shown.
  ///
  /// If a menu is removed, and that menu provided an [onRemove] callback when
  /// it was created, then that callback will be called.
  ///
  /// See also:
  ///
  ///  * [remove], which removes only the current instance.
  static void removeAny() {
    _menuOverlayEntry?.remove();
    _menuOverlayEntry = null;
    if (_shownInstance != null) {
      _shownInstance!.onRemove?.call();
      _shownInstance = null;
    }
  }

  /// True if and only if this menu is currently being shown.
  bool get isShown => _shownInstance == this;

  /// Cause the underlying [OverlayEntry] to rebuild during the next pipeline
  /// flush.
  ///
  /// It's necessary to call this function if the output of [contextMenuBuilder]
  /// has changed.
  ///
  /// Errors if the context menu is not currently shown.
  ///
  /// See also:
  ///
  ///  * [OverlayEntry.markNeedsBuild]
  void markNeedsBuild() {
    assert(isShown);
    _menuOverlayEntry?.markNeedsBuild();
  }

  /// Remove this menu from the UI.
  ///
  /// Does nothing if this instance is not currently shown. In other words, if
  /// another context menu is currently shown, that menu will not be removed.
  ///
  /// This method should only be called once. The instance cannot be shown again
  /// after removing. Create a new instance.
  ///
  /// If an [onRemove] method was given to this instance, it will be called.
  ///
  /// See also:
  ///
  ///  * [removeAny], which removes any shown instance of the context menu.
  void remove() {
    if (!isShown) {
      return;
    }
    removeAny();
  }
}
