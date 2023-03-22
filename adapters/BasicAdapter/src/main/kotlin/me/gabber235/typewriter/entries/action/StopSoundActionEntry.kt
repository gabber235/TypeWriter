package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Sound
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

@Entry("stop_sound", "Stop a or all sounds for a player", Colors.RED, Icons.MUSIC)
data class StopSoundActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	@Sound
	@Help("The sound to stop.")
	val sound: Optional<String> = Optional.empty(),
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)

		if (sound.isPresent) {
			player.stopSound(sound.get())
		} else {
			player.stopAllSounds()
		}
	}
}