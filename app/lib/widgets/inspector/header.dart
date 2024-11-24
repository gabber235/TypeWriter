import "package:collapsible/collapsible.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/hooks/delayed_execution.dart";
import "package:typewriter/models/entry_blueprint.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/headers/colored_action.dart";
import "package:typewriter/widgets/inspector/headers/content_mode_action.dart";
import "package:typewriter/widgets/inspector/headers/help_action.dart";
import "package:typewriter/widgets/inspector/headers/length_action.dart";
import "package:typewriter/widgets/inspector/headers/placeholder_action.dart";
import "package:typewriter/widgets/inspector/headers/regex_action.dart";
import "package:typewriter/widgets/inspector/headers/variable_action.dart";
import "package:typewriter/widgets/inspector/section_title.dart";

part "header.g.dart";

class FieldHeader extends StatefulHookConsumerWidget {
  const FieldHeader({
    required this.child,
    required this.dataBlueprint,
    required this.path,
    this.leading = const [],
    this.trailing = const [],
    this.actions = const [],
    this.canExpand = false,
    this.defaultExpanded = false,
    super.key,
  }) : super();

  final Widget child;
  final DataBlueprint dataBlueprint;
  final String path;

  final List<Widget> leading;
  final List<Widget> trailing;
  final List<Widget> actions;
  final bool canExpand;
  final bool defaultExpanded;

  @override
  ConsumerState<FieldHeader> createState() => _FieldHeaderState();
}

class _FieldHeaderState extends ConsumerState<FieldHeader> {
  late HeaderActions combinedActions;

  @override
  void initState() {
    combinedActions = HeaderActions(
      leading: widget.leading,
      trailing: widget.trailing,
      actions: widget.actions,
    );
    super.initState();
  }

