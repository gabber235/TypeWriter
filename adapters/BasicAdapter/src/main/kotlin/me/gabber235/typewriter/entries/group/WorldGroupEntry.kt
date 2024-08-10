package me.gabber235.typewriter.entries.group

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.GroupId
import org.bukkit.entity.Player

@Entry("world_group", "Group for the whole world", Colors.MYRTLE_GREEN, "bx:world")
/**
 * The `World Group` is a group that includes all the players in a world.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to a world, like the state of a world event or the state of a world boss.
 */
class WorldGroupEntry(
    override val id: String = "",
    override val name: String = "",
) : GroupEntry {
    override fun groupId(player: Player): GroupId = GroupId(player.world.uid)
}