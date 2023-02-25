package com.caleb.typewriter.combatlogx.entries.events

import com.github.sirblobman.combatlogx.api.event.PlayerTagEvent
import com.github.sirblobman.combatlogx.api.event.PlayerUntagEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*

@Entry("on_player_untag", "[CombatLogX] When a player is no longer tagged", Colors.YELLOW, Icons.HEART_CIRCLE_PLUS)
class PlayerUntagEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(PlayerUntagEventEntry::class)
fun onUntag(event: PlayerUntagEvent, query: Query<PlayerUntagEventEntry>) {

	val player: Player = event.player

	query.find() triggerAllFor player

}

