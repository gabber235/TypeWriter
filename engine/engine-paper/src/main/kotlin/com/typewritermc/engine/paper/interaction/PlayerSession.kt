package com.typewritermc.engine.paper.interaction

import com.typewritermc.core.utils.getAll
import com.typewritermc.engine.paper.entry.entries.Event
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.ThreadType.DISPATCHERS_ASYNC
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import java.time.Duration
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.safeCast


class PlayerSession(val player: Player) : KoinComponent {
    private var job: Job? = null

    private var scope: InteractionScope? = null
    val interaction: Interaction? get() = scope?.interaction

    private var trackers: List<SessionTracker> = emptyList()
    private var triggerHandlers: List<TriggerHandler> = emptyList()

    private var scheduledEvent: Event? = null
    private val eventMutex = Mutex()

    private var lastTickTime = System.currentTimeMillis()

    internal fun setup() {
        triggerHandlers = getKoin().getAll<TriggerHandler>()
        trackers = getKoin().getAll<SessionTracker>(player)
        trackers.forEach {
            try {
                it.setup()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        startJob()
    }

    private fun startJob() {
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
                else logger.fine("The session ticker for ${player.name} is running behind! Took ${endTime - startTime}ms")
            }
        }
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
        interaction?.tick(deltaTime)
        trackers.forEach { it.tick() }
    }

    private suspend fun runSchedule() {
        val scheduledEvent = scheduledEvent ?: return
        onEvent(scheduledEvent.distinct())
        this.scheduledEvent = null
    }

    /** Handles an event. */
    private suspend fun onEvent(event: Event) {
        var endInteraction = false
        val nextInteractions = mutableListOf<Interaction>()
        var toTrigger = event

        // To prevent infinite loops, we limit the number of recursive calls.
        var loops = 0
        while (toTrigger.triggers.isNotEmpty() && loops < 1000) {
            loops++
            val triggering = Event(toTrigger.player, toTrigger.triggers.filter { it.canTriggerFor(toTrigger.player) })

            if (triggering.triggers.isEmpty()) break
            toTrigger = Event(triggering.player)

            triggerHandlers.forEach { handler ->
                fun TriggerContinuation.apply() {
                    when (this) {
                        TriggerContinuation.Done -> {}
                        is TriggerContinuation.Append -> toTrigger = toTrigger.merge(this.event)
                        is TriggerContinuation.StartInteraction -> nextInteractions.add(this.interaction)
                        TriggerContinuation.EndInteraction -> endInteraction = true
                        is TriggerContinuation.Multi -> this.continuations.forEach { it.apply() }
                    }
                }

                handler.trigger(triggering, interaction).apply()
            }
        }

        if (nextInteractions.isEmpty() && endInteraction) {
            scope?.teardown()
            scope = null
            return
        }

        val nextInteraction = nextInteractions.maxByOrNull { it.priority } ?: return
        // Only more or equal important interactions should be started.
        if (nextInteraction.priority < (interaction?.priority ?: 0)) {
            return
        }

        if (scope == null) {
            scope = InteractionScope(nextInteraction)
            scope?.initialize()
        } else {
            scope?.swapInteraction(nextInteraction)
        }
    }

    fun <T : SessionTracker> tracker(klass: KClass<T>): T? {
        return klass.safeCast(trackers.firstOrNull { it::class.isSubclassOf(klass) })
    }

    suspend fun teardown() {
        job?.cancel()
        scope?.teardown(force = true)
        trackers.forEach { it.teardown() }
    }
}