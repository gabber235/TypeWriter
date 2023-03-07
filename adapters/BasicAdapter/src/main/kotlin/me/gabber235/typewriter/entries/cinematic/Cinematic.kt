package me.gabber235.typewriter.entries.cinematic

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.CinematicStartTrigger
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player


@Entry("cinematic", "Starts and finishes a cinematic", Colors.RED, Icons.CAMERA_RETRO)
data class Cinematic(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("triggers")
	override val customTriggers: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	val pageId: String = "",
	@Help("If the player is already in a cinematic, should the cinematic be replaced?")
	val override: Boolean = false
) : CustomTriggeringActionEntry {
	override fun execute(player: Player) {
		super.execute(player)

		CinematicStartTrigger(pageId, customTriggers, override) triggerFor player
	}
}