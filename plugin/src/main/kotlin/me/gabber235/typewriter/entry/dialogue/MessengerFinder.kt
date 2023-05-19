package me.gabber235.typewriter.entry.dialogue

import lirand.api.extensions.events.listen
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.adapters.MessengerData
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.plugin
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

class MessengerFinder : KoinComponent {
    private val adapterLoader: AdapterLoader by inject()

    private var messengers = emptyMap<MessengerData, MessengerFilter>()

    fun initialize() {
        messengers = adapterLoader.adapters.flatMap { it.messengers }.associateBy({ it }, ::instantiateFilter)
    }

    private fun instantiateFilter(data: MessengerData): MessengerFilter {
        return if (data.filter.kotlin.isCompanion) {
            data.filter.kotlin.objectInstance as MessengerFilter
        } else {
            data.filter.kotlin.createInstance()
        }
    }

    fun findMessenger(player: Player, entry: DialogueEntry): DialogueMessenger<out DialogueEntry> {
        val messenger =
            messengers
                .filter { it.key.dialogue.isInstance(entry) }
                .filter { it.value.filter(player, entry) }
                .maxByOrNull { it.key.priority }?.key?.messenger
                ?: return EmptyDialogueMessenger(player, entry)

        return messenger.kotlin.primaryConstructor!!.call(player, entry)
    }
}

enum class MessengerState {
    RUNNING,
    FINISHED,
    CANCELLED,
}

open class DialogueMessenger<DE : DialogueEntry>(val player: Player, val entry: DE) {

    protected val listener: Listener = object : Listener {}

    open var state: MessengerState = MessengerState.RUNNING
        protected set

    open fun init() {}
    open fun tick(cycle: Int) {}

    open fun dispose() {
        HandlerList.unregisterAll(listener)
    }

    open fun end() {
        player.chatHistory.resendMessages(player)
    }

    open val triggers: List<String>
        get() = entry.triggers

    open val modifiers: List<Modifier>
        get() = entry.modifiers

    protected inline fun <reified E : Event> listen(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        noinline block: (event: E) -> Unit,
    ) {
        plugin.listen(listener, priority, ignoreCancelled, block)
    }
}

class EmptyDialogueMessenger(player: Player, entry: DialogueEntry) : DialogueMessenger<DialogueEntry>(player, entry) {
    override fun init() {
        state = MessengerState.FINISHED
    }
}