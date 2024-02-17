package me.gabber235.typewriter.interaction

import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.cinematic.CinematicSequence
import me.gabber235.typewriter.entry.dialogue.DialogueSequence
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.entries.SystemTrigger.*
import me.gabber235.typewriter.entry.quest.QuestTracker
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Interaction(val player: Player) : KoinComponent {
    private val interactionHandler: InteractionHandler by inject()

    internal var dialogue: DialogueSequence? = null
    internal var cinematic: CinematicSequence? = null
    internal val questTracker: QuestTracker = QuestTracker(player)
    internal val factWatcher: FactWatcher = FactWatcher(player)
    private val sidebarManager: SidebarManager = SidebarManager(player)

    val hasDialogue: Boolean
        get() = dialogue != null

    val hasCinematic: Boolean
        get() = cinematic != null

    suspend fun tick() {
        dialogue?.tick()
        cinematic?.tick()
        factWatcher.tick()
        sidebarManager.tick()
    }

    /** Handles an event. All [SystemTrigger]'s are handled by the plugin itself. */
    suspend fun onEvent(event: Event) {
        triggerActions(event)
        handleDialogue(event)
        handleCinematic(event)
        handleFactWatcher(event)
    }

    /**
     * Triggers all actions that are registered for the given event.
     *
     * @param event The event that should be triggered
     */
    private fun triggerActions(event: Event) {
        // Trigger all actions
        val actions =
            Query.findWhere<ActionEntry> {
                it in event && it.criteria.matches(event.player)
            }
        actions.forEach { action -> action.execute(event.player) }
        val newTriggers =
            actions.flatMap { it.triggers }.map { EntryTrigger(it) }.filter {
                it !in event
            } // Stops infinite loops
        if (newTriggers.isNotEmpty()) {
            interactionHandler.triggerActions(event.player, newTriggers)
        }
    }

    private suspend fun handleDialogue(event: Event) {
        if (DIALOGUE_END in event) {
            val dialogue = dialogue ?: return
            this.dialogue = null
            dialogue.end()
            return
        }

        if (DIALOGUE_NEXT in event) {
            onDialogueNext()
            return
        }

        // Try to trigger new/next dialogue
        tryTriggerNextDialogue(event)
    }

    /**
     * Tries to trigger a new dialogue. If no dialogue can be found, it will end the dialogue
     * sequence.
     */
    private fun tryTriggerNextDialogue(event: Event) {
        val nextDialogue =
            Query.findWhere<DialogueEntry> { it in event }
                .sortedByDescending { it.criteria.size }
                .firstOrNull { it.criteria.matches(event.player) }

        if (nextDialogue != null) {
            // If there is no sequence yet, start a new one
            if (dialogue == null) {
                dialogue = DialogueSequence(player, nextDialogue)
                dialogue?.init()
            } else {
                // If there is a sequence, trigger the next dialogue
                dialogue?.next(nextDialogue)
            }
        } else if (dialogue?.isActive == false) {
            // If there is no next dialogue and the sequence isn't active anymore, we can end the
            // sequence
            DIALOGUE_END triggerFor player
        }
    }

    /**
     * Called when the player clicks the next button. If there is no next dialogue, the sequence
     * will be ended.
     */
    private suspend fun onDialogueNext() {
        val dialog = dialogue ?: return
        dialog.isActive = false
        if (dialog.triggers.isEmpty()) {
            // We need to immediately end the dialogue, otherwise it may be triggered again
            onEvent(Event(player, listOf(DIALOGUE_END)))
            return
        }

        // We need to immediately end the dialogue, otherwise it may be triggered again
        onEvent(Event(player, dialog.triggers.map(::EntryTrigger)))
        return
    }

    private suspend fun handleCinematic(event: Event) {
        if (CINEMATIC_END in event) {
            val cinematic = cinematic ?: return
            this.cinematic = null
            cinematic.end()
            return
        }

        val triggers = event.triggers.filterIsInstance<CinematicStartTrigger>()
        if (triggers.isEmpty()) return
        // If any of the triggers is an override, we should use that one
        // Otherwise, we should use the first one
        val trigger = triggers.firstOrNull { it.override } ?: triggers.first()
        if (cinematic != null && !trigger.override) return

        val cinematic = cinematic
        this.cinematic = null
        cinematic?.end()

        val entries =
            Query.findWhereFromPage<CinematicEntry>(trigger.pageId) {
                it.criteria.matches(event.player)
                        && it.id !in trigger.ignoreEntries
            }

        if (entries.isEmpty() && !trigger.simulate) return

        val actions = entries.mapNotNull {
            if (trigger.simulate) {
                it.createSimulated(player)
            } else {
                it.create(player)
            }
        }

        this.cinematic = CinematicSequence(trigger.pageId, player, actions, trigger.triggers, trigger.minEndTime)
    }

    private fun handleFactWatcher(event: Event) {
        val factRefreshes = event.triggers.filterIsInstance<RefreshFactTrigger>()
        if (factRefreshes.isEmpty()) return

        factRefreshes.forEach {
            factWatcher.refreshFact(it.fact)
        }
    }

    suspend fun end() {
        val dialogue = dialogue
        val cinematic = cinematic

        this.dialogue = null
        this.cinematic = null

        dialogue?.end()
        cinematic?.end(force = true)
        questTracker.dispose()
        sidebarManager.dispose(force = true)
    }
}