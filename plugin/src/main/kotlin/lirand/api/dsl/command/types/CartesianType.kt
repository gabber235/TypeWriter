package lirand.api.dsl.command.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.extensions.server.nmsNumberVersion
import lirand.api.extensions.server.nmsVersion
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * A Cartesian type.
 *
 * @param T the type of the argument
 */
interface CartesianType<T> : Type<T> {

	/**
	 * Splits the remaining string of the builder by a whitespace and forwards
	 * providing suggestions to [suggest]
	 * if the source is a player and the block the player is looking at is within
	 * a 5 block radius. After which, forwards to [suggest].
	 *
	 * @param S the type of the source
	 * @param context the context
	 * @param builder the builder
	 * @return the suggestions
	 */
	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val remaining = builder.remaining

		val parts = if (remaining.isNotBlank())
			remaining.split(' ').filter { it.isNotBlank() }
		else
			emptyList()

		val sender = context.source
		if (sender is Player) {
			val block = sender.getTargetBlockExact(5)
			if (block != null) {
				suggest(builder, parts, block.location)
			}
		}

		suggest(builder, parts)

		return builder.buildFuture()
	}

	/**
	 * Provides suggestions using the given parameters.
	 *
	 * **Default implementation:**
	 * Does nothing.
	 *
	 * @param builder the builder
	 * @param parts the parts of the argument split by a whitespace
	 * @param location the location of the block that the source is looking at if
	 * within a 5 block radius
	 */
	fun suggest(builder: SuggestionsBuilder, parts: List<String>, location: Location) {}

	/**
	 * Provides suggestions using the given parameters.
	 *
	 * **Default implementation:**
	 * Does nothing.
	 *
	 * @param builder the builder
	 * @param parts the parts of the argument split by a whitespace
	 */
	fun suggest(builder: SuggestionsBuilder, parts: List<String>) {}
}

/**
 * A 2D Cartesian type.
 *
 * @param T the type of the argument
 */
internal interface Cartesian2DType<T> : CartesianType<T> {
	override fun suggest(builder: SuggestionsBuilder, parts: List<String>, location: Location) {
		when (parts.size) {
			0 -> {
				builder.suggest("${location.x}")
					.suggest("${location.x} ${location.z}")
			}
			1 -> builder.suggest("${parts[0]} ${location.z}")
		}
	}

	override fun map(): ArgumentType<*> {
		return argumentVec2
	}

	companion object {
		val argumentVec2 = run {
			val nmsPackage = if (nmsNumberVersion < 17)
				"net.minecraft.server.v$nmsVersion"
			else
				"net.minecraft.commands.arguments.coordinates"

			val clazz = Class.forName("$nmsPackage.ArgumentVec2")

			clazz.getConstructor(Boolean::class.java)
				.newInstance(true) as ArgumentType<*>
		}
	}
}

/**
 * A 3D Cartesian type.
 *
 * @param T the type of the argument
 */
internal interface Cartesian3DType<T> : CartesianType<T> {

	override fun suggest(builder: SuggestionsBuilder, parts: List<String>, location: Location) {
		when (parts.size) {
			0 -> {
				builder.suggest("${location.x}")
					.suggest("${location.x} ${location.y}")
					.suggest("${location.x} ${location.y} ${location.z}")
			}
			1 -> {
				builder.suggest("${parts[0]} ${location.y}")
					.suggest("${parts[0]} ${location.y} ${location.z}")
			}
			2 -> builder.suggest("${parts[0]} ${parts[1]} ${location.z}")
		}
	}

	override fun map(): ArgumentType<*> {
		return argumentVec3
	}

	companion object {
		val argumentVec3 = run {
			val nmsPackage = if (nmsNumberVersion < 17)
				"net.minecraft.server.v$nmsVersion"
			else
				"net.minecraft.commands.arguments.coordinates"

			val clazz = Class.forName("$nmsPackage.ArgumentVec3")

			clazz.getConstructor(Boolean::class.java)
				.newInstance(false) as ArgumentType<*>
		}
	}
}