package me.gabber235.typewriter.entries.audience

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Page
import me.gabber235.typewriter.entry.PageType
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.cinematic.isPlayingCinematic
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.events.AsyncCinematicEndEvent
import me.gabber235.typewriter.events.AsyncCinematicStartEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@Entry(
    "cinematic_audience",
    "Filters an audience based on if they are in a cinematic",
    Colors.MEDIUM_SEA_GREEN,
    "mdi:movie"
)
/**
 * The `Cinematic Audience` entry filters an audience based on if they are in a cinematic.
 *
 * If no cinematic is referenced, it will filter based on if any cinematic is active.
 *
 * ## How could this be used?
 * This could be used to hide the sidebar or boss bar when a cinematic is playing.
 */
class CinematicAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("When not set it will filter based on if any cinematic is active.")
    @Page(PageType.CINEMATIC)
    @SerializedName("cinematic")
    val pageId: String = "",
    @Help("When inverted, the audience will be filtered when not in a cinematic.")
    val inverted: Boolean = false
) : AudienceFilterEntry {
    override fun display(): AudienceFilter = CinematicAudienceFilter(
        ref(),
        pageId,
        inverted,
    )
}

class CinematicAudienceFilter(
    ref: Ref<out AudienceFilterEntry>,
    private val pageId: String,
    private val inverted: Boolean,
) : AudienceFilter(ref) {
    override fun filter(player: Player): Boolean {
        val inCinematic = if (pageId.isNotBlank()) player.isPlayingCinematic(pageId) else player.isPlayingCinematic()
        return if (inverted) !inCinematic else inCinematic
    }

    @EventHandler
    fun onCinematicStart(event: AsyncCinematicStartEvent) {
        if (pageId.isNotBlank() && event.pageId != pageId) return
        event.player.updateFilter(!inverted)
    }

    @EventHandler
    fun onCinematicEnd(event: AsyncCinematicEndEvent) {
        if (pageId.isNotBlank() && event.pageId != pageId) return
        event.player.updateFilter(inverted)
    }
}

