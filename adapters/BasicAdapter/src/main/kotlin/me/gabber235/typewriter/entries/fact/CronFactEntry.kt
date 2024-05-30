package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.ExpirableFactEntry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.PersistableFactEntry
import me.gabber235.typewriter.facts.FactData
import me.gabber235.typewriter.facts.FactId
import me.gabber235.typewriter.utils.CronExpression
import java.time.LocalDateTime

@Entry("cron_fact", "Saved until a specified date, like (0 0 * * 1)", Colors.PURPLE, "mingcute:calendar-time-add-fill")
/**
 * A [fact](/docs/facts) that is saved until a specified date, like (0 0 \* \* 1).
 *
 * ## How could this be used?
 *
 * This fact could be used to create weekly rewards, which are reset every week. Or to simulate the opening hours of a shop.
 */
class CronFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("The cron expression when the fact expires.")
    // The <Link to="https://www.netiq.com/documentation/cloud-manager-2-5/ncm-reference/data/bexyssf.html">Cron Expression</Link> when the fact expires.
    val cron: CronExpression = CronExpression.default(),
) : ExpirableFactEntry, PersistableFactEntry {
    override fun hasExpired(id: FactId, data: FactData): Boolean {
        return cron.nextLocalDateTimeAfter(data.lastUpdate).isBefore(LocalDateTime.now())
    }
}