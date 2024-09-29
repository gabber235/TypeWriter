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
import com.typewritermc.engine.paper.utils.toBukkitVector

@Entry("look_at_block_activity", "A look at block activity", Colors.BLUE, "fa6-solid:cube")
/**
 * The `LookAtBlockActivityEntry` makes the entity look at a specific block.
 *
 * ## Usage
 * This can be used to make an entity appear to observe blocks around it, such as observing a specific structure.
 */
class LookAtBlockActivityEntry(
    override val id: String = "",
    override val name: String = "",
    val blockPosition: Position = Position.ORIGIN,
    @Help("The activity that supplies the x, y, z of the block to look at.")
    val childActivity: Ref<out EntityActivityEntry> = emptyRef()
) : GenericEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: PositionProperty
    ): EntityActivity<in ActivityContext> {
        return LookAtBlockActivity(currentLocation, blockPosition.add(0.0, -1.0, 0.0))
    }
}

class LookAtBlockActivity(
    private val startLocation: PositionProperty,
    private val blockPosition: Position
) : EntityActivity<ActivityContext> {
    private val yawVelocity = Velocity(0f)
    private val pitchVelocity = Velocity(0f)
    private var currentLocation: PositionProperty = startLocation

    override fun initialize(context: ActivityContext) {}

    override fun tick(context: ActivityContext): TickResult {
        // Convert block position to Bukkit vector
        val blockVector = blockPosition.toBukkitVector()
        // Convert NPC's eye position to Bukkit vector
        val npcEyeVector = currentLocation.toBukkitVector()

        // Calculate direction to the block
        val direction = blockVector.clone().subtract(npcEyeVector).normalize()

        // Calculate target yaw and pitch
        val targetYaw = getLookYaw(direction.x, direction.z)
        val targetPitch = getLookPitch(0.0, direction.y, direction.length()) // Use length for pitch

        // Update entity's yaw and pitch smoothly
        val (yaw, pitch) = updateLookDirection(
            LookDirection(currentLocation.yaw, currentLocation.pitch),
            LookDirection(targetYaw, targetPitch),
            yawVelocity,
            pitchVelocity
        )

        // Update the current position
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
