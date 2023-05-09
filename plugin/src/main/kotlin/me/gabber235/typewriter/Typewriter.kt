package me.gabber235.typewriter

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.delay
import lirand.api.architecture.KotlinPlugin
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.EntryListeners
import me.gabber235.typewriter.entry.dialogue.MessengerFinder
import me.gabber235.typewriter.extensions.placeholderapi.TypewriteExpansion
import me.gabber235.typewriter.extensions.protocollib.entityTestCommand
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.interaction.ChatHistoryHandler
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.ui.CommunicationHandler

class Typewriter : KotlinPlugin() {
    companion object {
        lateinit var plugin: Typewriter
            private set
    }

    init {
        plugin = this
    }

    override fun onLoad() {
        super.onLoad()
        AdapterLoader.loadAdapters()
    }

    override suspend fun onEnableAsync() {
        typeWriterCommand()

        if (!server.pluginManager.isPluginEnabled("ProtocolLib")) {
            logger.warning("ProtocolLib is not enabled, Typewriter will not work without it. Shutting down...")
            server.pluginManager.disablePlugin(this)
            return
        }

        EntryDatabase.init()
        InteractionHandler.init()
        FactDatabase.init()
        MessengerFinder.init()
        ChatHistoryHandler.init()

        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            TypewriteExpansion.register()
        }

        // We want to initialize all the adapters after all the plugins have been enabled to make sure
        // that all the plugins are loaded.
        plugin.launch {
            delay(1)
            AdapterLoader.initializeAdapters()
            CommunicationHandler.initialize()
        }

        entityTestCommand()
    }

    val isFloodgateInstalled: Boolean by lazy {
        plugin.server.pluginManager.isPluginEnabled("Floodgate")
    }

    override suspend fun onDisableAsync() {
        ChatHistoryHandler.shutdown()
        CommunicationHandler.shutdown()
        InteractionHandler.shutdown()
        EntryListeners.unregister()
        FactDatabase.shutdown()
    }
}