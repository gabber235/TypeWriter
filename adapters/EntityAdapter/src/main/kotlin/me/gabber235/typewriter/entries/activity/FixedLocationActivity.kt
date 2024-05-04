package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.ref
import java.util.*

@Entry("fixed_location_activity", "A fixed location activity", Colors.BLUE, "majesticons:map-marker-area")
class FixedLocationActivityEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The location of the activity")
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity = FixedLocationActivity(ref())
}

private class FixedLocationActivity(val ref: Ref<FixedLocationActivityEntry>) : EntityActivity {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean = ref canActivateFor context

    override fun currentTask(context: TaskContext, currentLocation: LocationProperty): EntityTask {
        return FixedLocationActivityTask(currentLocation)
    }
}

private class FixedLocationActivityTask(override val location: LocationProperty) : EntityTask {
    override fun tick(context: TaskContext) {}
    override fun mayInterrupt(): Boolean = true
    override fun isComplete(): Boolean = false
}