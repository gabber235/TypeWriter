package me.gabber235.typewriter.entries.audience

import io.papermc.paper.event.player.PlayerPickItemEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.utils.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.inventory.InventoryAction.*
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

@Entry(
    "item_in_slot_audience",
    "Filters an audience based on if they have a specific item in a specific slot",
    Colors.MEDIUM_SEA_GREEN,
    "mdi:hand"
)
/**
 * The `Item In Slot Audience` entry filters an audience based on if they have a specific item in a specific slot.
 *
 * ## How could this be used?
 * It can be used to have magnet boots which allow players to move in certain areas.
 */
class ItemInSlotAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("The item to check for.")
    val item: Item = Item.Empty,
    @Help("The slot to check.")
    val slot: Int = 0,
    override val inverted: Boolean = false,
) : AudienceFilterEntry, Invertible {
    override fun display(): AudienceFilter = ItemInSlotAudienceFilter(ref(), item, slot)
}

class ItemInSlotAudienceFilter(
    ref: Ref<out AudienceFilterEntry>,
    private val item: Item,
    private val slot: Int,
) : AudienceFilter(ref), TickableDisplay {
    override fun filter(player: Player): Boolean {
        val itemInSlot = player.inventory.getItem(slot) ?: return false
        return item.isSameAs(player, itemInSlot)
    }

    override fun tick() {
        consideredPlayers.forEach { it.refresh() }
    }
}