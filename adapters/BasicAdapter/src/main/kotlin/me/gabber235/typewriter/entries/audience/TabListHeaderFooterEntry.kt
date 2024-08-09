package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@Entry(
    "tab_list_header_footer",
    "Set the header and footer of the tab list",
    Colors.DARK_ORANGE,
    "mdi:page-layout-header"
)
/**
 * The `TabListHeaderFooterEntry` is an entry that sets the header and footer of the tab list.
 * It takes in lines for the header and footer.
 * And shows them to the players in the tab list.
 */
class TabListHeaderFooterEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The lines to display in the header of the tab list")
    val header: List<Ref<out AudienceEntry>> = emptyList(),
    @Help("The lines to display in the footer of the tab list")
    val footer: List<Ref<out AudienceEntry>> = emptyList(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : AudienceFilterEntry, PriorityEntry {
    override val children: List<Ref<out AudienceEntry>>
        get() = header + footer

    override fun display(): AudienceFilter = TabListHeaderFooterFilter(ref()) { player ->
        PlayerTabListHeaderFooter(player, TabListHeaderFooterFilter::class, ref())
    }
}

private class TabListHeaderFooterFilter(
    ref: Ref<TabListHeaderFooterEntry>,
    createDisplay: (Player) -> PlayerTabListHeaderFooter,
) : SingleFilter<TabListHeaderFooterEntry, PlayerTabListHeaderFooter>(ref, createDisplay) {
    override val displays: MutableMap<UUID, PlayerTabListHeaderFooter>
        get() = map

    companion object {
        private val map = ConcurrentHashMap<UUID, PlayerTabListHeaderFooter>()
    }
}

private class PlayerTabListHeaderFooter(
    player: Player,
    displayKClass: KClass<out SingleFilter<TabListHeaderFooterEntry, *>>,
    current: Ref<TabListHeaderFooterEntry>
) : PlayerSingleDisplay<TabListHeaderFooterEntry>(player, displayKClass, current) {
    private var headerEntries = emptyList<Ref<LinesEntry>>()
    private var footerEntries = emptyList<Ref<LinesEntry>>()
    private var lastHeader = ""
    private var lastFooter = ""

    override fun setup() {
        super.setup()
        headerEntries = ref.get()?.header?.descendants(LinesEntry::class) ?: emptyList()
        footerEntries = ref.get()?.footer?.descendants(LinesEntry::class) ?: emptyList()
    }

    override fun tick() {
        super.tick()
        val entry = ref.get()
        if (entry == null) {
            if (lastHeader.isNotEmpty() || lastFooter.isNotEmpty()) {
                clear()
            }
            return
        }

        val header = headerEntries.display(player)
        val footer = footerEntries.display(player)

        if (header == lastHeader && footer == lastFooter) return
        player.sendPlayerListHeaderAndFooter(header.asMini(), footer.asMini())
        lastHeader = header
        lastFooter = footer
    }

    private fun List<Ref<LinesEntry>>.display(player: Player): String {
        return this
            .asSequence()
            .filter { player.inAudience(it) }
            .sortedByDescending { it.priority }
            .mapNotNull { it.get()?.lines(player) }
            .flatMap { it.parsePlaceholders(player).lines() }
            .joinToString("\n")
    }

    private fun clear() {
        player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty())
        lastHeader = ""
        lastFooter = ""
    }

    override fun dispose() {
        super.dispose()
        clear()
    }
}