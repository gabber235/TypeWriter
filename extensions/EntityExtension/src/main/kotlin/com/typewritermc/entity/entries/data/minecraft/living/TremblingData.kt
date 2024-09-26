package com.typewritermc.entity.entries.data.minecraft.living

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.HoglinMeta
import me.tofaa.entitylib.meta.mobs.monster.piglin.BasePiglinMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("trembling_data", "Makes nether mobs tremble", Colors.RED, "solar:hand-shake-bold")
@Tags("trembling_data")
class TremblingData(
    override val id: String = "",
    override val name: String = "",
    val trembling: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<TremblingProperty> {
    override fun type(): KClass<TremblingProperty> = TremblingProperty::class

    override fun build(player: Player): TremblingProperty = TremblingProperty(trembling)
}

data class TremblingProperty(val trembling: Boolean) : EntityProperty {
    companion object :
        SinglePropertyCollectorSupplier<TremblingProperty>(TremblingProperty::class, TremblingProperty(false))
}

fun applyTremblingData(entity: WrapperEntity, property: TremblingProperty) {
    entity.metas {
        meta<BasePiglinMeta> { isImmuneToZombification = property.trembling.not() }
        meta<HoglinMeta> { isImmuneToZombification = property.trembling.not() }
        error("Could not apply TremblingData to ${entity.entityType} entity.")
    }
}