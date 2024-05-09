package lirand.api.dsl.command.types

import com.mojang.brigadier.arguments.StringArgumentType

/**
 * A string type is mapped to a greedy [StringArgumentType].
 *
 * @param T the type of the argument
 */
fun interface GreedyStringType<T> : Type<T> {

	/**
	 * @return a greedy [StringArgumentType]
	 */
	override fun map(): StringArgumentType {
		return StringArgumentType.greedyString()
	}
}