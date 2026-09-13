package com.typewritermc.imprint

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

/** Marks the one class that a hosted artifact exposes as its loader entrypoint. */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class ImprintRuntimeEntrypoint

/** Intermediate code generation result consumed by the Imprint manifest task. */
@Serializable
data class HostedRuntimeEntrypointMetadata(
    val classes: List<String>,
)

@OptIn(ExperimentalSerializationApi::class)
object HostedRuntimeEntrypointMetadataCodec {
    private val cbor = Cbor { encodeDefaults = true }

    fun encode(metadata: HostedRuntimeEntrypointMetadata): ByteArray = cbor.encodeToByteArray(metadata)

    fun decode(bytes: ByteArray): HostedRuntimeEntrypointMetadata = cbor.decodeFromByteArray(bytes)
}
