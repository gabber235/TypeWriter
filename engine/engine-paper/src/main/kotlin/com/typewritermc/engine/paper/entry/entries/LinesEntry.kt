package com.typewritermc.engine.paper.entry.entries

import com.typewritermc.core.entries.PriorityEntry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import org.bukkit.entity.Player
import kotlin.reflect.KClass

@Tags("lines")
interface LinesEntry : EntityData<LinesProperty>, AudienceEntry, PlaceholderEntry, PriorityEntry {
    /**
     * The lines of the sidebar.
     * Multiple lines are separated by a newline character.
     */
    fun lines(player: Player): String

    override fun parser(): PlaceholderParser = placeholderParser {
        supplyPlayer { player -> lines(player) }
    }

    override fun type(): KClass<LinesProperty> = LinesProperty::class
    override fun build(player: Player): LinesProperty = LinesProperty(lines(player))
}

data class LinesProperty(
    val lines: String,
) : EntityProperty {
    companion object : PropertyCollectorSupplier<LinesProperty> {
        override val type: KClass<LinesProperty> = LinesProperty::class
        override fun collector(suppliers: List<PropertySupplier<out LinesProperty>>): PropertyCollector<LinesProperty> {
            return TextDisplayCollector(suppliers.filterIsInstance<PropertySupplier<LinesProperty>>())
        }
    }
}

class TextDisplayCollector(
    private val suppliers: List<PropertySupplier<LinesProperty>>,
) : PropertyCollector<LinesProperty> {
    override val type: KClass<LinesProperty> = LinesProperty::class

    override fun collect(player: Player): LinesProperty {
        return suppliers.filter {
            it.canApply(player)
        }.joinToString("\n") {
            it.build(player).lines
        }.parsePlaceholders(player).let { LinesProperty(it) }
    }
}