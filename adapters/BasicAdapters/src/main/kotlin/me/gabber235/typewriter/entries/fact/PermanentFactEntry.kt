package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.FactEntry

@Entry("permanent_fact", "Saved permanently, it never gets removed")
data class PermanentFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
) :
	FactEntry
