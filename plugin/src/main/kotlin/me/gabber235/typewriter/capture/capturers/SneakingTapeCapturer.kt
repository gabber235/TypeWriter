package me.gabber235.typewriter.capture.capturers

import me.gabber235.typewriter.capture.RecordedCapturer
import org.bukkit.entity.Player

class SneakingTapeCapturer(override val title: String) : RecordedCapturer<Tape<Boolean>> {
    private val tape = mutableTapeOf<Boolean>()
    private var lastSneaking: Boolean = false

    override fun startRecording(player: Player) {}

    override fun captureFrame(player: Player, frame: Int) {
        val sneaking = player.isSneaking
        if (sneaking != lastSneaking) {
            lastSneaking = sneaking
            tape[frame] = sneaking
        }
    }

    override fun stopRecording(player: Player): Tape<Boolean> = tape
}
