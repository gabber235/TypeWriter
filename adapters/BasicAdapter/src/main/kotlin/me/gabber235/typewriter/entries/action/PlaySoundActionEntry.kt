package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

@Entry("play_sound", "Play sound at player, or location", Colors.RED, Icons.MUSIC)
data class PlaySoundActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	val location: Optional<Location> = Optional.empty(),
	val sound: Sound = Sound.ENTITY_VILLAGER_YES,
	val volume: Float = 1.0f,
	val pitch: Float = 1.0f,
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)

		if (location.isPresent) {
			location.get().world?.playSound(location.get(), sound, volume, pitch)
		} else {
			player.world.playSound(player.location, sound, volume, pitch)
		}
	}
}