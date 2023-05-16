package me.gabber235.typewriter.interaction

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.*
import lirand.api.extensions.server.registerSuspendingEvents
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.entries.Event
import me.gabber235.typewriter.entry.entries.EventTrigger
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.triggerFor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private const val TICK_MS = 50L
private const val AVERAGE_SCHEDULING_DELAY_MS = 5L

object InteractionHandler : Listener {
    private val interactions = ConcurrentHashMap<UUID, Interaction>()
    private var job: Job? = null

    private val Player.interaction: Interaction
        get() = interactions.getOrPut(uniqueId) { Interaction(this) }


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
        val interaction = player.interaction
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


    /**
     * Triggers an event.
     * All events that start with "system."
     * Are handled by the plugin itself.
     * All other events are handled based on the entries in the database.
     *
     * @param event The event to trigger
     */
    fun triggerEvent(event: Event) {
        plugin.launch {
            try {
                event.player.interaction.onEvent(event)
            } catch (e: Exception) {
                plugin.logger.severe("An error occurred while handling event ${event}: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun init() {
        job = plugin.launch(Dispatchers.IO) {
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
                        plugin.logger.severe("An error occurred while ticking interaction ${interaction.player.name}: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }.joinAll()
        }
    }

    // When a player leaves the server, we need to end the interaction and clear its chat history.
    @EventHandler
    suspend fun onPlayerQuit(event: PlayerQuitEvent) {
        interactions.remove(event.player.uniqueId)?.end()
        event.player.chatHistory.clear()
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