package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.*
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

@Entry("spoken_dialogue_cinematic", "Play a spoken dialogue cinematic", Colors.CYAN, Icons.MESSAGE)
data class SpokenDialogueCinematicEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	@Help("The speaker of the dialogue")
	@EntryIdentifier(SpeakerEntry::class)
	val speaker: String = "",
	@Segments(icon = Icons.MESSAGE)
	val segments: List<SpokenDialogueSegment> = emptyList(),
) : CinematicEntry {
	val speakerDisplayName: String
		get() = speakerEntry?.displayName ?: ""

	private val speakerEntry: SpeakerEntry?
		get() = Query.findById(speaker)

	override fun create(player: Player): CinematicAction {
		return SpokenDialogueCinematicAction(
			player,
			this,
		)
	}
}

data class SpokenDialogueSegment(
	override val startFrame: Int = 0,
	override val endFrame: Int = 0,
	@Help("The text to display to the player.")
	val text: String = "",
) : Segment

val spokenFormat: String by snippet(
	"cinematic.dialogue.spoken.format",
	"""
		|<gray><st>${" ".repeat(60)}</st>
		|
		|<gray><padding>[ <bold><speaker></bold><reset><gray> ]
		|
		|<message>
		|
		|<gray><st>${" ".repeat(60)}</st>
		""".trimMargin()
)

val spokenPadding: String by snippet("cinematic.dialogue.spoken.padding", "    ")
val spokenMinLines: Int by snippet("cinematic.dialogue.spoken.minLines", 4)
val spokenMaxLineLength: Int by snippet("cinematic.dialogue.spoken.maxLineLength", 40)
val spokenPercentage: Double by snippet("cinematic.dialogue.spoken.percentage", 0.5)

class SpokenDialogueCinematicAction(
	private val player: Player,
	private val entry: SpokenDialogueCinematicEntry,
) : CinematicAction {

	private val speakerName = entry.speakerDisplayName
	private var wasActive = false
	override fun tick(frame: Int) {
		super.tick(frame)
		val segment = (entry.segments activeSegmentAt frame)

		if (segment == null) {
			if (wasActive) {
				player.chatHistory.resendMessages(player)
				wasActive = false
			}
			return
		}

		wasActive = true

		val percentage = segment percentageAt frame

		// The percentage of the dialogue that should be displayed.
		val displayPercentage = percentage / spokenPercentage

		val message = segment.text.parsePlaceholders(player).asPartialFormattedMini(
			displayPercentage,
			padding = spokenPadding,
			minLines = spokenMinLines,
			maxLineLength = spokenMaxLineLength,
		)

		val component = spokenFormat.asMiniWithResolvers(
			Placeholder.parsed("speaker", speakerName),
			Placeholder.component("message", message),
			Placeholder.parsed("padding", spokenPadding),
		)

		val componentWithDarkMessages = player.chatHistory.composeDarkMessage(component)
		player.sendMessage(componentWithDarkMessages)
	}

	override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}

