package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import com.typewritermc.engine.paper.utils.item.Item
import org.bukkit.event.player.PlayerItemConsumeEvent

@Entry("consume_item_event", "triggers when the player consumes food", Colors.YELLOW, "game-icons:eating")
class ConsumeItemEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val item: Item = Item.Empty,
) : EventEntry

@EntryListener(ConsumeItemEventEntry::class)
fun onItemConsume(event: PlayerItemConsumeEvent, query: Query<ConsumeItemEventEntry>) {
    val player = event.player
    query.findWhere { entry ->
        entry.item.isSameAs(player, event.item)
    }.triggerAllFor(player, context())
}