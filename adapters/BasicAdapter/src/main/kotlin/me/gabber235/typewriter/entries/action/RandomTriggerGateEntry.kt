package me.gabber235.typewriter.entries.action

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
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
    override val id: String,
    override val name: String,
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    @Help("The amount of triggers to fire.")
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