package me.gabber235.typewriter.entries.data.minecraft.display

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta.BillboardConstraints
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("billboard_constraint_data", "Constraints for a billboard", Colors.RED, "material-symbols:aspect_ratio")
@Tags("billboard_constraint_data")
class BillboardConstraintData(
    override val id: String = "",
    override val name: String = "",
    @Help("Billboard Constraint")
    val constraint: BillboardConstraints = BillboardConstraints.CENTER,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<BillboardConstraintProperty> {
    override val type: KClass<BillboardConstraintProperty> = BillboardConstraintProperty::class

    override fun build(player: Player): BillboardConstraintProperty =
        BillboardConstraintProperty(constraint)
}

data class BillboardConstraintProperty(val constraint: BillboardConstraints) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<BillboardConstraintProperty>(BillboardConstraintProperty::class)
}

fun applyBillboardConstraintData(entity: WrapperEntity, property: BillboardConstraintProperty) {
    entity.metas {
        meta<AbstractDisplayMeta> { billboardConstraints = property.constraint }
        error("Could not apply BillboardConstraintData to ${entity.entityType} entity.")
    }
}

