package me.gabber235.typewriter.entries.activity

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.ActivityContext
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.SingleChildActivity
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.GenericEntityActivityEntry
import me.gabber235.typewriter.utils.logErrorIfNull
import java.util.*

@Entry("game_time_activity", "A game time activity", Colors.PALATINATE_BLUE, "bi:clock-fill")
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
    val world: String = "",
    val activities: List<GameTimedActivity> = emptyList(),
    val defaultActivity: Ref<out EntityActivityEntry> = emptyRef(),
) : GenericEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<ActivityContext> {
        return GameTimeActivity(
            world,
            activities,
            defaultActivity,
            currentLocation,
        )
    }
}

class GameTimeRange(
    val start: Long = 0,
    val end: Long = 0,
) {
    operator fun contains(time: Long): Boolean {
        return time in start until end
    }
}

class GameTimedActivity(
    val time: GameTimeRange,
    val activity: Ref<out EntityActivityEntry>,
) {
    operator fun contains(time: Long): Boolean {
        return time in this.time
    }
}

class GameTimeActivity(
    private val world: String,
    private val activities: List<GameTimedActivity>,
    private val defaultActivity: Ref<out EntityActivityEntry>,
    startLocation: LocationProperty,
) : SingleChildActivity<ActivityContext>(startLocation) {
    override fun currentChild(context: ActivityContext): Ref<out EntityActivityEntry> {
        val world =
            server.getWorld(world).logErrorIfNull("Could not find world '$world'") ?: return defaultActivity

        val worldTime = world.time % 24000
        return activities.firstOrNull { it.contains(worldTime) }
            ?.activity
            ?: defaultActivity
    }
}