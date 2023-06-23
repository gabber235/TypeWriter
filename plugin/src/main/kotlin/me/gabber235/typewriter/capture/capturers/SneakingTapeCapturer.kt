package me.gabber235.typewriter.capture.capturers

import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.gabber235.typewriter.capture.RecordedCapturer
import me.gabber235.typewriter.plugin
import org.bukkit.entity.Player

class SneakingTapeCapturer(override val title: String) : RecordedCapturer<Tape<Boolean>> {
    private val tape = mutableTapeOf<Boolean>()
    private var lastSneaking: Boolean = false
    private var lastTick = -1
    private var job: Job? = null

    override fun startRecording(player: Player) {
        job?.cancel()
        job = plugin.launch {
            while (true) {
                captureFrame(player, frame = ++lastTick)
                delay(1.ticks)
            }
        }
    }

    private fun captureFrame(player: Player, frame: Int) {
        val sneaking = player.isSneaking
        if (sneaking != lastSneaking) {
            lastSneaking = sneaking
            tape[frame] = sneaking
        }
    }

    override fun stopRecording(player: Player): Tape<Boolean> {
        job?.cancel()
        return tape
    }
}
