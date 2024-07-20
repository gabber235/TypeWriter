package me.gabber235.typewriter.utils

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandTree
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.entries.CustomCommandEntry
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import org.koin.java.KoinJavaComponent.get

fun CustomCommandEntry.Companion.refreshAndRegisterAll(newEntries: List<CustomCommandEntry>): List<CustomCommandEntry> {
    val oldEntries = get<EntryDatabase>(EntryDatabase::class.java).commandEvents

    if (oldEntries.isEmpty() && newEntries.isEmpty()) return emptyList()

    oldEntries.forEach { it.unregister() }
    newEntries.forEach { it.register() }

    return newEntries
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