package lirand.api.dsl.menu.exposed

import java.util.*

class MenuTypedDataMap(initialCapacity: Int, loadFactor: Float) : WeakHashMap<String, Any?>(initialCapacity, loadFactor) {

	constructor() : this(16, 0.75F)
	constructor(map: Map<String, Any?>) : this(((map.size / 0.75f).toInt() + 1).coerceAtLeast(16), 0.75f) {
		putAll(map)
	}

	fun <T> getTyped(key: String): T {
		return get(key) as T
	}

}