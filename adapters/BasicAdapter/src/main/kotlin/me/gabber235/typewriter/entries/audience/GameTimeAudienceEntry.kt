package me.gabber235.typewriter.entries.audience

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.logger
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerChangedWorldEvent

@Entry(
    "game_time_audience",
    "Filters an audience based on the game time",
    Colors.MEDIUM_SEA_GREEN,
    "bi:clock-fill"
)
/**
 * The `GameTimeAudienceEntry` filters an audience based on the game time.
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
 * This can be used to only allow passage to a certain area at night.
 * For example, a vampire castle that only opens its doors at night.
 * Or a market that only opens during the day.
 */
class GameTimeAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    val world: String = "",
    val activeTimes: List<GameTimeRange> = emptyList(),
    override val inverted: Boolean = false,
) : AudienceFilterEntry, Invertible {
    override fun display(): AudienceFilter = GameTimeAudienceFilter(ref(), world, activeTimes)
}

class GameTimeAudienceFilter(
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

class GameTimeRange(
    val start: Long = 0,
    val end: Long = 0,
) {
    operator fun contains(time: Long): Boolean {
        return time in start until end
    }
}