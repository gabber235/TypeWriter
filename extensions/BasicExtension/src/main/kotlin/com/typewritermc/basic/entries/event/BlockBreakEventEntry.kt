@file:JvmName("Some")

package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.*
import com.typewritermc.core.interaction.EntryContextKey
import com.typewritermc.core.utils.point.Position
import com.typewritermc.core.utils.point.toBlockPosition
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.entry.startDialogueWithOrNextDialogue
import com.typewritermc.engine.paper.utils.item.Item
import com.typewritermc.engine.paper.utils.toPosition
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import java.util.*
import kotlin.reflect.KClass

@Entry("on_block_break", "When the player breaks a block", Colors.YELLOW, "mingcute:pickax-fill")
@ContextKeys(BlockBreakContextKeys::class)
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
    val block: Optional<Material> = Optional.empty(),
    val location: Optional<Var<Position>> = Optional.empty(),
    @Help("The item the player must be holding when the block is broken.")
    val itemInHand: Var<Item> = ConstVar(Item.Empty),
) : EventEntry

enum class BlockBreakContextKeys(override val klass: KClass<*>) : EntryContextKey {
    @KeyType(Material::class)
    TYPE(Material::class),

    @KeyType(Position::class)
    BLOCK_POSITION(Position::class),

    @KeyType(Position::class)
    CENTER_POSITION(Position::class)
}

private fun hasItemInHand(player: Player, item: Item): Boolean {
    return item.isSameAs(player, player.inventory.itemInMainHand) || item.isSameAs(
        player,
        player.inventory.itemInOffHand
    )
}

@EntryListener(BlockBreakEventEntry::class)
fun onBlockBreak(event: BlockBreakEvent, query: Query<BlockBreakEventEntry>) {
    val player = event.player
    val position = event.block.location.toPosition()
    query.findWhere { entry ->
        // Check if the player clicked on the correct location
        if (!entry.location.map { it.get(player) == position }.orElse(true)) return@findWhere false

        // Check if the player is holding the correct item
        if (!hasItemInHand(player, entry.itemInHand.get(player))) return@findWhere false

        // Check if block type is correct
        entry.block.map { it == event.block.type }.orElse(true)
    }.startDialogueWithOrNextDialogue(player) {
        BlockBreakContextKeys.TYPE to event.block.type
        BlockBreakContextKeys.BLOCK_POSITION to position.toBlockPosition()
        BlockBreakContextKeys.CENTER_POSITION to position.mid()
    }
}