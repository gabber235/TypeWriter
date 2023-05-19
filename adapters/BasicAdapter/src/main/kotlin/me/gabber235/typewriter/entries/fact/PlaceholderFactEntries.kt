package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.extensions.placeholderapi.isPlaceholder
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("number_placeholder", "Computed Fact for a placeholder number", Colors.PURPLE, Icons.HASHTAG)
data class NumberPlaceholderFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    @Help("Placeholder to parse (e.g. %player_level%) - Only placeholders that return a number or boolean are supported!")
    private val placeholder: String = "",
) : ReadableFactEntry {
    override fun read(playerId: UUID): Fact {
        if (!placeholder.isPlaceholder) {
            logger.warning("Placeholder '$placeholder' is not a valid placeholder! Make sure it is only a placeholder starting & ending with %")
            return Fact(id, 0)
        }
        val value = placeholder.parsePlaceholders(playerId)
        return Fact(id, value.toIntOrNull() ?: value.toBooleanStrictOrNull()?.toInt() ?: 0)
    }
}

fun Boolean.toInt() = if (this) 1 else 0

@Entry("value_placeholder", "Fact for a placeholder value", Colors.PURPLE, Icons.USER_TAG)
data class ValuePlaceholderFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    @Help("Placeholder to parse (e.g. %%player_gamemode%%)")
    private val placeholder: String = "",
    @Help("Values to match the placeholder with and their corresponding fact value. Regex is supported.")
    private val values: Map<String, Int> = mapOf()
) : ReadableFactEntry {
    override fun read(playerId: UUID): Fact {
        if (!placeholder.isPlaceholder) {
            logger.warning("Placeholder '$placeholder' is not a valid placeholder! Make sure it is only a placeholder starting & ending with %")
            return Fact(id, 0)
        }
        val parsed = placeholder.parsePlaceholders(playerId)
        val value = values[parsed] ?: values.entries.firstOrNull { it.key.toRegex().matches(parsed) }?.value ?: 0
        return Fact(id, value)
    }
}