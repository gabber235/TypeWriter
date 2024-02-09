package me.gabber235.typewriter.extensions.placeholderapi

import lirand.api.extensions.server.server
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.entry.entries.SoundIdEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object TypewriteExpansion : PlaceholderExpansion(), KoinComponent {
    private val plugin: Plugin by inject()
    override fun getIdentifier(): String = "typewriter"

    override fun getAuthor(): String = "gabber235"

    override fun getVersion(): String = plugin.pluginMeta.version

    override fun persist(): Boolean = true

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (params.startsWith("speaker_")) {
            val speakerName = params.substring(8)
            val speaker: SpeakerEntry = Query.findById(speakerName) ?: Query.findByName(speakerName) ?: return null
            return speaker.displayName
        }

        val entry: Entry = Query.findById(params) ?: Query.findByName(params) ?: return null
        if (entry is ReadableFactEntry) {
            if (player == null) return null
            return entry.readForPlayer(player).value.toString()
        }

        if (entry is SpeakerEntry) {
            return entry.displayName
        }

        if (entry is SoundIdEntry) {
            return entry.soundId
        }

        return null
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