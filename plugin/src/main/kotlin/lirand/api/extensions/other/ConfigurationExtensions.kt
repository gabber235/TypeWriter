package lirand.api.extensions.other

import org.bukkit.configuration.ConfigurationSection

fun ConfigurationSection.putAll(map: Map<String, Any?>) {
	for ((key, value) in map) {
		if (value is Map<*, *>) {
			set(key, null)
			(getConfigurationSection(key) ?: createSection(key)).putAll(value as Map<String, Any?>)
		}
		else {
			set(key, value)
		}
	}
}

/**
 * @returns the count of absent values that was set, if returns 0, no values was set to the Configuration
 */
fun ConfigurationSection.putAllIfAbsent(map: Map<String, Any?>): Int {
	var missing = 0
	for ((key, value) in map) {
		if (value is Map<*, *>) {
			missing += (getConfigurationSection(key) ?: createSection(key)).putAllIfAbsent(value as Map<String, Any?>)
		}
		else if (!contains(key)) {
			missing++
		}
	}
	return missing
}

fun ConfigurationSection.toMap(): Map<String, Any?> {
	return getValues(false).onEach { (key, value) ->
		if (value is ConfigurationSection) {
			set(key, value.toMap())
		}
	}
}