package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.bgsoftware.superiorskyblock.api.events.IslandInviteEvent
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer
import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.SnakeCase
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.interaction.Interaction
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*

@Entry("on_invite_to_island", "[SuperiorSkyblock] When a player is invited to a Skyblock island", Colors.YELLOW, Icons.ENVELOPE)
class IslandInviteEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	val inviteeTriggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(IslandInviteEventEntry::class)
fun onInvite(event: IslandInviteEvent, query: Query<IslandInviteEventEntry>) {

	var player: Player = event.player?.asPlayer() ?: return

	var target: Player = event.target?.asPlayer() ?: return

	val entries = query.find()

	entries triggerAllFor player


	if(target.isOnline) {

		entries.flatMap { it.inviteeTriggers }.map { s -> EntryTrigger(s) }.let { triggers ->
			InteractionHandler.startInteractionAndTrigger(target, triggers) }

	}

}

