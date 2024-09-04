package lirand.api

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

    init {
        _instances[plugin] = this
    }
}