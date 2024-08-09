package me.gabber235.typewriter.entries.group

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.GroupId
import org.bukkit.entity.Player

@Entry("global_group", "One group with all the online players", Colors.MYRTLE_GREEN, "fa6-solid:globe")
/**
 * The `Global Group` is a group that includes all the online players.
 *
 * ## How could this be used?
 * This could be used to have facts that are the same for all the players,
 * like a global objective that all the players have to work together to achieve.
 */
class GlobalGroupEntry(
    override val id: String = "",
    override val name: String = "",
) : GroupEntry {
    override fun groupId(player: Player): GroupId = GroupId("global")
}