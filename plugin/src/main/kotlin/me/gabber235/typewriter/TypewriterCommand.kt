package me.gabber235.typewriter

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.builders.LiteralDSLBuilder
import lirand.api.dsl.command.builders.command
import lirand.api.dsl.command.types.PlayerType
import lirand.api.dsl.command.types.WordType
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.extensions.readUnquoted
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.entries.CinematicStartTrigger
import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger.CINEMATIC_END
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.facts.formattedName
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.ui.CommunicationHandler
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.inventory.Book
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

fun Plugin.typeWriterCommand() = command("typewriter") {
	alias("tw")

	reloadCommands()

	factsCommands()

	clearChatCommand()

	connectCommand()

	cinematicCommand()
}

private fun LiteralDSLBuilder.reloadCommands() {
	literal("reload") {
		requiresPermissions("typewriter.reload")
		executes {
			source.msg("Reloading configuration...")
			TypewriterReloadEvent().callEvent()
			source.msg("Configuration reloaded!")
		}
	}
}

private fun LiteralDSLBuilder.factsCommands() {
	literal("facts") {
		requiresPermissions("typewriter.facts")
		fun Player.listCachedFacts(source: CommandSender) {
			val facts = FactDatabase.listCachedFacts(uniqueId)
			if (facts.isEmpty()) {
				source.msg("$name has no facts.")
			} else {
				source.sendMini("\n\n")
				val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
				source.msg("$name has the following facts:\n")
				facts.map { it to EntryDatabase.getFact(it.id) }.forEach { (fact, entry) ->
					if (entry == null) return@forEach

					source.sendMini(
						"<hover:show_text:'${
							entry.comment.replace(
								Regex(" +"),
								" "
							)
						}\n\n<gray><i>Click to modify'><click:suggest_command:'/tw facts $name set ${entry.name} ${fact.value}'><gray> - </gray><blue>${entry.formattedName}:</blue> ${fact.value} <gray><i>(${
							formatter.format(
								fact.lastUpdate
							)
						})</i></gray>"
					)
				}
			}
		}


		argument("player", PlayerType) { player ->
			executes {
				player.get().listCachedFacts(source)
			}

			literal("set") {
				argument("fact", FactType) { fact ->
					argument("value", integer(0)) { value ->
						executes {
							FactDatabase.modify(player.get().uniqueId) {
								set(fact.get().id, value.get())
							}
							source.msg("Set <blue>${fact.get().formattedName}</blue> to ${value.get()} for ${player.get().name}")
						}
					}
				}
			}

			literal("reset") {
				executes {
					val p = player.get()
					FactDatabase.modify(p.uniqueId) {
						FactDatabase.listCachedFacts(p.uniqueId).forEach { (id, _) ->
							set(id, 0)
						}
					}
					source.msg("All facts for ${p.name} have been reset.")
				}
			}
		}

		executesPlayer {
			source.listCachedFacts(source)
		}
		literal("set") {
			argument("fact", FactType) { fact ->
				argument("value", integer(0)) { value ->
					executesPlayer {
						FactDatabase.modify(source.uniqueId) {
							set(fact.get().id, value.get())
						}
						source.msg("Fact <blue>${fact.get().formattedName}</blue> set to ${value.get()}.")
					}
				}
			}
		}

		literal("reset") {
			executesPlayer {
				FactDatabase.modify(source.uniqueId) {
					FactDatabase.listCachedFacts(source.uniqueId).forEach { (id, _) ->
						set(id, 0)
					}
				}
				source.msg("All your facts have been reset.")
			}
		}
	}
}

private fun LiteralDSLBuilder.clearChatCommand() {
	literal("clearChat") {
		requiresPermissions("typewriter.clearChat")
		executesPlayer {
			source.chatHistory.let {
				it.clear()
				it.resendMessages(source)
			}
		}
	}
}

private fun LiteralDSLBuilder.connectCommand() {
	literal("connect") {
		requiresPermissions("typewriter.connect")
		executesConsole {
			if (CommunicationHandler.server == null) {
				source.msg("The server is not hosting the websocket. Try and enable it in the config.")
				return@executesConsole
			}

			val url = CommunicationHandler.generateUrl(playerId = null)
			source.msg("Connect to<blue> $url </blue>to start the connection.")
		}
		executesPlayer {
			if (CommunicationHandler.server == null) {
				source.msg("The server is not hosting the websocket. Try and enable it in the config.")
				return@executesPlayer
			}

			val url = CommunicationHandler.generateUrl(source.uniqueId)

			val bookTitle = "<blue>Connect to the server</blue>".asMini()
			val bookAuthor = "<blue>Typewriter</blue>".asMini()

			val bookPage = """
				|<blue><bold>Connect to Panel</bold></blue>
				|
				|<#3e4975>Click on the link below to connect to the panel. Once you are connected, you can start writing.</#3e4975>
				|
				|<hover:show_text:'<gray>Click to open the link'><click:open_url:'$url'><blue>[Link]</blue></click></hover>
				|
				|<gray><i>Because of security reasons, this link will expire in 5 minutes.</i></gray>
			""".trimMargin().asMini()

			val book = Book.book(bookTitle, bookAuthor, bookPage)
			source.openBook(book)
		}
	}
}

private fun LiteralDSLBuilder.cinematicCommand() = literal("cinematic") {
	literal("stop") {
		requiresPermissions("typewriter.cinematic.stop")
		executesPlayer {
			CINEMATIC_END triggerFor source
		}

		argument("player", PlayerType) { player ->
			executes {
				CINEMATIC_END triggerFor player.get()
			}
		}
	}

	literal("start") {
		requiresPermissions("typewriter.cinematic.start")

		argument("cinematic", CinematicType) { cinematicId ->
			executesPlayer {
				CinematicStartTrigger(cinematicId.get(), emptyList(), override = true) triggerFor source
			}

			argument("player", PlayerType) { player ->
				executes {
					CinematicStartTrigger(cinematicId.get(), emptyList(), override = true) triggerFor player.get()
				}
			}
		}
	}
}


open class FactType(
	open val notFoundExceptionType: ChatCommandExceptionType = PlayerType.notFoundExceptionType
) : WordType<FactEntry> {
	companion object Instance : FactType()

	override fun parse(reader: StringReader): FactEntry {
		val name = reader.readUnquoted()
		return EntryDatabase.findFactByName(name) ?: throw notFoundExceptionType.create(name)
	}


	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {

		EntryDatabase.facts.filter { it.name.startsWith(builder.remaining, true) }.forEach {
			builder.suggest(it.name)
		}

		return builder.buildFuture()
	}

	override fun getExamples(): Collection<String> = listOf("test.fact", "key.some_fact")
}

open class CinematicType(
	open val notFoundExceptionType: ChatCommandExceptionType = PlayerType.notFoundExceptionType
) : WordType<String> {
	companion object Instance : CinematicType()

	override fun parse(reader: StringReader): String {
		val name = reader.readString()
		if (name !in EntryDatabase.getPageNames()) throw notFoundExceptionType.create(name)
		return name
	}

	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {

		EntryDatabase.getPageNames().filter { it.startsWith(builder.remaining, true) }.forEach {
			builder.suggest(it)
		}

		return builder.buildFuture()
	}

	override fun getExamples(): Collection<String> = listOf("test.cinematic", "key.some_cinematic")
}