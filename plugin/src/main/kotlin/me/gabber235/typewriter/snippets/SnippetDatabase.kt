package me.gabber235.typewriter.snippets

import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.utils.get
import me.gabber235.typewriter.utils.reloadable
import org.bukkit.configuration.file.YamlConfiguration
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

object SnippetDatabase {
	private val file by lazy {
		val file = plugin.dataFolder["snippets.yml"]
		if (!file.exists()) {
			file.parentFile.mkdirs()
			file.createNewFile()
		}
		file
	}

	private val ymlConfiguration by reloadable { YamlConfiguration.loadConfiguration(file) }

	fun get(path: String, default: Any): Any {
		val value = ymlConfiguration.get(path)

		if (value == null) {
			ymlConfiguration.set(path, default)
			ymlConfiguration.save(file)
			return default
		}

		return value
	}

	fun <T : Any> getSnippet(path: String, klass: KClass<T>, default: T): T {
		val value = get(path, default)

		val casted = klass.safeCast(value)

		if (casted == null) {
			ymlConfiguration.set(path, default)
			ymlConfiguration.save(file)
			return default
		}

		return casted
	}

	fun registerSnippet(path: String, defaultValue: Any) {
		get(path, defaultValue)
	}
}