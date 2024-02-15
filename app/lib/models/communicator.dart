import "dart:async";
import "dart:convert";

import "package:flutter/material.dart" hide Page;
import "package:flutter_animate/flutter_animate.dart" hide Adapter;
import "package:freezed_annotation/freezed_annotation.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:socket_io_client/socket_io_client.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/entry.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/models/staging.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/passing_reference.dart";
import "package:typewriter/utils/socket_extensions.dart";
import "package:typewriter/widgets/components/general/toasts.dart";

part "communicator.g.dart";
part "communicator.freezed.dart";

final uuidRegex =
    RegExp(r"^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$");

enum ConnectionState {
  none,
  connecting,
  connected,
  disconnected,
}

final connectionStateProvider =
    StateProvider<ConnectionState>((ref) => ConnectionState.none);

final socketProvider = StateNotifierProvider<SocketNotifier, Socket?>(
  SocketNotifier.new,
  name: "socketProvider",
);

class SocketNotifier extends StateNotifier<Socket?> {
  SocketNotifier(this.ref) : super(null);

  final StateNotifierProviderRef<SocketNotifier, Socket?> ref;
  bool _disposed = false;

  /// When a socket gets disconnected, we want to try to reconnect it.
  /// Socket.io will try to reconnect automatically.
  /// Only if this fails within a certain time, we want consider the connection lost.
  Timer? _timeoutTimer;

  void _startTimeoutTimer(Socket socket) {
    final currentConnectionState = _connectionState;
    _timeoutTimer = Timer(30.seconds, () {
      if (_disposed) return;
      if (_connectionState != currentConnectionState) return;
      _connectionState = ConnectionState.none;
      state?.dispose();
      state?.destroy();
      state = null;
      socket
        ..dispose()
        ..destroy();
      ref.read(appRouter).replaceAll([
        const ErrorConnectRoute(),
      ]);
    });
  }

  void _handleError(String message) {
    if (_disposed) {
      debugPrint(
        "The socket was disposed so a connection error should not be possible. This is a bug.",
      );
      return;
    }
    if (_canError) return;
    _connectionState = ConnectionState.none;
    debugPrint(message);
    state?.dispose();
    state = null;

    ref.read(appRouter).replaceAll(
      [const ErrorConnectRoute()],
    );
  }

  ConnectionState get _connectionState {
    return ref.read(connectionStateProvider);
  }

  set _connectionState(ConnectionState state) {
    if (_disposed) return;
    ref.read(connectionStateProvider.notifier).state = state;
  }

  bool get _canError {
    final state = _connectionState;
    return state == ConnectionState.connecting ||
        state == ConnectionState.connected;
  }

  void init(String hostname, int port, [String? token]) {
    if (state != null) return;
    if (_connectionState != ConnectionState.none) return;
    _connectionState = ConnectionState.connecting;

    // If a token is provided but is not a valid UUID, we will not connect for security reasons.
    if (token != null && !uuidRegex.hasMatch(token)) {
      _connectionState = ConnectionState.none;
      ref.read(appRouter).replaceAll([const HomeRoute()]);
      return;
    }

    var url = "http://$hostname:$port";
    if (token != null) url += "?token=$token";

    debugPrint("Initializing socket");
    final socket = io(
      url,
      OptionBuilder().setTransports(["websocket"]).disableAutoConnect().build(),
    );

    socket
      ..onConnect((data) {
        if (_disposed) {
          debugPrint(
            "The socket was disposed so a connect should not be possible. This is a bug.",
          );
          return;
        }
        debugPrint("connected: $data");
        _timeoutTimer?.cancel();
        state = socket;
        final shouldSetup = _connectionState == ConnectionState.connecting;
        _connectionState = ConnectionState.connected;
        if (shouldSetup) setup(socket);
      })
      ..onConnectError((data) {
        _handleError("connect error $data");
      })
      ..onConnectTimeout((data) {
        _handleError("connect timeout $data");
      })
      ..onError((data) {
        _handleError("error $data");
      })
      ..onDisconnect((data) {
        if (_disposed) {
          debugPrint(
            "The socket was disposed so a disconnect should not be possible. This is a bug.",
          );
          return;
        }
        if (_connectionState != ConnectionState.connected) return;
        _connectionState = ConnectionState.disconnected;
        debugPrint("disconnected: $data");
        _startTimeoutTimer(socket);
      })
      ..connect();

    _startTimeoutTimer(socket);
  }

