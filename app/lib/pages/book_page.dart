import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/app_router.dart";

class BookPage extends HookConsumerWidget {
  const BookPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) => AutoTabsRouter(
        routes: const [
          PagesListRoute(),
        ],
        builder: (context, child, animation) => Scaffold(
          body: Row(
            children: [
              const _SideRail(),
              Expanded(
                child: FadeTransition(
                  opacity: animation,
                  child: child,
                ),
              ),
            ],
          ),
        ),
      );
}

class _SideRail extends HookConsumerWidget {
  const _SideRail();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    var index = 0;
    return Container(
      width: 60,
      color: const Color(0xFF11274f),
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(13.0),
            child: Image.asset("assets/typewriter-icon.png"),
          ),
          const SizedBox(height: 10),
          _RailButton(index++, icon: FontAwesomeIcons.filePen),
        ],
      ),
    );
  }
}

class _RailButton extends HookConsumerWidget {
  const _RailButton(
    this.index, {
    required this.icon,
  });
  final int index;
  final IconData icon;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final tabRouter = context.tabsRouter;
    final isSelected = tabRouter.activeIndex == index;
    return Material(
      color: isSelected ? Colors.white.withOpacity(0.15) : Colors.transparent,
      borderRadius: BorderRadius.circular(8),
      child: InkWell(
        onTap: () => tabRouter.setActiveIndex(index),
        borderRadius: BorderRadius.circular(8),
        child: Padding(
          padding: const EdgeInsets.all(12.0),
          child: Icon(
            icon,
            color: isSelected ? Colors.white : Colors.white54,
            size: 20,
          ),
        ),
      ),
    );
  }
}
