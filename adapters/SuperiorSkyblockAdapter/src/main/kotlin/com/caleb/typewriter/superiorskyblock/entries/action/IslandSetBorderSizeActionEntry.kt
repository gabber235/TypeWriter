package com.caleb.typewriter.superiorskyblock.entries.action

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import org.bukkit.entity.Player

@Entry("island_set_border_size", "Set a player's island's border size", Colors.RED, "fa6-solid:border-all")
/**
 * The `Island Set Border Size` action is used to set a player's island's border size.
 *
 * ## How could this be used?
 *
 * It could be used to reward the player for completing a quest, or upon reaching a certain level.
 */
class IslandSetBorderSizeActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The size to set the island's border to")
    val size: Int = 0
) : ActionEntry {

    override fun execute(player: Player) {
        super.execute(player)

        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.islandSize = size
    }
}