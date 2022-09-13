package me.gabber235.typewriter.entry.action

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.facts.FactDatabase
import org.bukkit.entity.Player

interface ActionEntry : RuleEntry {
	fun execute(player: Player) {
		FactDatabase.modify(player.uniqueId, modifiers)
	}
}

class SimpleActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	@SerializedName("triggered_by")
	override val triggerdBy: List<String> = emptyList(),
) : ActionEntry