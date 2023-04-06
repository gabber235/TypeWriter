package me.ahdg6.typewriter.mythicmobs.entries.fact

import io.lumine.mythic.bukkit.MythicBukkit
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("mythic_mob_count_fact", "Count the number of active Mythic Mobs of the specified type", Colors.PURPLE, Icons.PLACE_OF_WORSHIP)
data class MobCountFact(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	@Help("The id of the mob to count")
	val mobName: String = "",
) : ReadableFactEntry {
	override fun read(playerId: UUID): Fact {
		val mob = MythicBukkit.inst().mobManager.getMythicMob(mobName)
		if (!mob.isPresent) return Fact(id, 0)

		var count = 0
		for (activeMob in MythicBukkit.inst().mobManager.activeMobs) {
			if (activeMob.type == mob.get()) {
                count++
            }
		}

		return Fact(id, count)
	}
}