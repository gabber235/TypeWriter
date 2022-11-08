package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.event.EventEntry

@Entry("run_command", "When a player runs a command")
class RunCommandEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val command: String = "",
) : EventEntry