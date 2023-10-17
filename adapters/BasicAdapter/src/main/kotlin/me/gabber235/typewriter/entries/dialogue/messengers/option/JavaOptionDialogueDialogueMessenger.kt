package me.gabber235.typewriter.entries.dialogue.messengers.option

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.Option
import me.gabber235.typewriter.entries.dialogue.OptionDialogueEntry
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.dialogue.MessengerState
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.matches
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.asMiniWithResolvers
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import kotlin.math.max
import kotlin.math.min

val optionFormat: String by snippet(
    "dialogue.option.format", """
			|<gray><st>${" ".repeat(60)}</st>
			|<white> <speaker><reset>: <text>
			|
			|<options>
			|<#5d6c78>[ <grey><white>Scroll</white> to change option and press<white> <key:key.swapOffhand> </white>to select <#5d6c78>]</#5d6c78>
			|<gray><st>${" ".repeat(60)}</st>
		""".trimMargin()
)

private val selectedPrefix: String by snippet("dialogue.option.prefix.selected", "<#78ff85>>>")
private val upPrefix: String by snippet("dialogue.option.prefix.up", "<white> ↑")
private val downPrefix: String by snippet("dialogue.option.prefix.down", "<white> ↓")
private val unselectedPrefix: String by snippet("dialogue.option.prefix.unselected", "  ")

private val selectedOption: String by snippet(
    "dialogue.option.selected",
    " <prefix> <#5d6c78>[ <white><option_text> <#5d6c78>]\n"
)
private val unselectedOption: String by snippet(
    "dialogue.option.unselected",
    " <prefix> <#5d6c78>[ <grey><option_text> <#5d6c78>]\n"
)

@Messenger(OptionDialogueEntry::class)
class JavaOptionDialogueDialogueMessenger(player: Player, entry: OptionDialogueEntry) :
    DialogueMessenger<OptionDialogueEntry>(player, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = true
    }

    private var selectedIndex = 0
    private val selected get() = usableOptions.getOrNull(selectedIndex)

    private var usableOptions: List<Option> = emptyList()
    private var speakerDisplayName = ""

    override val triggers: List<String>
        get() = entry.triggers + (selected?.triggers ?: emptyList())

    override val modifiers: List<Modifier>
        get() = entry.modifiers + (selected?.modifiers ?: emptyList())

    override fun init() {
        super.init()
        usableOptions =
            entry.options.filter { it.criteria.matches(player.uniqueId) }.sortedByDescending { it.criteria.size }

        speakerDisplayName = entry.speakerDisplayName

        listen<PlayerSwapHandItemsEvent> { event ->
            if (event.player.uniqueId != player.uniqueId) return@listen
            state = MessengerState.FINISHED
            event.isCancelled = true
        }

        listen<PlayerItemHeldEvent> { event ->
            if (event.player.uniqueId == player.uniqueId) {
                val curSlot = event.previousSlot
                val newSlot = event.newSlot
                val dif = loopingDistance(curSlot, newSlot, 8)
                val index = selectedIndex
                event.isCancelled = true
                var newIndex = (index + dif) % usableOptions.size
                while (newIndex < 0) newIndex += usableOptions.size
                selectedIndex = newIndex
                tick(0)
            }
        }
    }

    override fun tick(cycle: Int) {
        if (state != MessengerState.RUNNING) return

        if (cycle % 100 > 0) {
            // Only update periodically to avoid spamming the player
            return
        }

        val message = optionFormat.asMiniWithResolvers(
            Placeholder.parsed("speaker", speakerDisplayName.parsePlaceholders(player)),
            Placeholder.parsed("text", entry.text.parsePlaceholders(player)),
            Placeholder.component("options", formatOptions()),
        )

        val component = player.chatHistory.composeDarkMessage(message)
        player.sendMessage(component)
    }

    private fun formatOptions(): Component {
        val around = usableOptions.around(selectedIndex, 1, 2)

        val lines = mutableListOf<Component>()

        for (i in 0..3) {
            if (i >= around.size) {
                lines.add("\n".asMini())
                continue
            }
            val option = around[i]
            val isSelected = selected == option

            val prefix = if (isSelected) selectedPrefix
            else if (i == 0 && selectedIndex > 1 && usableOptions.size > 4) upPrefix
            else if (i == 3 && selectedIndex < usableOptions.size - 3 && usableOptions.size > 4) downPrefix
            else unselectedPrefix

            val format = if (isSelected) selectedOption else unselectedOption
            lines += format.asMiniWithResolvers(
                Placeholder.parsed("prefix", prefix),
                Placeholder.parsed("option_text", option.text.parsePlaceholders(player))
            )
        }

        return Component.join(JoinConfiguration.noSeparators(), lines)
    }


    private fun loopingDistance(x: Int, y: Int, n: Int): Int {
        val max = max(x, y)
        val min = min(x, y)
        val first = max - min
        val second = n - (max - min - 1)
        return if (x < y) {
            if (first < second) first else -second
        } else {
            if (first < second) -first else second
        }
    }

    private fun <T> List<T>.around(index: Int, before: Int = 1, after: Int = 1): List<T> {
        val total = before + after + 1
        return if (index <= before) subList(0, min(size, total))
        else if (size - index <= after) subList(max(0, size - total), size)
        else subList(index - before, index + after + 1)
    }
}