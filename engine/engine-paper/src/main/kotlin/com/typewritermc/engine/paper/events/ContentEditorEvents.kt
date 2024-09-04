package com.typewritermc.engine.paper.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class ContentEditorStartEvent(
    player: Player
) : PlayerEvent(player) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}

class ContentEditorEndEvent(
    player: Player
) : PlayerEvent(player) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}
