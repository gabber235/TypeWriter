package me.gabber235.typewriter.entries.action

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Material
import org.bukkit.entity.Player

@Entry("give_item", "Give an item to the player", Colors.RED, "streamline:give-gift-solid")
/**
 * The `Give Item Action` is an action that gives a player an item. This action provides you with the ability to give an item with a specified Minecraft material, amount, display name, and lore.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to give players rewards for completing quests, unlockables for reaching certain milestones, or any other custom items you want to give players. The possibilities are endless!
 */
class GiveItemActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The item to give.")
    val item: Item = Item.Empty,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        player.inventory.addItem(item.build(player))
    }
}

@EntryMigration(GiveItemActionEntry::class, "0.4.0")
@NeedsMigrationIfContainsAny(["material", "amount", "displayName", "lore"])
fun migrate040GiveItemAction(json: JsonObject, context: EntryMigratorContext): JsonObject {
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