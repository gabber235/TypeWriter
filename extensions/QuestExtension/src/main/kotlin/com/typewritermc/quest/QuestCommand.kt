package com.typewritermc.quest

import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.TypewriterCommand
import com.typewritermc.engine.paper.entryArgument
import com.typewritermc.engine.paper.optionalTarget
import com.typewritermc.engine.paper.targetOrSelfPlayer
import com.typewritermc.engine.paper.utils.msg
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.literalArgument

@TypewriterCommand
fun CommandTree.questCommands() = literalArgument("quest") {
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
