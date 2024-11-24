package com.typewritermc.entity.entries.data.minecraft.living.armorstand

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.utils.point.Vector
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.extensions.packetevents.metas
import com.typewritermc.engine.paper.utils.toPacketVector3f
import me.tofaa.entitylib.meta.other.ArmorStandMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("rotation_data", "Rotation data", Colors.RED, "mdi:rotate-right")
@Tags("rotation_data", "armor_stand_data")
class RotationData(
    override val id: String = "",
    override val name: String = "",
    val rotation: Map<RotationSlot, Vector> = emptyMap(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<RotationProperty> {
    override fun type(): KClass<RotationProperty> = RotationProperty::class

    override fun build(player: Player): RotationProperty = RotationProperty(rotation)
}

data class RotationProperty(val data: Map<RotationSlot, Vector>) : EntityProperty {
    companion object : PropertyCollectorSupplier<RotationProperty> {
        override val type: KClass<RotationProperty> = RotationProperty::class

        override fun collector(suppliers: List<PropertySupplier<out RotationProperty>>): PropertyCollector<RotationProperty> {
            return RotationCollector(suppliers.filterIsInstance<PropertySupplier<RotationProperty>>())
        }
    }
}

enum class RotationSlot {
    HEAD,
    BODY,
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG
}

private class RotationCollector(
    private val suppliers: List<PropertySupplier<RotationProperty>>,
) : PropertyCollector<RotationProperty> {
    override val type: KClass<RotationProperty> = RotationProperty::class

    override fun collect(player: Player): RotationProperty {
        val properties = suppliers.filter { it.canApply(player) }
            .map { it.build(player) }
        val data = mutableMapOf<RotationSlot, Vector>()
        properties.asSequence()
            .flatMap { it.data.asSequence() }
            .filter { (slot, _) -> !data.containsKey(slot) }
            .forEach { (slot, rotation) -> data[slot] = rotation }

        return RotationProperty(data)
    }
}

fun applyRotationData(entity: WrapperEntity, property: RotationProperty) {
    entity.metas {
        meta<ArmorStandMeta> {
            property.data.forEach { (slot, rotation) ->
                when (slot) {
                    RotationSlot.HEAD -> headRotation = rotation.toPacketVector3f()
                    RotationSlot.BODY -> bodyRotation = rotation.toPacketVector3f()
                    RotationSlot.LEFT_ARM -> leftArmRotation = rotation.toPacketVector3f()
                    RotationSlot.RIGHT_ARM -> rightArmRotation = rotation.toPacketVector3f()
                    RotationSlot.LEFT_LEG -> leftLegRotation = rotation.toPacketVector3f()
                    RotationSlot.RIGHT_LEG -> rightLegRotation = rotation.toPacketVector3f()
                }
            }
        }
    }
}
