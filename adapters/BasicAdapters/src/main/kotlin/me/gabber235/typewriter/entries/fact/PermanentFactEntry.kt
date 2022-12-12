package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.utils.Icons

@Entry("permanent_fact", "Saved permanently, it never gets removed", Colors.PURPLE, Icons.DATABASE)
data class PermanentFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
) :
	FactEntry
