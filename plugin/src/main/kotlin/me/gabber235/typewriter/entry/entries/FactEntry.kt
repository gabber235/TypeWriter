package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.StaticEntry
import me.gabber235.typewriter.facts.Fact

@Tags("fact")
interface FactEntry : StaticEntry {
	val comment: String

	fun canSave(fact: Fact): Boolean = true
	fun hasExpired(fact: Fact): Boolean = false
}