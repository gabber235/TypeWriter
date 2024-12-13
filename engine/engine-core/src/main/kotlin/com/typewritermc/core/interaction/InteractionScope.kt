package com.typewritermc.core.interaction

import java.time.Duration

class InteractionScope(
    var interaction: Interaction,
) {
    suspend fun initialize() {
        interaction.initialize()
    }
    suspend fun tick(deltaTime: Duration) {
        interaction.tick(deltaTime)
    }

    suspend fun swapInteraction(interaction: Interaction) {
        this.interaction.teardown()
        this.interaction = interaction
        interaction.initialize()
    }

    suspend fun teardown(force: Boolean = false) {
        interaction.teardown(force)
    }
}