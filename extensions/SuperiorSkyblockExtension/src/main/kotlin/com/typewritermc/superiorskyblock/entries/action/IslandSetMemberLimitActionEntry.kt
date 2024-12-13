package com.typewritermc.superiorskyblock.entries.action

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
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
    override fun ActionTrigger.execute() {
        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.teamLimit = size
    }
}