package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.ExpirableFactEntry
import me.gabber235.typewriter.entry.entries.PersistableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.CronExpression
import me.gabber235.typewriter.utils.Icons
import java.time.LocalDateTime

@Entry("cron_fact", "Saved until a specified date, like (0 0 * * 1)", Colors.PURPLE, Icons.CALENDAR_DAYS)
data class CronFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	val cron: CronExpression = CronExpression.default()
) : ExpirableFactEntry, PersistableFactEntry {

	override fun hasExpired(fact: Fact): Boolean {
		return cron.nextLocalDateTimeAfter(fact.lastUpdate).isBefore(LocalDateTime.now())
	}
}