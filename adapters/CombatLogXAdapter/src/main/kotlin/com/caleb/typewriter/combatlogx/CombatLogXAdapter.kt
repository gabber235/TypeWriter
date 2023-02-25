package com.caleb.typewriter.combatlogx

import com.github.sirblobman.combatlogx.api.ICombatLogX
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import org.bukkit.Bukkit


@Adapter("combatlogx", "For Using CombatLogX", "0.0.1")
object CombatLogXAdapter : TypewriteAdapter() {

	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("CombatLogX")) {
			Typewriter.plugin.logger.warning("CombatLogX plugin not found, try installing it or disabling the CombatLogX adapter")
			return
		}
	}

	fun getAPI(): ICombatLogX? {
		val pluginManager = Bukkit.getPluginManager()
		val plugin = pluginManager.getPlugin("CombatLogX")
		return plugin as ICombatLogX?
	}



}