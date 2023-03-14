package com.caleb.typewriter.mythicmobs.entries.fact

import io.lumine.mythic.bukkit.MythicBukkit
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("mob_exist_fact", "If the mob exist in the world", Colors.PURPLE, Icons.PLACE_OF_WORSHIP)
data class MobExsitFact(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	@Help("The mob's name to check")
	val mob: String = "",
) : ReadableFactEntry {
	override fun read(playerId: UUID): Fact {
		val player = server.getPlayer(playerId) ?: return Fact(id, 0)
		val mob = MythicBukkit.inst().mobManager.getMythicMob(mob) ?: return Fact(id, 0)
		var value = 0
		for (activeMob in MythicBukkit.inst().mobManager.activeMobs) {
			if (!activeMob.name.equals(mob)) continue;
			value = 1
			break
		}

		return Fact(id, value)
	}
}