package me.gabber235.typewriter

import App
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import me.gabber235.typewriter.adapters.Untested

@Untested
@Deprecated("Use the EntityAdapter instead")
@Adapter("FancyNpcs", "For the FancyNpcs plugin", App.VERSION)
/**
 * The FancyNpcs adapter allows you to create custom interactions with NPCs.
 */
object FancyNpcsAdapter : TypewriteAdapter() {
    override fun initialize() {
        if (!plugin.server.pluginManager.isPluginEnabled("FancyNpcs")) {
            logger.warning("FancyNpcs plugin not found, try installing it or disabling the FancyNpcs adapter")
            return
        }
    }
}
