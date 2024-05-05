package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.entry.roadnetwork.gps.PointToPointGPS
import me.gabber235.typewriter.snippets.snippet
import org.bukkit.Location
import java.util.*

private val locationActivityRange by snippet("entity.activity.target_location.range", 1.0)

@Entry("target_location_activity", "A location activity", Colors.BLUE, "mdi:map-marker-account")
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
    val roadNetwork: Ref<RoadNetworkEntry> = emptyRef(),
    val targetLocation: Location = Location(null, 0.0, 0.0, 0.0),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity =
        TargetLocationActivity(ref(), roadNetwork, targetLocation)
}

class TargetLocationActivity(
    val ref: Ref<TargetLocationActivityEntry>,
    private val network: Ref<RoadNetworkEntry>,
    private val targetLocation: Location,
) : EntityActivity {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        if (!ref.canActivateFor(context)) {
            return false
        }

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