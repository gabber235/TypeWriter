import "package:flutter/material.dart";
import "package:typewriter/widgets/components/general/iconify.dart";

class HeaderButton extends StatelessWidget {
  const HeaderButton({
    required this.icon,
    required this.onTap,
    this.tooltip,
    this.color = Colors.blue,
    super.key,
  });
  final String icon;
  final VoidCallback onTap;
  final String? tooltip;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Tooltip(
      message: tooltip ?? "",
      child: Material(
        borderRadius: BorderRadius.circular(4),
        color: color,
        child: InkWell(
          borderRadius: const BorderRadius.all(Radius.circular(4)),
          onTap: onTap,
          child: Padding(
            padding: const EdgeInsets.all(6),
            child: Iconify(
              icon,
              size: 16,
              color: Colors.white,
            ),
          ),
        ),
      ),
    );
  }
}
