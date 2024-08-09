package me.gabber235.typewriter.entry

import io.ktor.util.collections.*
import me.gabber235.typewriter.entry.entries.EventTrigger
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.logErrorIfNull
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FactWatcher(
    private val player: Player,
) {
    private val factCache = ConcurrentHashMap<Ref<ReadableFactEntry>, Int>()
    private val listeners = ConcurrentMap<UUID, FactListener>()

    fun tick() {
        refresh()
    }

    private fun refresh() {
        factCache.keys.forEach { refreshFact(it) }
    }

    fun refreshFact(ref: Ref<ReadableFactEntry>) {
        val old = factCache[ref] ?: return
        val fact =
            ref.get().logErrorIfNull("Tracking a fact $ref which does not have an entry associated with it.") ?: return
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

class RefreshFactTrigger(
    val fact: Ref<ReadableFactEntry>,
) : EventTrigger {
    override val id: String
        get() = "fact.${fact.id}"
}

private val Player.factWatcher: FactWatcher?
    get() = with(KoinJavaComponent.get<InteractionHandler>(InteractionHandler::class.java)) {
        interaction?.factWatcher
    }

fun Player.listenForFacts(
    facts: List<Ref<ReadableFactEntry>>,
    listener: (Player, Ref<ReadableFactEntry>) -> Unit
): FactListenerSubscription {
    val watcher = factWatcher ?: throw IllegalStateException("Player is not in an interaction")
    return watcher.addListener(facts, listener)
}

fun Player.stopListening(subscription: FactListenerSubscription) {
    val watcher = factWatcher ?: return
    watcher.removeListener(subscription)
}
