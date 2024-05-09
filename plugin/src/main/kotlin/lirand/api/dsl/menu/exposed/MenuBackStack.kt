package lirand.api.dsl.menu.exposed

import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import org.bukkit.entity.Player
import java.util.*

class MenuBackStack(val player: Player) : ArrayDeque<MenuBackStack.Frame>() {
	inner class Frame(
		val menu: StaticMenu<*, *>,
		val playerData: MenuTypedDataMap = MenuTypedDataMap(menu.playerData[player]),
		key: String? = null
	) {
		internal var _key: String? = key
		val key: String? get() = _key
	}

	private var nextKey: String? = null
	private var _nextPushToStack: Boolean = true
	val nextPushToStack: Boolean get() = _nextPushToStack


	fun next(
		key: String? = null,
		pushToStack: Boolean? = null
	): MenuBackStack {
		key?.let { nextKey = it }
		pushToStack?.let { _nextPushToStack = it }
		return this
	}


	override fun addFirst(e: Frame) {
		if (nextKey != null) {
			e._key = nextKey
			nextKey = null
		}

		super.addFirst(e)
	}
}