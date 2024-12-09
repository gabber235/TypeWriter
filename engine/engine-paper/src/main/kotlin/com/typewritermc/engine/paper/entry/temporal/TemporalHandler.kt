package com.typewritermc.engine.paper.entry.temporal

import com.typewritermc.engine.paper.entry.entries.Event
import com.typewritermc.engine.paper.interaction.Interaction
import com.typewritermc.engine.paper.interaction.TriggerContinuation
import com.typewritermc.engine.paper.interaction.TriggerHandler

class TemporalHandler : TriggerHandler {
    override suspend fun trigger(event: Event, currentInteraction: Interaction?): TriggerContinuation {
        if (TemporalStopTrigger in event && currentInteraction is TemporalInteraction) {
            return TriggerContinuation.Multi(
                TriggerContinuation.EndInteraction,
                TriggerContinuation.Append(Event(event.player, currentInteraction.eventTriggers)),
            )
        }

        return tryStartTemporalInteraction(event)
    }

    private fun tryStartTemporalInteraction(event: Event): TriggerContinuation {
        val triggers = event.triggers.filterIsInstance<TemporalStartTrigger>()
        if (triggers.isEmpty()) return TriggerContinuation.Done

        val trigger = triggers.maxBy { it.priority }
        return TriggerContinuation.StartInteraction(
            TemporalInteraction(
                trigger.pageId,
                event.player,
                trigger.eventTriggers,
                trigger.settings
            )
        )
    }
}