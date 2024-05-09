package lirand.api.dsl.command.types

import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import lirand.api.dsl.command.types.extensions.readUnquoted
import lirand.api.extensions.server.server
import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * A [Player] type.
 */
open class PlayerType(
    open val allowedPlayers: (sender: Player?) -> Map<out OfflinePlayer, Message?> = Instance.allowedPlayers,
    open val notFoundExceptionType: ChatCommandExceptionType = Instance.notFoundExceptionType
) : WordType<Player> {

    /**
     * Returns a [Player] from the result of the [allowedPlayers]
     * whose name matches the string returned by the given [reader].
     *
     * @param reader the reader
     * @return a [Player] with the given name
     * @throws ChatCommandSyntaxException if a player with the give name does not exist
     */
    override fun parse(reader: StringReader): Player {
        val name = reader.readUnquoted()
        return server.getPlayerExact(name)?.takeIf { it in allowedPlayers(null) }
            ?: throw notFoundExceptionType.createWithContext(reader)
    }

    /**
     * Returns the names of online players from the result of the [allowedPlayers]
     * that start with the remaining input of the given [builder].
     * If the source is a player, a check is performed to determine
     * the visibility of the suggested player to the source.
     * Players that are invisible to the source are not suggested.
     *
     * @param S the type of the source
     * @param context the context
     * @param builder the builder
     * @return the [Player] names that begin with the remaining input
     */
    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val sender = context.source as? Player? ?: return builder.buildFuture()

        allowedPlayers(sender).mapKeys { (offlinePlayer, _) -> offlinePlayer.player }
            .filterKeys { it != null && sender.canSee(it) && it.name.startsWith(builder.remaining, true) }
            .mapKeys { (player, _) -> player as Player }
            .forEach { (player, tooltip) ->
                if (tooltip != null)
                    builder.suggest(player.name, tooltip)
                else
                    builder.suggest(player.name)
            }

        return builder.buildFuture()
    }

    override fun getExamples(): Collection<String> = listOf("Bob", "Pante")


    companion object Instance : PlayerType(
        allowedPlayers = { server.onlinePlayers.associateWith { null } },
        notFoundExceptionType = ChatCommandExceptionType(
            Component.translatable("argument.entity.notfound.player")
        )
    )
}