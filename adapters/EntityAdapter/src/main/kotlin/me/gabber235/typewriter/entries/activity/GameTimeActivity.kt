package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.descendants
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.FilterActivity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.TaskContext
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.priority
import java.util.*

@Entry("game_time_activity", "A game time activity", Colors.BLUE, "bi:clock-fill")
/**
 * The `GameTimeActivityEntry` is an activity that activates child activities at specific times in the game.
 *
 * The total time of a Minecraft day is `24000` ticks.
 * Some examples of times are:
 * ------------------------------
 * | Time of day | Ticks        |
 * |-------------|--------------|
 * | Dawn        | 0            |
 * | Noon        | 6000         |
 * | Dusk        | 12000        |
 * | Midnight    | 18000        |
 * ------------------------------
 *
 * ## How could this be used?
 *
 * This can be used to create schedules for npc's to follow.
 * For example, going to the market in the morning, going to the tavern at night.
 */
class GameTimeActivityEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    val activeTimes: List<GameTimeRange> = emptyList(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity = GameTimeActivity(
        children
            .descendants(EntityActivityEntry::class)
            .mapNotNull { it.get() }
            .sortedByDescending { it.priority }
            .map { it.create(context) },
        activeTimes
    )
}

class GameTimeRange(
    val start: Long = 0,
    val end: Long = 0,
) {
    operator fun contains(time: Long): Boolean {
        return time in start until end
    }
}

class GameTimeActivity(
    children: List<EntityActivity>,
    private val activeTimes: List<GameTimeRange>,
) : FilterActivity(children) {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        val worldTime = currentLocation.toLocation().world?.time ?: return false
        val activationTime = worldTime % 24000

        return activeTimes.any { activationTime in it } && super.canActivate(context, currentLocation)
    }
}