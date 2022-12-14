package me.gabber235.typewriter.entry

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.*
import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.facts.Fact

interface Entry {
	val id: String
	val name: String
}

@Tags("static")
interface StaticEntry : Entry

@Tags("trigger")
interface TriggerEntry : Entry {
	@Triggers
	@SnakeCase
	val triggers: List<String>
}

@Tags("rule")
interface RuleEntry : TriggerEntry {
	@Triggers(isReceiver = true)
	@SnakeCase
	val triggeredBy: List<String>

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
	LESS_THAN_OR_EQUALS,

	@SerializedName(">=")
	GREATER_THAN_OR_EQUAL,
}

data class Criteria(
	@StaticEntryIdentifier(FactEntry::class)
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
	@StaticEntryIdentifier(FactEntry::class)
	val fact: String,
	val operator: ModifierOperator,
	val value: Int,
)
