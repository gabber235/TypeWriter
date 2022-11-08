package me.gabber235.typewriter

import com.comphenix.protocol.ProtocolLibrary
import lirand.api.architecture.KotlinPlugin
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.dialogue.MessengerFinder
import me.gabber235.typewriter.extensions.npc.NpcHandler
import me.gabber235.typewriter.extensions.placeholderapi.TypewriteExpansion
import me.gabber235.typewriter.extensions.superiorisland.SuperiorIslandExtension
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.interaction.ChatHistoryHandler
import me.gabber235.typewriter.interaction.InteractionHandler
import net.kyori.adventure.platform.bukkit.BukkitAudiences

class Typewriter : KotlinPlugin() {
	companion object {
		lateinit var plugin: Typewriter
			private set
		lateinit var adventure: BukkitAudiences
			private set
	}

	init {
		plugin = this
	}

	override fun onLoad() {
		super.onLoad()
		AdapterLoader.loadAdapters()
	}

	override fun onEnable() {
		adventure = BukkitAudiences.create(this)
		typeWriterCommand()

		if (!server.pluginManager.isPluginEnabled("ProtocolLib")) {
			logger.warning("ProtocolLib is not enabled, Typewriter will not work without it. Shutting down...")
			isEnabled = false
			return
		}

		EntryDatabase.loadEntries()
		InteractionHandler.init()
		FactDatabase.init()
		MessengerFinder.init()


		ProtocolLibrary.getProtocolManager()
			.addPacketListener(ChatHistoryHandler)

		if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
			TypewriteExpansion.register()
		}

		if (server.pluginManager.isPluginEnabled("Citizens")) {
			NpcHandler.init()
		}

		if (server.pluginManager.isPluginEnabled("SuperiorSkyblock2")) {
			SuperiorIslandExtension.init()
		}
	}

	val isFloodgateInstalled: Boolean by lazy {
		plugin.server.pluginManager.isPluginEnabled("Floodgate")
	}

	override fun onDisable() {
		InteractionHandler.shutdown()
		FactDatabase.shutdown()
		adventure.close()
	}
}