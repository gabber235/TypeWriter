package me.gabber235.typewriter.capture.capturers

import me.gabber235.typewriter.capture.ImmediateCapturer
import org.bukkit.Location
import org.bukkit.entity.Player

class LocationSnapshotCapturer(override val title: String) : ImmediateCapturer<Location> {
    override fun capture(player: Player): Location {
        return player.location
    }
}