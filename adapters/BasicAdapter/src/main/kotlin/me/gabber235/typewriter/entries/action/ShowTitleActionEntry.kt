package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import java.time.Duration
import java.util.*

@Entry("show_title", "Show a title to a player", Colors.RED, Icons.ALIGN_CENTER)
data class ShowTitleActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	@Help("The title text to show.")
	val title: String = "",
	@Help("The subtitle text to show.")
	val subtitle: String = "",
	@Help("Optional duration settings for the title.")
	val durations: Optional<TitleDurations> = Optional.empty(),
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)

		val adventureTitle: Title = durations.map { durations ->
			Title.title(
				title.parsePlaceholders(player).asMini(),
				subtitle.parsePlaceholders(player).asMini(),

				Title.Times.times(
					Duration.ofMillis(durations.fadeIn.toMillis()),
					Duration.ofMillis(durations.stay.toMillis()),
					Duration.ofMillis(durations.fadeOut.toMillis())
				)
			)
		}.orElseGet {
			Title.title(
				title.parsePlaceholders(player).asMini(),
				subtitle.parsePlaceholders(player).asMini(),
			)
		}

		player.showTitle(adventureTitle)
	}
}

data class TitleDurations(
	@Help("The duration of the fade in effect.")
	val fadeIn: Duration,
	@Help("The duration that it stays.")
	val stay: Duration,
	@Help("The duration of the fade out effect.")
	val fadeOut: Duration
)