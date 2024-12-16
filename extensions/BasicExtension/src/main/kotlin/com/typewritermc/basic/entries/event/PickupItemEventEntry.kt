package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.utils.item.Item
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
    val item: Var<Item> = ConstVar(Item.Empty),
) : EventEntry

@EntryListener(PickupItemEventEntry::class)
fun onPickupItem(event: EntityPickupItemEvent, query: Query<PickupItemEventEntry>) {
    if (event.entity !is Player) return

    val player = event.entity as Player

    query.findWhere { entry ->
        entry.item.get(player).isSameAs(player, event.item.itemStack, context())
    }.startDialogueWithOrNextDialogue(player, context())
}