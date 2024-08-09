package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.entry.entries.TickableDisplay
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.utils.CronExpression
import org.bukkit.entity.Player
import java.time.LocalDateTime

@Entry(
    "cron_audience",
    "Filters an audience based if the time matches a cron expression",
    Colors.MEDIUM_SEA_GREEN,
    "mdi:calendar-clock"
)
/**
 * The `Cron Audience` entry filters an audience based on if the real-world time matches a cron expression.
 * This will use the server's time, not the player's time.
 *
 * ## How could this be used?
 * This could be used for limited time events, like a holiday.
 */
class CronAudience(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    @Help("The cron expression to filter the audience by.")
    // The <Link to="https://www.netiq.com/documentation/cloud-manager-2-5/ncm-reference/data/bexyssf.html">Cron Expression</Link> when the fact expires.
    val cron: CronExpression = CronExpression.default(),
) : AudienceFilterEntry {
    override fun display(): AudienceFilter {
        return CronAudienceDisplay(ref(), cron)
    }
}

class CronAudienceDisplay(
    ref: Ref<out AudienceFilterEntry>,
    private val cron: CronExpression,
) : AudienceFilter(ref), TickableDisplay {
    private var lastUpdate: LocalDateTime = LocalDateTime.now()
    val active: Boolean
        get() {
            val now = LocalDateTime.now()
            val previousMinute = now.minusMinutes(1)
            return cron.nextLocalDateTimeAfter(previousMinute).isBefore(now)
        }

    override fun filter(player: Player): Boolean = active

    override fun tick() {
        val newTime = LocalDateTime.now()
        // If the minute/second has changed, we need to update the last update time
        if (cron.withSeconds && newTime.second == lastUpdate.second) return
        if (!cron.withSeconds && newTime.minute == lastUpdate.minute) return
        lastUpdate = newTime

        val active = active
        consideredPlayers.forEach { it.updateFilter(active) }
    }
}