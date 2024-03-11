package me.gabber235.typewriter.content

import me.gabber235.typewriter.content.components.IntractableItem
import me.gabber235.typewriter.content.components.ItemsComponent
import org.bukkit.entity.Player

abstract class ContentMode(
    val context: ContentContext,
    val player: Player,
) : ComponentContainer {
    override val components = mutableListOf<ContentComponent>()

    init {
        setup()
    }
    abstract fun setup()

    suspend fun initialize() {
        components.forEach { it.initialize(player) }
    }
    suspend fun tick() {
        components.forEach { it.tick(player) }
    }
    suspend fun dispose() {
        components.forEach { it.dispose(player) }
    }

    fun items(): Map<Int, IntractableItem> {
        return components.filterIsInstance<ItemsComponent>().flatMap { it.items(player).toList() }.toMap()
    }
}

interface ComponentContainer {
    val components: MutableList<ContentComponent>
    operator fun ContentComponent.unaryPlus() {
        components.add(this)
    }
}