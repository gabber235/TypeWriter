import "dart:convert";

import "package:flutter/material.dart" hide Page;
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:socket_io_client/socket_io_client.dart";
import "package:typewriter/app_router.dart";
import "package:typewriter/main.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/models/book.dart";
import "package:typewriter/models/page.dart";
import "package:typewriter/models/staging.dart";
import "package:typewriter/models/writers.dart";
import "package:typewriter/utils/socket_extensions.dart";

part "communicator.g.dart";

final uuidRegex = RegExp(r"^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$");

enum ConnectionState {
  none,
  connecting,
  connected,
}

final socketProvider = StateNotifierProvider<SocketNotifier, Socket?>(SocketNotifier.new);

class SocketNotifier extends StateNotifier<Socket?> {
  SocketNotifier(this.ref) : super(null);

  final StateNotifierProviderRef<SocketNotifier, Socket?> ref;

  bool get isConnected {
    final state = this.state;
    if (state == null) return false;
    return !state.disconnected && state.connected;
  }

  bool get isDisconnected {
    final state = this.state;
    if (state == null) return true;
    return state.disconnected;
  }

  ConnectionState _connectionState = ConnectionState.none;

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
      OptionBuilder().setTransports(["websocket"]).disableAutoConnect().disableReconnection().enableForceNew().build(),
    );

    socket
      ..onConnect((_) {
        debugPrint("connected");
        state = socket;
        _connectionState = ConnectionState.connected;
        setup(socket);
      })
      ..onConnectError((data) {
        if (_connectionState == ConnectionState.none) return;
        _connectionState = ConnectionState.none;
        debugPrint("connect error $data");
        state?.dispose();
        state = null;

        ref.read(appRouter).replaceAll([ErrorConnectRoute(hostname: hostname, port: port, token: token)]);
      })
      ..onConnectTimeout((data) {
        if (_connectionState == ConnectionState.none) return;
        _connectionState = ConnectionState.none;
        debugPrint("connect timeout $data");
        state?.dispose();
        state = null;
        ref.read(appRouter).replaceAll([ErrorConnectRoute(hostname: hostname, port: port, token: token)]);
      })
      ..onError((data) {
        if (_connectionState == ConnectionState.none) return;
        _connectionState = ConnectionState.none;
        debugPrint("error $data");
        state?.dispose();
        state = null;
        ref.read(appRouter).replaceAll([ErrorConnectRoute(hostname: hostname, port: port, token: token)]);
      })
      ..onDisconnect((_) {
        if (_connectionState == ConnectionState.none) return;
        _connectionState = ConnectionState.none;
        debugPrint("disconnected");
        state?.dispose(); // Make sure to dispose the socket
        state = null;
        ref.read(appRouter).replaceAll([const HomeRoute()]);
      })
      ..connect();
  }

  Future<void> setup(Socket socket) async {
    socket
      ..on("stagingState", (data) => ref.read(communicatorProvider).handleStagingState(data))
      ..on("createPage", (data) => ref.read(communicatorProvider).handleCreatePage(data))
      ..on("createEntry", (data) => ref.read(communicatorProvider).handleCreateEntry(data))
      ..on("updateEntry", (data) => ref.read(communicatorProvider).handleUpdateEntry(data))
      ..on("deleteEntry", (data) => ref.read(communicatorProvider).handleDeleteEntry(data))
      ..on("updateWriters", (data) => ref.read(communicatorProvider).handleUpdateWriters(data));

    await ref.read(communicatorProvider).fetchBook();
    await ref.read(appRouter).push(const BookRoute());
  }

  @override
  void dispose() {
    state?.dispose();
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
    final String rawAdapters = await socket.emitWithAckAsync("fetch", "adapters");

    final jsonPages = jsonDecode(rawPages) as List;
    final jsonAdapters = jsonDecode(rawAdapters) as List;

    final pages = jsonPages.map((e) => Page.fromJson(e)).toList();
    final adapters = jsonAdapters.map((e) => Adapter.fromJson(e)).toList();

    final book = Book(name: "Typewriter", adapters: adapters, pages: pages);
    ref.read(bookProvider.notifier).book = book;
  }

  Future<void> createPage(String name) async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    await socket.emitWithAckAsync("createPage", name);
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

    return socket.emitWithAckAsync("createEntry", jsonEncode(data));
  }

  Future<void> updateEntry(String pageId, String entryId, String path, dynamic value) async {
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

    socket.emit("updateEntry", jsonEncode(data));
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

    socket.emit("deleteEntry", jsonEncode(data));
  }

  Future<void> publish() async {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }
    if (ref.read(stagingStateProvider) != StagingState.staging) return;

    socket.emit("publish", "");
  }

  void updateSelfWriter(Map<String, dynamic> data) {
    final socket = ref.read(socketProvider);
    if (socket == null || !socket.connected) {
      return;
    }

    socket.emit("updateWriter", jsonEncode(data));
  }

  void handleStagingState(dynamic data) {
    final rawStaging = data as String;
    final state =
        StagingState.values.firstWhere((state) => state.name == rawStaging, orElse: () => StagingState.production);
    ref.read(stagingStateProvider.notifier).state = state;
  }

  void handleCreatePage(dynamic data) {
    final name = data as String;
    ref.read(bookProvider.notifier).insertPage(Page(name: name));
  }

  void handleCreateEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final pageId = json["pageId"] as String;
    final entry = Entry.fromJson(json["entry"] as Map<String, dynamic>);
    final page = ref.read(pageProvider(pageId));
    if (page == null) return;
    page.syncInsertEntry(ref, entry);
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
    page.syncInsertEntry(ref, newEntry);
  }

  void handleDeleteEntry(dynamic data) {
    final json = jsonDecode(data as String) as Map<String, dynamic>;
    final pageId = json["pageId"] as String;
    final entryId = json["entryId"] as String;
    final page = ref.read(pageProvider(pageId));
    if (page == null) return;
    page.syncDeleteEntry(ref, entryId);
  }

  void handleUpdateWriters(dynamic data) {
    ref.read(writersProvider.notifier).update(data);
  }
}
