package lirand.api.extensions.server

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(
	vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun Plugin.registerSuspendingEvents(
	vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerSuspendingEvents(it, this) }