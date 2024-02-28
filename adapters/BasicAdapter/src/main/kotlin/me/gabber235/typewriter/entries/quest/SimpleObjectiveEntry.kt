package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.ObjectiveEntry
import me.gabber235.typewriter.entry.entries.QuestEntry

@Entry("objective", "An objective definition", Colors.BLUE_VIOLET, "streamline:target-solid")
/**
 * The `Objective` entry is a tasks that the player can complete.
 * It is mainly for displaying the progress to a player.
 *
 * It is **not** necessary to use this entry for objectives.
 * It is just a visual novelty.
 *
 * The entry filters the audience based on if the objective is active.
 * It will show the objective to the player if the criteria are met.
 *
 * ## How could this be used?
 * This could be used to show the players what they need to do.
 */
class SimpleObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val criteria: List<Criteria> = emptyList(),
    override val display: String = "",
) : ObjectiveEntry