  Future<void> setup(Socket socket) async {
    socket
      ..on(
        "stagingState",
        (data) => ref.read(communicatorProvider).handleStagingState(data),
      )
      ..on(
        "createPage",
        (data) => ref.read(communicatorProvider).handleCreatePage(data),
      )
      ..on(
        "renamePage",
        (data) => ref.read(communicatorProvider).handleRenamePage(data),
      )
      ..on(
        "deletePage",
        (data) => ref.read(communicatorProvider).handleDeletePage(data),
      )
      ..on(
        "moveEntry",
        (data) => ref.read(communicatorProvider).handleMoveEntry(data),
      )
      ..on(
        "createEntry",
        (data) => ref.read(communicatorProvider).handleCreateEntry(data),
      )
      ..on(
        "updateEntry",
        (data) => ref.read(communicatorProvider).handleUpdateEntry(data),
      )
      ..on(
        "updateCompleteEntry",
        (data) =>
            ref.read(communicatorProvider).handleUpdateCompleteEntry(data),
      )
      ..on(
        "reorderEntry",
        (data) => ref.read(communicatorProvider).handleReorderEntry(data),
      )
      ..on(
        "deleteEntry",
        (data) => ref.read(communicatorProvider).handleDeleteEntry(data),
      )
      ..on(
        "updateWriters",
        (data) => ref.read(communicatorProvider).handleUpdateWriters(data),
      );

    await ref.read(communicatorProvider).fetchBook();
    await ref.read(appRouter).push(const BookRoute());
  }

  @override
  void dispose() {
    debugPrint("Disposing socket");
    _disposed = true;
    _timeoutTimer?.cancel();
    state?.dispose();
    state?.destroy();
    super.dispose();
  }
}

@Riverpod(keepAlive: true)
Communicator communicator(CommunicatorRef ref) => Communicator(ref);

@immutable
class Communicator {
  const Communicator(this.ref);

  final CommunicatorRef ref;

  Future<void> fetchBook() async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final String rawPages = await socket.emitWithAckAsync("fetch", "pages");
    final String rawAdapters =
        await socket.emitWithAckAsync("fetch", "adapters");

    final jsonPages = jsonDecode(rawPages) as List;
    final jsonAdapters = jsonDecode(rawAdapters) as List;

    // ignore: unnecessary_lambdas
    final pages = jsonPages.map((p) => Page.fromJson(p)).toList();
    // ignore: unnecessary_lambdas
    final adapters = jsonAdapters.map((a) => Adapter.fromJson(a)).toList();

