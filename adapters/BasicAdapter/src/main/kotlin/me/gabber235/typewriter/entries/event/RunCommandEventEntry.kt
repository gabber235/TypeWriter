package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.CustomCommandEntry
import me.gabber235.typewriter.utils.Icons

@Entry("on_run_command", "When a player runs a custom command", Colors.YELLOW, Icons.TERMINAL)
class RunCommandEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	override val command: String = "",
) : CustomCommandEntry

