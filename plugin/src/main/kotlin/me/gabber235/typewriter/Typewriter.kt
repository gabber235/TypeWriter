package me.gabber235.typewriter

import com.github.retrooper.packetevents.PacketEvents
import com.github.shynixn.mccoroutine.bukkit.launch
import com.google.gson.Gson
import kotlinx.coroutines.delay
import lirand.api.architecture.KotlinPlugin
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.adapters.AdapterLoaderImpl
import me.gabber235.typewriter.capture.Recorders
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.dialogue.MessengerFinder
import me.gabber235.typewriter.extensions.bstats.BStatsMetrics
import me.gabber235.typewriter.extensions.modrinth.Modrinth
import me.gabber235.typewriter.extensions.placeholderapi.PlaceholderExpansion
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.facts.FactStorage
import me.gabber235.typewriter.facts.storage.FileFactStorage
import me.gabber235.typewriter.interaction.ActionBarBlockerHandler
import me.gabber235.typewriter.interaction.ChatHistoryHandler
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.interaction.PacketBlocker
import me.gabber235.typewriter.snippets.SnippetDatabase
import me.gabber235.typewriter.snippets.SnippetDatabaseImpl
import me.gabber235.typewriter.ui.*
import me.gabber235.typewriter.utils.createBukkitDataParser
import me.gabber235.typewriter.utils.syncCommands
import me.tofaa.entitylib.APIConfig
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger
import kotlin.time.Duration.Companion.seconds

class Typewriter : KotlinPlugin(), KoinComponent {

    private val entityId = AtomicInteger(100000)

    override fun onLoad() {
        super.onLoad()
        val modules = module {
            single { this@Typewriter } withOptions
                    {
                        named("plugin")
                        bind<Plugin>()
                        createdAtStart()
                    }

            singleOf<AdapterLoader>(::AdapterLoaderImpl)
            singleOf<EntryDatabase>(::EntryDatabaseImpl)
            singleOf<StagingManager>(::StagingManagerImpl)
            singleOf<ClientSynchronizer>(::ClientSynchronizerImpl)
            singleOf(::InteractionHandler)
            singleOf(::MessengerFinder)
            singleOf(::CommunicationHandler)
            singleOf(::Writers)
            singleOf(::PanelHost)
            singleOf<SnippetDatabase>(::SnippetDatabaseImpl)
            singleOf(::FactDatabase)
            singleOf<FactStorage>(::FileFactStorage)
            singleOf(::EntryListeners)
            singleOf(::AudienceManager)
            singleOf(::Recorders)
            singleOf<AssetStorage>(::LocalAssetStorage)
            singleOf(::AssetManager)
            singleOf(::ChatHistoryHandler)
            singleOf(::ActionBarBlockerHandler)
            singleOf(::PacketBlocker)
            factory<Gson>(named("entryParser")) { createEntryParserGson(get()) }
            factory<Gson>(named("bukkitDataParser")) { createBukkitDataParser() }
        }
        startKoin {
            modules(modules)
            logger(MinecraftLogger(logger))
        }

        get<AdapterLoader>().loadAdapters()
    }

    override suspend fun onEnableAsync() {
        typeWriterCommand()

        if (!server.pluginManager.isPluginEnabled("packetevents")) {
            logger.warning(
                "PacketEvents is not enabled, Typewriter will not work without it. Shutting down..."
            )
            server.pluginManager.disablePlugin(this)
            return
        }

        val platform = SpigotEntityLibPlatform(this)
        val settings = APIConfig(PacketEvents.getAPI())
            .debugMode()
            .useAsyncEvents()
            .usePlatformLogger()
            .tickTickables()

        EntityLib.init(platform, settings)

        get<EntryDatabase>().initialize()
        get<StagingManager>().initialize()
        get<InteractionHandler>().initialize()
        get<FactDatabase>().initialize()
        get<MessengerFinder>().initialize()
        get<ChatHistoryHandler>().initialize()
        get<ActionBarBlockerHandler>().initialize()
        get<PacketBlocker>().initialize()
        get<AssetManager>().initialize()
        get<AudienceManager>().initialize()

        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            PlaceholderExpansion.register()
        }

        syncCommands()
        BStatsMetrics.registerMetrics()

        // We want to initialize all the adapters after all the plugins have been enabled to make
        // sure
        // that all the plugins are loaded.
        launch {
            delay(1)
            get<AdapterLoader>().initializeAdapters()
            get<EntryDatabase>().loadEntries()
            get<CommunicationHandler>().initialize()

            delay(5.seconds)
            Modrinth.initialize()
        }
    }

    val isFloodgateInstalled: Boolean by lazy { server.pluginManager.isPluginEnabled("Floodgate") }

    override suspend fun onDisableAsync() {
        get<StagingManager>().shutdown()
        get<ChatHistoryHandler>().shutdown()
        get<ActionBarBlockerHandler>().shutdown()
        get<PacketBlocker>().shutdown()
        get<CommunicationHandler>().shutdown()
        get<InteractionHandler>().shutdown()
        get<EntryListeners>().unregister()
        get<FactDatabase>().shutdown()
        get<AssetManager>().shutdown()
        get<AudienceManager>().shutdown()
    }
}

private class MinecraftLogger(private val logger: Logger) :
    org.koin.core.logger.Logger(logger.level.convertLogger()) {
    override fun display(level: Level, msg: MESSAGE) {
        when (level) {
            Level.DEBUG -> logger.fine(msg)
            Level.INFO -> logger.info(msg)
            Level.ERROR -> logger.severe(msg)
            Level.NONE -> logger.finest(msg)
            Level.WARNING -> logger.warning(msg)
        }
    }
}

fun java.util.logging.Level?.convertLogger(): Level {
    return when (this) {
        java.util.logging.Level.FINEST -> Level.DEBUG
        java.util.logging.Level.FINER -> Level.DEBUG
        java.util.logging.Level.FINE -> Level.DEBUG
        java.util.logging.Level.CONFIG -> Level.DEBUG
        java.util.logging.Level.INFO -> Level.INFO
        java.util.logging.Level.WARNING -> Level.WARNING
        java.util.logging.Level.SEVERE -> Level.ERROR
        else -> Level.INFO
    }
}

val logger: Logger by lazy { plugin.logger }

val plugin: Plugin by lazy { KoinJavaComponent.get(Plugin::class.java) }
