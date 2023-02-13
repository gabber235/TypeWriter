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
	@EntryIdentifier(TriggerableEntry::class)
	val triggers: List<String>
}

@Tags("triggerable")
interface TriggerableEntry : TriggerEntry {
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
	@Help("The fact to check before triggering the entry")
	@EntryIdentifier(FactEntry::class)
	val fact: String,
	@Help("The operator to use when comparing the fact value to the criteria value")
	val operator: CriteriaOperator,
	@Help("The value to compare the fact value to")
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
	@Help("The fact to modify when the entry is triggered")
	@EntryIdentifier(FactEntry::class)
	val fact: String,
	@Help("The operator to use when modifying the fact value")
	val operator: ModifierOperator,
	@Help("The value to modify the fact value by")
	val value: Int,
)
