package com.typewritermc.engine.paper

import com.typewritermc.core.books.pages.PageType
import com.typewritermc.core.entries.*
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.entry.entries.SystemTrigger.CINEMATIC_END
import com.typewritermc.engine.paper.entry.quest.trackQuest
import com.typewritermc.engine.paper.entry.quest.unTrackQuest
import com.typewritermc.engine.paper.entry.roadnetwork.content.RoadNetworkContentMode
import com.typewritermc.engine.paper.interaction.chatHistory
import com.typewritermc.engine.paper.ui.CommunicationHandler
import com.typewritermc.engine.paper.utils.ThreadType
import com.typewritermc.engine.paper.utils.asMini
import com.typewritermc.engine.paper.utils.msg
import com.typewritermc.engine.paper.utils.sendMini
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.StringTooltip
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.inventory.Book
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get
import java.time.format.DateTimeFormatter

fun typeWriterCommand() = commandTree("typewriter") {
    withAliases("tw")

    reloadCommands()

    factsCommands()

    clearChatCommand()

    connectCommand()

    cinematicCommand()

    triggerCommand()
    fireCommand()


    questCommands()

    roadNetworkCommands()

    manifestCommands()
}

private fun CommandTree.reloadCommands() = literalArgument("reload") {
    withPermission("typewriter.reload")
    anyExecutor { sender, _ ->
        sender.msg("Reloading configuration...")
        ThreadType.DISPATCHERS_ASYNC.launch {
            plugin.reload()
            sender.msg("Configuration reloaded!")
        }
    }
}

private fun CommandTree.factsCommands() = literalArgument("facts") {
    withPermission("typewriter.facts")

    literalArgument("set") {
        withPermission("typewriter.facts.set")
        argument(entryArgument<WritableFactEntry>("fact")) {
            integerArgument("value") {
                optionalTarget {
                    anyExecutor { sender, args ->
                        val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                        val fact = args["fact"] as WritableFactEntry
                        val value = args["value"] as Int
                        fact.write(target, value)
                        sender.msg("Fact <blue>${fact.formattedName}</blue> set to $value for ${target.name}.")
                    }
                }
            }
        }
    }

    literalArgument("reset") {
        withPermission("typewriter.facts.reset")
        optionalTarget {
            anyExecutor { sender, args ->
                val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                val entries = Query.find<WritableFactEntry>().toList()
                if (entries.none()) {
                    sender.msg("There are no facts available.")
                    return@anyExecutor
                }

                for (entry in entries) {
                    entry.write(target, 0)
                }
                sender.msg("All facts for ${target.name} have been reset.")
            }
        }
    }

    optionalTarget {
        anyExecutor { sender, args ->
            val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor

            val factEntries = Query.find<ReadableFactEntry>().toList()
            if (factEntries.none()) {
                sender.msg("There are no facts available.")
                return@anyExecutor
            }

            sender.sendMini("\n\n")
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
            sender.msg("${target.name} has the following facts:\n")

            for (entry in factEntries) {
                val data = entry.readForPlayersGroup(target)
                sender.sendMini(
                    "<hover:show_text:'${
                        entry.comment.replace(
                            Regex(" +"),
                            " "
                        ).replace("'", "\\'")
                    }\n\n<gray><i>Click to modify'><click:suggest_command:'/tw facts set ${entry.name} ${data.value} ${target.name}'><gray> - </gray><blue>${entry.formattedName}:</blue> ${data.value} <gray><i>(${
                        formatter.format(
                            data.lastUpdate
                        )
                    })</i></gray>"
                )
            }
        }
    }
}

private fun CommandTree.clearChatCommand() = literalArgument("clearChat") {
    withPermission("typewriter.clearChat")
    playerExecutor { player, _ ->
        player.chatHistory.let {
            it.clear()
            it.resendMessages(player)
        }
    }
}

private fun CommandTree.connectCommand() {
    val communicationHandler: CommunicationHandler = get(CommunicationHandler::class.java)
    literalArgument("connect") {
        withPermission("typewriter.connect")
        consoleExecutor { console, _ ->
            if (communicationHandler.server == null) {
                console.msg("The server is not hosting the websocket. Try and enable it in the config.")
                return@consoleExecutor
            }

            val url = communicationHandler.generateUrl(playerId = null)
            console.msg("Connect to<blue> $url </blue>to start the connection.")
        }
        playerExecutor { player, _ ->
            if (communicationHandler.server == null) {
                player.msg("The server is not hosting the websocket. Try and enable it in the config.")
                return@playerExecutor
            }

            val url = communicationHandler.generateUrl(player.uniqueId)

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
            player.openBook(book)
        }
    }
}

private fun CommandTree.cinematicCommand() = literalArgument("cinematic") {
    literalArgument("start") {
        withPermission("typewriter.cinematic.start")

        argument(pages("cinematic", PageType.CINEMATIC)) {
            optionalTarget {
                anyExecutor { sender, args ->
                    val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                    val page = args["cinematic"] as Page
                    CinematicStartTrigger(page.id, emptyList()) triggerFor target
                }
            }
        }
    }

    literalArgument("stop") {
        withPermission("typewriter.cinematic.stop")
        optionalTarget {
            anyExecutor { sender, args ->
                val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                CINEMATIC_END triggerFor target
            }
        }
    }
}

private fun CommandTree.triggerCommand() = literalArgument("trigger") {
    withPermission("typewriter.trigger")

    argument(entryArgument<TriggerableEntry>("entry")) {
        optionalTarget {
            anyExecutor { sender, args ->
                val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                val entry = args["entry"] as TriggerableEntry
                EntryTrigger(entry) triggerFor target
            }
        }
    }
}

