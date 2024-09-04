package com.typewritermc.engine.paper.content.components

import com.typewritermc.engine.paper.content.ContentComponent
import com.typewritermc.engine.paper.content.ComponentContainer
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