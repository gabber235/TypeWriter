package com.caleb.typewriter.superiorskyblock

import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter


@Adapter("SuperiorSkyblock", "For SuperiorSkyblock2, made by Caleb (Sniper)", "0.2.0")
object SuperiorSkyblockAdapter : TypewriteAdapter() {


	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("SuperiorSkyblock2")) {
			plugin.logger.warning("SuperiorSkyblock2 plugin not found, try installing it or disabling the SuperiorSkyblock2 adapter")
		}

	}


}