private fun CommandTree.fireCommand() = literalArgument("fire") {
    withPermission("typewriter.fire")

    argument(entryArgument<FireTriggerEventEntry>("entry")) {
        optionalTarget {
            anyExecutor { sender, args ->
                val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                val entry = args["entry"] as FireTriggerEventEntry
                entry.triggers triggerEntriesFor target
            }
        }
    }
}

private fun CommandTree.questCommands() = literalArgument("quest") {
    literalArgument("track") {
        withPermission("typewriter.quest.track")

        argument(entryArgument<QuestEntry>("quest")) {
            optionalTarget {
                anyExecutor { sender, args ->
                    val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                    val quest = args["quest"] as QuestEntry
                    target.trackQuest(quest.ref())
                    sender.msg("You are now tracking <blue>${quest.display(target)}</blue>.")
                }
            }
        }
    }


    literalArgument("untrack") {
        withPermission("typewriter.quest.untrack")

        optionalTarget {
            anyExecutor { sender, args ->
                val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                target.unTrackQuest()
                sender.msg("You are no longer tracking any quests.")
            }
        }
    }
}

private fun CommandTree.roadNetworkCommands() = literalArgument("roadNetwork") {
    literalArgument("edit") {
        withPermission("typewriter.roadNetwork.edit")

        argument(entryArgument<RoadNetworkEntry>("network")) {
            optionalTarget {
                anyExecutor { sender, args ->
                    val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                    val entry = args["network"] as RoadNetworkEntry
                    val data = mapOf(
                        "entryId" to entry.id
                    )
                    val context = ContentContext(data)
                    ContentModeTrigger(
                        context,
                        RoadNetworkContentMode(context, target)
                    ) triggerFor target
                }
            }
        }
    }
}

private fun CommandTree.manifestCommands() = literalArgument("manifest") {
    literalArgument("inspect") {
        withPermission("typewriter.manifest.inspect")

        optionalTarget {
            anyExecutor { sender, args ->
                val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                val inEntries = Query.findWhere<AudienceEntry> { target.inAudience(it) }.sortedBy { it.name }.toList()
                if (inEntries.none()) {
                    sender.msg("You are not in any audience entries.")
                    return@anyExecutor
                }

                sender.sendMini("\n\n")
                sender.msg("You are in the following audience entries:")
                for (entry in inEntries) {
                    sender.sendMini(
                        "<hover:show_text:'<gray>${entry.id}'><click:copy_to_clipboard:${entry.id}><gray> - </gray><blue>${entry.formattedName}</blue></click></hover>"
                    )
                }
            }
        }
    }

    literalArgument("page") {
        argument(pages("page", PageType.MANIFEST)) {
            optionalTarget {
                anyExecutor { sender, args ->
                    val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                    val page = args["page"] as Page
                    val audienceEntries =
                        Query.findWhereFromPage<AudienceEntry>(page.id) { true }.sortedBy { it.name }.toList()

                    if (audienceEntries.isEmpty()) {
                        sender.msg("No audience entries found on page ${page.name}")
                        return@anyExecutor
                    }

                    val entryStates = audienceEntries.groupBy { target.audienceState(it) }

                    sender.sendMini("\n\n")
                    sender.msg("These are the audience entries on page <i>${page.name}</i>:")
                    for (state in AudienceDisplayState.entries) {
                        val entries = entryStates[state] ?: continue
                        val color = state.color
                        sender.sendMini("\n<b><$color>${state.displayName}</$color></b>")

                        for (entry in entries) {
                            sender.sendMini(
                                "<hover:show_text:'<gray>${entry.id}'><click:copy_to_clipboard:${entry.id}><gray> - </gray><$color>${entry.formattedName}</$color></click></hover>"
                            )
                        }
                    }
                }
            }
        }
    }
}

fun CommandArguments.targetOrSelfPlayer(commandSender: CommandSender): Player? {
    val target = this["target"] as? Player
    if (target != null) return target
    val self = commandSender as? Player
    if (self != null) return self
    commandSender.msg("<red>You must specify a target to execute this command on.")
    return null
}

fun Argument<*>.optionalTarget(block: Argument<*>.() -> Unit) = playerArgument("target", optional = true, block)

inline fun <reified E : Entry> entryArgument(name: String): Argument<E> = CustomArgument(StringArgument(name)) { info ->
    Query.findById(E::class, info.input)
        ?: Query.findByName(E::class, info.input)
        ?: throw CustomArgumentException.fromMessageBuilder(MessageBuilder("Could not find entry: ").appendArgInput())
}.replaceSuggestions(ArgumentSuggestions.stringsWithTooltips { _ ->
    Query.find<E>().map {
        StringTooltip.ofString(it.name, it.id)
    }.toList().toTypedArray()
})

fun pages(name: String, type: PageType): Argument<Page> = CustomArgument(StringArgument(name)) { info ->
    val pages = Query.findPagesOfType(type).toList()
    val page = pages.firstOrNull { it.id == info.input || it.name == info.input }
    if (page == null) {
        throw CustomArgumentException.fromMessageBuilder(MessageBuilder("Page does not exist."))
    }
    page
}.replaceSuggestions(ArgumentSuggestions.stringsWithTooltips { _ ->
    val pages = Query.findPagesOfType(type).toList()
    pages.map {
        StringTooltip.ofString(it.name, it.name)
    }.toList().toTypedArray()
})