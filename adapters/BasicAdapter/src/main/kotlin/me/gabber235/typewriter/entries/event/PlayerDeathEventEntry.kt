package me.gabber235.typewriter.entries.event

import io.papermc.paper.event.player.AsyncChatEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.plainText
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("on_player_death", "When a player dies", Colors.YELLOW, Icons.SKULL_CROSSBONES)
class PlayerDeathEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val entityType: EntityType = EntityType.PLAYER,
) : EventEntry


@EntryListener(PlayerDeathEventEntry::class)
fun onDeath(event: EntityDeathEvent, query: Query<PlayerDeathEventEntry>) {

	if(event.entity !is Player) return

	val player = event.entity as Player

	query.find() triggerAllFor player
}