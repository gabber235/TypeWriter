package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandInviteEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.*
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("on_island_invite", "When a player is invited to a Skyblock island", Colors.YELLOW, Icons.ENVELOPE)
class IslandInviteEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	@Help("The triggers for the player who got invited")
	val inviteeTriggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(IslandInviteEventEntry::class)
fun onInvite(event: IslandInviteEvent, query: Query<IslandInviteEventEntry>) {
	val player = event.player?.asPlayer() ?: return
	val target = event.target?.asPlayer()

	val entries = query.find()

	entries triggerAllFor player

	if (target?.isOnline == true) {
		entries.flatMap { it.inviteeTriggers } triggerEntriesFor target
	}
}

