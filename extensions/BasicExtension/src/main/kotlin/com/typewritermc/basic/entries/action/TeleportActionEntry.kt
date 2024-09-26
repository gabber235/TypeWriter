package com.typewritermc.basic.entries.action

import com.google.gson.annotations.SerializedName
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.WithRotation
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.CustomTriggeringActionEntry
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.toBukkitLocation
import org.bukkit.entity.Player

@Entry("teleport", "Teleport a player", Colors.RED, "teenyicons:google-streetview-solid")
/**
 * The `Teleport Action` entry is used to teleport a player to a location.
 *
 * ## How could this be used?
 * This could be used to teleport a player to a location when they click a button.
 * Or it could be used for a fast travel system where players talk to an NPC and are teleported to a location.
 */
class TeleportActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
    @WithRotation
    val location: Position = Position.ORIGIN,
) : CustomTriggeringActionEntry {
    override fun execute(player: Player) {
        SYNC.launch {
            player.teleport(location.toBukkitLocation())
            super.execute(player)
            player.triggerCustomTriggers()
        }
    }
}