package me.gabber235.typewriter.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TypewriterReloadEvent : Event() {
	override fun getHandlers(): HandlerList = getHandlerList()

	companion object {
		private val handlers = HandlerList()

		@JvmStatic
		fun getHandlerList(): HandlerList = handlers
	}
}