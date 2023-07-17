package me.gabber235.typewriter.entries.action

import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.asMini
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

@Entry("remove_item", "Remove an item from the players inventory", Colors.RED, Icons.WAND_SPARKLES)
/**
 * The `Remove Item Action` is an action that removes an item from the player's inventory.
 * This action provides you with the ability to remove items from the player's inventory in response to specific events.
 *
 * ## How could this be used?
 *
 * This can be used when `giving` an NPC an item, and you want to remove the item from the player's inventory.
 * Or when you want to remove an item from the player's inventory when they complete a quest or achievement.
 */
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
    @Help("Does the player need to have the exact amount of items?")
    private val exactAmount: Boolean = false,
    @Placeholder
    @Colored
    @Help("The name of the item.")
    // If the name is given, the item must have the same name to be removed.
    private val itemName: Optional<String> = Optional.empty(),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val item = ItemStack(material, amount).meta {
            itemName.ifPresent {
                displayName(it.asMini())
            }
        }
        if (exactAmount) {
            if (player.inventory.containsAtLeast(item, amount)) {
                player.inventory.removeItemAnySlot(item)
            }
        } else {
            player.inventory.removeItemAnySlot(item)
        }
    }
}

