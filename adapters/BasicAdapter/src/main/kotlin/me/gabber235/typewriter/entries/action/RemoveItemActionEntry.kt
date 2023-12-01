package me.gabber235.typewriter.entries.action

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

@Entry("remove_item", "Remove an item from the players inventory", Colors.RED, Icons.WAND_SPARKLES)
/**
 * The `Remove Item Action` is an action that removes an item from the player's inventory.
 * This action provides you with the ability to remove items from the player's inventory in response to specific events.
 * <Admonition type="caution">
 *     This action will try to remove "as much as possible" but does not verify if the player has enough items in their inventory.
 *     If you want to guarantee that the player has enough items in their inventory, add an
 *     <Link to='../fact/inventory_item_count_fact'>Inventory Item Count Fact</Link> to the criteria.
 * </Admonition>
 *
 * ## How could this be used?
 *
 * This can be used when `giving` an NPC an item, and you want to remove the item from the player's inventory.
 * Or when you want to remove a key from the player's inventory after they use it to unlock a door.
 */
class RemoveItemActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    override val triggers: List<String> = emptyList(),
    @Help("The item to remove.")
    val item: Item = Item.Empty,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        player.inventory.removeItemAnySlot(item.build(player))
    }
}

@EntryMigration(RemoveItemActionEntry::class, "0.4.0")
@NeedsMigrationIfContainsAny(["material", "amount", "itemName"])
fun migrate040RemoveItemAction(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "material", "amount", "itemName")

    val material = json.getAndParse<Material>("material", context.gson).optional
    val amount = json.getAndParse<Int>("amount", context.gson).optional
    val displayName = json.getAndParse<Optional<String>>("itemName", context.gson).optional

    val item = Item(
        material = material,
        amount = amount,
        name = displayName,
    )
    data["item"] = context.gson.toJsonTree(item)

    return data
}


