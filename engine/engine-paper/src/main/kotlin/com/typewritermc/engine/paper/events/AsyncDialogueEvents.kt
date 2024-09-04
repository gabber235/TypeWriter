package com.typewritermc.engine.paper.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class AsyncDialogueStartEvent(player: Player) : PlayerEvent(player, true) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}

class AsyncDialogueSwitchEvent(player: Player): PlayerEvent(player, true) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}

class AsyncDialogueEndEvent(player: Player): PlayerEvent(player, true) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}