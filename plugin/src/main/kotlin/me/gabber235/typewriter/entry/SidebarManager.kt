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
import me.gabber235.typewriter.entry.entries.LinesEntry
import me.gabber235.typewriter.entry.entries.SidebarEntry
import me.gabber235.typewriter.events.PublishedBookEvent
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.Listener

private const val MAX_LINES = 15
private const val SCOREBOARD_OBJECTIVE = "typewriter"

class SidebarManager(
    private val player: Player,
) : Listener {
    private var sidebar: Ref<SidebarEntry>? = null
    private var lines = emptyList<Ref<LinesEntry>>()
    private var lastTitle = ""
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
        lastTitle = ""
        lastLines = emptyList()
    }

    fun tick() {
        val newSidebar = Query.findWhere<SidebarEntry> { player.inAudience(it.ref()) }.maxByOrNull {
            it.priority
        }
        val title = newSidebar?.display(player) ?: ""

        if (newSidebar?.ref() != sidebar) {
            if (sidebar == null) createSidebar(title)
            else if (newSidebar == null) disposeSidebar()

            sidebar = newSidebar?.ref()
            lines = sidebar?.descendants(LinesEntry::class) ?: emptyList()
        }

        val lines = lines
            .filter { player.inAudience(it) }
            .sortedByDescending { it.priority }
            .mapNotNull { it.get()?.lines(player) }
            .flatMap { it.parsePlaceholders(player).lines() }

        if (lines != lastLines || title != lastTitle) {
            sendSidebar(title, lines)
            lastTitle = title
            lastLines = lines
        }
    }

    private fun createSidebar(title: String) {
        WrapperPlayServerScoreboardObjective(
            SCOREBOARD_OBJECTIVE,
            CREATE,
            title.asMini(),
            INTEGER,
            ScoreFormat.blankScore(),
        ).sendPacketTo(player)

        WrapperPlayServerDisplayScoreboard(1, SCOREBOARD_OBJECTIVE).sendPacketTo(player)
    }

    private fun disposeSidebar() {
        WrapperPlayServerScoreboardObjective(
            SCOREBOARD_OBJECTIVE,
            WrapperPlayServerScoreboardObjective.ObjectiveMode.REMOVE,
            Component.empty(),
            null,
            null
        ).sendPacketTo(player)
    }

    private fun sendSidebar(title: String, lines: List<String>) {
        val packet = WrapperPlayServerScoreboardObjective(
            SCOREBOARD_OBJECTIVE,
            UPDATE,
            title.asMini(),
            INTEGER,
            ScoreFormat.blankScore(),
        )
        packet.sendPacketTo(player)

        val displayPacket = WrapperPlayServerDisplayScoreboard(1, SCOREBOARD_OBJECTIVE)
        displayPacket.sendPacketTo(player)


        for ((index, line) in lines.withIndex().take(MAX_LINES)) {
            val lastLine = lastLines.getOrNull(index)
            if (lastLine == line) continue

            WrapperPlayServerUpdateScore(
                "${SCOREBOARD_OBJECTIVE}_line_$index",
                WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
                SCOREBOARD_OBJECTIVE,
                MAX_LINES - index,
                line.asMini(),
                ScoreFormat.blankScore(),
            ).sendPacketTo(player)
        }

        if (lines.size < lastLines.size) {
            for (i in lines.size until lastLines.size) {
                WrapperPlayServerResetScore(
                    "${SCOREBOARD_OBJECTIVE}_line_$i",
                    SCOREBOARD_OBJECTIVE,
                ).sendPacketTo(player)
            }
        }
    }
}