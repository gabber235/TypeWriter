package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.facts.FactDatabase
import org.bukkit.entity.Player

@Tags("action")
interface ActionEntry : TriggerableEntry {
	fun execute(player: Player) {
		FactDatabase.modify(player.uniqueId, modifiers)
	}
}