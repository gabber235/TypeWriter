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

@Entry("island_set_member_limit", "Set a player's island's member limit", Colors.RED, "fa6-solid:people-group")
/**
 * The `Island Set Member Limit Action` is an action that sets the member limit of an island.
 *
 * ## How could this be used?
 *
 * This could be used as a reward for a quest or as if they reach a certain level.
 *
 */
class IslandSetMemberLimitActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The new limit to set the island's member limit to")
    val size: Int = 0
) : ActionEntry {

    override fun execute(player: Player) {
        super.execute(player)

        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.teamLimit = size
    }
}