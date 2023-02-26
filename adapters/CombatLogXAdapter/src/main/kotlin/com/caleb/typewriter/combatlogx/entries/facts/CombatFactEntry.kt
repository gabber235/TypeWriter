package com.caleb.typewriter.combatlogx.entries.facts

import com.caleb.typewriter.combatlogx.CombatLogXAdapter
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("combat_fact", "If the player is in combat", Colors.PURPLE, Icons.SHIELD_HALVED)
data class CombatFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
) : ReadableFactEntry {
	override fun read(playerId: UUID): Fact {
		val combatLogger = CombatLogXAdapter.getAPI() ?: return Fact(id, 0)
		val player = server.getPlayer(playerId) ?: return Fact(id, 0)
		val value = if (combatLogger.combatManager.isInCombat(player)) 1 else 0

		return Fact(id, value)
	}
}