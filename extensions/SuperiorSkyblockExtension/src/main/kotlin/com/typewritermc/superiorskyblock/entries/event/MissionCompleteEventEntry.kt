package com.typewritermc.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.MissionCompleteEvent
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor

@Entry("on_mission_complete", "When a player completes a mission", Colors.YELLOW, "fa6-solid:clipboard-check")
/**
 * The `Mission Complete` event is triggered when a player completes a mission.
 *
 * ## How could this be used?
 *
 * This event could be used to reward players for completing missions.
 */
class MissionCompleteEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val missionName: String = "",
) : EventEntry

@EntryListener(MissionCompleteEventEntry::class)
fun onMissionComplete(event: MissionCompleteEvent, query: Query<MissionCompleteEventEntry>) {
    val player = event.player.asPlayer() ?: return

    query.findWhere { entry -> entry.missionName == event.mission.name }.triggerAllFor(player, context())
}