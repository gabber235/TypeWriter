package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.entry.quest.isQuestTracked
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.events.AsyncTrackedQuestUpdate
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@Entry(
    "tracked_quest_audience",
    "Filters an audience based on if they have a quest tracked",
    Colors.MEDIUM_SEA_GREEN,
    "mdi:notebook-heart"
)
/**
 * The `Tracked Quest Audience` entry filters an audience based on if they have a quest tracked.
 *
 * If no quest is referenced, it will filter based on if any quest is tracked.
 *
 * ## How could this be used?
 *
 * This could be used to show a boss bar or sidebar based on if a player has a quest tracked.
 */
class TrackedQuestAudience(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("When not set it will filter based on if any quest is tracked.")
    val quest: Ref<QuestEntry> = emptyRef(),
) : AudienceFilterEntry {
    override fun display(): AudienceFilter = TrackedQuestAudienceFilter(
        ref(),
        quest
    )
}

class TrackedQuestAudienceFilter(
    ref: Ref<out AudienceFilterEntry>,
    private val quest: Ref<QuestEntry>
) : AudienceFilter(ref) {
    override fun filter(player: Player): Boolean = player.isQuestTracked(quest)

    @EventHandler
    private fun onTrackedQuestUpdate(event: AsyncTrackedQuestUpdate) {
        if (quest.isSet) {
            event.player.updateFilter(event.to == quest)
            return
        }

        event.player.updateFilter(event.to != null)
    }
}