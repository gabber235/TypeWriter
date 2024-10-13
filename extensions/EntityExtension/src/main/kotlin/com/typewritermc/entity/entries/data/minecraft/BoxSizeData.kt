package com.typewritermc.entity.entries.data.minecraft

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("box_size_data", "This specifies the width and height of an entity", Colors.RED, "mdi:cube-outline")
@Tags("box_size_data")
class BoxSizeData(
    override val id: String = "",
    override val name: String = "",
    @Default("1.0")
    val width: Double = 1.0,
    @Default("1.0")
    val height: Double = 1.0,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<BoxSizeProperty> {
    override fun type(): KClass<BoxSizeProperty> = BoxSizeProperty::class
    override fun build(player: Player): BoxSizeProperty = BoxSizeProperty(width, height)
}

data class BoxSizeProperty(val width: Double, val height: Double) : EntityProperty {
    companion object :
        SinglePropertyCollectorSupplier<BoxSizeProperty>(BoxSizeProperty::class, BoxSizeProperty(1.0, 1.0))
}