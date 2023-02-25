package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("on_player_death", "When a player dies", Colors.YELLOW, Icons.SKULL_CROSSBONES)
class PlayerDeathEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Help("The cause of the death.")
	val deathCause: Optional<DamageCause> = Optional.empty()
) : EventEntry


@EntryListener(PlayerDeathEventEntry::class)
fun onDeath(event: EntityDeathEvent, query: Query<PlayerDeathEventEntry>) {
	if (event.entity !is Player) return

	val player = event.entity as Player

	query findWhere { entry ->
		entry.deathCause.map { it == event.entity.lastDamageCause?.cause }.orElse(true)
	} triggerAllFor player
}