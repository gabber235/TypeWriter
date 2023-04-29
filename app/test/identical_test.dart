import "package:flutter_test/flutter_test.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/segment.dart";

void main() {
  test("Test if two strings are identical", () {
    final s = uuid.v4();
    final string1 = "$s-1";
    final string2 = "$s-1";

    expect(identical(string1, string2), true);
  });

  final testProvider = StateProvider((ref) => const Segment());
  test("If the same freezed object is set for a provider expect it to update only once", () {
    final s = uuid.v4();
    final container = ProviderContainer();
    addTearDown(container.dispose);

    var count = 0;

    container.listen(testProvider, fireImmediately: false, (old, value) => count++);
    expect(count, 0);

    final obj1 = Segment(path: "$s-1");
    final obj2 = Segment(path: "$s-1");

    assert(obj1 == obj2, true);

    container.read(testProvider.notifier).state = obj1;
    expect(count, 1);

    container.read(testProvider.notifier).state = obj2;
    expect(count, 1); // Error: Expected: <1> Actual: <2>
  });
}
