package me.gabber235.typewriter.content.components

import me.gabber235.typewriter.content.ComponentContainer
import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.content.contentEditorCachedInventory
import org.bukkit.entity.Player

fun ComponentContainer.cachedInventory() = +CachedInventoryComponent()

class CachedInventoryComponent : ContentComponent, ItemsComponent {
    private var cachedInventory: Map<Int, IntractableItem>? = null

    override suspend fun initialize(player: Player) {
        cachedInventory = player.contentEditorCachedInventory?.mapIndexedNotNull { index, item ->
            item?.let { index to IntractableItem(it) {} }
        }?.toMap() ?: emptyMap()
    }

    override suspend fun tick(player: Player) {}
    override suspend fun dispose(player: Player) {}
    override fun items(player: Player): Map<Int, IntractableItem> {
        return cachedInventory ?: emptyMap()
    }
}