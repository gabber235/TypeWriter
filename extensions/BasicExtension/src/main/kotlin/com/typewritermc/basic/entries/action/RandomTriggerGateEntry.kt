package com.typewritermc.basic.entries.action

import com.google.gson.annotations.SerializedName
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.CustomTriggeringActionEntry
import org.bukkit.entity.Player

@Entry("random_trigger", "Randomly selects its connected triggers", Colors.PINK, "mdi:clover")
/**
 * The `Random Trigger Gate` is a gate that triggers a specified number of entries randomly. This gate provides you with the ability to randomly select and trigger a set number of entries in response to a specific event.
 *
 * ## How could this be used?
 *
 * This gate can be useful in a variety of situations. You can use it to create a mini-game that randomly selects events to occur, or to trigger a set number of actions randomly in response to a specific event. The possibilities are endless!
 */
class RandomTriggerGateEntry(
    override val id: String = "",
    override val name: String = "",
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @Help("The number of triggers to fire next.")
    private val amount: Int = 1,
) : CustomTriggeringActionEntry {

    override fun execute(player: Player) {
        val selectedTriggers = mutableListOf<Ref<TriggerableEntry>>()

        if (customTriggers.isNotEmpty()) {
            val randomIndices = (customTriggers.indices).shuffled().take(amount)
            for (index in randomIndices) {
                selectedTriggers.add(customTriggers[index])
            }
        }

        super.execute(player)
        selectedTriggers triggerEntriesFor player
    }
}