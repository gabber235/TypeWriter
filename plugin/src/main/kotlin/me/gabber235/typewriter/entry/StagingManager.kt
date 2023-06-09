package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.entry.StagingState.*
import me.gabber235.typewriter.events.PublishedBookEvent
import me.gabber235.typewriter.events.StagingChangeEvent
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

interface StagingManager {
    val stagingState: StagingState

    fun initialize()
    fun fetchPages(): Map<String, JsonObject>
    fun createPage(data: JsonObject): Result<String, String>
    fun renamePage(oldName: String, newName: String): Result<String, String>
    fun deletePage(name: String): Result<String, String>
    fun createEntry(pageId: String, data: JsonObject): Result<String, String>
    fun updateEntryField(pageId: String, entryId: String, path: String, value: JsonElement): Result<String, String>
    fun updateEntry(pageId: String, data: JsonObject): Result<String, String>
    fun reorderEntry(pageId: String, entryId: String, newIndex: Int): Result<String, String>
    fun deleteEntry(pageId: String, entryId: String): Result<String, String>
    suspend fun publish(): Result<String, String>
    fun shutdown()
}

class StagingManagerImpl : StagingManager, KoinComponent {
    private val gson: Gson by inject()

    private val pages = ConcurrentHashMap<String, JsonObject>()

    private val autoSaver =
        Timeout(
            3.seconds,
            ::saveStaging,
            immediateRunnable = {
                // When called to save, we immediately want to update the staging state
                if (stagingState != PUBLISHING) stagingState = STAGING
            })

    private val stagingDir
        get() = plugin.dataFolder["staging"]

    private val publishedDir
        get() = plugin.dataFolder["pages"]

    override var stagingState = PUBLISHED
        private set(value) {
            field = value
            StagingChangeEvent(value).callEvent()
        }

    override fun initialize() {
        loadState()

        plugin.listen<TypewriterReloadEvent> { loadState() }
    }

    private fun loadState() {
        stagingState = if (stagingDir.exists()) {
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
    }

    private fun fetchPages(dir: File): Map<String, JsonObject> {
        val pages = mutableMapOf<String, JsonObject>()
        dir.pages().forEach { file ->
            val page = file.readText()
            pages[file.nameWithoutExtension] = gson.fromJson(page, JsonObject::class.java)
        }
        return pages
    }

    override fun fetchPages(): Map<String, JsonObject> {
        return pages
    }

    override fun createPage(data: JsonObject): Result<String, String> {
        if (!data.has("name")) return failure("Name is required")
        val nameJson = data["name"]
        if (!nameJson.isJsonPrimitive || !nameJson.asJsonPrimitive.isString) return failure("Name must be a string")
        val name = nameJson.asString

        if (pages.containsKey(name)) return failure("Page with that name already exists")

        // Add the version of this page to track migrations
        data.addProperty("version", plugin.pluginMeta.version)

        pages[name] = data
        autoSaver()
        return ok("Successfully created page with name $name")
    }

    override fun renamePage(oldName: String, newName: String): Result<String, String> {
        val oldPage = pages[oldName] ?: return failure("Page does not exist")
        if (pages.containsKey(newName)) return failure("Page with that name already exists")

        oldPage.addProperty("name", newName)

        pages[newName] = oldPage
        autoSaver()
        return ok("Successfully renamed page from $oldName to $newName")
    }

    override fun deletePage(name: String): Result<String, String> {
        pages.remove(name) ?: return failure("Page does not exist")

        // Delete from the file system
        val file = stagingDir["$name.json"]
        if (file.exists()) {
            file.delete()
        }

        autoSaver()
        return ok("Successfully deleted page with name $name")
    }

    override fun createEntry(pageId: String, data: JsonObject): Result<String, String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"] as? JsonArray ?: JsonArray()

        entries.add(data)
        page.add("entries", entries)

        autoSaver()
        return ok("Successfully created entry with id ${data["id"]}")
    }

    override fun updateEntryField(
        pageId: String,
        entryId: String,
        path: String,
        value: JsonElement
    ): Result<String, String> {
        // Update the page
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"].asJsonArray
        val entry = entries.find { it.asJsonObject["id"].asString == entryId } ?: return failure("Entry does not exist")

        // Update the entry
        val pathParts = path.split(".")
        var current: JsonElement = entry.asJsonObject
        pathParts.forEachIndexed { index, key ->
            if (index == pathParts.size - 1) {
                if (current.isJsonObject) {
                    current.asJsonObject.add(key, value)
                } else if (current.isJsonArray) {
                    current.asJsonArray[Integer.parseInt(key)] = value
                }
            } else if (current.isJsonObject) {
                current = current.asJsonObject[key] ?: JsonObject().also { current.asJsonObject.add(key, it) }
            } else if (current.isJsonArray) {
                current =
                    current.asJsonArray[Integer.parseInt(key)] ?: JsonObject().also { current.asJsonArray.add(it) }
            }
        }

        autoSaver()
        return ok("Successfully updated field")
    }

    override fun updateEntry(pageId: String, data: JsonObject): Result<String, String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"] as? JsonArray ?: return failure("Page does not have any entries")
        val entryId = data["id"]?.asString ?: return failure("Entry does not have an id")

        entries.removeAll { entry -> entry.asJsonObject["id"]?.asString == entryId }
        entries.add(data)

        autoSaver()
        return ok("Successfully updated entry with id ${data["id"]}")
    }

    override fun reorderEntry(pageId: String, entryId: String, newIndex: Int): Result<String, String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"].asJsonArray
        val oldIndex = entries.indexOfFirst { it.asJsonObject["id"].asString == entryId }

        if (oldIndex == -1) return failure("Entry does not exist")
        if (oldIndex == newIndex) return ok("Entry is already at the correct index")

        val correctIndex = if (oldIndex < newIndex) newIndex - 1 else newIndex

        val entryAtNewIndex = entries[correctIndex]
        entries[correctIndex] = entries[oldIndex]
        entries[oldIndex] = entryAtNewIndex

        autoSaver()
        return ok("Successfully reordered entry")
    }

    override fun deleteEntry(pageId: String, entryId: String): Result<String, String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"].asJsonArray
        val entry = entries.find { it.asJsonObject["id"].asString == entryId } ?: return failure("Entry does not exist")

        entries.remove(entry)

        autoSaver()
        return ok("Successfully deleted entry with id $entryId")
    }

    private fun getPage(id: String): Result<JsonObject, String> {
        val page = pages[id] ?: return failure("Page does not exist")
        return ok(page)
    }

    private fun saveStaging() {
        // If we are already publishing, we don't want to save the staging
        if (stagingState == PUBLISHING) return
        val dir = stagingDir

        pages.forEach { (name, page) ->
            val file = dir["$name.json"]
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            file.writeText(page.toString())
        }

        stagingState = STAGING
    }

    // Save the page to the file
    override suspend fun publish(): Result<String, String> {
        if (stagingState != STAGING) return failure("Can only publish when in staging")
        autoSaver.cancel()
        return withContext(Dispatchers.IO) {
            stagingState = PUBLISHING

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
                PublishedBookEvent().callEvent()
                logger.info("Published the staging state")
                stagingState = PUBLISHED
                ok("Successfully published the staging state")
            } catch (e: Exception) {
                e.printStackTrace()
                stagingState = STAGING
                failure("Failed to publish the staging state")
            }
        }
    }

    override fun shutdown() {
        if (stagingState == STAGING) saveStaging()
    }
}

enum class StagingState {
    PUBLISHING,
    STAGING,
    PUBLISHED
}
