package me.gabber235.typewriter.entry.cinematic

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.cinematic.CinematicState.*
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicSettings
import me.gabber235.typewriter.entry.entries.SystemTrigger.CINEMATIC_END
import me.gabber235.typewriter.events.AsyncCinematicEndEvent
import me.gabber235.typewriter.events.AsyncCinematicStartEvent
import me.gabber235.typewriter.events.AsyncCinematicTickEvent
import me.gabber235.typewriter.interaction.*
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent
import java.time.Duration


class CinematicSequence(
    val pageId: String,
    private val player: Player,
    private val actions: List<CinematicAction>,
    private val triggers: List<Ref<TriggerableEntry>>,
    private val settings: CinematicSettings,
) {
    private var state = STARTING
    private var playTime = Duration.ofMillis(-1)
    private val frame: Int get() = (playTime.toMillis()/50).toInt()

    val priority by lazy { KoinJavaComponent.get<EntryDatabase>(EntryDatabase::class.java).pagePriority(pageId) }

    suspend fun start() {
        if (state != STARTING) return

        if (settings.blockChatMessages) player.startBlockingMessages()
        if (settings.blockActionBarMessages) player.startBlockingActionBar()

        actions.forEach {
            try {
                it.setup()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        state = PLAYING

        DISPATCHERS_ASYNC.switchContext {
            AsyncCinematicStartEvent(player, pageId).callEvent()
        }
    }

    suspend fun tick(deltaTime: Duration) {
        if (state != PLAYING) return
        if (canEnd) return

        // Make sure that the first frame is 0
        if (playTime.isNegative) playTime = Duration.ZERO
        else playTime += deltaTime

        coroutineScope {
            actions.map {
                launch {
                    try {
                        it.tick(frame)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.joinAll()
        }
        AsyncCinematicTickEvent(player, frame).callEvent()

        if (canEnd) {
            CINEMATIC_END triggerFor player
        }
    }

    private val canEnd get() = actions.all { it.canFinish(frame) }

    suspend fun end(force: Boolean = false) {
        if (state != PLAYING) return
        state = ENDING
        val originalFrame = frame

        if (settings.blockChatMessages) player.stopBlockingMessages()
        if (settings.blockActionBarMessages) player.stopBlockingActionBar()

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

private enum class CinematicState {
    STARTING, PLAYING, ENDING
}

private val Player.cinematicSequence: CinematicSequence?
    get() = with(KoinJavaComponent.get<InteractionHandler>(InteractionHandler::class.java)) {
        interaction?.cinematic
    }

fun Player.isPlayingCinematic(pageId: String): Boolean = cinematicSequence?.pageId == pageId

fun Player.isPlayingCinematic(): Boolean = cinematicSequence != null