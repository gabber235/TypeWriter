package com.typewritermc.engine.paper.entry.action

import com.typewritermc.core.entries.Query
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.Event
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.interaction.TriggerContinuation
import com.typewritermc.engine.paper.interaction.TriggerHandler

class ActionHandler : TriggerHandler {
    override suspend fun trigger(event: Event, currentInteraction: Interaction?): TriggerContinuation {
        val actions = Query.findWhere<ActionEntry> { it in event }.toList()
        if (actions.isEmpty()) return TriggerContinuation.Done

        val events = actions.mapNotNull { action ->
            val trigger = ActionTrigger(event.player, event.context, action)
            with(action) {
                trigger.execute()
            }

            if (trigger.automaticModifiers) {
                trigger.applyModifiers()
            }

            if (!trigger.automaticTriggering) return@mapNotNull null
            val nextTriggers = action.eventTriggers.filter { it !in event } // Stops infinite loops
            if (nextTriggers.isEmpty()) return@mapNotNull null

            Event(event.player, trigger.buildNewContext(), nextTriggers)
        }

        if (events.isEmpty()) return TriggerContinuation.Done
        return TriggerContinuation.Append(events)
    }
}