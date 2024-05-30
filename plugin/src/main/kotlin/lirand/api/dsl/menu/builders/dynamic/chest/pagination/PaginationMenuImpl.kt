package lirand.api.dsl.menu.builders.dynamic.chest.pagination

import lirand.api.dsl.menu.builders.dynamic.SlotDSL
import lirand.api.dsl.menu.builders.dynamic.chest.ChestMenuDSL
import lirand.api.dsl.menu.builders.dynamic.chest.pagination.slot.PaginationSlotDSL
import lirand.api.dsl.menu.builders.dynamic.chest.pagination.slot.PaginationSlotImpl
import lirand.api.dsl.menu.builders.dynamic.chest.slot
import lirand.api.dsl.menu.exposed.PlayerInventoryMenuEvent
import lirand.api.dsl.menu.exposed.PlayerMenuEvent
import lirand.api.dsl.menu.exposed.calculateSlot
import lirand.api.dsl.menu.exposed.dynamic.Slot
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.ItemsAdapter
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.ItemsProvider
import lirand.api.dsl.menu.exposed.MenuPlayerDataMap
import lirand.api.dsl.menu.exposed.MenuTypedDataMap
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.*

internal const val PAGINATION_OPEN_PAGE_KEY = "PAGINATION:open_page"

class PaginationMenuImpl<T>(
	override val menu: ChestMenuDSL,
	override val itemsProvider: ItemsProvider<T>,
	override val previousPageSlot: Slot<Inventory>?,
	override val nextPageSlot: Slot<Inventory>?,
	override val linesRange: IntRange = 1 until menu.lines,
	override val slotsRange: IntRange = 1..9,
	override val autoUpdateSwitchPageSlots: Boolean = true
) : PaginationMenuDSL<T> {
	override val eventHandler = PaginationDSLEventHandler(menu.plugin)

	override var itemsAdapter: ItemsAdapter<T>? = null
		private set

	override val slots = TreeMap<Int, PaginationSlotDSL<T>>()

	internal val itemSlotData = WeakHashMap<T, MenuTypedDataMap>()
	internal val itemPlayerSlotData = WeakHashMap<T, MenuPlayerDataMap>()

	private val currentPlayerPages = WeakHashMap<Player, Int>()
	private val currentPlayerItems = WeakHashMap<Player, List<T>>()

	init {
		(nextPageSlot as SlotDSL<Inventory>?)?.onInteract {
			if (hasNextPage(player))
				nextPage(player, inventory)
		}

		(previousPageSlot as SlotDSL<Inventory>?)?.onInteract {
			if (hasPreviousPage(player))
				previousPage(player, inventory)
		}

		menu.preOpen {
			val items = getPlayerItems(player).toList()
			currentPlayerItems[player] = items

			val openPage = menu.playerData[player][PAGINATION_OPEN_PAGE_KEY] as? Int?
			if (openPage != null) {
				if (isPageAvailable(player, openPage)) {
					currentPlayerPages[player] = openPage
				}
				else {
					isCanceled = true
					eventHandler.handlePageAvailable(this)
				}
			}
		}

		menu.onClose {
			currentPlayerPages.remove(player)
			currentPlayerItems.remove(player)

			for (value in itemPlayerSlotData.values) {
				value.remove(player)
			}
		}

		if (autoUpdateSwitchPageSlots) {
			onPageChange {
				(menu as? ChestMenuDSL)?.let { menu ->
					nextPageSlot?.let { menu.updateSlot(it, player) }
					previousPageSlot?.let { menu.updateSlot(it, player) }
				}
			}
		}

		for (line in linesRange) {
			for (slotPos in slotsRange) {
				val menuSlot = menu.calculateSlot(line, slotPos)
				val slotRoot = menu.slot(menuSlot, null) as SlotDSL<Inventory>

				val paginationSlot = PaginationSlotImpl(
					this,
					slotRoot
				)

				slotRoot.apply {
					fun getCurrentItemForPlayer(player: Player): T? {
						val pagination = this@PaginationMenuImpl

						return pagination.getCurrentItemForPlayer(
							player,
							menuSlot,
							pagination.getPlayerCurrentPage(player)
						)
					}

					onRender {
						paginationSlot.eventHandler.handleRender(
							getCurrentItemForPlayer(player),
							this
						)
					}
					onUpdate {
						paginationSlot.eventHandler.handleUpdate(
							getCurrentItemForPlayer(player),
							this
						)
					}
					onInteract {
						paginationSlot.eventHandler.handleInteract(
							getCurrentItemForPlayer(player),
							this
						)
					}
				}

				slots[menuSlot] = paginationSlot
			}
		}
	}

	override fun adaptOnUpdate(adapter: ItemsAdapter<T>) {
		itemsAdapter = adapter
	}

	override fun hasPreviousPage(player: Player): Boolean {
		return isPageAvailable(player, getPlayerCurrentPage(player) - 1)
	}

	override fun hasNextPage(player: Player): Boolean {
		return isPageAvailable(player, getPlayerCurrentPage(player) + 1)
	}

	override fun getPlayerCurrentPage(player: Player): Int = currentPlayerPages.getOrDefault(player, 1)

	/**
	 * Update current item list calling [itemsAdapter]
	 */
	override fun updateItems(player: Player) {
		val adapter = itemsAdapter ?: return
		val view = menu.views[player] ?: return

		val playerInventoryMenuEvent = PlayerInventoryMenuEvent(menu, player, view.inventory)

		val items = adapter.invoke(playerInventoryMenuEvent, getPlayerItems(player))

		currentPlayerItems[player] = items.toList()

		if (!isPageAvailable(player, getPlayerCurrentPage(player)))
			currentPlayerPages[player] = getMaxPages(player)

		val currentPage = getPlayerCurrentPage(player)

		forEachSlot { slotPos, pageSlot ->
			val item: T? = getCurrentItemForPlayer(player, slotPos, currentPage)

			pageSlot.updateSlot(item, item, slotPos, player, view.inventory)
		}

		eventHandler.handlePageChange(playerInventoryMenuEvent)
	}

	private fun getCurrentItemForPlayer(player: Player, slotPos: Int, page: Int): T? {
		val items = getPlayerItems(player)

		val startLineNotUsage = linesRange.first - 1
		val lineEndSlotNotUsage = 9 - slotsRange.last
		val startSlotNotUsage = slotsRange.first - 1

		val startLineSlotsUsage = startLineNotUsage * 9
		val currentLine = (slotPos / 9) + 1
		val currentLineUsage = currentLine - startLineNotUsage

		val currentUsageStartSlots = startSlotNotUsage * currentLineUsage
		val currentEndSlotUsage = if (currentLineUsage > 1) lineEndSlotNotUsage * (currentLineUsage - 1) else 0

		val slotItemIndex =
			getPageStartIndex(page) + (slotPos - startLineSlotsUsage - currentUsageStartSlots - currentEndSlotUsage)

		return items.elementAtOrNull(slotItemIndex)
	}

	private fun nextPage(player: Player, inventory: Inventory) {
		val nextPage = getPlayerCurrentPage(player) + 1

		changePage(player, inventory, nextPage)
	}

	private fun previousPage(player: Player, inventory: Inventory) {
		val previousPage = getPlayerCurrentPage(player) - 1

		changePage(player, inventory, previousPage)
	}

	private fun changePage(player: Player, inventory: Inventory, nextPage: Int) {
		val currentPage = getPlayerCurrentPage(player)

		currentPlayerPages[player] = nextPage

		forEachSlot { slotPos, pageSlot ->
			val actualItem: T? = getCurrentItemForPlayer(player, slotPos, currentPage)

			val nextItem: T? = getCurrentItemForPlayer(player, slotPos, nextPage)

			pageSlot.updateSlot(actualItem, nextItem, slotPos, player, inventory, true)

			eventHandler.handlePageChange(PlayerInventoryMenuEvent(menu, player, inventory))
		}
	}

	private inline fun forEachSlot(
		block: (Int, PaginationSlotImpl<T>) -> Unit
	) {
		for (line in linesRange) {
			for (slot in slotsRange) {
				val slotPos = menu.calculateSlot(line, slot)
				val pageSlot = slots[slotPos] as? PaginationSlotImpl<T>
					?: continue

				block(slotPos, pageSlot)
			}
		}
	}

	private fun getPageStartIndex(page: Int) = ((page - 1) * getMaxSlotPerPage())

	private fun getPlayerItems(player: Player) =
		currentPlayerItems[player] ?: itemsProvider(PlayerMenuEvent(menu, player))

	private fun getMaxSlotPerPage() =
		(linesRange.last - linesRange.first + 1) * (slotsRange.last - slotsRange.first + 1)

	private fun getMaxPages(player: Player): Int {
		val itemsCount = getPlayerItems(player).size

		val maxPerPage = getMaxSlotPerPage()

		var pages = (itemsCount / maxPerPage)
		val mod = itemsCount % maxPerPage

		if (mod > 0) pages++

		return pages
	}

	private fun isPageAvailable(player: Player, page: Int): Boolean {
		val maxPages = getMaxPages(player)

		return page in 1..maxPages
	}
}