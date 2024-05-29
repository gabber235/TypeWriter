package me.gabber235.typewriter.database

import com.google.gson.*
import com.google.gson.stream.JsonReader
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.StagingState.PUBLISHED
import me.gabber235.typewriter.entry.StagingState.STAGING
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.get
import me.gabber235.typewriter.utils.ok
import org.koin.core.component.KoinComponent
import java.io.File
import java.util.*

class FileDatabase : Database(), KoinComponent {

    private val factsPlayersDir
        get() = plugin.dataFolder["players"]

    private val stagingDir
        get() = plugin.dataFolder["staging"]

    private val publishedDir
        get() = plugin.dataFolder["pages"]


    override fun initialize() {
        if (!factsPlayersDir.exists()) {
            factsPlayersDir.mkdirs()
        }
    }

    override fun shutdown() {

    }

    override fun getDatabaseType(): DatabaseType {
        return DatabaseType.FILE
    }

    override suspend fun loadFacts(uuid: UUID): Set<Fact> {
        val file = factsPlayersDir["$uuid.json"]
        if (!file.exists()) {
            return emptySet()
        }
        return factsDataGson.fromJson(file.readText(), Array<Fact>::class.java).toSet()
    }

    override suspend fun storeFacts(uuid: UUID, facts: Set<Fact>) {
        val file = factsPlayersDir["$uuid.json"]
        file.writeText(factsDataGson.toJson(facts))
    }

    override fun storeAsset(path: String, content: String) {
        val file = plugin.dataFolder.resolve("assets/$path")
        file.parentFile.mkdirs()
        file.writeText(content)
    }

    override fun fetchAsset(path: String): Result<String> {
        val file = plugin.dataFolder.resolve("assets/$path")
        if (!file.exists()) {
            return Result.failure(IllegalArgumentException("Asset $path not found."))
        }
        return Result.success(file.readText())
    }

    override fun deleteAsset(path: String) {
        val asset = plugin.dataFolder.resolve("assets/$path")
        val deletedAsset = plugin.dataFolder.resolve("deleted_assets/$path")
        deletedAsset.parentFile.mkdirs()
        asset.renameTo(deletedAsset)
    }

    override fun fetchAllAssetPaths(): Set<String> {
        return plugin.dataFolder.resolve("assets").walk().filter { it.isFile }
            .map { it.relativeTo(plugin.dataFolder.resolve("assets")).path }.toSet()
    }

    override fun loadStagingPages(pages: MutableMap<String, JsonObject>): StagingState {
        val stagingState = if (stagingDir.exists()) {
            // Migrate staging directory to use the new format
            stagingDir.migrateIfNecessary()

            val stagingPages = fetchPages(stagingDir)
            val publishedPages = fetchPages(publishedDir)

            if (stagingPages == publishedPages) PUBLISHED else STAGING
        } else PUBLISHED

        // Read the pages from the file
        val dir =
            if (stagingState == STAGING) stagingDir else publishedDir
        pages.putAll(fetchPages(dir))
        return stagingState
    }

    override fun loadPages(): List<Page> {
        if (!publishedDir.exists()) {
            publishedDir.mkdirs()
        }

        publishedDir.migrateIfNecessary()

        return publishedDir.pages().mapNotNull { file ->
            val id = file.nameWithoutExtension
            val dialogueReader = JsonReader(file.reader())
            dialogueReader.parsePage(id, entryParserGson).getOrNull()
        }
    }

    private fun fetchPages(dir: File): Map<String, JsonObject> {
        val pages = mutableMapOf<String, JsonObject>()
        dir.pages().forEach { file ->
            val page = file.readText()
            val pageName = file.nameWithoutExtension
            val pageJson = bukkitDataGson.fromJson(page, JsonObject::class.java)
            // Sometimes the name of the page is out of sync with the file name, so we need to update it
            pageJson.addProperty("name", pageName)
            pages[pageName] = pageJson
        }
        return pages
    }

    override fun saveStagingPages(pages: MutableMap<String, JsonObject>) {
        pages.forEach { (name, page) ->
            val file = stagingDir["$name.json"]
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            file.writeText(page.toString())
        }

        stagingDir.listFiles()?.filter { it.nameWithoutExtension !in pages.keys }?.forEach { it.delete() }
    }

    override suspend fun publishStagingPages(pages: MutableMap<String, JsonObject>): Result<String> {
        try {
            pages.forEach { (name, page) ->
                val file = publishedDir["$name.json"]
                file.writeText(page.toString())
            }

            val stagingPages = pages.keys
            val publishedFiles = publishedDir.listFiles()?.toList() ?: emptyList()

            val deletedPages = publishedFiles.filter { it.nameWithoutExtension !in stagingPages }
            if (deletedPages.isNotEmpty()) {
                logger.info(
                    "Deleting ${deletedPages.size} pages, as they are no longer in staging. (${
                        deletedPages.joinToString(
                            ", "
                        ) { it.nameWithoutExtension }
                    })"
                )
                deletedPages.backup()
                deletedPages.forEach { it.delete() }
            }

            // Delete the staging folder
            stagingDir.deleteRecursively()
            logger.info("Published the staging state")
            return ok("Successfully published the staging state")
        } catch (e: Exception) {
            e.printStackTrace()
            return failure("Failed to publish the staging state")
        }
    }
}