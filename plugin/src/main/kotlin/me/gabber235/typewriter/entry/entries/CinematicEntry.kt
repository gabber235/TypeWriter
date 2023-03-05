package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.*
import org.bukkit.entity.Player
import java.time.Duration

@Tags("cinematic")
interface CinematicEntry : TriggerableEntry {
	fun create(player: Player): CinematicAction
}

interface CinematicAction {
	fun setup() {}
	fun tick(delta: Duration) {}
	fun teardown(): Revertible? = null

	val canFinish: Boolean
		get() = true
}

interface Revertible {
	fun revert()
}
