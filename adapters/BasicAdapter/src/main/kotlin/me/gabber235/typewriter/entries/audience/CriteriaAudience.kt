package me.gabber235.typewriter.entries.audience

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.*
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

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
    val criteria: List<Criteria> = emptyList(),
    override val inverted: Boolean = false,
) : AudienceFilterEntry, Invertible {
    override fun display(): AudienceFilter = CriteriaAudienceFilter(ref(), criteria)
}

class CriteriaAudienceFilter(
    ref: Ref<out AudienceFilterEntry>,
    val criteria: List<Criteria>,
) : AudienceFilter(ref) {
    private val factWatcherSubscriptions = ConcurrentHashMap<UUID, FactListenerSubscription>()
    override fun filter(player: Player): Boolean = criteria matches player

    override fun onPlayerAdd(player: Player) {
        factWatcherSubscriptions.compute(player.uniqueId) { _, subscription ->
            subscription?.cancel(player)
            player.listenForFacts(
                criteria.map { it.fact },
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

    override fun dispose() {
        super.dispose()
        factWatcherSubscriptions.forEach { (playerId, subscription) ->
            val player = server.getPlayer(playerId) ?: return@forEach
            subscription.cancel(player)
        }
    }
}