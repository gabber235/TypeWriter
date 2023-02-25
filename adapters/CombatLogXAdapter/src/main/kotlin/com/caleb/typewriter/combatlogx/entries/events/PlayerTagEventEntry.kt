package com.caleb.typewriter.combatlogx.entries.events

import com.github.sirblobman.combatlogx.api.event.PlayerTagEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*

@Entry("on_player_tag", "[CombatLogX] When a player gets tagged", Colors.YELLOW, Icons.HEART_CRACK)
class PlayerTagEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	val aggressorTriggers: List<String> = emptyList()
) : EventEntry

@EntryListener(PlayerTagEventEntry::class)
fun onTagged(event: PlayerTagEvent, query: Query<PlayerTagEventEntry>) {

	val player: Player = event.player
	val aggressor = event.enemy

	val entries = query.find()

	//if aggresor is not null and is a player, trigger all entries with aggressor triggers
	if(aggressor != null && aggressor is Player) {
		query.find() triggerAllFor player

		entries.flatMap { it.aggressorTriggers }.map { s-> EntryTrigger(s) }.let { triggers ->
			InteractionHandler.startInteractionAndTrigger(aggressor, triggers) }
	} else {
		query.find() triggerAllFor player
	}

}

