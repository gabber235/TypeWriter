package me.gabber235.typewriter.entry.dialogue

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_NEXT
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.events.AsyncDialogueEndEvent
import me.gabber235.typewriter.events.AsyncDialogueStartEvent
import me.gabber235.typewriter.events.AsyncDialogueSwitchEvent
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.interaction.startBlockingActionBar
import me.gabber235.typewriter.interaction.startBlockingMessages
import me.gabber235.typewriter.interaction.stopBlockingActionBar
import me.gabber235.typewriter.interaction.stopBlockingMessages
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.playSound
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration

class DialogueSequence(private val player: Player, initialEntry: DialogueEntry) : KoinComponent {
    private val messengerFinder: MessengerFinder by inject()
    private val factDatabase: FactDatabase by inject()

    private var currentEntry: DialogueEntry = initialEntry
    private var currentMessenger = messengerFinder.findMessenger(player, initialEntry)
    private var playTime = Duration.ZERO
    var isActive = false

    val triggers: List<Ref<out TriggerableEntry>>
        get() = currentMessenger.triggers


    fun init() {
        setup()
        tick(playTime)
        DISPATCHERS_ASYNC.launch {
            AsyncDialogueStartEvent(player).callEvent()
        }
    }

    private fun setup() {
        isActive = true
        playTime = Duration.ZERO
        currentMessenger.init()
        player.playSpeakerSound(currentEntry.speaker.get())
        player.startBlockingMessages()
        player.startBlockingActionBar()
    }

    fun tick(deltaTime: Duration) {
        if (!isActive) return
        playTime += deltaTime

        if (currentMessenger.state == MessengerState.FINISHED) {
            isActive = false
            DIALOGUE_NEXT triggerFor player
        } else if (currentMessenger.state == MessengerState.CANCELLED) {
            isActive = false
            DIALOGUE_END triggerFor player
        }

        currentMessenger.tick(playTime)
    }

    fun next(nextEntry: DialogueEntry): Boolean {
        cleanupEntry(false)
        currentEntry = nextEntry
        currentMessenger = messengerFinder.findMessenger(player, nextEntry)
        setup()
        DISPATCHERS_ASYNC.launch {
            AsyncDialogueSwitchEvent(player).callEvent()
        }
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

        factDatabase.modify(player, messenger.modifiers)
    }

    fun end() {
        isActive = false
        cleanupEntry(true)
        DISPATCHERS_ASYNC.launch {
            AsyncDialogueEndEvent(player).callEvent()
        }
    }
}

fun Player.playSpeakerSound(speaker: SpeakerEntry?) {
    val sound = speaker?.sound ?: return
    playSound(sound)
}