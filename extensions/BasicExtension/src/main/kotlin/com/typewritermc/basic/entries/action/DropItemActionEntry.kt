package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.utils.Item
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.toBukkitLocation
import org.bukkit.entity.Player
import java.util.*

@Entry("drop_item", "Drop an item at location, or on player", Colors.RED, "fa-brands:dropbox")
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
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val item: Item = Item.Empty,
    @Help("The location to drop the item. (Defaults to the player's location)")
    private val location: Optional<Position> = Optional.empty(),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        // Run on main thread
        SYNC.launch {
            if (location.isPresent) {
                val position = location.get()
                val bukkitLocation = position.toBukkitLocation()
                bukkitLocation.world.dropItem(bukkitLocation, item.build(player))
            } else {
                player.location.world.dropItem(player.location, item.build(player))
            }
        }
    }
}