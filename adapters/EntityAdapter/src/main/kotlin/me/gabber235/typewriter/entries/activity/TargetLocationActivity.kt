package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.roadnetwork.gps.PointToPointGPS
import me.gabber235.typewriter.snippets.snippet
import org.bukkit.Location
import java.util.*

private val locationActivityRange by snippet("entity.activity.target_location.range", 1.0)

@Entry("target_location_activity", "A location activity", Colors.BLUE, "fa6-solid:map-marker-alt")
/**
 * The `TargetLocationActivityEntry` is an activity that makes the entity navigate to a specific location.
 *
 * The activity will only activate when the entity is outside a certain range.
 *
 * ## How could this be used?
 * This could be used to make an entity navigate to a specific location.
 */
class TargetLocationActivityEntry(
    override val id: String = "",
    override val name: String = "",
    override val priorityOverride: Optional<Int> = Optional.empty(),
    val roadNetwork: Ref<RoadNetworkEntry> = emptyRef(),
    val targetLocation: Location = Location(null, 0.0, 0.0, 0.0),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity = TargetLocationActivity(roadNetwork, targetLocation)
}

class TargetLocationActivity(
    private val network: Ref<RoadNetworkEntry>,
    private val targetLocation: Location,
) : EntityActivity {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        // Only activate if outside the defined range
        val distance = currentLocation.distanceSqrt(targetLocation.toProperty()) ?: return false
        return distance > locationActivityRange * locationActivityRange
    }

    override fun currentTask(context: TaskContext, currentLocation: LocationProperty): EntityTask {
        return NavigationActivityTask(PointToPointGPS(
            network,
            { currentLocation.toLocation() },
            { targetLocation }
        ), currentLocation)
    }
}