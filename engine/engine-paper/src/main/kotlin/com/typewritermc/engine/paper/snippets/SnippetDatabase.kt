package com.typewritermc.engine.paper.snippets

import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.get
import com.typewritermc.engine.paper.utils.reloadable
import org.bukkit.configuration.file.YamlConfiguration
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

interface SnippetDatabase {
    fun get(path: String, default: Any, comment: String = ""): Any
    fun <T : Any> getSnippet(path: String, klass: KClass<T>, default: T, comment: String = ""): T
    fun registerSnippet(path: String, defaultValue: Any, comment: String = "")
}

class SnippetDatabaseImpl : SnippetDatabase, KoinComponent {
    private val file by lazy {
        val file = plugin.dataFolder["snippets.yml"]
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        file
    }

    private val ymlConfiguration by reloadable { YamlConfiguration.loadConfiguration(file) }

    override fun get(path: String, default: Any, comment: String): Any {
        val value = ymlConfiguration.get(path)

        if (value == null) {
            ymlConfiguration.set(path, default)
            if (comment.isNotBlank()) {
                ymlConfiguration.setComments(path, comment.lines())
            }
            ymlConfiguration.save(file)
            return default
        }

        return value
    }

    override fun <T : Any> getSnippet(path: String, klass: KClass<T>, default: T, comment: String): T {
        val value = get(path, default)

        val casted = klass.safeCast(value)

        if (casted == null) {
            ymlConfiguration.set(path, default)
            if (comment.isNotBlank()) {
                ymlConfiguration.setComments(path, comment.lines())
            }
            ymlConfiguration.save(file)
            return default
        }

        return casted
    }

    override fun registerSnippet(path: String, defaultValue: Any, comment: String) {
        get(path, defaultValue, comment)
    }
}