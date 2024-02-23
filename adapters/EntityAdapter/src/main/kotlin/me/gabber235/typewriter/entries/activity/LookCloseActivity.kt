package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.EntityTask
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.snippets.snippet
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.atan2
import kotlin.math.sqrt

private val playerCloseLookRange by snippet("entity.look_close_activity.player_close_look_range", 10.0)

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
    override fun create(player: Player?): EntityActivity = LookCloseActivity(player)
}

class LookCloseActivity(
    private val player: Player?,
) : EntityActivity {
    override fun canActivate(currentLocation: LocationProperty): Boolean {
        // Can only activate if there is a player nearby
        if (player == null) {
            return currentLocation.bukkitWorld?.players?.any {
                val distance = currentLocation.distanceSquared(it.location) ?: return@any false
                distance <= playerCloseLookRange * playerCloseLookRange
            } ?: false
        }

        val distance = currentLocation.distanceSquared(player.location) ?: return false
        return distance <= playerCloseLookRange * playerCloseLookRange
    }

    override fun currentTask(currentLocation: LocationProperty): EntityTask {
        return LookCloseActivityTask(player, currentLocation)
    }
}

class LookCloseActivityTask(
    private val player: Player?,
    override var location: LocationProperty,
) : EntityTask {
    private var target: Player? = null
    private val yawVelocity = Velocity(0f)
    private val pitchVelocity = Velocity(0f)

    private fun findNewTarget(): Player? {
        if (player == null) {
            val closestTarget = location.bukkitWorld?.players
                ?.minByOrNull { location.distanceSquared(it.location) ?: Double.POSITIVE_INFINITY }
            if (closestTarget == null) {
                return null
            }
            val distance = location.distanceSquared(closestTarget.location)

            if (distance == null || distance > playerCloseLookRange * playerCloseLookRange) {
                return null
            }

            return closestTarget
        }


        val distance = location.distanceSquared(player.location)
        if (distance == null || distance > playerCloseLookRange * playerCloseLookRange) {
            return null
        }
        return player
    }

    override fun tick() {
        var target = target
        if (target == null) target = findNewTarget()
        if (target == null) return

        val distance = location.distanceSquared(target.location) ?: Double.POSITIVE_INFINITY
        if (distance > playerCloseLookRange * playerCloseLookRange) {
            this.target = null
            return
        }

        this.target = target

        val direction = target.location.toVector().subtract(location.toVector()).normalize()


        val targetYaw = Math.toDegrees(atan2(-direction.x, direction.z))
        val targetPitch =
            -Math.toDegrees(atan2(direction.y, sqrt(direction.x * direction.x + direction.z * direction.z)))


        val currentYaw = if (location.yaw - targetYaw > 180) {
            location.yaw - 360
        } else if (location.yaw - targetYaw < -180) {
            location.yaw + 360
        } else {
            location.yaw
        }
        val currentPitch = location.pitch

        val yaw = smoothDamp(currentYaw, targetYaw.toFloat(), yawVelocity, 0.2f)
        val pitch = smoothDamp(currentPitch, targetPitch.toFloat(), pitchVelocity, 0.2f)

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
