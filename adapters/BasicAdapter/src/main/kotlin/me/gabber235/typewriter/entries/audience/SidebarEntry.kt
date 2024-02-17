package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import org.bukkit.entity.Player

@Entry("sidebar", "Display a sidebar for players", Colors.DARK_ORANGE, "mdi:page-layout-sidebar-right")
/**
 * The `SidebarEntry` is a display that shows a sidebar to players.
 *
 * To display lines on the sidebar, use the `SidebarLinesEntry` as its descendants.
 *
 * ## How could this be used?
 * This could be used to show a list of objectives, or the region a player is in.
 */
class SimpleSidebarEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val title: String = "",
) : SidebarEntry {
    override fun display(): AudienceFilter = PassThroughFilter(ref())
}

@Entry("simple_sidebar_lines", "Lines for a sidebar", Colors.ORANGE_RED, "bi:layout-text-sidebar")
/**
 * The `SimpleSidebarLinesEntry` is a display that shows lines on a sidebar.
 *
 * Separating lines with a newline character will display them on separate lines.
 *
 * ## How could this be used?
 * This could be used to show a list of objectives, or the region a player is in.
 */
class SimpleSidebarLinesEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The lines to display on the sidebar. Separate lines with a newline character.")
    @Colored
    @Placeholder
    @MultiLine
    val lines: String = "",
) : SidebarLinesEntry {
    override fun lines(player: Player): String = lines.parsePlaceholders(player)
    override fun display(): AudienceDisplay = PassThroughDisplay()
}
