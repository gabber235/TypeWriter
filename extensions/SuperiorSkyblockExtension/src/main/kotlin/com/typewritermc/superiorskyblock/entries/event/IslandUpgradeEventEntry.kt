package com.typewritermc.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandUpgradeEvent
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import org.bukkit.entity.Player

@Entry("on_island_upgrade", "When a player upgrades their Skyblock island", Colors.YELLOW, "fa6-solid:arrow-up")
/**
 * The `Island Upgrade Event` is fired when a player upgrades their island.
 *
 * ## How could this be used?
 *
 * This event could be used to give players a reward when they upgrade their island.
 */
class IslandUpgradeEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry

@EntryListener(IslandUpgradeEventEntry::class)
fun onUpgradeCommand(event: IslandUpgradeEvent, query: Query<IslandUpgradeEventEntry>) {
    val sPlayer: SuperiorPlayer = event.player ?: return
    val player: Player = sPlayer.asPlayer() ?: return

    if (sPlayer.island == null) return

    query.find().triggerAllFor(player, context())
}