import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';
import 'package:typewriter/pages/book_page.dart';
import 'package:typewriter/pages/home_page.dart';
import 'package:typewriter/pages/page_editor.dart';
import 'package:typewriter/pages/pages_list.dart';

part 'app_router.gr.dart';

@CustomAutoRouter(
  replaceInRouteName: "Page,Route",
  routes: [
    AutoRoute(page: HomePage, initial: true),
    AutoRoute(
      path: "/book",
      page: BookPage,
      children: [
        AutoRoute(
          path: "pages",
          page: PagesList,
          name: "PagesListRoute",
          initial: true,
          children: [
            AutoRoute(
              path: "",
              page: EmptyPageEditor,
              name: "EmptyPageEditorRoute",
              initial: true,
            ),
            AutoRoute(
              path: ":id",
              page: PageEditor,
              name: "PageEditorRoute",
            ),
          ],
        ),
      ],
    ),
  ],
  transitionsBuilder: slideLeftWithFade,
)
class AppRouter extends _$AppRouter {}

Widget slideLeftWithFade(BuildContext context, Animation<double> animation,
    Animation<double> secondaryAnimation, Widget child) {
  return SlideTransition(
    position: Tween<Offset>(
      begin: const Offset(.5, 0.0),
      end: Offset.zero,
    ).animate(animation),
    child: FadeTransition(opacity: animation, child: child),
  );
}
