package com.caleb.typewriter.superiorskyblock.entries.action

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.caleb.typewriter.superiorskyblock.SuperiorSkyblockAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("superiorskyblock_disband_island", "[SuperiorSkyblock] Disbands player's island", Colors.RED, Icons.TRASH)
data class IslandDisbandActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),

) : ActionEntry {

    override fun execute(player: Player) {
        super.execute(player)

        var sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        var island = sPlayer.getIsland()
        island?.disbandIsland();

    }

}