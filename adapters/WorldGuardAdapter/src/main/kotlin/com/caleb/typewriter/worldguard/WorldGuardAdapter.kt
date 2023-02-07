package com.caleb.typewriter.worldguard

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter


@Adapter("worldguard", "For Using WorldGuard", "0.0.1")
object WorldGuardAdapter : TypewriteAdapter() {

	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("WorldGuard")) {
			Typewriter.plugin.logger.warning("WorldGuard plugin not found, try installing it or disabling the WorldGuard adapter")
			return
		}
	}



}