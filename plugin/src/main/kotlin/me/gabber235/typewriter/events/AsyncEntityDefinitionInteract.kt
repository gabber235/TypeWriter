package me.gabber235.typewriter.events

import com.github.retrooper.packetevents.protocol.player.InteractionHand
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class AsyncFakeEntityInteract(
    player: Player,
    val entityId: Int,
    val hand: InteractionHand,
    val action: InteractAction,
) : PlayerEvent(player, true) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}

class AsyncEntityDefinitionInteract(
    player: Player,
    val entityId: Int,
    val definition: EntityDefinitionEntry,
    val hand: InteractionHand,
    val action: InteractAction,
) : PlayerEvent(player, true) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}