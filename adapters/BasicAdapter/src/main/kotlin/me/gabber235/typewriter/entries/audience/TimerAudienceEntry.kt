import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.utils.ThreadType
import org.bukkit.entity.Player
import java.time.Duration
import java.util.*

@Entry(
    "timer_audience",
    "Triggers an action every specified duration when the player is in the audience",
    Colors.GREEN,
    "mdi:timer-outline"
)
/**
 * The `Timer Audience` entry is an audience filter that triggers an action every specified duration when the player is in the audience.
 *
 * :::caution
 * Very short durations can cause performance issues, and may be inaccurate.
 * :::
 *
 * ## How could this be used?
 * This can be used to trigger a sequence every few seconds.
 */
class TimerAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    val duration: Duration = Duration.ofSeconds(1),
    val onTimer: Ref<TriggerableEntry> = emptyRef(),
) : AudienceEntry {
    override fun display(): AudienceDisplay = TimerAudienceDisplay(duration, onTimer)
}

class TimerAudienceDisplay(
    private val duration: Duration,
    private val onTimer: Ref<TriggerableEntry>,
) : AudienceDisplay() {
    private val jobs = mutableMapOf<UUID, Job>()

    override fun onPlayerAdd(player: Player) {
        if (duration.isZero || duration.isNegative) {
            logger.warning("Timer duration must be positive, otherwise it will infinitely trigger.")
            return
        }
        jobs[player.uniqueId] = ThreadType.DISPATCHERS_ASYNC.launch {
            while (player in this) {
                delay(duration.toMillis())
                onTimer triggerFor player
            }
        }
    }

    override fun onPlayerRemove(player: Player) {
        jobs[player.uniqueId]?.cancel()
    }
}