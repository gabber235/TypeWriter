package me.gabber235.typewriter.entries.activity

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.descendants
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.logger
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerChangedWorldEvent
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
    val world: String = "",
    val activeTimes: List<GameTimeRange> = emptyList(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity = GameTimeActivity(
        ref(),
        children
            .descendants(EntityActivityEntry::class)
            .mapNotNull { it.get() }
            .sortedByDescending { it.priority }
            .map { it.create(context) },
    )

    override fun display(): AudienceFilter = GameTimeFilter(ref(), world, activeTimes)
}

class GameTimeRange(
    val start: Long = 0,
    val end: Long = 0,
) {
    operator fun contains(time: Long): Boolean {
        return time in start until end
    }
}

class GameTimeFilter(
    val ref: Ref<out AudienceFilterEntry>,
    private val world: String,
    private val activeTimes: List<GameTimeRange>,
) : AudienceFilter(ref), TickableDisplay {
    override fun filter(player: Player): Boolean {
        if (player.world.name != world) return false
        val worldTime = player.world.time % 24000
        return activeTimes.any { worldTime in it }
    }

    @EventHandler
    private fun onWorldChange(event: PlayerChangedWorldEvent) {
        event.player.refresh()
    }

    override fun tick() {
        val world = server.getWorld(world)
        if (world == null) {
            logger.warning("World '${this.world}' does not exist, $ref will not work.")
            return
        }

        val isActive = activeTimes.any { world.time % 24000 in it }
        if (isActive && consideredPlayers.isNotEmpty() && players.isEmpty()) {
            consideredPlayers.forEach { it.refresh() }
        } else if (!isActive && players.isNotEmpty()) {
            players.forEach { it.updateFilter(false) }
        }
    }
}

class GameTimeActivity(
    val ref: Ref<out GameTimeActivityEntry>,
    children: List<EntityActivity>,
) : FilterActivity(children) {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        return ref.canActivateFor(context)
    }
}