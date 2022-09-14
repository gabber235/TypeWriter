package me.gabber235.typewriter.extensions.superiorisland

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.event.entries.IslandCreateEventEntry
import me.gabber235.typewriter.interaction.InteractionHandler

object SuperiorIslandExtension {
	fun init() {
		plugin.listen<IslandCreateEvent> { event ->
			val player = event.player.asPlayer() ?: return@listen
			val triggers = EntryDatabase.findEventEntries(IslandCreateEventEntry::class) { true }
				.flatMap { it.triggers }
			if (triggers.isEmpty()) return@listen

			InteractionHandler.startInteractionAndTrigger(player, triggers)
		}
	}
}