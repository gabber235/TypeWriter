package com.typewritermc.basic.entries.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.SidebarEntry
import com.typewritermc.engine.paper.entry.entries.Var
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
    override val title: Var<String> = ConstVar(""),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : SidebarEntry