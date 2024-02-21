package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.PlaceholderEntry
import me.gabber235.typewriter.entry.PriorityEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import org.bukkit.entity.Player
import kotlin.reflect.KClass

@Tags("lines")
interface LinesEntry : EntityData<LinesProperty>, AudienceEntry, PlaceholderEntry, PriorityEntry {
    /**
     * The lines of the sidebar.
     * Multiple lines are separated by a newline character.
     */
    fun lines(player: Player): String

    override fun display(player: Player?): String? = player?.let { lines(it) }

    override val type: KClass<LinesProperty>
        get() = LinesProperty::class

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