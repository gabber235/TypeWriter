package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.facts.Fact

interface FactEntry : Entry {
	val comment: String

	fun canSave(fact: Fact): Boolean = true
	fun hasExpired(fact: Fact): Boolean = false
}