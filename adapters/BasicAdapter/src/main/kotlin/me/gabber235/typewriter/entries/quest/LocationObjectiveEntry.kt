package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.ObjectiveEntry
import me.gabber235.typewriter.entry.entries.QuestEntry
import org.bukkit.Location
import java.util.*

@Entry("location_objective", "A location objective definition", Colors.BLUE_VIOLET, "streamline:target-solid")
/**
 * The `LocationObjective` entry is a task that the player can complete by reaching a specific location.
 * It is mainly for displaying the progress to a player.
 *
 * It is **not** necessary to use this entry for objectives.
 * It is just a visual novelty.
 *
 * The entry filters the audience based on if the objective is active.
 * It will show the objective to the player if the criteria are met.
 *
 * It allows path streams to be displayed to the target location.
 *
 * ## How could this be used?
 * This could be used to guide the players to a specific location.
 * Showing the players where they need to go with a path stream.
 */
class LocationObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val criteria: List<Criteria> = emptyList(),
    override val display: String = "",
    val targetLocation: Location,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : ObjectiveEntry