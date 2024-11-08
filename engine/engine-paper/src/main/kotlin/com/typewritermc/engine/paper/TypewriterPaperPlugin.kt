package com.typewritermc.engine.paper

import com.github.retrooper.packetevents.PacketEvents
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.google.gson.Gson
import com.typewritermc.core.TypewriterCore
import com.typewritermc.core.entries.Library
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.dialogue.MessengerFinder
import com.typewritermc.engine.paper.entry.entity.EntityHandler
import com.typewritermc.engine.paper.entry.entries.CustomCommandEntry
import com.typewritermc.engine.paper.entry.entries.createRoadNetworkParser
import com.typewritermc.engine.paper.entry.roadnetwork.RoadNetworkManager
import com.typewritermc.engine.paper.events.TypewriterUnloadEvent
import com.typewritermc.engine.paper.extensions.bstats.BStatsMetrics
import com.typewritermc.engine.paper.extensions.modrinth.Modrinth
import com.typewritermc.engine.paper.extensions.placeholderapi.PlaceholderExpansion
import com.typewritermc.engine.paper.facts.FactDatabase
import com.typewritermc.engine.paper.facts.FactStorage
import com.typewritermc.engine.paper.facts.storage.FileFactStorage
import com.typewritermc.engine.paper.interaction.ActionBarBlockerHandler
import com.typewritermc.engine.paper.interaction.ChatHistoryHandler
import com.typewritermc.engine.paper.interaction.InteractionHandler
import com.typewritermc.engine.paper.interaction.PacketInterceptor
import com.typewritermc.engine.paper.loader.dataSerializers
import com.typewritermc.engine.paper.snippets.SnippetDatabase
import com.typewritermc.engine.paper.snippets.SnippetDatabaseImpl
import com.typewritermc.engine.paper.ui.ClientSynchronizer
import com.typewritermc.engine.paper.ui.CommunicationHandler
import com.typewritermc.engine.paper.ui.PanelHost
import com.typewritermc.engine.paper.ui.Writers
import com.typewritermc.engine.paper.utils.createBukkitDataParser
import com.typewritermc.engine.paper.utils.registerAll
import com.typewritermc.engine.paper.utils.unregisterAll
import com.typewritermc.loader.DependencyChecker
import com.typewritermc.loader.ExtensionLoader
import com.typewritermc.core.serialization.createDataSerializerGson
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import kotlinx.coroutines.delay
import lirand.api.architecture.KotlinPlugin
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
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
import java.io.File
import java.util.logging.Logger
import kotlin.time.Duration.Companion.seconds
import com.typewritermc.core.extension.InitializableManager
import com.typewritermc.engine.paper.loader.PaperDependencyChecker

class TypewriterPaperPlugin : KotlinPlugin(), KoinComponent {
    override fun onLoad() {
        super.onLoad()
        val modules = module {
            single { this@TypewriterPaperPlugin } withOptions
                    {
                        named("plugin")
                        bind<Plugin>()
                        bind<JavaPlugin>()
                        bind<TypewriterPaperPlugin>()
                        createdAtStart()
                    }

            single<Logger> { this@TypewriterPaperPlugin.logger } withOptions {
                named("logger")
                createdAtStart()
            }

            singleOf(::TypewriterCore)
            factory<File>(named("baseDir")) { plugin.dataFolder }
            singleOf(::ExtensionLoader)
            singleOf(::Library)
            singleOf(::InitializableManager)
            single { PaperDependencyChecker() } withOptions {
                named("dependencyChecker")
                bind<DependencyChecker>()
                bind<PaperDependencyChecker>()
                createdAtStart()
            }

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
            dataSerializers()
            factory<Gson>(named("dataSerializer")) { createDataSerializerGson(getAll()) }
            factory<Gson>(named("bukkitDataParser")) { createBukkitDataParser() }
            factory<Gson>(named("roadNetworkParser")) { createRoadNetworkParser() }
        }
        startKoin {
            modules(modules)
            logger(MinecraftLogger(logger))
        }

        CommandAPI.onLoad(CommandAPIBukkitConfig(this).usePluginNamespace().skipReloadDatapacks(true))
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

        get<FactDatabase>().initialize()
        get<AssetManager>().initialize()
        get<InteractionHandler>().initialize()
        get<ChatHistoryHandler>().initialize()
        get<ActionBarBlockerHandler>().initialize()
        get<PacketInterceptor>().initialize()
        get<AudienceManager>().initialize()
        get<EntityHandler>().initialize()

        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            PlaceholderExpansion.register()
        }

        BStatsMetrics.registerMetrics()

        // We want to initialize all the extensions after all the plugins have been enabled to make sure
        // that all the plugins are loaded.
        launch {
            delay(1)
            load()
            get<CommunicationHandler>().initialize()

            delay(5.seconds)
            Modrinth.initialize()
        }
    }

    fun load() {
        // Needs to be first, as it will load the classLoader
        get<TypewriterCore>().load()

        get<StagingManager>().loadState()
        get<RoadNetworkManager>().load()
        get<InteractionHandler>().load()
        get<EntryListeners>().load()
        get<AudienceManager>().load()
        get<MessengerFinder>().load()
        CustomCommandEntry.registerAll()
    }

    suspend fun unload() {
        TypewriterUnloadEvent().callEvent()
        CustomCommandEntry.unregisterAll()
        get<MessengerFinder>().unload()
        get<AudienceManager>().unload()
        get<EntryListeners>().unload()
        get<InteractionHandler>().unload()
        get<RoadNetworkManager>().unload()
        get<StagingManager>().unload()

        // Needs to be last, as it will unload the classLoader
        get<TypewriterCore>().unload()
    }

    suspend fun reload() {
        unload()
        load()
    }

    val isFloodgateInstalled: Boolean by lazy { server.pluginManager.isPluginEnabled("Floodgate") }

    override suspend fun onDisableAsync() {
        CommandAPI.onDisable()

        get<StagingManager>().shutdown()
        get<FactDatabase>().shutdown()
        get<ChatHistoryHandler>().shutdown()
        get<ActionBarBlockerHandler>().shutdown()
        get<PacketInterceptor>().shutdown()
        get<CommunicationHandler>().shutdown()
        get<InteractionHandler>().shutdown()
        get<AssetManager>().shutdown()
        get<AudienceManager>().shutdown()
        get<EntityHandler>().shutdown()

        unload()
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

val plugin: TypewriterPaperPlugin by lazy { KoinJavaComponent.get(JavaPlugin::class.java) }
