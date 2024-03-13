package me.gabber235.typewriter.content.components

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import lirand.api.extensions.events.unregister
import lirand.api.extensions.inventory.meta
import lirand.api.extensions.server.registerEvents
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.ItemInteractionType.*
import me.gabber235.typewriter.content.pageId
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.Page
import me.gabber235.typewriter.entry.PageType
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.maxFrame
import me.gabber235.typewriter.interaction.startBlockingActionBar
import me.gabber235.typewriter.interaction.stopBlockingActionBar
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.koin.java.KoinJavaComponent
import java.util.*

fun ContentMode.cinematic(context: ContentContext) = +SimulateCinematicComponent(context)

class SimulateCinematicComponent(
    private val context: ContentContext,
) : CompoundContentComponent(), ItemsComponent, Listener {
    private var actions = emptyList<CinematicAction>()
    private var maxFrame = 0

    private var playbackSpeed = 0.0
    private var partialFrame: Double = 0.0
        set(value) {
            field = value.coerceIn(0.0, maxFrame.toDouble())
        }
    val frame: Int
        get() = partialFrame.toInt()

    // If we are in scrolling modes where we scroll through the frames. If set to the UUID of the player
    private var scrollFrames: UUID? = null

    override suspend fun initialize(player: Player) {
        val page = findCinematicPageById(context.pageId) ?: return

        actions = page.entries.filterIsInstance<CinematicEntry>().mapNotNull { it.createSimulating(player) }

        plugin.registerEvents(this)
        player.startBlockingActionBar()

        actions.forEach {
            try {
                it.setup()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        maxFrame = actions.maxFrame()

        bossBar {
            val frameDisplay = "$frame".padStart(maxFrame.digits)
            val scrolling =
                if (scrollFrames != null) " <gray>- <gradient:#9452ff:#ff2eea><b>(Scrolling)</b></gradient>" else ""
            title =
                "<yellow><bold>${page.id} <reset><gray>- <white>$frameDisplay/$maxFrame (${playbackSpeed}x)$scrolling"
            color = if (scrollFrames != null) BossBar.Color.PURPLE else BossBar.Color.YELLOW
            overlay = BossBar.Overlay.NOTCHED_20
            progress = (partialFrame / maxFrame).toFloat()
        }

        super.initialize(player)
    }

    override suspend fun tick(player: Player) {
        partialFrame += playbackSpeed

        if (frame >= maxFrame) {
            playbackSpeed = 0.0
        }

        if (frame <= 0) {
            playbackSpeed = 0.0
        }

        coroutineScope {
            actions.map {
                launch {
                    try {
                        it.tick(frame)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.joinAll()
        }

        super.tick(player)
    }

    @EventHandler
    private fun onScroll(event: PlayerItemHeldEvent) {
        if (event.player.uniqueId != scrollFrames) return
        val delta = loopingDistance(event.previousSlot, event.newSlot, 8)
        partialFrame += delta * 10
        event.player.playSound("block.note_block.hat", pitch = 1f + (delta * 0.1f), volume = 0.5f)
        event.isCancelled = true
    }

    override suspend fun dispose(player: Player) {
        unregister()
        player.stopBlockingActionBar()
        actions.forEach {
            try {
                it.teardown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        actions = emptyList()
        super.dispose(player)
    }

    override fun items(player: Player): Map<Int, IntractableItem> {
        val playbackSpeed = ItemStack(Material.CLOCK).meta {
            name = "<yellow><bold>Playback Speed"
            loreString = """
                    |<line> <green><b>Right Click: </b><white>Increases speed by 1
                    |<line> <green>Shift + Right Click: <white>Increases speed by 0.25
                    |<line> <red><b>Left Click: </b><white>Decreases speed by 1
                    |<line> <red>Shift + Left Click: <white>Decreases speed by 0.25
                    |<line> <blue><b><key:key.swapOffhand>: </b><white>Pause/Resume
                """.trimMargin()
        } onInteract { (type) ->
            when (type) {
                RIGHT_CLICK -> playbackSpeed += 1
                SHIFT_RIGHT_CLICK -> playbackSpeed += 0.25
                LEFT_CLICK -> playbackSpeed -= 1
                SHIFT_LEFT_CLICK -> playbackSpeed -= 0.25
                SWAP, DROP -> playbackSpeed = if (playbackSpeed != 0.0) 0.0 else 1.0
                else -> {
                    return@onInteract
                }
            }
            player.playSound("ui.button.click")
        }

        val skip = ItemStack(Material.AMETHYST_SHARD).meta {
            name = "<yellow><bold>Skip Frame"
            loreString = """
                    |<line> <green><b>Right Click: </b><white>Goes forward 20 frames
                    |<line> <green>Shift + Right Click: <white>Goes forward 1 frames
                    |<line> <red><b>Left Click: </b><white>Goes backwards 20 frames
                    |<line> <red>Shift + Left Click: <white>Goes backwards 1 frames
                    |<line> <yellow><b><key:key.drop>: </b><white>Rewind to start
                    |<line> <blue><b><key:key.swapOffhand>: </b><white>Go into advanced playback control mode
                """.trimMargin()
        } onInteract { (type) ->
            when (type) {
                RIGHT_CLICK -> partialFrame += 20
                SHIFT_RIGHT_CLICK -> partialFrame += 1
                LEFT_CLICK -> partialFrame -= 20
                SHIFT_LEFT_CLICK -> partialFrame -= 1
                DROP -> partialFrame = 0.0
                SWAP -> {
                    scrollFrames = if (scrollFrames == null) {
                        player.playSound("block.amethyst_block.hit")
                        player.uniqueId
                    } else {
                        player.playSound("block.amethyst_block.fall")
                        null
                    }
                }

                else -> {
                    return@onInteract
                }
            }
            player.playSound("ui.button.click")
        }

        return mapOf(
            0 to playbackSpeed,
            1 to skip,
        )
    }
}

fun findCinematicPageById(pageId: String?): Page? {
    if (pageId.isNullOrEmpty()) {
        logger.warning("Can only simulate cinematic for a page")
        return null
    }
    val entryDatabase = KoinJavaComponent.get<EntryDatabase>(EntryDatabase::class.java)
    val page = entryDatabase.findPageById(pageId)
    if (page == null) {
        logger.warning("Page $pageId not found, make sure to publish before using content mode")
        return null
    }

    if (page.type != PageType.CINEMATIC) {
        logger.warning("Page $pageId is not a cinematic page")
        return null
    }
    return page
}