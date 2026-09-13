package com.typewritermc.loader.artifact

/**
 * Loader side names for the API artifact transfer contract.
 *
 * The aliases keep implementation code in the artifact package while the API package remains the cross module
 * contract. Wire implementations must preserve the API result and offset semantics.
 */
typealias ArtifactDigest = com.typewritermc.loader.api.artifact.ArtifactDigest
typealias BlobChunk = com.typewritermc.loader.api.artifact.BlobChunk

/** Exposes the API blob transfer contract to loader implementation packages. */
typealias BlobEndpoint = com.typewritermc.loader.api.artifact.BlobEndpoint
typealias BlobMetadata = com.typewritermc.loader.api.artifact.BlobMetadata
typealias BlobWriteSession = com.typewritermc.loader.api.artifact.BlobWriteSession
typealias DigestAlgorithm = com.typewritermc.loader.api.artifact.DigestAlgorithm
typealias TransferId = com.typewritermc.loader.api.artifact.TransferId

/** Maximum accepted immutable blob size in bytes, shared with the loader API contract. */
const val MAXIMUM_BLOB_SIZE: Long = com.typewritermc.loader.api.artifact.MAXIMUM_BLOB_SIZE

/** Default read and write chunk size in bytes, shared with the loader API contract. */
const val DEFAULT_CHUNK_SIZE: Int = com.typewritermc.loader.api.artifact.DEFAULT_CHUNK_SIZE

/** Maximum accepted read and write chunk size in bytes, shared with the loader API contract. */
const val MAXIMUM_CHUNK_SIZE: Int = com.typewritermc.loader.api.artifact.MAXIMUM_CHUNK_SIZE
