package me.gabber235.typewriter.entries.dialogue.messengers.random

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.RandomSpokenDialogueEntry
import me.gabber235.typewriter.entry.dialogue.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerSwapHandItemsEvent

@Messenger(RandomSpokenDialogueEntry::class)
class JavaRandomSpokenDialogueDialogueMessenger(player: Player, entry: RandomSpokenDialogueEntry) :
	DialogueMessenger<RandomSpokenDialogueEntry>(player, entry) {

	companion object : MessengerFilter {
		override fun filter(player: Player, entry: DialogueEntry): Boolean = true
	}

	private var speakerDisplayName = ""
	private var text = ""

	override fun init() {
		super.init()
		speakerDisplayName = entry.speakerDisplayName
		text = entry.messages.randomOrNull() ?: ""

		listen<PlayerSwapHandItemsEvent> { event ->
			if (event.player.uniqueId != player.uniqueId) return@listen
			state = MessengerState.FINISHED
			event.isCancelled = true
		}
	}

	override fun tick(cycle: Int) {
		val durationInTicks = entry.duration.toTicks()

		val percentage = (cycle / durationInTicks.toDouble())

		val nextColor = if (cycle > durationInTicks * 2.5 && (cycle / 6) % 3 == 0) "red" else "gray"

		val continueOrFinish = if (triggers.isEmpty()) "finish" else "continue"

		val message = """
				|<gray><st>${" ".repeat(60)}</st>
				|
				|<gray>    [ <bold>$speakerDisplayName</bold><reset><gray> ]
				|
				|<white>
			""".trimMargin()
			.asMini()
			.append(
				text(
					text.parsePlaceholders(player),
					percentage.coerceAtMost(1.0)
				).color(NamedTextColor.WHITE)
			)
			.append(
				"""
				|
				|<$nextColor>${" ".repeat(20)} Press<white> <key:key.swapOffhand> </white>to $continueOrFinish</${nextColor}>
				|<gray><st>${" ".repeat(60)}</st>
			""".trimMargin().asMini()
			)

		val component = player.chatHistory.composeDarkMessage(message)
		player.sendMessage(component)
	}

	private fun text(
		message: String,
		percentage: Double,
	): Component {
		val talk = talk(message.replace("\n", "\n<reset><white>")).asMini()
		val components = talk.iterable(ComponentIteratorType.DEPTH_FIRST)

		val msg = components.first().plainText()
		val total = msg.length
		var left = (total * percentage).toInt().coerceIn(0, total)

		val coms = mutableListOf<Component>(Component.text("    "))

		val it = components.iterator()
		while (it.hasNext() && left > 0) {
			val com = it.next()
			if (com !is TextComponent) {
				coms.add(com)
				continue
			}

			val text = com.content()
			val size = text.length
			if (left >= size) {
				coms.add(com.children(mutableListOf()))
				left -= size
			} else {
				val newText = text.substring(0, left)
				coms.add(com.content(newText).children(mutableListOf()))
				left = 0
			}
		}

		val component = Component.join(JoinConfiguration.noSeparators(), coms)

		val newLines = msg.count { it == '\n' }
		val newLinesComponent = component.plainText().count { it == '\n' }
		val missingLines = newLines - newLinesComponent
		val missingLinesString = "\n".repeat(missingLines)

		return component.append(Component.text(missingLinesString)).replaceText(
			TextReplacementConfig.builder().match("\\n").replacement(Component.text("\n    ")).build()
		)
	}

	private fun talk(
		message: String,
		suffix: String = "",
		minLines: Int = 3,
	): String {
		val parts = message.split("\n").toMutableList()
		parts[parts.size - 1] = "${parts.last()} $suffix"


		val lines = parts.flatMap { it.splitLine() }.toMutableList()
		while (lines.size < minLines) {
			lines.add("\n")
		}
		return lines.joinToString("")
	}

	private fun String.splitLine(maxLength: Int = 40): List<String> {
		if (this.striped().length <= maxLength) return listOf("$this\n")
		val parts = split(" ")
		val lines = mutableListOf<String>()
		var currentLine = ""

		for (part in parts) {
			if ((currentLine + part).striped().length <= maxLength) {
				currentLine += "$part "
			} else {
				lines.add(currentLine + "\n")
				currentLine = "$part "
			}
		}
		lines.add(currentLine + "\n")
		return lines
	}
}