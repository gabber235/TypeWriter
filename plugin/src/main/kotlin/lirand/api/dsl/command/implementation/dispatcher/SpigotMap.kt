package lirand.api.dsl.command.implementation.dispatcher

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.LiteralCommandNode
import lirand.api.dsl.command.implementation.tree.nodes.AliasableCommandNode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field

internal class SpigotMap(var prefix: String, var plugin: Plugin, var map: SimpleCommandMap) : PlatformMap {

	private val knownCommands: MutableMap<String, Command> =
		mapKnownCommandsField.get(map) as MutableMap<String, Command>

	lateinit var dispatcher: CommandDispatcher<CommandSender>

	override fun register(node: LiteralCommandNode<CommandSender>): DispatcherCommand? {
		if (knownCommands.containsKey(node.name)) {
			return null
		}
		val wrapped = wrap(node)
		map.register(prefix, wrapped)
		return wrapped
	}

	override fun unregister(name: String): DispatcherCommand? {
		val commands = knownCommands
		val command = commands[name] as? DispatcherCommand ?: return null
		commands.remove(name, command)
		commands.remove("$prefix:$name", command)
		for (alias in command.aliases) {
			commands.remove(alias, command)
			commands.remove("$prefix:$alias", command)
		}
		command.unregister(map)
		return command
	}

	fun wrap(node: LiteralCommandNode<CommandSender>): DispatcherCommand {
		val aliases = ArrayList<String>()
		if (node is AliasableCommandNode<*>) {
			for (alias in (node as AliasableCommandNode<*>).aliases) {
				aliases.add(alias.name)
			}
		}
		return DispatcherCommand(node.name, plugin, dispatcher, node.usageText, aliases)
	}



	private companion object {
		val mapKnownCommandsField: Field = SimpleCommandMap::class.java
			.getDeclaredField("knownCommands").apply {
				isAccessible = true
			}
	}
}