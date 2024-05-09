package lirand.api.dsl.command.types

import com.mojang.brigadier.arguments.StringArgumentType

/**
 * A string type is mapped to a quotable [StringArgumentType].
 *
 * @param T the type of the argument
 */
fun interface QuotableStringType<T> : Type<T> {

	/**
	 * @return a quotable [StringArgumentType]
	 */
	override fun map(): StringArgumentType {
		return StringArgumentType.string()
	}

}