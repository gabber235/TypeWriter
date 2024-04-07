package me.gabber235.typewriter.content

import lirand.api.extensions.events.unregister
import lirand.api.extensions.server.registerEvents
import lirand.api.extensions.world.clearAll
import me.gabber235.typewriter.content.components.IntractableItem
import me.gabber235.typewriter.content.components.ItemInteraction
import me.gabber235.typewriter.content.components.ItemInteractionType
import me.gabber235.typewriter.entry.entries.SystemTrigger
import me.gabber235.typewriter.entry.forceTriggerFor
import me.gabber235.typewriter.events.ContentEditorEndEvent
import me.gabber235.typewriter.events.ContentEditorStartEvent
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.gabber235.typewriter.utils.msg
import me.gabber235.typewriter.utils.playSound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.koin.java.KoinJavaComponent
import java.util.concurrent.ConcurrentLinkedDeque

class ContentEditor(
    val context: ContentContext,
    val player: Player,
    mode: ContentMode,
) : Listener {
    val cachedInventory = player.inventory.contents.copyOf()
    private val stack = ConcurrentLinkedDeque(listOf(mode))
    private var items = emptyMap<Int, IntractableItem>()

    private val mode: ContentMode?
        get() = stack.peek()

    suspend fun initialize() {
        player.playSound("block.beacon.activate")
        SYNC.switchContext {
            ContentEditorStartEvent(player).callEvent()
            player.inventory.clearAll()
        }
        plugin.registerEvents(this)
        val mode = mode
        if (mode == null) {
            SystemTrigger.CONTENT_END forceTriggerFor player
            return
        }
        val result = mode.setup()
        if (result.isFailure) {
            logger.severe("Failed to setup content mode for player ${player.name}: ${result.exceptionOrNull()?.message}")
            player.msg("<red><b>Failed to setup content mode. Please report this to the server administrator.")
            SystemTrigger.CONTENT_END forceTriggerFor player
            return
        }
        mode.initialize()
    }

    suspend fun tick() {
        applyInventory()
        mode?.tick()
    }

    private suspend fun applyInventory() {
        val previousSlots = items.keys
        items = mode?.items() ?: emptyMap()
        val newSlots = items.keys
        val toRemove = previousSlots - newSlots
        SYNC.switchContext {
            items.forEach { (slot, item) ->
                player.inventory.setItem(slot, item.item)
            }
            toRemove.forEach { player.inventory.setItem(it, null) }
        }
    }

    suspend fun pushMode(newMode: ContentMode) {
        player.playSound("ui.loom.take_result")
        val previous = mode
        newMode.setup()
        stack.push(newMode)
        previous?.dispose()
        newMode.initialize()
    }

    suspend fun swapMode(newMode: ContentMode) {
        player.playSound("ui.loom.take_result")
        val previous = stack.pop()
        newMode.setup()
        stack.push(newMode)
        previous.dispose()
        newMode.initialize()
    }

    suspend fun popMode(): Boolean {
        player.playSound("ui.cartography_table.take_result")
        stack.pop()?.dispose()
        mode?.initialize()
        return mode != null
    }

    suspend fun dispose() {
        unregister()
        val cache =  stack.toList()
        stack.clear()
        cache.forEach { it.dispose() }
        SYNC.switchContext {
            player.inventory.contents = cachedInventory
            player.playSound("block.beacon.deactivate")
            ContentEditorEndEvent(player).callEvent()
        }
    }

    fun isInLastMode(): Boolean {
        return stack.size == 1
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked != player) return
        if (event.clickedInventory != player.inventory) return
        val item = items[event.slot] ?: return
        item.action(
            ItemInteraction(ItemInteractionType.INVENTORY_CLICK, event.slot)
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
    get() = with(KoinJavaComponent.get<InteractionHandler>(InteractionHandler::class.java)) {
        interaction?.content
    }

val Player.isInContent: Boolean
    get() = content != null

val Player.inLastContentMode: Boolean
    get() = content?.isInLastMode() == true

val Player.contentEditorCachedInventory: Array<org.bukkit.inventory.ItemStack?>?
    get() = content?.cachedInventory