part of "mutation_activity_button.dart";

class _ActivityCard extends StatelessWidget {
  const _ActivityCard({
    required this.title,
    required this.message,
    required this.phase,
    required this.actions,
  });
  final String title;
  final String message;
  final MutationActivityPhase phase;
  final List<Widget> actions;

  @override
  Widget build(BuildContext context) {
    final color = phase.color(context);
    return Padding(
      padding: EdgeInsets.only(top: context.spacing.space2),
      child: DecoratedBox(
        decoration: BoxDecoration(
          color: color.withValues(alpha: .05),
          borderRadius: context.shapes.mediumBorderRadius,
          border: Border.all(color: color.withValues(alpha: .2)),
        ),
        child: Padding(
          padding: EdgeInsets.all(context.spacing.space3),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            mainAxisSize: MainAxisSize.min,
            children: [
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  DecoratedBox(
                    decoration: BoxDecoration(
                      color: color.withValues(alpha: .12),
                      borderRadius: context.shapes.smallBorderRadius,
                    ),
                    child: Padding(
                      padding: EdgeInsets.all(context.spacing.space2),
                      child: Icon(phase.icon, color: color, size: 18),
                    ),
                  ),
                  SizedBox(width: context.spacing.space3),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(title, style: context.theme.textTheme.titleSmall),
                        SizedBox(height: context.spacing.space1),
                        Text(
                          message,
                          style: context.theme.textTheme.bodySmall?.copyWith(
                            color: context.colors.contentSecondary,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              if (actions.isNotEmpty) ...[
                SizedBox(height: context.spacing.space2),
                Wrap(
                  alignment: WrapAlignment.end,
                  spacing: context.spacing.space1,
                  children: actions,
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}
