package me.gabber235.typewriter.entries.action

import com.github.shynixn.mccoroutine.launch
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("delayed_action", "Delay an action for a certain amount of time", Colors.RED, Icons.SOLID_HOURGLASS_HALF)
class DelayedActionEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("triggered_by")
	override val triggeredBy: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	@SerializedName("triggers")
	@field:Triggers
	val nextTriggers: List<String> = emptyList(),
	private val duration: Long = 0, // Number of milliseconds
) : ActionEntry {
	// Disable the normal triggers. So that the action can manually trigger the next actions.
	override val triggers: List<String>
		get() = emptyList()

	override fun execute(player: Player) {
		plugin.launch {
			delay(duration)
			super.execute(player)
			InteractionHandler.startInteractionAndTrigger(player, nextTriggers)
		}
	}
}