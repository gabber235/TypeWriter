package me.gabber235.typewriter.entries.dialogue.messengers.option

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.Option
import me.gabber235.typewriter.entries.dialogue.OptionDialogueEntry
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.dialogue.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.matches
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.facts.facts
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.sendMessage
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import kotlin.math.max
import kotlin.math.min

@Messenger(OptionDialogueEntry::class)
class JavaOptionDialogueDialogueMessenger(player: Player, entry: OptionDialogueEntry) :
	DialogueMessenger<OptionDialogueEntry>(player, entry) {

	companion object : MessengerFilter {
		override fun filter(player: Player, entry: DialogueEntry): Boolean = true
	}

	private var selectedIndex = 0
	private val selected get() = usableOptions[selectedIndex]

	private var usableOptions: List<Option> = emptyList()
	private var speakerDisplayName = ""

	override val triggers: List<String>
		get() = entry.triggers + selected.triggers

	override val modifiers: List<Modifier>
		get() = entry.modifiers + selected.modifiers

	override fun init() {
		super.init()
		val facts = player.facts
		usableOptions = entry.options.filter { it.criteria.matches(facts) }.sortedByDescending { it.criteria.size }

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
		val message = """
			|<gray><st>${" ".repeat(60)}</st>
			|<white> ${speakerDisplayName}: ${entry.text}
			|
			|${formatOptions()}
			|<#5d6c78>[ <grey><white>Scroll</white> to change option and press<white> <key:key.swapOffhand> </white>to select <#5d6c78>]</#5d6c78>
			|<gray><st>${" ".repeat(60)}</st>
		""".trimMargin().asMini()

		val component = player.chatHistory.composeDarkMessage(message)
		player.sendMessage(component)
	}

	override fun end() {
		player.chatHistory.resendMessages(player)
	}

	private fun formatOptions(): String {
		val around = usableOptions.around(selectedIndex, 1, 2)

		val lines = mutableListOf<String>()

		for (i in 0..3) {
			if (i >= around.size) {
				lines.add("\n")
				continue
			}
			val option = around[i]
			val selected = selected == option

			val prefix = if (selected) "<#78ff85>>>"
			else if (i == 0 && selectedIndex > 1 && usableOptions.size > 4) "<white> ↑"
			else if (i == 3 && selectedIndex < usableOptions.size - 3 && usableOptions.size > 4) "<white> ↓"
			else "  "

			lines += if (selected) {
				" $prefix <#5d6c78>[ <white>${option.text.parsePlaceholders(player)} <#5d6c78>]\n"
			} else {
				" $prefix <#5d6c78>[ <grey>${option.text.parsePlaceholders(player)} <#5d6c78>]\n"
			}
		}

		return lines.joinToString("")
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