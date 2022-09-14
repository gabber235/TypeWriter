package me.gabber235.typewriter.entry.dialogue.messengers.spoken

import me.gabber235.typewriter.entry.dialogue.entries.SpokenDialogueEntry
import me.gabber235.typewriter.entry.dialogue.messengers.Messenger
import me.gabber235.typewriter.entry.dialogue.messengers.MessengerState
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class JavaSpokenDialogueMessenger(player: Player, entry: SpokenDialogueEntry) :
	Messenger<SpokenDialogueEntry>(player, entry) {

	private var speakerDisplayName = ""
	override fun init() {
		super.init()
		speakerDisplayName = entry.speakerDisplayName

		listen<PlayerSwapHandItemsEvent> { event ->
			if (event.player.uniqueId != player.uniqueId) return@listen
			state = MessengerState.FINISHED
			event.isCancelled = true
		}
	}

	override fun tick(cycle: Int) {
		val duration = entry.duration

		val percentage = (cycle / duration.toDouble())

		val nextColor = if (cycle > duration * 2.5 && (cycle / 6) % 3 == 0) "red" else "gray"

		val continueOrFinish = if (triggers.isEmpty()) "finish" else "continue"

		val message = """
				|<gray><st>${" ".repeat(60)}</st>
				|
				|<gray>    [ <bold>$speakerDisplayName</bold> ]
				|
				|<white>
			""".trimMargin()
			.asMini()
			.append(
				text(
					entry.text.parsePlaceholders(player),
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