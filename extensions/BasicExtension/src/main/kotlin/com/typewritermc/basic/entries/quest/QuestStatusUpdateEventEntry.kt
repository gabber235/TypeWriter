package com.typewritermc.basic.entries.quest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.entries.QuestEntry
import com.typewritermc.engine.paper.entry.quest.QuestStatus
import com.typewritermc.engine.paper.events.AsyncQuestStatusUpdate
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
    @Help("When not set, it will trigger for all quests.")
    val quest: Ref<QuestEntry> = emptyRef(),
    @Help("When not set, it will trigger for all statuses.")
    val from: Optional<QuestStatus> = Optional.empty(),
    val to: QuestStatus = QuestStatus.INACTIVE,
) : EventEntry

@EntryListener(QuestStatusUpdateEventEntry::class)
fun onQuestStatusUpdate(event: AsyncQuestStatusUpdate, query: Query<QuestStatusUpdateEventEntry>) {
    query.findWhere {
        (!it.quest.isSet || it.quest == event.quest) &&
                (!it.from.isPresent || it.from.get() == event.from) &&
                it.to == event.to
    } triggerAllFor event.player
}