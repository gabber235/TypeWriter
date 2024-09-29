package com.typewritermc.entity.entries.activity

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.entity.*
import com.typewritermc.engine.paper.entry.entries.EntityActivityEntry
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
        return LookAtPitchYawActivity(currentLocation, yaw, pitch)
    }
}

class LookAtPitchYawActivity(
    private val startLocation: PositionProperty,
    private val targetYaw: Float,
    private val targetPitch: Float
) : EntityActivity<ActivityContext> {
    private val yawVelocity = Velocity(0f)
    private val pitchVelocity = Velocity(0f)
    private var currentLocation: PositionProperty = startLocation

    override fun initialize(context: ActivityContext) {}

    override fun tick(context: ActivityContext): TickResult {
        // Update entity's yaw and pitch smoothly
        val (yaw, pitch) = updateLookDirection(
            LookDirection(currentLocation.yaw, currentLocation.pitch),
            LookDirection(targetYaw, targetPitch),
            yawVelocity,
            pitchVelocity
        )

        // Update the current position with the new yaw and pitch
        currentLocation = PositionProperty(currentLocation.world, currentLocation.x, currentLocation.y, currentLocation.z, yaw, pitch)

        return TickResult.CONSUMED
    }

    override fun dispose(context: ActivityContext) {
        yawVelocity.value = 0f
        pitchVelocity.value = 0f
    }

    // Return the current position
    override val currentPosition: PositionProperty
        get() = currentLocation
}
