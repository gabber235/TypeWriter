package me.gabber235.typewriter.capture.capturers

import me.gabber235.typewriter.capture.ImmediateCapturer
import me.gabber235.typewriter.utils.round
import org.bukkit.Location
import org.bukkit.entity.Player

class LocationSnapshotCapturer(override val title: String) : ImmediateCapturer<Location> {
    override suspend fun capture(player: Player): Location {
        val location = player.location
        return Location(
            location.world,
            location.x.round(2),
            location.y.round(2),
            location.z.round(2),
            location.yaw.round(2),
            location.pitch.round(2)
        )
    }
}