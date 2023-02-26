package com.caleb.typewriter.superiorskyblock.entries.condition

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("superiorskyblock_is_island_level_condition", "[SuperiorSkyblock] Continues if a player's island meets level criteria", Colors.PINK, Icons.FILTER)
class IsIslandLevelConditionEntry(
    override val id: String,
    override val name: String,
    @SerializedName("triggers")
    @Triggers
    @EntryIdentifier(TriggerableEntry::class)
    val nextTriggers: List<String> = emptyList(),
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    @Help("The level above which the island must be")
    private var aboveLevel: Int = 0,
    @Help("The level below which the island must be")
    private var belowLevel: Int = 0

) : ActionEntry {

    override val triggers: List<String>
        get() = emptyList()

    override fun execute(player: Player) {

        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island ?: return

        val islandLevel = island.islandLevel.toInt()

        if (belowLevel == 0) {
            belowLevel = islandLevel
        }

        if (aboveLevel == 0) {
            aboveLevel = islandLevel
        }

        if (islandLevel in aboveLevel..belowLevel) {
            super.execute(player)
            nextTriggers.triggerEntriesFor(player)
        }

    }
}