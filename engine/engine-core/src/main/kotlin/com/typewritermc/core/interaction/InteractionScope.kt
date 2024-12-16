package com.typewritermc.core.interaction

import java.time.Duration
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InteractionScope(
    var interaction: Interaction,
    var bound: InteractionBound = InteractionBound.Empty,
) {
    private val interactionBoundStateOverrides = ConcurrentHashMap<UUID, InteractionBoundStateOverride>()

    val boundState: InteractionBoundState
        get() {
            val override = interactionBoundStateOverrides.values.maxByOrNull { it.priority }
            if (override != null) {
                return override.state
            }

            return if (interaction.priority > bound.priority) {
                InteractionBoundState.BLOCKING
            } else {
                InteractionBoundState.INTERRUPTING
            }
        }

    suspend fun initialize() {
        interaction.initialize()
        bound.initialize()
    }
    suspend fun tick(deltaTime: Duration) {
        interaction.tick(deltaTime)
        bound.tick()
    }

    suspend fun swapInteraction(interaction: Interaction) {
        this.interaction.teardown()
        this.interaction = interaction
        interaction.initialize()
    }

    suspend fun swapBound(bound: InteractionBound) {
        this.bound.teardown()
        this.bound = bound
        bound.initialize()
    }

    suspend fun teardown(force: Boolean = false) {
        bound.teardown()
        interaction.teardown(force)
    }

    fun addBoundStateOverride(id: UUID = UUID.randomUUID(), state: InteractionBoundState, priority: Int = 0): UUID {
        interactionBoundStateOverrides[id] = InteractionBoundStateOverride(state, priority)
        return id
    }

    fun removeBoundStateOverride(id: UUID): Boolean {
        return interactionBoundStateOverrides.remove(id) != null
    }
}

data class InteractionBoundStateOverride(
    val state: InteractionBoundState,
    val priority: Int,
)

data class InteractionBoundStateOverrideSubscription(
    val id: UUID = UUID.randomUUID(),
    val playerUUID: UUID,
)
