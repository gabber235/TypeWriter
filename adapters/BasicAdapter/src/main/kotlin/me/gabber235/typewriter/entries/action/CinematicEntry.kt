package me.gabber235.typewriter.entries.action

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Page
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.CinematicStartTrigger
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
import org.bukkit.entity.Player


@Entry("cinematic", "Start a new cinematic", Colors.RED, "fa-solid:camera-retro")
/**
 * The `Cinematic` action is used to start a new cinematic.
 *
 * ## How could this be used?
 *
 * This action can be useful in situations where you want to start a cinematic.
 * See the [Cinematic](/docs/first-cinematic) tutorial for more information.
 */
class CinematicEntry(
    override val id: String = "",
    override val name: String = "",
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @SerializedName("page")
    @Page(PageType.CINEMATIC)
    @Help("The cinematic page to start.")
    val pageId: String = "",
    @Help("If the player is already in a cinematic, should the cinematic be replaced?")
    val override: Boolean = false
) : CustomTriggeringActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        CinematicStartTrigger(pageId, customTriggers, override) triggerFor player
    }
}