package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("on_player_kill_player", "When a player kills a player", Colors.YELLOW, Icons.SKULL)
class PlayerKillPlayerEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	@Help("The triggers to be executed for the target player.")
	val targetTriggers: List<String> = emptyList(),
) : EventEntry


@EntryListener(PlayerKillPlayerEventEntry::class)
fun onKill(event: EntityDeathEvent, query: Query<PlayerKillPlayerEventEntry>) {
	val killer =
		event.entity.killer ?: return

	val target = event.entity as? Player ?: return

	val entries = query.find()

	entries triggerAllFor killer
	entries.flatMap { it.targetTriggers } triggerEntriesFor target
}