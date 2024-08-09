package com.caleb.typewriter.superiorskyblock.entries.group

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.GroupId
import org.bukkit.entity.Player

@Entry("island_group", "Group for the whole island", Colors.MYRTLE_GREEN, "fa6-solid:globe")
/**
 * The `Island Group` is a group that includes all the players on an island.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to an island, like challenges for an island.

 */
class IslandGroupEntry(
    override val id: String = "",
    override val name: String = "",
) : GroupEntry {
    override fun groupId(player: Player): GroupId? {
        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island ?: return null
        return GroupId(island.uniqueId)
    }
}