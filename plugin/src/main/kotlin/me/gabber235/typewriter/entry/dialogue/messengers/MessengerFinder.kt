package me.gabber235.typewriter.entry.dialogue.messengers

import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.dialogue.DialogueEntry
import me.gabber235.typewriter.entry.dialogue.entries.*
import me.gabber235.typewriter.entry.dialogue.messengers.message.UniversalMessageDialogueMessenger
import me.gabber235.typewriter.entry.dialogue.messengers.option.BedrockOptionDialogueMessenger
import me.gabber235.typewriter.entry.dialogue.messengers.option.JavaOptionDialogueMessenger
import me.gabber235.typewriter.entry.dialogue.messengers.spoken.BedrockSpokenDialogueMessenger
import me.gabber235.typewriter.entry.dialogue.messengers.spoken.JavaSpokenDialogueMessenger
import me.gabber235.typewriter.interaction.chatHistory
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.geysermc.floodgate.api.FloodgateApi

object MessengerFinder {

	fun findMessenger(player: Player, entry: DialogueEntry): Messenger<out DialogueEntry> {
		return when (entry) {
			is SpokenDialogueEntry  -> {
				if (player.isFloodgate) BedrockSpokenDialogueMessenger(player, entry)
				else JavaSpokenDialogueMessenger(player, entry)
			}

			is OptionDialogueEntry  -> {
				if (player.isFloodgate) BedrockOptionDialogueMessenger(player, entry)
				else JavaOptionDialogueMessenger(player, entry)
			}

			is MessageDialogueEntry -> {
				UniversalMessageDialogueMessenger(player, entry)
			}

			else                    -> {
				throw IllegalArgumentException("No messenger found for entry type ${entry::class.simpleName}")
			}
		}
	}

	private val Player.isFloodgate: Boolean
		get() {
			if (!plugin.isFloodgateInstalled) return false
			return FloodgateApi.getInstance().isFloodgatePlayer(this.uniqueId)
		}
}

enum class MessengerState {
	RUNNING,
	FINISHED,
	CANCELLED,
}

open class Messenger<DE : DialogueEntry>(val player: Player, val entry: DE) {

	protected val listener: Listener = object : Listener {}

	open var state: MessengerState = MessengerState.RUNNING
		protected set

	open fun init() {}
	open fun tick(cycle: Int) {}

	open fun dispose() {
		HandlerList.unregisterAll(listener)
	}

	open fun end() {
		player.chatHistory.resendMessages(player)
	}

	open val triggers: List<String>
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