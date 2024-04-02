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
import java.util.Optional

@Entry("in_cinematic_fact", "If the player has the given MMOCore class", Colors.PURPLE, "eos-icons:storage-class")
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
    val pageID: Optional<String> = Optional.empty()
): ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        if (pageID == null)
            if (player.isPlayingCinematic()) return FactData(1) else return FactData(0)
        else
            if (player.isPlayingCinematic(pageID.toString())) return FactData(1) else return FactData(0)
    }
}
