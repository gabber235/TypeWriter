package me.gabber235.typewriter.interaction

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.gabber235.typewriter.content.ContentEditor
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.cinematic.CinematicSequence
import me.gabber235.typewriter.entry.dialogue.DialogueSequence
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.entries.SystemTrigger.*
import me.gabber235.typewriter.entry.quest.QuestTracker
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration


class Interaction(val player: Player) : KoinComponent {
    private var job: Job
    private val interactionHandler: InteractionHandler by inject()

    internal var dialogue: DialogueSequence? = null
    internal var cinematic: CinematicSequence? = null
    internal var content: ContentEditor? = null
    internal val questTracker: QuestTracker = QuestTracker(player)
    internal val factWatcher: FactWatcher = FactWatcher(player)

    private var scheduledEvent: Event? = null
    private val eventMutex = Mutex()

    private var lastTickTime = System.currentTimeMillis()

    val hasDialogue: Boolean
        get() = dialogue != null

    val hasCinematic: Boolean
        get() = cinematic != null

    val hasContent: Boolean
        get() = content != null

    init {
        job = DISPATCHERS_ASYNC.launch {
            // Wait for the plugin to be enabled
            delay(100)
            while (plugin.isEnabled) {
                val startTime = System.currentTimeMillis()
                eventMutex.withLock {
                    runSchedule()
                }
                val now = System.currentTimeMillis()
                val deltaTime = now - lastTickTime
                lastTickTime = now
                tick(Duration.ofMillis(deltaTime))
                val endTime = System.currentTimeMillis()
                // Wait for the remainder or the tick
                val wait = TICK_MS - (endTime - startTime) - AVERAGE_SCHEDULING_DELAY_MS
                if (wait > 0) delay(wait)
                else logger.fine("The interaction for ${player.name} is running behind! Took ${endTime - startTime}ms")
            }
        }
    }

    internal fun setup() {
        questTracker.setup()
    }

    /**
     * Adds an event to the schedule. If there is already an event scheduled, it will be merged with
     */
    suspend fun addToSchedule(event: Event) = eventMutex.withLock {
        scheduledEvent = event.merge(scheduledEvent)
    }

    /**
     * Forces an event to be executed.
     * This will bypass the schedule and execute the event immediately.
     * This will also clear the schedule.
     */
    suspend fun forceEvent(event: Event) {
        eventMutex.withLock {
            scheduledEvent = event.merge(scheduledEvent)
            runSchedule()
        }
    }

    suspend fun tick(deltaTime: Duration) {
        dialogue?.tick(deltaTime)
        cinematic?.tick(deltaTime)
        content?.tick()
        factWatcher.tick()
    }

    private suspend fun runSchedule() {
        val scheduledEvent = scheduledEvent ?: return
        onEvent(scheduledEvent.distinct())
        this.scheduledEvent = null
    }

    /** Handles an event. All [SystemTrigger]'s are handled by the plugin itself. */
    private suspend fun onEvent(event: Event) {
        handleContent(event)
        triggerActions(event)
        handleFactWatcher(event)
        if (hasContent) return
        handleDialogue(event)
        handleCinematic(event)
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
            }.toList() // Stops infinite loops
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
                .sortedWith { a, b ->
                    val priorityDiff = b.priority - a.priority
                    if (priorityDiff != 0) return@sortedWith priorityDiff
                    b.criteria.size - a.criteria.size
                }
                .firstOrNull { it.criteria.matches(event.player) }


        if (nextDialogue != null) {
            // If there is no sequence yet, start a new one
            if (dialogue == null) {
                dialogue = DialogueSequence(player, nextDialogue).also {
                    it.init()
                }
            } else if (dialogue?.isActive == false || nextDialogue.priority >= (dialogue?.priority ?: 0)) {
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
        val trigger = triggers.maxBy { it.priority }
        val cinematic = cinematic
        if (cinematic != null && trigger.priority <= cinematic.priority) return

        this.cinematic = null
        cinematic?.end()

        val actions =
            Query.findWhereFromPage<CinematicEntry>(trigger.pageId) {
                it.criteria.matches(event.player)
            }.map { it.create(event.player) }.toList()

        if (actions.isEmpty()) return

        this.cinematic = CinematicSequence(trigger.pageId, player, actions, trigger.triggers, trigger.settings).also {
            it.start()
        }
    }

    private suspend fun handleContent(event: Event) {
        if (CONTENT_END in event) {
            val content = content ?: return
            this.content = null
            content.dispose()
        }

        if (CONTENT_POP in event) {
            val content = content ?: return
            if (content.popMode()) return
            this.content = null
            content.dispose()
        }

        val trigger = event.triggers.asSequence().filterIsInstance<ContentModeTrigger>().firstOrNull() ?: return

        listOf(DIALOGUE_END, CINEMATIC_END) triggerFor player

        val content = content
        if (content != null) {
            if (trigger is ContentModeSwapTrigger) {
                content.swapMode(trigger.mode)
            } else {
                content.pushMode(trigger.mode)
            }
            return
        }
        this.content = ContentEditor(trigger.context, player, trigger.mode).also {
            it.initialize()
        }
    }

    private fun handleFactWatcher(event: Event) {
        val factRefreshes = event.triggers.filterIsInstance<RefreshFactTrigger>()
        if (factRefreshes.isEmpty()) return

        factRefreshes.forEach {
            factWatcher.refreshFact(it.fact)
        }
    }

    suspend fun end() {
        job.cancel()
        val dialogue = dialogue
        val cinematic = cinematic
        val content = content

        this.dialogue = null
        this.cinematic = null
        this.content = null

        dialogue?.end()
        cinematic?.end(force = true)
        content?.dispose()
        questTracker.dispose()
    }
}