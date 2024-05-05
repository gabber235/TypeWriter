package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.descendants
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.entry.ref
import java.time.Duration
import java.util.*

@Entry(
    name = "timed_activity",
    description = "Allows child activities for a limited amount of time",
    Colors.BLUE,
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
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    @Help("The duration child activities will be active for.")
    val duration: Duration = Duration.ofSeconds(10),
    @Help("The cooldown time before the activity can be activated again.")
    val cooldown: Duration = Duration.ofSeconds(1),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity {
        return TimedActivity(
            ref(),
            children
                .descendants(EntityActivityEntry::class)
                .mapNotNull { it.get() }
                .sortedByDescending { it.priority }
                .map { it.create(context) },
            duration,
            cooldown,
        )
    }
}

class TimedActivity(
    val ref: Ref<TimedActivityEntry>,
    children: List<EntityActivity>,
    private val duration: Duration,
    private val cooldown: Duration,
) : FilterActivity(children) {
    private var trackers = mutableMapOf<UUID, PlayerTimeTracker>()

    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        if (!ref.canActivateFor(context)) {
            return false
        }

        if (!super.canActivate(context, currentLocation)) {
            return false
        }

        context.viewers.forEach { player ->
            val tracker = trackers.computeIfAbsent(player.uniqueId) {
                PlayerTimeTracker(duration, cooldown)
            }
            tracker.update()
        }

        return !trackers.all { (_, tracker) -> tracker.isActive }
    }

    private inner class PlayerTimeTracker(
        private val duration: Duration,
        private val cooldown: Duration,
    ) {
        private var lastStart = System.currentTimeMillis()

        val isActive: Boolean
            get() = System.currentTimeMillis() - lastStart < duration.toMillis()

        val isCooldown: Boolean
            get() = System.currentTimeMillis() - lastStart < cooldown.toMillis() + duration.toMillis()

        fun update() {
            if (!isCooldown) {
                lastStart = System.currentTimeMillis()
            }
        }
    }
}