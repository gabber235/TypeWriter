package me.gabber235.typewriter.entries.event

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import java.util.*

@Entry("on_block_break", "When the player breaks a block", Colors.YELLOW, "mingcute:pickax-fill")
/**
 *The `Block Break Event` is triggered when a player breaks a block.
 *
 * ## How could this be used?
 *
 * This could allow you to make custom ores with custom drops, give the player a reward after breaking a certain amount of blocks.
 */
class BlockBreakEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @MaterialProperties(MaterialProperty.BLOCK)
    @Help("The block that was broken.")
    val block: Optional<Material> = Optional.empty(),
    @Help("The location of the block that was broken.")
    val location: Optional<Location> = Optional.empty(),
    @Help("The item the player must be holding when the block is broken.")
    val itemInHand: Item = Item.Empty,
) : EventEntry

private fun hasItemInHand(player: Player, item: Item): Boolean {
    return item.isSameAs(player, player.inventory.itemInMainHand) || item.isSameAs(
        player,
        player.inventory.itemInOffHand
    )
}

@EntryListener(BlockBreakEventEntry::class)
fun onBlockBreak(event: BlockBreakEvent, query: Query<BlockBreakEventEntry>) {
    query findWhere { entry ->
        // Check if the player clicked on the correct location
        if (!entry.location.map { it == event.block.location }.orElse(true)) return@findWhere false

        // Check if the player is holding the correct item
        if (!hasItemInHand(event.player, entry.itemInHand)) return@findWhere false

        // Check if block type is correct
        entry.block.map { it == event.block.type }.orElse(true)
    } startDialogueWithOrNextDialogue event.player
}

@EntryMigration(BlockBreakEventEntry::class, "0.4.0")
@NeedsMigrationIfNotParsable
fun migrate040BlockBreakEvent(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "itemInHand")

    val itemInHand = json.getAndParse<Optional<Material>>("itemInHand", context.gson).optional

    val item = Item(material = itemInHand)

    data["itemInHand"] = context.gson.toJsonTree(item)

    return data
}