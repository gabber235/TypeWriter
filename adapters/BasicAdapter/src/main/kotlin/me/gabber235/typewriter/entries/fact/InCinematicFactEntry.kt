package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Page
import me.gabber235.typewriter.entry.PageType
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.cinematic.isPlayingCinematic
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import org.bukkit.entity.Player

@Entry("in_cinematic_fact", "Tests whether the player is in a cinematic.", Colors.PURPLE, "pepicons-pop:camera")
/**
 * The 'In Cinematic Fact' is a fact that returns 1 if the player has an active cinematic, and 0 if not.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 * With this fact, it is possible to make an entry only take action if the player does not have an active cinematic.
 */
class InCinematicFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("The Cinematic page, which must be active for the player for the fact to be true (1).")
    @Page(PageType.CINEMATIC)
    val pageID: String = ""
): ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        if (player.isPlayingCinematic(pageID)) return FactData(1) else return FactData(0)
    }
}
