package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.GenericEntityActivityEntry
import me.gabber235.typewriter.snippets.snippet
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max

private val playerCloseLookRange by snippet("entity.activity.look_close.range", 10.0)

@Entry("look_close_activity", "A look close activity", Colors.BLUE, "fa6-solid:eye")
/**
 * The `LookCloseActivityEntry` is an activity that makes the entity look to the player closest to it.
 *
 * The activity is specific to the player, then the entity will look at that player.
 *
 * ## How could this be used?
 * This could be used to make an entity look at the player closest to it.
 */
class LookCloseActivityEntry(
    override val id: String = "",
    override val name: String = "",
) : GenericEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<in ActivityContext> {
        return LookCloseActivity(currentLocation)
    }
}

class LookCloseActivity(
    override var currentLocation: LocationProperty,
) : EntityActivity<ActivityContext> {
    private var target: Target? = null
    private val yawVelocity = Velocity(0f)
    private val pitchVelocity = Velocity(0f)


    override fun initialize(context: ActivityContext) {}

    private fun findNewTarget(context: ActivityContext): Target? {
        val closestTarget = context.viewers
            .filter { it.isValid && it.gameMode != GameMode.SPECTATOR && !it.isInvisible }
            .minByOrNull { currentLocation.distanceSqrt(it.location) ?: Double.POSITIVE_INFINITY }

        if (closestTarget == null) {
            return null
        }
        val distance = currentLocation.distanceSqrt(closestTarget.location)

        if (distance == null || distance > playerCloseLookRange * playerCloseLookRange) {
            return null
        }

        return Target(closestTarget)
    }

    override fun tick(context: ActivityContext): TickResult {
        if (!context.isViewed) {
            this.target = null
            return TickResult.CONSUMED
        }
        if (target?.shouldRefresh == true) this.target = null

        var target = target
        if (target == null) target = findNewTarget(context)
        if (target == null) return TickResult.IGNORED

        this.target = target

        val npcEyeLocation = currentLocation.toVector().add(Vector(0.0, context.entityState.eyeHeight, 0.0))
        val direction = target.eyeLocation.toVector().subtract(npcEyeLocation).normalize()


        val targetYaw = getLookYaw(direction.x, direction.z)
        val targetPitch = getLookPitch(direction.x, direction.y, direction.z)

        val (yaw, pitch) = updateLookDirection(
            LookDirection(currentLocation.yaw, currentLocation.pitch),
            LookDirection(targetYaw, targetPitch),
            yawVelocity,
            pitchVelocity
        )

        currentLocation =
            LocationProperty(currentLocation.world, currentLocation.x, currentLocation.y, currentLocation.z, yaw, pitch)
        return TickResult.CONSUMED
    }

    override fun dispose(context: ActivityContext) {
        target = null
        yawVelocity.value = 0f
        pitchVelocity.value = 0f
    }

    inner class Target(val player: Player, private val lookupTime: Long = System.currentTimeMillis()) {
        val shouldRefresh: Boolean
            get() {
                if (!player.isValid) return true
                if (player.location.world.uid != this@Target.location.world) return true
                if (this@Target.location.distanceSquared(player.location.toProperty()) > playerCloseLookRange * playerCloseLookRange) return true
                return System.currentTimeMillis() - lookupTime > 1000
            }

        val location: LocationProperty
            get() = player.location.toProperty()

        val eyeLocation: LocationProperty
            get() = player.eyeLocation.toProperty()
    }

}

data class LookDirection(
    val yaw: Float,
    val pitch: Float,
)

fun updateLookDirection(
    current: LookDirection,
    target: LookDirection,
    yawVelocity: Velocity,
    pitchVelocity: Velocity,
    smoothTime: Float = 0.2f
): Pair<Float, Float> {
    val correctedYaw = if (current.yaw - target.yaw > 180) {
        current.yaw - 360
    } else if (current.yaw - target.yaw < -180) {
        current.yaw + 360
    } else {
        current.yaw
    }

    val yaw = smoothDamp(correctedYaw, target.yaw, yawVelocity, smoothTime)
    val pitch = smoothDamp(current.pitch, target.pitch, pitchVelocity, smoothTime)

    return yaw to pitch
}

class Velocity(var value: Float)

fun smoothDamp(
    current: Float,
    target: Float,
    currentVelocity: Velocity,
    smoothTime: Float,
    maxSpeed: Float = Float.POSITIVE_INFINITY,
    deltaTime: Float = 1f / 20f // For 20 ticks per second
): Float {
    val smoothTime = smoothTime.coerceAtLeast(0.0001f)
    val omega = 2f / smoothTime

    val x = omega * deltaTime
    val exp = 1f / (1f + x + 0.48f * x * x + 0.235f * x * x * x)
    var change = current - target

    // Clamp maximum speed
    val maxChange = maxSpeed * smoothTime
    change = change.coerceIn(-maxChange, maxChange)
    val targetTemp = current - change

    val temp = (currentVelocity.value + omega * change) * deltaTime
    currentVelocity.value = (currentVelocity.value - omega * temp) * exp
    var output = targetTemp + (change + temp) * exp

    // Prevent overshooting
    if (target - current > 0f == output > target) {
        output = target
        currentVelocity.value = (output - target) / deltaTime
    }

    return output
}

fun getLookYaw(dx: Double, dz: Double): Float {
    val radians = atan2(dz, dx)
    val degrees = Math.toDegrees(radians).toFloat() - 90
    if (degrees < -180) return degrees + 360
    if (degrees > 180) return degrees - 360
    return degrees
}

fun getLookPitch(dx: Double, dy: Double, dz: Double): Float {
    val radians = -atan2(dy, max(abs(dx), abs(dz)))
    return Math.toDegrees(radians).toFloat()
}