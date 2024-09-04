package com.typewritermc.basic.entries.action

import com.google.gson.annotations.SerializedName
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.books.pages.PageType
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Page
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.CinematicStartTrigger
import com.typewritermc.engine.paper.entry.entries.CustomTriggeringActionEntry
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
 * See the [Cinematic](/docs/creating-stories/cinematics) tutorial for more information.
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