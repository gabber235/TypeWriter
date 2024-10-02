package com.typewritermc.entity.entries.activity

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.entity.*
import com.typewritermc.engine.paper.entry.entries.EntityActivityEntry
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.entry.entries.GenericEntityActivityEntry

@Entry("look_at_pitch_yaw_activity", "A look at pitch and yaw activity", Colors.BLUE, "fa6-solid:compass")
/**
 * The `LookAtPitchYawActivityEntry` makes the entity look at a specific pitch and yaw.
 *
 * ## Usage
 * This can be used to make an entity face a specific direction without focusing on a block.
 */
class LookAtPitchYawActivityEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The pitch value to look at.")
    val pitch: Float = 0f,
    @Help("The yaw value to look at.")
    val yaw: Float = 0f,
    @Help("The activity that adjusts the pitch and yaw.")
    val childActivity: Ref<out EntityActivityEntry> = emptyRef()
) : GenericEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: PositionProperty
    ): EntityActivity<in ActivityContext> {
        val activity = childActivity.get() ?: IdleActivity
        return LookAtPitchYawActivity(currentLocation, yaw, pitch, activity.create(context, currentLocation))
    }
}

class LookAtPitchYawActivity(
    startLocation: PositionProperty,
    private val targetYaw: Float,
    private val targetPitch: Float,
    private val childActivity: EntityActivity<ActivityContext>,
) : EntityActivity<ActivityContext> {
    private val yawVelocity = Velocity(0f)
    private val pitchVelocity = Velocity(0f)
    private var currentDirection: LookDirection = LookDirection(startLocation.yaw, startLocation.pitch)

    override fun initialize(context: ActivityContext) {
        childActivity.initialize(context)
    }

    override fun tick(context: ActivityContext): TickResult {
        val (yaw, pitch) = updateLookDirection(
            currentDirection,
            LookDirection(targetYaw, targetPitch),
            yawVelocity,
            pitchVelocity
        )

        currentDirection = LookDirection(yaw, pitch)

        return childActivity.tick(context)
    }

    override fun dispose(context: ActivityContext) {
        yawVelocity.value = 0f
        pitchVelocity.value = 0f

        childActivity.dispose(context)
    }

    override val currentPosition: PositionProperty
        get() = childActivity.currentPosition.copy(yaw = currentDirection.yaw, pitch = currentDirection.pitch)

    override val currentProperties: List<EntityProperty>
        get() = childActivity.currentProperties.filter { it !is PositionProperty } + currentPosition
}
