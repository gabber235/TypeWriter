import "package:flutter/foundation.dart";
import "package:flutter_animate/flutter_animate.dart";
import "package:typewriter_panel/typewriter_panel.dart";

/// Applies the result of an action to the owning search controller.
typedef ActionEffectCallback = void Function(SearchActionEffect effect);

/// Owns one active result action and exposes its lifecycle to the UI.
///
/// Submission is rejected while another action is running or when the result
/// selection no longer exists in the supplied snapshot. Completed and failed
/// states reset automatically after their feedback window. Disposal cancels
/// publication from work that finishes later.
class ActionController with ChangeNotifier {
  ActionController({required this._effectCallback});

  final ActionEffectCallback _effectCallback;

  var _disposed = false;

  var _state = SearchActionState.idle();
  SearchActionState get state => _state;

  /// Submits [actionType] for [resultIds] using the actions and result tree in
  /// [snapshot].
  SearchActionSubmitResult execute(
    Type actionType,
    Set<String> resultIds,
    SearchSourceSnapshot snapshot,
  ) {
    if (_state is SearchActionRunning) {
      return SearchActionSubmitResult.busy;
    }

    if (resultIds.isEmpty) {
      return SearchActionSubmitResult.invalidSelection;
    }

    final action = snapshot.actions[actionType];
    if (action == null) {
      return SearchActionSubmitResult.actionNotFound;
    }

    final results = snapshot.nodes.findResults(resultIds);

    if (results.isEmpty) {
      return SearchActionSubmitResult.invalidSelection;
    }

    assert(() {
      final doNotHaveAction = results
          .where((r) => !r.actions.contains(actionType))
          .toList();
      if (doNotHaveAction.isEmpty) return true;

      throw StateError(
        "Result(s) do not have action $actionType: $doNotHaveAction, yet were trying to execute it.",
      );
    }());

    _execute(actionType, action, results);
    return SearchActionSubmitResult.submitted;
  }

  Future<void> _execute(
    Type actionType,
    SearchAction action,
    List<SearchResult> results,
  ) async {
    if (_disposed) {
      return;
    }
    final resultIds = results.map((r) => r.id).toSet();
    _state = SearchActionState.running(
      action: actionType,
      resultIds: resultIds,
    );
    notifyListeners();

    try {
      final SearchActionResult result;
      switch (action) {
        case SingleSearchAction():
          assert(
            results.length == 1,
            "For a SingleSearchAction, exactly one result must be selected.",
          );
          result = await action.execute(results.first);
        case RepeatedSearchAction():
          final actionResults = await Future.wait(
            results.map((r) => action.execute(r)),
          );
          result = actionResults.merge();
        case BatchSearchAction():
          result = await action.executeBatch(results);
        default:
          throw StateError("Unknown action type: $action");
      }

      if (_disposed) {
        return;
      }

      switch (result) {
        case SearchActionResultCompleted():
          _state = SearchActionState.completed(
            action: actionType,
            resultIds: resultIds,
          );
        case SearchActionResultFailed(:final message):
          _state = SearchActionState.failed(
            action: actionType,
            resultIds: resultIds,
            message: message,
          );
      }
      notifyListeners();

      _effectCallback(result.effect);
    } on Error catch (e) {
      debugPrint(e.toString());
      debugPrintStack(stackTrace: e.stackTrace);
      if (_disposed) {
        return;
      }
      _state = SearchActionState.failed(
        action: actionType,
        resultIds: resultIds,
        message: e.toString(),
      );
      notifyListeners();
    }
    await _clearAutoReset();
  }

  Future<void> _clearAutoReset() async {
    if (_disposed) {
      return;
    }

    await Future<void>.delayed(
      _state is SearchActionFailed ? 10.seconds : 3.seconds,
    );
    if (_disposed) {
      return;
    }

    if (_state is SearchActionRunning) {
      return;
    }

    _state = SearchActionState.idle();
    notifyListeners();
  }

  /// Stops late asynchronous completions from notifying listeners.
  @override
  void dispose() {
    assert(!_disposed);
    _disposed = true;
    super.dispose();
  }
}
