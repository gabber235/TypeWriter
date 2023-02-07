package me.gabber235.typewriter.entries.event

import io.papermc.paper.event.player.AsyncChatEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.plainText
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("on_player_kill_player", "When a player kills a player", Colors.YELLOW, Icons.SKULL)
class PlayerKillPlayerEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	val targetTriggers: List<String> = emptyList(),
) : EventEntry


@EntryListener(PlayerKillPlayerEventEntry::class)
fun onKill(event: EntityDeathEvent, query: Query<PlayerKillPlayerEventEntry>) {

	event.entity.killer ?: return
	if(event.entity.killer !is Player) return

	val player = event.entity.killer as Player

	if(event.entity !is Player) return

	val target = event.entity as Player

	val entries = query.find()

	entries triggerAllFor player

	entries.flatMap { it.targetTriggers }.map { s -> EntryTrigger(s) }.let { triggers ->
		InteractionHandler.startInteractionAndTrigger(target, triggers) }
}