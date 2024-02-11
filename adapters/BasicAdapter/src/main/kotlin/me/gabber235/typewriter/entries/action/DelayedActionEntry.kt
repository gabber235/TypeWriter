package me.gabber235.typewriter.entries.action

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
import me.gabber235.typewriter.utils.ThreadType.SYNC
import org.bukkit.entity.Player
import java.time.Duration

@Entry("delayed_action", "Delay an action for a certain amount of time", Colors.RED, "fa-solid:hourglass")
/**
 * The `Delayed Action Entry` is an entry that fires its triggers after a specified duration. This entry provides you with the ability to create time-based actions and events.
 *
 * ## How could this be used?
 *
 * This entry can be useful in a variety of situations where you need to delay an action or event.
 * You can use it to create countdown timers, to perform actions after a certain amount of time has elapsed, or to schedule events in the future.
 */
class DelayedActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The time to delay the action for.")
    // The duration before the next triggers are fired.
    private val duration: Duration = Duration.ZERO, // Number of milliseconds
) : CustomTriggeringActionEntry {

    override fun execute(player: Player) {
        SYNC.launch {
            delay(duration.toMillis())
            super.execute(player)
            player.triggerCustomTriggers()
        }
    }
}