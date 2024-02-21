package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.WithRotation
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entity.AudienceEntityActivity
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.EntityTask
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.ref
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

@Entry("fixed_location_activity", "A fixed location activity", Colors.BLUE, "fa6-solid:map-marker")
class FixedLocationActivityEntry(
    override val id: String,
    override val name: String,
    @Help("The location of the activity")
    @WithRotation
    val location: Location = Location(null, 0.0, 0.0, 0.0),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(player: Player?): EntityActivity = FixedLocationActivity(location, player, ref())
}

private class FixedLocationActivity(
    private val location: Location,
    player: Player?,
    ref: Ref<EntityActivityEntry>,
) :
    AudienceEntityActivity(player, ref) {
    override fun currentTask(currentLocation: Location): EntityTask {
        return FixedLocationActivityTask(location)
    }

    override fun primaryLocation(): Location = location
}

private class FixedLocationActivityTask(override val location: Location) : EntityTask {
    override fun tick() {
    }

    override fun mayInterrupt(): Boolean = true

    override fun isComplete(): Boolean = false
}