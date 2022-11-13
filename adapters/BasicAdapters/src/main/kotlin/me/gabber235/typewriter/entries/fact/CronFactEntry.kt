package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.CronExpression
import java.time.LocalDateTime

@Entry("cron_fact", "Saved until a specified date, like (0 0 * * 1)")
data class CronFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	val data: String = "",
) : FactEntry {
	@delegate:Transient
	private val cache: CronExpression? by lazy {
		try {
			CronExpression.createDynamic(data)
		} catch (e: Exception) {
			null
		}
	}

	override fun hasExpired(fact: Fact): Boolean {
		return cache?.nextLocalDateTimeAfter(fact.lastUpdate)?.isBefore(LocalDateTime.now()) == true
	}
}