package com.typewritermc.realm.routes

import com.typewritermc.realm.compiler.CompiledContentRepository
import com.typewritermc.services.libs.communicator.router.CommunicatorRoutesBuilder
import skirout.library.v1.compiled_content.WatchCompiledContentResponse

/**
 * Owns the initial side of the compiled content watch protocol.
 *
 * The repository supplies the current active activation at subscription time. Subsequent changes are published by
 * [CompiledContentEvents], so this route does not retain watcher state or acknowledge event delivery.
 */
internal class CompiledContentRoutes(
    private val content: CompiledContentRepository,
    private val contracts: LibraryContracts,
) {
    /** Registers the initial activation response for compiled content watchers. */
    fun register(builder: CommunicatorRoutesBuilder) =
        with(builder) {
            watch(contracts.watchCompiledContent) {
                WatchCompiledContentResponse.createInitial(activation = content.activeActivation()?.toSkir())
            }
        }
}
