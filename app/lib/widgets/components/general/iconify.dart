import "package:flutter/material.dart";
import "package:flutter_svg/flutter_svg.dart";

class Iconify extends StatelessWidget {
  const Iconify(
    this.icon, {
    this.size,
    this.color,
    super.key,
  });

  final String? icon;
  final double? size;
  final Color? color;

  String _url(String icon, double size) {
    final split = icon.split(":");
    if (split.length != 2) {
      throw Exception("Invalid icon name: $icon");
    }
    final collection = split[0];
    final name = split[1];
    return "https://api.iconify.design/$collection/$name.svg?height=$size";
  }

  @override
  Widget build(BuildContext context) {
    final icon = this.icon;
    final theme = IconTheme.of(context);
    final size = this.size ?? theme.size ?? 12;
    final color = this.color ?? theme.color ?? Colors.black;

    if (icon == null) return SizedBox(width: size, height: size);

    return SizedBox(
      width: size,
      height: size,
      child: SvgPicture.network(
        _url(icon, size),
        width: size,
        height: size,
        theme: SvgTheme(currentColor: color),
        placeholderBuilder: (context) =>
            Icon(Icons.question_mark, size: size, color: color),
      ),
    );
  }
}
