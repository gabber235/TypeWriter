package me.gabber235.typewriter.entry.event.entries

import me.gabber235.typewriter.entry.event.EventEntry

class NpcEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val identifier: String = "",
) : EventEntry