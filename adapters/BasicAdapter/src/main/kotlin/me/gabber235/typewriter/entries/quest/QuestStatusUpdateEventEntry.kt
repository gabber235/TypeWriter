package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.entry.quest.QuestStatus
import me.gabber235.typewriter.events.AsyncQuestStatusUpdate
import java.util.*

@Entry(
    "quest_status_update_event",
    "Triggered when a quest status is updated for a player",
    Colors.YELLOW,
    "mdi:notebook-edit"
)
/**
 * The `Quest Status Update Event` entry is triggered when a quest status is updated for a player.
 *
 * When no quest is referenced, it will trigger for all quests.
 *
 * ## How could this be used?
 * This could be used to show a title or notification to a player when a quest status is updated.
 */
class QuestStatusUpdateEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("When not set it will trigger for all quests.")
    val quest: Ref<QuestEntry> = emptyRef(),
    @Help("When not set it will trigger for all statuses.")
    val from: Optional<QuestStatus> = Optional.empty(),
    @Help("The status the quest is updated to.")
    val to: QuestStatus
) : EventEntry

@EntryListener(QuestStatusUpdateEventEntry::class)
fun onQuestStatusUpdate(event: AsyncQuestStatusUpdate, query: Query<QuestStatusUpdateEventEntry>) {
    query.findWhere {
        (!it.quest.isSet || it.quest == event.quest) &&
                (!it.from.isPresent || it.from.get() == event.from) &&
                it.to == event.to
    } triggerAllFor event.player
}