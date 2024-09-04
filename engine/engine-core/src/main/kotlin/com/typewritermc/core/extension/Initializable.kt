package com.typewritermc.core.extension

import com.typewritermc.core.utils.Reloadable
import com.typewritermc.loader.ExtensionLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.logging.Logger

interface Initializable {
    fun initialize()
    fun shutdown()
}

class InitializableManager : KoinComponent, Reloadable {
    private val logger: Logger by inject()
    private val extensionLoader: ExtensionLoader by inject()

    private var initializables: List<Initializable> = emptyList()

    override fun load() {
        initializables = extensionLoader.extensions.flatMap { it.initializers }
            .map { extensionLoader.loadClass(it.className).kotlin }
            .mapNotNull {
                val obj = it.objectInstance
                if (obj == null) {
                    logger.warning("Initializer ${it.simpleName} is not an object. Make sure that it is a object class.")
                    return@mapNotNull null
                }
                val initializer = obj as? Initializable
                if (initializer == null) {
                    logger.warning("Initializer ${it.simpleName} is not an Initializable. Make sure that it inherits from Initializable.")
                    return@mapNotNull null
                }
                initializer
            }

        initializables.forEach {
            try {
                it.initialize()
            } catch (e: Exception) {
                logger.severe("Failed to initialize ${it.javaClass.simpleName}: ${e.message}")
            }
        }
    }

    override fun unload() {
        initializables.forEach {
            try {
                it.shutdown()
            } catch (e: Exception) {
                logger.severe("Failed to shutdown ${it.javaClass.simpleName}: ${e.message}")
            }
        }
        initializables = emptyList()
    }
}