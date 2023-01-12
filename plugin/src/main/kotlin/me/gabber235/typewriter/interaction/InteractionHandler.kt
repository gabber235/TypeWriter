package me.gabber235.typewriter.interaction

import com.github.shynixn.mccoroutine.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.entries.SystemTrigger.*
import me.gabber235.typewriter.entry.matches
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
		initialTriggers: List<EventTrigger>,
		continueTrigger: EventTrigger? = null
	) {
		if (interactions.containsKey(player.uniqueId)) {
			if (continueTrigger != null) {
				triggerEvent(Event(player, continueTrigger))
			}
		} else {
			triggerEvent(Event(player, listOf(INTERACTION_START) + initialTriggers))
		}
	}

	/** Some triggers start an interaction. Though we don't want to trigger the starting of an interaction multiple times,
	 * we need to check if the player is already in an interaction.
	 *
	 * @param player The player who interacted
	 * @param triggers A list of triggers that should be fired.
	 */
	fun startInteractionAndTrigger(player: Player, triggers: List<EventTrigger>) {
		var triggers = triggers
		if (!interactions.containsKey(player.uniqueId)) {
			triggers = listOf(INTERACTION_START) + triggers
		}
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
		val interaction = interactions[event.player.uniqueId]
		if (INTERACTION_START in event) {
			if (interaction != null) return
			interactions[event.player.uniqueId] = Interaction(event.player)
			interactions[event.player.uniqueId]?.onEvent(event)
			return
		}

		if (INTERACTION_END in event) {
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
		val facts = event.player.facts
		val actions = Query.findWhere<ActionEntry> { it in event && it.criteria.matches(facts) }
		actions.forEach { action ->
			action.execute(event.player)
		}
		val newTriggers = actions.flatMap { it.triggers }
			.map { EntryTrigger(it) }
			.filter { it !in event } // Stops infinite loops
		if (newTriggers.isNotEmpty()) {
			triggerEvent(Event(event.player, newTriggers))
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
						triggerEvent(Event(interaction.player, INTERACTION_END))
					}
				}
			}
		}

		// When a player leaves the server, we need to end the interaction and clear its chat history.
		plugin.listen<PlayerQuitEvent> { event ->
			interactions.remove(event.player.uniqueId)?.end()
			event.player.chatHistory.clear()
		}

		// When a player tries to execute a command, we need to end the dialogue.
		plugin.listen<PlayerCommandPreprocessEvent>(priority = EventPriority.LOWEST, ignoreCancelled = true) { event ->
			triggerEvent(Event(event.player, DIALOGUE_END))
		}
	}

	fun shutdown() {
		job?.cancel()
		job = null
	}
}