package me.ahdg6.typewriter.rpgregions

import App
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import me.gabber235.typewriter.logger

@Adapter("RPGRegions", "For Using RPGRegions", App.VERSION)
object RPGRegionsAdapter : TypewriteAdapter() {

    override fun initialize() {
        if (!server.pluginManager.isPluginEnabled("RPGRegions")) {
            logger.warning("RPGRegions plugin not found, try installing it or disabling the adapter")
            return
        }
    }

}