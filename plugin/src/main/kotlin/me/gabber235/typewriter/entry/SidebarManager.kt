package me.gabber235.typewriter.entry

import com.github.retrooper.packetevents.protocol.score.ScoreFormat
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResetScore
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective.RenderType.INTEGER
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore
import lirand.api.extensions.events.listen
import lirand.api.extensions.events.unregister
import me.gabber235.typewriter.entry.entries.SidebarEntry
import me.gabber235.typewriter.entry.entries.SidebarLinesEntry
import me.gabber235.typewriter.events.PublishedBookEvent
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import kotlin.math.max

class SidebarManager(
    private val player: Player,
) : Listener {
    private var sidebar: Ref<SidebarEntry>? = null
    private var lines = emptyList<Ref<SidebarLinesEntry>>()
    private var lastLines = emptyList<String>()

    init {
        plugin.listen<PublishedBookEvent>(this) { dispose(false) }
        plugin.listen<TypewriterReloadEvent>(this) { dispose(false) }
    }

    fun dispose(force: Boolean) {
        if (force) unregister()
        disposeSidebar()
        sidebar = null
        lines = emptyList()
        lastLines = emptyList()
    }

    fun tick() {
        val newSidebar = Query.findWhere<SidebarEntry> { player.inAudience(it.ref()) }.maxByOrNull { it.priority }
        val title = newSidebar?.display(player) ?: ""

        if (newSidebar?.ref() != sidebar) {
            if (sidebar == null) createSidebar(title)
            else disposeSidebar()

            sidebar = newSidebar?.ref()
            lines = sidebar?.descendants(SidebarLinesEntry::class) ?: emptyList()
        }

        val lines = lines
            .filter { player.inAudience(it) }
            .mapNotNull { it.get()?.lines(player) }
            .flatMap { it.split("\n") }

        if (lines != lastLines) {
            sendSidebar(title, lines)
            lastLines = lines
        }
    }

    private fun createSidebar(title: String) {
        WrapperPlayServerScoreboardObjective(
            "typewriter",
            CREATE,
            title.asMini(),
            INTEGER,
            ScoreFormat.blankScore()
        ).sendPacketTo(player)

        WrapperPlayServerDisplayScoreboard(1, "typewriter").sendPacketTo(player)
    }

    private fun disposeSidebar() {
        WrapperPlayServerScoreboardObjective(
            "typewriter",
            WrapperPlayServerScoreboardObjective.ObjectiveMode.REMOVE,
            Component.empty(),
            null,
            null
        ).sendPacketTo(player)
    }

    fun sendSidebar(title: String, lines: List<String>) {
        val packet = WrapperPlayServerScoreboardObjective(
            "typewriter",
            UPDATE,
            title.asMini(),
            INTEGER,
            ScoreFormat.blankScore()
        )
        packet.sendPacketTo(player)

        val displayPacket = WrapperPlayServerDisplayScoreboard(1, "typewriter")
        displayPacket.sendPacketTo(player)

        val lineCount = max(lastLines.size, lines.size).coerceAtMost(15)

        for (i in 0 until lineCount) {
            val line = lines.getOrNull(i)
            val lastLine = lastLines.getOrNull(i)
            if (lastLine == line) continue

            if (line == null) {
                WrapperPlayServerResetScore(
                    "typewriter_line_$i",
                    "typewriter"
                ).sendPacketTo(player)
                continue
            }

            WrapperPlayServerUpdateScore(
                "typewriter_line_$i",
                WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
                "typewriter",
                lineCount - i,
                line.asMini(),
                ScoreFormat.blankScore(),
            ).sendPacketTo(player)
        }
    }
}