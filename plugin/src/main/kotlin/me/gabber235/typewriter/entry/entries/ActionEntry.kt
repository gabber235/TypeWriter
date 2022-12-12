package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.RuleEntry
import me.gabber235.typewriter.facts.FactDatabase
import org.bukkit.entity.Player

@Tags("action")
interface ActionEntry : RuleEntry {
	fun execute(player: Player) {
		FactDatabase.modify(player.uniqueId, modifiers)
	}
}