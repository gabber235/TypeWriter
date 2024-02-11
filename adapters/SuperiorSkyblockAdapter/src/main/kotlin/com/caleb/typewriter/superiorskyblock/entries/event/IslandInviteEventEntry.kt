package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandInviteEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry

@Entry("on_island_invite", "When a player is invited to a Skyblock island", Colors.YELLOW, "fa6-solid:envelope")
/**
 * The `Island Invite Event` is an event that is triggered when a player is invited to an island.
 *
 * ## How could this be used?
 *
 * This event could be used to give the player who got invited a reward.
 */
class IslandInviteEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The triggers for the player who got invited")
    val inviteeTriggers: List<Ref<TriggerableEntry>> = emptyList(),
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

