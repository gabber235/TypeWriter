package com.typewritermc.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandInviteEvent
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import com.typewritermc.engine.paper.entry.triggerEntriesFor

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
    val inviteeTriggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry

@EntryListener(IslandInviteEventEntry::class)
fun onInvite(event: IslandInviteEvent, query: Query<IslandInviteEventEntry>) {
    val player = event.player?.asPlayer() ?: return
    val target = event.target?.asPlayer()

    val entries = query.find()

    entries.triggerAllFor(player, context())

    if (target?.isOnline == true) {
        entries.flatMap { it.inviteeTriggers }.triggerEntriesFor(target, context())
    }
}

