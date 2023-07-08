import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";

GlobalKey useGlobalKey() => useMemoized(GlobalKey.new);
