package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.EntityTask
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import org.bukkit.entity.Player
import java.util.*

@Entry("fixed_location_activity", "A fixed location activity", Colors.BLUE, "majesticons:map-marker-area")
class FixedLocationActivityEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The location of the activity")
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(player: Player?): EntityActivity = FixedLocationActivity()
}

private class FixedLocationActivity :
    EntityActivity {
    override fun canActivate(currentLocation: LocationProperty): Boolean = true

    override fun currentTask(currentLocation: LocationProperty): EntityTask {
        return FixedLocationActivityTask(currentLocation)
    }
}

private class FixedLocationActivityTask(override val location: LocationProperty) : EntityTask {
    override fun tick() {
    }

    override fun mayInterrupt(): Boolean = true

    override fun isComplete(): Boolean = false
}