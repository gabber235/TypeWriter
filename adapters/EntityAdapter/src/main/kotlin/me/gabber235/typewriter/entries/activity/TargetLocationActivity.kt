package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.GenericEntityActivityEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
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
    @Help("The activity that will be used when the entity is at the target location.")
    val idleActivity: Ref<out EntityActivityEntry> = emptyRef(),
) : GenericEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<in ActivityContext> {
        return TargetLocationActivity(roadNetwork, targetLocation, idleActivity, currentLocation)
    }
}

class TargetLocationActivity(
    private val network: Ref<RoadNetworkEntry>,
    private val targetLocation: Location,
    private val idleActivity: Ref<out EntityActivityEntry>,
    private val startLocation: LocationProperty,
) : GenericEntityActivity {
    private var state: State = IdleState()
    private var currentActivity: EntityActivity<ActivityContext>? = null


    private interface State {
        val isValid: Boolean
        fun nextState(): State
        fun createActivity(
            context: ActivityContext,
            currentLocation: LocationProperty
        ): EntityActivity<ActivityContext>
    }

    private inner class IdleState : State {
        override val isValid: Boolean
            get() {
                val distance = currentLocation.distanceSqrt(targetLocation) ?: return false
                return distance <= locationActivityRange * locationActivityRange
            }

        override fun nextState(): State = NavigatingState()

        override fun createActivity(
            context: ActivityContext,
            currentLocation: LocationProperty
        ): EntityActivity<ActivityContext> {
            return (idleActivity.get() ?: IdleActivity).create(context, currentLocation)
        }
    }

    private inner class NavigatingState : State {
        override val isValid: Boolean
            get() {
                val distance = currentLocation.distanceSqrt(targetLocation) ?: return true
                return distance > locationActivityRange * locationActivityRange
            }

        override fun nextState(): State = IdleState()

        override fun createActivity(
            context: ActivityContext,
            currentLocation: LocationProperty
        ): EntityActivity<ActivityContext> {
            return NavigationActivity(PointToPointGPS(
                network,
                { currentLocation.toLocation() },
                { targetLocation }
            ), currentLocation)
        }
    }

    override fun initialize(context: ActivityContext) {
        if (!state.isValid) {
            state = state.nextState()
        }

        currentActivity = state.createActivity(context, currentLocation)
        currentActivity?.initialize(context)
    }

    override fun tick(context: ActivityContext): TickResult {
        if (!state.isValid) {
            currentActivity?.dispose(context)
            state = state.nextState()
            currentActivity = state.createActivity(context, currentLocation)
            currentActivity?.initialize(context)
        }

        return currentActivity?.tick(context) ?: TickResult.IGNORED
    }

    override fun dispose(context: ActivityContext) {
        currentActivity?.dispose(context)
        currentActivity = null
    }

    override val currentLocation: LocationProperty
        get() = currentActivity?.currentLocation ?: startLocation
}