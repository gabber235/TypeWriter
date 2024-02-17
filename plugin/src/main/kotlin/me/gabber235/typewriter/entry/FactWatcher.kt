package me.gabber235.typewriter.entry

import me.gabber235.typewriter.entry.entries.EventTrigger
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.interaction.InteractionHandler
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FactWatcher(
    private val player: Player,
) {
    private val factWatch = ConcurrentHashMap<Ref<ReadableFactEntry>, Int>()
    private val listeners = mutableListOf<FactListener>()

    fun tick() {
        refresh()
    }

    private fun refresh() {
        factWatch.keys.forEach { refreshFact(it) }
    }

    fun refreshFact(ref: Ref<ReadableFactEntry>) {
        val old = factWatch[ref] ?: return
        val fact = ref.get() ?: return
        val new = fact.readForPlayersGroup(player).value
        if (old != new) {
            factWatch[ref] = new
            notifyListeners(ref)
        }
    }

    private fun notifyListeners(ref: Ref<ReadableFactEntry>) {
        listeners.forEach { listener ->
            if (ref in listener) {
                listener.listener(player, ref)
            }
        }
    }

    fun addListener(
        facts: List<Ref<ReadableFactEntry>>,
        listener: (Player, Ref<ReadableFactEntry>) -> Unit
    ): FactListenerSubscription {
        val id = UUID.randomUUID()
        listeners.add(FactListener(id, facts, listener))
        for (fact in facts) {
            factWatch.computeIfAbsent(fact) { fact.get()?.readForPlayersGroup(player)?.value ?: 0 }
        }

        return FactListenerSubscription(id)
    }

    fun removeListener(subscription: FactListenerSubscription) {
        listeners.removeIf { it.id == subscription.id }

        for (fact in factWatch.keys.toList()) {
            if (listeners.none { fact in it }) {
                factWatch.remove(fact)
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
