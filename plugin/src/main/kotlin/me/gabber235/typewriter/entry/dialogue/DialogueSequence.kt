package me.gabber235.typewriter.entry.dialogue

import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_NEXT
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.interaction.startBlockingActionBar
import me.gabber235.typewriter.interaction.startBlockingMessages
import me.gabber235.typewriter.interaction.stopBlockingActionBar
import me.gabber235.typewriter.interaction.stopBlockingMessages
import me.gabber235.typewriter.utils.playSound
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DialogueSequence(private val player: Player, initialEntry: DialogueEntry) : KoinComponent {
    private val messengerFinder: MessengerFinder by inject()
    private val factDatabase: FactDatabase by inject()

    private var currentEntry: DialogueEntry = initialEntry
    private var currentMessenger = messengerFinder.findMessenger(player, initialEntry)
    private var cycle = 0
    var isActive = false

    val triggers: List<String>
        get() = currentMessenger.triggers


    fun init() {
        isActive = true
        cycle = 0
        currentMessenger.init()
        player.playSpeakerSound(currentEntry.speakerEntry)
        player.startBlockingMessages()
        player.startBlockingActionBar()
        tick()
    }

    fun tick() {
        if (!isActive) return
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
        currentMessenger = messengerFinder.findMessenger(player, nextEntry)
        init()
        return true
    }

    private fun cleanupEntry(final: Boolean) {
        val messenger = currentMessenger

        if (final) {
            player.stopBlockingMessages()
            player.stopBlockingActionBar()
            messenger.end()
        }
        messenger.dispose()

        factDatabase.modify(player.uniqueId, messenger.modifiers)
    }

    fun end() {
        isActive = false
        cleanupEntry(true)
    }
}

fun Player.playSpeakerSound(speaker: SpeakerEntry?) {
    val sound = speaker?.sound ?: return
    playSound(sound)
}