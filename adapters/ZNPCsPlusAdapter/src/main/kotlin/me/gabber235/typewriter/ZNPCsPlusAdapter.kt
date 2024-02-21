package me.gabber235.typewriter

import App
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import me.gabber235.typewriter.adapters.Untested

@Untested
@Deprecated("Use the EntityAdapter instead")
@Adapter("ZNPCsPlus", "For the ZNPCsPlus plugin", App.VERSION)
/**
 * The ZNPCsPlus adapter allows you to create custom interactions with NPCs.
 */
object ZNPCsPlusAdapter : TypewriteAdapter() {
    override fun initialize() {
        if (!plugin.server.pluginManager.isPluginEnabled("ZNPCsPlus")) {
            logger.warning("ZNPCsPlus plugin not found, try installing it or disabling the ZNPCsPlus adapter")
            return
        }
    }
}
