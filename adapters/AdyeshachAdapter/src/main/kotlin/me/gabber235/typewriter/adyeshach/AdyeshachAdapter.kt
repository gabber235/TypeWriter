package me.gabber235.typewriter.adyeshach

import App
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter

@Adapter("Adyeshach", "For the Adyeshach plugin", App.VERSION)
object  AdyeshachAdapter : TypewriteAdapter() {
	override fun initialize() {
		if (!plugin.server.pluginManager.isPluginEnabled("Adyeshach")) {
			plugin.logger.warning("Adyeshach plugin not found, try installing it or disabling the Adyeshach adapter")
			return
		}
	}

	override fun shutdown() {
	}
}