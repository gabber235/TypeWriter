package me.gabber235.typewriter.capture.capturers

import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.capture.RecordedCapturer
import org.bukkit.Location
import org.bukkit.entity.Player

class LocationTapeCapturer(override val title: String) : RecordedCapturer<Tape<Location>> {
    private val tape = mutableTapeOf<Location>()
    private var lastLocation: Location? = null
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
        val location = player.location
        if (location != lastLocation) {
            lastLocation = location
            tape[frame] = location
        }
    }

    override fun stopRecording(player: Player): Tape<Location> {
        job?.cancel()
        return tape
    }
}