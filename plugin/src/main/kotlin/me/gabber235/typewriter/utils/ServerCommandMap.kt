package me.gabber235.typewriter.utils

import me.gabber235.typewriter.entry.entries.CustomCommandEntry
import me.gabber235.typewriter.entry.triggerAllFor
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.lang.reflect.Field

val commandMap: CommandMap by lazy {
	val server = Bukkit.getServer()
	val commandMapField: Field = server.javaClass.getDeclaredField("commandMap")
	commandMapField.isAccessible = true
	commandMapField.get(server) as CommandMap
}

fun CustomCommandEntry.register() {
	val result = commandMap.register(command, object : BukkitCommand(command) {
		override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
			println("Triggered command $command for $name (${id})")
			triggerAllFor(sender as Player)
			return true
		}
	})

	println("Registered command $command for $name (${id}) Success: $result")
}
