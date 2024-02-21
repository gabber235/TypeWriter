package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.inAudience
import me.gabber235.typewriter.entry.isActive
import org.bukkit.Location
import org.bukkit.entity.Player

abstract class AudienceEntityActivity(
    private val player: Player?,
    private val ref: Ref<EntityActivityEntry>,
) : EntityActivity {
    override fun canActivate(currentLocation: Location): Boolean {
        if (player == null) return ref.isActive
        return player.inAudience(ref)
    }
}