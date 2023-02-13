package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.entry.TriggerEntry
import org.bukkit.entity.Player

@Tags("event")
interface EventEntry : TriggerEntry

interface CustomCommandEntry : EventEntry {
	val command: String
}

class Event(val player: Player, val triggers: List<EventTrigger>) {
	constructor(player: Player, vararg triggers: EventTrigger) : this(player, triggers.toList())

	operator fun contains(trigger: EventTrigger) = triggers.contains(trigger)

	operator fun contains(entry: Entry) = EntryTrigger(entry.id) in triggers

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Event) return false

		if (triggers != other.triggers) return false
		if (player.uniqueId != other.player.uniqueId) return false

		return true
	}

	override fun hashCode(): Int {
		var result = triggers.hashCode()
		result = 31 * result + player.hashCode()
		return result
	}
}

interface EventTrigger {
	val id: String
}

data class EntryTrigger(override val id: String) : EventTrigger

enum class SystemTrigger : EventTrigger {
	DIALOGUE_NEXT,
	DIALOGUE_END,
	;

	override val id: String
		get() = "system.${name.lowercase().replace('_', '.')}"
}