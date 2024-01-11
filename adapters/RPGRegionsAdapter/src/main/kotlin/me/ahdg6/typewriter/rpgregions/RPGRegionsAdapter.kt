package me.ahdg6.typewriter.rpgregions

import App
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import me.gabber235.typewriter.logger

@Adapter("RPGRegions", "For Using RPGRegions", App.VERSION)
/**
 * The RPGRegions Adapter is an adapter for the RPGRegions plugin. It allows you to use RPGRegions's discovery system in your dialogue.
 *
 * :::danger
 * This adapter is not tested. If you find any issues, please report them!
 * :::
 */
object RPGRegionsAdapter : TypewriteAdapter() {

    override fun initialize() {
        if (!server.pluginManager.isPluginEnabled("RPGRegions")) {
            logger.warning("RPGRegions plugin not found, try installing it or disabling the adapter")
            return
        }
    }

}