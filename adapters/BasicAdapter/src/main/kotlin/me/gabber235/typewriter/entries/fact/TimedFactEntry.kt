package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.ExpirableFactEntry
import me.gabber235.typewriter.entry.entries.PersistableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.time.Duration
import java.time.LocalDateTime

@Entry("timed_fact", "Saved for a specified duration, like 20 minutes", Colors.PURPLE, Icons.STOPWATCH)
data class TimedFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	val duration: Duration = Duration.ZERO,
) : ExpirableFactEntry, PersistableFactEntry {
	override fun hasExpired(fact: Fact): Boolean {
		return LocalDateTime.now().isAfter(fact.lastUpdate.plus(duration))
	}
}
