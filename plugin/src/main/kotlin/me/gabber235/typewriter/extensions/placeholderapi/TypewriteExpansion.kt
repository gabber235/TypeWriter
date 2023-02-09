package me.gabber235.typewriter.extensions.placeholderapi

import lirand.api.extensions.server.server
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.facts.FactDatabase
import org.bukkit.entity.Player

object TypewriteExpansion : PlaceholderExpansion() {
	override fun getIdentifier(): String = "typewriter"

	override fun getAuthor(): String = "gabber235"

	override fun getVersion(): String = Typewriter.plugin.description.version

	override fun persist(): Boolean = true

	override fun onPlaceholderRequest(player: Player?, params: String): String? {

		if (params.startsWith("speaker_")) {
			val speakerName = params.substring(8)
			val speaker: SpeakerEntry = Query.findByName(speakerName) ?: Query.findById(speakerName) ?: return null
			return speaker.displayName
		}

		if (player == null) return null
		val fact = FactDatabase.getCachedFact(player.uniqueId, params) ?: return null
		return "${fact.value}"
	}
}

fun String.parsePlaceholders(player: Player?): String {
	return if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
		PlaceholderAPI.setPlaceholders(player, this)
	} else this
}