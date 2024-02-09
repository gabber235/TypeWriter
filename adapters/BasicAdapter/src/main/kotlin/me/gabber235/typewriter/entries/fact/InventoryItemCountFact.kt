package me.gabber235.typewriter.entries.fact

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

@Entry(
    "inventory_item_count_fact",
    "The amount of a specific item in the player's inventory",
    Colors.PURPLE,
    Icons.BAG_SHOPPING
)
/**
 * The `Inventory Item Count Fact` is a fact that returns the amount of a specific item in the player's inventory.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 *
 * This could be used to check if the player has a specific item in their inventory, or to check if they have a specific amount of an item.
 * Like giving the player a quest to collect 10 apples, and then checking if they have 10 apples in their inventory.
 */
class InventoryItemCountFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val audience: Ref<GroupEntry> = emptyRef(),
    @Help("The item to check for.")
    val item: Item = Item.Empty,
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val amount = player.inventory.contents.filterNotNull().filter { item.isSameAs(player, it) }.sumOf { it.amount }
        return FactData(amount)
    }
}

@EntryMigration(InventoryItemCountFact::class, "0.4.0")
@NeedsMigrationIfContainsAny(["material", "itemName"])
fun migrate040InventoryItemCount(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "material", "itemName")

    val material = json.getAndParse<Optional<Material>>("material", context.gson).optional
    val displayName = json.getAndParse<Optional<String>>("itemName", context.gson).optional

    val item = Item(
        material = material,
        name = displayName,
    )
    data["item"] = context.gson.toJsonTree(item)

    return data
}
