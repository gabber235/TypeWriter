package me.gabber235.typewriter.entry.cinematic

import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger.CINEMATIC_END
import me.gabber235.typewriter.entry.triggerEntriesFor
import me.gabber235.typewriter.entry.triggerFor
import org.bukkit.entity.Player

class CinematicSequence(
    private val player: Player,
    private val entries: List<CinematicEntry>,
    private val triggers: List<String>
) {
    private var frame = -1
    private var actions = emptyList<CinematicAction>()

    suspend fun start() {
        if (frame > -1) return
        actions = entries.map { it.create(player) }
        actions.forEach {
            try {
                it.setup()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun tick() {
        if (frame == -1) {
            start()
        }

        frame++
        actions.forEach {
            try {
                it.tick(frame)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (canEnd) {
            CINEMATIC_END triggerFor player
        }
    }

    private val canEnd get() = actions.all { it.canFinish(frame) }

    suspend fun end() {
        actions.forEach {
            try {
                it.teardown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        triggers triggerEntriesFor player
    }
}