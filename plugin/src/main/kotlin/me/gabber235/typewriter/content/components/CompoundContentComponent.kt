package me.gabber235.typewriter.content.components

import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.content.ComponentContainer
import org.bukkit.entity.Player

abstract class CompoundContentComponent : ContentComponent, ComponentContainer {
    override val components = mutableListOf<ContentComponent>()
    override suspend fun initialize(player: Player) {
        components.forEach { it.initialize(player) }
    }

    override suspend fun tick(player: Player) {
        components.forEach { it.tick(player) }
    }

    override suspend fun dispose(player: Player) {
        components.forEach { it.dispose(player) }
        components.clear()
    }
}