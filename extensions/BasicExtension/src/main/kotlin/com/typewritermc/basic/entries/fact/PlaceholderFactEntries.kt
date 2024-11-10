package com.typewritermc.basic.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.core.extension.annotations.Regex
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.extensions.placeholderapi.isPlaceholder
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.facts.FactData
import com.typewritermc.engine.paper.logger
import org.bukkit.entity.Player

@Entry("number_placeholder", "Computed Fact for a placeholder number", Colors.PURPLE, "ph:placeholder-fill")
/**
 * A [fact](/docs/creating-stories/facts) that is computed from a placeholder.
 * This placeholder is evaluated when the fact is read and must return a number or boolean.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 *
 * Make sure the player has a high enough level.
 * Then allow them to start a quest or enter a dungeon.
 */
class NumberPlaceholderFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Placeholder
    @Help("Placeholder to parse (e.g. %player_level%) - Only placeholders that return a number or boolean are supported!")
    /**
     * The placeholder to parse.
     * For example %player_level%.
     *
     * <Admonition type="caution">
     *      Only placeholders that return a number or boolean are supported!
     *      If you want to use a placeholder that returns a string,
     *      use the <Link to='value_placeholder'>ValuePlaceholderFactEntry</Link> instead.
     * </Admonition>
     */
    private val placeholder: Var<String> = ConstVar(""),
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val placeholder = placeholder.get(player)
        if (!placeholder.isPlaceholder) {
            logger.warning("Placeholder '$placeholder' is not a valid placeholder! Make sure it is only a placeholder starting & ending with %")
            return FactData(0)
        }
        val value = placeholder.parsePlaceholders(player)
        return FactData(value.toIntOrNull() ?: value.toBooleanStrictOrNull()?.toInt() ?: 0)
    }
}

fun Boolean.toInt() = if (this) 1 else 0

@Entry("value_placeholder", "Fact for a placeholder value", Colors.PURPLE, "fa6-solid:user-tag")
/**
 * A [fact](/docs/creating-stories/facts) that is computed from a placeholder.
 * This placeholder is evaluated when the fact is read and can return anything.
 * The value will be computed based on the `values` specified.
 * <fields.ReadonlyFactInfo/>
 *
 * ## How could this be used?
 *
 * If you only want to run certain actions if the player is in creative mode.
 * Or depending on the weather, change the dialogue of the NPC.
 */
class ValuePlaceholderFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Placeholder
    @Help("Placeholder to parse (e.g. %player_gamemode%)")
    private val placeholder: Var<String> = ConstVar(""),
    @Regex
    @Help("Values to match the placeholder with and their corresponding fact value. Regex is supported.")
    /**
     * The values to match the placeholder with and their corresponding fact value.
     *
     * An example would be:
     * ```yaml
     * values:
     *  SURVIVAL: 0
     *  CREATIVE: 1
     *  ADVENTURE: 2
     *  SPECTATOR: 3
     * ```
     * If the placeholder returns `CREATIVE`, the fact value will be `1`.
     * If no value matches, the fact value will be `0`.
     *
     * Values can have placeholders inside them.
     */
    private val values: Map<String, Int> = mapOf()
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val placeholder = placeholder.get(player)
        if (!placeholder.isPlaceholder) {
            logger.warning("Placeholder '$placeholder' is not a valid placeholder! Make sure it is only a placeholder starting & ending with %")
            return FactData(0)
        }
        val parsed = placeholder.parsePlaceholders(player)
        val value = values[parsed] ?: values.entries.firstOrNull { it.key.parsePlaceholders(player).toRegex().matches(parsed) }?.value ?: 0
        return FactData(value)
    }
}