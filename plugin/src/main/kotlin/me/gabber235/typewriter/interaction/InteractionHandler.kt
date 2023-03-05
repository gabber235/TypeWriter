package me.gabber235.typewriter.interaction

import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.entries.Event
import me.gabber235.typewriter.entry.entries.EventTrigger
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.triggerFor
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object InteractionHandler {
	private val interactions = ConcurrentHashMap<UUID, Interaction>()
	private var runnable: BukkitTask? = null

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
	 * All events that start with "system." are handled by the plugin itself.
	 * All other events are handled based on the entries in the database.
	 *
	 * @param event The event to trigger
	 */
	fun triggerEvent(event: Event) {
		event.player.interaction.onEvent(event)
	}

	fun init() {
		runnable = object : BukkitRunnable() {
			override fun run() {
				tick()
			}
		}.runTaskTimer(plugin, 0, 1)

		// When a player leaves the server, we need to end the interaction and clear its chat history.
		plugin.listen<PlayerQuitEvent> { event ->
			interactions.remove(event.player.uniqueId)?.end()
			event.player.chatHistory.clear()
		}

		// When a player tries to execute a command, we need to end the dialogue.
		plugin.listen<PlayerCommandPreprocessEvent>(priority = EventPriority.LOWEST, ignoreCancelled = true) { event ->
			DIALOGUE_END triggerFor event.player
		}
	}

	fun tick() {
		interactions.forEach { (_, interaction) ->
			interaction.tick()
		}
	}

	fun shutdown() {
		runnable?.cancel()
		runnable = null
		interactions.forEach { (_, interaction) ->
			interaction.end()
		}
		interactions.clear()
	}
}