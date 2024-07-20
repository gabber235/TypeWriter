package lirand.api.extensions.math

import org.bukkit.Axis
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

/*
 * LOCATION
 */

operator fun Location.component1() = x
operator fun Location.component2() = y
operator fun Location.component3() = z
operator fun Location.component4() = yaw
operator fun Location.component5() = pitch

fun Location(world: World?, x: Number, y: Number, z: Number) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

// extensions
fun Location.add(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) = add(x.toDouble(), y.toDouble(), z.toDouble())
fun Location.subtract(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) =
	subtract(x.toDouble(), y.toDouble(), z.toDouble())


operator fun Location.get(axis: Axis): Double {
	return when (axis) {
		Axis.X -> x
		Axis.Y -> y
		Axis.Z -> z
	}
}

operator fun Location.set(axis: Axis, value: Number) {
	when (axis) {
		Axis.X -> x = value.toDouble()
		Axis.Y -> y = value.toDouble()
		Axis.Z -> z = value.toDouble()
	}
}


val Location.blockLocation: Location get() = Location(world, blockX, blockY, blockZ)

// operator functions
// immutable
operator fun Location.plus(vector: Vector) = clone().add(vector)
operator fun Location.minus(vector: Vector) = clone().subtract(vector)
operator fun Location.plus(location: Location) = clone().add(location)
operator fun Location.minus(location: Location) = clone().subtract(location)

// mutable
operator fun Location.plusAssign(vector: Vector) {
	add(vector)
}

operator fun Location.minusAssign(vector: Vector) {
	subtract(vector)
}

operator fun Location.plusAssign(location: Location) {
	add(location)
}

operator fun Location.minusAssign(location: Location) {
	subtract(location)
}

/*
 * VECTOR
 */

operator fun Vector.component1() = x
operator fun Vector.component2() = y
operator fun Vector.component3() = z

val Vector.isFinite: Boolean get() = x.isFinite() && y.isFinite() && z.isFinite()

// fast construct
fun Vector(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) = Vector(x.toDouble(), y.toDouble(), z.toDouble())

// extensions
operator fun Vector.get(axis: Axis): Double {
	return when (axis) {
		Axis.X -> x
		Axis.Y -> y
		Axis.Z -> z
	}
}

operator fun Vector.set(axis: Axis, value: Number) {
	when (axis) {
		Axis.X -> x = value.toDouble()
		Axis.Y -> y = value.toDouble()
		Axis.Z -> z = value.toDouble()
	}
}


// operator functions
// immutable
operator fun Vector.plus(vector: Vector) = clone().add(vector)
operator fun Vector.minus(vector: Vector) = clone().subtract(vector)
operator fun Vector.times(vector: Vector) = clone().multiply(vector)
operator fun Vector.times(number: Number) = clone().multiply(number.toDouble())

// mutable
operator fun Vector.plusAssign(vector: Vector) {
	add(vector)
}

operator fun Vector.minusAssign(vector: Vector) {
	subtract(vector)
}

operator fun Vector.timesAssign(vector: Vector) {
	multiply(vector)
}

operator fun Vector.timesAssign(number: Number) {
	multiply(number.toDouble())
}