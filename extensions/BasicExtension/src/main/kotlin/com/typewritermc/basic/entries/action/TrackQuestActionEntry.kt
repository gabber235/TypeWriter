package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.*
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.QuestEntry
import com.typewritermc.engine.paper.entry.quest.trackQuest
import org.bukkit.entity.Player

@Entry("track_quest", "Start tracking a quest for a player", Colors.RED, "material-symbols:bookmark")
/**
 * The `Track Quest Action` is an action that tracks a quest when triggered.
 *
 * Though quests are tracked automatically, this action can force a quest to be tracked.
 *
 * ## How could this be used?
 *
 * Start tacking a quest, so it displays in the player's quest tracker.
 */
class TrackQuestActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val quest: Ref<QuestEntry> = emptyRef(),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        player trackQuest quest
    }
}