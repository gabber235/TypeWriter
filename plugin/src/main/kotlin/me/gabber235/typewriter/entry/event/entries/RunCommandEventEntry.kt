package me.gabber235.typewriter.entry.event.entries

import me.gabber235.typewriter.entry.event.EventEntry

class RunCommandEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val command: String = "",
) : EventEntry