  @override
  void didUpdateWidget(covariant FieldHeader oldWidget) {
    super.didUpdateWidget(oldWidget);

    if (widget.leading != oldWidget.leading ||
        widget.trailing != oldWidget.trailing ||
        widget.actions != oldWidget.actions) {
      combinedActions = HeaderActions(
        leading: widget.leading,
        trailing: widget.trailing,
        actions: widget.actions,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final parent = Header.maybeOf(context);

    final headerActionFilters = ref.watch(headerActionFiltersProvider);
    final availableActions = headerActionFilters
        .where((filter) => filter.shouldShow(widget.path, widget.dataBlueprint))
        .toList();

    // If there already is a header for this path, we don't need to create a new
    if (parent?.path == widget.path) {
      useDelayedExecution(() {
        parent?.combineActions(
          HeaderActions(
            leading: widget.leading +
                filterActions(availableActions, HeaderActionLocation.leading),
            trailing: widget.trailing +
                filterActions(availableActions, HeaderActionLocation.trailing),
            actions: widget.actions +
                filterActions(availableActions, HeaderActionLocation.actions),
          ),
        );
      });
      return widget.child;
    }

    final name =
        ref.watch(pathDisplayNameProvider(widget.path)).nullIfEmpty ?? "Fields";

    final expanded = useState(widget.defaultExpanded);
    final depth = (parent?.depth ?? -1) + 1;

    return Header(
      key: ValueKey(widget.path),
      path: widget.path,
      expanded: expanded,
      canExpand: widget.canExpand,
      depth: depth,
      combineActions: (actions) => setState(() {
        combinedActions = combinedActions.merge(actions);
      }),
      child: Material(
        color: widget.canExpand
            ? depth.isEven
                ? Theme.of(context).colorScheme.surface
                : Theme.of(context).colorScheme.surfaceContainer
            : Colors.transparent,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(6),
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Material(
              borderRadius: BorderRadius.circular(4),
              clipBehavior: Clip.none,
              color: Colors.transparent,
              child: InkWell(
                borderRadius: BorderRadius.circular(4),
                onTap: widget.canExpand
                    ? () => expanded.value = !expanded.value
                    : null,
                child: WritersIndicator(
                  enabled: widget.canExpand && !expanded.value,
                  provider: fieldWritersProvider(widget.path),
                  offset: widget.canExpand
                      ? const Offset(50, 25)
                      : const Offset(15, 15),
                  child: Row(
                    children: [
                      if (widget.canExpand)
                        Icon(
                          expanded.value
                              ? Icons.expand_less
                              : Icons.expand_more,
                        ),
                      ...createActions(
                        availableActions,
                        HeaderActionLocation.leading,
                        combinedActions.leading,
                      ),
                      Padding(
                        padding: EdgeInsets.symmetric(
                          vertical: widget.canExpand ? 10 : 0,
                        ),
                        child: SectionTitle(
                          title: name,
                        ),
                      ),
                      ...createActions(
                        availableActions,
                        HeaderActionLocation.trailing,
                        combinedActions.trailing,
                      ),
                      const Spacer(),
                      ...createActions(
                        availableActions,
                        HeaderActionLocation.actions,
                        combinedActions.actions,
                      ),
                    ],
                  ),
                ),
              ),
            ),
            if (widget.canExpand)
              Collapsible(
                collapsed: !expanded.value,
                axis: CollapsibleAxis.vertical,
                child: Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 6, vertical: 3),
                  child: widget.child,
                ),
              )
            else
              widget.child,
          ],
        ),
      ),
    );
  }

  List<Widget> filterActions(
    List<HeaderActionFilter> filters,
    HeaderActionLocation location,
  ) {
    return filters
        .where(
          (filter) =>
              filter.location(widget.path, widget.dataBlueprint) == location,
        )
        .map((filter) => filter.build(widget.path, widget.dataBlueprint))
        .toList();
  }

  List<Widget> createActions(
    List<HeaderActionFilter> filters,
    HeaderActionLocation location,
    List<Widget> actions,
  ) {
    final children = [
      if (location == HeaderActionLocation.leading) ...actions,
      if (location == HeaderActionLocation.trailing) ...actions,
      for (final filter in filters)
        if (filter.location(widget.path, widget.dataBlueprint) == location)
          filter.build(widget.path, widget.dataBlueprint),
      if (location == HeaderActionLocation.actions) ...actions,
    ].joinWith(() => const SizedBox(width: 8));

    if (children.isEmpty) return children;

    return [
      if (location == HeaderActionLocation.leading ||
          location == HeaderActionLocation.trailing)
        const SizedBox(width: 8),
      ...children,
      if (location == HeaderActionLocation.leading) const SizedBox(width: 8),
    ];
  }
}

class Header extends InheritedWidget {
  const Header({
    required this.path,
    required this.expanded,
    required this.canExpand,
    required super.child,
    required this.depth,
    required this.combineActions,
    super.key,
  });

  final String path;
  final ValueNotifier<bool> expanded;
  final bool canExpand;
  final int depth;
  final void Function(HeaderActions) combineActions;

  @override
  bool updateShouldNotify(covariant Header oldWidget) => path != oldWidget.path;

  static Header? maybeOf(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<Header>();
}

class HeaderActions {
  const HeaderActions({
    this.leading = const [],
    this.trailing = const [],
    this.actions = const [],
  });

  final List<Widget> leading;
  final List<Widget> trailing;
  final List<Widget> actions;

  HeaderActions merge(HeaderActions other) {
    return HeaderActions(
      leading: [...leading, ...other.leading],
      trailing: [...trailing, ...other.trailing],
      actions: [...actions, ...other.actions],
    );
  }
}

@riverpod
List<HeaderActionFilter> headerActionFilters(Ref ref) => [
      HelpHeaderActionFilter(),
      ColoredHeaderActionFilter(),
      PlaceholderHeaderActionFilter(),
      RegexHeaderActionFilter(),
      LengthHeaderActionFilter(),
      ContentModeHeaderActionFilter(),
      VariableHeaderActionFilter(),
    ];

abstract class HeaderActionFilter {
  bool shouldShow(String path, DataBlueprint dataBlueprint);

  HeaderActionLocation location(String path, DataBlueprint dataBlueprint);
  Widget build(String path, DataBlueprint dataBlueprint);
}

enum HeaderActionLocation {
  leading,
  trailing,
  actions,
}
