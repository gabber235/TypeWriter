package me.gabber235.typewriter.capture.capturers

import me.gabber235.typewriter.capture.RecordedCapturer
import org.bukkit.Location
import org.bukkit.entity.Player

class LocationTapeCapturer(override val title: String) : RecordedCapturer<Tape<Location>> {
    private val tape = mutableTapeOf<Location>()
    private var lastLocation: Location? = null
    override fun startRecording(player: Player) {}

    override fun captureFrame(player: Player, frame: Int) {
        val location = player.location
        if (location != lastLocation) {
            lastLocation = location
            tape[frame] = location
        }
    }

    override fun stopRecording(player: Player): Tape<Location> = tape
}