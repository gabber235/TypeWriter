package com.typewritermc.engine.paper.entry.action

import com.typewritermc.core.entries.Query
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.EntryTrigger
import com.typewritermc.engine.paper.entry.entries.Event
import com.typewritermc.engine.paper.interaction.Interaction
import com.typewritermc.engine.paper.interaction.TriggerContinuation
import com.typewritermc.engine.paper.interaction.TriggerHandler

class ActionHandler : TriggerHandler {
    override suspend fun trigger(event: Event, currentInteraction: Interaction?): TriggerContinuation {
        val actions = Query.findWhere<ActionEntry> { it in event }.toList()
        if (actions.isEmpty()) return TriggerContinuation.Done
        actions.forEach { it.execute(event.player) }

        val newTriggers =
            actions.flatMap { it.eventTriggers }.filter {
                it !in event
            }.toList() // Stops infinite loops

        if (newTriggers.isEmpty()) return TriggerContinuation.Done
        return TriggerContinuation.Append(Event(event.player, newTriggers))
    }
}