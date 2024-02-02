import "package:flutter/material.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";

class PublishPagesIntent extends Intent {
  const PublishPagesIntent();
}

final stagingStateProvider = StateProvider((ref) => StagingState.production);

enum StagingState {
  publishing("Publishing", Colors.lightBlue),
  staging("Staging", Colors.orange),
  production("Production", Colors.green);

  const StagingState(this.label, this.color);

  final String label;
  final Color color;
}
