import "package:hooks_riverpod/hooks_riverpod.dart";

class PassingRef {
  const PassingRef({
    this.providerRef,
    this.widgetRef,
  }) : assert(providerRef != null || widgetRef != null);

  final Ref<dynamic>? providerRef;
  final WidgetRef? widgetRef;

  T read<T>(ProviderListenable<T> provider) {
    if (providerRef != null) {
      return providerRef!.read(provider);
    } else {
      return widgetRef!.read(provider);
    }
  }
}

extension RefX on Ref<dynamic> {
  PassingRef get passing => PassingRef(providerRef: this);
}

extension WidgetRefX on WidgetRef {
  PassingRef get passing => PassingRef(widgetRef: this);
}
