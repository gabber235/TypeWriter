package me.gabber235.typewriter.entry.dialogue

import lirand.api.extensions.world.playSound
import me.gabber235.typewriter.entry.dialogue.messengers.MessengerFinder
import me.gabber235.typewriter.entry.dialogue.messengers.MessengerState
import me.gabber235.typewriter.entry.event.Event
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.interaction.InteractionHandler
import org.bukkit.*
import org.bukkit.entity.Player

class DialogueSequence(private val player: Player, initialEntry: DialogueEntry) {
	private var currentEntry: DialogueEntry = initialEntry
	private var currentMessenger = MessengerFinder.findMessenger(player, initialEntry)
	private var cycle = 0
	var isActive = false
		private set

	val triggers: List<String>
		get() = currentMessenger.triggers

	fun init() {
		isActive = true
		cycle = 0
		currentMessenger.init()
		if (!currentEntry.speakerEntry?.sound.isNullOrBlank()) {
			val soundNamespace = NamespacedKey.fromString(currentEntry.speakerEntry?.sound ?: "")
			val sound = Sound.values().firstOrNull() { it.key == soundNamespace }
			if (sound != null) {
				player.playSound(sound, SoundCategory.VOICE)
			}
		}
		tick()
	}

	private fun cleanupEntry(final: Boolean) {
		val messenger = currentMessenger
		messenger.dispose()
		if (final) messenger.end()

		FactDatabase.modify(player.uniqueId, messenger.modifiers)
	}

	fun tick() {
		currentMessenger.tick(cycle++)

		if (currentMessenger.state == MessengerState.FINISHED) {
			isActive = false
			InteractionHandler.triggerEvent(Event("system.dialogue.next", player))
		} else if (currentMessenger.state == MessengerState.CANCELLED) {
			isActive = false
			InteractionHandler.triggerEvent(Event("system.dialogue.end", player))
		}
	}

	fun next(nextEntry: DialogueEntry): Boolean {
		cleanupEntry(false)
		currentEntry = nextEntry
		currentMessenger = MessengerFinder.findMessenger(player, nextEntry)
		init()
		return true
	}

	fun end() {
		isActive = false
		cleanupEntry(true)
	}
}