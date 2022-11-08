package me.gabber235.typewriter.entry.dialogue

import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.interaction.chatHistory
import org.bukkit.entity.Player
import org.bukkit.event.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

object MessengerFinder {

	private var messengers = emptyMap<MessengerData, MessengerFilter>()

	fun init() {
		messengers = AdapterLoader.getAdapterData().flatMap { it.messengers }.associateBy({ it }, {
			if (it.filter.kotlin.isCompanion) {
				it.filter.kotlin.objectInstance as MessengerFilter
			} else {
				it.filter.kotlin.createInstance()
			}
		})
	}

	fun findMessenger(player: Player, entry: DialogueEntry): DialogueMessenger<out DialogueEntry> {
		val messenger =
			messengers
				.filter { it.key.dialogue.isInstance(entry) }
				.filter { it.value.filter(player, entry) }
				.maxByOrNull { it.key.priority }?.key?.messenger
				?: return EmptyDialogueMessenger(player, entry)

		return messenger.kotlin.primaryConstructor!!.call(player, entry)
	}
}

enum class MessengerState {
	RUNNING,
	FINISHED,
	CANCELLED,
}

open class DialogueMessenger<DE : DialogueEntry>(val player: Player, val entry: DE) {

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

class EmptyDialogueMessenger(player: Player, entry: DialogueEntry) : DialogueMessenger<DialogueEntry>(player, entry) {
	override fun init() {
		state = MessengerState.FINISHED
	}
}