package lirand.api.dsl.command.types

import com.mojang.brigadier.arguments.ArgumentType

/**
 * An `ArgumentType` that has no supported client-side equivalent.
 *
 * @param T the type of the argument
 */
interface Type<T> : ArgumentType<T> {

	/**
	 * @return an [ArgumentType] that is supported by the client.
	 * Otherwise, the client will be disconnected from the server upon connection.
	 */
	fun map(): ArgumentType<*>

}