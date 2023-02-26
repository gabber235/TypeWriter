package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandInviteEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("on_invite_to_island", "[SuperiorSkyblock] When a player is invited to a Skyblock island", Colors.YELLOW, Icons.ENVELOPE)
class IslandInviteEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
    @Triggers
    @EntryIdentifier(TriggerableEntry::class)
    @Help("The triggers to run on behalf of the player who was invited")
    val inviteeTriggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(IslandInviteEventEntry::class)
fun onInvite(event: IslandInviteEvent, query: Query<IslandInviteEventEntry>) {

    val player: Player = event.player?.asPlayer() ?: return

    val target: Player = event.target?.asPlayer() ?: return

    val entries = query.find()

    entries triggerAllFor player


    if (target.isOnline) {

        entries.flatMap { it.inviteeTriggers }.map { s -> EntryTrigger(s) }.let { triggers ->
            query.find() triggerAllFor player
        }

    }

}

