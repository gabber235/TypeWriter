package me.gabber235.typewriter.entry

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.facts.Fact

interface Entry {
	val id: String
	val name: String
	val triggers: List<String>
}

interface RuleEntry : Entry {
	val criteria: List<Criteria>
	val modifiers: List<Modifier>
}

enum class CriteriaOperator {
	@SerializedName("==")
	EQUALS,

	@SerializedName("<")
	LESS_THAN,

	@SerializedName(">")
	GREATER_THAN,

	@SerializedName("<=")
	LER_THAN_OR_EQUAL,

	@SerializedName(">=")
	GREATER_THAN_OR_EQUAL,
}

data class Criteria(
	val fact: String,
	val operator: CriteriaOperator,
	val value: Int,
) {
	fun isValid(facts: Set<Fact>): Boolean {
		val value = facts.find { it.name == this.fact }?.value ?: 0
		return when (operator) {
			CriteriaOperator.EQUALS                -> value == this.value
			CriteriaOperator.LESS_THAN             -> value < this.value
			CriteriaOperator.GREATER_THAN          -> value > this.value
			CriteriaOperator.LER_THAN_OR_EQUAL     -> value <= this.value
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
