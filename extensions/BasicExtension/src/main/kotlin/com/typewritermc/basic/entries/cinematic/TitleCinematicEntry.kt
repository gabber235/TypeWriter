package com.typewritermc.basic.entries.cinematic

import io.papermc.paper.util.Tick
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.cinematic.SimpleCinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.PrimaryCinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.utils.asMini
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import java.time.Duration

@Entry("title_cinematic", "Show a title during a cinematic", Colors.CYAN, "fluent:align-center-vertical-32-filled")
/**
 * The `Title Cinematic` entry shows a title during a cinematic.
 *
 * ## How could this be used?
 *
 * This entry could be used to show a title during a cinematic, such as a title for a cutscene.
 */
class TitleCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = "fluent:align-center-vertical-32-filled")
    val segments: List<TitleSegment> = emptyList(),
) : PrimaryCinematicEntry {
    override fun create(player: Player): CinematicAction {
        return TitleCinematicAction(
            player,
            this,
        )
    }
}

data class TitleSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val fadeIn: Long = 20,
    val fadeOut: Long = 20,
) : Segment

class TitleCinematicAction(
    private val player: Player,
    entry: TitleCinematicEntry,
) : SimpleCinematicAction<TitleSegment>() {

    override val segments: List<TitleSegment> = entry.segments

    override suspend fun startSegment(segment: TitleSegment) {
        super.startSegment(segment)

        val totalDuration: Int = segment.endFrame - segment.startFrame
        val adjustedDuration: Long = (totalDuration - segment.fadeIn - segment.fadeOut).coerceAtLeast(0)

        val times: Title.Times = Title.Times.times(
            Duration.of(segment.fadeIn, Tick.tick()),
            Duration.of(adjustedDuration, Tick.tick()),
            Duration.of(segment.fadeOut, Tick.tick())
        )

        val title: Title = Title.title(
            segment.title.parsePlaceholders(player).asMini(),
            segment.subtitle.parsePlaceholders(player).asMini(),
            times
        )

        player.showTitle(title)
    }

    override suspend fun stopSegment(segment: TitleSegment) {
        super.stopSegment(segment)
        player.resetTitle()
    }
}