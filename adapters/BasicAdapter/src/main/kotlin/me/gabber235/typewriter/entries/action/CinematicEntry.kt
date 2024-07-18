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
 * A cinematic can only be overridden
 * if another cinematic is triggered with a higher page priority than the current one.
 *
 * ## How could this be used?
 *
 * This action can be useful in situations where you want to start a cinematic.
 * See the [Cinematic](/docs/first-cinematic) tutorial for more information.
 */
class CinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
    @SerializedName("page")
    @Page(PageType.CINEMATIC)
    @Help("The cinematic page to start.")
    val pageId: String = "",
) : CustomTriggeringActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        CinematicStartTrigger(pageId, customTriggers) triggerFor player
    }
}