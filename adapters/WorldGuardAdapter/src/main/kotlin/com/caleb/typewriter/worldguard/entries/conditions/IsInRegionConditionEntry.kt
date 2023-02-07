package com.caleb.typewriter.worldguard.entries.conditions

import com.google.gson.annotations.SerializedName
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("region_in_region_condition", "[WorldGuard] Continues if a player is in a region", Colors.PINK, Icons.FILTER)
class IsInRegionConditionEntry(
    override val id: String,
    override val name: String,
    @SerializedName("triggers")
    @Triggers
    @EntryIdentifier(TriggerableEntry::class)
    val nextTriggers: List<String> = emptyList(),
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    private var region: String = "",
    private var isInRegion: Boolean = true

) : ActionEntry {

    override val triggers: List<String>
        get() = emptyList()

    override fun execute(player: Player) {

        val regionContainer = WorldGuard.getInstance().platform.regionContainer
        val regionManager = regionContainer.get(BukkitAdapter.adapt(player.world))
        val regions = regionManager?.getApplicableRegions(BukkitAdapter.asBlockVector(player.location)) ?: return

        if(isInRegion && region in regions.map { it.id }) {
            super.execute(player)
            InteractionHandler.startInteractionAndTrigger(player, nextTriggers.map { EntryTrigger(it) })
        }
        else if(!isInRegion && region !in regions.map { it.id }) {
            super.execute(player)
            InteractionHandler.startInteractionAndTrigger(player, nextTriggers.map { EntryTrigger(it) })
        }

    }
}