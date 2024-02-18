package me.gabber235.typewriter.interaction

import kotlinx.coroutines.*
import lirand.api.extensions.server.registerSuspendingEvents
import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.entries.Event
import me.gabber235.typewriter.entry.entries.EventTrigger
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.events.PublishedBookEvent
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.ThreadType.SYNC
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.KoinComponent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private const val TICK_MS = 50L
private const val AVERAGE_SCHEDULING_DELAY_MS = 5L

class InteractionHandler : Listener, KoinComponent {
    private val interactions = ConcurrentHashMap<UUID, Interaction>()
    private var job: Job? = null

    internal val Player.interaction: Interaction?
        get() = interactions[uniqueId]


    /** Some triggers start dialogue. Though we don't want to trigger the starting of dialogue multiple times,
     * we need to check if the player is already in a dialogue.
     *
     * @param player The player who interacted
     * @param initialTriggers The trigger that should be fired after the interaction started
     * @param continueTrigger The trigger that should be fired if the interaction is already active
     */
    fun startDialogueWithOrTriggerEvent(
        player: Player,
        initialTriggers: List<EventTrigger>,
        continueTrigger: EventTrigger? = null
    ) {
        val interaction = player.interaction ?: return
        if (interaction.hasDialogue) {
            if (continueTrigger != null) {
                triggerEvent(Event(player, continueTrigger))
            }
        } else {
            triggerEvent(Event(player, initialTriggers))
        }
    }

    /**
     * Triggers a list of actions.
     *
     * @param player The player who interacted
     * @param triggers A list of triggers that should be fired.
     */
    fun triggerActions(player: Player, triggers: List<EventTrigger>) {
        triggerEvent(Event(player, triggers))
    }


    private fun triggerEvent(event: Event) {
        // If the event is empty, we don't need to do anything
        if (event.triggers.isEmpty()) return

        SYNC.launch {
            try {
                event.player.interaction?.onEvent(event)
            } catch (e: Exception) {
                logger.severe("An error occurred while handling event ${event}: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun initialize() {

        job = DISPATCHERS_ASYNC.launch {
            while (plugin.isEnabled) {
                val startTime = System.currentTimeMillis()
                tick()
                val endTime = System.currentTimeMillis()
                // Wait for the remainder or the tick
                val wait = TICK_MS - (endTime - startTime) - AVERAGE_SCHEDULING_DELAY_MS
                if (wait > 0) delay(wait)
            }
        }

        plugin.registerSuspendingEvents(this)
    }

    suspend fun tick() {
        coroutineScope {
            interactions.map { (_, interaction) ->
                launch {
                    try {
                        interaction.tick()
                    } catch (e: Exception) {
                        logger.severe("An error occurred while ticking interaction ${interaction.player.name}: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }.joinAll()
        }
    }

    // When a player joins the server, we need to create an interaction for them.
    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        interactions[event.player.uniqueId] = Interaction(event.player)
    }

    // When a player leaves the server, we need to end the interaction.
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        runBlocking {
            interactions.remove(event.player.uniqueId)?.end()
        }
    }

    // When the plugin reloads, we need to end all interactions and create new ones.
    @EventHandler(priority = EventPriority.MONITOR)
    suspend fun onReloadTypewriter(event: TypewriterReloadEvent) {
        resetAllInteractions()
    }

    // When a book is published, we need to end all interactions.
    @EventHandler(priority = EventPriority.MONITOR)
    suspend fun onPublishBook(event: PublishedBookEvent) {
        resetAllInteractions()
    }

    private suspend fun resetAllInteractions() {
        interactions.forEach { (_, interaction) ->
            interaction.end()
        }
        interactions.clear()
        interactions.putAll(server.onlinePlayers.map { it.uniqueId to Interaction(it) })
    }

    // When a player tries to execute a command, we need to end the dialogue.
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        DIALOGUE_END triggerFor event.player
    }

    suspend fun shutdown() {
        job?.cancel()
        job = null
        interactions.forEach { (_, interaction) ->
            interaction.end()
        }
        interactions.clear()
    }
}