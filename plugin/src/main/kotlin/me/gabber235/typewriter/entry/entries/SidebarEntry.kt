package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.PlaceholderEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import org.bukkit.entity.Player

@Tags("sidebar")
interface SidebarEntry : AudienceFilterEntry, PlaceholderEntry {
    @Help("The title of the sidebar")
    @Colored
    @Placeholder
    val title: String

    override fun display(player: Player?): String? = title.parsePlaceholders(player)
}

@Tags("sidebar_lines")
interface SidebarLinesEntry : AudienceEntry, PlaceholderEntry {
    /**
     * The lines of the sidebar.
     * Multiple lines are separated by a newline character.
     */
    fun lines(player: Player): String

    override fun display(player: Player?): String? = player?.let { lines(it) }
}