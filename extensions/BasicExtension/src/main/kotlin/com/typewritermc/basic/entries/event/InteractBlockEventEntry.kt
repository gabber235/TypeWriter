package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.*
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.MaterialProperty.BLOCK
import com.typewritermc.core.interaction.EntryContextKey
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.utils.item.Item
import com.typewritermc.engine.paper.utils.toPosition
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import kotlin.reflect.KClass

@Entry("on_interact_with_block", "When the player interacts with a block", Colors.YELLOW, "mingcute:finger-tap-fill")
@ContextKeys(InteractBlockContextKeys::class)
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
    val block: Material = Material.AIR,
    val location: Optional<Var<Position>> = Optional.empty(),
    @Help("The item the player must be holding when the block is interacted with.")
    val itemInHand: Var<Item> = ConstVar(Item.Empty),
    @Help("""
        Cancel the event when triggered.
        It will only cancel the event if all the criteria are met.
        If set to false, it will not modify the event.
    """)
    val cancel: Boolean = false,
    val interactionType: InteractionType = InteractionType.ALL,
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
    ALL(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.PHYSICAL),
    CLICK(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR),
    RIGHT_CLICK(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
    LEFT_CLICK(Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR),
    PHYSICAL(Action.PHYSICAL),
}

enum class InteractBlockContextKeys(override val klass: KClass<*>) : EntryContextKey {
    @KeyType(Position::class)
    POSITION(Position::class),
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
    val player = event.player
    val block = event.clickedBlock
    val location = block?.location ?: player.location
    // The even triggers twice. Both for the main hand and offhand.
    // We only want to trigger once.
    if (event.hand == org.bukkit.inventory.EquipmentSlot.OFF_HAND) return // Disable off-hand interactions
    val entries = query.findWhere { entry ->
        // Check if the player is sneaking
        if (!entry.shiftType.isApplicable(player)) return@findWhere false

        // Check if the player is interacting with the block in the correct way
        if (!entry.interactionType.actions.contains(event.action)) return@findWhere false

        // Check if the player clicked on the correct location
        if (!entry.location.map { it.get(player).sameBlock(location.toPosition()) }
                .orElse(true)) return@findWhere false

        // Check if the player is holding the correct item
        if (!hasItemInHand(player, entry.itemInHand.get(player))) return@findWhere false

        entry.block == (block?.type ?: Material.AIR)
    }.toList()
    if (entries.isEmpty()) return

    entries.startDialogueWithOrNextDialogue(player) {
        InteractBlockContextKeys.POSITION to location.toPosition()
    }
    if (entries.any { it.cancel }) event.isCancelled = true
}