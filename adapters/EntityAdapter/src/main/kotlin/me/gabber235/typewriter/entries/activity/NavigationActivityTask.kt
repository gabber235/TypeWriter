package me.gabber235.typewriter.entries.activity

import kotlinx.coroutines.Job
import kotlinx.coroutines.future.await
import me.gabber235.typewriter.entry.entity.EntityTask
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.TaskContext
import me.gabber235.typewriter.entry.entity.toProperty
import me.gabber235.typewriter.entry.entries.roadNetworkMaxDistance
import me.gabber235.typewriter.entry.roadnetwork.content.toLocation
import me.gabber235.typewriter.entry.roadnetwork.content.toPathPosition
import me.gabber235.typewriter.entry.roadnetwork.gps.GPS
import me.gabber235.typewriter.entry.roadnetwork.gps.GPSEdge
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.utils.*
import org.bukkit.util.BoundingBox
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.pathing.strategy.strategies.WalkablePathfinderStrategy
import org.patheloper.api.wrapper.PathPosition
import org.patheloper.mapping.PatheticMapper
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class NavigationActivityTask(
    gps: GPS,
    startLocation: LocationProperty,
) : EntityTask {
    private var path: List<GPSEdge>? = null
    private var state: NavigationActivityTaskState = NavigationActivityTaskState.Searching(gps, startLocation)

    override val location: LocationProperty
        get() = state.location()

    override fun tick(context: TaskContext) {
        state.tick(context)
        if (state.isComplete()) {
            path = path?.subList(1, path?.size ?: 0) ?: (state as NavigationActivityTaskState.Searching).let {
                it.path ?: return
            }

            val currentEdge = path?.firstOrNull() ?: return

            state = when {
                currentEdge.isFastTravel -> NavigationActivityTaskState.FastTravel(currentEdge)
                context.isViewed -> NavigationActivityTaskState.Walking(currentEdge, location)

                else -> NavigationActivityTaskState.FakeNavigation(currentEdge)
            }
        }

        val state = state
        // The fake navigation is used to improve the performance, it however, goes through buildings
        // So, we switch to walking when the entity is viewed
        if (state is NavigationActivityTaskState.FakeNavigation && context.isViewed) {
            this.state = NavigationActivityTaskState.Walking(state.edge, state.percentage)
        }

        // And we switch back to fake navigation when the entity is not viewed
        if (state is NavigationActivityTaskState.Walking && !context.isViewed) {
            this.state = NavigationActivityTaskState.FakeNavigation(state.edge, location)
        }
    }

    override fun mayInterrupt(): Boolean = true

    override fun isComplete(): Boolean = path?.isEmpty() == true && state.isComplete()

    override fun dispose() {
        state.dispose()
    }
}


private sealed interface NavigationActivityTaskState {
    fun location(): LocationProperty
    fun isComplete(): Boolean = false
    fun tick(context: TaskContext) {}
    fun dispose() {}

    class Searching(
        private val gps: GPS,
        private val location: LocationProperty,
    ) : NavigationActivityTaskState {
        internal var path: List<GPSEdge>? = null
        private val job: Job = ThreadType.DISPATCHERS_ASYNC.launch {
            val result = gps.findPath()
            path = if (result.isFailure) {
                logger.severe("Failed to find path: ${result.exceptionOrNull()}")
                emptyList()
            } else {
                result.getOrThrow()
            }
        }

        override fun location(): LocationProperty = location

        override fun isComplete(): Boolean = path != null

        override fun dispose() {
            job.cancel()
            super.dispose()
        }
    }

    class FakeNavigation(
        val edge: GPSEdge,
        val location: LocationProperty = edge.start.toProperty(),
    ) : NavigationActivityTaskState {
        private var ticks: Int = 0

        // Fixme: Magic number
        private val maxTicks = (location.distance(edge.end.toProperty()) * 20).toInt()

        val percentage: Double
            get() = ticks.toDouble() / maxTicks

        override fun location(): LocationProperty = location

        override fun tick(context: TaskContext) {
            super.tick(context)
            ticks++
        }

        override fun isComplete(): Boolean = ticks >= maxTicks
    }

    class FastTravel(
        val edge: GPSEdge,
    ) : NavigationActivityTaskState {
        override fun location(): LocationProperty = edge.end.toProperty()
        override fun isComplete(): Boolean = true
    }

    class Walking : NavigationActivityTaskState {
        val edge: GPSEdge

        // TODO: Have the walking speed specific to the entity. Or input it as a parameter
        private val walkingSpeed = 0.2085

        // TODO: Have bounding box specific to the entity, instead of just picking a player's bounding box
        private val boundingBox: BoundingBox = BoundingBox(-.3, .0, -.3, 0.3, 1.8, 0.3)

        private var location: LocationProperty
        private var pathIndex: Int = 0
        private var path: List<LocationProperty>? = null

