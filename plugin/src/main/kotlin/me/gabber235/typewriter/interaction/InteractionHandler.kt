package me.gabber235.typewriter.interaction

import com.github.shynixn.mccoroutine.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.event.Event
import me.gabber235.typewriter.facts.facts
import org.bukkit.entity.Player
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


	fun triggerEvent(event: Event) {
		val interaction = interactions[event.player.uniqueId]
		if (event.name == "system.interaction.start") {
			if (interaction != null) return
			interactions[event.player.uniqueId] = Interaction(event.player)
			interactions[event.player.uniqueId]?.onEvent(event)
			return
		}

		if (event.name == "system.interaction.end") {
			if (interaction == null) return
			if (interaction.isActive) return
			interaction.end()
			interactions.remove(event.player.uniqueId)
			return
		}

		interaction?.onEvent(event)

		// Trigger all actions
		EntryDatabase.findActions(event.name, event.player.facts).forEach { action ->
			action.execute(event.player)
		}
	}

	fun init() {
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

		plugin.listen<PlayerQuitEvent> { event ->
			interactions.remove(event.player.uniqueId)?.end()
		}
	}

	fun shutdown() {
		job?.cancel()
		job = null
	}
}