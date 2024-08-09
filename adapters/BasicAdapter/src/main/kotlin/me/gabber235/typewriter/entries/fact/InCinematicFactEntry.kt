package me.gabber235.typewriter.entries.fact

import com.google.gson.annotations.SerializedName
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

@Entry("in_cinematic_fact", "If the player is in a cinematic", Colors.PURPLE, "eos-icons:storage-class")
/**
 * The 'In Cinematic Fact' is a fact that returns 1 if the player has an active cinematic, and 0 if not.
 *
 * If no cinematic is referenced, it will filter based on if any cinematic is active.
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
    @Help("When not set it will filter based on if any cinematic is active.")
    @Page(PageType.CINEMATIC)
    @SerializedName("cinematic")
    val pageId: String = "",
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val inCinematic = if (pageId.isNotBlank())
            player.isPlayingCinematic(pageId)
        else
            player.isPlayingCinematic()

        return FactData(inCinematic.toInt())
    }
}
