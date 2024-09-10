package com.typewritermc.engine.paper.events

import lirand.api.extensions.server.server
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TypewriterUnloadEvent : Event(!server.isPrimaryThread) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}