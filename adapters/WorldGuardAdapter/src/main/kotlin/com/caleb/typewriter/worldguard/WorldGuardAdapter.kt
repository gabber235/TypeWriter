package com.caleb.typewriter.worldguard

import App
import com.sk89q.worldguard.WorldGuard
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriterAdapter
import me.gabber235.typewriter.logger

@Adapter("WorldGuard", "For Using WorldGuard", App.VERSION)
/**
 * The WorldGuard Adapter allows you to create dialogue that is triggered by WorldGuard regions.
 */
object WorldGuardAdapter : TypewriterAdapter() {

    override fun initialize() {
        if (!server.pluginManager.isPluginEnabled("WorldGuard")) {
            logger.warning("WorldGuard plugin not found, try installing it or disabling the WorldGuard adapter")
            return
        }

        val worldGuard = WorldGuard.getInstance()

        val registered = worldGuard.platform.sessionManager.registerHandler(WorldGuardHandler.Factory(), null)

        if (!registered) {
            logger.warning("Failed to register WorldGuardHandler. This is a bug, please report it on the Typewriter Discord.")
        }
    }
}