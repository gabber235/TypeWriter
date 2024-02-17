package me.gabber235.typewriter.entry.cinematic

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.SystemTrigger.CINEMATIC_END
import me.gabber235.typewriter.entry.triggerEntriesFor
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.events.AsyncCinematicEndEvent
import me.gabber235.typewriter.events.AsyncCinematicStartEvent
import me.gabber235.typewriter.events.AsyncCinematicTickEvent
import me.gabber235.typewriter.interaction.*
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent
import java.util.*

private const val STARTING_FRAME = -1
private const val ENDED_FRAME = -2

class CinematicSequence(
    val pageId: String,
    private val player: Player,
    private val actions: List<CinematicAction>,
    private val triggers: List<Ref<TriggerableEntry>>,
    private val minEndTime: Optional<Int>,
) {
    private var frame = STARTING_FRAME

    suspend fun start() {
        if (frame > STARTING_FRAME) return

        player.startBlockingMessages()
        player.startBlockingActionBar()

        actions.forEach {
            try {
                it.setup()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        DISPATCHERS_ASYNC.switchContext {
            AsyncCinematicStartEvent(player, pageId).callEvent()
        }
    }

    suspend fun tick() {
        if (frame == ENDED_FRAME) return
        if (frame == STARTING_FRAME) start()
        if (canEnd) return

        frame++
        actions.forEach {
            try {
                it.tick(frame)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        AsyncCinematicTickEvent(player, frame).callEvent()

        if (canEnd) {
            CINEMATIC_END triggerFor player
        }
    }

    private val canEnd get() = actions.all { it.canFinish(frame) } && minEndTime.map { frame >= it }.orElse(true)

    suspend fun end(force: Boolean = false) {
        if (frame == ENDED_FRAME || frame == STARTING_FRAME) return
        val originalFrame = frame
        frame = ENDED_FRAME

        player.stopBlockingMessages()
        player.stopBlockingActionBar()

        actions.forEach {
            try {
                it.teardown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (force) return
        triggers triggerEntriesFor player

        DISPATCHERS_ASYNC.switchContext {
            AsyncCinematicEndEvent(player, originalFrame, pageId).callEvent()
        }
    }
}

private val Player.cinematicSequence: CinematicSequence?
    get() = with(KoinJavaComponent.get<InteractionHandler>(InteractionHandler::class.java)) {
        interaction?.cinematic
    }

fun Player.isPlayingCinematic(pageId: String): Boolean = cinematicSequence?.pageId == pageId

fun Player.isPlayingCinematic(): Boolean = cinematicSequence != null