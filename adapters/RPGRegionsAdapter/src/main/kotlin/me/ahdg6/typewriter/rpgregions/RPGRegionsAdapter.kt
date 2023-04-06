package me.ahdg6.typewriter.rpgregions

import App
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter

@Adapter("RPGRegions", "For Using RPGRegions", App.VERSION)
object RPGRegionsAdapter : TypewriteAdapter() {

	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("RPGRegions")) {
			Typewriter.plugin.logger.warning("RPGRegions plugin not found, try installing it or disabling the adapter")
			return
		}
	}

}