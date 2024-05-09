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

open class StateType(
	open val trueCases: (sender: Player?) -> Map<String, Message?> = Instance.trueCases,
	open val falseCases: (sender: Player?) -> Map<String, Message?> = Instance.falseCases,
	open val unknownStateExceptionType: ChatCommandExceptionType = Instance.unknownStateExceptionType
) : WordType<Boolean> {

	/**
	 * Returns a `true` if the string returned by the given [reader]
	 * corresponds to the states from the result of the [trueCases]
	 * or `false` if it corresponds to the states from the result of the [falseCases].
	 *
	 * @param reader the reader
	 * @return a [Boolean] that corresponds to the states from [trueCases] or [falseCases] respectively
	 * @throws ChatCommandSyntaxException if a state with the give name does not exist
	 */
	override fun parse(reader: StringReader): Boolean {
		return when (reader.readUnquoted()) {
			in trueCases(null) -> true
			in falseCases(null) -> false
			else -> throw unknownStateExceptionType.createWithContext(reader)
		}
	}

	/**
	 * Returns the names of states from the results of the [trueCases] and [falseCases]
	 * that start with the remaining input of the given [builder].
	 *
	 * @param S the type of the source
	 * @param context the context
	 * @param builder the builder
	 * @return the state names that begin with the remaining input
	 */
	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val sender = context.source as? Player? ?: return builder.buildFuture()

		(trueCases(sender) + falseCases(sender))
			.filterKeys { it.startsWith(builder.remaining, true) }
			.forEach { (case, tooltip) ->
				if (tooltip != null)
					builder.suggest(case, tooltip)
				else
					builder.suggest(case)
			}
		return builder.buildFuture()
	}

	override fun getExamples(): Collection<String> = listOf("enable", "disable")


	companion object Instance : StateType(
		trueCases = { mapOf("enable" to null) },
		falseCases = { mapOf("disable" to null) },
		unknownStateExceptionType =
			ChatCommandExceptionType(Component.translatable("command.unknown.argument"))
	)
}