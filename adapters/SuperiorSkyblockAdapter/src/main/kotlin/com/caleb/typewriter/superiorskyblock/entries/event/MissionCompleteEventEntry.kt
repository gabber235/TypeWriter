package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.MissionCompleteEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry

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
    @Help("The name of the mission that needs to be completed")
    val missionName: String = "",
) : EventEntry

@EntryListener(MissionCompleteEventEntry::class)
fun onMissionComplete(event: MissionCompleteEvent, query: Query<MissionCompleteEventEntry>) {
    val player = event.player.asPlayer() ?: return

    query.findWhere { entry -> entry.missionName == event.mission.name } triggerAllFor player
}