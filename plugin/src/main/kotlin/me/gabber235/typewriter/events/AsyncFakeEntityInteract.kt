package me.gabber235.typewriter.events

import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class AsyncFakeEntityInteract(
    player: Player,
    val entityId: Int,
    val definition: EntityDefinitionEntry,
) : PlayerEvent(player, true) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}