package lirand.api.dsl.command.types

import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import lirand.api.dsl.command.types.extensions.TranslatableMessage
import lirand.api.dsl.command.types.extensions.readUnquoted
import lirand.api.dsl.command.types.extensions.suggestTranslatable
import lirand.api.extensions.server.server
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture


/**
 * A type that supports a comma separated list of [Player] names enclosed
 * in double quotation marks in addition to the following tags:
 *
 *  * `@a` - All online players
 *  * `@r` - A random online player
 *
 */
open class PlayersType(
	open val allowedPlayers: (sender: Player?) -> Map<out OfflinePlayer, Message?> = Instance.allowedPlayers,
	open val notFoundExceptionType: ChatCommandExceptionType = Instance.notFoundExceptionType,
	open val additionalSelectors: AdditionalSelectors = Instance.additionalSelectors,
	open val invalidSelectorExceptionType: ChatCommandExceptionType = Instance.invalidSelectorExceptionType
) : QuotableStringType<List<Player>> {

	/**
	 * Returns the online players from the result of the [allowedPlayers]
	 * whose names are contained in the string returned
	 * by the given [reader] or were chosen through the supported tags.
	 *
	 * @param reader the reader
	 * @return the online players whose names are contained in the given string
	 * or were chosen through the supported tags
	 * @throws ChatCommandSyntaxException if an unsupported tag or [Player] name was given
	 * or @a was used in a comma separated list of arguments
	 */
	override fun parse(reader: StringReader): List<Player> {
		val allowedOnlinePlayers = allowedPlayers(null).keys.mapNotNull { it.player }.toMutableList()

		val argument = if (reader.peek() == '"') reader.readQuotedString() else reader.readUnquoted()
		if (additionalSelectors.isAllSelectorSupported
			&& argument.equals("@a", ignoreCase = true)
		) {
			return allowedOnlinePlayers
		}

		val players = mutableListOf<Player>()
		val names = comma.split(argument)
		for (name in names) {
			if (additionalSelectors.isRandomSelectorSupported && name == "@r") {
				players.add(allowedOnlinePlayers.random().also {
					allowedOnlinePlayers.remove(it)
				})

				continue
			}

			val player = server.getPlayerExact(name)
			if (player != null && player in allowedOnlinePlayers) {
				players.add(player)
			}
			else if (name == "@a" || (!additionalSelectors.isRandomSelectorSupported && name == "@r")) {
				throw invalidSelectorExceptionType.createWithContext(reader)
			}
			else {
				throw notFoundExceptionType.createWithContext(reader)
			}
		}
		return players
	}

	/**
	 * Returns the names of online players from the result of the [allowedPlayers]
	 * and tags that start with the given input.
	 *
	 * @param S the type of the source
	 * @param context the context
	 * @param builder the builder
	 * @return the names of online players from the result of [allowedPlayers] and tags
	 * that start with the remaining input
	 */
	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val sender = context.source as? Player? ?: return builder.buildFuture()

		val allowedOnlinePlayers = allowedPlayers(sender).mapKeys { (offlinePlayer, _) -> offlinePlayer.player }
			.filterKeys { it != null }
			.mapKeys { (player, _) -> player as Player }

		var remaining = builder.remaining

		if (additionalSelectors.isAllSelectorSupported && "@".startsWith(remaining)) {
			builder.suggestTranslatable(
				"@a",
				TranslatableMessage("argument.entity.selector.allPlayers")
			)
		}

		val isStartQuoted = remaining.startsWith("\"")
		remaining = remaining.replace("\"", "")

		val parts = comma.split(remaining)
		val beginningParts = parts.slice(0 until parts.lastIndex)
		val last = parts.last()
		val beginning = remaining.removeSuffix(last)

		if (additionalSelectors.isRandomSelectorSupported && "@r".startsWith(last)
			&& beginningParts.size < allowedOnlinePlayers.size
		) {
			var suggestion = "$beginning@r"
			if (isStartQuoted) suggestion = "\"$suggestion"

			builder.suggestTranslatable(
				suggestion,
				TranslatableMessage("argument.entity.selector.randomPlayer")
			)
		}

		if (!isStartQuoted && parts.size > 1) return builder.buildFuture()

		allowedOnlinePlayers
			.filterKeys {
				sender.canSee(it) && it.name.startsWith(last)
					&& it.name !in beginningParts
			}
			.forEach { (player, tooltip) ->
				var suggestion = "$beginning${player.name}"
				if (isStartQuoted) suggestion = "\"$suggestion"

				if (tooltip != null)
					builder.suggest(suggestion, tooltip)
				else
					builder.suggest(suggestion)
			}

		return builder.buildFuture()
	}

	override fun getExamples(): Collection<String> = listOf("@a", "@r", "\"dyamo, Notch\"")


	companion object Instance : PlayersType(
		allowedPlayers = { server.onlinePlayers.associateWith { null } },
		notFoundExceptionType = ChatCommandExceptionType(
			Component.translatable("argument.entity.notfound.player")
		),
		additionalSelectors = AdditionalSelectors.NONE,
		invalidSelectorExceptionType = ChatCommandExceptionType(
			Component.text("'@a' cannot be used in a list of players."),
		)
	) {
		private val comma = Regex(""",\s*""")
	}
}


enum class AdditionalSelectors {
	ANY, ONLY_ALL, ONLY_RANDOM, NONE;

	val isAllSelectorSupported: Boolean
		get() = this == ANY || this == ONLY_ALL

	val isRandomSelectorSupported: Boolean
		get() = this == ANY || this == ONLY_RANDOM
}