package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.snippets.snippet
import org.bukkit.GameMode
import org.bukkit.entity.Player
import java.util.*
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
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity = LookCloseActivity()
}

class LookCloseActivity : EntityActivity {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        // Only if there is someone to look at
        return context.viewers.any { player ->
            val distance = currentLocation.distanceSqrt(player.location) ?: return@any false
            distance <= playerCloseLookRange * playerCloseLookRange
        }
    }

    override fun currentTask(context: TaskContext, currentLocation: LocationProperty): EntityTask {
        return LookCloseActivityTask(currentLocation)
    }
}

class LookCloseActivityTask(
    override var location: LocationProperty,
) : EntityTask {
    inner class Target(val player: Player, private val lookupTime: Long = System.currentTimeMillis()) {
        val shouldRefresh: Boolean
            get() {
                if (!player.isValid) return true
                if (player.location.world.uid != this@LookCloseActivityTask.location.world) return true
                if (this@LookCloseActivityTask.location.distanceSquared(player.location.toProperty()) > playerCloseLookRange * playerCloseLookRange) return true
                return System.currentTimeMillis() - lookupTime > 1000
            }

        val location: LocationProperty
            get() = player.location.toProperty()
    }

    private var target: Target? = null
    private val yawVelocity = Velocity(0f)
    private val pitchVelocity = Velocity(0f)

    private fun findNewTarget(context: TaskContext): Target? {
        val closestTarget = context.viewers
            .filter { it.isValid && it.gameMode != GameMode.SPECTATOR && !it.isInvisible }
            .minByOrNull { location.distanceSqrt(it.location) ?: Double.POSITIVE_INFINITY }

        if (closestTarget == null) {
            return null
        }
        val distance = location.distanceSqrt(closestTarget.location)

        if (distance == null || distance > playerCloseLookRange * playerCloseLookRange) {
            return null
        }

        return Target(closestTarget)
    }

    override fun tick(context: TaskContext) {
        if (!context.isViewed) {
            this.target = null
            return
        }
        var target = target
        if (target == null) target = findNewTarget(context)
        if (target == null) return

        if (target.shouldRefresh) {
            this.target = null
            return
        }

        this.target = target

        val direction = target.location.toVector().subtract(location.toVector()).normalize()


        val targetYaw = getLookYaw(direction.x, direction.z)
        val targetPitch = getLookPitch(direction.x, direction.y, direction.z)

        val currentYaw = if (location.yaw - targetYaw > 180) {
            location.yaw - 360
        } else if (location.yaw - targetYaw < -180) {
            location.yaw + 360
        } else {
            location.yaw
        }
        val currentPitch = location.pitch

        val yaw = smoothDamp(currentYaw, targetYaw, yawVelocity, 0.2f)
        val pitch = smoothDamp(currentPitch, targetPitch, pitchVelocity, 0.2f)

        location = LocationProperty(location.world, location.x, location.y, location.z, yaw, pitch)
    }

    override fun mayInterrupt(): Boolean = true

    override fun isComplete(): Boolean = target == null
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