package com.caleb.typewriter.vault.entries.audience

import lirand.api.extensions.server.hasPermissionOrStar
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("permission_audience", "Audiences grouped by permission", Colors.MYRTLE_GREEN, Icons.KEY)
/**
 * The `Permission Audience` is an audience for which a player has a certain permission.
 * To determine if a player is part of this audience, the permissions of the player are checked.
 * If the player has all the permissions, they are part of the audience.
 */
class PermissionAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    val audiences: List<PermissionAudience> = emptyList(),
) : AudienceEntry {
    override fun audienceId(player: Player): AudienceId? {
        return audiences
            .firstOrNull {
                it.permissions.all { permission -> player.hasPermissionOrStar(permission) }
            }
            ?.let { AudienceId(it.audience) }
    }
}

data class PermissionAudience(
    val audience: String = "",
    val permissions: List<String> = emptyList(),
)