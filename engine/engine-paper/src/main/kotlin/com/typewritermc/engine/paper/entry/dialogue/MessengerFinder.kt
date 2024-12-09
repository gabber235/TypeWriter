package com.typewritermc.engine.paper.entry.dialogue

import com.github.shynixn.mccoroutine.bukkit.ticks
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.utils.Reloadable
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.interaction.chatHistory
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.loader.ExtensionLoader
import kotlinx.coroutines.delay
import lirand.api.extensions.events.unregister
import lirand.api.extensions.server.registerEvents
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.primaryConstructor

class MessengerFinder : KoinComponent, Reloadable {
    private val extensionLoader: ExtensionLoader by inject()
    private var messengers = listOf<MessengerData>()

    override suspend fun load() {
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

    override suspend fun unload() {
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

    open val eventTriggers: List<EventTrigger>
        get() = entry.eventTriggers

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

