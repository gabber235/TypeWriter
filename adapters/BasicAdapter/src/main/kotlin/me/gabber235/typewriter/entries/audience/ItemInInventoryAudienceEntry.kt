package me.gabber235.typewriter.entries.audience

import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.delay
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.ThreadType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerPickupItemEvent

@Entry(
    "item_in_inventory_audience",
    "Filters an audience based on if they have a specific item in their inventory",
    Colors.MEDIUM_SEA_GREEN,
    "mdi:bag-personal"
)
/**
 * The `Item In Inventory Audience` entry filters an audience based on if they have a specific item in their inventory.
 *
 * ## How could this be used?
 * This could show a boss bar or sidebar based on if a player has a specific item in their inventory.
 */
class ItemInInventoryAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("The item to check for in the inventory.")
    val item: Item = Item.Empty,
    override val inverted: Boolean = false,
) : AudienceFilterEntry, Invertible {
    override fun display(): AudienceFilter = ItemInInventoryAudienceFilter(ref(), item)
}

class ItemInInventoryAudienceFilter(
    ref: Ref<out AudienceFilterEntry>,
    private val item: Item,
) : AudienceFilter(ref), TickableDisplay {
    override fun filter(player: Player): Boolean {
        return player.inventory.contents.any { it != null && item.isSameAs(player, it) } || item.isSameAs(player, player.itemOnCursor)
    }

    override fun tick() {
        consideredPlayers.forEach { it.refresh() }
    }
}
