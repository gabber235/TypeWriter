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
import java.util.*

@Entry("on_player_hit_entity", "When a player hits an entity", Colors.YELLOW, Icons.HEART_CRACK)
class PlayerHitEntityEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val entityType: EntityType = EntityType.PLAYER,
) : EventEntry


@EntryListener(PlayerHitEntityEventEntry::class)
fun onHit(event: EntityDamageByEntityEvent, query: Query<PlayerHitEntityEventEntry>) {

	if(event.damager !is Player) return

	val player = event.damager as Player

	query findWhere { it.entityType == event.entityType } triggerAllFor player
}