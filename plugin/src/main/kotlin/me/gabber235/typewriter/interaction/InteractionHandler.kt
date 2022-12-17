package me.gabber235.typewriter.interaction

import com.github.shynixn.mccoroutine.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.entries.Event
import me.gabber235.typewriter.facts.facts
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object InteractionHandler {
	private val interactions = ConcurrentHashMap<UUID, Interaction>()
	private var job: Job? = null


	/** Some triggers start an interaction. Though we don't want to trigger the starting of an interaction multiple times,
	 * we need to check if the player is already in an interaction.
	 *
	 * @param player The player who interacted
	 * @param initialTriggers The trigger that should be fired after the interaction started
	 * @param continueTrigger The trigger that should be fired if the interaction is already active
	 */
	fun startInteractionWithOrTriggerEvent(
		player: Player,
		initialTriggers: List<String>,
		continueTrigger: String? = null
	) {
		if (interactions.containsKey(player.uniqueId)) {
			if (continueTrigger != null) {
				triggerEvent(Event(continueTrigger, player))
			}
		} else {
			triggerEvent(Event("system.interaction.start", player))
			for (trigger in initialTriggers) {
				triggerEvent(Event(trigger, player))
			}
		}
	}

	/** Some triggers start an interaction. Though we don't want to trigger the starting of an interaction multiple times,
	 * we need to check if the player is already in an interaction.
	 *
	 * @param player The player who interacted
	 * @param triggers A list of triggers that should be fired.
	 */
	fun startInteractionAndTrigger(player: Player, triggers: List<String>) {
		if (!interactions.containsKey(player.uniqueId)) {
			triggerEvent(Event("system.interaction.start", player))
		}
		for (trigger in triggers) {
			triggerEvent(Event(trigger, player))
		}
	}


	/**
	 * Triggers an event.
	 * All events that start with "system." are handled by the plugin itself.
	 * All other events are handled based on the entries in the database.
	 *
	 * @param event The event to trigger
	 */
	fun triggerEvent(event: Event) {
		val interaction = interactions[event.player.uniqueId]
		if (event.id == "system.interaction.start") {
			if (interaction != null) return
			interactions[event.player.uniqueId] = Interaction(event.player)
			interactions[event.player.uniqueId]?.onEvent(event)
			return
		}

		if (event.id == "system.interaction.end") {
			if (interaction == null) return
			if (interaction.isActive) return
			interaction.end()
			interactions.remove(event.player.uniqueId)
			return
		}

		interaction?.onEvent(event)

		triggerActions(event)
	}

	/**
	 * Triggers all actions that are registered for the given event.
	 *
	 * @param event The event that should be triggered
	 */
	private fun triggerActions(event: Event) {
		// Trigger all actions
		val actions = EntryDatabase.findActions(event.id, event.player.facts)
		actions.forEach { action ->
			action.execute(event.player)
		}
		actions.flatMap { it.triggers }
			.filter { it != event.id } // Stops infinite loops
			.forEach { trigger ->
				triggerEvent(Event(trigger, event.player))
			}
	}

	fun init() {
		// We want interactions to end if the interaction is not active.
		job = plugin.launch {
			while (plugin.isEnabled) {
				delay(50)
				interactions.forEach { (_, interaction) ->
					interaction.tick()
					if (!interaction.isActive) {
						triggerEvent(Event("system.interaction.end", interaction.player))
					}
				}
			}
		}

		// When a player leaves the server, we need to end the interaction and clear its chat history.
		plugin.listen<PlayerQuitEvent> { event ->
			interactions.remove(event.player.uniqueId)?.end()
			event.player.chatHistory.clear()
		}

		// When a player tries to execute a command, we need to end the interaction.
		plugin.listen<PlayerCommandPreprocessEvent>(priority = EventPriority.MONITOR, ignoreCancelled = true) { event ->
			triggerEvent(Event("system.dialogue.end", event.player))

			// TODO: Move this to the BasicAdapter
//			val message = event.message.removePrefix("/")
//
//			val triggers = EntryDatabase.findEventEntries(RunCommandEventEntry::class) {
//				Regex(it.command).matches(message)
//			}
//				.flatMap { it.triggers }
//			if (triggers.isEmpty()) return@listen
//			startInteractionAndTrigger(event.player, triggers)
		}
	}

	fun shutdown() {
		job?.cancel()
		job = null
	}
}