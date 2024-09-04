package com.typewritermc.engine.paper.utils

import com.typewritermc.core.entries.Query
import com.typewritermc.engine.paper.entry.entries.CustomCommandEntry
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.plugin
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandTree

fun CustomCommandEntry.Companion.registerAll() {
    val entries = Query.find<CustomCommandEntry>()
    entries.forEach { it.register() }
}

fun CustomCommandEntry.Companion.unregisterAll() {
    val entries = Query.find<CustomCommandEntry>()
    entries.forEach { it.unregister() }
}

fun CustomCommandEntry.register() {
    if (command.isBlank()) {
        logger.warning("Command for $name is blank. Skipping registration.")
        return
    }
    CommandTree(command).apply { builder() }.register(plugin)

    logger.info("Registered command $command for $name (${id})")
}

fun CustomCommandEntry.unregister() {
    if (command.isBlank()) return
    CommandAPI.unregister(command)
}