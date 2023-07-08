package me.gabber235.typewriter.entries.action

import com.github.shynixn.mccoroutine.bukkit.launch
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
import me.gabber235.typewriter.plugin
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
    override val customTriggers: List<String> = emptyList(),
    @Help("The time to delay the action for.")
    private val duration: Duration = Duration.ZERO, // Number of milliseconds
) : CustomTriggeringActionEntry {

    override fun execute(player: Player) {
        plugin.launch {
            delay(duration.toMillis())
            super.execute(player)
            player.triggerCustomTriggers()
        }
    }
}