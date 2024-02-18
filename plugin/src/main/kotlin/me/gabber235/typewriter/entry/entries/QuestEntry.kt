package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.quest.trackedQuest
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.asMiniWithResolvers
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed
import org.bukkit.entity.Player

@Tags("quest")
interface QuestEntry : AudienceFilterEntry, PlaceholderEntry {
    @Help("The name to display to the player.")
    @Colored
    @Placeholder
    val displayName: String

    @Help("When the criteria is met, it considers the quest to be active.")
    val activeCriteria: List<Criteria>

    @Help("When the criteria is met, it considers the quest to be completed.")
    val completedCriteria: List<Criteria>


    override fun display(player: Player?): String = displayName.parsePlaceholders(player)
}

private val inactiveObjectiveDisplay by snippet("quest.objective.inactive", "<gray><name></gray>")
private val showingObjectiveDisplay by snippet("quest.objective.showing", "<white><name></white>")
private val finishedObjectiveDisplay by snippet("quest.objective.finished", "<green>✔</green> <gray><name></gray>")

@Tags("objective")
interface ObjectiveEntry : AudienceFilterEntry, PlaceholderEntry {
    @Help("The quest that the objective is a part of.")
    val quest: Ref<QuestEntry>

    @Help("The criteria need to be met for the objective to be able to be shown.")
    val criteria: List<Criteria>

    @Help("The finished criteria need to be met for the objective to shown as completed. If empty, it will never show as completed.")
    val finishedCriteria: List<Criteria>

    @Help("The name to display to the player.")
    @Colored
    @Placeholder
    val displayName: String

    override fun display(player: Player?): String {
        val text = when {
            player == null -> return displayName.parsePlaceholders(null)
            finishedCriteria.isNotEmpty() && finishedCriteria.matches(player) -> finishedObjectiveDisplay
            criteria.matches(player) -> showingObjectiveDisplay
            else -> inactiveObjectiveDisplay
        }
        return text.asMiniWithResolvers(parsed("name", displayName)).asMini().parsePlaceholders(player)
    }
}

fun Player.trackedShowingObjectives() = trackedQuest()?.let { questShowingObjectives(it) } ?: emptyList()

fun Player.questShowingObjectives(quest: Ref<QuestEntry>) = Query.findWhere<ObjectiveEntry> { objectiveEntry ->
    objectiveEntry.quest == quest && inAudience(objectiveEntry.ref())
}