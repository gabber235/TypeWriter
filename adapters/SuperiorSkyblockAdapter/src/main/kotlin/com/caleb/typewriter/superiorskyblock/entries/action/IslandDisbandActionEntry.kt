package com.caleb.typewriter.superiorskyblock.entries.action

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("disband_island", "Disbands player's island", Colors.RED, Icons.TRASH)
data class IslandDisbandActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
) : ActionEntry {

	override fun execute(player: Player) {
		super.execute(player)

		val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
		val island = sPlayer.island
		island?.disbandIsland();
	}
}