package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import org.bukkit.entity.Player

@Tags("event")
interface EventEntry : TriggerEntry

interface CustomCommandEntry : EventEntry {
	@Help("The command to register. Do not include the leading slash.")
	val command: String

	fun filter(player: Player, commandLabel: String, args: Array<out String>): CommandFilterResult =
		CommandFilterResult.Success

	fun execute(player: Player, commandLabel: String, args: Array<out String>) {
		triggerAllFor(player)
	}

	sealed interface CommandFilterResult {
		object Success : CommandFilterResult
		object Failure : CommandFilterResult
		object FailureWithDefaultMessage : CommandFilterResult
		data class FailureWithMessage(val message: String) : CommandFilterResult
	}

	companion object
}

class Event(val player: Player, val triggers: List<EventTrigger>) {
	constructor(player: Player, vararg triggers: EventTrigger) : this(player, triggers.toList())

	operator fun contains(trigger: EventTrigger) = triggers.contains(trigger)

	operator fun contains(entry: Entry) = EntryTrigger(entry.id) in triggers

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Event) return false

		if (triggers != other.triggers) return false
		if (player.uniqueId != other.player.uniqueId) return false

		return true
	}

	override fun hashCode(): Int {
		var result = triggers.hashCode()
		result = 31 * result + player.hashCode()
		return result
	}
}

interface EventTrigger {
	val id: String
}

data class EntryTrigger(override val id: String) : EventTrigger

enum class SystemTrigger : EventTrigger {
	DIALOGUE_NEXT,
	DIALOGUE_END,
	CINEMATIC_END,
	;

	override val id: String
		get() = "system.${name.lowercase().replace('_', '.')}"
}

class CinematicStartTrigger(val pageId: String, val triggers: List<String>) : EventTrigger {
	override val id: String
		get() = "system.cinematic.start.$pageId"
}