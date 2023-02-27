package com.caleb.typewriter.worldguard

import com.sk89q.worldguard.WorldGuard
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter


@Adapter("WorldGuard", "For Using WorldGuard", "0.1.0")
object WorldGuardAdapter : TypewriteAdapter() {

	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("WorldGuard")) {
			Typewriter.plugin.logger.warning("WorldGuard plugin not found, try installing it or disabling the WorldGuard adapter")
			return
		}

		val worldGuard = WorldGuard.getInstance()

		val registered = worldGuard.platform.sessionManager.registerHandler(WorldGuardHandler.Factory(), null)

		if (!registered) {
			Typewriter.plugin.logger.warning("Failed to register WorldGuardHandler. This is a bug, please report it on the Typewriter Discord.")
		}
	}
}