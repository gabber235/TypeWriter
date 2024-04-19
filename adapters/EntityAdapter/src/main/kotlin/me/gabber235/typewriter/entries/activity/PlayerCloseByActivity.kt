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
import java.time.Duration
import java.util.*

@Entry("player_close_by_activity", "A player close by activity", Colors.BLUE, "material-symbols-light:frame-person")
/**
 * The `PlayerCloseByActivityEntry` is an activity that activates child activities when a viewer is close by.
 *
 * The activity will only activate when the viewer is within the defined range.
 *
 * When the maximum idle duration is reached, the activity will deactivate.
 * If the maximum idle duration is set to 0, then it won't use the timer.
 *
 * ## How could this be used?
 * When the player has to follow the NPC and walks away, let the NPC wander around (or stand still) around the point the player walked away. When the player returns, resume its path.
 *
 * When the npc is walking, and a player comes in range Stand still.
 */
class PlayerCloseByActivityEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    @Help("The range in which the player has to be close by to activate the activity.")
    val range: Double = 10.0,
    @Help("The maximum duration a player can be idle in the same range before the activity deactivates.")
    val maxIdleDuration: Duration = Duration.ofSeconds(30),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity =
        PlayerCloseByActivity(
            children
                .descendants(EntityActivityEntry::class)
                .mapNotNull { it.get() }
                .sortedByDescending { it.priority }
                .map { it.create(context) },
            range,
            maxIdleDuration
        )
}

class PlayerCloseByActivity(
    childActivities: List<EntityActivity>,
    private val range: Double,
    private val maxIdleDuration: Duration,
) : FilterActivity(childActivities) {
    private var trackers = mutableMapOf<UUID, PlayerLocationTracker>()

    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        val trackingPlayers = context.viewers
            .filter { it.isValid }
            .filter { (it.location.toProperty().distanceSqrt(currentLocation) ?: Double.MAX_VALUE) < range * range }

        val trackingPlayerIds = trackingPlayers.map { it.uniqueId }

        trackers.keys.removeAll { it !in trackingPlayerIds }

        trackingPlayers.forEach { player ->
            trackers.computeIfAbsent(player.uniqueId) { PlayerLocationTracker(player.location.toProperty()) }
                .update(player.location.toProperty())
        }

        val canActivate =
            !trackers.all { (_, tracker) -> tracker.isIdle(maxIdleDuration) } &&
                    super.canActivate(context, currentLocation)
        // Cleanup memory
        if (!canActivate) {
            trackers.clear()
        }
        return canActivate
    }

    private class PlayerLocationTracker(
        var location: LocationProperty,
        var lastSeen: Long = System.currentTimeMillis()
    ) {
        fun update(location: LocationProperty) {
            if ((this.location.distanceSqrt(location) ?: Double.MAX_VALUE) < 0.1) return
            this.location = location
            lastSeen = System.currentTimeMillis()
        }

        fun isIdle(maxIdleDuration: Duration): Boolean {
            return System.currentTimeMillis() - lastSeen > maxIdleDuration.toMillis()
        }
    }
}