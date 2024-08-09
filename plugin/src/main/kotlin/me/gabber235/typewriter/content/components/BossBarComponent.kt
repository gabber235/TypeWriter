package me.gabber235.typewriter.content.components

import me.gabber235.typewriter.content.ComponentContainer
import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.entity.Player

fun ComponentContainer.bossBar(builder: BossBarBuilder.() -> Unit) {
    +BossBarComponent(builder)
}

class BossBarComponent(
    private val builder: BossBarBuilder.() -> Unit
) : ContentComponent {
    private var bossBar: BossBar? = null
    override suspend fun initialize(player: Player) {
        val barBuilder = BossBarBuilder().apply(builder)
        bossBar = BossBar.bossBar(
            barBuilder.title.parsePlaceholders(player).asMini(),
            barBuilder.progress.coerceIn(0.0f, 1.0f),
            barBuilder.color,
            barBuilder.overlay,
            barBuilder.flags
        ).also {
            player.showBossBar(it)
        }
    }

    override suspend fun tick(player: Player) {
        val bossBar = bossBar ?: return
        val barBuilder = BossBarBuilder().apply(builder)
        bossBar.name(barBuilder.title.parsePlaceholders(player).asMini())
            .progress(barBuilder.progress.coerceIn(0.0f, 1.0f))
            .color(barBuilder.color)
            .overlay(barBuilder.overlay)
            .removeFlags(bossBar.flags() - barBuilder.flags)
            .addFlags(barBuilder.flags - bossBar.flags())
    }

    override suspend fun dispose(player: Player) {
        bossBar?.let { player.hideBossBar(it) }
    }
}

class BossBarBuilder {
    var title: String = ""
    var progress: Float = 1.0f
    var color: BossBar.Color = BossBar.Color.WHITE
    var overlay: BossBar.Overlay = BossBar.Overlay.PROGRESS
    val flags = mutableSetOf<BossBar.Flag>()

    operator fun BossBar.Flag.unaryPlus() {
        flags.add(this)
    }
}