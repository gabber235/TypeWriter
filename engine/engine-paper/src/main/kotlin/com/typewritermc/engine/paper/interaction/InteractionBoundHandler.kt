package com.typewritermc.engine.paper.interaction

import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.priority
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionBound
import com.typewritermc.engine.paper.entry.InteractionBoundEntry
import com.typewritermc.engine.paper.entry.entries.Event
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import org.bukkit.entity.Player

class InteractionBoundHandler : TriggerHandler {
    override suspend fun trigger(event: Event, currentInteraction: Interaction?): TriggerContinuation {
        val directTriggers = event.triggers.filterIsInstance<InteractionBoundTrigger>()
        val entries = Query.findWhere<InteractionBoundEntry> { it in event }.toList()

        val triggers = directTriggers + entries.map(BoundTrigger::EntryBoundTrigger)
        val bound = triggers.maxByOrNull { it.priority }?.build(event.player)

        val nextEntries = entries
            .flatMap { it.eventTriggers }
            // Stops infinite loops
            .filter { it !in event }

        val continuations = mutableListOf<TriggerContinuation>()

        if (bound != null) {
            continuations.add(TriggerContinuation.StartInteractionBound(bound))
        }
        if (nextEntries.isNotEmpty()) {
            continuations.add(TriggerContinuation.Append(Event(event.player, event.context, nextEntries)))
        }

        return TriggerContinuation.Multi(continuations)
    }
}

data class InteractionBoundTrigger(val bound: InteractionBound) : EventTrigger, BoundTrigger {
    override val id: String
        get() = "system.interaction.bound"

    override fun build(player: Player): InteractionBound = bound

    override val priority: Int
        get() = bound.priority
}

private interface BoundTrigger {
    val priority: Int

    fun build(player: Player): InteractionBound

    class EntryBoundTrigger(val entry: InteractionBoundEntry) : BoundTrigger {
        override fun build(player: Player): InteractionBound = entry.build(player)

        override val priority: Int
            get() = entry.priority
    }
}