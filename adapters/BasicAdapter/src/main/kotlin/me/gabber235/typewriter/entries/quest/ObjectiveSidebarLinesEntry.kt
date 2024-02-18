package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.PassThroughDisplay
import me.gabber235.typewriter.entry.entries.SidebarLinesEntry
import me.gabber235.typewriter.entry.entries.trackedShowingObjectives
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.asMiniWithResolvers
import org.bukkit.entity.Player

@Entry(
    "objective_sidebar_lines",
    "Display all the current objectives in the sidebar",
    Colors.ORANGE_RED,
    "fluent:clipboard-task-list-ltr-24-filled"
)
/**
 * The `ObjectiveSidebarLinesEntry` is a display that shows all the current objectives in the sidebar.
 *
 * ## How could this be used?
 * This could be used to show a list of tracked objectives
 */
class ObjectiveSidebarLinesEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The format for the line. Use <objective> to replace with the objective name.")
    @Colored
    @Placeholder
    @MultiLine
    val format: String = "<objective>",
) : SidebarLinesEntry {
    override fun lines(player: Player): String {
        return player.trackedShowingObjectives().joinToString("\n") {
            format.parsePlaceholders(player).asMiniWithResolvers(
                net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed(
                    "objective",
                    it.display(player)
                )
            ).asMini()
        }
    }

    override fun display(): AudienceDisplay = PassThroughDisplay()
}