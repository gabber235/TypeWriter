package me.gabber235.typewriter.entries.audience

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.milliseconds

@Entry("boss_bar", "Boss Bar", Colors.GREEN, "carbon:progress-bar")
/**
 * The `BossBarEntry` is a display that shows a bar at the top of the screen.
 *
 * ## How could this be used?
 * This could be used to show objectives in a quest, or to show the progress of a task.
 */
class BossBarEntry(
    override val id: String = "",
    override val name: String = "",
    @Colored
    @Placeholder
    @Help("The title of the boss bar")
    val title: String = "",
    @Help("How filled up the bar is. 0.0 is empty, 1.0 is full.")
    val progress: Double = 1.0,
    @Help("The color of the boss bar")
    val color: BossBar.Color = BossBar.Color.WHITE,
    @Help("If the bossbar has notches")
    val style: BossBar.Overlay = BossBar.Overlay.PROGRESS,
    @Help("Any flags to apply to the boss bar")
    val flags: List<BossBar.Flag> = emptyList(),
) : AudienceEntry {
    override fun display(): AudienceDisplay {
        return BossBarDisplay(title, progress, color, style, flags)
    }
}

class BossBarDisplay(
    private val title: String,
    private val progress: Double,
    private val color: BossBar.Color,
    private val style: BossBar.Overlay,
    private val flags: List<BossBar.Flag>,
) : AudienceDisplay() {
    private val bars = ConcurrentHashMap<UUID, BossBar>()
    private var job: Job? = null

    override fun initialize() {
        super.initialize()
        job = DISPATCHERS_ASYNC.launch {
            while (true) {
                for ((id, bar) in bars) {
                    bar.name(title.parsePlaceholders(id).asMini())
                }
                delay(50.milliseconds)
            }
        }
    }

    override fun onPlayerAdd(player: Player) {
        val bar = BossBar.bossBar(
            title.parsePlaceholders(player).asMini(),
            progress.toFloat(),
            color,
            style,
            flags.toSet()
        )
        bars[player.uniqueId] = bar
        player.showBossBar(bar)
    }

    override fun onPlayerRemove(player: Player) {
        bars.remove(player.uniqueId)?.let { player.hideBossBar(it) }
    }

    override fun dispose() {
        super.dispose()
        job?.cancel()
        job = null
    }
}