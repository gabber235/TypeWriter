package me.gabber235.typewriter.extensions.placeholderapi

import lirand.api.extensions.server.server
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.gabber235.typewriter.entry.PlaceholderEntry
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.entry.entries.trackedShowingObjectives
import me.gabber235.typewriter.entry.quest.trackedQuest
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.snippets.snippet
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*


private val noneTracked by snippet("quest.tracked.none", "<gray>None tracked</gray>")

object PlaceholderExpansion : PlaceholderExpansion(), KoinComponent {
    private val plugin: Plugin by inject()
    override fun getIdentifier(): String = "typewriter"

    override fun getAuthor(): String = "gabber235"

    override fun getVersion(): String = plugin.pluginMeta.version

    override fun persist(): Boolean = true

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        // TODO: Replace placeholder
        if (params.startsWith("speaker_")) {
            val speakerName = params.substring(8)
            logger.warning("Speaker specific placeholder is deprecated, move to the generic entry placeholder: %typewriter_${params}% -> %typewriter_${speakerName}%")
            val speaker: SpeakerEntry = Query.findById(speakerName) ?: Query.findByName(speakerName) ?: return null

            return speaker.displayName
        }

        if (params == "tracked_quest") {
            if (player == null) return null
            return player.trackedQuest()?.get()?.display(player) ?: noneTracked
        }

        if (params == "tracked_objectives") {
            if (player == null) return null
            return player.trackedShowingObjectives().joinToString(", ") { it.display(player) }
                .ifBlank { noneTracked }
        }

        val entry: PlaceholderEntry = Query.findById(params) ?: Query.findByName(params) ?: return null
        return entry.display(player)
    }
}

fun String.parsePlaceholders(player: OfflinePlayer?): String {
    return if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
        PlaceholderAPI.setPlaceholders(player, this)
    } else this
}

fun String.parsePlaceholders(player: Player?): String = parsePlaceholders(player as OfflinePlayer?)

fun String.parsePlaceholders(playerId: UUID): String = parsePlaceholders(server.getOfflinePlayer(playerId))

val String.isPlaceholder: Boolean
    get() {
        return if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPI.getPlaceholderPattern().matcher(this).matches()
        } else false
    }