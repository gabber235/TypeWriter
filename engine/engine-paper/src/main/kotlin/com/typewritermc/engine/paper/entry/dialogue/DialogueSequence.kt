package com.typewritermc.engine.paper.entry.dialogue

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.priority
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.entry.entries.SpeakerEntry
import com.typewritermc.engine.paper.entry.entries.SystemTrigger.DIALOGUE_END
import com.typewritermc.engine.paper.entry.entries.SystemTrigger.DIALOGUE_NEXT
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.events.AsyncDialogueEndEvent
import com.typewritermc.engine.paper.events.AsyncDialogueStartEvent
import com.typewritermc.engine.paper.events.AsyncDialogueSwitchEvent
import com.typewritermc.engine.paper.facts.FactDatabase
import com.typewritermc.engine.paper.interaction.*
import com.typewritermc.engine.paper.utils.ThreadType.DISPATCHERS_ASYNC
import com.typewritermc.engine.paper.utils.playSound
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent
import java.time.Duration

class DialogueSequence(private val player: Player, initialEntry: DialogueEntry) : KoinComponent {
    private val _speakers: MutableSet<Ref<SpeakerEntry>> = mutableSetOf()
    val speakers: Set<Ref<SpeakerEntry>> by ::_speakers

    private val messengerFinder: MessengerFinder by inject()
    private val factDatabase: FactDatabase by inject()

    internal var currentEntry: DialogueEntry = initialEntry
    private var currentMessenger = messengerFinder.findMessenger(player, initialEntry)
    private var playTime = Duration.ZERO
    var isActive = false

    val triggers: List<Ref<out TriggerableEntry>>
        get() = currentMessenger.triggers

    val priority: Int
        get() = currentEntry.priority


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
        _speakers.add(currentEntry.speaker)
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

    fun next(nextEntry: DialogueEntry) {
        cleanupEntry(false)
        currentEntry = nextEntry
        currentMessenger = messengerFinder.findMessenger(player, nextEntry)
        setup()
        DISPATCHERS_ASYNC.launch {
            AsyncDialogueSwitchEvent(player).callEvent()
        }
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


private val Player.dialogueSequence: DialogueSequence?
    get() = with(KoinJavaComponent.get<InteractionHandler>(InteractionHandler::class.java)) {
        interaction?.dialogue
    }

val Player.currentDialogue: DialogueEntry?
    get() {
        val sequence = dialogueSequence ?: return null
        if (!sequence.isActive) return null
        return sequence.currentEntry
    }

val Player.speakersInDialogue: Set<Ref<SpeakerEntry>>
    get() {
        val sequence = dialogueSequence ?: return emptySet()
        return sequence.speakers
    }

fun Player.playSpeakerSound(speaker: SpeakerEntry?) {
    val sound = speaker?.sound ?: return
    playSound(sound)
}