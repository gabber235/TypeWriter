package com.typewritermc.basic.entries.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Colored
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.MultiLine
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.entries.LinesEntry
import org.bukkit.entity.Player
import java.util.*

@Entry("simple_lines", "Statically determined lines of text", Colors.ORANGE_RED, "bi:layout-text-sidebar")
/**
 * The `SimpleSidebarLinesEntry` is a display that shows lines.
 *
 * Separating lines with a newline character will display them on separate lines.
 *
 * ## How could this be used?
 * This could be used to show a list of objectives, or the region a player is in.
 */
class SimpleLinesEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The lines to display on the sidebar. Separate lines with a newline character.")
    @Colored
    @Placeholder
    @MultiLine
    val lines: String = "",
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : LinesEntry {
    override fun lines(player: Player): String = lines
}