package lirand.api

import lirand.api.extensions.server.commands.CommandController
import org.bukkit.plugin.Plugin

class LirandAPI internal constructor(internal val plugin: Plugin) {

    companion object {
        private val _instances = mutableMapOf<Plugin, LirandAPI>()
        val instances: Map<Plugin, LirandAPI> get() = _instances

        fun register(plugin: Plugin): LirandAPI {
            check(plugin !in instances) { "Api for this plugin already initialized." }

            return LirandAPI(plugin)
        }
    }

    internal val commandController = CommandController(plugin)

    init {
        _instances[plugin] = this

        commandController.initialize()
    }
}