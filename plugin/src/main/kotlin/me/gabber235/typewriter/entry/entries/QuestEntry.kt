package me.gabber235.typewriter.entry.entries

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.FactListenerSubscription
import me.gabber235.typewriter.entry.PlaceholderEntry
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.inAudience
import me.gabber235.typewriter.entry.listenForFacts
import me.gabber235.typewriter.entry.matches
import me.gabber235.typewriter.entry.quest.trackedQuest
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.asMiniWithResolvers
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

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

private val inactiveObjectiveDisplay by snippet("quest.objective.inactive", "<gray><display></gray>")
private val showingObjectiveDisplay by snippet("quest.objective.showing", "<white><display></white>")

@Tags("objective")
interface ObjectiveEntry : AudienceFilterEntry, PlaceholderEntry {
    @Help("The quest that the objective is a part of.")
    val quest: Ref<QuestEntry>

    @Help("The criteria need to be met for the objective to be able to be shown.")
    val criteria: List<Criteria>

    @Help("The name to display to the player.")
    @Colored
    @Placeholder
    val display: String

    override fun display(): AudienceFilter {
        return ObjectiveAudienceFilter(
            ref(),
            criteria,
        )
    }

    override fun display(player: Player?): String {
        val text = when {
            player == null -> inactiveObjectiveDisplay
            criteria.matches(player) -> showingObjectiveDisplay
            else -> inactiveObjectiveDisplay
        }
        return text.asMiniWithResolvers(parsed("display", display)).asMini().parsePlaceholders(player)
    }
}


class ObjectiveAudienceFilter(
    objective: Ref<ObjectiveEntry>,
    private val criteria: List<Criteria>,
) : AudienceFilter(objective) {
    private val factWatcherSubscriptions = ConcurrentHashMap<UUID, FactListenerSubscription>()

    override fun filter(player: Player): Boolean =
        criteria.matches(player)

    override fun onPlayerAdd(player: Player) {
        super.onPlayerAdd(player)

        factWatcherSubscriptions.compute(player.uniqueId) { _, subscription ->
            subscription?.cancel(player)
            player.listenForFacts(
                (criteria).map { it.fact },
                ::onFactChange,
            )
        }
    }

    private fun onFactChange(player: Player, fact: Ref<ReadableFactEntry>) {
        player.refresh()
    }

    override fun onPlayerRemove(player: Player) {
        super.onPlayerRemove(player)
        factWatcherSubscriptions.remove(player.uniqueId)?.cancel(player)
    }

    override fun dispose() {
        super.dispose()
        factWatcherSubscriptions.forEach { (playerId, subscription) ->
            val player = server.getPlayer(playerId) ?: return@forEach
            subscription.cancel(player)
        }
    }
}

fun Player.trackedShowingObjectives() = trackedQuest()?.let { questShowingObjectives(it) } ?: emptyList()

fun Player.questShowingObjectives(quest: Ref<QuestEntry>) = Query.findWhere<ObjectiveEntry> { objectiveEntry ->
    objectiveEntry.quest == quest && inAudience(objectiveEntry.ref())
}