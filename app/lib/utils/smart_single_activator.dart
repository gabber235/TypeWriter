import "package:flutter/foundation.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";

bool get isApple => defaultTargetPlatform == TargetPlatform.iOS || defaultTargetPlatform == TargetPlatform.macOS;

/// If the user is on an apple platform, this will return true if the [meta] key is pressed.
/// Otherwise, it will return true if the [control] key is pressed.
bool get hasOverrideDown =>
    RawKeyboard.instance.keysPressed.contains(LogicalKeyboardKey.controlLeft) && !isApple ||
    RawKeyboard.instance.keysPressed.contains(LogicalKeyboardKey.controlRight) && !isApple ||
    RawKeyboard.instance.keysPressed.contains(LogicalKeyboardKey.metaLeft) && isApple ||
    RawKeyboard.instance.keysPressed.contains(LogicalKeyboardKey.metaRight) && isApple;

/// A [SingleActivator] that automatically maps the [control] key to the [meta] key for apple platforms.
class SmartSingleActivator extends SingleActivator {
  SmartSingleActivator(
    super.trigger, {
    bool control = false,
    super.alt,
    super.shift,
    super.includeRepeats,
  }) : super(control: control && !isApple, meta: control && isApple);
}
