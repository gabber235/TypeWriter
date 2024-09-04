package com.typewritermc.engine.paper.extensions.placeholderapi

import com.typewritermc.core.entries.Query
import com.typewritermc.engine.paper.entry.PlaceholderEntry
import lirand.api.extensions.server.server
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import com.typewritermc.engine.paper.entry.entries.trackedShowingObjectives
import com.typewritermc.engine.paper.entry.quest.trackedQuest
import com.typewritermc.engine.paper.snippets.snippet
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