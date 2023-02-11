package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.triggerEntriesFor
import me.gabber235.typewriter.facts.FactDatabase
import org.bukkit.entity.Player

@Tags("action")
interface ActionEntry : TriggerableEntry {
	fun execute(player: Player) {
		FactDatabase.modify(player.uniqueId, modifiers)
	}
}

@Tags("custom_triggering_action")
interface CustomTriggeringActionEntry : ActionEntry {
	// Disable the normal triggers. So that the action can manually trigger the next actions.
	override val triggers: List<String>
		get() = emptyList()

	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	val customTriggers: List<String>

	fun Player.triggerCustomTriggers() {
		customTriggers triggerEntriesFor this
	}
}