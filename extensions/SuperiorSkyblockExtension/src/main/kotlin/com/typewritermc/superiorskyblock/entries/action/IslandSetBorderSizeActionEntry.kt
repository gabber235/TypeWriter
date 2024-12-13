package com.typewritermc.superiorskyblock.entries.action

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
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
    val size: Int = 0
) : ActionEntry {
    override fun ActionTrigger.execute() {
        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.islandSize = size
    }
}