package lirand.api.dsl.menu

import lirand.api.dsl.menu.builders.fixed.StaticSlotDSL
import lirand.api.dsl.menu.exposed.*
import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import lirand.api.extensions.inventory.get
import lirand.api.extensions.inventory.isNotEmpty
import lirand.api.extensions.server.registerEvents
import lirand.api.extensions.server.server
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

internal class MenuController(val plugin: Plugin) : Listener {

    private val supportedInventoryTypes = listOf(
        InventoryType.CHEST, InventoryType.ANVIL
    )


    fun initialize() {
        plugin.registerEvents(this)
    }


    @EventHandler
    fun onPluginDisableEvent(event: PluginDisableEvent) {
        for (player in server.onlinePlayers) {
            val holder = player.openInventory.topInventory.holder

            if (holder is StaticMenu<*, *>) {
                holder.close(player, true)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onClickEvent(event: InventoryClickEvent) {
        if (event.view.type !in supportedInventoryTypes)
            return

        val player = event.whoClicked as Player
        val inventory = event.inventory

        val menu = inventory.asMenu()?.takeIfHasPlayer(player) ?: return

        menu as StaticMenu<*, Inventory>

        handleMenuClick(menu, event, inventory)
        handleMenuMove(menu, event, inventory)
    }

    private fun <I : Inventory> handleMenuClick(menu: StaticMenu<*, I>, event: InventoryClickEvent, inventory: I) {
        if (event.slot != event.rawSlot || event.slot < 0) return

        val slot = menu.getSlotOrBaseSlot(event.slot) as StaticSlotDSL<I>

        val interactEvent = PlayerMenuSlotInteractEvent(
            menu, inventory, event.whoClicked as Player,
            event.slot, slot, slot.cancelEvents,
            event.click, event.action,
            event.currentItem, event.cursor,
            event.hotbarButton
        )

        slot.eventHandler.handleInteract(interactEvent)

        if (interactEvent.isCanceled) event.isCancelled = true
    }

    private fun <I : Inventory> handleMenuMove(menu: StaticMenu<*, I>, event: InventoryClickEvent, inventory: I) {
        val slotIndex = event.rawSlot.takeIf { it >= 0 } ?: return
        val menuSlot = menu.getSlotOrBaseSlot(slotIndex) as StaticSlotDSL<I>

        with(event) {
            if ((slot == rawSlot && (cursor.isNotEmpty ||
                        (hotbarButton != -1 && view.bottomInventory[hotbarButton].isNotEmpty)))
                || (slot != rawSlot && click.isShiftClick && event.inventory.any { it.isEmpty })
            ) {
                val movedItem = if (cursor.isNotEmpty)
                    cursor
                else if (hotbarButton != -1)
                    view.bottomInventory[hotbarButton]
                else
                    currentItem

                val moveEvent = PlayerMoveToMenuEvent(
                    menu, whoClicked as Player, inventory,
                    menuSlot.cancelEvents, movedItem ?: return, hotbarButton
                )

                menu.eventHandler.handleMoveToMenu(moveEvent)

                if (moveEvent.isCanceled) event.isCancelled = true
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onDragEvent(event: InventoryDragEvent) {
        if (event.view.type !in supportedInventoryTypes)
            return

        val player = event.whoClicked as Player
        val menu = event.inventory.asMenu()?.takeIfHasPlayer(player)
        if (menu != null) {
            val pass = event.inventorySlots.firstOrNull { it in event.rawSlots }
            if (pass != null) event.isCancelled = true
        }
    }

    @EventHandler
    fun onCloseEvent(event: InventoryCloseEvent) {
        if (event.view.type !in supportedInventoryTypes)
            return

        val player = event.player as Player
        val menuView = player.getMenuView() ?: return
        menuView.menu.close(player, false)
    }

    @EventHandler(ignoreCancelled = true)
    fun onPickupItemEvent(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return

        if (player.getMenuView() != null)
            event.isCancelled = true
    }
}