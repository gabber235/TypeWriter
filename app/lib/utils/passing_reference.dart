import "package:hooks_riverpod/hooks_riverpod.dart";

/// As riverpod does not have a unified reference type, we need to create one so that we can pass it around to functions that need to read providers.
/// The reference may never be cached, as it may be invalidated at any time.
/// Therefore, we can only use it to read providers, and not listen to them.
class PassingRef {
  const PassingRef({
    this.providerRef,
    this.widgetRef,
    this.container,
  }) : assert(
          providerRef != null || widgetRef != null || container != null,
          "At least one reference must be provided",
        );

  final Ref<dynamic>? providerRef;
  final WidgetRef? widgetRef;
  final ProviderContainer? container;

  T read<T>(ProviderListenable<T> provider) {
    if (providerRef != null) {
      return providerRef!.read(provider);
    } else if (widgetRef != null) {
      return widgetRef!.read(provider);
    } else {
      return container!.read(provider);
    }
  }

  void invalidate(ProviderOrFamily provider) {
    if (providerRef != null) {
      providerRef!.invalidate(provider);
    } else if (widgetRef != null) {
      widgetRef!.invalidate(provider);
    } else {
      container!.invalidate(provider);
    }
  }
}

extension RefX on Ref<dynamic> {
  PassingRef get passing => PassingRef(providerRef: this);
}

extension WidgetRefX on WidgetRef {
  PassingRef get passing => PassingRef(widgetRef: this);
}

extension ProviderContainerX on ProviderContainer {
  PassingRef get passing => PassingRef(container: this);
}
