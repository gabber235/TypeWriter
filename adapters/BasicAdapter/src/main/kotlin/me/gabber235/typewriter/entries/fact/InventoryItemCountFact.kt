package me.gabber235.typewriter.entries.fact

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.asMini
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

@Entry(
    "inventory_item_count_fact",
    "The amount of a specific item in the player's inventory",
    Colors.PURPLE,
    Icons.BAG_SHOPPING
)
data class InventoryItemCountFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    @Help("The material the item needs to be.")
    private val material: Optional<Material> = Optional.empty(),
    @Help("The name of the item.")
    private val itemName: Optional<String> = Optional.empty(),
) : ReadableFactEntry {
    private fun isValid(item: ItemStack): Boolean {
        if (material.isPresent) {
            if (item.type != material.get()) return false
        }
        if (itemName.isPresent) {
            if (item.itemMeta?.displayName() != itemName.get().asMini()) return false
        }
        return true
    }

    override fun read(playerId: UUID): Fact {
        val player = server.getPlayer(playerId) ?: return Fact(id, 0)
        val amount = player.inventory.contents.filterNotNull().filter { isValid(it) }.sumOf { it.amount }
        return Fact(id, amount)
    }
}
