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
import java.time.Duration
import java.time.LocalDateTime

@Entry("timed_fact", "Saved for a specified duration, like 20 minutes", Colors.PURPLE, "bi:stopwatch-fill")
/**
 * This fact is stored for a certain amount of time.
 * After that time, it is reset.
 *
 * ## How could this be used?
 *
 * This fact could serve as a timer, and when the fact runs out, it could be used to trigger an action.
 */
class TimedFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("The duration after which the fact expires.")
    val duration: Duration = Duration.ZERO,
) : ExpirableFactEntry, PersistableFactEntry {
    override fun hasExpired(id: FactId, data: FactData): Boolean {
        return LocalDateTime.now().isAfter(data.lastUpdate.plus(duration))
    }
}
