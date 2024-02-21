package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.PassThroughFilter
import me.gabber235.typewriter.entry.entries.SidebarEntry
import me.gabber235.typewriter.entry.ref
import java.util.*

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
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : SidebarEntry {
    override fun display(): AudienceFilter = PassThroughFilter(ref())
}