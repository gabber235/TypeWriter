package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("on_player_kill_entity", "When a player kills an entity", Colors.YELLOW, Icons.SKULL)
class PlayerKillEntityEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Help("The type of entity that was killed.")
	val entityType: Optional<EntityType> = Optional.empty(),
) : EventEntry


@EntryListener(PlayerKillEntityEventEntry::class)
fun onKill(event: EntityDeathEvent, query: Query<PlayerKillEntityEventEntry>) {
	val killer = event.entity.killer ?: return

	query findWhere { entry ->
		entry.entityType.map { it == event.entityType }.orElse(true)
	} triggerAllFor killer
}