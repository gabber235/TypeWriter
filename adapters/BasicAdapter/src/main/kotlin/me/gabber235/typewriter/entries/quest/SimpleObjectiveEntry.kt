package me.gabber235.typewriter.entries.quest

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.*
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Entry("objective", "An objective definition", Colors.BLUE_VIOLET, "streamline:target-solid")
/**
 * The `Objective` entry is a tasks that the player can complete.
 * It is mainly for displaying the progress to a player.
 *
 * It is **not** necessary to use this entry for objectives.
 * It is just a visual novelty.
 *
 * The entry filters the audience based on if the objective is active.
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
    override val finishedCriteria: List<Criteria> = emptyList(),
    override val displayName: String = "",
) : ObjectiveEntry {
    override fun display(): AudienceFilter {
        return ObjectiveAudienceFilter(
            ref(),
            criteria,
            finishedCriteria,
        )
    }
}

class ObjectiveAudienceFilter(
    objective: Ref<ObjectiveEntry>,
    private val criteria: List<Criteria>,
    private val finishedCriteria: List<Criteria> = emptyList(),
) : AudienceFilter(objective) {
    private val factWatcherSubscriptions = ConcurrentHashMap<UUID, FactListenerSubscription>()

    override fun filter(player: Player): Boolean = criteria.matches(player) || finishedCriteria.matches(player)

    override fun onPlayerAdd(player: Player) {
        super.onPlayerAdd(player)

        factWatcherSubscriptions.compute(player.uniqueId) { _, subscription ->
            subscription?.cancel(player)
            player.listenForFacts(
                (criteria + finishedCriteria).map { it.fact },
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