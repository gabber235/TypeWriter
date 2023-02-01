import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart" hide ConnectionState;
import "package:flutter_hooks/flutter_hooks.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/communicator.dart";
import "package:typewriter/pages/connect_page.dart";
import "package:typewriter/widgets/select_entries.dart";

class BookPage extends HookConsumerWidget {
  const BookPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final connectionState = ref.watch(connectionStateProvider);

    return AutoTabsRouter(
      routes: const [
        PagesListRoute(),
      ],
      builder: (context, child, animation) {
        return Stack(
          children: [
            Scaffold(
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
            if (connectionState == ConnectionState.disconnected) ...[
              const ModalBarrier(
                dismissible: false,
                color: Colors.black54,
              ),
              const _ReconnectOverlay(),
            ]
          ],
        );
      },
    );
  }
}

class _ReconnectOverlay extends HookConsumerWidget {
  const _ReconnectOverlay({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final controller = useAnimationController(
      duration: const Duration(seconds: 30),
    )..forward();

    useAnimation(controller);

    useDelayedExecution(() {
      // When this widget is mounted, we want to close all the popups
      while (ModalRoute.of(context)?.isCurrent != true) {
        Navigator.of(context).pop();
      }
    });

    return Dialog(
      clipBehavior: Clip.antiAlias,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          SizedBox(
            width: 420,
            child: LinearProgressIndicator(value: 1 - controller.value),
          ),
          Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text("Connection lost, Reconnecting...", style: Theme.of(context).textTheme.titleLarge),
                const SizedBox(height: 8),
                const ConnectionScroller(
                  style: TextStyle(color: Colors.grey, fontSize: 16),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
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
          Flexible(
            // When selecting entries we don't want to go to some other page
            child: SelectingEntriesBlocker(
              child: Column(
                children: [
                  _RailButton(index++, icon: FontAwesomeIcons.filePen),
                  const Spacer(),
                  const _ReloadBookButton(),
                ],
              ),
            ),
          ),
          const SizedBox(height: 10),
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

class _ReloadBookButton extends HookConsumerWidget {
  const _ReloadBookButton({super.key}) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return IconButton(
      icon: const Icon(Icons.refresh, color: Colors.white70),
      onPressed: () => ref.read(bookProvider.notifier).reload(),
    );
  }
}
