package com.typewritermc.services.libs.communicator.telemetry

import com.typewritermc.services.libs.communicator.transport.MessageHeaders
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapSetter

/** Reads propagated values from immutable headers using case insensitive lookup. */
object MessageHeadersGetter : TextMapGetter<MessageHeaders> {
    override fun keys(carrier: MessageHeaders): Iterable<String> = carrier.map { it.first }

    override fun get(
        carrier: MessageHeaders?,
        key: String,
    ): String? = carrier?.first(key)
}

/**
 * Collects propagator writes without mutating caller headers.
 *
 * [ownedFields] are removed before injection, preventing stale trace values from surviving reinjection. Other headers
 * remain available to the outgoing message.
 */
class MessageHeadersSetter(
    initial: MessageHeaders,
    ownedFields: Collection<String> = emptyList(),
) : TextMapSetter<Unit> {
    /** Current immutable headers after all injections. */
    var headers: MessageHeaders = ownedFields.fold(initial) { headers, field -> headers.remove(field) }
        private set

    override fun set(
        carrier: Unit?,
        key: String,
        value: String,
    ) {
        headers = headers.set(key, value)
    }
}
