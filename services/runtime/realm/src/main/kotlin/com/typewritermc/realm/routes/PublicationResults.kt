package com.typewritermc.realm.routes

import com.typewritermc.services.libs.communicator.result.CommunicationResult

/**
 * Converts failed transport publication into a producer failure.
 *
 * This confirms acceptance by the communicator only. Subscriber processing and durable receipt remain outside this
 * boundary.
 */
internal fun CommunicationResult<Unit>.requirePublished() {
    if (this is CommunicationResult.Failure) error("Watch publication failed: $error")
}
