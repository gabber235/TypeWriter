package lirand.api.dsl.command.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.builders.ContinuableNodeDSLBuilder
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import lirand.api.dsl.command.types.extensions.until
import lirand.api.extensions.server.nmsNumberVersion
import lirand.api.extensions.server.nmsVersion
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

fun <T> ContinuableNodeDSLBuilder<*>.prefixed(
	prefixedType: Pair<String, ArgumentType<T>>,
	unknownPrefixExceptionType: ChatCommandExceptionType = PrefixedType.defaultExceptionType
) = PrefixedType(prefixedType.first, prefixedType.second, unknownPrefixExceptionType)


open class PrefixedType<T>(
	val prefix: String,
	open val type: ArgumentType<T>,
	open val unknownPrefixExceptionType: ChatCommandExceptionType = defaultExceptionType
) : Type<T> {

	val prefixForm = "${prefix.lowercase()}:"

	/**
	 * Returns the result of parsing argument of the provided [type] if it has [prefix].
	 *
	 * @param reader the reader
	 * @return a [T] parsed by [type].
	 * @throws ChatCommandSyntaxException if argument hasn't been parsed by [type] or doesn't have [prefix].
	 */
	override fun parse(reader: StringReader): T {
		val prefix = reader.until(':').lowercase()
		if (reader.canRead()) reader.skip()
		else throw unknownPrefixExceptionType.createWithContext(reader, prefix)

		if (prefix == this.prefix.lowercase())
			return type.parse(reader)
		else
			throw unknownPrefixExceptionType.createWithContext(reader, prefix)
	}

	/**
	 * Returns the result of type's [ArgumentType.listSuggestions] if [prefix] was provided.
	 * If not returns [prefix].
	 *
	 * @param S the type of the source
	 * @param context the context
	 * @param builder the builder
	 * @return the result of type's [ArgumentType.listSuggestions] if [prefix] was provided
	 */
	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		if (builder.remaining.startsWith(prefixForm)) {
			return type.listSuggestions(context, SuggestionsBuilder(builder.input, builder.start + prefixForm.length))
		}

		if (prefixForm.startsWith(builder.remaining)) {
			builder.suggest(prefixForm)
		}
		
		return builder.buildFuture()
	}

	override fun getExamples(): List<String> = type.examples.map { "$prefix:$it" }


	override fun map(): ArgumentType<*> {
		return argumentTag
	}



	companion object {
		val argumentTag = run {
			val nmsPackage = if (nmsNumberVersion < 17)
				"net.minecraft.server.v$nmsVersion"
			else
				"net.minecraft.commands.arguments.item"

			val clazz = Class.forName("$nmsPackage.ArgumentTag")

			clazz.getConstructor()
				.newInstance() as ArgumentType<*>
		}

		internal val defaultExceptionType = ChatCommandExceptionType {
			Component.translatable("argument.id.unknown", Component.text(it[0].toString()))
		}
	}
}