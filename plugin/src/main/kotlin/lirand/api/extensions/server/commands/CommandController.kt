package lirand.api.extensions.server.commands

import lirand.api.LirandAPI
import lirand.api.extensions.server.registerEvents
import org.bukkit.command.Command
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal fun provideCommandController(plugin: Plugin) = LirandAPI.instances[plugin]?.commandController


internal class CommandController(val plugin: Plugin) : Listener {

	val commands = mutableListOf<Command>()


	fun initialize() {
		plugin.registerEvents(this)
	}


	@EventHandler
	fun onPluginDisableEvent(event: PluginDisableEvent) {
		if (event.plugin != plugin) return

		commands.forEach {
			it.unregister(plugin)
		}
	}
}