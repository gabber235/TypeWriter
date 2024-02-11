package com.caleb.typewriter.superiorskyblock.entries.action

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import org.bukkit.entity.Player

@Entry("island_disband", "Disbands player's island", Colors.RED, "fa6-solid:trash-alt")
/**
 * The `Island Disband Action` disbands a player's island.
 *
 * ## How could this be used?
 *
 * This could be used to make a system of "re-birthing,"
 * where a player can disband their island and start over with benefits.
 */
class IslandDisbandActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : ActionEntry {

    override fun execute(player: Player) {
        super.execute(player)

        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.disbandIsland()
    }
}