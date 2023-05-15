package me.gabber235.typewriter.capture

import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object Recorders {
    private val recorders = ConcurrentHashMap<UUID, Recorder<*>>()

    fun isRecording(player: Player): Boolean {
        return recorders.containsKey(player.uniqueId)
    }

    suspend fun <T> record(player: Player, capturer: Capturer<T>): T {
        when (capturer) {
            is ImmediateCapturer -> return capturer.capture(player)
            is RecordedCapturer -> {
                if (recorders.containsKey(player.uniqueId)) {
                    throw IllegalStateException("Already recording!")
                }
                val recorder = Recorder(player, capturer)
                recorders[player.uniqueId] = recorder
                val result = recorder.record()
                recorders.remove(player.uniqueId)
                return result
            }
        }
    }
}

val Player.isRecording: Boolean
    get() = Recorders.isRecording(this)