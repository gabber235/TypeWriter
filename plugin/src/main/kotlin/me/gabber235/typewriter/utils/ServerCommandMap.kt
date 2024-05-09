package me.gabber235.typewriter.utils

import lirand.api.dsl.command.builders.LiteralDSLBuilder
import lirand.api.dsl.command.implementation.dispatcher.Dispatcher
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

    Dispatcher.of(plugin).update()
    return newEntries
}

fun CustomCommandEntry.register() {
    Dispatcher.of(plugin).register(LiteralDSLBuilder(plugin, command).apply { builder() })

    logger.info("Registered command $command for $name (${id})")
}

fun CustomCommandEntry.unregister() {
    Dispatcher.of(plugin).unregister(command)
}