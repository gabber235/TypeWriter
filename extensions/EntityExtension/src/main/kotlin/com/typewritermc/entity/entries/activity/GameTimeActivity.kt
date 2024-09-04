package com.typewritermc.entity.entries.activity

import lirand.api.extensions.server.server
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entity.ActivityContext
import com.typewritermc.engine.paper.entry.entity.EntityActivity
import com.typewritermc.engine.paper.entry.entity.PositionProperty
import com.typewritermc.engine.paper.entry.entity.SingleChildActivity
import com.typewritermc.engine.paper.entry.entries.EntityActivityEntry
import com.typewritermc.engine.paper.entry.entries.GenericEntityActivityEntry
import com.typewritermc.engine.paper.utils.logErrorIfNull

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
        currentLocation: PositionProperty
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
    startLocation: PositionProperty,
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