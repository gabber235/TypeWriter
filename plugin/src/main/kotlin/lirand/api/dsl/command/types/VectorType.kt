package lirand.api.dsl.command.types

import com.mojang.brigadier.StringReader
import org.bukkit.util.Vector

/**
 * A [Vector] type.
 *
 * @see LocationType
 */
abstract class VectorType(private val cubic: Boolean) : Type<Vector> {

	override fun parse(reader: StringReader): Vector {
		val vector = Vector()

		reader.skipWhitespace()
		vector.x = reader.readDouble()

		if (cubic) {
			reader.skipWhitespace()
			vector.y = reader.readDouble()
		}

		reader.skipWhitespace()
		vector.z = reader.readDouble()

		return vector
	}
}

/**
 * A 2D [Vector] type.
 */
open class Vector2DType protected constructor() : VectorType(false), Cartesian2DType<Vector> {
	override fun getExamples(): Collection<String> = listOf("0 0", "0.0 0.0")

	companion object Instance : Vector2DType()
}

/**
 * A 3D [Vector] type.
 */
open class Vector3DType protected constructor() : VectorType(true), Cartesian3DType<Vector> {
	override fun getExamples(): Collection<String> = listOf("0 0 0", "0.0 0.0 0.0")

	companion object Instance : Vector3DType()
}