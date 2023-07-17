package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.BLOCK
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.ITEM
import me.gabber235.typewriter.entry.EntryListener
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.startDialogueWithOrNextDialogue
import me.gabber235.typewriter.utils.Icons
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
    @MaterialProperties(ITEM)
    @Help("The item the player must be holding when the block is interacted with.")
    val itemInHand: Optional<Material> = Optional.empty(),
) : EventEntry

private fun hasMaterialInHand(player: Player, material: Material): Boolean {
    return player.inventory.itemInMainHand.type == material || player.inventory.itemInOffHand.type == material
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
        if (!entry.itemInHand.map { hasMaterialInHand(event.player, it) }.orElse(true)) return@findWhere false

        entry.block == event.clickedBlock!!.type
    } startDialogueWithOrNextDialogue event.player
}