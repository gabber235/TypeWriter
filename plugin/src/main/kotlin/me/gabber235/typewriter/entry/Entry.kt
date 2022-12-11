package me.gabber235.typewriter.entry

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.facts.Fact

interface Entry {
	val id: String
	val name: String
}

interface TriggerEntry : Entry {
	@Triggers
	val triggers: List<String>
}

interface RuleEntry : TriggerEntry {
	val criteria: List<Criteria>
	val modifiers: List<Modifier>

	@Triggers(isReceiver = true)
	val triggeredBy: List<String>
}

enum class CriteriaOperator {
	@SerializedName("==")
	EQUALS,

	@SerializedName("<")
	LESS_THAN,

	@SerializedName(">")
	GREATER_THAN,

	@SerializedName("<=")
	LESS_THAN_OR_EQUALS,

	@SerializedName(">=")
	GREATER_THAN_OR_EQUAL,
}

data class Criteria(
	val fact: String,
	val operator: CriteriaOperator,
	val value: Int,
) {
	fun isValid(facts: Set<Fact>): Boolean {
		val value = facts.find { it.id == this.fact }?.value ?: 0
		return when (operator) {
			CriteriaOperator.EQUALS                -> value == this.value
			CriteriaOperator.LESS_THAN             -> value < this.value
			CriteriaOperator.GREATER_THAN          -> value > this.value
			CriteriaOperator.LESS_THAN_OR_EQUALS   -> value <= this.value
			CriteriaOperator.GREATER_THAN_OR_EQUAL -> value >= this.value
		}
	}
}

enum class ModifierOperator {
	@SerializedName("=")
	SET,

	@SerializedName("+")
	ADD;
}

data class Modifier(
	val fact: String,
	val operator: ModifierOperator,
	val value: Int,
)
