package com.typewritermc.core

import com.typewritermc.core.entries.Library
import com.typewritermc.core.extension.InitializableManager
import com.typewritermc.core.utils.Reloadable
import com.typewritermc.loader.ExtensionLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class TypewriterCore : KoinComponent, Reloadable {
    private val directory by inject<File>(named("baseDir"))
    private val extensionLoader by inject<ExtensionLoader>()
    private val library by inject<Library>()
    private val initializableManager by inject<InitializableManager>()

    override fun load() {
        val extensionsDirectory = directory.resolve("extensions")
        if (!extensionsDirectory.exists()) {
            extensionsDirectory.mkdirs()
        }
        val extensionJars = extensionsDirectory.listFiles()?.filter { it.name.endsWith(".jar") } ?: emptyList()
        // Needs to be loaded first as it will load the classLoader
        extensionLoader.load(extensionJars)
        library.load()
        initializableManager.load()
    }

    override fun unload() {
        initializableManager.unload()
        library.unload()
        // Needs to be last, as it will unload the classLoader
        extensionLoader.unload()
    }
}