package me.gabber235.typewriter.entry.dialogue

import me.gabber235.typewriter.entry.RuleEntry

interface DialogueEntry : RuleEntry {
	val text: String
	val triggerdBy: List<String>
}

