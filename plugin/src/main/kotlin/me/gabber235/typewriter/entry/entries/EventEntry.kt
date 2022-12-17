package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.TriggerEntry
import org.bukkit.entity.Player

@Tags("event")
interface EventEntry : TriggerEntry

open class Event(val id: String, val player: Player) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Event) return false

		if (id != other.id) return false
		if (player.uniqueId != other.player.uniqueId) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + player.uniqueId.hashCode()
		return result
	}
}