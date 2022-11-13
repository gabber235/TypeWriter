package me.gabber235.typewriter.adapters

import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.entries.DialogueEntry
import org.bukkit.entity.Player

class MessengerData(
	val messenger: Class<out DialogueMessenger<*>>,
	val dialogue: Class<out DialogueEntry>,
	val filter: Class<out MessengerFilter>,
	val priority: Int
)

interface MessengerFilter {
	fun filter(player: Player, entry: DialogueEntry): Boolean
}

class EmptyMessengerFilter : MessengerFilter {
	override fun filter(player: Player, entry: DialogueEntry): Boolean {
		return true
	}
}