package me.gabber235.typewriter

import com.comphenix.protocol.ProtocolLibrary
import lirand.api.architecture.KotlinPlugin
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.interaction.ChatHistoryHandler
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.npc.NpcHandler
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


		ProtocolLibrary.getProtocolManager()
			.addPacketListener(ChatHistoryHandler)

		if (server.pluginManager.isPluginEnabled("Citizens")) {
			NpcHandler.init()
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