package me.gabber235.typewriter.entry.action

import com.github.shynixn.mccoroutine.launch
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.interaction.InteractionHandler
import org.bukkit.entity.Player

class DelayedActionEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("triggers")
	val next_triggers: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	@SerializedName("triggered_by")
	override val triggerdBy: List<String> = emptyList(),
	private val duration: Long = 0, // Number of milliseconds
) : ActionEntry {
	// Disable the normal triggers. So that the action can manually trigger the next actions.
	override val triggers: List<String>
		get() = emptyList()

	override fun execute(player: Player) {
		plugin.launch {
			delay(duration)
			super.execute(player)
			InteractionHandler.startInteractionAndTrigger(player, next_triggers)
		}
	}
}