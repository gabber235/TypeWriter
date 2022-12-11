package me.gabber235.typewriter.entries.action

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry

@Entry("simple_action", "Simple action to modify facts")
class SimpleActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	@SerializedName("triggered_by")
	override val triggeredBy: List<String> = emptyList(),
) : ActionEntry