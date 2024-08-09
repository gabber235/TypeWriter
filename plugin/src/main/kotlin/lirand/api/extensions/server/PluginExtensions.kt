package lirand.api.extensions.server

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import java.io.File
import java.net.URL
import java.net.URLDecoder
import java.util.jar.JarFile

fun Plugin.registerEvents(
	vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun Plugin.registerSuspendingEvents(
	vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerSuspendingEvents(it, this) }


fun Plugin.getKey(name: String) = NamespacedKey(this, name)