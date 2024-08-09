import "package:flutter/material.dart";
import "package:typewriter/widgets/components/general/iconify.dart";

class Admonition extends StatelessWidget {
  const Admonition({
    required this.color,
    required this.icon,
    required this.child,
    this.onTap,
    super.key,
  });

  const Admonition.info({
    required this.child,
    this.onTap,
    super.key,
  })  : color = Colors.blue,
        icon = "material-symbols:info-rounded";

  const Admonition.warning({
    required this.child,
    this.onTap,
    super.key,
  })  : color = Colors.orange,
        icon = "iconoir:warning-triangle-solid";

  const Admonition.danger({
    required this.child,
    this.onTap,
    super.key,
  })  : color = Colors.red,
        icon = "iconoir:warning-triangle-solid";

  final Color color;
  final String icon;
  final Widget child;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return Material(
      color: color.withOpacity(0.1),
      shape: RoundedRectangleBorder(
        side: BorderSide(
          color: color,
          width: 1,
        ),
        borderRadius: BorderRadius.circular(8),
      ),
      child: InkWell(
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 10),
          child: Row(
            children: [
              Iconify(
                icon,
                color: color,
              ),
              const SizedBox(width: 12),
              Flexible(
                child: DefaultTextStyle(
                  style: TextStyle(
                    color: color,
                    fontVariations: const [FontVariation.width(1.2)],
                  ),
                  child: child,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
