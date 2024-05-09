package lirand.api.dsl.command.types

import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import lirand.api.extensions.server.server
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * A [World] type.
 */
open class WorldType(
	open val allowedWorlds: (sender: Player?) -> Map<out World, Message?> = Instance.allowedWorlds,
	open val notFoundExceptionType: ChatCommandExceptionType = Instance.notFoundExceptionType
) : QuotableStringType<World> {

	/**
	 * Returns a [World] from the result of the [allowedWorlds]
	 * which name matches the string returned by the given [reader].
	 * A name that contains whitespaces must be enclosed in double quotation marks.
	 *
	 * @param reader the reader
	 * @return a [World] with the given name
	 * @throws ChatCommandSyntaxException if a world with the given name does not exist
	 */
	override fun parse(reader: StringReader): World {
		val name = reader.readString()

		return server.getWorld(name)?.takeIf { it in allowedWorlds(null) }
			?: throw notFoundExceptionType.createWithContext(reader, name)
	}

	/**
	 * Returns the [World] names from the result of the [allowedWorlds]
	 * that start with the remaining input of the given [builder].
	 *
	 * @param S the type of the source
	 * @param context the context
	 * @param builder the builder
	 * @return the [World] names that start with the remaining input
	 */
	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val sender = context.source as? Player? ?: return builder.buildFuture()

		allowedWorlds(sender).mapKeys { (world, _) -> world.name }
			.filterKeys { it.startsWith(builder.remaining, true) }
			.forEach { (worldName, tooltip) ->
				if (tooltip != null)
					builder.suggest(worldName, tooltip)
				else
					builder.suggest(worldName)
			}

		return builder.buildFuture()
	}

	override fun getExamples(): List<String> = listOf("my_fancy_world", "\"Yet another world\"")

	companion object Instance : WorldType(
		allowedWorlds = { server.worlds.associateWith { null } },
		notFoundExceptionType = ChatCommandExceptionType {
			Component.translatable("argument.dimension.invalid", Component.text(it[0].toString()))
		}
	)
}