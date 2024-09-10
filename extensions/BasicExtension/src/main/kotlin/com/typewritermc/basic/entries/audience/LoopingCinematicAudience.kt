package com.typewritermc.basic.entries.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.books.pages.PageType
import com.typewritermc.core.entries.Query
import com.typewritermc.core.extension.annotations.Page
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.entry.matches
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.ThreadType
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Entry(
    "looping_cinematic_audience",
    "Show the audience members a cinematic that loops",
    Colors.GREEN,
    "mdi:movie-open-play"
)
/**
 * The `Looping Cinematic Audience` entry is used to show the audience members a cinematic that loops.
 *
 * **It is recommended that this entry is bounded by location or region,
 * to prevent players from receiving packets for cinematics they cannot see.**
 *
 * :::caution
 * The Cinematic can only have entries that are compatible with looping (non-primary entries).
 * Anything that cannot have two or more instances active at once will not work.
 * :::
 *
 * ## How could this be used?
 * To display particles on a loop, such as a fountain.
 * Or sparks that come from a broken wire.
 */
class LoopingCinematicAudience(
    override val id: String = "",
    override val name: String = "",
    @Page(PageType.CINEMATIC)
    val cinematicId: String = "",
) : AudienceEntry {
    override fun display(): AudienceDisplay {
        val entries = Query.findWhereFromPage<CinematicEntry>(cinematicId) { true }.toList()

        val inValidEntries = entries.filterIsInstance<PrimaryCinematicEntry>().map { it.name }

        if (inValidEntries.isNotEmpty()) {
            logger.warning("The cinematic $cinematicId has primary entries that cannot be looped: $inValidEntries, skipping these entries.")
        }

        val loopingEntries = entries.filter { it !is PrimaryCinematicEntry }

        return LoopingCinematicAudienceDisplay(loopingEntries)
    }
}

private class LoopingCinematicAudienceDisplay(
    private val loopingEntries: List<CinematicEntry>,
) : AudienceDisplay(), TickableDisplay {
    private val tracked = ConcurrentHashMap<UUID, LoopingCinematicPlayerDisplay>()

    override fun tick() {
        tracked.values.forEach { it.tick() }
    }

    override fun onPlayerAdd(player: Player) {
        tracked[player.uniqueId] = LoopingCinematicPlayerDisplay(player, loopingEntries)
    }

    override fun onPlayerRemove(player: Player) {
        tracked.remove(player.uniqueId)?.teardown()
    }
}

private class LoopingCinematicPlayerDisplay(
    private val player: Player,
    private val loopingEntries: List<CinematicEntry>,
) {
    private var display = setupDisplay()

    private fun setupDisplay(): CinematicDisplay {
        val actions = loopingEntries.filter { it.criteria.matches(player) }.map { it.create(player) }
        return CinematicDisplay(actions).also { it.setup() }
    }

    fun tick() {
        display.tick()

        if (display.isFinished) {
            display.teardown()
            display = setupDisplay()
        }
    }

    fun teardown() {
        display.teardown()
    }
}

private class CinematicDisplay(
    private val actions: List<CinematicAction>,
) {
    private var hasSetupCompleted = false
    var frame: Int = -1

    val isFinished: Boolean
        get() = actions.all { it canFinish frame }

    fun setup() {
        ThreadType.DISPATCHERS_ASYNC.launch {
            actions.forEach { it.setup() }
            hasSetupCompleted = true
        }
    }

    fun tick() {
        if (!hasSetupCompleted) return
        frame++
        ThreadType.DISPATCHERS_ASYNC.launch {
            actions.forEach { it.tick(frame) }
        }
    }

    fun teardown() {
        ThreadType.DISPATCHERS_ASYNC.launch {
            actions.forEach { it.teardown() }
        }
    }
}