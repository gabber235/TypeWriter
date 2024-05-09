package lirand.api.dsl.command.types

import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import lirand.api.dsl.command.types.extensions.readUnquoted
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * A [Enum] type.
 */
open class EnumType<T : Enum<T>>(
	val clazz: Class<T>,
	open val allowedConstants: (sender: Player?) -> Map<T, Message?> = { clazz.enumConstants.associateWith { null } },
	open val notFoundExceptionType: ChatCommandExceptionType = ChatCommandExceptionType {
		Component.translatable("argument.id.unknown", Component.text(it[0].toString()))
	}
) : WordType<T> {

	private val enumConstants = clazz.enumConstants.associateBy { it.name.lowercase() }


	/**
	 * Returns a [T] from the result of the [allowedConstants]
	 * which key matches the string returned by the given [reader].
	 *
	 * @param reader the reader
	 * @return a [T] with the given key
	 * @throws ChatCommandSyntaxException if a [T] with the given key does not exist
	 */
	override fun parse(reader: StringReader): T {
		val name = reader.readUnquoted().lowercase()

		return enumConstants[name]?.takeIf { it in allowedConstants(null) }
			?: throw notFoundExceptionType.createWithContext(reader, name)
	}

	/**
	 * Returns the [T] constants from the result of the [allowedConstants]
	 * that start with the remaining input of the given [builder].
	 *
	 * @param S the type of the source
	 * @param context the context
	 * @param builder the builder
	 * @return the [T] constants that start with the remaining input
	 */
	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val sender = context.source as? Player? ?: return builder.buildFuture()

		allowedConstants(sender).mapKeys { (constant, _) -> constant.name.lowercase() }
			.filterKeys { it.startsWith(builder.remaining, true) }
			.forEach { (constant, tooltip) ->
				if (tooltip != null)
					builder.suggest(constant, tooltip)
				else
					builder.suggest(constant)
			}

		return builder.buildFuture()
	}

	override fun getExamples(): List<String> = enumConstants.keys.toList()



	companion object {
		inline fun <reified T : Enum<T>> build(
			noinline allowedConstants: (sender: Player?) -> Map<T, Message?> = { enumValues<T>().associateWith { null } },
			notFoundExceptionType: ChatCommandExceptionType = ChatCommandExceptionType {
				Component.translatable("argument.id.unknown", Component.text(it[0].toString()))
			}
		) = EnumType(T::class.java, allowedConstants, notFoundExceptionType)
	}
}