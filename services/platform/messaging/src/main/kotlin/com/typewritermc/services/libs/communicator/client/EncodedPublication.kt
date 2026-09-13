package com.typewritermc.services.libs.communicator.client

import com.typewritermc.services.libs.communicator.address.MessageAddress
import com.typewritermc.services.libs.communicator.transport.Payload

/**
 * An immutable publication prepared for sending without encoding or caller supplied headers.
 *
 * The address and bytes are copied or validated by their value types. [Communicator.publishEncoded] treats successful
 * publication as transport acceptance only.
 */
data class EncodedPublication(
    val address: MessageAddress,
    val payload: Payload,
)
