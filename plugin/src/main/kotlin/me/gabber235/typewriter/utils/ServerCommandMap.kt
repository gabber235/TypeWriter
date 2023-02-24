package me.gabber235.typewriter.utils

import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.entries.CustomCommandEntry
import me.gabber235.typewriter.entry.entries.CustomCommandEntry.CommandFilterResult.*
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.lang.reflect.Field
import java.lang.reflect.Method

private val commandMap: CommandMap by lazy {
	val commandMapField: Field = server.javaClass.getDeclaredField("commandMap")
	commandMapField.isAccessible = true
	commandMapField.get(server) as CommandMap
}

private val syncCommandsMethod: Method by lazy {
	val method = server.javaClass.getDeclaredMethod("syncCommands")
	method.isAccessible = true
	method
}


fun CustomCommandEntry.Companion.refreshAndRegisterAll(newEntries: List<CustomCommandEntry>): List<CustomCommandEntry> {
	EntryDatabase.commandEvents.forEach { it.unregister() }

	newEntries.forEach { it.register() }

	syncCommands()
	return newEntries
}

private fun syncCommands() {
	syncCommandsMethod.invoke(server)
}

fun CustomCommandEntry.register() {
	val result = commandMap.register("typewriter", object : BukkitCommand(command) {
		override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
			if (sender !is Player) {
				sender.msg("This command can only be executed by a player")
				return true
			}
			when (val filterResult = filter(sender, commandLabel, args)) {
				is Success                   -> this@register.execute(sender, commandLabel, args)
				is Failure                   -> {}
				is FailureWithDefaultMessage -> sender.msg("You can not execute this command")
				is FailureWithMessage        -> sender.msg(filterResult.message)
			}

			return true
		}
	})

	println("Registered command $command for $name (${id}) Success: $result")
}

fun CustomCommandEntry.unregister() {
	val command = commandMap.getCommand(command) ?: return
	commandMap.knownCommands.values.removeIf(command::equals)
	command.unregister(commandMap)
}
