package lirand.api.dsl.menu.exposed

import org.bukkit.entity.Player
import java.util.WeakHashMap

class MenuPlayerDataMap(initialCapacity: Int, loadFactor: Float) : WeakHashMap<Player, MenuTypedDataMap>(initialCapacity, loadFactor) {
	constructor() : this(16, 0.75F)
	constructor(map: Map<Player, MenuTypedDataMap>) : this(((map.size / 0.75f).toInt() + 1).coerceAtLeast(16), 0.75f) {
		putAll(map)
	}

	override fun get(key: Player): MenuTypedDataMap {
		return super.get(key) ?: MenuTypedDataMap().also {
			put(key, it)
		}
	}
}