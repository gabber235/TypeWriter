package me.gabber235.typewrite.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons

@Entry("island_create", "When a player creates an island", Colors.YELLOW, Icons.TERMINAL)
class IslandCreateEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(IslandCreateEventEntry::class)
fun onIslandCreate(event: IslandCreateEvent, query: Query<IslandCreateEventEntry>) {
	val player = event.player.asPlayer() ?: return

	query.find() triggerAllFor player
}