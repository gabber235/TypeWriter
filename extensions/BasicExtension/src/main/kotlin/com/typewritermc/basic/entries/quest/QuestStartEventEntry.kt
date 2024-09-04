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

@Entry("quest_start_event", "Triggered when a quest is started for a player", Colors.YELLOW, "mdi:notebook-plus")
/**
 * The `Quest Start Event` entry is triggered when a quest is started for a player.
 *
 * When no quest is referenced, it will trigger for all quests.
 *
 * ## How could this be used?
 * This could be used to show a title or notification to a player when a quest is started.
 */
class QuestStartEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("When not set, it will trigger for all quests.")
    val quest: Ref<QuestEntry> = emptyRef()
) : EventEntry

@EntryListener(QuestStartEventEntry::class)
fun onQuestStart(event: AsyncQuestStatusUpdate, query: Query<QuestStartEventEntry>) {
    if (event.to != QuestStatus.ACTIVE) return

    query.findWhere {
        !it.quest.isSet || it.quest == event.quest
    } triggerAllFor event.player
}