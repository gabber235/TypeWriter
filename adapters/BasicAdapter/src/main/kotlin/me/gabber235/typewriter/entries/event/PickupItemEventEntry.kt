package me.gabber235.typewriter.entries.event

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent

@Entry("on_item_pickup", "When the player picks up an item", Colors.YELLOW, "fa6-solid:hand-holding-medical")
/**
 * The `Pickup Item Event` is triggered when the player picks up an item.
 *
 * ## How could this be used?
 *
 * This event could be used to trigger a quest or to trigger a cutscene, when the player picks up a specific item.
 */
class PickupItemEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The item to listen for.")
    val item: Item = Item.Empty,
) : EventEntry

@EntryListener(PickupItemEventEntry::class)
fun onPickupItem(event: EntityPickupItemEvent, query: Query<PickupItemEventEntry>) {
    if (event.entity !is Player) return

    val player = event.entity as Player

    query findWhere { entry ->
        entry.item.isSameAs(player, event.item.itemStack)
    } startDialogueWithOrNextDialogue player
}

@EntryMigration(PickupItemEventEntry::class, "0.4.0")
@NeedsMigrationIfContainsAny(["material"])
fun migrate040PickupItemEvent(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "material")

    val material = json.getAndParse<Material>("material", context.gson).optional
    val item = Item(
        material = material,
    )
    data["item"] = context.gson.toJsonTree(item)

    return data
}
