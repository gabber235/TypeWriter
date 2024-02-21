package me.gabber235.typewriter.entry

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.entry.entries.WritableFactEntry
import me.gabber235.typewriter.facts.FactData
import org.bukkit.entity.Player
import java.util.*

interface Entry {
    val id: String
    val name: String
}

interface PriorityEntry : Entry {
    /**
     * Normally, the priority of an entry is determined by the priority of the page it is on.
     * Subtypes may want to allow the user to override the priority for that specific entry.
     * This is useful when entries need to have fine-grained control over their priority.
     */
    @Help("The priority of the entry. If not set, the priority of the page will be used.")
    val priorityOverride: Optional<Int>
}

@Tags("static")
interface StaticEntry : Entry

@Tags("manifest")
interface ManifestEntry : Entry

@Tags("trigger")
interface TriggerEntry : Entry {
    @Help("The entries that will be fired after this entry.")
    val triggers: List<Ref<TriggerableEntry>>
}

@Tags("triggerable")
interface TriggerableEntry : TriggerEntry {
    @Help("The criteria that must be met before this entry is triggered")
    val criteria: List<Criteria>

    @Help("The modifiers that will be applied when this entry is triggered")
    val modifiers: List<Modifier>
}

@Tags("placeholder")
interface PlaceholderEntry : Entry {
    fun display(player: Player?): String?
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

    ;

    fun isValid(value: Double, criteria: Double): Boolean {
        return when (this) {
            EQUALS -> value == criteria
            LESS_THAN -> value < criteria
            GREATER_THAN -> value > criteria
            LESS_THAN_OR_EQUALS -> value <= criteria
            GREATER_THAN_OR_EQUAL -> value >= criteria
        }
    }
}

data class Criteria(
    @Help("The fact to check before triggering the entry")
    val fact: Ref<ReadableFactEntry> = emptyRef(),
    @Help("The operator to use when comparing the fact value to the criteria value")
    val operator: CriteriaOperator = CriteriaOperator.EQUALS,
    @Help("The value to compare the fact value to")
    val value: Int = 0,
) {
    fun isValid(fact: FactData?): Boolean {
        val value = fact?.value ?: 0
        return operator.isValid(value.toDouble(), this.value.toDouble())
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
    val fact: Ref<WritableFactEntry> = emptyRef(),
    @Help("The operator to use when modifying the fact value")
    val operator: ModifierOperator = ModifierOperator.ADD,
    @Help("The value to modify the fact value by")
    val value: Int = 0,
)
