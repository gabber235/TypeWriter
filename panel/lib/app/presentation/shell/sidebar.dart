// Defines the desktop sidebar shell and its shared size controls.
//
// The sidebar is a Pane so global directional navigation can move between
// it and route content. Its width is bounded by the viewport and kept in
// sidebarSizeProvider, while the surrounding route decides what content it
// displays.
import "dart:math";

import "package:auto_route/auto_route.dart";
import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:iconify_flutter_plus/icons/material_symbols.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter_panel/typewriter_panel.dart";

part "sidebar_controller.dart";
part "sidebar_links.dart";
part "sidebar_shell.dart";
part "sidebar_state.dart";
part "sidebar_user_menu.dart";
part "sidebar.g.dart";

/// Width change for an ordinary keyboard resize step, in logical pixels.
const double kSidebarResizeSmallStep = 10;

/// Width change for a modified keyboard resize step, in logical pixels.
const double kSidebarResizeLargeStep = 50;

/// Smallest width allowed by the sidebar controller, in logical pixels.
const double kSidebarMinSize = 150;

/// Initial sidebar width, in logical pixels.
const double kSidebarDefaultSize = 220;

/// Fraction of the viewport width available to the sidebar before clamping.
const double kSidebarMaxFactor = 1 / 3;
