package me.gabber235.typewriter.entry.dialogue

import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_NEXT
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.facts.FactDatabase
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.SoundCategory
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
        player.playSpeakerSound(currentEntry.speakerEntry)
        tick()
    }

    fun tick() {
        currentMessenger.tick(cycle++)

        if (currentMessenger.state == MessengerState.FINISHED) {
            isActive = false
            DIALOGUE_NEXT triggerFor player
        } else if (currentMessenger.state == MessengerState.CANCELLED) {
            isActive = false
            DIALOGUE_END triggerFor player
        }
    }

    fun next(nextEntry: DialogueEntry): Boolean {
        cleanupEntry(false)
        currentEntry = nextEntry
        currentMessenger = MessengerFinder.findMessenger(player, nextEntry)
        init()
        return true
    }

    private fun cleanupEntry(final: Boolean) {
        val messenger = currentMessenger
        messenger.dispose()
        if (final) messenger.end()

        FactDatabase.modify(player.uniqueId, messenger.modifiers)
    }

    fun end() {
        isActive = false
        cleanupEntry(true)
    }
}

fun Player.playSpeakerSound(speaker: SpeakerEntry?) {
    val soundName = speaker?.sound ?: return
    if (soundName.isBlank()) return
    val soundNamespace = NamespacedKey.fromString(speaker.sound)
    val sound = Sound.values().firstOrNull { it.key == soundNamespace } ?: return
    playSound(this, sound, SoundCategory.VOICE, 1f, 1f)
}