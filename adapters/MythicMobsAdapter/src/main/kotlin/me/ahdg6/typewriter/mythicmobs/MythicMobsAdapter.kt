package me.ahdg6.typewriter.mythicmobs

import App
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter

@Adapter("MythicMobs", "For Using MythicMobs", App.VERSION)
object MythicMobsAdapter : TypewriteAdapter() {

	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("MythicMobs")) {
			Typewriter.plugin.logger.warning("MythicMobs plugin not found, try installing it or disabling the MythicMobs adapter")
			return
		}
	}

}