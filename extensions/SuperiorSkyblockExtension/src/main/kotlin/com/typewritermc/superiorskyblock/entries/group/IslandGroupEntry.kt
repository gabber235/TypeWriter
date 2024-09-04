package com.typewritermc.superiorskyblock.entries.group

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.GroupId
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