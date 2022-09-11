package me.gabber235.typewriter.entry.event

import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.entry.RuleEntry
import org.bukkit.entity.Player

interface EventEntry : Entry

open class Event(val name: String, val player: Player) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Event) return false

		if (name != other.name) return false
		if (player.uniqueId != other.player.uniqueId) return false

		return true
	}

	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + player.uniqueId.hashCode()
		return result
	}
}