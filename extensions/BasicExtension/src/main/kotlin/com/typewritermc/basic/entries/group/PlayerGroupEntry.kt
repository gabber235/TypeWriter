package com.typewritermc.basic.entries.group

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.GroupId
import org.bukkit.entity.Player

@Entry("player_group", "Group for every individual player", Colors.MYRTLE_GREEN, "fa6-solid:user")
/**
 * The `Player Group` is a group that is specific to each individual player.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to each player, like their progress in a quest or their reputation with a faction.
 */
class PlayerGroupEntry(
    override val id: String = "",
    override val name: String = "",
) : GroupEntry {
    override fun groupId(player: Player): GroupId = GroupId(player.uniqueId)
}