    final book = Book(name: "Typewriter", adapters: adapters, pages: pages);
    ref.read(bookProvider.notifier).book = book;
  }

  Future<void> createPage(Page page) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    handleAck(
      await socket.emitWithAckAsync("createPage", jsonEncode(page.toJson())),
    );
  }

  Future<void> renamePage(String old, String newName) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "old": old,
      "new": newName,
    };

    await socket.emitWithAckAsync("renamePage", jsonEncode(data));
  }

  Future<void> changePageValue(String page, String path, dynamic value) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "pageId": page,
      "path": path,
      "value": value,
    };

    handleAck(
      await socket.emitWithAckAsync("changePageValue", jsonEncode(data)),
    );
  }

  Future<void> deletePage(String name) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    handleAck(
      await socket.emitWithAckAsync("deletePage", name),
    );
  }

  Future<void> moveEntry(String entryId, String fromPage, String toPage) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "entryId": entryId,
      "fromPage": fromPage,
      "toPage": toPage,
    };

    handleAck(
      await socket.emitWithAckAsync("moveEntry", jsonEncode(data)),
    );
  }

  Future<void> createEntry(String page, Entry entry) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "pageId": page,
      "entry": entry.toJson(),
    };

    handleAck(
      await socket.emitWithAckAsync("createEntry", jsonEncode(data)),
    );
  }

  Future<void> updateEntry(
    String pageId,
    String entryId,
    String path,
    dynamic value,
  ) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "pageId": pageId,
      "entryId": entryId,
      "path": path,
      "value": value,
    };

    handleAck(
      await socket.emitWithAckAsync("updateEntry", jsonEncode(data)),
    );
  }

  Future<void> updateEntireEntry(String pageId, Entry entry) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "pageId": pageId,
      "entry": entry.toJson(),
    };

    handleAck(
      await socket.emitWithAckAsync("updateCompleteEntry", jsonEncode(data)),
    );
  }

  Future<void> reorderEntry(String pageId, String entryId, int newIndex) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "pageId": pageId,
      "entryId": entryId,
      "newIndex": newIndex,
    };

    handleAck(
      await socket.emitWithAckAsync("reorderEntry", jsonEncode(data)),
    );
  }

  Future<void> deleteEntry(String pageId, String entryId) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    final data = {
      "pageId": pageId,
      "entryId": entryId,
    };

    handleAck(
      await socket.emitWithAckAsync("deleteEntry", jsonEncode(data)),
    );
  }

  Future<void> publish() async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }
    if (ref.read(stagingStateProvider) != StagingState.staging) return;

    handleAck(
      await socket.emitWithAckAsync("publish", ""),
    );
  }

  Future<void> updateSelfWriter(Map<String, dynamic> data) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    handleAck(
      await socket.emitWithAckAsync("updateSelfWriter", jsonEncode(data)),
    );
  }

  Future<Response> requestCapture(Map<String, dynamic> data) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return const Response(
        success: false,
        message: "Socket not connected",
      );
    }

    final response = await socket.emitWithAckAsync(
      "captureRequest",
      jsonEncode(data),
    ) as String?;

    if (response == null) {
      return const Response(
        success: false,
        message: "No response from server",
      );
    }

    final json = jsonDecode(response) as Map<String, dynamic>;
    return Response.fromJson(json);
  }

  void handleStagingState(dynamic data) {
    final rawStaging = data as String;
    final state = StagingState.values.firstWhere(
      (state) => state.name == rawStaging,
      orElse: () => StagingState.production,
    );
    ref.read(stagingStateProvider.notifier).state = state;
  }

  void handleCreatePage(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final page = Page.fromJson(json);
    ref.read(bookProvider.notifier).insertPage(page);
  }

  void handleRenamePage(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final old = json["old"] as String;
    final newName = json["new"] as String;
    ref.read(bookProvider.notifier).syncRenamePage(old, newName);
  }

  void handleDeletePage(dynamic data) {
    final name = data as String;
    ref.read(bookProvider.notifier).syncDeletePage(name);
  }

  void handleMoveEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final entryId = json["entryId"] as String;
    final fromPage = json["fromPage"] as String;
    final toPage = json["toPage"] as String;
    ref.read(bookProvider.notifier).syncMoveEntry(entryId, fromPage, toPage);
  }

  void handleCreateEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final pageId = json["pageId"] as String;
    final entry = Entry.fromJson(json["entry"] as Map<String, dynamic>);
    final page = ref.read(pageProvider(pageId));
    if (page == null) return;
    page.syncInsertEntry(ref.passing, entry);
  }

  void handleUpdateEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final pageId = json["pageId"] as String;
    final entryId = json["entryId"] as String;
    final path = json["path"] as String;
    final value = json["value"];
    final entry = ref.read(entryProvider(pageId, entryId));
    if (entry == null) return;
    final newEntry = entry.copyWith(path, value);
    final page = ref.read(pageProvider(pageId));
    if (page == null) return;
    page.syncInsertEntry(ref.passing, newEntry);
  }

  void handleUpdateCompleteEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final pageId = json["pageId"] as String;
    final entry = Entry.fromJson(json["entry"] as Map<String, dynamic>);
    final page = ref.read(pageProvider(pageId));
    if (page == null) return;
    page.syncInsertEntry(ref.passing, entry);
  }

  void handleReorderEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final pageId = json["pageId"] as String;
    final entryId = json["entryId"] as String;
    final newIndex = json["newIndex"] as int;
    final page = ref.read(pageProvider(pageId));
    if (page == null) return;
    page.syncReorderEntry(ref.passing, entryId, newIndex);
  }

  void handleDeleteEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final pageId = json["pageId"] as String;
    final entryId = json["entryId"] as String;
    final page = ref.read(pageProvider(pageId));
    if (page == null) return;
    page.syncDeleteEntry(ref.passing, entryId);
  }

  void handleUpdateWriters(dynamic data) {
    ref.read(writersProvider.notifier).syncWriters(data);
  }

  void handleAck(dynamic data) {
    if (data == null) {
      return;
    }
    if (data is! String) {
      debugPrint("Could not parse ack: $data");
      return;
    }

    final json = jsonDecode(data) as Map<String, dynamic>;
    final response = Response.fromJson(json);

    if (!response.success) {
      debugPrint("Ack failed: ${response.message}");
      Toasts.showError(
        ref.passing,
        response.message,
        description: "Reloading the full book to resync with the server.",
      );
      fetchBook();
      return;
    }

    debugPrint("Ack: ${response.message}");
  }
}

@freezed
class Response with _$Response {
  const factory Response({
    required bool success,
    required String message,
  }) = _Response;

  factory Response.fromJson(Map<String, dynamic> json) =>
      _$ResponseFromJson(json);
}
