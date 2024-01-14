package me.gabber235.typewriter.capture

import org.bukkit.entity.Player

sealed interface Capturer<T> {
    val title: String
}

interface ImmediateCapturer<T> : Capturer<T> {
    suspend fun capture(player: Player): T
}

interface RecordedCapturer<T> : Capturer<T> {
    fun startRecording(player: Player)

    fun captureFrame(player: Player, frame: Int)

    fun stopRecording(player: Player): T
}