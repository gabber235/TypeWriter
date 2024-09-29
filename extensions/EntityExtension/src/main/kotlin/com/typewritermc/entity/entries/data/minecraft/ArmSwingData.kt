package com.typewritermc.entity.entries.data.minecraft

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.ArmSwing
import me.tofaa.entitylib.wrapper.WrapperEntity

data class ArmSwingProperty(val armSwing: ArmSwing) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ArmSwingProperty>(ArmSwingProperty::class)

    // Since this is a one time use property, we should always let it override the previous property.
    override fun equals(other: Any?): Boolean = false
    override fun hashCode(): Int = javaClass.hashCode()
}

fun ArmSwing.toProperty() = ArmSwingProperty(this)

fun applyArmSwingData(entity: WrapperEntity, property: ArmSwingProperty) {
    if (property.armSwing.swingLeft) {
        entity.sendPacketsToViewers(
            WrapperPlayServerEntityAnimation(
                entity.entityId,
                WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_OFF_HAND
            )
        )
    }
    if (property.armSwing.swingRight) {
        entity.sendPacketsToViewers(
            WrapperPlayServerEntityAnimation(
                entity.entityId,
                WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM
            )
        )
    }
}
