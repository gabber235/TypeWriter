package me.gabber235.typewrite.superiorskyblock

import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter

@Adapter("superior_skyblock", "For the SuperiorSkyblock plugin", "0.1.0")
object SuperiorSkyblockAdapter : TypewriteAdapter() {
	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("SuperiorSkyblock2")) {
			plugin.logger.warning("SuperiorSkyblock2 plugin not found, try installing it or disabling the SuperiorSkyblock adapter")
		}
	}
}