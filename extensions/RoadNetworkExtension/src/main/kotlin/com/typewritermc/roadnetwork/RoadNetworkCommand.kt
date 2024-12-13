package com.typewritermc.roadnetwork

import com.typewritermc.core.extension.annotations.TypewriterCommand
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.ContentModeTrigger
import com.typewritermc.roadnetwork.content.RoadNetworkContentMode
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.entryArgument
import com.typewritermc.engine.paper.optionalTarget
import com.typewritermc.engine.paper.targetOrSelfPlayer
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.literalArgument

@TypewriterCommand
fun CommandTree.roadNetworkCommands() = literalArgument("roadNetwork") {
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
                    ).triggerFor(target, context())
                }
            }
        }
    }
}
