package com.typewritermc.basic.entries.quest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.entry.matches
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.snippets.snippet
import com.typewritermc.engine.paper.utils.asMini
import com.typewritermc.engine.paper.utils.asMiniWithResolvers
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed
import org.bukkit.entity.Player
import java.util.*

private val completedObjectiveDisplay by snippet(
    "quest.completable_objective.completed",
    "<green>✔</green> <gray><display></gray>"
)

@Entry(
    "completable_objective",
    "An objective that can show a completed stage",
    Colors.BLUE_VIOLET,
    "fluent:clipboard-checkmark-16-filled"
)
/**
 * The `Completable Objective` entry is an objective that can show a completed stage.
 * It is shown to the player when the show criteria are met, regardless of if the completed criteria are met.
 *
 * ## How could this be used?
 * This is useful when the quest has multiple objectives that can be completed in different orders.
 * For example, the player needs to collect wheat, eggs, and milk to make a cake.
 * The order in which the player collects the items does not matter.
 * But you want to show the player which items they have collected.
 */
class CompletableObjective(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    @Help("The criteria need to be met for the objective to be able to be shown.")
    val showCriteria: List<Criteria> = emptyList(),
    @Help("The criteria to display the objective as completed.")
    val completedCriteria: List<Criteria> = emptyList(),
    override val display: String = "",
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : ObjectiveEntry {
    override val criteria: List<Criteria> get() = showCriteria

    override fun display(player: Player?): String {
        val text = when {
            player == null -> inactiveObjectiveDisplay
            completedCriteria.matches(player) -> completedObjectiveDisplay
            showCriteria.matches(player) -> showingObjectiveDisplay
            else -> inactiveObjectiveDisplay
        }
        return text.asMiniWithResolvers(parsed("display", display)).asMini().parsePlaceholders(player)
    }
}