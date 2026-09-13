import "package:flutter_test/flutter_test.dart";
import "package:typewriter_panel/typewriter_panel.dart";

void main() {
  test(
    "atomic resource reservation blocks overlaps but permits disjoint work",
    () async {
      final coordinator = MutationCoordinator();
      addTearDown(coordinator.dispose);
      final first = await coordinator.reserve({"a", "b"});
      var overlappingStarted = false;
      final overlapping = coordinator.reserve({"b", "c"}).then((reservation) {
        overlappingStarted = true;
        return reservation;
      });
      final disjoint = await coordinator.reserve({"d"});

      expect(overlappingStarted, isFalse);
      disjoint.release();
      expect(overlappingStarted, isFalse);
      first.release();
      final second = await overlapping;
      expect(overlappingStarted, isTrue);

      second.release();
    },
  );

  test(
    "session disposal prevents queued submissions from dispatching",
    () async {
      final coordinator = MutationCoordinator();
      final active = await coordinator.reserve({"a"});
      final blocked = coordinator.reserve({"a"});
      final failure = expectLater(blocked, throwsStateError);
      coordinator.dispose();
      await failure;

      active.release();
      await expectLater(coordinator.reserve({"b"}), throwsStateError);
    },
  );
}
