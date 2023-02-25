package com.caleb.typewriter.superiorskyblock.entries.condition

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.entry.triggerAllFor
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Bukkit
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
    private var aboveLevel: Int = 0,
    private var belowLevel: Int = 0

) : ActionEntry {

    override val triggers: List<String>
        get() = emptyList()

    override fun execute(player: Player) {

        var sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        var island = sPlayer.getIsland()

        if(island == null) {
            return
        }

        var islandLevel = island.getIslandLevel().toInt()

        if(belowLevel == 0) {
            belowLevel = islandLevel
        }

        if(aboveLevel == 0) {
            aboveLevel = islandLevel
        }

        if(islandLevel in aboveLevel..belowLevel) {
            super.execute(player)
            InteractionHandler.startInteractionAndTrigger(player, nextTriggers.map { EntryTrigger(it) })
        }

    }
}