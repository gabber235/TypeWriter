package com.typewritermc.core

import com.typewritermc.core.entries.Library
import com.typewritermc.core.extension.DependencyInject
import com.typewritermc.core.extension.InitializableManager
import com.typewritermc.core.utils.Reloadable
import com.typewritermc.loader.ExtensionLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

class TypewriterCore : KoinComponent, Reloadable {
    private val directory by inject<File>(named("baseDir"))
    private val extensionLoader by inject<ExtensionLoader>()
    private val library by inject<Library>()
    private val dependencyInject by inject<DependencyInject>()
    private val initializableManager by inject<InitializableManager>()

    override suspend fun load() {
        val extensionsDirectory = directory.resolve("extensions")
        if (!extensionsDirectory.exists()) {
            extensionsDirectory.mkdirs()
        }
        val extensionJars = extensionsDirectory.listFiles()?.filter { it.name.endsWith(".jar") } ?: emptyList()
        // Needs to be loaded first as it will load the classLoader
        extensionLoader.load(extensionJars)
        library.load()
        dependencyInject.load()
        initializableManager.load()
    }

    override suspend fun unload() {
        initializableManager.unload()
        dependencyInject.unload()
        library.unload()
        // Needs to be last, as it will unload the classLoader
        extensionLoader.unload()
    }

    companion object {
        val module = module {
            singleOf(::ExtensionLoader)
            singleOf(::Library)
            singleOf(::DependencyInject)
            singleOf(::InitializableManager)
        }
    }
}