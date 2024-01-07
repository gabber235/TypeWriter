package me.gabber235.typewriter.entries.event

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.BLOCK
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

@Entry("on_interact_with_block", "When the player interacts with a block", Colors.YELLOW, Icons.HAND_POINTER)
/**
 * The `Interact Block Event` is triggered when a player interacts with a block by right-clicking it.
 *
 * ## How could this be used?
 *
 * This could be used to create special interactions with blocks, such as opening a secret door when you right-click a certain block, or a block that requires a key to open.
 */
class InteractBlockEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
    @MaterialProperties(BLOCK)
    @Help("The block that was interacted with.")
    val block: Material = Material.AIR,
    @Help("The location of the block that was interacted with.")
    val location: Optional<Location> = Optional.empty(),
    @Help("The item the player must be holding when the block is interacted with.")
    val itemInHand: Item = Item.Empty
) : EventEntry

private fun hasItemInHand(player: Player, item: Item): Boolean {
    return item.isSameAs(player, player.inventory.itemInMainHand) || item.isSameAs(
        player,
        player.inventory.itemInOffHand
    )
}

@EntryListener(InteractBlockEventEntry::class)
fun onInteractBlock(event: PlayerInteractEvent, query: Query<InteractBlockEventEntry>) {
    if (event.clickedBlock == null) return
    if (event.action != Action.RIGHT_CLICK_BLOCK) return
    // The even triggers twice. Both for the main hand and offhand.
    // We only want to trigger once.
    if (event.hand != org.bukkit.inventory.EquipmentSlot.HAND) return // Disable off-hand interactions
    query findWhere { entry ->
        // Check if the player clicked on the correct location
        if (!entry.location.map { it == event.clickedBlock!!.location }.orElse(true)) return@findWhere false

        // Check if the player is holding the correct item
        if (!hasItemInHand(event.player, entry.itemInHand)) return@findWhere false

        entry.block == event.clickedBlock!!.type
    } startDialogueWithOrNextDialogue event.player
}

@EntryMigration(InteractBlockEventEntry::class, "0.4.0")
@NeedsMigrationIfNotParsable
fun migrateInteractBlockEventEntry(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "itemInHand")

    val itemInHand = json.getAndParse<Optional<Material>>("itemInHand", context.gson).optional

    val item = Item(material = itemInHand)
    data["itemInHand"] = context.gson.toJsonTree(item)

    return data
}