package com.typewritermc.engine.paper.content

import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.content.components.IntractableItem
import com.typewritermc.engine.paper.content.components.ItemInteraction
import com.typewritermc.engine.paper.content.components.ItemInteractionType
import com.typewritermc.engine.paper.entry.entries.SystemTrigger
import com.typewritermc.engine.paper.entry.forceTriggerFor
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.events.ContentEditorEndEvent
import com.typewritermc.engine.paper.events.ContentEditorStartEvent
import com.typewritermc.engine.paper.interaction.InteractionHandler
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.msg
import com.typewritermc.engine.paper.utils.playSound
import lirand.api.extensions.events.unregister
import lirand.api.extensions.server.registerEvents
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.koin.java.KoinJavaComponent
import java.util.concurrent.ConcurrentLinkedDeque

class ContentEditor(
    val context: ContentContext,
    val player: Player,
    mode: ContentMode,
) : Listener {
    private val stack = ConcurrentLinkedDeque(listOf(mode))
    private var items = emptyMap<Int, IntractableItem>()
    private val cachedOriginalItems = mutableMapOf<Int, ItemStack>()

    private val mode: ContentMode?
        get() = stack.peek()

    suspend fun initialize(): Result<Unit> {
        player.playSound("block.beacon.activate")
        SYNC.switchContext {
            ContentEditorStartEvent(player).callEvent()
        }
        plugin.registerEvents(this)
        val mode = mode
        if (mode == null) {
            SystemTrigger.CONTENT_END forceTriggerFor player
            return failure("No content mode found")
        }
        val result = mode.setup()
        if (result.isFailure) {
            logger.severe("Failed to setup content mode for player ${player.name}: ${result.exceptionOrNull()?.message}")
            player.msg("<red><b>Failed to setup content mode. Please see the console for more details.")
            return result
        }
        mode.initialize()
        return ok(Unit)
    }

    suspend fun tick() {
        applyInventory()
        mode?.tick()
    }

    private suspend fun applyInventory() {
        val previousSlots = items.keys
        items = mode?.items() ?: emptyMap()
        val currentSlots = items.keys
        val newSlots = currentSlots - previousSlots
        val removedSlots = previousSlots - currentSlots
        SYNC.switchContext {
            newSlots.forEach { slot ->
                val originalItem = player.inventory.getItem(slot) ?: ItemStack.empty()
                cachedOriginalItems.putIfAbsent(slot, originalItem)
            }
            items.forEach { (slot, item) ->
                player.inventory.setItem(slot, item.item)
            }
            removedSlots.forEach { slot ->
                val originalItem = cachedOriginalItems.remove(slot)
                player.inventory.setItem(slot, originalItem)
            }
        }
    }

    suspend fun pushMode(newMode: ContentMode): Result<Unit> {
        player.playSound("ui.loom.take_result")
        val previous = mode
        val result = newMode.setup()
        stack.push(newMode)
        previous?.dispose()
        if (result.isFailure) {
            logger.severe("Failed to setup content mode: ${result.exceptionOrNull()?.message}")
            player.msg("<red><bold>Failed to setup content mode. Please see the console for more details.")
            return result
        }
        newMode.initialize()
        return ok(Unit)
    }

    suspend fun swapMode(newMode: ContentMode): Result<Unit> {
        player.playSound("ui.loom.take_result")
        val previous = stack.pop()
        val result = newMode.setup()
        stack.push(newMode)
        previous.dispose()
        if (result.isFailure) {
            logger.severe("Failed to setup content mode: ${result.exceptionOrNull()?.message}")
            player.msg("<red><bold>Failed to setup content mode. Please see the console for more details.")
            return result
        }
        newMode.initialize()
        return ok(Unit)
    }

    suspend fun popMode(): Boolean {
        player.playSound("ui.cartography_table.take_result")
        stack.pop()?.dispose()
        mode?.initialize()
        return mode != null
    }

    suspend fun dispose() {
        unregister()
        cachedOriginalItems.forEach { (slot, item) ->
            player.inventory.setItem(slot, item)
        }
        cachedOriginalItems.clear()
        val cache = stack.toList()
        stack.clear()
        cache.forEach { it.dispose() }
        SYNC.switchContext {
            player.playSound("block.beacon.deactivate")
            ContentEditorEndEvent(player).callEvent()
        }
    }

    fun isInLastMode(): Boolean = stack.size == 1

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked != player) return
        if (event.clickedInventory != player.inventory) return
        val item = items[event.slot] ?: return
        item.action(
            ItemInteraction(ItemInteractionType.INVENTORY_CLICK, event.slot),
        )
        event.isCancelled = true
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.player != player) return
        // The even triggers twice. Both for the main hand and offhand.
        // We only want to trigger once.
        if (event.hand != org.bukkit.inventory.EquipmentSlot.HAND) return // Disable off-hand interactions
        val slot = player.inventory.heldItemSlot
        val item = items[slot] ?: return
        val type = when (event.action) {
            Action.LEFT_CLICK_AIR,
            Action.LEFT_CLICK_BLOCK -> if (event.player.isSneaking) ItemInteractionType.SHIFT_LEFT_CLICK else ItemInteractionType.LEFT_CLICK

            Action.RIGHT_CLICK_AIR,
            Action.RIGHT_CLICK_BLOCK -> if (event.player.isSneaking) ItemInteractionType.SHIFT_RIGHT_CLICK else ItemInteractionType.RIGHT_CLICK

            else -> return
        }
        item.action(ItemInteraction(type, slot))
        event.isCancelled = true
    }

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) {
        if (event.player != player) return
        val slot = player.inventory.heldItemSlot
        val item = items[slot] ?: return
        item.action(ItemInteraction(ItemInteractionType.DROP, slot))
        event.isCancelled = true
    }

    @EventHandler
    fun onSwapItem(event: PlayerSwapHandItemsEvent) {
        if (event.player != player) return
        val slot = player.inventory.heldItemSlot
        val item = items[slot] ?: return
        item.action(ItemInteraction(ItemInteractionType.SWAP, slot))
        event.isCancelled = true
    }
}

private val Player.content: ContentEditor?
    get() =
        with(KoinJavaComponent.get<InteractionHandler>(InteractionHandler::class.java)) {
            interaction?.content
        }

val Player.isInContent: Boolean
    get() = content != null

val Player.inLastContentMode: Boolean
    get() = content?.isInLastMode() == true
