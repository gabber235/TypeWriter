package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.entry.matches
import me.gabber235.typewriter.entry.ref
import org.bukkit.entity.Player

@Entry("criteria_audience", "An audience filter based on criteria", Colors.MEDIUM_SEA_GREEN, "fa-solid:filter")
/**
 * The `Criteria Audience` entry filters an audience based on criteria.
 *
 * ## How could this be used?
 * This could be used to show a boss bar or npc when the player is in a certain quest stage.
 */
class CriteriaAudience(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    val criteria: List<Criteria> = emptyList()
) : AudienceFilterEntry{
    override fun display(): AudienceFilter = CriteriaAudienceFilter(ref(), criteria)
}

class CriteriaAudienceFilter(
    ref: Ref<out AudienceFilterEntry>,
    val criteria: List<Criteria>,
) : AudienceFilter(ref) {
    override fun filter(player: Player): Boolean = criteria matches player
}