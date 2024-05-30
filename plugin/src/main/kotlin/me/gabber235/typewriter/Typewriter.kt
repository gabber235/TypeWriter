package me.gabber235.typewriter

import com.github.retrooper.packetevents.PacketEvents
import com.github.shynixn.mccoroutine.bukkit.launch
import com.google.gson.Gson
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import kotlinx.coroutines.delay
import lirand.api.architecture.KotlinPlugin
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.adapters.AdapterLoaderImpl
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.dialogue.MessengerFinder
import me.gabber235.typewriter.entry.entity.EntityHandler
import me.gabber235.typewriter.entry.entries.createRoadNetworkParser
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkManager
import me.gabber235.typewriter.extensions.bstats.BStatsMetrics
import me.gabber235.typewriter.extensions.modrinth.Modrinth
import me.gabber235.typewriter.extensions.placeholderapi.PlaceholderExpansion
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.facts.FactStorage
import me.gabber235.typewriter.facts.storage.FileFactStorage
import me.gabber235.typewriter.interaction.ActionBarBlockerHandler
import me.gabber235.typewriter.interaction.ChatHistoryHandler
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.interaction.PacketInterceptor
import me.gabber235.typewriter.snippets.SnippetDatabase
import me.gabber235.typewriter.snippets.SnippetDatabaseImpl
import me.gabber235.typewriter.ui.ClientSynchronizer
import me.gabber235.typewriter.ui.CommunicationHandler
import me.gabber235.typewriter.ui.PanelHost
import me.gabber235.typewriter.ui.Writers
import me.gabber235.typewriter.utils.createBukkitDataParser
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
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
import org.patheloper.mapping.PatheticMapper
import java.util.logging.Logger
import kotlin.time.Duration.Companion.seconds

class Typewriter : KotlinPlugin(), KoinComponent {

    override fun onLoad() {
        super.onLoad()
        val modules = module {
            single { this@Typewriter } withOptions
                    {
                        named("plugin")
                        bind<Plugin>()
                        bind<JavaPlugin>()
                        createdAtStart()
                    }

            singleOf<AdapterLoader>(::AdapterLoaderImpl)
            singleOf<EntryDatabase>(::EntryDatabaseImpl)
            singleOf<StagingManager>(::StagingManagerImpl)
            singleOf(::ClientSynchronizer)
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
            singleOf<AssetStorage>(::LocalAssetStorage)
            singleOf<AssetManager>(::AssetManager)
            singleOf(::ChatHistoryHandler)
            singleOf(::ActionBarBlockerHandler)
            singleOf(::PacketInterceptor)
            singleOf(::EntityHandler)
            singleOf(::RoadNetworkManager)
            factory<Gson>(named("entryParser")) { createEntryParserGson(get()) }
            factory<Gson>(named("bukkitDataParser")) { createBukkitDataParser() }
            factory<Gson>(named("roadNetworkParser")) { createRoadNetworkParser() }
        }
        startKoin {
            modules(modules)
            logger(MinecraftLogger(logger))
        }

        CommandAPI.onLoad(CommandAPIBukkitConfig(this).usePluginNamespace())

        get<AdapterLoader>().loadAdapters()
    }

    override suspend fun onEnableAsync() {
        CommandAPI.onEnable()
        typeWriterCommand()

        if (!server.pluginManager.isPluginEnabled("packetevents")) {
            logger.warning("PacketEvents is not enabled, Typewriter will not work without it. Shutting down...")
            server.pluginManager.disablePlugin(this)
            return
        }

        PacketEvents.getAPI().settings.downsampleColors(false)


        get<EntryDatabase>().initialize()
        get<StagingManager>().initialize()
        get<InteractionHandler>().initialize()
        get<FactDatabase>().initialize()
        get<MessengerFinder>().initialize()
        get<ChatHistoryHandler>().initialize()
        get<ActionBarBlockerHandler>().initialize()
        get<PacketInterceptor>().initialize()
        get<AssetManager>().initialize()
        get<EntityHandler>().initialize()
        get<AudienceManager>().initialize()
        get<RoadNetworkManager>().initialize()

        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            PlaceholderExpansion.register()
        }

        BStatsMetrics.registerMetrics()
        PatheticMapper.initialize(this)

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
        CommandAPI.onDisable()
        PatheticMapper.shutdown()
        get<AdapterLoader>().shutdown()
        get<StagingManager>().shutdown()
        get<EntityHandler>().shutdown()
        get<RoadNetworkManager>().shutdown()
        get<ChatHistoryHandler>().shutdown()
        get<ActionBarBlockerHandler>().shutdown()
        get<PacketInterceptor>().shutdown()
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

val plugin: JavaPlugin by lazy { KoinJavaComponent.get(JavaPlugin::class.java) }
