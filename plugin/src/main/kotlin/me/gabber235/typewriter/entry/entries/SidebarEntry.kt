package me.gabber235.typewriter.entry.entries

import com.github.retrooper.packetevents.protocol.score.ScoreFormat
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResetScore
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private const val MAX_LINES = 15
private const val SCOREBOARD_OBJECTIVE = "typewriter"

@Tags("sidebar")
interface SidebarEntry : AudienceFilterEntry, PlaceholderEntry, PriorityEntry {
    @Help("The title of the sidebar")
    @Colored
    @Placeholder
    val title: String

    override fun display(player: Player?): String? = title.parsePlaceholders(player)

    override fun display(): AudienceFilter = SidebarFilter(ref()) { player ->
        PlayerSidebarDisplay(player, SidebarFilter::class, ref())
    }
}

private class SidebarFilter(
    ref: Ref<SidebarEntry>,
    createDisplay: (Player) -> PlayerSidebarDisplay,
) : SingleFilter<SidebarEntry, PlayerSidebarDisplay>(ref, createDisplay) {
    override val displays: MutableMap<UUID, PlayerSidebarDisplay>
        get() = map

    companion object {
        private val map = ConcurrentHashMap<UUID, PlayerSidebarDisplay>()
    }
}

private class PlayerSidebarDisplay(
    player: Player,
    displayKClass: KClass<out SingleFilter<SidebarEntry, *>>,
    current: Ref<SidebarEntry>,
) : PlayerSingleDisplay<SidebarEntry>(player, displayKClass, current) {
    private var lines = emptyList<Ref<LinesEntry>>()
    private var lastTitle = ""
    private var lastLines = emptyList<String>()

    override fun initialize() {
        super.initialize()
        val sidebar = ref.get() ?: return
        val title = sidebar.display(player) ?: ""

        createSidebar(title)
    }

    override fun setup() {
        super.setup()
        lines = ref.descendants(LinesEntry::class)
    }

    override fun tick() {
        super.tick()

        val sidebar = ref.get() ?: return
        val title = sidebar.display(player) ?: ""

        val lines = lines
            .filter { player.inAudience(it) }
            .sortedByDescending { it.priority }
            .mapNotNull { it.get()?.lines(player) }
            .flatMap { it.parsePlaceholders(player).lines() }

        if (lines != lastLines || title != lastTitle) {
            refreshSidebar(title, lines)
            lastTitle = title
            lastLines = lines
        }
    }

    override fun dispose() {
        super.dispose()
        disposeSidebar()
        lines = emptyList()
        lastTitle = ""
        lastLines = emptyList()
    }

    private fun createSidebar(title: String) {
        WrapperPlayServerScoreboardObjective(
            SCOREBOARD_OBJECTIVE,
            WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE,
            title.asMini(),
            WrapperPlayServerScoreboardObjective.RenderType.INTEGER,
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

    private fun refreshSidebar(title: String, lines: List<String>) {
        val packet = WrapperPlayServerScoreboardObjective(
            SCOREBOARD_OBJECTIVE,
            WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE,
            title.asMini(),
            WrapperPlayServerScoreboardObjective.RenderType.INTEGER,
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
