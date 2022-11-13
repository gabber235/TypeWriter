package me.gabber235.typewriter.entry.event.entries

import me.gabber235.typewriter.entry.entries.EventEntry

class IslandCreateEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
) : EventEntry