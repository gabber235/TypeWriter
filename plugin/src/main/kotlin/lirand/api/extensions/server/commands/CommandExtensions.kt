package lirand.api.extensions.server.commands

import lirand.api.extensions.server.server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field

private val serverCommands: SimpleCommandMap by lazy {
	server::class.java.getDeclaredField("commandMap").apply {
		isAccessible = true
	}.get(server) as SimpleCommandMap
}

private val knownCommandsField: Field by lazy {
	SimpleCommandMap::class.java.getDeclaredField("knownCommands").apply {
		isAccessible = true
	}
}

fun Command.register(plugin: Plugin) {
	serverCommands.register(plugin.name, this)

	val commands = provideCommandController(plugin)?.commands ?: return
	commands.add(this)
}

fun Command.unregister(plugin: Plugin) {
	try {
		val knownCommands = knownCommandsField.get(serverCommands) as MutableMap<String, Command>
		val toRemove = ArrayList<String>()
		for ((key, value) in knownCommands) {
			if (value === this) {
				toRemove.add(key)
			}
		}
		for (name in toRemove) {
			knownCommands.remove(name)
		}

		provideCommandController(plugin)?.commands?.removeIf { it === this }
	} catch (e: Exception) {
		e.printStackTrace()
	}
}

/**
 * Dispatches the command given by [commandLine].
 *
 * @param commandLine the command without a leading /
 */
fun CommandSender.dispatchCommand(commandLine: String) =
	server.dispatchCommand(this, commandLine)