        private var velocity = Vector.ZERO

        private var yawVelocity = Velocity(0f)
        private var pitchVelocity = Velocity(0f)

        private val job: Job

        constructor(edge: GPSEdge, startPercentage: Double) {
            this.edge = edge
            this.location = edge.start.lerp(edge.end, startPercentage).toProperty()

            job = ThreadType.DISPATCHERS_ASYNC.launch {
                calculatePath(edge.start.toPathPosition())
                if (path == null) return@launch
                val index = (path!!.size * startPercentage).toInt()
                location = path!![index]
                pathIndex = index
            }
        }

        constructor(edge: GPSEdge, startLocation: LocationProperty) {
            this.edge = edge
            this.location = startLocation

            job = ThreadType.DISPATCHERS_ASYNC.launch {
                calculatePath(startLocation.toLocation().toPathPosition())
            }
        }

        private suspend fun calculatePath(startLocation: PathPosition) {
            val pathFinder = PatheticMapper.newPathfinder(
                PathingRuleSet.createAsyncRuleSet()
                    .withMaxLength(roadNetworkMaxDistance.toInt())
                    .withLoadingChunks(true)
                    .withAllowingDiagonal(true)
                    .withAllowingFailFast(true)
            )

            val result = pathFinder.findPath(
                startLocation,
                edge.end.toPathPosition(),
                WalkablePathfinderStrategy(),
            ).await()
            if (result.hasFailed()) {
                logger.severe("Failed to find path: ${result.pathState}. You should recalculate the edges of the road network. Skipping to next node.")
                location = edge.end.toProperty()
            } else {
                path = result.path.map { it.toLocation().toProperty().mid() }
            }
        }

        override fun location(): LocationProperty = location

        override fun tick(context: TaskContext) {
            val path = path ?: return

            val targetPoint = path[pathIndex]


            val velocity = calculateVelocity(targetPoint, walkingSpeed)
            val result = calculateMovement(velocity)
            location = result.newPosition
            this.velocity = result.newVelocity

            val (yaw, pitch) = calculateRotation(path)
            location = location.copy(yaw = yaw, pitch = pitch)


            // FIXME: Change magic number to be dependent on the speed.
            if ((location.distanceSqrt(targetPoint) ?: Double.POSITIVE_INFINITY) < 0.10) {
                pathIndex = (pathIndex + 1).coerceAtMost(path.size - 1)
            }

            super.tick(context)
        }

        fun calculateVelocity(target: LocationProperty, speed: Double): Vector {
            var speed = speed
            val dx: Double = target.x - location.x
            val dy: Double = target.y - location.y
            val dz: Double = target.z - location.z
            val distSquared = dx * dx + dy * dy + dz * dz
            // Limit the speed to make sure not to overshoot the target
            if (speed > distSquared) {
                speed = distSquared
            }
            val radians = atan2(dz, dx)
            val speedX = cos(radians) * speed
            val speedY = dy * speed
            val speedZ = sin(radians) * speed

            val targetVelocity = Vector(speedX, speedY, speedZ)

            val gravityVelocity = if (dy < 0) {
                Vector(0.0, -0.1, 0.0)
            } else {
                Vector(0.0, 0.2, 0.0)
            }

            val newVelocity = (velocity + targetVelocity).normalize() * speed + gravityVelocity

            return newVelocity
        }

        fun calculateMovement(velocity: Vector): PhysicsResult {
            // Prevent ghosting
            return BlockCollision.handlePhysics(
                boundingBox,
                velocity,
                location,
                BukkitBlockGetter(
                    location.bukkitWorld
                        ?: throw IllegalStateException("Trying to navigate in ${location.world} which is not loaded, how did you manage to do that?")
                ),
                false,
            )
        }

        fun calculateRotation(path: List<LocationProperty>): Pair<Float, Float> {
            val targetLookPoint = path.getOrNull(pathIndex + 3) ?: path.last()
            val targetYaw = getLookYaw(targetLookPoint.x - location.x, targetLookPoint.z - location.z)
            val targetPitch = getLookPitch(
                targetLookPoint.x - location.x,
                targetLookPoint.y - location.y,
                targetLookPoint.z - location.z
            )
            val currentYaw = if (location.yaw - targetYaw > 180) {
                location.yaw - 360
            } else if (location.yaw - targetYaw < -180) {
                location.yaw + 360
            } else {
                location.yaw
            }

            val yaw = smoothDamp(currentYaw, targetYaw, yawVelocity, 0.2f)
            val pitch = smoothDamp(location.pitch, targetPitch, pitchVelocity, 0.2f)

            return yaw to pitch
        }

        override fun isComplete(): Boolean {
            return (location.distanceSqrt(edge.end) ?: Double.POSITIVE_INFINITY) < 1
        }

        override fun dispose() {
            job.cancel()
            super.dispose()
        }
    }
}