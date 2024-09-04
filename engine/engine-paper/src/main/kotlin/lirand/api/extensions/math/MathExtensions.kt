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


val Location.blockLocation: Location get() = Location(world, blockX, blockY, blockZ)