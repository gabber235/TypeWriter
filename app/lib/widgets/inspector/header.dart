import "package:collapsible/collapsible.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/components/app/writers.dart";
import "package:typewriter/widgets/inspector/editors.dart";
import "package:typewriter/widgets/inspector/headers/capture_action.dart";
import "package:typewriter/widgets/inspector/headers/colored_action.dart";
import "package:typewriter/widgets/inspector/headers/entry_selector_action.dart";
import "package:typewriter/widgets/inspector/headers/help_action.dart";
import "package:typewriter/widgets/inspector/headers/length_action.dart";
import "package:typewriter/widgets/inspector/headers/multiline_action.dart";
import "package:typewriter/widgets/inspector/headers/placeholder_action.dart";
import "package:typewriter/widgets/inspector/section_title.dart";

part "header.g.dart";

class FieldHeader extends HookConsumerWidget {
  const FieldHeader({
    required this.child,
    required this.field,
    required this.path,
    this.leading = const [],
    this.trailing = const [],
    this.actions = const [],
    this.canExpand = false,
    this.defaultExpanded = false,
    super.key,
  }) : super();

  final Widget child;
  final FieldInfo field;
  final String path;

  final List<Widget> leading;
  final List<Widget> trailing;
  final List<Widget> actions;
  final bool canExpand;
  final bool defaultExpanded;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final parent = Header.of(context);

    // If there already is a header for this path, we don't need to create a new
    if (parent?.path == path) {
      return child;
    }

    final headerActionFilters = ref.watch(headerActionFiltersProvider);
    final availableActions = headerActionFilters
        .where((filter) => filter.shouldShow(path, field))
        .toList();
    final name =
        ref.watch(pathDisplayNameProvider(path)).nullIfEmpty ?? "Fields";

    final expanded = useState(defaultExpanded);

    return Header(
      path: path,
      expanded: expanded,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Material(
            borderRadius: BorderRadius.circular(4),
            clipBehavior: Clip.none,
            child: InkWell(
              borderRadius: BorderRadius.circular(4),
              onTap: canExpand ? () => expanded.value = !expanded.value : null,
              child: WritersIndicator(
                enabled: canExpand && !expanded.value,
                provider: fieldWritersProvider(path),
                offset: canExpand ? const Offset(50, 25) : const Offset(15, 15),
                child: Row(
                  children: [
                    if (canExpand)
                      Icon(
                        expanded.value ? Icons.expand_less : Icons.expand_more,
                      ),
                    ...createActions(
                      availableActions,
                      HeaderActionLocation.leading,
                    ),
                    SectionTitle(
                      title: name,
                    ),
                    ...createActions(
                      availableActions,
                      HeaderActionLocation.trailing,
                    ),
                    const Spacer(),
                    ...createActions(
                      availableActions,
                      HeaderActionLocation.actions,
                    ),
                  ],
                ),
              ),
            ),
          ),
          if (canExpand)
            Collapsible(
              collapsed: !expanded.value,
              axis: CollapsibleAxis.vertical,
              child: Padding(
                padding: const EdgeInsets.only(left: 8),
                child: child,
              ),
            )
          else
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: child,
            ),
        ],
      ),
    );
  }

  List<Widget> createActions(
    List<HeaderActionFilter> filters,
    HeaderActionLocation location,
  ) {
    final children = [
      if (location == HeaderActionLocation.leading) ...leading,
      if (location == HeaderActionLocation.trailing) ...trailing,
      for (final filter in filters)
        if (filter.location(path, field) == location) filter.build(path, field),
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
    required super.child,
    super.key,
  });

  final String path;
  final ValueNotifier<bool> expanded;

  @override
  bool updateShouldNotify(covariant Header oldWidget) => path != oldWidget.path;

  static Header? of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<Header>();
}

@riverpod
List<HeaderActionFilter> headerActionFilters(HeaderActionFiltersRef ref) => [
      HelpHeaderActionFilter(),
      ColoredHeaderActionFilter(),
      PlaceholderHeaderActionFilter(),
      RegexHeaderActionFilter(),
      LengthHeaderActionFilter(),
      EntrySelectorHeaderActionFilter(),
      CaptureHeaderActionFilter(),
    ];

abstract class HeaderActionFilter {
  bool shouldShow(String path, FieldInfo field);

  HeaderActionLocation location(String path, FieldInfo field);
  Widget build(String path, FieldInfo field);
}

enum HeaderActionLocation {
  leading,
  trailing,
  actions,
}
