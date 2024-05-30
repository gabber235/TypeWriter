package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.GenericEntityActivityEntry
import java.time.Duration
import java.util.*
import kotlin.random.Random

@Entry("random_look_activity", "A random look activity", Colors.BLUE, "fa6-solid:eye")
/**
 * The `Random Look Activity` is used to make the entity look in random directions.
 *
 * ## How could this be used?
 * This could be used to make the entity look distracted.
 */
class RandomLookActivityEntry(
    override val id: String = "",
    override val name: String = "",
    val pitchRange: ClosedFloatingPointRange<Float> = -90f..90f,
    val yawRange: ClosedFloatingPointRange<Float> = -180f..180f,
    @Help("The duration between each look")
    val duration: Duration = Duration.ofSeconds(2),
) : GenericEntityActivityEntry {
    override fun create(context: ActivityContext, currentLocation: LocationProperty): EntityActivity<ActivityContext> {
        return RandomLookActivity(pitchRange, yawRange, duration, currentLocation)
    }
}

class RandomLookActivity(
    private val pitchRange: ClosedFloatingPointRange<Float>,
    private val yawRange: ClosedFloatingPointRange<Float>,
    private val duration: Duration,
    override var currentLocation: LocationProperty,
) : GenericEntityActivity {
    private var targetPitch: Float = pitchRange.random()
    private var targetYaw: Float = yawRange.random()
    private val pitchVelocity = Velocity(0f)
    private val yawVelocity = Velocity(0f)
    private var lastChangeTime = System.currentTimeMillis()


    override fun initialize(context: ActivityContext) {}

    override fun tick(context: ActivityContext): TickResult {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastChangeTime > duration.toMillis()) {
            targetPitch = pitchRange.random()
            targetYaw = yawRange.random()
            lastChangeTime = currentTime
        }


        val (yaw, pitch) = updateLookDirection(
            LookDirection(currentLocation.yaw, currentLocation.pitch),
            LookDirection(targetYaw, targetPitch),
            yawVelocity,
            pitchVelocity,
            smoothTime = 0.5f,
        )

        currentLocation =
            LocationProperty(currentLocation.world, currentLocation.x, currentLocation.y, currentLocation.z, yaw, pitch)

        return TickResult.CONSUMED
    }

    override fun dispose(context: ActivityContext) {}
}

fun ClosedFloatingPointRange<Float>.random(): Float {
    return start + (endInclusive - start) * Random.nextFloat()
}