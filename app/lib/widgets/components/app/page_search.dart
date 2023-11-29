import "package:collection/collection.dart";
import "package:flutter/material.dart" hide Page;
import "package:flutter/services.dart";
import "package:font_awesome_flutter/font_awesome_flutter.dart";
import "package:fuzzy/fuzzy.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/pages/pages_list.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/smart_single_activator.dart";
import "package:typewriter/widgets/components/app/search_bar.dart";

part "page_search.g.dart";

class PageTypeFiler extends SearchFilter {
  const PageTypeFiler(this.type, {this.canRemove = true});

  final PageType type;
  @override
  final bool canRemove;

  @override
  String get title => type.name;
  @override
  Color get color => type.color;
  @override
  IconData get icon => type.icon;

  @override
  bool filter(SearchElement action) {
    if (action is PageSearchElement) {
      return action.page.type == type;
    }
    if (action is AddPageSearchElement) {
      return action.type == type;
    }
    return true;
  }
}

@riverpod
Fuzzy<Page> _fuzzyPages(_FuzzyPagesRef ref) {
  final pages = ref.watch(pagesProvider);
  return Fuzzy(
    pages,
    options: FuzzyOptions(
      threshold: 0.4,
      sortFn: (a, b) => a.matches
          .map((e) => e.score)
          .sum
          .compareTo(b.matches.map((e) => e.score).sum),
      keys: [
        WeightedKey(
          name: "name",
          weight: 0.8,
          getter: (page) => page.pageName.formatted,
        ),
        WeightedKey(
          name: "type",
          weight: 0.2,
          getter: (page) => page.type.name,
        ),
      ],
    ),
  );
}

class PageFetcher extends SearchFetcher {
  const PageFetcher({
    this.onSelect,
    this.disabled = false,
  });

  final FutureOr<bool?> Function(Page)? onSelect;

  @override
  final bool disabled;

  @override
  String get title => "Pages";

  @override
  List<SearchElement> fetch(PassingRef ref) {
    final search = ref.read(searchProvider);
    if (search == null) return [];
    final fuzzy = ref.read(_fuzzyPagesProvider);

    final results = fuzzy.search(search.query);

    return results.map((result) {
      return PageSearchElement(
        result.item,
        onSelect: onSelect,
      );
    }).toList();
  }

  @override
  SearchFetcher copyWith({bool? disabled}) {
    return PageFetcher(
      onSelect: onSelect,
      disabled: disabled ?? this.disabled,
    );
  }
}

class AddPageFetcher extends SearchFetcher {
  AddPageFetcher({this.onAdded, this.disabled = false});

  final void Function(Page)? onAdded;

  @override
  final bool disabled;

  @override
  String get title => "Add Page";

  @override
  List<SearchElement> fetch(PassingRef ref) {
    return PageType.values.map((type) {
      return AddPageSearchElement(type, onAdded: onAdded);
    }).toList();
  }

  @override
  SearchFetcher copyWith({bool? disabled}) {
    return AddPageFetcher(
      onAdded: onAdded,
      disabled: disabled ?? this.disabled,
    );
  }
}

extension SearchBuilderX on SearchBuilder {
  void pageType(PageType type, {bool canRemove = true}) {
    filter(PageTypeFiler(type, canRemove: canRemove));
  }

  void fetchPage({FutureOr<bool?> Function(Page)? onSelect}) {
    fetch(PageFetcher(onSelect: onSelect));
  }

  void fetchAddPage({void Function(Page)? onAdded}) {
    fetch(AddPageFetcher(onAdded: onAdded));
  }
}

class PageSearchElement extends SearchElement {
  const PageSearchElement(this.page, {this.onSelect});

  final Page page;
  final FutureOr<bool?> Function(Page)? onSelect;

  @override
  String get title => page.pageName.formatted;

  @override
  String description(BuildContext context) {
    if (page.chapter.isEmpty) return page.type.name;
    return "~${page.chapter}";
  }

  @override
  Widget icon(BuildContext context) => Icon(page.type.icon);

  @override
  Color color(BuildContext context) => page.type.color;

  @override
  Widget suffixIcon(BuildContext context) => const Icon(Icons.open_in_new);

  @override
  List<SearchAction> actions(PassingRef ref) {
    return [
      const SearchAction(
        "Open",
        Icons.open_in_new,
        SingleActivator(LogicalKeyboardKey.enter),
      ),
      SearchAction(
        "Rename",
        FontAwesomeIcons.pencil,
        SmartSingleActivator(LogicalKeyboardKey.keyR, control: true),
        onTrigger: (context, __) async =>
            await showDialog<bool>(
              context: context,
              builder: (_) => RenamePageDialogue(old: page.pageName),
            ) ??
            false,
      ),
      SearchAction(
        "Change Chapter",
        FontAwesomeIcons.bookBookmark,
        SmartSingleActivator(LogicalKeyboardKey.keyC, control: true),
        onTrigger: (context, __) async =>
            await showDialog<bool>(
              context: context,
              builder: (_) => ChangeChapterDialogue(
                pageId: page.pageName,
                chapter: page.chapter,
              ),
            ) ??
            false,
      ),
      SearchAction(
        "Delete",
        FontAwesomeIcons.trash,
        SmartSingleActivator(LogicalKeyboardKey.backspace, control: true),
        color: Colors.red,
        onTrigger: (context, ref) =>
            showPageDeletionDialogue(context, ref, page.pageName),
      ),
    ];
  }

  @override
  Future<bool> activate(BuildContext context, PassingRef ref) async {
    if (onSelect != null) {
      return await onSelect?.call(page) ?? true;
    }

    final navigator = ref.read(appRouter);

    ref.read(searchProvider.notifier).endSearch();

    await navigator.navigateToPage(ref, page.pageName);
    return false;
  }
}

class AddPageSearchElement extends SearchElement {
  const AddPageSearchElement(this.type, {this.onAdded});

  final PageType type;
  final void Function(Page)? onAdded;

  @override
  String get title => "Add ${type.name}";

  @override
  String description(BuildContext context) => "Create a new ${type.name}";

  @override
  Widget icon(BuildContext context) => Icon(type.icon);

  @override
  Color color(BuildContext context) => type.color;

  @override
  Widget suffixIcon(BuildContext context) => const Icon(Icons.add);

  @override
  List<SearchAction> actions(PassingRef ref) {
    return [
      const SearchAction(
        "Add",
        Icons.add,
        SingleActivator(LogicalKeyboardKey.enter),
      ),
    ];
  }

  @override
  Future<bool> activate(BuildContext context, PassingRef ref) async {
    final pageName = await showDialog<String>(
      context: context,
      builder: (context) =>
          AddPageDialogue(fixedType: type, autoNavigate: false),
    );

    if (pageName == null) return false;
    final page = ref.read(pageProvider(pageName));
    if (page == null) return false;

    if (onAdded != null) {
      onAdded?.call(page);
    }

    await ref.read(appRouter).navigateToPage(ref, pageName);
    return true;
  }
}
