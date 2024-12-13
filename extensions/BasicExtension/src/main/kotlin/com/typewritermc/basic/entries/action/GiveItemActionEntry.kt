package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.snippets.snippet
import com.typewritermc.engine.paper.utils.item.Item
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.asMini
import org.bukkit.entity.Player

private val dropMessage by snippet("give_item.drop", "<gray>Some items have been dropped because your inventory is full")

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
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val item: Var<Item> = ConstVar(Item.Empty),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        SYNC.launch {
            val leftOver = player.inventory.addItem(item.get(player, context).build(player))
            leftOver.values.forEach {
                player.world.dropItemNaturally(player.location, it)
            }
            if (leftOver.isNotEmpty()) {
                player.sendMessage(dropMessage.parsePlaceholders(player).asMini())
            }
        }
    }
}