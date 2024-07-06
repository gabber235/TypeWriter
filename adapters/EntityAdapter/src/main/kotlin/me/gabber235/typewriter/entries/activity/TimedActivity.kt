package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.ActivityContext
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.SingleChildActivity
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.GenericEntityActivityEntry
import java.time.Duration
import java.util.*

@Entry(
    "timed_activity",
    "Allows child activities for a limited amount of time",
    Colors.PALATINATE_BLUE,
    "fa6-solid:hourglass"
)
/**
 * The `TimedActivityEntry` is an activity that allows child activities for a limited amount of time.
 *
 * When the duration is up, the activity will deactivate.
 * Then the activity will be on cooldown for a set amount of time before it can be activated again.
 *
 * ## How could this be used?
 * This could be used to make an entity do something for a limited amount of time.
 */
class TimedActivityEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The duration child activities will be active for.")
    val duration: Duration = Duration.ofSeconds(10),
    @Help("The cooldown time before the activity can be activated again.")
    val cooldown: Duration = Duration.ofSeconds(1),
    @Help("The activity that will be used when the duration is active.")
    val activeActivity: Ref<out EntityActivityEntry> = emptyRef(),
    @Help("The activity that will be used when it is on cooldown.")
    val cooldownActivity: Ref<out EntityActivityEntry> = emptyRef(),
) : GenericEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<in ActivityContext> {
        return TimedActivity(duration, cooldown, activeActivity, cooldownActivity, currentLocation)
    }
}

class TimedActivity(
    private val duration: Duration,
    private val cooldown: Duration,
    private val activeActivity: Ref<out EntityActivityEntry>,
    private val cooldownActivity: Ref<out EntityActivityEntry>,
    startLocation: LocationProperty,
) : SingleChildActivity<ActivityContext>(startLocation) {
    private var state: TimedActivityState = Active(System.currentTimeMillis())

    override fun currentChild(context: ActivityContext): Ref<out EntityActivityEntry> {
        if (state.hasExpired) {
            state = state.nextState
        }

        return when (state) {
            is Active -> activeActivity
            is Cooldown -> cooldownActivity
            else -> cooldownActivity
        }
    }

    interface TimedActivityState {
        val hasExpired: Boolean
        val nextState: TimedActivityState
    }

    inner class Active(private val startTime: Long) : TimedActivityState {
        override val hasExpired: Boolean
            get() = System.currentTimeMillis() - startTime > duration.toMillis()
        override val nextState: TimedActivityState
            get() = Cooldown(System.currentTimeMillis())
    }

    inner class Cooldown(private val startTime: Long) : TimedActivityState {
        override val hasExpired: Boolean
            get() = System.currentTimeMillis() - startTime > cooldown.toMillis()
        override val nextState: TimedActivityState
            get() = Active(System.currentTimeMillis())
    }
}