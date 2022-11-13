package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.facts.Fact

@Entry("session_fact", "Saved until a player logouts of the server")
data class SessionFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
) : FactEntry {
	override fun canSave(fact: Fact): Boolean = false
}