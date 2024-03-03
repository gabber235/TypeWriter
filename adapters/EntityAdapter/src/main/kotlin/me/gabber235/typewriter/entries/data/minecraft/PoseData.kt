package me.gabber235.typewriter.entries.data.minecraft

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("pose_data", "The pose of the entity", Colors.RED, "bi:person-arms-up")
class PoseData(
    override val id: String = "",
    override val name: String = "",
    @Help("The pose of the entity.")
    val pose: EntityPose = EntityPose.STANDING,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<PoseProperty> {
    override val type: KClass<PoseProperty> = PoseProperty::class

    override fun build(player: Player): PoseProperty = PoseProperty(pose)
}

data class PoseProperty(val pose: EntityPose) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<PoseProperty>(PoseProperty::class)
}

fun applyPoseData(entity: WrapperEntity, property: PoseProperty) {
    entity.metas {
        meta<EntityMeta> { pose = property.pose }
        error("Could not apply PoseData to ${entity.entityType} entity.")
    }
}