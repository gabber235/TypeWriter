import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:rive/rive.dart';
import 'package:typewriter/app_router.dart';
import 'package:typewriter/hooks/route_state.dart';
import 'package:typewriter/main.dart';

class PagesList extends StatelessWidget {
  const PagesList({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox.expand(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: const [
          _PagesSelector(),
          Expanded(child: AutoRouter()),
        ],
      ),
    );
  }
}

class _PagesSelector extends HookConsumerWidget {
  const _PagesSelector({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Container(
      color: const Color(0xFF163260),
      width: 170,
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 12),
            Text("Pages", style: Theme.of(context).textTheme.subtitle1),
            const SizedBox(height: 12),
            const _PageTile(name: "test"),
            const _PageTile(name: "tutorial"),
          ],
        ),
      ),
    );
  }
}

class _PageTile extends HookConsumerWidget {
  final String name;

  const _PageTile({
    Key? key,
    required this.name,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final isSelected = useRouteState(
      () => context.innerRouterOf(PagesListRoute.name),
      (controller) => controller.current.pathParams.getString("id", "") == name,
      defaultValue: false,
    );

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 2.0),
      child: Material(
        color: isSelected ? const Color(0xFF1e3f6f) : Colors.transparent,
        borderRadius: BorderRadius.circular(8),
        child: InkWell(
          onTap: () {
            context.router.push(PageEditorRoute(pageId: name));
          },
          borderRadius: BorderRadius.circular(8),
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    name.formatted,
                    style: Theme.of(context).textTheme.caption?.copyWith(
                          color: Colors.white,
                        ),
                  ),
                ),
                const SizedBox(width: 8),
                const Icon(Icons.chevron_right, size: 16),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class EmptyPageEditor extends HookConsumerWidget {
  const EmptyPageEditor({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      children: const [
        Spacer(),
        Expanded(
          flex: 2,
          child: RiveAnimation.asset(
            "assets/cute_robot.riv",
            stateMachines: ["Motion"],
          ),
        ),
        Text("Select a page to edit",
            style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold)),
        Spacer(),
      ],
    );
  }
}
