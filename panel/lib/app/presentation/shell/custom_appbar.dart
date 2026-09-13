import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Provides the shared route app bar for desktop and mobile layouts.
///
/// [leading] stays horizontally scrollable when space is constrained. The
/// optional [trailing] content is hidden below the desktop width threshold.
/// On mobile, [sidebar] is exposed through a modal bottom sheet instead of a
/// persistent side pane. The bar also reserves space for mutation activity.
class CustomAppBar extends HookConsumerWidget implements PreferredSizeWidget {
  const CustomAppBar({
    required this.leading,
    required this.sidebar,
    this.trailing,
    this.backgroundColor,
    this.height = 48.0,
    super.key,
  });

  /// Color used for the bar and its surface. The app bar theme is the fallback.
  final Color? backgroundColor;

  /// Bar height in logical pixels.
  final double height;

  /// Leading controls and route context.
  final List<Widget> leading;

  /// Optional desktop content shown when the bar has sufficient width.
  final Widget? trailing;

  /// Sidebar content presented by the mobile menu action.
  final Widget sidebar;

  @override
  Size get preferredSize => Size.fromHeight(height);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final color =
        backgroundColor ??
        Theme.of(context).appBarTheme.backgroundColor ??
        Theme.of(context).colorScheme.surface;

    return Pane(
      id: "appbar",
      margin: EdgeInsets.only(top: 2, left: 2, right: 2),
      borderRadius: context.shapes.mediumBorderRadius,
      child: SafeArea(
        bottom: false,
        child: SizedBox(
          height: height,
          child: Material(
            color: color,
            borderRadius: context.shapes.mediumBorderRadius,
            child: Surface(
              color: color,
              child: LayoutBuilder(
                builder: (context, constraints) {
                  final showTrailing =
                      trailing != null && constraints.maxWidth >= 600;
                  return Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: context.spacing.space2,
                    ),
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      spacing: context.spacing.space2,
                      children: [
                        Expanded(
                          child: SingleChildScrollView(
                            scrollDirection: Axis.horizontal,
                            child: Row(
                              spacing: context.spacing.space2,
                              children: leading,
                            ),
                          ),
                        ),
                        const MutationActivityButton(),
                        if (showTrailing) trailing!,
                        if (context.isMobile)
                          IconButton(
                            icon: const Icon(Icons.menu),
                            onPressed: () {
                              showModalBottomSheet(
                                context: context,
                                isScrollControlled: true,
                                backgroundColor: Colors.transparent,
                                builder: (ctx) => UncontrolledProviderScope(
                                  container: ProviderScope.containerOf(context),
                                  child: _MobileSidebarMenu(child: sidebar),
                                ),
                              );
                            },
                          ),
                        if (context.debugShowCheckedModeBanner)
                          const SizedBox(width: 40),
                      ],
                    ),
                  );
                },
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _MobileSidebarMenu extends StatelessWidget {
  const _MobileSidebarMenu({required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return DecoratedBox(
      decoration: BoxDecoration(
        color: Theme.of(context).scaffoldBackgroundColor,
        borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
      ),
      child: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const ModalHeader(),
            Expanded(
              child: Padding(
                padding: EdgeInsets.symmetric(
                  horizontal: context.spacing.space4,
                ),
                child: child,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
