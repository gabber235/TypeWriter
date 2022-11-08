import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:typewriter/app_router.dart';

void main() {
  runApp(const ProviderScope(child: TypeWriterApp()));
}

final appRouter = Provider<AppRouter>((ref) => AppRouter());

class TypeWriterApp extends HookConsumerWidget {
  const TypeWriterApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final router = ref.watch(appRouter);
    return MaterialApp.router(
      title: 'TypeWriter',
      theme: _buildTheme(Brightness.light),
      darkTheme: _buildTheme(Brightness.dark),
      debugShowCheckedModeBanner: false,
      routerDelegate: router.delegate(),
      routeInformationParser: router.defaultRouteParser(),
    );
  }

  ThemeData _buildTheme(Brightness brightness) {
    var baseTheme = ThemeData(brightness: brightness);

    return baseTheme.copyWith(
      textTheme: GoogleFonts.jetBrainsMonoTextTheme(baseTheme.textTheme),
      inputDecorationTheme: InputDecorationTheme(
        contentPadding: const EdgeInsets.only(left: 8),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: BorderSide.none,
        ),
        fillColor: brightness == Brightness.light
            ? Colors.black.withOpacity(0.05)
            : Colors.white.withOpacity(0.05),
        filled: true,
      ),
    );
  }
}

extension BuildContextExtension on BuildContext {
  bool get isDark => Theme.of(this).brightness == Brightness.dark;
}

extension StringExtension on String {
  String get capitalize {
    if (isEmpty) {
      return this;
    }
    return "${this[0].toUpperCase()}${substring(1)}";
  }

  String get formatted {
    if (isEmpty) {
      return this;
    }
    return split(".")
        .map((e) => e.capitalize)
        .join(" | ")
        .split("_")
        .map((e) => e.capitalize)
        .join(" ");
  }
}

extension ObjectExtension on Object? {
  T? cast<T>() {
    return this is T ? this as T : null;
  }
}
