package me.gabber235.typewriter.entries.gate

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("random_trigger", "Randomly selects its connected triggers", Colors.PINK, Icons.CLOVER)
class RandomTriggerGateEntry(
    override val id: String,
    override val name: String,
    @SerializedName("triggers")
    @Triggers
    @EntryIdentifier(TriggerableEntry::class)
    val nextTriggers: List<String> = emptyList(),
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    private val amount: Int = 1,

    ) : ActionEntry {

    override val triggers: List<String>
        get() = emptyList()

    override fun execute(player: Player) {

        val selectedTriggers = mutableListOf<String>()

        if (nextTriggers.isNotEmpty()) {
            val randomIndices = (nextTriggers.indices).shuffled().take(amount)
            for (index in randomIndices) {
                selectedTriggers.add(nextTriggers[index])
            }
        }

        super.execute(player)
        InteractionHandler.startInteractionAndTrigger(player, selectedTriggers.map { EntryTrigger(it) })

    }
}