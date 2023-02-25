package me.gabber235.typewriter.entries.gate

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("random_trigger", "Randomly selects its connected triggers", Colors.PINK, Icons.CLOVER)
class RandomTriggerGateEntry(
	override val id: String,
	override val name: String,
	@SerializedName("triggers")
	override val customTriggers: List<String> = emptyList(),
	override val criteria: List<Criteria>,
	override val modifiers: List<Modifier>,
	@Help("The amount of triggers to fire.")
	private val amount: Int = 1,
) : CustomTriggeringActionEntry {

	override fun execute(player: Player) {
		val selectedTriggers = mutableListOf<String>()

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