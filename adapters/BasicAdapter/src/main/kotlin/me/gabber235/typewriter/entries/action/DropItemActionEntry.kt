package me.gabber235.typewriter.entries.action

import com.github.shynixn.mccoroutine.bukkit.launch
import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

@Entry("drop_item", "Drop an item at location, or on player", Colors.RED, Icons.DROPBOX)
/**
 * The `Drop Item Action` is an action that drops an item in the world.
 * This action provides you with the ability to drop an item with a specified Minecraft material, amount, display name, lore, and location.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations.
 * You can use it to create treasure chests with randomized items, drop loot from defeated enemies, or spawn custom items in the world.
 * The possibilities are endless!
 */
class DropItemActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    override val triggers: List<String> = emptyList(),
    @Help("The item to drop.")
    val item: Item = Item.Empty,
    @Help("The location to drop the item. (Defaults to the player's location)")
    // The location to drop the item at. If this field is left blank, the item will be dropped at the location of the player triggering the action.
    private val location: Optional<Location> = Optional.empty(),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        // Run on main thread
        plugin.launch {
            if (location.isPresent) {
                location.get().world.dropItem(location.get(), item.build(player))
            } else {
                player.location.world.dropItem(player.location, item.build(player))
            }
        }
    }
}

@EntryMigration(DropItemActionEntry::class, "0.4.0")
@NeedsMigrationIfContainsAny(["material", "amount", "displayName", "lore"])
fun migrate040DropItemAction(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "material", "amount", "displayName", "lore")

    val material = json.getAndParse<Material>("material", context.gson).optional
    val amount = json.getAndParse<Int>("amount", context.gson).optional
    val displayName = json.getAndParse<String>("displayName", context.gson).optional
    val lore = json.getAndParse<String>("lore", context.gson).optional

    val item = Item(
        material = material,
        amount = amount,
        name = displayName,
        lore = lore,
    )
    data["item"] = context.gson.toJsonTree(item)

    return data
}
