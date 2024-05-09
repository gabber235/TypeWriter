package lirand.api.dsl.command.types.extensions

import org.bukkit.Axis
import org.bukkit.Location
import org.bukkit.World

open class RelativeLocation(
	world: World? = null,
	x: Double = 0.0,
	y: Double = 0.0,
	z: Double = 0.0
) : Location(world, x, y, z) {

	constructor(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0)
			: this(null, x, y, z)


	/**
	 * Whether world is relative.
	 */
	open var isRelativeWorld: Boolean = false

	/**
	 * Whether x coordinate is relative.
	 */
	open var isRelativeX: Boolean = false

	/**
	 * Whether y coordinate is relative.
	 */
	open var isRelativeY: Boolean = false

	/**
	 * Whether z coordinate is relative.
	 */
	open var isRelativeZ: Boolean = false

	/**
	 * Sets the relativity of the coordinate for the given [axis].
	 *
	 * @param state `true` if the coordinate for the [axis] is relative.
	 */
	fun setRelative(axis: Axis, state: Boolean) {
		when (axis) {
			Axis.X -> isRelativeX = state
			Axis.Y -> isRelativeY = state
			Axis.Z -> isRelativeZ = state
		}
	}

	/**
	 * Returns whether the coordinate for the given [axis] is absolute or relative.
	 *
	 * @return `true` if the coordinate for the given [axis] is relative.
	 */
	fun isRelative(axis: Axis): Boolean {
		return when (axis) {
			Axis.X -> isRelativeX
			Axis.Y -> isRelativeY
			Axis.Z -> isRelativeZ
		}
	}

	/**
	 * Calculates an absolute location relative to given [location].
	 *
	 * @param location the absolute location
	 * relative to which the result location should be calculated
	 *
	 * @return the location relative to this [location]
	 */
	open fun getRelativeTo(location: Location): Location {
		val world = if (isRelativeWorld) location.world ?: world else world
		val x = if (isRelativeX) location.x + x else x
		val y = if (isRelativeY) location.y + y else y
		val z = if (isRelativeZ) location.z + z else z

		return Location(world, x, y, z)
	}
}