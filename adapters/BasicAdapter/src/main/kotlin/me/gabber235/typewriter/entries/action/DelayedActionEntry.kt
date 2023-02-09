package me.gabber235.typewriter.entries.action

import com.github.shynixn.mccoroutine.launch
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.time.Duration

@Entry("delayed_action", "Delay an action for a certain amount of time", Colors.RED, Icons.SOLID_HOURGLASS_HALF)
class DelayedActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	@SerializedName("triggers")
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	val nextTriggers: List<String> = emptyList(),
	private val duration: Duration = Duration.ZERO, // Number of milliseconds
) : ActionEntry {
	// Disable the normal triggers. So that the action can manually trigger the next actions.
	override val triggers: List<String>
		get() = emptyList()

	override fun execute(player: Player) {
		plugin.launch {
			delay(duration.toMillis())
			super.execute(player)
			nextTriggers triggerEntriesFor player
		}
	}
}