package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.events.AsyncTrackedQuestUpdate
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@Entry(
    "tracked_objective_audience",
    "Filters an audience based on if they have a tracked objective",
    Colors.MEDIUM_SEA_GREEN,
    "mdi:target-account"
)
/**
 * The `Tracked Objective Audience` entry filters an audience based on if they have a tracked objective.
 * It looks if the player has an objective showing from the quest that is being tracked.
 *
 * ## How could this be used?
 * This could be used to show a boss bar or sidebar based on if a player has an objective showing.
 */
class TrackedObjectiveAudience(
    override val id: String,
    override val name: String,
    override val children: List<Ref<AudienceEntry>>
) : AudienceFilterEntry {
    override fun display(): AudienceFilter = TrackedObjectiveAudienceFilter(
        ref()
    )
}

class TrackedObjectiveAudienceFilter(
    ref: Ref<out AudienceFilterEntry>
) : AudienceFilter(ref), TickableDisplay {
    override fun filter(player: Player): Boolean = player.trackedShowingObjectives().any()

    override fun tick() {
        consideredPlayers.forEach { it.refresh() }
    }

    @EventHandler
    private fun onTrackedQuestUpdate(event: AsyncTrackedQuestUpdate) {
        event.player.refresh()
    }
}