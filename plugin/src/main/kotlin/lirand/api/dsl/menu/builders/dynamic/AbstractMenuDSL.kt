package lirand.api.dsl.menu.builders.dynamic

import lirand.api.dsl.menu.builders.fixed.AbstractStaticMenuDSL
import lirand.api.dsl.menu.exposed.*
import lirand.api.dsl.menu.exposed.dynamic.Slot
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

abstract class AbstractMenuDSL<S : Slot<I>, I : Inventory>(
	plugin: Plugin,
	cancelEvents: Boolean
) : AbstractStaticMenuDSL<S, I>(plugin, cancelEvents), MenuDSL<S, I> {

	protected var dynamicTitle: (PlayerMenuEvent.() -> String?)? = null
	override var title: String? = null


	override fun title(render: PlayerMenuEvent.() -> String?) {
		dynamicTitle = render
	}


	override fun update(player: Player) {
		val view = views[player] ?: return
		val updateEvent = PlayerMenuUpdateEvent(this, player, view.inventory)
		eventHandler.handleUpdate(updateEvent)

		for (index in rangeOfSlots) {
			val slot = getSlotOrBaseSlot(index)
			callSlotUpdateEvent(index, slot, player, view.inventory)
		}
	}

	override fun updateSlot(slot: S, player: Player) {
		if (!hasPlayer(player)) return

		val slotMatches = getSlotMatches(slot)

		val view = views.getValue(player)
		for ((index, slot) in slotMatches) {
			callSlotUpdateEvent(index, slot, player, view.inventory)
		}
	}

	override fun rerenderSlot(slot: S, player: Player) {
		if (!hasPlayer(player)) return

		val slotMatches = getSlotMatches(slot)

		val view = views.getValue(player)
		for ((index, slot) in slotMatches) {
			callSlotRenderEvent(index, slot, player, view.inventory)
		}
	}

	protected fun callSlotRenderEvent(index: Int, slot: S, player: Player, inventory: I) {
		val slotRenderEvent = PlayerMenuSlotRenderEvent(this, index, slot, player, inventory)
		slot.eventHandler.handleRender(slotRenderEvent)
	}

}