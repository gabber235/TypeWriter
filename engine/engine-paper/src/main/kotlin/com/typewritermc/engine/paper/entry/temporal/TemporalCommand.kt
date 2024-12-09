package com.typewritermc.engine.paper.entry.temporal

import com.typewritermc.core.books.pages.PageType
import com.typewritermc.core.entries.Page
import com.typewritermc.engine.paper.entry.entries.InteractionEndTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.optionalTarget
import com.typewritermc.engine.paper.pages
import com.typewritermc.engine.paper.targetOrSelfPlayer
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.literalArgument

fun CommandTree.temporalCommands() = literalArgument("cinematic") {
    literalArgument("start") {
        withPermission("typewriter.cinematic.start")

        argument(pages("cinematic", PageType.CINEMATIC)) {
            optionalTarget {
                anyExecutor { sender, args ->
                    val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                    val page = args["cinematic"] as Page
                    TemporalStartTrigger(page.id, emptyList()) triggerFor target
                }
            }
        }
    }

    literalArgument("stop") {
        withPermission("typewriter.cinematic.stop")
        optionalTarget {
            anyExecutor { sender, args ->
                val target = args.targetOrSelfPlayer(sender) ?: return@anyExecutor
                InteractionEndTrigger triggerFor target
            }
        }
    }
}
