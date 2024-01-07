import "dart:async";

import "package:socket_io_client/socket_io_client.dart";

extension SocketExt on Socket {
  Future<dynamic> emitWithAckAsync(
    String event,
    dynamic data, {
    bool binary = false,
  }) {
    final completer = Completer<dynamic>();

    emitWithAck(
      event,
      data,
      binary: binary,
      ack: (data) {
        if (!completer.isCompleted) completer.complete(data);
      },
    );
    return completer.future;
  }
}
