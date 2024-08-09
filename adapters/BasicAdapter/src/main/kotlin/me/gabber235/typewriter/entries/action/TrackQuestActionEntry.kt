package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.entry.quest.trackQuest
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