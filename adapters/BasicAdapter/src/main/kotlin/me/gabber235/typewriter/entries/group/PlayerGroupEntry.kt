package me.gabber235.typewriter.entries.group

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.GroupId
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