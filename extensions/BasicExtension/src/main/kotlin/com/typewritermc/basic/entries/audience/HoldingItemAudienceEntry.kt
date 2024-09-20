package com.typewritermc.basic.entries.audience

import io.papermc.paper.event.player.PlayerPickItemEvent
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.AudienceFilter
import com.typewritermc.engine.paper.entry.entries.AudienceFilterEntry
import com.typewritermc.engine.paper.entry.entries.Invertible
import com.typewritermc.core.entries.ref
import com.typewritermc.engine.paper.utils.item.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent

@Entry(
    "holding_item_audience",
    "Filters an audience based on if they are holding a specific item",
    Colors.MEDIUM_SEA_GREEN,
    "mdi:hand"
)
/**
 * The `Holding Item Audience` entry is an audience filter that filters an audience based on if they are holding a specific item.
 * The audience will only contain players that are holding the specified item.
 *
 * ## How could this be used?
 * Could show a path stream to a location when the player is holding a map.
 */
class HoldingItemAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("The item to check for.")
    val item: Item = Item.Empty,
    override val inverted: Boolean = false,
) : AudienceFilterEntry, Invertible {
    override fun display(): AudienceFilter = HoldingItemAudienceFilter(ref(), item)
}

class HoldingItemAudienceFilter(
    ref: Ref<out AudienceFilterEntry>,
    private val item: Item,
) : AudienceFilter(ref) {
    override fun filter(player: Player): Boolean {
        val holdingItem = player.inventory.itemInMainHand
        return item.isSameAs(player, holdingItem)
    }

    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        val player = event.player
        val newHoldingItem = player.inventory.getItem(event.newSlot)
        player.updateFilter(item.isSameAs(player, newHoldingItem))
    }

    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) = onInventoryEvent(event)

    @EventHandler
    fun onInventoryDragEvent(event: InventoryDragEvent) = onInventoryEvent(event)

    @EventHandler
    fun onInventoryOpenEvent(event: InventoryOpenEvent) = onInventoryEvent(event)

    @EventHandler
    fun onInventoryCloseEvent(event: InventoryCloseEvent) = onInventoryEvent(event)

    @EventHandler
    fun onInventoryEvent(event: InventoryEvent) {
        val player = event.view.player as? Player ?: return
        player.refresh()
    }

    @EventHandler
    fun onPickupItem(event: PlayerPickItemEvent) = event.player.refresh()

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) = event.player.refresh()
}