package me.gabber235.typewriter.entry.dialogue

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.delay
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.adapters.MessengerData
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.gabber235.typewriter.utils.config
import me.gabber235.typewriter.utils.reloadable
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
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

    open fun init() {
    }

    open fun tick(cycle: Int) {}

    open fun dispose() {
        HandlerList.unregisterAll(listener)
    }

    open fun end() {
        state = MessengerState.FINISHED
        player.chatHistory.resendMessages(player)

        // Resend the chat history again after a delay to make sure that the dialogue chat is fully cleared
        SYNC.launch {
            delay(1.ticks)
            player.chatHistory.resendMessages(player)
        }
    }

    open val triggers: List<Ref<out TriggerableEntry>>
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

private val confirmationKeyString by config(
    "confirmationKey", ConfirmationKey.SWAP_HANDS.name, comment = """
    |The key that should be pressed to confirm a dialogue option.
    |Possible values: ${ConfirmationKey.entries.joinToString(", ") { it.name }}
""".trimMargin()
)

val confirmationKey: ConfirmationKey by reloadable {
    val key = ConfirmationKey.fromString(confirmationKeyString)
    if (key == null) {
        plugin.logger.warning("Invalid confirmation key '$confirmationKeyString'. Using default key '${ConfirmationKey.SWAP_HANDS.name}' instead.")
        return@reloadable ConfirmationKey.SWAP_HANDS
    }
    key
}


enum class ConfirmationKey(val keybind: String) {
    SWAP_HANDS("<key:key.swapOffhand>"),
    JUMP("<key:key.jump>"),
    SNEAK("<key:key.sneak>"),
    ;

    private inline fun <reified E : PlayerEvent> listenEvent(
        listener: Listener,
        playerUUID: UUID,
        crossinline block: () -> Unit
    ) {
        plugin.listen(
            listener,
            EventPriority.HIGHEST,
        ) event@{ event: E ->
            if (event.player.uniqueId != playerUUID) return@event
            if (event is PlayerToggleSneakEvent && !event.isSneaking) return@event // Otherwise the event is fired twice
            block()
            if (event is Cancellable) event.isCancelled = true
        }
    }

    fun listen(listener: Listener, playerUUID: UUID, block: () -> Unit) {
        when (this) {
            SWAP_HANDS -> listenEvent<PlayerSwapHandItemsEvent>(listener, playerUUID, block)
            JUMP -> listenEvent<PlayerJumpEvent>(listener, playerUUID, block)
            SNEAK -> listenEvent<PlayerToggleSneakEvent>(listener, playerUUID, block)
        }
    }

    companion object {
        fun fromString(string: String): ConfirmationKey? {
            return entries.find { it.name.equals(string, true) }
        }
    }
}