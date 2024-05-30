package me.gabber235.typewriter.entry.entries

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.quest.QuestStatus
import me.gabber235.typewriter.entry.quest.isQuestActive
import me.gabber235.typewriter.entry.quest.trackQuest
import me.gabber235.typewriter.entry.quest.trackedQuest
import me.gabber235.typewriter.events.AsyncQuestStatusUpdate
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.asMiniWithResolvers
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Tags("quest")
interface QuestEntry : AudienceFilterEntry, PlaceholderEntry {
    @Help("The name to display to the player.")
    @Colored
    @Placeholder
    val displayName: String

    val facts: List<Ref<ReadableFactEntry>> get() = emptyList()
    fun questStatus(player: Player): QuestStatus

    override fun display(player: Player?): String = displayName.parsePlaceholders(player)
    override fun display(): AudienceFilter = QuestAudienceFilter(
        ref()
    )
}

class QuestAudienceFilter(
    private val quest: Ref<QuestEntry>
) : AudienceFilter(quest) {
    override fun filter(player: Player): Boolean = player isQuestActive quest

    @EventHandler
    fun onQuestStatusUpdate(event: AsyncQuestStatusUpdate) {
        if (event.quest != quest) return
        event.player.updateFilter(event.to == QuestStatus.ACTIVE)
    }
}

private val inactiveObjectiveDisplay by snippet("quest.objective.inactive", "<gray><display></gray>")
private val showingObjectiveDisplay by snippet("quest.objective.showing", "<white><display></white>")

@Tags("objective")
interface ObjectiveEntry : AudienceFilterEntry, PlaceholderEntry, PriorityEntry {
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
    private val objective: Ref<ObjectiveEntry>,
    private val criteria: List<Criteria>,
) : AudienceFilter(objective) {
    private val factWatcherSubscriptions = ConcurrentHashMap<UUID, FactListenerSubscription>()

    override fun filter(player: Player): Boolean =
        criteria.matches(player)

    override fun onPlayerAdd(player: Player) {
        factWatcherSubscriptions.compute(player.uniqueId) { _, subscription ->
            subscription?.cancel(player)
            return@compute player.listenForFacts(
                (criteria).map { it.fact },
                ::onFactChange,
            )
        }

        super.onPlayerAdd(player)
    }

    private fun onFactChange(player: Player, fact: Ref<ReadableFactEntry>) {
        player.refresh()
    }

    override fun onPlayerRemove(player: Player) {
        super.onPlayerRemove(player)
        factWatcherSubscriptions.remove(player.uniqueId)?.cancel(player)
    }

    override fun onPlayerFilterAdded(player: Player) {
        super.onPlayerFilterAdded(player)
        val quest = objective.get()?.quest ?: return

        if (!player.isQuestActive(quest)) {
            return
        }

        if (player.trackedQuest() == null) {
            player.trackQuest(quest)
            return
        }
        // If the player has a tracked quest, we only want to override it if the new quest has a higher priority.
        val highestObjectivePriority = player.trackedShowingObjectives().maxOfOrNull { it.priority } ?: 0
        if (objective.priority < highestObjectivePriority) {
            return
        }

        player.trackQuest(quest)
    }

    override fun dispose() {
        super.dispose()
        factWatcherSubscriptions.forEach { (playerId, subscription) ->
            val player = server.getPlayer(playerId) ?: return@forEach
            subscription.cancel(player)
        }
    }
}

fun Player.trackedShowingObjectives() = trackedQuest()?.let { questShowingObjectives(it) } ?: emptySequence()

fun Player.questShowingObjectives(quest: Ref<QuestEntry>) = Query.findWhere<ObjectiveEntry> { objectiveEntry ->
    objectiveEntry.quest == quest && inAudience(objectiveEntry.ref())
}