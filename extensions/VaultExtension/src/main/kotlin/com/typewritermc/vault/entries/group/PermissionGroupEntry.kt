package com.typewritermc.vault.entries.group

import lirand.api.extensions.server.hasPermissionOrStar
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.GroupId
import org.bukkit.entity.Player

@Entry("permission_group", "Groups grouped by permission", Colors.MYRTLE_GREEN, "fa6-solid:key")
/**
 * The `Permission Group` is a group for which a player has a certain permission.
 * To determine if a player is part of this group, the permissions of the player are checked.
 * If the player has all the permissions, they are part of the group.
 *
 * ## How could this be used?
 * To send a message to all the donators.
 */
class PermissionGroupEntry(
    override val id: String = "",
    override val name: String = "",
    val groups: List<PermissionGroup> = emptyList(),
) : GroupEntry {
    override fun groupId(player: Player): GroupId? {
        return groups
            .firstOrNull {
                it.permissions.all { permission -> player.hasPermissionOrStar(permission) }
            }
            ?.let { GroupId(it.group) }
    }
}

data class PermissionGroup(
    val group: String = "",
    val permissions: List<String> = emptyList(),
)