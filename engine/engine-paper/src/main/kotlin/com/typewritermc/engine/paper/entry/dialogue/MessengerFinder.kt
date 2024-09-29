package com.typewritermc.engine.paper.entry.dialogue

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.github.shynixn.mccoroutine.bukkit.ticks
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.utils.Reloadable
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.interaction.chatHistory
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.config
import com.typewritermc.engine.paper.utils.reloadable
import com.typewritermc.loader.ExtensionLoader
import kotlinx.coroutines.delay
import lirand.api.extensions.events.listen
import lirand.api.extensions.events.unregister
import lirand.api.extensions.server.registerEvents
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.primaryConstructor

class MessengerFinder : KoinComponent, Reloadable {
    private val extensionLoader: ExtensionLoader by inject()
    private var messengers = listOf<MessengerData>()

    override fun load() {
        messengers = extensionLoader.extensions.flatMap { it.dialogueMessengers }.map {
            val messenger = extensionLoader.loadClass(it.className).kotlin
            MessengerData(
                dialogue = extensionLoader.loadClass(it.entryClassName).kotlin as KClass<out DialogueEntry>,
                messenger = messenger as KClass<out DialogueMessenger<*>>,
                filter = messenger.companionObjectInstance as? MessengerFilter ?: EmptyMessengerFilter(),
                priority = it.priority,
            )
        }
    }

    override fun unload() {
        messengers = emptyList()
    }

    fun findMessenger(player: Player, entry: DialogueEntry): DialogueMessenger<out DialogueEntry> {
        val messenger =
            messengers
                .filter { it.dialogue.isInstance(entry) }
                .filter { it.filter.filter(player, entry) }
                .maxByOrNull { it.priority }?.messenger
                ?: return EmptyDialogueMessenger(player, entry)

        return messenger.primaryConstructor!!.call(player, entry)
    }
}

enum class MessengerState {
    RUNNING,
    FINISHED,
    CANCELLED,
}

open class DialogueMessenger<DE : DialogueEntry>(val player: Player, val entry: DE) : Listener {
    open var state: MessengerState = MessengerState.RUNNING
        protected set

    open var isCompleted = true

    open fun init() {
        plugin.registerEvents(this)
    }

    open fun tick(context: TickContext) {}

    open fun dispose() {
        unregister()
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

    fun completeOrFinish() {
        if (isCompleted) {
            state = MessengerState.FINISHED
        } else {
            isCompleted = true
        }
    }
}

class TickContext(
    val playTime: Duration,
    val deltaTime: Duration,
)

class MessengerData(
    val messenger: KClass<out DialogueMessenger<*>>,
    val dialogue: KClass<out DialogueEntry>,
    val filter: MessengerFilter,
    val priority: Int
)

interface MessengerFilter {
    fun filter(player: Player, entry: DialogueEntry): Boolean
}

class EmptyMessengerFilter : MessengerFilter {
    override fun filter(player: Player, entry: DialogueEntry): Boolean = true
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

private val typingDurationTypeString by config(
    "typingDurationType", TypingDurationType.TOTAL.name, comment = """
    |The type of typing duration that should be used.
    |Possible values: ${TypingDurationType.entries.joinToString(", ") { it.name }}
""".trimMargin()
)

val typingDurationType: TypingDurationType by reloadable {
    val type = TypingDurationType.fromString(typingDurationTypeString)
    if (type == null) {
        plugin.logger.warning("Invalid typing duration type '$typingDurationTypeString'. Using default type '${TypingDurationType.TOTAL.name}' instead.")
        return@reloadable TypingDurationType.TOTAL
    }
    type
}

enum class TypingDurationType {
    TOTAL,
    CHARACTER,
    ;

    fun calculatePercentage(playTime: Duration, duration: Duration, text: String): Double {
        return playTime.toMillis().toDouble() / totalDuration(text, duration).toMillis()
    }

    fun totalDuration(text: String, duration: Duration): Duration {
        return when (this) {
            TOTAL -> duration
            CHARACTER -> Duration.ofMillis(text.length * duration.toMillis())
        }
    }

    companion object {
        fun fromString(string: String): TypingDurationType? {
            return entries.find { it.name.equals(string, true) }
        }
    }
}