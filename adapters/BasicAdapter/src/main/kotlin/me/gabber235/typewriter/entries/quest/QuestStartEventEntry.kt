package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.entry.quest.QuestStatus
import me.gabber235.typewriter.events.AsyncQuestStatusUpdate

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
    @Help("When not set it will trigger for all quests.")
    val quest: Ref<QuestEntry> = emptyRef()
) : EventEntry

@EntryListener(QuestStartEventEntry::class)
fun onQuestStart(event: AsyncQuestStatusUpdate, query: Query<QuestStartEventEntry>) {
    if (event.to != QuestStatus.ACTIVE) return

    query.findWhere {
        !it.quest.isSet || it.quest == event.quest
    } triggerAllFor event.player
}