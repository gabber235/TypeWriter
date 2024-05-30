package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.entry.matches
import me.gabber235.typewriter.entry.quest.QuestStatus
import me.gabber235.typewriter.entry.quest.isQuestActive
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.events.AsyncQuestStatusUpdate
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@Entry("quest", "A quest definition", Colors.MEDIUM_PURPLE, "material-symbols:book-2")
/**
 * The `Quest` entry is a collection of tasks that the player can complete.
 * It is mainly for displaying the progress to a player.
 *
 * It is **not** necessary to use this entry for quests.
 * It is just a visual novelty.
 *
 * The entry filters the audience based on if the quest is active.
 *
 * ## How could this be used?
 * This could be used to show the progress of a quest to a player.
 * Connect objectives to the quest to show a player what they need to do.
 */
class SimpleQuestEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val displayName: String,
    @Help("When the criteria is met, it considers the quest to be active.")
    val activeCriteria: List<Criteria>,

    @Help("When the criteria is met, it considers the quest to be completed.")
    val completedCriteria: List<Criteria>,
) : QuestEntry {
    override val facts: List<Ref<ReadableFactEntry>>
        get() = (activeCriteria + completedCriteria).map { it.fact }
    override fun questStatus(player: Player): QuestStatus {
        return when {
            completedCriteria matches player -> QuestStatus.COMPLETED
            activeCriteria matches player -> QuestStatus.ACTIVE
            else -> QuestStatus.INACTIVE
        }
    }
}
