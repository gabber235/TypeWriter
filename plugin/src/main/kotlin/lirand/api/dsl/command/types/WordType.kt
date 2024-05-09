package lirand.api.dsl.command.types

import com.mojang.brigadier.arguments.StringArgumentType

/**
 * A word type that is mapped to a single word [StringArgumentType].
 *
 * @param T the type of the argument
 */
fun interface WordType<T> : Type<T> {

	/**
	 * @return a single word [StringArgumentType]
	 */
	override fun map(): StringArgumentType {
		return StringArgumentType.word()
	}
}