package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta

@Entry("on_potion_drank", "triggers when a player drinks a potion", Colors.YELLOW, "game-icons:drinking")
/**
 * The `On Drink Event` is triggered when a player drinks a potion.
 *
 * ## How could this be used?
 * You can create sequences linked together only by drinking a potion.
 */
class DrinkPotionEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    ) : EventEntry

    @EntryListener(DrinkPotionEventEntry::class)
    fun onpotiondrank(event: PlayerItemConsumeEvent, query: Query<DrinkPotionEventEntry>) {
        if (event.item.itemMeta != null) {
            if (event.item.itemMeta is PotionMeta) {
                query.find().triggerAllFor(event.player)
            }
        }
    }