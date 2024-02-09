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
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.PageType.CINEMATIC
import me.gabber235.typewriter.entry.entries.CinematicStartTrigger
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger.CINEMATIC_END
import me.gabber235.typewriter.entry.entries.WritableFactEntry
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.facts.formattedName
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.ui.CommunicationHandler
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.msg
import me.gabber235.typewriter.utils.sendMini
import net.kyori.adventure.inventory.Book
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.get
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass

fun Plugin.typeWriterCommand() = command("typewriter") {
    alias("tw")

    reloadCommands()

    factsCommands()

    clearChatCommand()

    connectCommand()

    cinematicCommand()

    triggerCommand()

    assetsCommands()
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
            val factEntries = Query.find<ReadableFactEntry>()
            if (factEntries.isEmpty()) {
                source.msg("There are no facts available.")
                return
            }

            source.sendMini("\n\n")
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
            source.msg("$name has the following facts:\n")

            for (entry in factEntries) {
                val data = entry.readForPlayer(this)
                source.sendMini(
                    "<hover:show_text:'${
                        entry.comment.replace(
                            Regex(" +"),
                            " "
                        ).replace("'", "\\'")
                    }\n\n<gray><i>Click to modify'><click:suggest_command:'/tw facts $name set ${entry.name} ${data.value}'><gray> - </gray><blue>${entry.formattedName}:</blue> ${data.value} <gray><i>(${
                        formatter.format(
                            data.lastUpdate
                        )
                    })</i></gray>"
                )
            }
        }


        argument("player", PlayerType) { player ->
            executes {
                player.get().listCachedFacts(source)
            }

            literal("set") {
                argument("fact", entryType<WritableFactEntry>()) { fact ->
                    argument("value", integer(0)) { value ->
                        executes {
                            fact.get().write(player.get(), value.get())
                            source.msg("Set <blue>${fact.get().formattedName}</blue> to ${value.get()} for ${player.get().name}")
                        }
                    }
                }
            }

            literal("reset") {
                executes {
                    val p = player.get()
                    val entries = Query.find<WritableFactEntry>()
                    if (entries.isEmpty()) {
                        source.msg("There are no facts available.")
                        return@executes
                    }

                    for (entry in entries) {
                        entry.write(p, 0)
                    }
                    source.msg("All facts for ${p.name} have been reset.")
                }
            }
        }

        executesPlayer {
            source.listCachedFacts(source)
        }
        literal("set") {
            argument("fact", entryType<WritableFactEntry>()) { fact ->
                argument("value", integer(0)) { value ->
                    executesPlayer {
                        fact.get().write(source, value.get())
                        source.msg("Fact <blue>${fact.get().formattedName}</blue> set to ${value.get()}.")
                    }
                }
            }
        }

        literal("reset") {
            executesPlayer {
                val entries = Query.find<WritableFactEntry>()
                if (entries.isEmpty()) {
                    source.msg("There are no facts available.")
                    return@executesPlayer
                }

                for (entry in entries) {
                    entry.write(source, 0)
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
    val communicationHandler: CommunicationHandler = get(CommunicationHandler::class.java)
    literal("connect") {
        requiresPermissions("typewriter.connect")
        executesConsole {
            if (communicationHandler.server == null) {
                source.msg("The server is not hosting the websocket. Try and enable it in the config.")
                return@executesConsole
            }

            val url = communicationHandler.generateUrl(playerId = null)
            source.msg("Connect to<blue> $url </blue>to start the connection.")
        }
        executesPlayer {
            if (communicationHandler.server == null) {
                source.msg("The server is not hosting the websocket. Try and enable it in the config.")
                return@executesPlayer
            }

            val url = communicationHandler.generateUrl(source.uniqueId)

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

    literal("simulate") {
        requiresPermissions("typewriter.cinematic.start")

        argument("cinematic", CinematicType) { cinematicId ->
            executesPlayer {
                CinematicStartTrigger(
                    cinematicId.get(),
                    emptyList(),
                    override = true,
                    simulate = true
                ) triggerFor source
            }

            argument("player", PlayerType) { player ->
                executes {
                    CinematicStartTrigger(
                        cinematicId.get(),
                        emptyList(),
                        override = true,
                        simulate = true
                    ) triggerFor player.get()
                }
            }
        }
    }
}

private fun LiteralDSLBuilder.triggerCommand() = literal("trigger") {
    requiresPermissions("typewriter.trigger")
    argument("entry", entryType<TriggerableEntry>()) { entry ->
        executesPlayer {
            EntryTrigger(entry.get()) triggerFor source
        }

        argument("player", PlayerType) { player ->
            executes {
                EntryTrigger(entry.get()) triggerFor player.get()
            }
        }

    }
}

private fun LiteralDSLBuilder.assetsCommands() {
    literal("assets") {
        requiresPermissions("typewriter.assets")
        literal("clean") {
            requiresPermissions("typewriter.assets.clean")
            executes {
                val deleted = get<AssetManager>(AssetManager::class.java).removeUnusedAssets()
                source.msg("Cleaned <blue>${deleted}</blue> assets.")
            }
        }
    }
}

inline fun <reified E : Entry> entryType() = EntryType(E::class)

open class EntryType<E : Entry>(
    val type: KClass<E>,
    open val notFoundExceptionType: ChatCommandExceptionType = PlayerType.notFoundExceptionType
) : WordType<E>, KoinComponent {
    override fun parse(reader: StringReader): E {
        val arg = reader.readUnquoted()
        return Query.findById(type, arg) ?: Query.findByName(type, arg) ?: throw notFoundExceptionType.create(arg)
    }


    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {

        Query.findWhere(type) { it.name.startsWith(builder.remaining, true) }.forEach {
            builder.suggest(it.name)
        }

        return builder.buildFuture()
    }

    override fun getExamples(): Collection<String> = emptyList()
}

open class CinematicType(
    open val notFoundExceptionType: ChatCommandExceptionType = PlayerType.notFoundExceptionType
) : WordType<String>, KoinComponent {
    private val entryDatabase: EntryDatabase by inject()

    companion object Instance : CinematicType()

    override fun parse(reader: StringReader): String {
        val name = reader.readString()
        if (name !in entryDatabase.getPageNames(CINEMATIC)) throw notFoundExceptionType.create(name)
        return name
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {

        entryDatabase.getPageNames(CINEMATIC).filter { it.startsWith(builder.remaining, true) }.forEach {
            builder.suggest(it)
        }

        return builder.buildFuture()
    }

    override fun getExamples(): Collection<String> = listOf("test.cinematic", "key.some_cinematic")
}