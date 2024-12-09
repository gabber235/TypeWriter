package com.typewritermc.engine.paper.facts

import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.interaction.PlayerSessionManager
import com.typewritermc.engine.paper.interaction.SessionTracker
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FactTracker(
    private val player: Player,
) : SessionTracker {
    private val factCache = ConcurrentHashMap<Ref<ReadableFactEntry>, Int>()
    private val listeners = ConcurrentHashMap<UUID, FactListener>()

    override fun setup() {
    }

    override fun tick() {
        refresh()
    }

    override fun teardown() {
        listeners.clear()
        factCache.clear()
    }


    private fun refresh() {
        factCache.keys.forEach { refreshFact(it) }
    }

    fun refreshFact(ref: Ref<ReadableFactEntry>) {
        val old = factCache[ref] ?: return
        val fact =
            ref.get() ?: return
        val new = fact.readForPlayersGroup(player).value
        if (old != new) {
            factCache[ref] = new
            notifyListeners(ref)
        }
    }

    private fun notifyListeners(ref: Ref<ReadableFactEntry>) {
        listeners
            .values
            .filter { ref in it }
            .forEach { listener ->
                listener.listener(player, ref)
            }
    }

    fun addListener(
        facts: List<Ref<ReadableFactEntry>>,
        listener: (Player, Ref<ReadableFactEntry>) -> Unit
    ): FactListenerSubscription {
        var id: UUID
        do {
            id = UUID.randomUUID()
        } while (listeners.containsKey(id))

        listeners[id] = FactListener(id, facts, listener)

        for (fact in facts) {
            factCache.computeIfAbsent(fact) { fact.get()?.readForPlayersGroup(player)?.value ?: 0 }
        }

        return FactListenerSubscription(id)
    }

    fun removeListener(subscription: FactListenerSubscription) {
        listeners.remove(subscription.id)

        for (fact in factCache.keys.toList()) {
            if (listeners.values.none { fact in it }) {
                factCache.remove(fact)
            }
        }
    }

    companion object {
        @JvmStatic
        fun listenForFacts(
            player: Player,
            facts: List<Ref<ReadableFactEntry>>,
            listener: (Player, Ref<ReadableFactEntry>) -> Unit
        ): FactListenerSubscription = player.listenForFacts(facts, listener)

        @JvmStatic
        fun stopListening(
            player: Player,
            subscription: FactListenerSubscription
        ) = player.stopListening(subscription)
    }
}

private class FactListener(
    val id: UUID,
    val facts: List<Ref<ReadableFactEntry>>,
    val listener: (Player, Ref<ReadableFactEntry>) -> Unit,
) {
    operator fun contains(ref: Ref<ReadableFactEntry>) = ref in facts
}

class FactListenerSubscription(
    val id: UUID,
) {
    fun cancel(player: Player) = player.stopListening(this)
}


internal val Player.factTracker: FactTracker?
    get() = with(KoinJavaComponent.get<PlayerSessionManager>(PlayerSessionManager::class.java)) {
        session?.tracker(FactTracker::class)
    }

fun Player.listenForFacts(
    facts: List<Ref<ReadableFactEntry>>,
    listener: (Player, Ref<ReadableFactEntry>) -> Unit
): FactListenerSubscription {
    val watcher = factTracker ?: throw IllegalStateException("Player is not in an quest")
    return watcher.addListener(facts, listener)
}

fun Player.stopListening(subscription: FactListenerSubscription) {
    val watcher = factTracker ?: return
    watcher.removeListener(subscription)
}
