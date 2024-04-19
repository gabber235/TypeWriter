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
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.PageType.CINEMATIC
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.entries.SystemTrigger.CINEMATIC_END
import me.gabber235.typewriter.entry.quest.trackQuest
import me.gabber235.typewriter.entry.quest.unTrackQuest
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkEditorState
import me.gabber235.typewriter.entry.roadnetwork.content.RoadNetworkContentMode
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.ui.CommunicationHandler
import me.gabber235.typewriter.utils.ThreadType
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

    questCommands()

    assetsCommands()

    roadNetworkCommands()

    manifestCommands()
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
            val factEntries = Query.find<ReadableFactEntry>().toList()
            if (factEntries.none()) {
                source.msg("There are no facts available.")
                return
            }

            source.sendMini("\n\n")
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
            source.msg("$name has the following facts:\n")

            for (entry in factEntries) {
                val data = entry.readForPlayersGroup(this)
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
                    val entries = Query.find<WritableFactEntry>().toList()
                    if (entries.none()) {
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
                val entries = Query.find<WritableFactEntry>().toList()
                if (entries.none()) {
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
                CinematicStartTrigger(cinematicId.get(), emptyList()) triggerFor source
            }

            argument("player", PlayerType) { player ->
                executes {
                    CinematicStartTrigger(cinematicId.get(), emptyList()) triggerFor player.get()
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

private fun LiteralDSLBuilder.questCommands() = literal("quest") {
    literal("track") {
        requiresPermissions("typewriter.quest.track")
        argument("quest", entryType<QuestEntry>()) { quest ->
            executesPlayer {
                source.trackQuest(quest.get().ref())
                source.msg("You are now tracking <blue>${quest.get().display(source)}</blue>.")
            }

            argument("player", PlayerType) { player ->
                requiresPermissions("typewriter.quest.track.other")
                executes {
                    player.get().trackQuest(quest.get().ref())
                    source.msg(
                        "You are now tracking <blue>${
                            quest.get().display(player.get())
                        }</blue> for ${player.get().name}."
                    )
                }
            }
        }
    }

    literal("untrack") {
        requiresPermissions("typewriter.quest.untrack")
        executesPlayer {
            source.unTrackQuest()
            source.msg("You are no longer tracking any quests.")
        }

        argument("player", PlayerType) { player ->
            requiresPermissions("typewriter.quest.untrack.other")
            executes {
                player.get().unTrackQuest()
                source.msg("You are no longer tracking any quests for ${player.get().name}.")
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
                source.msg("Cleaning unused assets...")
                ThreadType.DISPATCHERS_ASYNC.launch {
                    val deleted = get<AssetManager>(AssetManager::class.java).removeUnusedAssets()
                    source.msg("Cleaned <blue>${deleted}</blue> assets.")
                }
            }
        }
    }
}

private fun LiteralDSLBuilder.roadNetworkCommands() =
    literal("roadNetwork") {
        requiresPermissions("typewriter.roadNetwork")
        literal("edit") {
            requiresPermissions("typewriter.roadNetwork.edit")
            argument("network", entryType<RoadNetworkEntry>()) { network ->
                fun Player.editRoadNetwork(entry: RoadNetworkEntry) {
                    val data = mapOf(
                        "entryId" to entry.id
                    )
                    val context = ContentContext(data)
                    ContentModeTrigger(
                        context,
                        RoadNetworkContentMode(context, this)
                    ) triggerFor this
                }
                executesPlayer {
                    source.editRoadNetwork(network.get())
                }

                argument("player", PlayerType) { player ->
                    requiresPermissions("typewriter.roadNetwork.edit.other")
                    executes {
                        player.get().editRoadNetwork(network.get())
                    }
                }
            }
        }
    }

private fun LiteralDSLBuilder.manifestCommands() = literal("manifest") {
    requiresPermissions("typewriter.manifest")
    literal("inspect") {
        requiresPermissions("typewriter.manifest.inspect")

        fun Player.inspectManifest() {
            val inEntries = Query.findWhere<AudienceEntry> { inAudience(it) }.toList()
            if (inEntries.none()) {
                msg("You are not in any audience entries.")
                return
            }

            sendMini("\n\n")
            msg("You are in the following audience entries:")
            for (entry in inEntries) {
                sendMini(
                    "<hover:show_text:'<gray>${entry.id}'><click:copy_to_clipboard:${entry.id}><gray> - </gray><blue>${entry.formattedName}</blue></click></hover>"
                )
            }
        }

        executesPlayer {
            source.inspectManifest()
        }

        argument("player", PlayerType) { player ->
            requiresPermissions("typewriter.manifest.inspect.other")
            executes {
                player.get().inspectManifest()
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