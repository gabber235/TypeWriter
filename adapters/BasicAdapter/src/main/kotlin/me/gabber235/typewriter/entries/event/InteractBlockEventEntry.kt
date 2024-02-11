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
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.optional
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

@Entry("on_interact_with_block", "When the player interacts with a block", Colors.YELLOW, "mingcute:finger-tap-fill")
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
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @MaterialProperties(BLOCK)
    @Help("The block that was interacted with.")
    val block: Material = Material.AIR,
    @Help("The location of the block that was interacted with.")
    val location: Optional<Location> = Optional.empty(),
    @Help("The item the player must be holding when the block is interacted with.")
    val itemInHand: Item = Item.Empty,
    @Help("Cancel the event when triggered")
    /**
     * Cancel the event when triggered.
     * It will only cancel the event if all the criteria are met.
     * If set to false, it will not modify the event.
     */
    val cancel: Boolean = false,
    @Help("The type of interaction that should trigger the event.")
    val interactionType: InteractionType = InteractionType.ALL,
    @Help("The type of shift that should trigger the event.")
    val shiftType: ShiftType = ShiftType.ANY,
) : EventEntry

enum class ShiftType {
    ANY,
    SHIFT,
    NO_SHIFT;

    fun isApplicable(player: Player): Boolean {
        return when (this) {
            ANY -> true
            SHIFT -> player.isSneaking
            NO_SHIFT -> !player.isSneaking
        }
    }
}

enum class InteractionType(vararg val actions: Action) {
    ALL(Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_BLOCK, Action.PHYSICAL),
    CLICK(Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_BLOCK),
    RIGHT_CLICK(Action.RIGHT_CLICK_BLOCK),
    LEFT_CLICK(Action.LEFT_CLICK_BLOCK),
    PHYSICAL(Action.PHYSICAL),
}

private fun hasItemInHand(player: Player, item: Item): Boolean {
    return item.isSameAs(player, player.inventory.itemInMainHand) || item.isSameAs(
        player,
        player.inventory.itemInOffHand
    )
}

fun Location.isSameBlock(location: Location): Boolean {
    return this.world == location.world && this.blockX == location.blockX && this.blockY == location.blockY && this.blockZ == location.blockZ
}

@EntryListener(InteractBlockEventEntry::class)
fun onInteractBlock(event: PlayerInteractEvent, query: Query<InteractBlockEventEntry>) {
    if (event.clickedBlock == null) return
    // The even triggers twice. Both for the main hand and offhand.
    // We only want to trigger once.
    if (event.hand != org.bukkit.inventory.EquipmentSlot.HAND) return // Disable off-hand interactions
    val entries = query findWhere { entry ->
        // Check if the player is sneaking
        if (!entry.shiftType.isApplicable(event.player)) return@findWhere false

        // Check if the player is interacting with the block in the correct way
        if (!entry.interactionType.actions.contains(event.action)) return@findWhere false

        // Check if the player clicked on the correct location
        if (!entry.location.map { it.isSameBlock(event.clickedBlock!!.location) }
                .orElse(true)) return@findWhere false

        // Check if the player is holding the correct item
        if (!hasItemInHand(event.player, entry.itemInHand)) return@findWhere false

        entry.block == event.clickedBlock!!.type
    }
    if (entries.isEmpty()) return

    entries startDialogueWithOrNextDialogue event.player
    if (entries.any { it.cancel }) event.isCancelled = true
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