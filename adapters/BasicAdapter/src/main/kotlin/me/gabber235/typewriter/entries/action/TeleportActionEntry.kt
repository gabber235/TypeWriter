package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.WithRotation
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.ThreadType.SYNC
import org.bukkit.Location
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
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @WithRotation
    @Help("The location to teleport the player to.")
    val location: Location = Location(null, 0.0, 0.0, 0.0),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        SYNC.launch {
            player.teleport(location)
        }
    }
}