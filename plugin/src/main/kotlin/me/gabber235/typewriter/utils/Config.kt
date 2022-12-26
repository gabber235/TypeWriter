package me.gabber235.typewriter.utils

import me.gabber235.typewriter.Typewriter.Companion.plugin
import kotlin.reflect.*

inline fun <reified T : Any> config(key: String, default: T) = ConfigPropertyDelegate(key, T::class, default)

class ConfigPropertyDelegate<T : Any>(private val key: String, private val klass: KClass<T>, private val default: T) {
	operator fun getValue(thisRef: Any, property: KProperty<*>): T {
		val value = plugin.config.get(key)
		if (value == null) {
			plugin.config.set(key, default)
			plugin.saveConfig()
			return default
		}
		val t = klass.safeCast(value)
		if (t == null) {
			plugin.logger.warning("Invalid value for config key '$key', expected ${klass.simpleName}, got ${value::class.simpleName}")
			return default
		}
		return t
	}
}

