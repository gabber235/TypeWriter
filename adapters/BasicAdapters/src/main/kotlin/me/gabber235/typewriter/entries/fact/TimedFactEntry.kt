package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.DurationParser
import java.time.Duration
import java.time.LocalDateTime
import kotlin.time.toJavaDuration

@Entry("timed_fact", "Saved for a specified duration, like 20 minutes")
data class TimedFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	val data: String = "",
) : FactEntry {
	@delegate:Transient
	private val cache: Duration by lazy {
		try {
			DurationParser.parse(data).toJavaDuration()
		} catch (e: Exception) {
			Duration.ZERO
		}
	}

	override fun hasExpired(fact: Fact): Boolean {
		return LocalDateTime.now().isAfter(fact.lastUpdate.plus(cache))
	}
}
