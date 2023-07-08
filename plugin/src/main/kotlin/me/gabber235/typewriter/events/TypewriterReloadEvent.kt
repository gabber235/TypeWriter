package me.gabber235.typewriter.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TypewriterReloadEvent : Event() {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}