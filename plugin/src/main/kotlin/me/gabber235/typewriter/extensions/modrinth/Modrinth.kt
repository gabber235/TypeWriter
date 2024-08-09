package me.gabber235.typewriter.extensions.modrinth

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.await
import lirand.api.extensions.events.SimpleListener
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.entry.v
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.gabber235.typewriter.utils.asMini
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.text.DateFormat
import java.util.*
import kotlin.time.Duration.Companion.seconds

object Modrinth {
    private const val VERSIONS_URL = "https://api.modrinth.com/v2/project/typewriter/version"
    private var newVersion: ModrinthVersion? = null
    private val notifiedPlayers = mutableSetOf<UUID>()
    private val listener = SimpleListener()

    suspend fun initialize() {
        plugin.listen<PlayerJoinEvent>(listener) {
            if (!canSendNotification(it.player)) return@listen
            SYNC.launch {
                delay(5.seconds)
                notifyPlayer(it.player)
            }
        }
        loadVersion()
    }

    private suspend fun loadVersion() {
        val request = HttpRequest.newBuilder(URI(VERSIONS_URL))
            .GET()
            .build()

        val response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()

        if (response.statusCode() != 200) return

        val versions = GsonBuilder()
            .setDateFormat(DateFormat.FULL, DateFormat.FULL)
            .create().fromJson<List<ModrinthVersion>>(
                response.body(),
                object : com.google.gson.reflect.TypeToken<List<ModrinthVersion>>() {}.type
            )

        val latestVersion = findLatestVersion(versions) ?: return

        if (latestVersion.versionNumber.v <= plugin.pluginMeta.version.v) return

        newVersion = latestVersion

        plugin.logger.severe(
            """|
            |-----------------{ Typewriter Update }-----------------
            |    A new version of Typewriter is available!
            |    Current version: ${plugin.pluginMeta.version}
            |    New version:     ${latestVersion.versionNumber}
            |    Download it at:  ${latestVersion.url}
            |------------------------------------------------------
        """.trimMargin()
        )

        plugin.server.onlinePlayers.forEach { notifyPlayer(it) }
    }

    private fun findLatestVersion(versions: List<ModrinthVersion>): ModrinthVersion? {
        val currentIsDevelopment = plugin.pluginMeta.version.contains("dev")

        return versions
            .filter {
                currentIsDevelopment || it.versionType == "release"
            }
            .maxByOrNull { it.datePublished }
    }

    private fun canSendNotification(player: Player): Boolean {
        if (newVersion == null) return false
        if (player.uniqueId in notifiedPlayers) return false
        return player.hasPermission("typewriter.update")
    }

    private fun notifyPlayer(player: Player) {
        if (!canSendNotification(player)) return
        val newVersion = newVersion ?: return
        notifiedPlayers.add(player.uniqueId)

        player.sendMessage(
            """
            |<st><gray>             </st><gray>{ <dark_gray><bold>Typewriter Update</bold><gray> }<st>             </st>
            |
            |    A new version of Typewriter is available!
            |    <red>Current version: <reset>${plugin.pluginMeta.version}<reset>
            |    <green>New version:       <reset>${newVersion.versionNumber}<reset>
            |    <blue>Download it:       <reset><bold><click:open_url:${newVersion.url}><hover:show_text:Click to open>[Here]<reset>
            |    
            |<st><gray>                                                      </st>
        """.trimMargin().asMini()
        )
    }
}

data class ModrinthVersion(
    @SerializedName("game_versions") val gameVersions: List<String> = listOf(),
    val loaders: List<String> = listOf(),
    val id: String = "",
    @SerializedName("project_id") val projectId: String = "",
    @SerializedName("author_id") val authorId: String = "",
    val featured: Boolean = false,
    val name: String = "",
    @SerializedName("version_number") val versionNumber: String = "",
    val changelog: String = "",
    @SerializedName("changelog_url") val changelogUrl: String? = "",
    @SerializedName("date_published") val datePublished: Date = Date(0),
    val downloads: Int = 0,
    @SerializedName("version_type") val versionType: String = "",
    val status: String = "",
    @SerializedName("requested_status") val requestedStatus: String? = "",
    val files: List<Files> = listOf(),
    val dependencies: List<Dependency> = listOf()
) {
    val url: String
        get() = "https://modrinth.com/plugin/$projectId/version/$id"
}

data class Files(
    val hashes: Hashes = Hashes(),
    val url: String = "",
    val filename: String = "",
    val primary: Boolean = false,
    val size: Int = 0,
    @SerializedName("file_type") val fileType: String? = "",
)

data class Hashes(
    val sha512: String = "",
    val sha1: String = "",
)

data class Dependency(
    @SerializedName("version_id") val versionId: String? = "",
    @SerializedName("project_id") val projectId: String = "",
    @SerializedName("file_name") val fileName: String? = "",
    @SerializedName("dependency_type") val dependencyType: String = "",
)