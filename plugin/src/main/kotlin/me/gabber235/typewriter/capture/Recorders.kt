package me.gabber235.typewriter.capture

import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

interface Recorder<T> {
    suspend fun record(): T
}

object Recorders {
    private val recorders = ConcurrentHashMap<UUID, Recorder<*>>()

    fun isRecording(player: Player): Boolean {
        return recorders.containsKey(player.uniqueId)
    }

    suspend fun <T> record(player: Player, capturer: Capturer<T>): T {
        when (capturer) {
            is ImmediateCapturer -> return capturer.capture(player)
            is RecordedCapturer -> return record(player, StaticRecorder(player, capturer))
        }
    }

    suspend fun <T> record(player: Player, recorder: Recorder<T>): T {
        if (recorders.containsKey(player.uniqueId)) {
            throw IllegalStateException("Already recording!")
        }
        recorders[player.uniqueId] = recorder
        val result = recorder.record()
        recorders.remove(player.uniqueId)
        return result
    }
}

val Player.isRecording: Boolean
    get() = Recorders.isRecording(this)