package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Entry("remove_item", "Remove an item from the players inventory", Colors.RED, Icons.WAND_SPARKLES)
class RemoveItemActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    override val triggers: List<String> = emptyList(),
    @Help("The item to remove.")
    private val material: Material = Material.AIR,
    @Help("The amount of items to remove.")
    private val amount: Int = 1,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val item = ItemStack(material, amount)
        player.inventory.removeItemAnySlot(item)
    }
}