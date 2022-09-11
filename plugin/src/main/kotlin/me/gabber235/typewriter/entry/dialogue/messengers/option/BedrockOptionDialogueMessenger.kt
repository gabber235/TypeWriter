package me.gabber235.typewriter.entry.dialogue.messengers.option

import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.dialogue.entries.Option
import me.gabber235.typewriter.entry.dialogue.entries.OptionDialogueEntry
import me.gabber235.typewriter.entry.dialogue.messengers.Messenger
import me.gabber235.typewriter.entry.dialogue.messengers.MessengerState
import me.gabber235.typewriter.entry.matches
import me.gabber235.typewriter.facts.facts
import me.gabber235.typewriter.utils.legacy
import org.bukkit.entity.Player
import org.geysermc.cumulus.form.CustomForm
import org.geysermc.floodgate.api.FloodgateApi

class BedrockOptionDialogueMessenger(player: Player, entry: OptionDialogueEntry) :
	Messenger<OptionDialogueEntry>(player, entry) {
	private var selectedIndex = 0
	private val selected get() = usableOptions[selectedIndex]

	private var usableOptions: List<Option> = emptyList()

	override val triggers: List<String>
		get() = entry.triggers + selected.triggers

	override val modifiers: List<Modifier>
		get() = entry.modifiers + selected.modifiers


	override fun init(player: Player) {
		super.init(player)
		val facts = player.facts
		usableOptions = entry.options.filter { it.criteria.matches(facts) }
		FloodgateApi.getInstance().sendForm(
			player.uniqueId,
			CustomForm.builder()
				.title("<bold>${entry.speaker}</bold>".legacy())
				.label("${entry.text.legacy()}\n\n\n")
				.dropdown(
					"Select Response",
					usableOptions.map { it.text.legacy() })
				.label("\n\n\n\n")
				.closedOrInvalidResultHandler { _, _ ->
					state = MessengerState.CANCELLED
				}
				.validResultHandler { responds ->
					val dropdown = responds.asDropdown()
					selectedIndex = dropdown
					state = MessengerState.FINISHED
				}
		)
	}
}