package com.caleb.typewriter.combatlogx.entries.event

import com.github.sirblobman.combatlogx.api.event.PlayerUntagEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*

@Entry("on_player_exit_combat", "When a player is no longer in combat", Colors.YELLOW, Icons.HEART_CIRCLE_PLUS)
class PlayerExitCombatEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(PlayerExitCombatEventEntry::class)
fun onExitCombat(event: PlayerUntagEvent, query: Query<PlayerExitCombatEventEntry>) {
	val player: Player = event.player

	query.find() triggerAllFor player
}

