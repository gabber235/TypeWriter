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
import org.bukkit.block.Biome
import org.bukkit.entity.Player

@Entry("island_set_biome", "Set a player's island's biome", Colors.RED, "fa6-solid:map")
/**
 * The `Island Set Biome` action is used to set a player's island's biome.
 *
 * ## How could this be used?
 *
 * This could be used to simulate the seasons of the year, or to change the biome of the island to match the theme of the island.
 */
class IslandSetBiomeActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val biome: Biome = Biome.PLAINS
) : ActionEntry {
    override fun ActionTrigger.execute() {
        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.biome = biome
    }
}